package com.srodowanoc.zpi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public class Main extends Application {

	public static Stage stg;

	@Override
	public void start(Stage stage) {

		try {
			stg = stage;
			
//			Image image = new Image("com/srodowanoc/zpi/icons/icon.png");

			Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
			Scene scene = new Scene(root);
			stage.setScene(scene);
//			stage.getIcons().add(image);
			stage.setTitle("NBPAPP");

			stage.initStyle(StageStyle.UNDECORATED);

			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}


