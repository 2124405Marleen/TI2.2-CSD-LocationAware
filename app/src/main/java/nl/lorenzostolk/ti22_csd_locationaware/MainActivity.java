package nl.lorenzostolk.ti22_csd_locationaware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //AVANS SCHOOL TIME INVESTMENT by Lorenzo en Marleen 2019

    private Button buttonToMap;
    private RecyclerView recyclerView;
    private StatisticsAdapter statisticsAdapter;
    private ArrayList<WhenWhere> whenWhereList;

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
    private void initTestDataForRecyclerView() {
        whenWhereList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            whenWhereList.add(new WhenWhere(LocationEnum.HOGESCHOOLLAAN, 10.0));
            whenWhereList.add(new WhenWhere(LocationEnum.LOVENSDIJKSTRAAT, 10.0));
        }
    }
}
