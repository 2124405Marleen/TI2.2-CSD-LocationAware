package nl.lorenzostolk.ti22_csd_locationaware.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkingWeek implements Serializable {

    private int weekNumber;
    private int totalSeconds;
    private ArrayList<WhenWhere> days;
    private int year;

    public WorkingWeek(int weekNumber, int totalSeconds, ArrayList<WhenWhere> days, int year) {
        this.weekNumber = weekNumber;
        this.totalSeconds = totalSeconds;
        this.days = days;
        this.year = year;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public ArrayList<WhenWhere> getDays() {
        return days;
    }

    public int getYear() {
        return year;
    }
}
