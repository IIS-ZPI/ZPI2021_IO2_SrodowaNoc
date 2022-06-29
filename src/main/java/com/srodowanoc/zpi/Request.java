package com.srodowanoc.zpi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    public String data;
    public Integer status;

    /*
      NBP API - Table types
      a - table with mid-prices
      b - table with mid-prices for non-exchangeable currencies
      c - table with ask and bid prices
     */

    /**
     * Returns all currencies in given table
     */
    public Integer getAvailableCurrency(String tableName) {
        String url = String.format("http://api.nbp.pl/api/exchangerates/tables/%s/?format=json", tableName);
        sendRequest(url);
        return status;
    }

    /**
     * Returns exchange rates between dates for given currency
     */
    public Integer currencyDataBetweenDates(String tableName, String currency, String from, String to) {
        String url = String.format("http://api.nbp.pl/api/exchangerates/rates/%s/%s/%s/%s/?format=json", tableName, currency, from, to);
        sendRequest(url);
        return status;
    }

    public void sendRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            data = response.body();
            status = response.statusCode();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            data = "";
            status = 500;
        }

    }

}
