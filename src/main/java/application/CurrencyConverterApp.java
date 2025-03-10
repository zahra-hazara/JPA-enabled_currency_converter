package application;

import dao.CurrencyDAO;
import entity.Currency;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CurrencyConverterApp extends Application {
    private CurrencyDAO currencyDAO;
    private ComboBox<Currency> fromCurrencyComboBox;
    private ComboBox<Currency> toCurrencyComboBox;
    private TextField amountTextField;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        try {
            currencyDAO = new CurrencyDAO();

            // Create UI components
            Label fromLabel = new Label("From:");
            Label toLabel = new Label("To:");
            Label amountLabel = new Label("Amount:");

            fromCurrencyComboBox = new ComboBox<>();
            toCurrencyComboBox = new ComboBox<>();

            amountTextField = new TextField();
            amountTextField.setPromptText("Enter amount");

            Button convertButton = new Button("Convert");
            convertButton.setOnAction(e -> convertCurrency());

            Button addCurrencyButton = new Button("Add New Currency");
            addCurrencyButton.setOnAction(e -> openAddCurrencyDialog(primaryStage));

            resultLabel = new Label("Result will appear here");

            // Load currencies from the database
            loadCurrencies();

            // Create layout
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            gridPane.add(fromLabel, 0, 0);
            gridPane.add(fromCurrencyComboBox, 1, 0);
            gridPane.add(toLabel, 0, 1);
            gridPane.add(toCurrencyComboBox, 1, 1);
            gridPane.add(amountLabel, 0, 2);
            gridPane.add(amountTextField, 1, 2);

            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(convertButton, addCurrencyButton);

            VBox mainLayout = new VBox(10);
            mainLayout.setPadding(new Insets(10));
            mainLayout.getChildren().addAll(gridPane, buttonBox, resultLabel);

            // Create scene and set stage
            Scene scene = new Scene(mainLayout, 400, 250);
            primaryStage.setTitle("Currency Converter");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            displayError("Failed to initialize application: " + e.getMessage());
        }
    }

    private void testDatabaseConnection() {
        try {
            EntityManager em = Persistence.createEntityManagerFactory("currencyPU").createEntityManager();
            em.close();
            System.out.println("Database connection successful");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCurrencies() {
        try {
            List<Currency> currencies = currencyDAO.getAllCurrencies();
            fromCurrencyComboBox.setItems(FXCollections.observableArrayList(currencies));
            toCurrencyComboBox.setItems(FXCollections.observableArrayList(currencies));

            if (!currencies.isEmpty()) {
                fromCurrencyComboBox.getSelectionModel().select(0);
                toCurrencyComboBox.getSelectionModel().select(0);
            }
        } catch (Exception e) {
            displayError("Failed to load currencies: " + e.getMessage());
        }
    }

    private void convertCurrency() {
        try {
            Currency fromCurrency = fromCurrencyComboBox.getValue();
            Currency toCurrency = toCurrencyComboBox.getValue();

            if (fromCurrency == null || toCurrency == null) {
                displayError("Please select currencies");
                return;
            }

            String amountText = amountTextField.getText().trim();
            if (amountText.isEmpty()) {
                displayError("Please enter an amount");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                displayError("Invalid amount format");
                return;
            }

            // Convert amount based on exchange rates
            double amountInBaseCurrency = amount / fromCurrency.getExchangeRate();
            double convertedAmount = amountInBaseCurrency * toCurrency.getExchangeRate();

            resultLabel.setText(String.format("%.2f %s = %.2f %s",
                    amount,
                    fromCurrency.getCode(),
                    convertedAmount,
                    toCurrency.getCode()));
        } catch (Exception e) {
            displayError("Conversion error: " + e.getMessage());
        }
    }

    private void openAddCurrencyDialog(Stage primaryStage) {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Currency");

        // Create form controls
        Label codeLabel = new Label("Code:");
        TextField codeField = new TextField();

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label symbolLabel = new Label("Symbol:");
        TextField symbolField = new TextField();

        Label rateLabel = new Label("Exchange Rate:");
        TextField rateField = new TextField();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        // Create layout
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(10));
        formPane.setHgap(10);
        formPane.setVgap(10);

        formPane.add(codeLabel, 0, 0);
        formPane.add(codeField, 1, 0);
        formPane.add(nameLabel, 0, 1);
        formPane.add(nameField, 1, 1);
        formPane.add(symbolLabel, 0, 2);
        formPane.add(symbolField, 1, 2);
        formPane.add(rateLabel, 0, 3);
        formPane.add(rateField, 1, 3);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(formPane, buttonBox);

        // Set actions
        saveButton.setOnAction(e -> {
            try {
                String code = codeField.getText().trim();
                String name = nameField.getText().trim();
                String symbol = symbolField.getText().trim();
                String rateText = rateField.getText().trim();

                if (code.isEmpty() || name.isEmpty() || symbol.isEmpty() || rateText.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText("Missing Fields");
                    alert.setContentText("All fields are required");
                    alert.showAndWait();
                    return;
                }

                double rate;
                try {
                    rate = Double.parseDouble(rateText);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText("Invalid Exchange Rate");
                    alert.setContentText("Exchange rate must be a valid number");
                    alert.showAndWait();
                    return;
                }

                Currency newCurrency = new Currency(code, name, symbol, rate);
                currencyDAO.addCurrency(newCurrency);

                dialog.close();

                // Refresh currency lists
                loadCurrencies();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to save currency");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        Scene scene = new Scene(mainLayout, 300, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        try {
            if (currencyDAO != null) {
                currencyDAO.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing EntityManagerFactory: " + e.getMessage());
        }
    }
}