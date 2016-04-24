package fr.polytech.udp.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This class represents an UDP client launcher.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class UDPClientLauncher extends Application
{
	/**
	 * The entry of the application.
	 * 
	 * @param args
	 *            Some arguments.
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("TchatClient.fxml"));
		final BorderPane root = fxmlLoader.load();

		final TchatClientController controller = fxmlLoader.getController();
		final UDPClient udpClient = new UDPClient(controller);

		controller.setMessageSender(udpClient);
		controller.setStage(primaryStage);

		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}