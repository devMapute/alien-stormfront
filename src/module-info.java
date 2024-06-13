module Alien_Stormfront {
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.controls;
	
	requires javafx.media;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
	opens view to javafx.graphics, javafx.fxml;
	opens model to javafx.graphics, javafx.fxml;

}
