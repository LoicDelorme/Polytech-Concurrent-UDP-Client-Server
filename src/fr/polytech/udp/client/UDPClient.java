package fr.polytech.udp.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import fr.polytech.udp.server.UDPServer;

/**
 * This class represents an UDP client.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class UDPClient implements IMessageSender
{
	/**
	 * The socket.
	 */
	private DatagramSocket socket;

	/**
	 * The message receiver.
	 */
	private final IMessageReceiver messageReceiver;

	/**
	 * The server address.
	 */
	private InetAddress serverAddress;

	/**
	 * The server port.
	 */
	private int serverPort;

	/**
	 * Create an UDP client.
	 * 
	 * @param messageReceiver
	 *            The message receiver.
	 */
	public UDPClient(IMessageReceiver messageReceiver)
	{
		this.messageReceiver = messageReceiver;
	}

	/**
	 * @see fr.polytech.udp.client.IMessageSender#connect(java.net.InetAddress, int)
	 */
	@Override
	public void connect(InetAddress address, int port) throws Exception
	{
		this.socket = new DatagramSocket();
		this.serverAddress = address;
		this.serverPort = port;
	}

	/**
	 * @see fr.polytech.udp.client.IMessageSender#disconnect()
	 */
	@Override
	public void disconnect() throws Exception
	{
		sendMessage("/quit");
		this.socket.close();
		this.socket = null;
	}

	/**
	 * @see fr.polytech.udp.client.IMessageSender#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String message) throws Exception
	{
		final String formattedMessage = message + "\0";
		final DatagramPacket sendPacket = new DatagramPacket(formattedMessage.getBytes(), formattedMessage.getBytes().length, this.serverAddress, this.serverPort);
		this.socket.send(sendPacket);
		this.messageReceiver.notifyMessageHasBeenSent(String.format("moi@%s => %s", InetAddress.getLocalHost().getHostAddress(), formattedMessage));

		if (!message.equals("/quit"))
		{
			final DatagramPacket receivePacket = new DatagramPacket(new byte[UDPServer.DEFAULT_PACKET_SIZE], UDPServer.DEFAULT_PACKET_SIZE);
			this.socket.receive(receivePacket);
			final String receivedMessage = new String(receivePacket.getData()).split("\0")[0];
			this.serverAddress = receivePacket.getAddress();
			this.serverPort = receivePacket.getPort();
			this.messageReceiver.notifyMessageHasBeenReceived(String.format("server@%s:%d => %s", this.serverAddress.getHostAddress(), this.serverPort, receivedMessage));
		}
	}
}