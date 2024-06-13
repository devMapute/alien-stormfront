package view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CHARACTER;
import model.CharacterPicker;
import model.InfoLabel;
import model.StormFrontButton;
import model.StormFrontSubScene;

public class ViewManager {

	private static final double WIDTH = 1024;
	private static final double HEIGHT = 768;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;

	private final static int MENU_BUTTONS_START_X = 100;
	private final static int MENU_BUTTONS_START_Y = 150;

	private StormFrontSubScene creditsSubScene;
	private StormFrontSubScene helpSubScene;
	private StormFrontSubScene scoreSubScene;
	private StormFrontSubScene characterChooserScene;

	private StormFrontSubScene sceneToHide;

	List<StormFrontButton> menuButtons;

	List<CharacterPicker> characterList;
	private CHARACTER chosenCharacter;

	public ViewManager() {
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, WIDTH, HEIGHT);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		createSubScenes();
		createButtons();
		createBackground();
		createLogo();

		//		StormFrontSubScene subscene = new StormFrontSubScene();
		//		subscene.setLayoutX(200);
		//		subscene.setLayoutY(100);
		//		mainPane.getChildren().add(subscene);
	}

	private void showSubScene(StormFrontSubScene subScene) {
		if (sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		subScene.moveSubScene();
		sceneToHide = subScene;

	}

	private void createSubScenes() {
		creditsSubScene = new StormFrontSubScene();
		mainPane.getChildren().add(creditsSubScene);

		helpSubScene = new StormFrontSubScene();
		mainPane.getChildren().add(helpSubScene);

		scoreSubScene = new StormFrontSubScene();
		mainPane.getChildren().add(scoreSubScene);

		createHelpSubScene();
		createCreditsSubScene();
		createCharacterChooserSubScene();

	}

	private void createHelpSubScene() {
		Image helpImage = new Image("help.png");
		ImageView helpImageView = new ImageView(helpImage);
		helpImageView.setFitWidth(600); 
		helpImageView.setFitHeight(400); 
		helpImageView.setLayoutX(1);
		helpImageView.setLayoutY(1);
		helpSubScene.getPane().getChildren().add(helpImageView);
	}
	
	private void createCreditsSubScene() {
		Image creditsImage = new Image("credits.png");
		ImageView creditsImageView = new ImageView(creditsImage);
		creditsImageView.setFitWidth(600); 
		creditsImageView.setFitHeight(400); 
		creditsImageView.setLayoutX(1);
		creditsImageView.setLayoutY(1);
		creditsSubScene.getPane().getChildren().add(creditsImageView);
	}

	private void createCharacterChooserSubScene() {
		characterChooserScene = new StormFrontSubScene();
		mainPane.getChildren().add(characterChooserScene);

		InfoLabel chooseCharacterLabel = new InfoLabel("CHOOSE YOUR CHARACTER");
		chooseCharacterLabel.setLayoutX(110);
		chooseCharacterLabel.setLayoutY(25);
		characterChooserScene.getPane().getChildren().add(chooseCharacterLabel);
		characterChooserScene.getPane().getChildren().add(createCharactersToChoose());
		characterChooserScene.getPane().getChildren().add(createButtonToStart());
	}


	private HBox createCharactersToChoose() {
		HBox box = new HBox();
		box.setSpacing(20);

		characterList = new ArrayList<>();
		for (CHARACTER character : CHARACTER.values()) {
			CharacterPicker characterToPick = new CharacterPicker(character);


			characterList.add(characterToPick);
			box.getChildren().add(characterToPick);
			characterToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					for (CharacterPicker character : characterList) {
						character.setIsCircleChosen(false);
					}
					characterToPick.setIsCircleChosen(true);
					chosenCharacter = characterToPick.getCharacter();
				}

			});
		}

		box.setLayoutX(300 - (118 * 2));
		box.setLayoutY(100);
		return box;
	}

	
	private StormFrontButton createButtonToStart() {
		StormFrontButton startButton = new StormFrontButton("START");
		startButton.setLayoutY(300);
		startButton.setLayoutX(350);
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (chosenCharacter != null) {
					GameViewManager gameManager = new GameViewManager();
					gameManager.createNewGame(mainStage, chosenCharacter);
				}

			}

		});
		return startButton;
	}

	
	public Stage getMainStage() {
		return mainStage;
	}

	
	private void addMenuButton(StormFrontButton button) {
		button.setLayoutX(MENU_BUTTONS_START_X);
		button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}

	
	private void createButtons() {
		createStartButton();
		createScoresButton();
		createHelpButton();
		createCreditsButton();
		createExitButton();
	}

	
	private void createStartButton() { //START BUTTON
		StormFrontButton startButton = new StormFrontButton("PLAY");
		addMenuButton(startButton);

		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(characterChooserScene);

			}

		});

	}

	
	private void createScoresButton() { //SCORES BUTTON
		StormFrontButton scoresButton = new StormFrontButton("SCORES");
		addMenuButton(scoresButton);

		scoresButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(scoreSubScene);
			}

		});
	}

	
	private void createHelpButton() { //HELP BUTTON
		StormFrontButton helpButton = new StormFrontButton("HELP");
		addMenuButton(helpButton);

		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(helpSubScene);
			}
		});
	}

	
	private void createCreditsButton() { //CREDITS BUTTON
		StormFrontButton creditsButton = new StormFrontButton("CREDITS");
		addMenuButton(creditsButton);

		creditsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(creditsSubScene);
			}

		});
	}

	
	private void createExitButton() { //EXIT BUTTON
		StormFrontButton exitButton = new StormFrontButton("EXIT");
		addMenuButton(exitButton);

		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.close();
			}

		});
	}

	
	private void createLogo() { //LOGO
		ImageView logo = new ImageView("asf.png");
		double logoWidth = 500; // Adjust the value as needed
		double logoHeight = 100; // Adjust the value as needed

		logo.setFitWidth(logoWidth);
		logo.setFitHeight(logoHeight);

		logo.setLayoutX(400);
		logo.setLayoutY(75);
		logo.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(new DropShadow());
			}

		});

		logo.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(null);
			}

		});

		mainPane.getChildren().add(logo);
	}

	
	private void createBackground() { //BACKGROUND
		Image backgroundImage = new Image("burningcity.png", 800, 600, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));

	}
}
