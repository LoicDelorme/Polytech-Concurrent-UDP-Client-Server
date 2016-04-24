package fr.polytech.udp.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class represents an UDP server.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class UDPServer
{
	/**
	 * The default packet size.
	 */
	public static final int DEFAULT_PACKET_SIZE = 1024;

	/**
	 * The server socket.
	 */
	private final DatagramSocket serverSocket;

	/**
	 * The range ports.
	 */
	private final BlockingQueue<Integer> rangePorts;

	/**
	 * Create an UDP server.
	 * 
	 * @param port
	 *            The port on which the server will listen.
	 * @param minimalBoundary
	 *            The minimal boundary.
	 * @param maximalBoundary
	 *            The maximal boundary.
	 * @throws SocketException
	 *             If an error occurred.
	 */
	public UDPServer(int port, int minimalBoundary, int maximalBoundary) throws SocketException
	{
		this.rangePorts = new ArrayBlockingQueue<Integer>(maximalBoundary - minimalBoundary);
		initializeRangePorts(minimalBoundary, maximalBoundary);

		this.serverSocket = new DatagramSocket(port);
	}

	/**
	 * Initialize the range ports.
	 * 
	 * @param minimalBoundary
	 *            The minimal boundary.
	 * @param maximalBoundary
	 *            The maximal boundary.
	 */
	private void initializeRangePorts(int minimalBoundary, int maximalBoundary)
	{
		for (int offset = minimalBoundary; offset < maximalBoundary; offset++)
		{
			this.rangePorts.add(offset);
		}
	}

	/**
	 * Get an available port.
	 * 
	 * @return The available port.
	 * @throws InterruptedException
	 *             If an error occurred.
	 */
	public Integer getAvailablePort() throws InterruptedException
	{
		return this.rangePorts.take();
	}

	/**
	 * Give back a port.
	 * 
	 * @param port
	 *            The port.
	 */
	public void giveBackPort(int port)
	{
		this.rangePorts.add(port);
	}

	/**
	 * Run the server.
	 * 
	 * @throws Exception
	 *             If an error occurred.
	 */
	public void run() throws Exception
	{
		DatagramPacket receivePacket = null;
		while (true)
		{
			receivePacket = new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
			this.serverSocket.receive(receivePacket);

			final PortWorker portWorker = new PortWorker(receivePacket, this);
			portWorker.start();
		}
	}
}