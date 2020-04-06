package nl.lorenzostolk.ti22_csd_locationaware.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;
import nl.lorenzostolk.ti22_csd_locationaware.Model.LocationEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WhenWhere implements Serializable {

    private LocationEnum location;
    private LocalDateTime arrival;
    private LocalDateTime departure;
    private int totalSecondsSpendDuringStay;

    public WhenWhere(LocationEnum location, LocalDateTime arrival, LocalDateTime departure) {
        this.location = location;
        this.arrival = arrival;
        this.departure = departure;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeArrivalAndDeparture(){
        return arrival.getDayOfMonth() + "-" + arrival.getMonthValue() + " " + arrival.toLocalTime()
                + " - " + departure.getDayOfMonth() + "-" + departure.getMonthValue() + " " + departure.toLocalTime();
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

    public int getTotalSecondsSpendDuringStay() {
        return totalSecondsSpendDuringStay;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeSpend() {
        int totalSecondsSpend = 0;
        if(arrival.getDayOfYear() == departure.getDayOfYear()) {
            //Calculate seconds of arrival
            int totalSecondsArrival = (arrival.getHour() * 3600) + (arrival.getMinute() * 60)
                    + (arrival.getSecond());
            int totalSecondsDeparture = (departure.getHour() * 3600) + (departure.getMinute() * 60)
                    + (departure.getSecond());
            //Calculate total time visit
            totalSecondsSpend = totalSecondsDeparture - totalSecondsArrival;

        } else if(departure.getDayOfYear() - arrival.getDayOfYear() >= 2){
            //Visit of more days in a row
            int daysStayLeft = departure.getDayOfYear() -arrival.getDayOfYear();
            //Calculate minutes in a day
            int secondesTillMidnightDayOne = 86400 - ((arrival.getHour()*3600)
                    + (arrival.getMinute()*60) + arrival.getSecond());
            totalSecondsSpend = secondesTillMidnightDayOne;
            if(daysStayLeft > 1){
                //86400 seconds in 24 full hours
                totalSecondsSpend = totalSecondsSpend + 86400;
            }
            //Calculate seconds last day of visit
            int secondesFromMidnightTillDeparture = ((departure.getHour()*3600)
                    + (departure.getMinute()*60) + departure.getSecond());
            totalSecondsSpend = totalSecondsSpend + secondesFromMidnightTillDeparture;
        } else {
            //Visit with one night
            int secondesTillMidnightArrival = 86400 - ((arrival.getHour()*3600)
                    + (arrival.getMinute()*60) + arrival.getSecond());
            int secondesAfterMidnightTillDeparture = ((departure.getHour()*3600)
                    + (departure.getMinute()*60) + departure.getSecond());
            totalSecondsSpend = secondesTillMidnightArrival + secondesAfterMidnightTillDeparture;
        }

        this.totalSecondsSpendDuringStay = totalSecondsSpend;
        //Format time for output
        int secondsWhoDidnotFitInHours = totalSecondsSpend % 3600;
        int hoursSpend = (totalSecondsSpend - secondsWhoDidnotFitInHours) / 3600;
        int secondsWhoDidnotFitInMinutes = secondsWhoDidnotFitInHours % 60;
        int minutesSpend = (secondsWhoDidnotFitInHours - secondsWhoDidnotFitInMinutes) / 60;
        int secondsSpend = secondsWhoDidnotFitInMinutes;
        
        String hours = String.format("%02d", hoursSpend);
        String minutes = String.format("%02d", minutesSpend);
        String seconds = String.format("%02d", secondsSpend);

        return (hours + ":" + minutes + ":" + seconds);
    }
}


