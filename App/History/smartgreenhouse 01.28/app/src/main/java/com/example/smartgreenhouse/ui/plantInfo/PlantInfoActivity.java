package com.example.smartgreenhouse.ui.plantInfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.smartgreenhouse.R;

import java.util.ArrayList;

public class PlantInfoActivity extends AppCompatActivity {

    ListView listView;
    PlantInfoAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<PlantInfoModel> arrayList = new ArrayList<PlantInfoModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("식물 도감");

        title = new String[]{"Plant1", "Plant2", "Plant3", "Plant4", "Plant5", "Plant6"};
        description = new String[]{"Plant detail...", "Plant detail...", "Plant detail...", "Plant detail...", "Plant detail...", "Plant detail..."};
        icon = new int[]{R.drawable.c1, R.drawable.c1, R.drawable.c1, R.drawable.c1, R.drawable.c1, R.drawable.c1};

        listView = findViewById(R.id.listView);

        for (int i =0; i<title.length; i++){
            PlantInfoModel model = new PlantInfoModel(title[i], description[i], icon[i]);
            //bind all strings in an array
            arrayList.add(model);
        }

        //pass results to listViewAdapter class
        adapter = new PlantInfoAdapter(this, arrayList);

        //bind the adapter to the listview
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }
}
