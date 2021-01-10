package com.currencyapi.currencychecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class CurrencyUtil {

    public static BigDecimal getNumber(String jsonFromUrl, String currency) throws JSONException {
        JSONObject json = new JSONObject(jsonFromUrl);
        JSONObject jsonValue = json.getJSONObject("rates");
        String stringVal = jsonValue.getString(currency);
        BigDecimal bigDecimal = new BigDecimal(stringVal);
        return bigDecimal;
    }

    public static String getYesterdayDate(){
        LocalDate localDate = LocalDate.now();
        LocalDate yesterday = localDate.minusDays(1);
        String yesterdayDateFormat = yesterday.format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
        return yesterdayDateFormat;
    }
}
