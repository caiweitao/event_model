package cwt.event.base;

/**
 * @author caiweitao
 * @Date 2020年8月7日
 * @Description 事件抽象类
 */
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
