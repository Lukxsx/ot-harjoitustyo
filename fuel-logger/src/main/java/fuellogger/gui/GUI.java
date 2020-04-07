package fuellogger.gui;

import fuellogger.domain.Car;
import fuellogger.domain.Logic;
import fuellogger.domain.Refueling;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    private Logic l;
    Scene refuelingsScene;
    Scene carSelectScene;
    Car currentCar;

    @Override
    public void start(Stage primaryStage) throws Exception {

        l = new Logic("database.db");
        this.currentCar = null;
        // window settings
        primaryStage.setTitle("Fuel logger");

        /*
        *
        *   Car selection view
        *
         */
        VBox carSelectLayout = new VBox();

        Label carSelectInfo = new Label("Select a car to use");
        carSelectLayout.getChildren().add(carSelectInfo);

        // tableview for car selection
        TableView carSelect = new TableView();
        carSelect.setEditable(true);

        TableColumn<String, Car> csNameColumn = new TableColumn("Car");
        csNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //TableColumn csOdometerColumn = new TableColumn("Odometer");
        carSelect.getColumns().addAll(csNameColumn);

        // date for table
        ObservableList<Car> carData = FXCollections.observableArrayList();

        // get cars from the database
        ArrayList<Car> cars = l.cars;
        for (Car c : cars) {
            carData.add(c);
        }
        carSelect.setItems(carData);

        // car selection button
        TableViewSelectionModel<Car> CSselectionModel = carSelect.getSelectionModel();
        CSselectionModel.setSelectionMode(SelectionMode.SINGLE);
        Button csSelectButton = new Button("Select");
        csSelectButton.setOnAction((ActionEvent e) -> {
            ObservableList<Car> selectedCar = CSselectionModel.getSelectedItems();
            if (!(selectedCar.size() == 0)) {
                System.out.println(selectedCar.get(0));
                currentCar = selectedCar.get(0);
                System.out.println(currentCar);
                try {
                    refuelingsScene = refuelScene();
                } catch (SQLException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                primaryStage.setScene(refuelingsScene);
            }
        });

        // car adding dialog
        TextField csNameField = new TextField();
        csNameField.setPromptText("Name");
        TextField csFuelCField = new TextField();
        csFuelCField.setPromptText("Fuel capacity");
        Button csAddButton = new Button("Add");

        csAddButton.setOnAction((ActionEvent e) -> {
            Car c = new Car(csNameField.getText(), Integer.valueOf(csFuelCField.getText()));
            System.out.println(c);
            l.addCar(c);
            carData.add(c);
            csNameField.clear();
            csFuelCField.clear();
        });

        Label csAddInfo = new Label("Add a new car");
        HBox csAddLayout = new HBox();
        csAddLayout.getChildren().addAll(csNameField, csFuelCField, csAddButton);
        csAddLayout.setSpacing(5);

        // car select layout
        carSelectLayout.setSpacing(10);
        carSelectLayout.getChildren().add(carSelect);
        carSelectLayout.getChildren().add(csSelectButton);
        carSelectLayout.getChildren().add(csAddInfo);
        carSelectLayout.getChildren().add(csAddLayout);

        carSelectScene = new Scene(carSelectLayout);

        refuelingsScene = null;

        primaryStage.setScene(carSelectScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Scene refuelScene() throws SQLException {
        /*
        *
        *   Refuelings view
        *
         */

        VBox refuelLayout = new VBox();

        HBox refuelTopLayout = new HBox();
        refuelTopLayout.setSpacing(20);

        Label rfCarLabel = new Label();
        rfCarLabel.setText("Selected car: " + currentCar.getName());

        Label rfAvgConsumption = new Label();
        double avg = l.avgConsumption(currentCar);
        DecimalFormat df = new DecimalFormat("#.##");
        rfAvgConsumption.setText("Average consumption: " + df.format(avg) + " l/100km");
        refuelTopLayout.getChildren().add(rfCarLabel);
        refuelTopLayout.getChildren().add(rfAvgConsumption);

        TableView refills = new TableView();
        refills.setEditable(true);

        TableColumn<Integer, Refueling> rfOdometerColumn = new TableColumn("Odometer");
        rfOdometerColumn.setCellValueFactory(new PropertyValueFactory<>("odometer"));
        TableColumn<Double, Refueling> rfVolumeColumn = new TableColumn("Volume");
        rfVolumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));

        refills.getColumns().addAll(rfOdometerColumn, rfVolumeColumn);

        // date for table
        ObservableList<Refueling> refuelingData = FXCollections.observableArrayList();

        // get cars from the database
        ArrayList<Refueling> ref = l.getRefuelings(currentCar);
        for (Refueling r : ref) {
            refuelingData.add(r);
        }
        refills.setItems(refuelingData);

        refuelLayout.getChildren().add(refuelTopLayout);
        refuelLayout.getChildren().add(refills);
        Scene rfs = new Scene(refuelLayout);
        return rfs;
    }

}
