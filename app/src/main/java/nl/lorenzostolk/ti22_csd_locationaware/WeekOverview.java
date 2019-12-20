package nl.lorenzostolk.ti22_csd_locationaware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.textclassifier.TextClassification;
import android.widget.TextView;
import android.widget.Toast;

public class WeekOverview extends AppCompatActivity {

    private TextView textYear;
    private TextView textWeekNumber;
    private RecyclerView hourOverviewThisWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_overview);

        this.textYear = findViewById(R.id.Week_overview_text_year);
        this.hourOverviewThisWeek = findViewById(R.id.Week_overview_hours_list);
        this.textWeekNumber = findViewById(R.id.Week_overview_text_week);

        WorkingWeek week = (WorkingWeek) getIntent().getSerializableExtra("WEEK");


        this.textYear.setText(String.valueOf(week.getYear()));
        this.textWeekNumber.setText("Week: " + week.getWeekNumber());


    }
}
