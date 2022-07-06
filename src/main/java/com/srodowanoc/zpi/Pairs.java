package com.srodowanoc.zpi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

import static com.srodowanoc.zpi.Statistics.findStartAndEndDates;

public class Pairs implements Initializable {
    @FXML
    private VBox table;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox currencySelect;
    @FXML
    private ComboBox currencySelect2;

    Map<String, String> currencies;

    String currency1 = "euro", currency2 = "dolar amerykański";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        currencies = new HashMap<>();
        currencySelect.getItems().addAll(Utils.getAvailableCurrencies(currencies));
        currencySelect.setOnAction((EventHandler<ActionEvent>) this::selectCurrency);
        currencySelect.setValue(currency1);

        currencySelect2.getItems().addAll(Utils.getAvailableCurrencies(currencies));
        currencySelect2.setOnAction((EventHandler<ActionEvent>) this::selectCurrency2);
        currencySelect2.setValue(currency2);
        prepareData();
    }

    @FXML
    private void selectCurrency(ActionEvent event) {
        currency1 = currencySelect.getSelectionModel().getSelectedItem().toString();
        prepareData();
    }

    @FXML
    private void selectCurrency2(ActionEvent event) {
        currency2 = currencySelect2.getSelectionModel().getSelectedItem().toString();
        prepareData();
    }

    private void prepareData() {
        // TODO: Waluty do wyboru: http://api.nbp.pl/api/exchangerates/tables/a/?format=json
        String[][] data = getData(currencies.get(currency1), currencies.get(currency2));
        if (data != null) {
            errorLabel.setVisible(false);
            Utils.printData(data, table);
            for (String[] row : data) {
                System.out.println(Arrays.deepToString(row));
            }
        } else {
            errorLabel.setVisible(true);
        }
    }

    /**
     * Returns 2D arrays of strings that should be displayed as table
     */
    public String[][] getData(String firstCurrency, String secondCurrency) {
        String[] dates = findStartAndEndDates();

        Request firstRequest = new Request();
        firstRequest.currencyDataBetweenDates("a", firstCurrency, dates[0], dates[1]);

        Request secondRequest = new Request();
        secondRequest.currencyDataBetweenDates("a", secondCurrency, dates[0], dates[1]);

        if (firstRequest.status != 200 && secondRequest.status != 200) {
            System.out.println("Unable to get data!");
            return null;
        }

        JsonObject firstJsonObject = JsonParser.parseString(firstRequest.data).getAsJsonObject();
        JsonObject secondJsonObject = JsonParser.parseString(secondRequest.data).getAsJsonObject();

        JsonArray firstData = firstJsonObject.get("rates").getAsJsonArray();
        JsonArray secondData = secondJsonObject.get("rates").getAsJsonArray();

        firstData = reverseArray(firstData);
        secondData = reverseArray(secondData);

        String[][] dataToDisplay = new String[30][4];
        addHeaders(dataToDisplay);

        for (int i = 1; i < 30; i = i + 1) {
            String[] indicators = getData(
                    firstData,
                    secondData,
                    i
            );
            dataToDisplay[i][0] = indicators[0];
            dataToDisplay[i][1] = indicators[1];
            dataToDisplay[i][2] = indicators[2];
            dataToDisplay[i][3] = indicators[3];
        }

        return dataToDisplay;

    }

    /**
     * Returns data, exchange rate, change, change in percent
     */
    public String[] getData(JsonArray firstData, JsonArray secondData, int dayNumber) {
        JsonElement firstCurrencyToday = firstData.get(dayNumber - 1);
        JsonElement firstCurrencyYesterday = firstData.get(dayNumber);

        JsonElement secondCurrencyToday = secondData.get(dayNumber - 1);
        JsonElement secondCurrencyYesterday = secondData.get(dayNumber);

        float firstCurrencyTodayPrice = firstCurrencyToday.getAsJsonObject().get("mid").getAsFloat();
        float secondCurrencyTodayPrice = secondCurrencyToday.getAsJsonObject().get("mid").getAsFloat();

        float firstCurrencyYesterdayPrice = firstCurrencyYesterday.getAsJsonObject().get("mid").getAsFloat();
        float secondCurrencyYesterdayPrice = secondCurrencyYesterday.getAsJsonObject().get("mid").getAsFloat();

        float todayExchangeRate = firstCurrencyTodayPrice / secondCurrencyTodayPrice;
        float yesterdayExchangeRate = firstCurrencyYesterdayPrice / secondCurrencyYesterdayPrice;

        float changeInPercentFloat = (todayExchangeRate / yesterdayExchangeRate) * 100 - 100;

        String date = firstData.get(dayNumber - 1).getAsJsonObject().get("effectiveDate").getAsString();
        String exchangeRate = String.valueOf(todayExchangeRate);
        String change = String.format("%.4f", (firstCurrencyTodayPrice / secondCurrencyTodayPrice) - (firstCurrencyYesterdayPrice / secondCurrencyYesterdayPrice));
        String changeInPercent = String.format("%.2f", changeInPercentFloat).replaceAll(",", ".") + "%";

        return new String[]{date, exchangeRate, change, changeInPercent};
    }

    /**
     * Reverse the order of data from "oldest to newest" to "newest to oldest"
     */
    public JsonArray reverseArray(JsonArray array) {
        JsonArray reversed = new JsonArray();

        for (int i = array.size() - 1; i >= 0; i = i - 1) {
            JsonElement el = array.get(i);
            reversed.add(el);
        }

        return reversed;
    }

    /**
     * Adds headers to the data that will be displayed
     */
    public void addHeaders(String[][] dataToDisplay) {
        dataToDisplay[0][0] = "Data";
        dataToDisplay[0][1] = "Kurs";
        dataToDisplay[0][2] = "Zmiana";
        dataToDisplay[0][3] = "Zmiana w %";
    }

}
