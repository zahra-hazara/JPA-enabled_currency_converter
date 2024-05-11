package application.controller;


import dao.CurrencyDao;
import entity.*;


public class CurrencyController {
    private CurrencyDao currencyDao;

    public CurrencyController(CurrencyDao currencyDao) {
        this.currencyDao = this.currencyDao;
    }

    public double convertCurrency(String fromAbbreviation, String toAbbreviation, double amount) {
        Currency fromCurrency = currencyDao.findCurrencyByAbbreviation(fromAbbreviation);
        Currency toCurrency = currencyDao.findCurrencyByAbbreviation(toAbbreviation);
        if (fromCurrency != null && toCurrency != null) {
            double fromRate = fromCurrency.getExchangeRate();
            double toRate = toCurrency.getExchangeRate();
            return (amount * fromRate) / toRate;
        } else {
            throw new IllegalArgumentException("Unknown currency: " + fromAbbreviation + " or " + toAbbreviation);
        }
    }

    public void addCurrency(String code, double rate) {
    }
}
