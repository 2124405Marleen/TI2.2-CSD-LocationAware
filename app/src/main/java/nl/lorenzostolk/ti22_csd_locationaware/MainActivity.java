package nl.lorenzostolk.ti22_csd_locationaware;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //AVANS SCHOOL TIME INVESTMENT by Lorenzo en Marleen 2019

    private Button buttonToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtonToMap();
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
}
