package eu.jpereira.trainings.designpatterns.structural.decorator.channel;

/**
 * New channel added without modifying existing code (Open/Closed Principle).
 */
public class GooglePlusChannel implements SocialChannel {

	public static final String NAME = "googleplus";

	private String lastMessage;

	@Override
	public void deliverMessage(String message) {
		this.lastMessage = message;
		System.out.println("Google+: " + message);
	}

	public String getLastMessage() {
		return lastMessage;
	}
}
