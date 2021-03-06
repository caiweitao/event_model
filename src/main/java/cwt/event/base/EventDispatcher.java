package cwt.event.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author caiweitao
 * @Date 2020年8月7日
 * @Description 事件分发器(由于事件都是预先注册好的，所以这里不考虑多线程问题，不允许在游戏运行过成中动态添加观察者)
 */
public enum EventDispatcher {

	INSTANCE; //采用枚举实现单例模式
	
	private ExecutorService executorService;
	
	// 事件类型与事件监听器列表的映射关系
	private final Map<EventType,List<EventListener<? extends Event>>> observers = new HashMap<>();
	
	// 异步执行的事件队列 
	private LinkedBlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
	
	EventDispatcher(){
		executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new EventWorker());
	}
	
	/**
	 * 注册事件
	 * @param evtType 事件类型
	 * @param listener 具体监听器
	 */
	public void registerEvent(EventType evtType, EventListener<? extends Event> listener) {
		List<EventListener<? extends Event>> listeners = observers.get(evtType);
		if(listeners == null) {
			listeners = new ArrayList<EventListener<? extends Event>>();
			observers.put(evtType, listeners);
		}
		listeners.add(listener);
	}
	
	/**
	 * 派发事件
	 * @param event
	 */
	public void fireEvent(Event event) {
		if(event == null) {
			throw new NullPointerException("event cannot be null");
		}
		
		//如果事件是同步的，那么就在消息主线程执行逻辑
		if (event.isSync()) {
			handler(event);
		} else {
			//否则，就丢到事件线程异步执行
			eventQueue.add(event);
		}
		
	}
	
	/**
	 * 同步处理器
	 * @param event
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handler (Event event) {
		EventType evtType = event.getEvtType();
		List<EventListener<? extends Event>> listeners = observers.get(evtType);
		if(listeners != null) {
			for(EventListener listener:listeners) {
				try{
					listener.handleEvent(event);
				} catch(Exception e) {
					e.printStackTrace();  //防止其中一个listener报异常而中断其他逻辑
				}
			}
		}
	}
	
	public void shutdown () {
		eventQueue.add(new Event(EventType.EXIT, false));
		System.out.println("退出异步执行线程......");
	}

	/**
	 * @author caiweitao
	 * @Date 2020年8月7日
	 * @Description 异步执行线程
	 */
	private class EventWorker implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Event event = eventQueue.take();
					if (event.getEvtType() == EventType.EXIT) {
						break;
					}
					handler(event);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
