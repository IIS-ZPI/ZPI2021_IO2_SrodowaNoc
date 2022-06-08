package com.srodowanoc.zpi;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Statistics implements Initializable {

    @FXML
    private AnchorPane ap;

    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    Stage stage = Main.stg;

    TimerTask changeScreen = new TimerTask() {
        @Override
        public void run() {
            stage = Main.stg;
            if (!stage.isFullScreen()) {
                ap.setMinWidth(1324);
                ap.setMinHeight(722);
            } else {
                ap.setMinWidth(screenBounds.getWidth() - 42);
                ap.setMinHeight(screenBounds.getHeight() - 46);
            }
        }
    };
    Timer timer = new Timer();



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        timer.schedule(changeScreen, 0, 1);

    }
}





