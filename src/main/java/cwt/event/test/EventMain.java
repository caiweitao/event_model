package cwt.event.test;

import cwt.event.base.Event;
import cwt.event.base.EventDispatcher;
import cwt.event.base.EventListener;
import cwt.event.base.EventType;

public class EventMain {

	public static void main(String[] args) {
		//启动服务的时候完成
		EventListener attr = new AttrChangeListener();
		EventListener skill = new SkillListener();
		EventDispatcher.INSTANCE.registerEvent(EventType.ROLE_LEVEL_UP, attr);
		EventDispatcher.INSTANCE.registerEvent(EventType.ROLE_LEVEL_UP, skill);
		
		//在事件发生的地方完成
		Event event = new LevelUpEvent(EventType.ROLE_LEVEL_UP,10001);
		event.setSync(false);
		EventDispatcher.INSTANCE.fireEvent(event);
	}
}
