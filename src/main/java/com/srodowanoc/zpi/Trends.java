package com.srodowanoc.zpi;


import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.web.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Trends implements Initializable {
    @FXML
    private WebView webView;

    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    Stage stage = Main.stg;

    TimerTask changeScreen = new TimerTask() {
        @Override
        public void run() {
            stage = Main.stg;
            if (!stage.isFullScreen()) {
                webView.setMinWidth(1325);
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

        // Example
        // TODO: Waluty do wyboru: http://api.nbp.pl/api/exchangerates/tables/a/?format=json
        String[][] data = getData("EUR");
        if (data != null) {
            // TODO: Printing data
            for (String[] row : data) {
                System.out.println(Arrays.deepToString(row));
            }
        } else {
            // TODO: Error handling
        }

    }

    /**
     * Returns 2D arrays of strings that should be displayed as table
     */
    public String[][] getData(String currency) {
        String[] dates = findStartAndEndDates();
        Request request = new Request();
        request.currencyDataBetweenDates("a", currency, dates[0], dates[1]);

        if (request.status != 200) {
            System.out.println("Unable to get data!");
            return null;
        }

        JsonObject jsonObject = new JsonParser().parse(request.data).getAsJsonObject();
        JsonArray data = jsonObject.get("rates").getAsJsonArray();
        data = reverseArray(data);

        String[][] dataToDisplay = new String[7][4];
        addHeaders(dataToDisplay);

        int[][] indicators = {
                calculateIndicatorsByDays(data, 6),
                calculateIndicatorsByDays(data, 11),
                calculateIndicatorsByDays(data, 23),
                calculateIndicatorsByDays(data, data.size() / 4),
                calculateIndicatorsByDays(data, data.size() / 2),
                calculateIndicatorsByDays(data, data.size())
        };

        for (int i = 0; i < indicators.length; i = i + 1) {
            dataToDisplay[i+1][1] = String.valueOf(indicators[i][0]);
            dataToDisplay[i+1][2] = String.valueOf(indicators[i][1]);
            dataToDisplay[i+1][3] = String.valueOf(indicators[i][2]);
        }

        return dataToDisplay;

    }

    /**
     * Reverse the order of data from "oldest to newest" to "newest to oldest"
     */
    public JsonArray reverseArray(JsonArray array) {
        JsonArray reversed = new JsonArray();

        for (int i = array.size()-1; i >= 0; i = i - 1) {
            JsonElement el = array.get(i);
            reversed.add(el);
        }

        return reversed;
    }

    /**
     * Adds headers to the data that will be displayed
     */
    public void addHeaders(String[][] dataToDisplay) {
        dataToDisplay[0][0] = "Liczba dni";
        dataToDisplay[0][1] = "Sesje wzrostowe";
        dataToDisplay[0][2] = "Sesje spadkowe";
        dataToDisplay[0][3] = "Sesje bez zmian";
        dataToDisplay[1][0] = "7 dni";
        dataToDisplay[2][0] = "14 dni";
        dataToDisplay[3][0] = "Miesiąc";
        dataToDisplay[4][0] = "Kwartał";
        dataToDisplay[5][0] = "Pół roku";
        dataToDisplay[6][0] = "Rok";
    }

    /**
     * Returns today date and date year ago
     */
    public String[] findStartAndEndDates() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String to = dateFormat.format(today);
        calendar.add(Calendar.YEAR, -1);
        Date nextYear = calendar.getTime();
        String from = dateFormat.format(nextYear);
        return new String[]{from, to};
    }

    /**
     * Calculate number of growth, decline and stable session over given number of days
     */
    public int[] calculateIndicatorsByDays(JsonArray data, int numberOfDays) {
        int growth = 0;
        int decline = 0;
        int stable = 0;

        for (int i = 1; i < numberOfDays; i = i + 1) {
            JsonElement yesterdayData = data.get(i-1);
            JsonElement todayData = data.get(i);
            float yesterdayMidPrice = yesterdayData.getAsJsonObject().get("mid").getAsFloat();
            float todayMidPrice = todayData.getAsJsonObject().get("mid").getAsFloat();

            if (todayMidPrice > yesterdayMidPrice) {
                growth = growth + 1;
            }
            if (todayMidPrice < yesterdayMidPrice) {
                decline = decline + 1;
            }
            if (todayMidPrice == yesterdayMidPrice) {
                stable = stable + 1;
            }
        }

        return new int[]{growth, decline, stable};
    }

    }
}
