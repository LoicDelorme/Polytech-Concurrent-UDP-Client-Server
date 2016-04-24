package fr.polytech.udp.client;

import java.net.InetAddress;

/**
 * This interface represents a message sender.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public interface IMessageSender
{
	/**
	 * Try to connect to the server.
	 * 
	 * @param address
	 *            The server address.
	 * @param port
	 *            The server port.
	 * @throws Exception
	 *             If an error occurred.
	 */
	public void connect(InetAddress address, int port) throws Exception;

	/**
	 * Disconnect to the server.
	 * 
	 * @throws Exception
	 *             If an error occurred.
	 */
	public void disconnect() throws Exception;

	/**
	 * Send a message.
	 * 
	 * @param message
	 *            The message to send.
	 * @throws Exception
	 *             If an error occurred.
	 */
	public void sendMessage(String message) throws Exception;
}