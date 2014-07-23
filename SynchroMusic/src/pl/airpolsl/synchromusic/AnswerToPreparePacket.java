package pl.airpolsl.synchromusic;

public abstract class AnswerToPreparePacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1360963942945611225L;

	@Override
	abstract void setRawData();
	
	protected int id;

}
  