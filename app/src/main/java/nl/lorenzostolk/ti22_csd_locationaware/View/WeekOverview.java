package nl.lorenzostolk.ti22_csd_locationaware.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.lorenzostolk.ti22_csd_locationaware.Model.WhenWhere;
import nl.lorenzostolk.ti22_csd_locationaware.Model.WorkingWeek;
import nl.lorenzostolk.ti22_csd_locationaware.R;
import nl.lorenzostolk.ti22_csd_locationaware.Controller.WeekAdapter;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class WeekOverview extends AppCompatActivity {

    private TextView textYear;
    private TextView textWeekNumber;
    private RecyclerView hourOverviewThisWeek;
    private WeekAdapter weekAdapter;
    private ArrayList<WhenWhere> whenWhereList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_overview);


        this.textYear = findViewById(R.id.Week_overview_text_year);
        this.hourOverviewThisWeek = findViewById(R.id.Week_overview_hours_list);
        this.textWeekNumber = findViewById(R.id.Week_overview_text_week);

        WorkingWeek week = (WorkingWeek) getIntent().getSerializableExtra("WEEK");
        this.whenWhereList = week.getDays();

        this.textYear.setText(String.valueOf(week.getYear()));
        this.textWeekNumber.setText("Week: " + week.getWeekNumber());
        initRecyclerView();
    }

    private void initRecyclerView(){
        hourOverviewThisWeek = findViewById(R.id.Week_overview_hours_list);
        hourOverviewThisWeek.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
        weekAdapter = new WeekAdapter(whenWhereList);
        hourOverviewThisWeek.setAdapter(weekAdapter);
    }
}
