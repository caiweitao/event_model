package cwt.event.test;

import cwt.event.base.Event;
import cwt.event.base.EventDispatcher;
import cwt.event.base.EventType;

public class EventMain {

	public static void main(String[] args) {
		//启动服务的时候完成
		new EventListenManager().initEventListen();
		
		//触发事件
		Event event = new LevelUpEvent(EventType.ROLE_LEVEL_UP,10001);
		event.setSync(false);
		EventDispatcher.INSTANCE.fireEvent(event);
		
		try {
			Thread.sleep(1000*15);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		EventDispatcher.INSTANCE.shutdown();
	}
}
