package com.srodowanoc.zpi;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage implements Initializable {
    @FXML
    private ImageView background;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    Stage stage = Main.stg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
