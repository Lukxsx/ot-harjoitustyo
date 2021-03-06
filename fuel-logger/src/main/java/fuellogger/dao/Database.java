package fuellogger.dao;

import fuellogger.domain.Car;
import fuellogger.domain.Refueling;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class manages the SQLite database. It converts Car and Refueling objects
 * to database and vice versa.
 */
public class Database {

    private Connection db;
    private boolean error;

    public Database(String dbName) {
        this.error = false;
        this.db = null;
        try {
            db = DriverManager.getConnection("jdbc:sqlite:" + dbName);

            Statement s = db.createStatement();
            s.execute("CREATE TABLE IF NOT EXISTS Car (id INTEGER PRIMARY KEY, name TEXT UNIQUE,"
                    + " fuel_capacity INTEGER NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS Refueling (id INTEGER PRIMARY KEY, "
                    + "car_id INTEGER, odometer INTEGER UNIQUE, volume REAL"
                    + ", price REAL, day INTEGER, month INTEGER, year INTEGER)");
        } catch (SQLException e) {
            this.error = true;
        }

    }

    /**
     * Adds a Car object to the database.
     *
     * @param car a car object to be added
     *
     * @return returns true if car was added successfully to the database
     */
    public boolean addCar(Car car) {
        try (PreparedStatement s = db.prepareStatement("INSERT INTO Car (name, fuel_capacity) VALUES (?, ?)")) {
            s.setString(1, car.getName());
            s.setInt(2, car.getFuelcapacity());
            s.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Returns an ArrayList of all cars in the database.
     *
     * @return a list of cars in the database
     */
    public ArrayList<Car> getCars() {
        ArrayList<Car> cars = new ArrayList<>();
        try {
            PreparedStatement carQuery = db.prepareStatement("SELECT * FROM Car");
            ResultSet carResults = carQuery.executeQuery();
            while (carResults.next()) {
                cars.add(new Car(carResults.getString("name"),
                        carResults.getInt("fuel_capacity")));
            }
        } catch (SQLException e) {
            
        }
        return cars;
    }

    /**
     * Adds a Refueling to the database
     *
     * @param refueling refueling object to be added
     * @return returns true if adding was successful
     */
    public boolean addRefueling(Refueling refueling) {
        int carid = getCarId(refueling.car);
        if (carid == 0) {
            return false;
        }
        try (PreparedStatement s = db.prepareStatement("INSERT INTO Refueling "
                + "(car_id, odometer, volume, price, day, month, year) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)")) {
            
            s.setInt(1, carid);
            s.setInt(2, refueling.odometer);
            s.setDouble(3, refueling.volume);
            s.setDouble(4, refueling.price);
            s.setInt(5, refueling.date.getDayOfMonth());
            s.setInt(6, refueling.date.getMonthValue());
            s.setInt(7, refueling.date.getYear());
            
            s.executeUpdate();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Returns id of a car (in the database)
     *
     * @param car the car to find the id
     * @return returns the id or zero if the car could not be found
     */
    public int getCarId(Car car) {
        try {
            PreparedStatement carIdQuery = db.prepareStatement("SELECT id FROM Car WHERE name=?");
            carIdQuery.setString(1, car.getName());
            ResultSet idQueryResults = carIdQuery.executeQuery();
            if (idQueryResults.next()) {
                return idQueryResults.getInt("id");
            }
        } catch (SQLException e) {
            // method returns zerp if error
        }
        return 0;
    }

    /**
     * Get a car by it's database id
     *
     * @param id id you want to get
     * @return returns the car or null if no car with the id could be found
     */
    public Car getCar(int id) {
        try {
            PreparedStatement carQuery = db.prepareStatement("SELECT * FROM Car WHERE id=?");
            carQuery.setInt(1, id);
            ResultSet carQresults = carQuery.executeQuery();
            if (carQresults.next()) {
                return new Car(carQresults.getString("name"), carQresults.getInt("fuel_capacity"));
            }
        } catch (SQLException e) {
            // return null if error
        }
        return null;
    }

    /**
     * Returns a list of all refuelings in the database
     *
     * @return list of all refuelings
     */
    public ArrayList<Refueling> getRefuelings() {
        ArrayList<Refueling> refuelings = new ArrayList<>();
        try {
            PreparedStatement refQuery = db.prepareStatement("SELECT * FROM Refueling");
            ResultSet refResults = refQuery.executeQuery();
            while (refResults.next()) {
                LocalDate d = LocalDate.of(refResults.getInt("year"),
                        refResults.getInt("month"), refResults.getInt("day"));
                refuelings.add(new Refueling(getCar(refResults.getInt("car_id")),
                        refResults.getInt("odometer"),
                        refResults.getDouble("volume"),
                        refResults.getDouble("price"), d));
            }
        } catch (SQLException e) {
            // method returns just empty arraylist if error
        }
        return refuelings;
    }

    /**
     * Returns a list of refuelings of a specified car
     *
     * @param car car to get refuelings from
     * @return refuelings of a car
     */
    public ArrayList<Refueling> getRefuelings(Car car) {
        ArrayList<Refueling> refuelings = new ArrayList<>();
        try {
            PreparedStatement refQuery = db.prepareStatement("SELECT * FROM Refueling WHERE car_id=?");
            refQuery.setInt(1, getCarId(car));
            ResultSet refResults = refQuery.executeQuery();
            while (refResults.next()) {
                LocalDate d = LocalDate.of(refResults.getInt("year"),
                        refResults.getInt("month"), refResults.getInt("day"));
                Refueling r = new Refueling(car, refResults.getInt("odometer"),
                        refResults.getDouble("volume"), refResults.getDouble("price"), d);
                refuelings.add(r);
            }
        } catch (SQLException e) {
            // method returns just empty arraylist if error
        }

        return refuelings;
    }

    /**
     * Clears the database of all cars and refuelings. Used for tests.
     */
    public boolean clear() {
        try {
            Statement s = db.createStatement();
            s.execute("DELETE FROM Car");
            s.execute("DELETE FROM Refueling");
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if there was an error at initializing the database
     *
     * @return returns true, if there was an error
     */
    public boolean isError() {
        return error;
    }

}
