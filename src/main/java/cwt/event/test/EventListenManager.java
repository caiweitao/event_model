package cwt.event.test;

import cwt.event.base.EventListenManagerBase;
import cwt.event.base.EventType;
import cwt.event.base.Evt;

/**
 * 监听器管理类
 * @author caiweitao
 * @Date 2020年8月8日
 * @Description
 */
public class EventListenManager extends EventListenManagerBase {

	@Evt(eventType=EventType.ROLE_LEVEL_UP)
	public AttrChangeListener attr;
//	@Evt(eventType=EventType.ROLE_LEVEL_UP)
//	public SkillListener skill;
	
}
