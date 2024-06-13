package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

public class SmallInfoLabel extends Label {

	public final static String FONT_PATH = "src/model/resources/Vermin_Vibes_1989.ttf";
	public final static String BACKGROUND_IMAGE = "grey_textplaceholder.png";

	public SmallInfoLabel(String text) {
		setPrefWidth(130);
		setPrefHeight(50);
		setText(text);
		setWrapText(true);
		setLabelFont();
		BackgroundImage backgroundImage = new BackgroundImage(new Image(BACKGROUND_IMAGE, 130, 50, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		
		setBackground(new Background(backgroundImage));
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(10,10,10,10));
	}
	
	private void setLabelFont() {
		try {
			setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 15));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
