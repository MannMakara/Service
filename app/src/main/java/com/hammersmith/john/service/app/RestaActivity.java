package com.hammersmith.john.service.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.hammersmith.john.service.R;

public class RestaActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spa);

        listView = (ListView) findViewById(R.id.list_pro);

        String[] location = {"Kompong Som","Phnom Penh","Siem Reap"};
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),location);
        listView.setAdapter(customAdapter);
    }

}
