package cwt.event.base;

/**
 * @author caiweitao
 * @Date 2020年8月7日
 * @Description 事件监听接口
 */
public interface EventListener {

	/**
	 * 事件触发后，处理具体逻辑
	 * @param event
	 */
	public void handleEvent(Event event);
}
