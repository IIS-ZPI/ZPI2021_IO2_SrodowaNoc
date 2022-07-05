package com.srodowanoc.zpi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class Utils {

	public static void printData(String[][] data, VBox table) {
		table.getChildren().clear();
		for (int i = 1; i < data.length; i++) {
			String[] row = data[i];
			HBox line = new HBox();
			line.setSpacing(25);
			for (String entry : row) {
				Label l = new Label(entry);
				l.setPrefWidth(150);
				l.setMinWidth(150);
				l.setMaxWidth(150);
				line.getChildren().add(l);
			}
			table.getChildren().add(line);
		}
	}

	public static String[] getAvailableCurrencies(Map<String, String> currencies) {
		Request request = new Request();
		request.getAvailableCurrency("a");
		if (request.status != 200) {
			System.out.println("Unable to get data!");
			return null;
		}
		request.data = request.data.substring(1, request.data.length() - 1);
		JsonObject jsonObject = JsonParser.parseString(request.data).getAsJsonObject();
		JsonArray data = jsonObject.get("rates").getAsJsonArray();

		String[] res = new String[data.size()];
		for (int i = 0; i < data.size(); ++i) {
			JsonElement element = data.get(i);
			String name = element.getAsJsonObject().get("currency").getAsString();
			res[i] = name;
			String code = element.getAsJsonObject().get("code").getAsString();
			currencies.put(name, code);
		}
		return res;
	}
}
