package com.example.taxiallocatorapp.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Rider {

    private long riderId;
    private String username;
    private Long requestId;

    public Rider(long riderId, String username, Long requestId) {
        this.riderId = riderId;
        this.username = username;
        this.requestId = requestId;
    }

    public long getRiderId() {
        return riderId;
    }

    public void setRiderId(long riderId) {
        this.riderId = riderId;
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
        return "Rider{" +
                "riderId=" + riderId +
                ", username='" + username + '\'' +
                ", requestId=" + requestId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rider rider = (Rider) o;
        return riderId == rider.riderId &&
                requestId == rider.requestId &&
                username.equals(rider.username);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(riderId, username, requestId);
    }
}
