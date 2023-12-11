/*

Author: Juan Manqueros
UIN: 651226746
UIC Spring 2023


*/

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;


public class JavaFXTemplate extends Application {
	Server server;
	ListView<String> listItems;
	HashMap<String, Scene> sceneMap;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Welcome to Three Card Poker");
		// create our HashMap of scenes
		sceneMap = new HashMap<>();
		//Different scenes created and put in hashmap scenemap
		// our first scene is the home scene
		sceneMap.put("home", createServerGui(primaryStage));
		primaryStage.setScene(sceneMap.get("home"));
		primaryStage.show();

	}


	public Scene createServerGui(Stage pStage) {
		BorderPane welcomePane = new BorderPane();

		// creating new gamedata object
		GameInfo gameInfo = new GameInfo();

		// creating our port label and text
		Label portLabel = new Label ("Enter Port Number");
		portLabel.setTextAlignment(TextAlignment.CENTER); // centering text inside label
		TextField portInput = new TextField();
		portInput.setMaxSize(200, 150);


		// creating on and off buttons
		Button connectButton = new Button("Connect");

		// align our buttons vertically in a VBox
		// spacing is 20, and aligned in the center
		VBox portVBox = new VBox();
		portVBox.getChildren().addAll(portLabel, portInput, connectButton);
		portVBox.setAlignment(Pos.CENTER);
		portVBox.setSpacing(5);

		// event handler for our connect button that takes player to the game scene
		// connect button also lets the server know that a client is entering the game
		listItems = new ListView<>();
		connectButton.setOnAction(e -> {
			try {
				gameInfo.port = Integer.parseInt(portInput.getText()); // adding port number into object
				sceneMap.put("gameData", createResultsGui(pStage, server));
				pStage.setScene(sceneMap.get("gameData"));

				server = new Server(gameInfo, data -> {
					Platform.runLater(() -> {

					});
				});

			} catch (NumberFormatException nex) {
				System.err.println("Please enter a port number");
			}
		});

		// set buttons at the bottom of welcome scene
		welcomePane.setCenter(portVBox);


		return new Scene(welcomePane, 400, 400);
	}

	public Scene createResultsGui(Stage pstage, Server server){
		BorderPane welcomePane = new BorderPane();
		welcomePane.setPadding(new Insets(70));
		welcomePane.setStyle("-fx-background-color: coral");

		VBox serverVBox = new VBox();
		serverVBox.getChildren().add(listItems);
		serverVBox.setAlignment(Pos.CENTER);

		Button stopServerButton = new Button("Stop Server");
		serverVBox.getChildren().add(stopServerButton);

		stopServerButton.setOnAction(e -> {
			stopServer();
		});

		welcomePane.setCenter(serverVBox);



		return new Scene(welcomePane, 500, 500);
	}

	public void stopServer(){
		try {
			server.stopServer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

