package fr.polytech.udp.client;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class represents a tchat client controller.
 *
 * @author DELORME Loïc
 * @since 1.0.0
 */
public class TchatClientController implements Initializable, IMessageReceiver
{
	/**
	 * The connected button.
	 */
	@FXML
	private Button connectedButton;

	/**
	 * The disconnected button.
	 */
	@FXML
	private Button disconnectedButton;

	/**
	 * The send button.
	 */
	@FXML
	private Button sendButton;

	/**
	 * The IP address text field.
	 */
	@FXML
	private TextField ipAddress;

	/**
	 * The port text field.
	 */
	@FXML
	private TextField port;

	/**
	 * The message text area.
	 */
	@FXML
	private TextArea message;

	/**
	 * The scroll pane.
	 */
	@FXML
	private ScrollPane scrollPane;

	/**
	 * The messages.
	 */
	@FXML
	private VBox messages;

	/**
	 * The message sender.
	 */
	private IMessageSender messageSender;

	/**
	 * The is connected statue.
	 */
	private final BooleanProperty isConnected;

	/**
	 * Create a tchat client controller.
	 */
	public TchatClientController()
	{
		this.isConnected = new SimpleBooleanProperty(false);
	}

	/**
	 * Set the message sender.
	 * 
	 * @param messageSender
	 *            The message sender.
	 */
	public void setMessageSender(IMessageSender messageSender)
	{
		this.messageSender = messageSender;
	}

	/**
	 * Set the stage.
	 * 
	 * @param primaryStage
	 *            The primary stage.
	 */
	public void setStage(Stage primaryStage)
	{
		primaryStage.setOnCloseRequest(e ->
		{
			try
			{
				if (this.isConnected.get())
				{
					this.messageSender.disconnect();
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});
	}

	/**
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		this.connectedButton.disableProperty().bind(this.ipAddress.textProperty().isEmpty().or(this.port.textProperty().isEmpty()).or(this.isConnected));
		this.disconnectedButton.disableProperty().bind(this.isConnected.not());
		this.sendButton.disableProperty().bind(this.message.textProperty().isEmpty().or(this.isConnected.not()));

		this.connectedButton.setOnAction(e ->
		{
			try
			{
				this.messageSender.connect(InetAddress.getByName(this.ipAddress.getText()), Integer.parseInt(this.port.getText()));
				this.isConnected.set(true);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});

		this.disconnectedButton.setOnAction(e ->
		{
			try
			{
				this.messageSender.disconnect();
				this.isConnected.set(false);

				this.message.clear();
				this.messages.getChildren().clear();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});

		this.sendButton.setOnAction(e ->
		{
			try
			{
				final String message = this.message.getText();
				if (!message.equals("/quit"))
				{
					this.messageSender.sendMessage(message);
					this.message.clear();
				}
				else
				{
					this.disconnectedButton.fire();
				}
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		});
	}

	/**
	 * @see fr.polytech.udp.client.IMessageReceiver#notifyMessageHasBeenSent(java.lang.String)
	 */
	@Override
	public void notifyMessageHasBeenSent(String message)
	{
		addMessageToWorkFlow(message, Pos.TOP_RIGHT);
	}

	/**
	 * @see fr.polytech.udp.client.IMessageReceiver#notifyMessageHasBeenReceived(java.lang.String)
	 */
	@Override
	public void notifyMessageHasBeenReceived(String message)
	{
		addMessageToWorkFlow(message, Pos.TOP_LEFT);
	}

	/**
	 * Add the message to the work flow.
	 * 
	 * @param message
	 *            The message.
	 * @param position
	 *            The position of the message.
	 */
	private void addMessageToWorkFlow(String message, Pos position)
	{
		final HBox builtMessageBox = new HBox(new Label(message));
		builtMessageBox.setPadding(new Insets(0, 10, 0, 10));
		builtMessageBox.setAlignment(position);

		this.messages.getChildren().add(builtMessageBox);
	}
}