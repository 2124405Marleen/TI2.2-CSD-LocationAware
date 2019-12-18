package nl.lorenzostolk.ti22_csd_locationaware;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //AVANS SCHOOL TIME INVESTMENT by Lorenzo en Marleen 2019

    private Button buttonToMap;
    private RecyclerView recyclerView;
    private StatisticsAdapter statisticsAdapter;
    private ArrayList<WhenWhere> whenWhereList;

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
        statisticsAdapter = new StatisticsAdapter(whenWhereList);
        recyclerView.setAdapter(statisticsAdapter);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTestDataForRecyclerView() {
        whenWhereList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            whenWhereList.add(new WhenWhere(LocationEnum.LOVENSDIJKSTRAAT, LocalDateTime.of(2019, 12, 18, 10, 35, 07),
                    LocalDateTime.of(2019, 12, 20, 13, 00, 00)));
            whenWhereList.add(new WhenWhere(LocationEnum.HOGESCHOOLLAAN, LocalDateTime.now(), LocalDateTime.now()));
        }
    }
}
