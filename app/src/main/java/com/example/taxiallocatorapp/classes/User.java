package com.example.taxiallocatorapp.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class User {

    private String username;
    private String password;
    private Long riderId;
    private Long driverId;

    public User(String username, String password, Long riderId, Long driverId) {
        this.username = username;
        this.password = password;
        this.riderId = riderId;
        this.driverId = driverId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", riderId=" + riderId +
                ", driverId=" + driverId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return riderId == user.riderId &&
                driverId == user.driverId &&
                username.equals(user.username) &&
                password.equals(user.password);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(username, riderId, driverId);
    }
}
