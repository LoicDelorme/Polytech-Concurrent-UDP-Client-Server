package fr.polytech.udp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

/**
 * This class represents a data worker.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class DataWorker extends Thread
{
	/**
	 * The socket.
	 */
	private final DatagramSocket socket;

	/**
	 * The receive packet.
	 */
	private DatagramPacket receivePacket;

	/**
	 * The UDP server.
	 */
	private final UDPServer server;

	/**
	 * The client address.
	 */
	private final InetAddress clientAddress;

	/**
	 * The client port.
	 */
	private final int clientPort;

	/**
	 * Create a data worker.
	 * 
	 * @param receivePacket
	 *            The receive packet.
	 * @param port
	 *            The port.
	 * @param server
	 *            The UDP server.
	 * @throws IOException
	 *             If an error occurred.
	 */
	public DataWorker(DatagramPacket receivePacket, int port, UDPServer server) throws IOException
	{
		super(String.format("DataWorker(%s)", UUID.randomUUID().toString()));
		this.socket = new DatagramSocket(port);
		this.receivePacket = receivePacket;
		this.server = server;
		this.clientAddress = receivePacket.getAddress();
		this.clientPort = receivePacket.getPort();
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				processData();
				if (this.socket.isClosed())
				{
					break;
				}

				this.receivePacket = new DatagramPacket(new byte[UDPServer.DEFAULT_PACKET_SIZE], UDPServer.DEFAULT_PACKET_SIZE);
				this.socket.receive(this.receivePacket);
			}
			catch (IOException e)
			{
				System.err.println(Thread.currentThread().getName() + ": " + e.getMessage());
			}
		}

		this.interrupt();
	}

	/**
	 * Process data.
	 * 
	 * @throws IOException
	 *             If an error occurred.
	 */
	private void processData() throws IOException
	{
		final byte[] data = this.receivePacket.getData();
		final String message = new String(data).split("\0")[0];

		switch (message)
		{
			case "/quit":
				final int port = this.socket.getLocalPort();
				this.socket.close();
				this.server.giveBackPort(port);
				break;
			default:
				System.out.println(String.format("%s: client@%s:%d => %s", Thread.currentThread().getName(), this.clientAddress.getHostAddress(), this.clientPort, message));
				final byte[] response = String.format("ACK(%s)\0", message).getBytes();
				final DatagramPacket sendPacket = new DatagramPacket(response, response.length, this.clientAddress, this.clientPort);
				this.socket.send(sendPacket);
				break;
		}
	}
}