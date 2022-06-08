package com.srodowanoc.zpi;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.web.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Pairs implements Initializable {
    @FXML
    private WebView webView;

    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    Stage stage = Main.stg;

    TimerTask changeScreen = new TimerTask() {
        @Override
        public void run() {
            stage = Main.stg;
            if (!stage.isFullScreen()) {
                webView.setMinWidth(1324);
                webView.setMinHeight(722);
            } else {
                webView.setMinWidth(screenBounds.getWidth() - 42);
                webView.setMinHeight(screenBounds.getHeight() - 46);
            }
        }
    };
    Timer timer = new Timer();


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        timer.schedule(changeScreen, 0, 1);

    }
}
