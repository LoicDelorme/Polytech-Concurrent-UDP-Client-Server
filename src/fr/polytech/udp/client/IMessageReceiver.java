package fr.polytech.udp.client;

/**
 * This interface represents a message receiver.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public interface IMessageReceiver
{
	/**
	 * Notify that a message has been sent.
	 * 
	 * @param message
	 *            The sent message.
	 */
	public void notifyMessageHasBeenSent(String message);

	/**
	 * Notify that a message has been received.
	 * 
	 * @param message
	 *            The received message.
	 */
	public void notifyMessageHasBeenReceived(String message);
}