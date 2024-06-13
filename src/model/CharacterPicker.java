package model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class CharacterPicker extends VBox{
	
	private ImageView circleImage;
	private ImageView characterImage;
	
	
	private String circleNotChosen = "grey_circle.png";
	private String circleChosen = "grey_boxTick.png";
	
	private CHARACTER character;
	
	private boolean isCircleChosen;
	
	
	public CharacterPicker(CHARACTER character) {
		circleImage = new ImageView(circleNotChosen);
		characterImage = new ImageView(character.getUrl());
		characterImage.setFitHeight(200);
		characterImage.setFitWidth(200);
		this.character = character;
		
		isCircleChosen = false;
		this.setAlignment(Pos.CENTER);
		this.setSpacing(20);
		this.getChildren().add(circleImage);
		this.getChildren().add(characterImage);

		
	}
	
	
	public CHARACTER getCharacter() {
		return character ;
	}
	
	public boolean getIsCircleChosen() {
		return isCircleChosen;
	}
	
	public void setIsCircleChosen ( boolean isCircleChosen) {
		this.isCircleChosen = isCircleChosen;
		String imageToSet = this.isCircleChosen ? circleChosen : circleNotChosen;
		circleImage.setImage(new Image(imageToSet));
	}

}
