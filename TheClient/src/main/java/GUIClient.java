import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
/*
    Coder: Cynthia Sanchez Vazquez
    Project 3: Three Card Poker

	This the GUI that the player see's when starting the three card poker game.
	There are three different scenes: welcome, game, and result scenes.
	The welcome scene allows the player to choose which port and ipaddress to use to connect to the server
	Only when there is a connection between the server and the client will the game scene load
	During the game scene the player is guided on how to play based on which buttons are enabled/disabled and text prompts
	There is also a menu bar that allows the player to start a new game, choose a different look for the game scene, or exit
	The result scene will only show once the server lets the client program whether the player lost or won
	Different result scenes will load based whether the player won or lost
	In the result scene the player can choose to exit or play again
 */
public class GUIClient extends Application {

	GameInfo starterInfo = new GameInfo();

	BorderPane gamePane; // placed borderpane here to help transition from face down to face up cards
	HBox dealerCardHBox, playerCardHBox,buttonHBox, playButtonsAndMoneyBox, bankAlignment, anteHBox, wagerHBox, pairPlusHBox ; // holds the different buttons for the game scene and helps with layout of them
	VBox playVBox, moneyBox; // holds buttons for playing, folding, or dealing
	Button dealButton, anteButton, wagerButton, pairPlusButton; // placed here to help when creating a new scene with cards facing up

	Label bank, bankMoney;
	Image gameCardDisplay, gameCardDisplay2, gameCardDisplay3, gameCardDisplay4; // hold card images to display
	ImageView card1, card2, card3, card4, card5, card6;
	boolean playerWon = true; // used to initiate different styles in the result scene
	HashMap<String, Scene> sceneMap; // hashmap that will store different scenes for game
	ListView<String> gameUpdates; // listview will store game updates to show to player

	ArrayList<Card> currentPlayerCards, currentDealerCards; // used to store cards received from server to send back

	Client clientConnection; // client object that will allow this class to use its methods

	Text moneyDisplay, totalWinningsDisplay; // text used to show data in results scenes

	TextField anteTextField, wagerTextField, pairPlusTextField; // placed out here to change betting money available during game

	boolean altLook = false;  // boolean to signal whether user wants a new look

	int moneyTotal = 1000; // initial money player has to bet



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
		sceneMap.put("home", welcomeScene(primaryStage));
		primaryStage.setScene(sceneMap.get("home"));
		primaryStage.show();
		
	}

	// this method returns our home screen scene
	public Scene welcomeScene(Stage pStage) {

		// borderpane for our initial welcome screen
		BorderPane welcomePane = new BorderPane();

		// creating gamedata object
		GameInfo pokerInfo = new GameInfo();

		// creating image for the background
		Image homeBackGround = new Image( "3cardPoker.png");
		BackgroundImage bGround = new BackgroundImage (homeBackGround,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background hbg = new Background(bGround);
		welcomePane.setBackground(hbg);

		// creating our port label and text
		Label portLabel = new Label ("Enter Port Number");
		portLabel.setTextAlignment(TextAlignment.CENTER); // centering text inside label
		portLabel.setStyle("-fx-text-fill: salmon;"+ "-fx-font-weight: bold;" + "-fx-font-size: 18;");
		TextField portInput = new TextField();
		portInput.setMaxSize(200, 150);


		// creating our ip address label and text
		Label ipAddressLabel = new Label("Enter IP Address");
		ipAddressLabel.setTextAlignment(TextAlignment.CENTER); // centering text inside label
		ipAddressLabel.setStyle("-fx-text-fill: salmon;"+ "-fx-font-weight: bold;" + "-fx-font-size: 18;");
		TextField ipAddressInput = new TextField();
		ipAddressInput.setMaxSize(200, 150);
		pokerInfo.ipAddress = ipAddressInput.getText(); // adding ip address info to gamedata class

		// creating connect button
		Button connectButton = new Button("Connect");
		connectButton.setStyle("-fx-text-fill: salmon;"+ "-fx-font-weight: bold;" + "-fx-font-size: 18;");

		// align our buttons vertically in a VBox
		// spacing is 20, and aligned in the center
		VBox buttons = new VBox(5, portLabel, portInput, ipAddressLabel, ipAddressInput, connectButton);
		buttons.setPadding(new Insets(0, 0, 20, 0));
		buttons.setAlignment(Pos.BOTTOM_CENTER);



		// event handler for our connect button that takes player to the game scene
		// connect button also lets the server know that a client is entering the game
		gameUpdates = new ListView<>();
		moneyDisplay = new Text();
		totalWinningsDisplay = new Text();
		connectButton.setOnAction(e -> {
			try {
				String text = portInput.getText();
				int pVal = Integer.parseInt(text);
				pokerInfo.port = pVal; // adding port info to GameInfo class

				clientConnection = new Client(pokerInfo, data -> {  //Where client receives data from server
					Platform.runLater(() -> {
						starterInfo = (GameInfo) data;

						// method that takes in server information
						protocolAnalysis(starterInfo, pStage);

					});
				});

				clientConnection.start(); //initiates run


			} catch (NumberFormatException ex) {
				System.err.println("Please enter the port number");
			}
		});

		// set buttons at the bottom of welcome scene
		welcomePane.setBottom(buttons);


		return new Scene(welcomePane, 900, 700);
	}

	// game scene will provide the player different playing options throughout the game and will show game updates
	public Scene gameScene(Stage pStage) {
		// borderpane for our initial home screen
		gamePane = new BorderPane();

		// arraylist to hold buttons to help with create new look
		ArrayList<Button> buttonHolder = new ArrayList<>();

		// set the game background
		Image mainBackGround = new Image("pokerTable.png");
		BackgroundImage bGround = new BackgroundImage(mainBackGround,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background hbg = new Background(bGround);
		gamePane.setBackground(hbg);

		//creating a menubar and place it on top
		MenuBar gameMenu = menuBarFunction(pStage);
		gameMenu.setStyle("-fx-background-color:slategrey;"+"-fx-font-weight:bold;"+"-fx-font-size:18;");
		gamePane.setTop(gameMenu);


		// money related buttons
		anteButton = new Button("Confirm Ante");
		anteButton.setPrefWidth(115);
		buttonHolder.add(anteButton);
		wagerButton = new Button("Confirm Wager");
		wagerButton.setPrefWidth(115);
		wagerButton.setDisable(true);
		buttonHolder.add(wagerButton);
		pairPlusButton = new Button("Confirm Pair Plus");
		pairPlusButton.setPrefWidth(115);
		buttonHolder.add(pairPlusButton);

		// label to display bank information
		bank = new Label("Betting Money: ");
		bank.setStyle("-fx-font-weight:bold;" + "-fx-text-fill: red;" + "-fx-font-size: 20;");
		bankMoney = new Label();
		bankMoney.setText(Integer.toString(moneyTotal));
		bankMoney.setStyle("-fx-font-weight:bold;" + "-fx-text-fill: red;" + "-fx-font-size: 20;");

		// aligning bank and bankMoney next to each other
		bankAlignment = new HBox(3, bank, bankMoney);


		// text field to input ante.
		anteTextField = new TextField();
		anteTextField.setPrefWidth(100);
		anteTextField.setPromptText("$5-$25"); // player has to bet between 5-25 dollars
		// text field to input wager.
		wagerTextField = new TextField();
		wagerTextField.setPrefWidth(100);
		wagerTextField.setPromptText("0");
		wagerTextField.setDisable(true);
		// text field to input pair plus.
		pairPlusTextField = new TextField();
		pairPlusTextField.setPrefWidth(100);
		pairPlusTextField.setPromptText("0");


		// aligning money related buttons and textfields
		anteHBox = new HBox(10, anteTextField, anteButton);
		wagerHBox = new HBox(10, wagerTextField, wagerButton);
		pairPlusHBox = new HBox(10, pairPlusTextField, pairPlusButton);

		moneyBox = new VBox(10, bankAlignment, anteHBox, wagerHBox, pairPlusHBox);
		moneyBox.setPadding(new Insets(0, 20, 20, 0));
		moneyBox.setAlignment(Pos.BOTTOM_RIGHT);


		// buttons to start or end game
		Button playButton = new Button("Play");
		playButton.setDisable(true);
		buttonHolder.add(playButton);
		playButton.setStyle("-fx-background-color:red;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
		Button foldButton = new Button("Fold");
		foldButton.setStyle("-fx-background-color:red;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
		foldButton.setDisable(true);
		buttonHolder.add(foldButton);
		dealButton = new Button("Deal Cards");
		dealButton.setPrefWidth(97);
		dealButton.setStyle("-fx-background-color:red;"+"-fx-font-weight:bold;"+"-fx-text-fill:white;");
		dealButton.setDisable(true);
		buttonHolder.add(dealButton);

		// creating new-look if chose by player
		if (altLook){
			for(int i = 0; i < buttonHolder.size(); i++){
				buttonHolder.get(i).setStyle("-fx-background-color:lightblue;"+"-fx-font-weight:bold;"+"-fx-text-fill:black;");
			}
		}

		// event handlers for enabling/disabling money buttons and textfields
		anteButton.setOnAction(e -> {
			// other buttons will on enable if initial bet is over 0
			if((Integer.valueOf(anteTextField.getText()) >= 5) && (Integer.valueOf(anteTextField.getText()) <= 25) ) {
				anteButton.setDisable(true);
				anteTextField.setDisable(true);
				dealButton.setDisable(false);
				wagerTextField.setPromptText(anteTextField.getText()); // play wager has to be the same as ante wager
			}
		});

		wagerButton.setOnAction(e -> {
			// play wager has to be the same as ante wager
			if (Integer.valueOf(wagerTextField.getText()) == Integer.valueOf(anteTextField.getText())) {
				wagerButton.setDisable(true);
				wagerTextField.setDisable(true);
				playButton.setDisable(false);
			}

		});

		pairPlusButton.setOnAction(e -> {
			if ((Integer.valueOf(pairPlusTextField.getText()) >= 5) && (Integer.valueOf(pairPlusTextField.getText()) <= 25)){
				pairPlusButton.setDisable(true);
				pairPlusTextField.setDisable(true);
			}
		});

		// creating card buttons that show the back of the dealer's cards
		// back of the card image depends on which look the player chooses
		if(altLook){
			gameCardDisplay = new Image("backofCards2.png");
		}
		else {
			gameCardDisplay = new Image("backofCards.png");
		}

		// preparing to display dealer's cards face down
		card1 = new ImageView(gameCardDisplay);
		card1.setFitHeight(120);
		card1.setFitWidth(90);
		card2 = new ImageView(gameCardDisplay);
		card2.setFitHeight(120);
		card2.setFitWidth(90);
		card3 = new ImageView(gameCardDisplay);
		card3.setFitHeight(120);
		card3.setFitWidth(90);

		// aligning dealers cards in the center of game
		dealerCardHBox = new HBox(10, card1, card2, card3);
		dealerCardHBox.setAlignment(Pos.CENTER);
		gamePane.setCenter(dealerCardHBox);


		// preparing to display player's cards face down
		card4 = new ImageView(gameCardDisplay);
		card4.setFitHeight(120);
		card4.setFitWidth(90);
		card5 = new ImageView(gameCardDisplay);
		card5.setFitHeight(120);
		card5.setFitWidth(90);
		card6 = new ImageView(gameCardDisplay);
		card6.setFitHeight(120);
		card6.setFitWidth(90);

		playerCardHBox = new HBox(10, card4, card5, card6);


		// listview for player updates
		gameUpdates.setMaxSize(180,180);
		gameUpdates.setStyle("-fx-background-color: black");
		VBox updatesBox = new VBox();
		updatesBox.getChildren().add(gameUpdates);
		updatesBox.setAlignment(Pos.BOTTOM_LEFT);

		// aligning buttons on the game scene
		buttonHBox = new HBox(10, playButton, foldButton);
		buttonHBox.setAlignment(Pos.CENTER);

		playVBox = new VBox(10, playerCardHBox, buttonHBox, dealButton);
		playVBox.setPadding(new Insets(0, 0, 15, 0));
		playVBox.setAlignment(Pos.CENTER);

		playButtonsAndMoneyBox = new HBox(160, playVBox, moneyBox);
		playButtonsAndMoneyBox.setAlignment(Pos.BOTTOM_RIGHT);


		gamePane.setBottom(playButtonsAndMoneyBox);
		gamePane.setTop(gameMenu);
		gamePane.setLeft(updatesBox);

		// letting the server know that the player is ready to view their cards
		dealButton.setOnAction(e -> {
			GameInfo temp = new GameInfo();
			temp.bank = moneyTotal;
			temp.anteWager = Integer.parseInt(anteTextField.getText());
			if(!pairPlusTextField.getText().isEmpty()) {
				temp.pairPlus = Integer.parseInt(pairPlusTextField.getText());
			}
			temp.code = "DEAL";
			clientConnection.send(temp);

			dealButton.setDisable(true);
			pairPlusButton.setDisable(true);
			pairPlusTextField.setDisable(true);
			wagerButton.setDisable(false);
			wagerTextField.setDisable(false);
			foldButton.setDisable(false);

		});

		// letting the server know that the player is ready to place their bet and view the dealer's cards
		playButton.setOnAction(e->{
			playButton.setDisable(true);
			foldButton.setDisable(true);

			// sending server player information
			GameInfo temp = new GameInfo();
			if (!pairPlusTextField.getText().isEmpty()){
				temp.pairPlus = Integer.valueOf(pairPlusTextField.getText());
			}
			temp.playWager = Integer.parseInt(wagerTextField.getText());
			temp.bank = moneyTotal;
			temp.playerCards = currentPlayerCards;
			temp.dealerCards = currentDealerCards;
			temp.code = "PLAY";
			clientConnection.send(temp);
		});

		// resets the game but the player loses their antewager, wager, and pairplus money
		foldButton.setOnAction(e->{
			GameInfo temp = new GameInfo();
			if(!wagerTextField.getText().isEmpty()) {
				moneyTotal -= Integer.valueOf(wagerTextField.getText());
			}
			clearVariables(temp, moneyTotal, "FOLD");
			clientConnection.send(temp);
			sceneMap.put("game", gameScene(pStage));
			pStage.setScene(sceneMap.get("game"));

		});


		return new Scene(gamePane, 900, 700);
	}

	// scene will display the results of the game and allow the user to play again or quit
	public Scene resultScene(Stage pStage) {
		// borderpane for our initial home screen
		BorderPane resultPane = new BorderPane();

		//Creating image for the background
		Image mainBackGround = new Image( "pokerTable.png");
		BackgroundImage bGround = new BackgroundImage (mainBackGround,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background hbg = new Background(bGround);
		resultPane.setBackground(hbg);

		//creating winning sign that will display if the player is a winner
		Button winner = new Button();
		ImageView winnerView = new ImageView();
		if (playerWon == true){
			Image winnerImage = new Image("winnerLabel.png");
			winnerView = new ImageView(winnerImage);
		}
		winnerView.setPreserveRatio(true);
		winnerView.setFitHeight(200);
		winnerView.setFitWidth(280);

		// showing winnings
		Label winnings = new Label();
		winnings.setText("WINNINGS");
		winnings.setStyle("-fx-font-weight:bold;"+"-fx-text-fill: red;" + "-fx-font-size: 30;");
		moneyDisplay.setStyle("-fx-font-weight:bold;"+"-fx-text-fill: white;" + "-fx-font-size: 22;");

		// total money
		Label totalWinnings = new Label();
		totalWinnings.setText("TOTAL WINNINGS");
		totalWinnings.setStyle("-fx-font-weight:bold;"+"-fx-text-fill: red;" + "-fx-font-size: 30;");
		totalWinningsDisplay.setStyle("-fx-font-weight:bold;"+"-fx-text-fill: white;" + "-fx-font-size: 22;");

		// creating play again button
		Button playAgainButton = new Button("Play");
		playAgainButton.setDisable(false);
		playAgainButton.setStyle("-fx-background-color:red;"+"-fx-font-weight:bold;"+"-fx-text-fill:black;");
		playAgainButton.setMaxSize(150,150);

		// creating quit button
		Button qButton = new Button("Quit");
		qButton.setStyle("-fx-background-color:red;"+"-fx-font-weight:bold;"+"-fx-text-fill: black;");
		qButton.setMaxSize(150,150);

		// aligning quit and play buttons horizontally
		HBox playQuitHolder = new HBox(10, playAgainButton, qButton);
		playQuitHolder.setAlignment(Pos.CENTER);

		// aligning winning vertically
		VBox resultDisplay = new VBox(winnerView, winnings, moneyDisplay, totalWinnings, totalWinningsDisplay, playQuitHolder);
		resultDisplay.setAlignment(Pos.CENTER);


		// even handlers for play and quit buttons
		playAgainButton.setOnAction(e->{
			GameInfo temp = new GameInfo();
			int currentWinnings = Integer.parseInt(totalWinningsDisplay.getText());
			clearVariables(temp, currentWinnings, "PLAY_AGAIN");
			clientConnection.send(temp);
			sceneMap.put("game", gameScene(pStage));
			pStage.setScene(sceneMap.get("game"));
		});
		qButton.setOnAction(e -> System.exit(0));


		resultPane.setCenter(resultDisplay);
		return new Scene(resultPane, 900, 700);
	}

	// method resets the game and lets the server know where the player is at in the game
	public void clearVariables(GameInfo temp, int currentMoney, String message){
		temp.playWager = 0;
		temp.pairPlus = 0;
		temp.dealerCards.clear();
		temp.anteWager = 0;
		temp.playerCards.clear();
		temp.bank = currentMoney;
		temp.playerUpdates.clear();
		temp.code = message;
		gameUpdates.getItems().clear();
	}


	// method creates the menu bar found in the game scene
	// options provide allow the player to start a new game, change the look of the game, or exit the game
	public MenuBar menuBarFunction(Stage pStage){
		// create our menu tab titled "Menu"
		Menu menu = new Menu("Options");

		// create the items we want in the menu tab
		MenuItem freshStart = new MenuItem("Fresh Start");
		MenuItem newLook = new MenuItem("New Look");
		MenuItem exit = new MenuItem ("Exit");

		// add the items into the menu tab
		menu.getItems().add(freshStart);
		menu.getItems().add(newLook);
		menu.getItems().add(exit);

		// create the menu bar
		MenuBar mb = new MenuBar();
		// then add the menu with the menu items, into the menu bar
		mb.getMenus().add(menu);

		// freshStart will clear the whole game
		freshStart.setOnAction(e ->{
			//clear winnings and allow user to start a new game
			GameInfo temp = new GameInfo();
			int winnings = 1000;
			clearVariables(temp, winnings, "PLAY_AGAIN");
			sceneMap.put("game", gameScene(pStage));
			pStage.setScene(sceneMap.get("game"));
		});
		// newLook will change the theme of the game
		newLook.setOnAction(e -> {
			// altTheme is a bool that lets us know whether the new-look feature has been activated
			altLook = !altLook; // if altLook is true, it is now false, and vice versa
			sceneMap.put("game", gameScene(pStage));
			pStage.setScene(sceneMap.get("game"));
		});

		// event handler for exit button
		exit.setOnAction(e -> System.exit(0));

		return mb;
		}

		private void protocolAnalysis(GameInfo gameData, Stage pStage){
			// transitioning to game scene only if there is a connection between server and client
			if (starterInfo.code.equals("connected")){
				// creating the game scene and transitioning to that game scene
				sceneMap.put("game", gameScene(pStage));
				pStage.setScene(sceneMap.get("game"));
			}


			if(starterInfo.code.equals("DEAL")){
				// storing players cards to help with winning analysis
				currentPlayerCards = new ArrayList<>();
				currentPlayerCards = starterInfo.playerCards;

				// grabbing the player's cards that need to b flipped over
				gameCardDisplay2 = new Image(starterInfo.playerCards.get(0).suit + starterInfo.playerCards.get(0).rank +".png");
				card4= new ImageView(gameCardDisplay2);
				card4.setFitHeight(120);
				card4.setFitWidth(90);
				gameCardDisplay3 = new Image(starterInfo.playerCards.get(1).suit + starterInfo.playerCards.get(1).rank +".png");
				card5= new ImageView(gameCardDisplay3);
				card5.setFitHeight(120);
				card5.setFitWidth(90);
				gameCardDisplay4 = new Image(starterInfo.playerCards.get(2).suit + starterInfo.playerCards.get(2).rank +".png");
				card6= new ImageView(gameCardDisplay4);
				card6.setFitHeight(120);
				card6.setFitWidth(90);

				playerCardHBox = new HBox(10, card4, card5, card6);
				playVBox = new VBox(10, playerCardHBox, buttonHBox, dealButton);
				playVBox.setPadding(new Insets(0, 0, 15, 0));
				playVBox.setAlignment(Pos.CENTER);

				// changing bank display
				moneyTotal = starterInfo.bank;
				bankMoney.setText(Integer.toString(moneyTotal));

				playButtonsAndMoneyBox = new HBox(160, playVBox, moneyBox);
				playButtonsAndMoneyBox.setAlignment(Pos.BOTTOM_RIGHT);

				gamePane.setBottom(playButtonsAndMoneyBox);
			}

			if(starterInfo.code.equals("PLAY")){
				// storing dealer's card to send back for winning analysis
				currentDealerCards = new ArrayList<>();
				currentDealerCards = starterInfo.dealerCards;


				// grabbing the dealer's cards to display
				gameCardDisplay2 = new Image(starterInfo.dealerCards.get(0).suit + starterInfo.dealerCards.get(0).rank +".png");
				card1= new ImageView(gameCardDisplay2);
				card1.setFitHeight(120);
				card1.setFitWidth(90);
				gameCardDisplay3 = new Image(starterInfo.dealerCards.get(1).suit + starterInfo.dealerCards.get(1).rank +".png");
				card2= new ImageView(gameCardDisplay3);
				card2.setFitHeight(120);
				card2.setFitWidth(90);
				gameCardDisplay4 = new Image(starterInfo.dealerCards.get(2).suit + starterInfo.dealerCards.get(2).rank +".png");
				card3= new ImageView(gameCardDisplay4);
				card3.setFitHeight(120);
				card3.setFitWidth(90);

				// uploading game updates so the player can see
				for (int i = 0; i < starterInfo.playerUpdates.size(); i++) {
					gameUpdates.getItems().add(starterInfo.playerUpdates.get(i));
				}

				dealerCardHBox = new HBox(10, card1, card2, card3);
				dealerCardHBox.setAlignment(Pos.CENTER);
				gamePane.setCenter(dealerCardHBox);


				// slow down the transition to the result scene
				PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));

				// initiating result scene for when a player wins
				if (starterInfo.winType.equals("STRAIGHTFLUSH") || starterInfo.winType.equals("THREEOFAKIND") || starterInfo.winType.equals("STRAIGHT") ||
					starterInfo.winType.equals("FLUSH") || starterInfo.winType.equals("PAIR")){
					moneyDisplay.setText(Integer.toString(starterInfo.bank));  // displaying total winnings
					int gameWinnings = starterInfo.bank - 1000; // displaying winning for the game
					totalWinningsDisplay.setText(Integer.toString(gameWinnings));
					// creating the game scene and transitioning to that game scene
					pauseTransition.setOnFinished(e -> {
						sceneMap.put("results", resultScene(pStage));
						pStage.setScene(sceneMap.get("results"));
					});

					pauseTransition.play();
				}
				else {  // initiating result scene for when a player loses
					moneyDisplay.setText(Integer.toString(starterInfo.bank));
					int gameWinnings = starterInfo.bank - 1000;
					totalWinningsDisplay.setText(Integer.toString(gameWinnings));
					playerWon = false;
					// creating the game scene and transitioning to that game scene
					pauseTransition.setOnFinished(e -> {
					sceneMap.put("results", resultScene(pStage));
					pStage.setScene(sceneMap.get("results"));
					});

					pauseTransition.play();
				}

				if(starterInfo.code.equals("PLAY_AGAIN")){
					// receiving money total to make sure it keeps track of money a player lost after they choose to play again
					moneyTotal = starterInfo.bank;
				}

			}


		}


	}
