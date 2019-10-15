package com.example.taxiallocatorapp.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class RideRequest {

    private long requestId;
    private long riderId;
    private long requestTimestamp;
    private String location;

    // Nullable attributes.
    private Long driverId;
    private Long acceptedTimestamp;
    private Long completedTimestamp;

    public RideRequest(long requestId, long riderId, long requestTimestamp, String location,
                       Long driverId, Long acceptedTimestamp, Long completedTimestamp) {
        this.requestId = requestId;
        this.riderId = riderId;
        this.requestTimestamp = requestTimestamp;
        this.location = location;
        this.driverId = driverId;
        this.acceptedTimestamp = acceptedTimestamp;
        this.completedTimestamp = completedTimestamp;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getRiderId() {
        return riderId;
    }

    public void setRiderId(long riderId) {
        this.riderId = riderId;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getAcceptedTimestamp() {
        return acceptedTimestamp;
    }

    public void setAcceptedTimestamp(Long acceptedTimestamp) {
        this.acceptedTimestamp = acceptedTimestamp;
    }

    public Long getCompletedTimestamp() {
        return completedTimestamp;
    }

    public void setCompletedTimestamp(Long completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "requestId=" + requestId +
                ", riderId=" + riderId +
                ", requestTimestamp=" + requestTimestamp +
                ", location='" + location + '\'' +
                ", driverId=" + driverId +
                ", acceptedTimestamp=" + acceptedTimestamp +
                ", completedTimestamp=" + completedTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RideRequest that = (RideRequest) o;
        return requestId == that.requestId &&
                riderId == that.riderId &&
                requestTimestamp == that.requestTimestamp &&
                driverId == that.driverId &&
                acceptedTimestamp == that.acceptedTimestamp &&
                completedTimestamp == that.completedTimestamp &&
                location.equals(that.location);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(requestId, riderId, requestTimestamp, location, driverId, acceptedTimestamp, completedTimestamp);
    }
}
