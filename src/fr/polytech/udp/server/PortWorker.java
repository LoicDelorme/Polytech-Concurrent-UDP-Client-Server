package fr.polytech.udp.server;

import java.net.DatagramPacket;
import java.util.UUID;

/**
 * This class represents a port worker.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class PortWorker extends Thread
{
	/**
	 * The receive packet.
	 */
	private final DatagramPacket receivePacket;

	/**
	 * The UDP server.
	 */
	private final UDPServer server;

	/**
	 * Create a port worker.
	 * 
	 * @param receivePacket
	 *            The receive packet.
	 * @param server
	 *            The UDP server.
	 */
	public PortWorker(DatagramPacket receivePacket, UDPServer server)
	{
		super(String.format("PortWorker(%s)", UUID.randomUUID().toString()));
		this.receivePacket = receivePacket;
		this.server = server;
	}

	@Override
	public void run()
	{
		try
		{
			final Integer availablePort = this.server.getAvailablePort(); // blocking
			final DataWorker dataWorker = new DataWorker(this.receivePacket, availablePort, this.server);
			dataWorker.start();
		}
		catch (Exception e)
		{
			System.err.println(Thread.currentThread().getName() + ": " + e.getMessage());
		}
	}
}