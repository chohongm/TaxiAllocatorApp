package com.example.taxiallocatorapp.constants;

public class TableConstants {

    // Table names
    public static final String USER_TABLE = "USER";
    public static final String DRIVER_TABLE = "DRIVER";
    public static final String RIDER_TABLE = "RIDER";
    public static final String RIDE_REQUEST_TABLE = "RIDE_REQUEST";

    // Table column names
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DRIVER_ID = "driver_id";
    public static final String REQUEST_ID = "request_id";
    public static final String RIDER_ID = "rider_id";
    public static final String REQUEST_TIMESTAMP = "request_timestamp";
    public static final String LOCATION = "location";
    public static final String ACCEPTED_TIMESTAMP = "accepted_timestamp";
    public static final String COMPLETED_TIMESTAMP = "completed_timestamp";

    // Create table queries
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " ("
            + USERNAME + " TEXT PRIMARY KEY, "
            + PASSWORD + " TEXT NOT NULL, "
            + RIDER_ID + " INTEGER, "
            + DRIVER_ID + " INTEGER, "
            + "FOREIGN KEY (" + RIDER_ID + ") REFERENCES " + RIDER_TABLE + " (" + RIDER_ID + "), "
            + "FOREIGN KEY (" + DRIVER_ID + ") REFERENCES " + DRIVER_TABLE + " (" + DRIVER_ID + ")"
            + ")";

    public static final String CREATE_DRIVER_TABLE = "CREATE TABLE " + DRIVER_TABLE + " ("
            + DRIVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT NOT NULL UNIQUE, "
            + REQUEST_ID + " INTEGER, "
            + "FOREIGN KEY (" + USERNAME + ") REFERENCES " + USER_TABLE + " (" + USERNAME + "), "
            + "FOREIGN KEY (" + REQUEST_ID + ") REFERENCES " + RIDE_REQUEST_TABLE + " (" + REQUEST_ID + ")"
            + ")";

    public static final String CREATE_RIDER_TABLE = "CREATE TABLE " + RIDER_TABLE + " ("
            + RIDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USERNAME + " TEXT NOT NULL UNIQUE, "
            + REQUEST_ID + " INTEGER, "
            + "FOREIGN KEY (" + USERNAME + ") REFERENCES " + USER_TABLE + " (" + USERNAME + "), "
            + "FOREIGN KEY (" + REQUEST_ID + ") REFERENCES " + RIDE_REQUEST_TABLE + " (" + REQUEST_ID + ")"
            + ")";

    public static final String CREATE_RIDE_REQUEST_TABLE = "CREATE TABLE " + RIDE_REQUEST_TABLE + " ("
            + REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RIDER_ID + " INTEGER NOT NULL, "
            + REQUEST_TIMESTAMP + " INTEGER NOT NULL, "
            + LOCATION + " VARCHAR (100) NOT NULL, "
            + DRIVER_ID + " INTEGER, "
            + ACCEPTED_TIMESTAMP + " INTEGER, "
            + COMPLETED_TIMESTAMP + " INTEGER, "
            + "FOREIGN KEY (" + RIDER_ID + ") REFERENCES " + RIDER_TABLE + " (" + RIDER_ID + "), "
            + "FOREIGN KEY (" + DRIVER_ID + ") REFERENCES " + DRIVER_TABLE + " (" + DRIVER_ID + ")"
            + ")";
}
