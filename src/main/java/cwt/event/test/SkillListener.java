package cwt.event.test;

import cwt.event.base.EventListener;

/**
 * @author caiweitao
 * @Date 2020年8月7日
 * @Description 学习技能操作
 */
public class SkillListener implements EventListener<LevelUpEvent> {

	@Override
	public void handleEvent(LevelUpEvent event) {
		System.out.println(event.getEvtType()+"事件，开始学习技能....");

	}

}
