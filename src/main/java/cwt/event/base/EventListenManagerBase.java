package cwt.event.base;

import java.lang.reflect.Field;

/**
 * @author caiweitao
 * @Date 2020年8月8日
 * @Description 监听器管理基类
 * 将要注册事件的监听器，在 EventListenManagerBase 子类属性中，
 * 通过注解 @Evt 标记监听器所感兴趣的事件。
 * 例如：
 * @Evt(eventType=EventType.LOGIN)
 * public LoginListener login;
 */
public abstract class EventListenManagerBase {
	
	/**
	 * 初始化 注册监听器（启动程序时调用）
	 */
	@SuppressWarnings("unchecked")
	public void initEventListen () {
		Field[] fields = getClass().getDeclaredFields();
		for (Field f:fields) {
			Evt evt = f.getAnnotation(Evt.class);
			if (evt != null) {
				EventType eventType = evt.eventType();
				Class<?> listenClass = f.getType();
				EventListener<? extends Event> newInstance;
				try {
					newInstance = (EventListener<? extends Event>)listenClass.newInstance();
					//注册事件
					EventDispatcher.INSTANCE.registerEvent(eventType, newInstance);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
