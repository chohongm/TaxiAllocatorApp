package com.example.taxiallocatorapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taxiallocatorapp.classes.Driver;
import com.example.taxiallocatorapp.classes.RideRequest;
import com.example.taxiallocatorapp.classes.Rider;
import com.example.taxiallocatorapp.classes.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.taxiallocatorapp.constants.TableConstants.ACCEPTED_TIMESTAMP;
import static com.example.taxiallocatorapp.constants.TableConstants.COMPLETED_TIMESTAMP;
import static com.example.taxiallocatorapp.constants.TableConstants.CREATE_DRIVER_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.CREATE_RIDER_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.CREATE_RIDE_REQUEST_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.CREATE_USER_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.DRIVER_ID;
import static com.example.taxiallocatorapp.constants.TableConstants.DRIVER_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.LOCATION;
import static com.example.taxiallocatorapp.constants.TableConstants.PASSWORD;
import static com.example.taxiallocatorapp.constants.TableConstants.REQUEST_ID;
import static com.example.taxiallocatorapp.constants.TableConstants.REQUEST_TIMESTAMP;
import static com.example.taxiallocatorapp.constants.TableConstants.RIDER_ID;
import static com.example.taxiallocatorapp.constants.TableConstants.RIDER_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.RIDE_REQUEST_TABLE;
import static com.example.taxiallocatorapp.constants.TableConstants.USERNAME;
import static com.example.taxiallocatorapp.constants.TableConstants.USER_TABLE;

// Basic table read, write, and update operations.
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "TAXI_ALLOCATOR.DB";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RIDER_TABLE);
        db.execSQL(CREATE_DRIVER_TABLE);
        db.execSQL(CREATE_RIDE_REQUEST_TABLE);
    }

    // Only use this when you need to reboot existing tables.
//    public void rebootTables() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + RIDER_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + DRIVER_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + RIDE_REQUEST_TABLE);
//        onCreate(db);
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RIDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DRIVER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RIDE_REQUEST_TABLE);

        onCreate(db);
    }

    // Writes a new user as a rider.
    public long insertUserAsRider(final String username, final String password,
                                  final long riderId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(PASSWORD, password);
        values.put(RIDER_ID, riderId);

        return db.insert(USER_TABLE, null, values);
    }

    // Writes a new user as a driver.
    public long insertUserAsDriver(final String username, final String password,
                                   final long driverId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(PASSWORD, password);
        values.put(DRIVER_ID, driverId);

        return db.insert(USER_TABLE, null, values);
    }

    public long insertRider(final String username) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);

        return db.insert(RIDER_TABLE, null, values);
    }

    public long insertDriver(final String username) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);

        return db.insert(DRIVER_TABLE, null, values);
    }

    public long insertRideRequest(final long riderId, final long requestTimestamp,
                                  final String location) {

        // Insert ride request.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RIDER_ID, riderId);
        values.put(REQUEST_TIMESTAMP, requestTimestamp);
        values.put(LOCATION, location);

        return db.insert(RIDE_REQUEST_TABLE, null, values);
    }

    public long updateRider(final long riderId, final long rideRequestId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_ID, rideRequestId);

        return db.update(RIDER_TABLE, values, RIDER_ID + " = ?",
                new String[]{Long.toString(riderId)});
    }

    public long updateDriver(final long driverId, final long rideRequestId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REQUEST_ID, rideRequestId);

        return db.update(DRIVER_TABLE, values, DRIVER_ID + " = ?",
                new String[]{Long.toString(driverId)});
    }

    public long updateRideRequest(final long requestId, final long driverID,
                                  final long acceptedTimestamp) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DRIVER_ID, driverID);
        values.put(ACCEPTED_TIMESTAMP, acceptedTimestamp);

        return db.update(RIDE_REQUEST_TABLE, values, REQUEST_ID + " = ?",
                new String[]{Long.toString(requestId)});
    }

    // Loads user data from user table that matches given username.
    public User getUser(String username) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + USER_TABLE + " WHERE "
                + USERNAME + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Handle nullable riderId and driverId.
            int riderIdIndex = cursor.getColumnIndex(RIDER_ID);
            int driverIdIndex = cursor.getColumnIndex(DRIVER_ID);
            Long riderId = (cursor.isNull(riderIdIndex)) ? null : cursor.getLong(riderIdIndex);
            Long driverId = (cursor.isNull(driverIdIndex)) ? null : cursor.getLong(driverIdIndex);
            user = new User(
                    cursor.getString(cursor.getColumnIndex(USERNAME)),
                    cursor.getString(cursor.getColumnIndex(PASSWORD)),
                    riderId, driverId);

            cursor.close();
        }

        return user;
    }

    // Loads the rider mathcing given riderId. Returns null if no matching rider found.
    public Rider getRider(long riderId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + RIDER_TABLE + " WHERE "
                + RIDER_ID + " = " + riderId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Rider rider = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Handle nullable fields.
            int requestIdIndex = cursor.getColumnIndex(REQUEST_ID);
            Long requestID = (cursor.isNull(requestIdIndex)) ? null : cursor.getLong(requestIdIndex);
            rider = new Rider(
                    cursor.getLong(cursor.getColumnIndex(RIDER_ID)),
                    cursor.getString(cursor.getColumnIndex(USERNAME)),
                    requestID);

            cursor.close();
        }

        return rider;
    }

    // Loads the driver mathcing given riderId. Returns null if no matching driver found.
    public Driver getDriver(long driverId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DRIVER_TABLE + " WHERE "
                + DRIVER_ID + " = " + driverId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Driver driver = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Handle nullable fields.
            int requestIdIndex = cursor.getColumnIndex(REQUEST_ID);
            Long requestID = (cursor.isNull(requestIdIndex)) ? null : cursor.getLong(requestIdIndex);
            driver = new Driver(
                    cursor.getLong(cursor.getColumnIndex(DRIVER_ID)),
                    cursor.getString(cursor.getColumnIndex(USERNAME)),
                    requestID);

            cursor.close();
        }

        return driver;
    }

    // Loads a ride request matching with given requestId.
    public RideRequest getRideRequest(final long requestId) {

        RideRequest request = null;

        String selectQuery = "SELECT  * FROM " + RIDE_REQUEST_TABLE + " WHERE "
                + REQUEST_ID + " = " + requestId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Handle nullable fields.
            int driverIdIndex = cursor.getColumnIndex(DRIVER_ID);
            int acceptedTimestampIndex = cursor.getColumnIndex(ACCEPTED_TIMESTAMP);
            int completedTimestampIndex = cursor.getColumnIndex(COMPLETED_TIMESTAMP);

            Long driverId = (cursor.isNull(driverIdIndex)) ?
                    null : cursor.getLong(driverIdIndex);
            Long acceptedTimestamp = (cursor.isNull(acceptedTimestampIndex)) ?
                    null : cursor.getLong(acceptedTimestampIndex);
            Long completedTimestamp = (cursor.isNull(completedTimestampIndex)) ?
                    null : cursor.getLong(completedTimestampIndex);

            request = new RideRequest(
                    cursor.getLong(cursor.getColumnIndex(REQUEST_ID)),
                    cursor.getLong(cursor.getColumnIndex(RIDER_ID)),
                    cursor.getLong(cursor.getColumnIndex(REQUEST_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndex(LOCATION)),
                    driverId, acceptedTimestamp, completedTimestamp);

            cursor.close();
        }

        return request;
    }

    // Gets the list of all ride requests sorted by request timestamp in descending order.
    public List<RideRequest> getAllRideRequests() {

        List<RideRequest> rideRequests = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + RIDE_REQUEST_TABLE + " ORDER BY " +
                REQUEST_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Handle nullable fields.
                int driverIdIndex = cursor.getColumnIndex(DRIVER_ID);
                int acceptedTimestampIndex = cursor.getColumnIndex(ACCEPTED_TIMESTAMP);
                int completedTimestampIndex = cursor.getColumnIndex(COMPLETED_TIMESTAMP);

                Long driverId = (cursor.isNull(driverIdIndex)) ?
                        null : cursor.getLong(driverIdIndex);
                Long acceptedTimestamp = (cursor.isNull(acceptedTimestampIndex)) ?
                        null : cursor.getLong(acceptedTimestampIndex);
                Long completedTimestamp = (cursor.isNull(completedTimestampIndex)) ?
                        null : cursor.getLong(completedTimestampIndex);

                RideRequest rideRequest = new RideRequest(
                        cursor.getLong(cursor.getColumnIndex(REQUEST_ID)),
                        cursor.getLong(cursor.getColumnIndex(RIDER_ID)),
                        cursor.getLong(cursor.getColumnIndex(REQUEST_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndex(LOCATION)),
                        driverId, acceptedTimestamp, completedTimestamp);
                rideRequests.add(rideRequest);

            } while (cursor.moveToNext());
        }
        db.close();

        return rideRequests;
    }
}
