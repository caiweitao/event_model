package cwt.event.test;

import cwt.event.base.EventListener;

/**
 * @author caiweitao
 * @Date 2020年8月7日
 * @Description 属性变化
 */
public class AttrChangeListener implements EventListener<LevelUpEvent> {

	@Override
	public void handleEvent(LevelUpEvent event) {
		System.out.println(event.getEvtType()+"事件，玩家["+event.getPlayerId()+"]处理属性变化操作");

	}

}
