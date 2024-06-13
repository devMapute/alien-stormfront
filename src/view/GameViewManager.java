package view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.BufferedImageLoader;
import model.CHARACTER;
import model.InfoLabel;
import model.SmallInfoLabel;
import model.SpriteSheet;
import model.StormFrontSubScene;

public class GameViewManager {

	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;

	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 600;

	private Stage menuStage;
	private ImageView character;
	private ImageView[] bullets;
	///////////////////////////
	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String BACKGROUND_IMAGE = "gamebackground.png";
	private final static String BULLET = "bullet.png";

	private boolean isUpKeyPressed;
	private boolean bulletFired_1, bulletFired_2, bulletFired_3;
	private AnimationTimer gameTimer;

	private double characterVelocityY = 0;
	private final double GRAVITY = 0.4;
	private int bulletCount = 0, bullet1, bullet2, bullet3;
	//////////////////////////

	private final static String ALIEN_IMAGE = "alien.gif";
	private final static String FLAME_IMAGE = "flame.gif";

	private ImageView[] Aliens;
	private ImageView[] Flames;
	Random randomPositionGenerator;
	private long startTime;
	private long elapsedTime;
	private int finalWaveCounter;

	private ImageView life;
	private SmallInfoLabel pointsLabel;
	private ImageView[] playerLifes;
	private int playerLife = 3;
	private int points;

	public final static int CHARACTER_RADIUS = 20;
	public final static int FLAMES_RADIUS = 20;
	public final static int ALIEN_RADIUS = 20;
	public final static int BULLET_RADIUS = 10;
	
	private StormFrontSubScene GameOverSubScene;
	private StormFrontSubScene YouWinSubScene;
	
	private StormFrontSubScene sceneToHide;

	public GameViewManager() {

		initializeStage();
		createKeyListener();
		randomPositionGenerator = new Random();
		startTime = System.currentTimeMillis();
	}

	private void createKeyListener() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.UP) {
					isUpKeyPressed = true;
				} else if (!bulletFired_1 && !bulletFired_2 && !bulletFired_3 && event.getCode() == KeyCode.SPACE) {
					bulletFired_1 = true;
					bullet1 = bulletCount;
				} else if (bulletFired_1 && !bulletFired_2 && !bulletFired_3 && event.getCode() == KeyCode.SPACE) {
					bulletFired_2 = true;
					bullet2 = bulletCount;
				} else if (bulletFired_1 && bulletFired_2 && !bulletFired_3 && event.getCode() == KeyCode.SPACE) {
					bulletFired_3 = true;
					bullet3 = bulletCount;
				}

			}

		});

		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.UP) {
					isUpKeyPressed = false;
				}

			}

		});
	}

	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.getIcons().add(new Image("emblem.png"));
		gameStage.setResizable(false);
		createSubScenes();
		gameStage.setScene(gameScene);

	}
	
	
	private void createSubScenes() {
		createGameOverSubScene();
		YouWinSubScene = new StormFrontSubScene();
		gamePane.getChildren().add(YouWinSubScene);
		
	}
	
	private void createGameOverSubScene() {
		GameOverSubScene = new StormFrontSubScene();

	    // Create game over label
	    InfoLabel gameoverLabel = new InfoLabel("GAME OVER!");
	    gameoverLabel.setLayoutX(250);
	    gameoverLabel.setLayoutY(100);

	    // Add the label to the subscene's pane
	    GameOverSubScene.getPane().getChildren().add(gameoverLabel);

	    // Add the subscene to the game pane
	    gamePane.getChildren().add(GameOverSubScene);
	}
	
	private EventHandler<ActionEvent> gameOverHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            showSubScene(GameOverSubScene);
            gameTimer.stop();
        }
    };
	
	
	private void createGameElements(CHARACTER character) {
		playerLife = 3;
		pointsLabel = new SmallInfoLabel("POINTS: 00");
		pointsLabel.setLayoutX(650);
		pointsLabel.setLayoutY(20);
		gamePane.getChildren().add(pointsLabel);
		
		playerLifes = new ImageView[3];
		for (int i = 0; i < playerLifes.length; i++) {
			playerLifes[i] = new ImageView(character.getUrlLife());
			playerLifes[i].setFitHeight(50);
			playerLifes[i].setFitWidth(50);
			playerLifes[i].setLayoutX(640+(50*i));
			playerLifes[i].setLayoutY(80);
			gamePane.getChildren().add(playerLifes[i]);
		}
		
		
		Aliens = new ImageView[3];
		for (int i = 0; i < Aliens.length; i++) {
			Aliens[i] = new ImageView(ALIEN_IMAGE);
			Aliens[i].setFitWidth(100);
			Aliens[i].setFitHeight(100);
			setNewElementPosition(Aliens[i]);
			gamePane.getChildren().add(Aliens[i]);
		}

		Flames = new ImageView[3];
		for (int i = 0; i < Flames.length; i++) {
			Flames[i] = new ImageView(FLAME_IMAGE);
			Flames[i].setFitWidth(50);
			Flames[i].setFitHeight(100);

			setNewElementPosition(Flames[i]);
			//			Flames[i].setLayoutY(Flames[i].getLayoutY()+50);
			gamePane.getChildren().add(Flames[i]);
		}
	}

	private void moveGameElements() {
		for (int i = 0; i <Aliens.length ; i++) {
			Aliens[i].setLayoutX(Aliens[i].getLayoutX()-5);
		}
		for (int i = 0; i <Flames.length ; i++) {
			Flames[i].setLayoutX(Flames[i].getLayoutX()-4);
		}
	}

	private void checkIfElementsAreBehindTheCharacter() {
		for (int i = 0; i <Aliens.length ; i++) {
			if(Aliens[i].getLayoutX() < -600) {
				setNewElementPosition(Aliens[i]);			
			}
		}
		for (int i = 0; i <Flames.length ; i++) {
			if(Flames[i].getLayoutX() < -600) {
				setNewElementPosition(Flames[i]);
			}
		}

	}

	private void setNewElementPosition(ImageView image) {
		double newXPosition = (randomPositionGenerator.nextInt(2000)) + GAME_WIDTH;
		double newYPosition = (GAME_HEIGHT / 2) + 100;

		// Check if the new position is too close to other elements
		if (Aliens != null) {
			for (ImageView element : Aliens) {
				if (element != null && Math.abs(newXPosition - element.getLayoutX()) < 300) {
					newXPosition += 300; // Add a gap of 300 pixels
					break;
				}
			}
		}

		if (Flames != null) {
			for (ImageView element : Flames) {
				if (element != null && Math.abs(newXPosition - element.getLayoutX()) < 300) {
					newXPosition += 300; // Add a gap of 300 pixels
					break;
				}
			}
		}

		image.setLayoutX(newXPosition);
		image.setLayoutY(newYPosition);
	}

	public void createNewGame(Stage menuStage, CHARACTER character) {
		this.menuStage = menuStage;
		this.menuStage.hide();

		playerLifes = new ImageView[playerLife];
		for (int i = 0; i < playerLife; i++) {
			playerLifes[i] = new ImageView(FLAME_IMAGE);
			// Set the layout position of the life images as per your requirement
			gamePane.getChildren().add(playerLifes[i]);
			
		}
		createBackground();
		createCharacter(character);
		createGameElements(character);
		createGameLoop();
		gameStage.show();
	}

	private void createGameLoop() {
		gameTimer = new AnimationTimer() {

			public void handle(long now) {
				elapsedTime = System.currentTimeMillis() - startTime;
				// Check if 20 seconds have passed
                if (elapsedTime > 10000 && Aliens.length == 3) {
                    // Increase the size of the Aliens array to 5 for the boss battle
                	System.out.println("BOSS BATTLE");
                    increaseAlienArraySize(5);
                    finalWaveCounter = 0;
                }
				
				moveBackground();
				moveGameElements();
				checkIfElementsAreBehindTheCharacter();
				checkIfElementsCollide();
				moveCharacter();
				moveBullet();
			}
		};

		gameTimer.start();

	}
	
	private void increaseAlienArraySize(int newSize) {
        ImageView[] newAliens = new ImageView[newSize];

        // Copy existing aliens to the new array
        for (int i = 0; i < Aliens.length; i++) {
            newAliens[i] = Aliens[i];
        }

        // Create new aliens to fill the remaining slots
        for (int i = Aliens.length; i < newSize; i++) {
            newAliens[i] = new ImageView(ALIEN_IMAGE);
            newAliens[i].setFitWidth(100);
            newAliens[i].setFitHeight(100);
            setNewElementPosition(newAliens[i]);
            gamePane.getChildren().add(newAliens[i]);
        }

        // Update the Aliens array
        Aliens = newAliens;
    }
	

	private void createBullets() {
		bullets = new ImageView[500];

		for(int i = 0; i < bullets.length; i++) {
			bullets[i] = new ImageView(BULLET);
			bullets[i].setFitWidth(30);
			bullets[i].setFitHeight(30);
			bullets[i].setLayoutX(48);
			bullets[i].setLayoutY((GAME_HEIGHT / 2 ) + 120);
			gamePane.getChildren().add(bullets[i]);
		}
	}
	private void createCharacter(CHARACTER chosenCharacter) {
		character = new ImageView(chosenCharacter.getUrl());
		character.setLayoutX(10);

		character.setFitWidth(100);
		character.setFitHeight(100);
		character.setLayoutY((GAME_HEIGHT / 2) +200);

		createBullets();

		gamePane.getChildren().add(character);
	}

	private void createBackground() {
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();

		ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
		ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);



		gridPane1.getChildren().add(backgroundImage1);
		gridPane2.getChildren().add(backgroundImage2);


		// Set the initial layout positions
		gridPane1.setLayoutX(0);
		gridPane2.setLayoutX(GAME_WIDTH);

		// Add the grid panes to the game pane
		gamePane.getChildren().addAll(gridPane1, gridPane2);
	}

	private void moveBackground() {
		gridPane1.setLayoutX(gridPane1.getLayoutX()-4);
		gridPane2.setLayoutX(gridPane2.getLayoutX()-4);

		if(gridPane1.getLayoutX() <= -GAME_WIDTH) {
			gridPane1.setLayoutX(GAME_WIDTH);
		}
		if(gridPane2.getLayoutX() <= -GAME_WIDTH) {
			gridPane2.setLayoutX(GAME_WIDTH);
		}
	}

	private void moveCharacter() {
		if (isUpKeyPressed && character.getLayoutY() >= (GAME_HEIGHT / 2 + 100)) {
			characterVelocityY = -15; // Initial jump velocity
		}

		// Apply gravity to the character's velocity
		characterVelocityY += GRAVITY;

		// Update the character's position
		character.setLayoutY(character.getLayoutY() + characterVelocityY);
		for(int i = bulletCount; i < bullets.length; i++) {
			bullets[i].setLayoutY(bullets[i].getLayoutY()+ characterVelocityY);
		}

		// Keep the character within the bounds of the game screen
		if (character.getLayoutY() > GAME_HEIGHT / 2 + 100) {
			character.setLayoutY(GAME_HEIGHT / 2 + 100);
			for(int i = bulletCount; i < bullets.length; i++) {
				bullets[i].setLayoutY((GAME_HEIGHT / 2) + 120);
			}
			characterVelocityY = 0; // Reset the velocity when the character lands
		}
	}

	private void moveBullet() {
		bulletFired_1 = checkBullet(bulletFired_1, bullet1);

		bulletFired_2 = checkBullet(bulletFired_2, bullet2);

		bulletFired_3 = checkBullet(bulletFired_3, bullet3);

	}

	private boolean checkBullet(boolean bulletFired, int bullet) {
		if(bulletFired){
			bullets[bullet].setLayoutX(bullets[bullet].getLayoutX()+5);
			bulletCount = bullet + 1;
		}

		if(bullets[bullet].getLayoutX() >= GAME_WIDTH) {
			bulletFired = false;
		}

		return bulletFired;
	}


	private void checkIfElementsCollide() {
		// alien + character collision
		for (int i = 0; i < Aliens.length; i++) {
			if (ALIEN_RADIUS + CHARACTER_RADIUS > calculateDistance(character.getLayoutX()+30, 
					character.getLayoutY()+30,
					Aliens[i].getLayoutX()+30, 
					Aliens[i].getLayoutY()+30)) 
			{
				// code runs when alien and character collide
				removeLife();
				setNewElementPosition(Aliens[i]);
				System.out.println("ALIEN_RADIUS + CHARACTER_RADIUS");//checker
			}
		}

		// character + flame collision
		for (int i = 0; i < Flames.length; i++) {
			if (CHARACTER_RADIUS + FLAMES_RADIUS > calculateDistance(Flames[i].getLayoutX()+30, 
					Flames[i].getLayoutY()+30,
					character.getLayoutX()+30, 
					character.getLayoutY()+30)) 
			{
				// code runs when character and flame collide
				removeLife();
				setNewElementPosition(Flames[i]);
				System.out.println("CHARACTER_RADIUS + FLAMES_RADIUS");//checker
			}
		}

//		// alien + bullet collision
		
		for (int i = 0; i < Aliens.length; i++) {
			double distance1 = calculateDistance(bullets[bullet1].getLayoutX()+10, bullets[bullet1].getLayoutY()+10, Aliens[i].getLayoutX()+30, Aliens[i].getLayoutY()+30);
			double distance2 = calculateDistance(bullets[bullet2].getLayoutX()+10, bullets[bullet2].getLayoutY()+10, Aliens[i].getLayoutX()+30, Aliens[i].getLayoutY()+30);
			double distance3 = calculateDistance(bullets[bullet3].getLayoutX()+10, bullets[bullet3].getLayoutY()+10, Aliens[i].getLayoutX()+30, Aliens[i].getLayoutY()+30);
			if (ALIEN_RADIUS + BULLET_RADIUS > distance1 && (bulletFired_1 || bulletFired_2 || bulletFired_3)) 
			{
				// code runs when alien and bullet collide
				setNewElementPosition(Aliens[i]);
				bullets[bullet1].setLayoutX(GAME_WIDTH+10);
				bullets[bullet1].setLayoutY(GAME_HEIGHT+10);
				bulletFired_1 = false;
				points += 10;
				String textToSet = "Points: ";
				pointsLabel.setText(textToSet + points);
				System.out.println("ALIEN_RADIUS + BULLET_RADIUS");//checker
				elapsedTime = System.currentTimeMillis() - startTime;
				// Check if 20 seconds have passed
                if (elapsedTime > 10000 ) {
                	finalWaveCounter++;
                }
				
			} else if (ALIEN_RADIUS + BULLET_RADIUS > distance2 && (bulletFired_1 || bulletFired_2 || bulletFired_3)) {
				setNewElementPosition(Aliens[i]);
				bullets[bullet2].setLayoutX(GAME_WIDTH+10);
				bullets[bullet2].setLayoutY(GAME_HEIGHT+10);
				bulletFired_2 = false;
				points += 10;
				String textToSet = "Points: ";
				pointsLabel.setText(textToSet + points);
				System.out.println("ALIEN_RADIUS + BULLET_RADIUS");
				elapsedTime = System.currentTimeMillis() - startTime;
				// Check if 20 seconds have passed
                if (elapsedTime > 10000 ) {
                	finalWaveCounter++;
                }
			} else if (ALIEN_RADIUS + BULLET_RADIUS > distance3 && (bulletFired_1 || bulletFired_2 || bulletFired_3)){
				setNewElementPosition(Aliens[i]);
				bullets[bullet3].setLayoutX(GAME_WIDTH+10);
				bullets[bullet3].setLayoutY(GAME_HEIGHT+10);
				bulletFired_3 = false;
				points += 10;
				String textToSet = "Points: ";
				pointsLabel.setText(textToSet + points);	
				System.out.println("ALIEN_RADIUS + BULLET_RADIUS");
				elapsedTime = System.currentTimeMillis() - startTime;
				// Check if 20 seconds have passed
                if (elapsedTime > 10000) {
                	finalWaveCounter++;
                }
			}
		}
		
		 // Check if all aliens are cleared
        if (areAllAliensCleared()) {
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }

	}
	
	private boolean areAllAliensCleared() {
        if(finalWaveCounter ==  10) {
        	return true;
        }else {
        	return false;
        }
        
    }

	private void removeLife() {
		if (playerLife > 0) {
			gamePane.getChildren().remove(playerLifes[playerLife - 1]);
			playerLife--;
		}
		if (playerLife <= 0) {
			gameOverHandler.handle(null);
			
		}
	}

	private double calculateDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	private void showSubScene(StormFrontSubScene subScene) {
		if (sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		subScene.moveSubScene();
		sceneToHide = subScene;

	}
}


