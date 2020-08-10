# event_model

我们经常会这样写代码：

    public void handle (Player player) {
    	if (isPass(player)) {// 过关判断
			// 解锁新功能逻辑
			// 获得物品逻辑
			// 任务逻辑
			...
		}
    }

所有代码都集中在一起，耦合度很高，一但想加新逻辑，又要在下面修改插入代码。
为了降低代码的耦合度，我们引入事件驱动模型（观察者模式）。
把玩家“过关”这个动作，包装成一个“过关”事件，一但触发，对这个事件感兴趣（注册了该事件）的监听器就会执行对应的逻辑。



## 监听器接口（EventListener） ##
  
所有监听器实现该接口，逻辑在 handleEvent（）方法中实现。

    public interface EventListener {
    
    	/**
    	 * 事件触发后，处理具体逻辑
    	 * @param event
    	 */
    	public void handleEvent(Event event);
    }

## 事件类（Event） ##

        public class Event {
    	
    	private boolean sync = true;//是否在消息主线程同步执行
    	private final EventType evtType;//事件类型
    	
    	public Event (EventType evtType) {
    		this.evtType = evtType;
    	}
    	
    	public Event (EventType evtType,boolean sync) {
    		this.evtType = evtType;
    		this.sync = sync;
    	}
    
    	public EventType getEvtType() {
    		return evtType;
    	}
    	
    	/**
    	 * 是否在消息主线程同步执行
    	 * @return
    	 */
    	public boolean isSync() {
    		return sync;
    	}
    	
    	public void setSync (boolean sync) {
    		this.sync = sync;
    	}
    }
    

    

## 事件类型枚举类（EventType） ##

	    public enum EventType {
    		LOGIN,//登陆
    		REGISTER,//注册
    		ROLE_LEVEL_UP,//角色升级
    		EXIT;//退出事件
    	}

## 事件分发器（EventDispatcher） ##

	
事件分发器是最核心部分，包括注册事件和派发事件（触发事件），派发事件又分同步和异步执行。

首先是定义了两个成员属性：

	Map<EventType,Set<EventListener>> observers

存放事件类型和监听器的映射关系，哪些监听器，对哪些事件类型感兴趣，一个事件可以有多个监听器感兴趣，所以是一对多的关系。

	LinkedBlockingQueue<Event> eventQueue

异步执行的事件队列，想通过异步执行的事件，先放到该队列，再又另外的线程去执行事件。

而这个异步执行线程，我们在构造方法中启动：

	    EventDispatcher(){
    		// 开启异步执行线程
    		new Thread(new EventWorker()).start();
    	}
	
监听器对什么事件感兴趣，我们在服务启动的时候就要通过事件注册方法进行注册：

	    /**
    	 * 注册事件
    	 * @param evtType 事件类型
    	 * @param listener 具体监听器
    	 */
    	public void registerEvent(EventType evtType, EventListener listener) {
    		Set<EventListener> listeners = observers.get(evtType);
    		if(listeners == null) {
    			listeners = new HashSet<EventListener>();
    			observers.put(evtType, listeners);
    		}
    		listeners.add(listener);
    	}
	
代码很容易理解，就是放进Map。

还有一个重要的方法就是事件派发，在要触发事件的地方调用，比如玩家登陆后要派发登陆事件。

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


这里把 EventDispatcher 定义为枚举类，目的是想通过枚举来实现单例。


## 事件类型注解（Evt） ##
事件类型注解是为了标注监听器对哪些事件感兴趣，在服务启动的时候，通过注解解析，注册事件。
	
例如：

	    /**
     	* 监听器管理类
     	* @author caiweitao
     	* @Date 2020年8月8日
     	* @Description
     	*/
    	public class EventListenManager extends EventListenManagerBase {
    
    		@Evt(eventType=EventType.LOGIN)
    		public LoginListener login;
    	}
    

## 监听器管理器（EventListenManagerBase） ##
监听器管理器跟事件类型注解一起使用，程序中创建自己的管理器继承 **EventListenManagerBase** ，然后调用 **initEventListen ()**方法就可以注册事件。

## 基本用法 ##

###① 在事件类型枚举类（**EventType**）中声明事件。###
	
###② 创建监听器（继承**EventListener**接口）。###
	
###③ 创建监听器管理器（继承**EventListenManagerBase**类），在成员变量中通过Evt注解，声明监听器感兴趣的事件。###
	
###④ 在服务启动时调用**EventListenManagerBase**类中的 initEventListen()**方法注册事件。###
	
###⑤ 在事件触发的地方调用**fireEvent(event)**###