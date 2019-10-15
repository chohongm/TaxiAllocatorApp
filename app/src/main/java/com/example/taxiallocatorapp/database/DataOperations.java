package com.example.taxiallocatorapp.database;

import com.example.taxiallocatorapp.classes.Driver;
import com.example.taxiallocatorapp.classes.Rider;
import com.example.taxiallocatorapp.classes.User;

import java.text.SimpleDateFormat;
import java.util.Date;

// Combinations of multiple table operations based on activity needs.
public class DataOperations {

    private DatabaseHelper dbHelper;

    public DataOperations(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // A new user is created and is also a new rider.
    public long createUserAsRider(final String username, final String password) {

        User user = dbHelper.getUser(username);
        if(user != null) {
            System.out.println("User already exists.");
            return -1;
        }

        // *Note that we need to transactionalize the two writes and the return value should be
        // based on the success.
        long riderId = dbHelper.insertRider(username);
        return dbHelper.insertUserAsRider(username, password, riderId);
    }

    // A new user is created and is also a new driver.
    public long createUserAsDriver(final String username, final String password) {

        User user = dbHelper.getUser(username);
        if(user != null) {
            System.out.println("User already exists.");
            return -1;
        }

        // *Note that we need to transactionalize the following two write operations and
        // the return value should be based on the success.
        long driverId = dbHelper.insertDriver(username);
        return dbHelper.insertUserAsDriver(username, password, driverId);
    }

    // Rider creates a new request.
    public long createRideRequest(final long riderId, final String location) {

        // Rider must exist.
        Rider rider = dbHelper.getRider(riderId);
        if (rider == null) {
            System.out.println("Rider does not exist.");
            return -1;
        }

        // Rider should not already be associated with another request.
        Long riderRequestId = rider.getRequestId();
        if(riderRequestId != null) {
            System.out.println("Rider is already on another request. RequestId: " +
                    riderRequestId);
            return -1;
        }

        // *Note that we need to transactionalize the following two write operations and
        // the return value should be based on the success.
        long requestTimestamp = System.currentTimeMillis(); // epoch in milliseconds (UTC time).
        long rideRequestId = dbHelper.insertRideRequest(riderId, requestTimestamp, location);

        // Associate the rider with the new request.
        dbHelper.updateRider(riderId, rideRequestId);

        return rideRequestId;
    }

    // Driver accepts a request.
    public long acceptRideRequest(final long driverId, final long requestId) {

        // Driver must exist.
        Driver driver = dbHelper.getDriver(driverId);
        if (driver == null) {
            System.out.println("Driver does not exist.");
            return -1;
        }

        // Driver should not already be associated with another request.
        Long driverRequestId = driver.getRequestId();
        if(driverRequestId != null) {
            System.out.println("Driver is already on another request. RequestId: " +
                    driverRequestId);
            return -1;
        }

        // *Note that we need to transactionalize the following two write operations and
        // the return value should be based on the success.
        long acceptedTimestamp = System.currentTimeMillis(); // epoch in milliseconds (UTC time).
        long res = dbHelper.updateRideRequest(requestId, driverId, acceptedTimestamp);
        if (res >= 0) {
            dbHelper.updateDriver(driverId, requestId);
            return requestId;
        }

        return -1;
    }

    public boolean isUsernameValid(String username) {
        return !username.isEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    public String epochToStringConverter(Long timestamp) {
        if (timestamp == null) {
            return "N/A";
        }
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(timestamp));
    }
}
