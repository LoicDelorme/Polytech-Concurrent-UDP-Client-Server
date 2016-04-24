package fr.polytech.udp.server;

/**
 * This class represents an UDP server launcher.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class UDPServerLauncher
{
	/**
	 * The default server port.
	 */
	public static final int DEFAULT_SERVER_PORT = 2000;

	/**
	 * The minimal boundary.
	 */
	public static final int MINIMAL_BOUNDARY = 2001;

	/**
	 * The maximal boundary.
	 */
	public static final int MAXIMAL_BOUNDARY = 2002;

	/**
	 * The entry of the application.
	 * 
	 * @param args
	 *            Some arguments.
	 * @throws Exception
	 *             If an error occurred.
	 */
	public static void main(String[] args) throws Exception
	{
		final UDPServer udpServer = new UDPServer(DEFAULT_SERVER_PORT, MINIMAL_BOUNDARY, MAXIMAL_BOUNDARY);
		udpServer.run();
	}
}