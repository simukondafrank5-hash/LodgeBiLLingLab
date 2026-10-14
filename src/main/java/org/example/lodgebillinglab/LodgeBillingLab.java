package org.example.lodgebillinglab;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LodgeBillingLab extends Application {

    private static final String NAVY = "#101D35";
    private static final String GOLD = "#D6AE55";
    private static final String CREAM = "#F7F2E8";

    private Stage stage;
    private TextField customerField;
    private TextField nightsField;
    private TextField paidField;
    private ComboBox<String> roomBox;
    private Label totalLabel;
    private Label balanceLabel;
    private TableView<Bill> billTable;

    private final ObservableList<Bill> bills =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("MOONLIGHT LODGE");
        showLogin();
        stage.show();
    }

    private void showLogin() {
        Label logo = new Label("✦ MOONLIGHT LODGE ✦");
        logo.setAlignment(Pos.CENTER);
        logo.setStyle("-fx-font-size: 27px; -fx-font-weight: bold; -fx-font-style: italic;"
                + " -fx-text-fill: " + GOLD + ";");

        Label title = new Label("");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + GOLD + ";");

        Label welcomeLine = new Label("Welcome to comfort, elegance and warm hospitality");
        welcomeLine.setWrapText(true);
        welcomeLine.setAlignment(Pos.CENTER);
        welcomeLine.setStyle("-fx-font-size: 13px; -fx-font-style: italic; -fx-text-fill: #F7DFA0;");

        Label subtitle = new Label("Management and Billing System");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label message = new Label();
        message.setStyle("-fx-text-fill: #ff7777;");

        Button loginButton = new Button("LOGIN");
        styleButton(loginButton);
        loginButton.setMaxWidth(300);

        loginButton.setOnAction(e -> {
            if (usernameField.getText().trim().equals("admin")
                    && passwordField.getText().equals("moonlight123")) {
                showDashboard();
            } else {
                message.setText("Incorrect username or password.");
            }
        });
        passwordField.setOnAction(e -> loginButton.fire());

        VBox loginCard = new VBox(15, logo, title, welcomeLine, subtitle, usernameField,
                passwordField, loginButton, message);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(35));
        loginCard.setMaxWidth(390);
        loginCard.setStyle("-fx-background-color: " + NAVY + "; -fx-background-radius: 15;");

        StackPane root = new StackPane(loginCard);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: " + CREAM + ";");

        stage.setScene(new Scene(root, 1000, 700));
    }

    private void showDashboard() {
        Label logo = new Label("✦ ML ✦");
        logo.setAlignment(Pos.CENTER);
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-style: italic;"
                + " -fx-text-fill: " + NAVY + "; -fx-background-color: " + GOLD
                + "; -fx-background-radius: 30; -fx-padding: 12 10 12 10;");

        Label heading = new Label("MOONLIGHT LODGE");
        heading.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: " + GOLD + ";");

        Label welcome = new Label("Welcome, Administrator");
        welcome.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-style: italic;");

        VBox headingBox = new VBox(5, heading, welcome);
        HBox brandBox = new HBox(12, logo, headingBox);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        Button logoutButton = new Button("Logout");
        styleButton(logoutButton);
        logoutButton.setOnAction(e -> showLogin());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(brandBox, spacer, logoutButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 24, 18, 24));
        header.setStyle("-fx-background-color: " + NAVY + ";");

        customerField = new TextField();
        customerField.setPromptText("Enter customer full name");

        roomBox = new ComboBox<>();
        roomBox.getItems().addAll("Standard", "Deluxe", "Executive Suite");
        roomBox.setValue("Standard");
        roomBox.setMaxWidth(Double.MAX_VALUE);

        nightsField = new TextField();
        nightsField.setPromptText("Enter number of nights");

        paidField = new TextField();
        paidField.setPromptText("Enter amount paid (K)");

        Label rateLabel = new Label();
        rateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + NAVY + ";");
        roomBox.setOnAction(e -> rateLabel.setText("Rate per night: " + money(getRoomRate())));
        rateLabel.setText("Rate per night: " + money(getRoomRate()));

        totalLabel = new Label("K0.00");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + NAVY + ";");
        balanceLabel = new Label("K0.00");
        balanceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + NAVY + ";");

        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(13);
        form.setPadding(new Insets(18, 10, 10, 10));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPercentWidth(38);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPercentWidth(62);
        form.getColumnConstraints().addAll(labelColumn, inputColumn);

        form.add(makeLabel("Customer Name"), 0, 0);
        form.add(customerField, 1, 0);
        form.add(makeLabel("Room Type"), 0, 1);
        form.add(roomBox, 1, 1);
        form.add(makeLabel("Number of Nights"), 0, 2);
        form.add(nightsField, 1, 2);
        form.add(makeLabel("Room Rate"), 0, 3);
        form.add(rateLabel, 1, 3);
        form.add(makeLabel("Amount Paid (K)"), 0, 4);
        form.add(paidField, 1, 4);
        form.add(makeLabel("Total Bill"), 0, 5);
        form.add(totalLabel, 1, 5);
        form.add(makeLabel("Balance Due"), 0, 6);
        form.add(balanceLabel, 1, 6);

        Button calculateButton = new Button("Calculate Bill");
        Button saveButton = new Button("Save Bill");
        Button clearButton = new Button("Clear Form");
        Button deleteButton = new Button("Delete Record");
        Button receiptButton = new Button("Print Receipt");

        styleButton(calculateButton);
        styleButton(saveButton);
        styleButton(clearButton);
        styleButton(deleteButton);
        styleButton(receiptButton);

        calculateButton.setOnAction(e -> calculateBill());
        saveButton.setOnAction(e -> saveBill());
        clearButton.setOnAction(e -> clearForm());
        deleteButton.setOnAction(e -> deleteBill());
        receiptButton.setOnAction(e -> showReceipt());

        HBox buttons = new HBox(10, calculateButton, saveButton, clearButton, deleteButton, receiptButton);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(14, 0, 0, 0));
        buttons.setMinHeight(48);

        VBox billingCard = new VBox(8, makeSectionTitle("Guest Billing"), form, buttons);
        billingCard.setPadding(new Insets(20));
        billingCard.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        billTable = new TableView<>();
        billTable.setItems(bills);
        billTable.setPlaceholder(new Label("No billing records saved yet."));
        billTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Bill, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Bill, String> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Bill, String> roomColumn = new TableColumn<>("Room Type");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));

        TableColumn<Bill, Integer> nightsColumn = new TableColumn<>("Nights");
        nightsColumn.setCellValueFactory(new PropertyValueFactory<>("nights"));

        TableColumn<Bill, String> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Bill, String> paidColumn = new TableColumn<>("Paid");
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));

        TableColumn<Bill, String> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        billTable.getColumns().addAll(idColumn, customerColumn, roomColumn,
                nightsColumn, totalColumn, paidColumn, balanceColumn);
        billTable.setPrefHeight(250);
        billTable.setMinHeight(180);

        VBox recordsCard = new VBox(12, makeSectionTitle("Billing Records"), billTable);
        recordsCard.setPadding(new Insets(20));
        recordsCard.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        VBox.setVgrow(billTable, Priority.ALWAYS);

        VBox content = new VBox(18, billingCard, recordsCard);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CREAM + ";");
        VBox.setVgrow(recordsCard, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + CREAM + "; -fx-background-color: " + CREAM + ";");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(scrollPane);

        stage.setScene(new Scene(root, 1100, 800));
    }

    private Label makeLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: " + NAVY + ";");
        return label;
    }

    private Label makeSectionTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + NAVY + ";");
        return label;
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: " + GOLD + "; -fx-text-fill: " + NAVY
                + "; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 9 14 9 14; -fx-cursor: hand;");
    }

    private double getRoomRate() {
        if (roomBox == null || roomBox.getValue() == null) {
            return 500;
        }
        switch (roomBox.getValue()) {
            case "Deluxe":
                return 800;
            case "Executive Suite":
                return 1200;
            default:
                return 500;
        }
    }

    private double readNumber(TextField field, String fieldName) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (!Double.isFinite(value) || value < 0) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid non-negative number.");
        }
    }

    private void calculateBill() {
        try {
            double nightsValue = readNumber(nightsField, "Number of nights");
            if (nightsValue < 1 || nightsValue != Math.floor(nightsValue)) {
                showMessage("Number of nights must be a whole number of at least 1.");
                return;
            }
            double paid = readNumber(paidField, "Amount paid");
            double total = nightsValue * getRoomRate();
            totalLabel.setText(money(total));
            balanceLabel.setText(money(total - paid));
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void saveBill() {
        String customer = customerField.getText().trim();
        if (customer.isEmpty()) {
            showMessage("Please enter the customer's name.");
            return;
        }

        try {
            double nightsValue = readNumber(nightsField, "Number of nights");
            if (nightsValue < 1 || nightsValue != Math.floor(nightsValue)) {
                showMessage("Number of nights must be a whole number of at least 1.");
                return;
            }
            int nights = (int) nightsValue;
            double paid = readNumber(paidField, "Amount paid");
            double total = nights * getRoomRate();
            double balance = total - paid;

            Bill bill = new Bill(bills.size() + 1, customer, roomBox.getValue(),
                    nights, money(total), money(paid), money(balance));
            bills.add(bill);
            showMessage("Bill saved successfully.");
            clearForm();
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void showReceipt() {
        Bill selectedBill = billTable.getSelectionModel().getSelectedItem();

        if (selectedBill == null) {
            showMessage("Select a saved billing record before creating a receipt.");
            return;
        }

        String receiptText =
                "          MOONLIGHT LODGE\n" +
                        "        GUEST PAYMENT RECEIPT\n" +
                        "--------------------------------------\n" +
                        "Receipt No:   " + selectedBill.getId() + "\n" +
                        "Customer:     " + selectedBill.getCustomer() + "\n" +
                        "Room Type:    " + selectedBill.getRoom() + "\n" +
                        "Nights:       " + selectedBill.getNights() + "\n" +
                        "--------------------------------------\n" +
                        "Total Bill:   " + selectedBill.getTotal() + "\n" +
                        "Amount Paid:  " + selectedBill.getPaid() + "\n" +
                        "Balance Due:  " + selectedBill.getBalance() + "\n" +
                        "--------------------------------------\n" +
                        "Thank you for choosing Moonlight Lodge!\n" +
                        "We look forward to welcoming you again.";

        TextArea receiptArea = new TextArea(receiptText);
        receiptArea.setEditable(false);
        receiptArea.setWrapText(true);
        receiptArea.setPrefColumnCount(32);
        receiptArea.setPrefRowCount(15);
        receiptArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");

        Button printButton = new Button("Print Receipt");
        styleButton(printButton);

        VBox receiptContent = new VBox(12, receiptArea, printButton);
        receiptContent.setPadding(new Insets(15));
        receiptContent.setAlignment(Pos.CENTER);
        receiptContent.setStyle("-fx-background-color: " + CREAM + ";");
        VBox.setVgrow(receiptArea, Priority.ALWAYS);

        Stage receiptStage = new Stage();
        receiptStage.setTitle("MOONLIGHT LODGE - Receipt");
        receiptStage.initOwner(stage);
        receiptStage.setScene(new Scene(receiptContent, 400, 470));

        printButton.setOnAction(e -> {
            javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
            if (job == null) {
                showMessage("No printer is available.");
                return;
            }

            TextArea printableReceipt = new TextArea(receiptText);
            printableReceipt.setEditable(false);
            printableReceipt.setWrapText(true);
            printableReceipt.setPrefColumnCount(32);
            printableReceipt.setPrefRowCount(15);
            printableReceipt.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

            if (job.showPrintDialog(receiptStage)) {
                if (job.printPage(printableReceipt)) {
                    job.endJob();
                    showMessage("Receipt sent to the printer.");
                } else {
                    showMessage("The receipt could not be printed.");
                }
            }
        });

        receiptStage.showAndWait();
    }

    private void deleteBill() {
        Bill selectedBill = billTable.getSelectionModel().getSelectedItem();
        if (selectedBill == null) {
            showMessage("Select a billing record in the table before deleting.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Billing Record");
        confirmation.setHeaderText("Delete the selected record?");
        confirmation.setContentText("Customer: " + selectedBill.getCustomer());

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bills.remove(selectedBill);
                for (int i = 0; i < bills.size(); i++) {
                    bills.get(i).setId(i + 1);
                }
                billTable.refresh();
                showMessage("Billing record deleted.");
            }
        });
    }

    private void clearForm() {
        customerField.clear();
        nightsField.clear();
        paidField.clear();
        roomBox.setValue("Standard");
        totalLabel.setText("K0.00");
        balanceLabel.setText("K0.00");
    }

    private String money(double amount) {
        return String.format("K%.2f", amount);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("MOONLIGHT LODGE");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Bill {
        private int id;
        private final String customer;
        private final String room;
        private final int nights;
        private final String total;
        private final String paid;
        private final String balance;

        public Bill(int id, String customer, String room, int nights,
                    String total, String paid, String balance) {
            this.id = id;
            this.customer = customer;
            this.room = room;
            this.nights = nights;
            this.total = total;
            this.paid = paid;
            this.balance = balance;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getCustomer() { return customer; }
        public String getRoom() { return room; }
        public int getNights() { return nights; }
        public String getTotal() { return total; }
        public String getPaid() { return paid; }
        public String getBalance() { return balance; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}



