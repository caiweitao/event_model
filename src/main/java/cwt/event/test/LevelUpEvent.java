package cwt.event.test;

import cwt.event.base.Event;
import cwt.event.base.EventType;

public class LevelUpEvent extends Event {
	private int playerId;//玩家id

	public LevelUpEvent(EventType evtType,int playerId) {
		super(evtType);
		this.playerId = playerId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}
