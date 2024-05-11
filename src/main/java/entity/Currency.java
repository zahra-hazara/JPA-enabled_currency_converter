package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    private String abbreviation;
    private double exchangeRate;

    public Currency() {
        // Default constructor
    }

    public Currency(String abbreviation, double exchangeRate) {
        this.abbreviation = abbreviation;
        this.exchangeRate = exchangeRate;
    }

    // Getters and setters
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
