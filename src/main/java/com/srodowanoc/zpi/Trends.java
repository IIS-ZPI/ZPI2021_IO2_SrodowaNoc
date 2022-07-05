package com.srodowanoc.zpi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

import static com.srodowanoc.zpi.Statistics.findStartAndEndDates;

public class Trends implements Initializable {
	@FXML
	private VBox table;

	@FXML
	private Label errorLabel;

	@FXML
	private ComboBox currencySelect;

	Map<String, String> currencies;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currencies = new HashMap<>();
		currencySelect.getItems().addAll(Utils.getAvailableCurrencies(currencies));
		currencySelect.setOnAction((EventHandler<ActionEvent>) this::selectCurrency);
		currencySelect.setValue("Euro");
		prepareData("EUR");
	}

	@FXML
	private void selectCurrency(ActionEvent event) {
		String currency = currencySelect.getSelectionModel().getSelectedItem().toString();
		prepareData(currencies.get(currency));
	}

	private void prepareData(String currency) {
		// TODO: Waluty do wyboru: http://api.nbp.pl/api/exchangerates/tables/a/?format=json
		String[][] data = getData(currency);
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
	public String[][] getData(String currency) {
		String[] dates = findStartAndEndDates();
		Request request = new Request();
		request.currencyDataBetweenDates("a", currency, dates[0], dates[1]);

		if (request.status != 200) {
			System.out.println("Unable to get data!");
			return null;
		}

		JsonObject jsonObject = JsonParser.parseString(request.data).getAsJsonObject();
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
			dataToDisplay[i + 1][1] = String.valueOf(indicators[i][0]);
			dataToDisplay[i + 1][2] = String.valueOf(indicators[i][1]);
			dataToDisplay[i + 1][3] = String.valueOf(indicators[i][2]);
		}

		return dataToDisplay;

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
	 * Calculate number of growth, decline and stable session over given number of days
	 */
	public int[] calculateIndicatorsByDays(JsonArray data, int numberOfDays) {
		int growth = 0;
		int decline = 0;
		int stable = 0;

		for (int i = 1; i < numberOfDays; i = i + 1) {
			JsonElement yesterdayData = data.get(i - 1);
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
