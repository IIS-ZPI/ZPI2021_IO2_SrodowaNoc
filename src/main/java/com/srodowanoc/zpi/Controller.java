package com.srodowanoc.zpi;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

	@FXML
	private BorderPane scene;

	@FXML
	private ImageView exit;

	@FXML
	private AnchorPane opacityPane, drawerPane, menuIcons;

	@FXML
	private Label drawerImage;

	@FXML
	private AnchorPane bar;

	@FXML
	private ImageView minimize;


	double x = 0, y = 0;

	private final Stage stage = Main.stg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		exit.setOnMouseClicked(event -> System.exit(0));

		minimize();

		setMovement();

		fadeIn();

		try {
			setScene("homePage.fxml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		drawerImage.setOnMouseClicked(event -> showMenu());

		menuIcons.setOnMouseEntered(mouseEvent -> {
			showMenu();
			fadeOut();
		});

		menuIcons.setOnMouseExited(mouseEvent -> hideMenu());

		drawerPane.setOnMouseEntered(mouseEvent -> showMenu());

		drawerPane.setOnMouseExited(mouseEvent -> {
			hideMenu();
			fadeIn();
		});

		opacityPane.setOnMouseEntered(mouseEvent -> {
			hideMenu();
			fadeIn();
		});
	}

	private void minimize() {
		minimize.setOnMouseClicked(event -> stage.setIconified(true));
	}

	public void setMovement() {
		bar.setOnMousePressed(event -> {
			x = event.getSceneX();
			y = event.getSceneY();
		});

	}

	public void fadeIn() {
		if (opacityPane.isVisible()) {
			FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
			fadeTransition1.setFromValue(0.5);
			fadeTransition1.setToValue(0);
			fadeTransition1.play();

			fadeTransition1.setOnFinished(event1 -> opacityPane.setVisible(false));
		}
	}

	public void fadeOut() {
		if (!opacityPane.isVisible()) {
			opacityPane.setVisible(true);
			FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), opacityPane);
			fadeTransition1.setFromValue(0);
			fadeTransition1.setToValue(0.5);
			fadeTransition1.play();
		}
	}

	public void hideMenu() {

		TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), drawerPane);
		translateTransition1.setToX(-200);
		translateTransition1.play();
	}

	public void showMenu() {
		TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), drawerPane);
		translateTransition1.setToX(0);
		translateTransition1.play();
	}


	public void setScene(String fxml) throws IOException {
		hideMenu();
		scene.setCenter(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml))));
	}

	public void trends() throws IOException {
		setScene("trends.fxml");
	}

	public void statistics() throws IOException {
		setScene("statistics.fxml");
	}

	public void pairs() throws IOException {
		setScene("pairs.fxml");
	}

}
