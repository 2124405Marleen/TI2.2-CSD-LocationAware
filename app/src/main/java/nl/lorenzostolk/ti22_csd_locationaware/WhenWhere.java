package nl.lorenzostolk.ti22_csd_locationaware;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WhenWhere implements Serializable {

    private LocationEnum location;
    private LocalDateTime arrival;
    private LocalDateTime departure;
    private double timeSpend;

    public WhenWhere(LocationEnum location, LocalDateTime arrival, LocalDateTime departure, double timeSpend) {
        this.location = location;
        this.arrival = arrival;
        this.departure = departure;
        this.timeSpend = timeSpend;
    }

    public WhenWhere(LocationEnum location, double timeSpend) {
        this.location = location;
        this.timeSpend = timeSpend;
    }

    public LocationEnum getLocation() {
        return location;
    }

    public void setLocation(LocationEnum location) {
        this.location = location;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public double getTimeSpend() {
        return timeSpend;
    }

    public void setTimeSpend(double timeSpend) {
        this.timeSpend = timeSpend;
    }
}
