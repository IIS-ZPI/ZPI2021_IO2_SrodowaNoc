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


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
}
