package nl.lorenzostolk.ti22_csd_locationaware.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.lorenzostolk.ti22_csd_locationaware.Model.LocationEnum;
import nl.lorenzostolk.ti22_csd_locationaware.Model.SPB;
import nl.lorenzostolk.ti22_csd_locationaware.R;
import nl.lorenzostolk.ti22_csd_locationaware.Controller.StatisticsAdapter;
import nl.lorenzostolk.ti22_csd_locationaware.Model.WhenWhere;
import nl.lorenzostolk.ti22_csd_locationaware.Model.WorkingWeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //AVANS SCHOOL TIME INVESTMENT by Lorenzo en Marleen 2019

    private Button buttonToMap;
    private RecyclerView recyclerView;
    private StatisticsAdapter statisticsAdapter;
    private ArrayList<WorkingWeek> weeks;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTestDataForRecyclerView();
        initButtonToMap();
        initRecyclerView();
    }

    public void initButtonToMap(){
        buttonToMap = findViewById(R.id.main_button_map);
        buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.StatisticRecyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        statisticsAdapter = new StatisticsAdapter(weeks);
        recyclerView.setAdapter(statisticsAdapter);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTestDataForRecyclerView() {
        ArrayList<WhenWhere> whenWhers = new ArrayList();
        whenWhers.add(new WhenWhere(LocationEnum.LOVENSDIJKSTRAAT,
                LocalDateTime.of(2019, 12, 12, 10, 11, 00),
                LocalDateTime.of(2019, 12, 13, 12, 11, 11)));
        weeks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            weeks.add(new WorkingWeek(1, 12, whenWhers, 2019));
        }
    }
}
