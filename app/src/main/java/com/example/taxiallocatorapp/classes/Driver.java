package com.example.taxiallocatorapp.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Driver {

    private long driverId;
    private String username;
    private Long requestId;


    public Driver(long driverId, String username, Long requestId) {
        this.driverId = driverId;
        this.username = username;
        this.requestId = requestId;
    }


    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", username='" + username + '\'' +
                ", requestId=" + requestId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return driverId == driver.driverId &&
                requestId == driver.requestId &&
                username.equals(driver.username);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(driverId, username, requestId);
    }
}
