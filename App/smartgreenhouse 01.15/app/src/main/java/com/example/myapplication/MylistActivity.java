package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MylistActivity extends AppCompatActivity {

    private RecyclerView horizontalList;
    private HorizontalListAdapter horizontalAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);

        // horizontalList = (RecyclerView)findViewById(R.id.);

        horizontalList.setHasFixedSize(true);

        //set horizontal LinearLayout as layout manager to creating horizontal list view
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalList.setLayoutManager(horizontalManager);
        horizontalAdapter = new HorizontalListAdapter(this);
        horizontalList.setAdapter(horizontalAdapter);
    }
}