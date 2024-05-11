package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main extends Application {
    private EntityManager entityManager;

    @Override
    public void start(Stage primaryStage) {
        // Initialize JPA EntityManager
        setupJpa();

        // UI Components
        ComboBox<String> currencyFrom = new ComboBox<>();
        ComboBox<String> currencyTo = new ComboBox<>();
        TextField amountField = new TextField();
        Button convertButton = new Button("Convert");
        TextField resultField = new TextField();

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(currencyFrom, 0, 0);
        grid.add(currencyTo, 1, 0);
        grid.add(amountField, 0, 1);
        grid.add(convertButton, 1, 1);
        grid.add(resultField, 0, 2, 2, 1);

        // Event Handling
        convertButton.setOnAction(e -> {
            try {
                // Dummy conversion logic, replace with actual call to controller/DAO
                double amount = Double.parseDouble(amountField.getText());
                double converted = amount * 1.1; // Placeholder for conversion rate
                resultField.setText(String.format("%.2f", converted));
            } catch (NumberFormatException ex) {
                resultField.setText("Invalid amount!");
            }
        });

        // Scene and Stage Setup
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setTitle("Currency Converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupJpa() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CurrencyConverterPU");
        entityManager = emf.createEntityManager();
    }

    @Override
    public void stop() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
