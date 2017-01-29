package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import heybook.team1.com.heybookv2.Adapter.ResultBookAdapter;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;

public class SearchResult extends BaseActivity {
    private RecyclerView resultDataRecyclerView;
    private ArrayList<Data> resultDataList;
    private ResultBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        resultDataRecyclerView = (RecyclerView)findViewById(R.id.searchResultRecyclerView);

        ArrayList<Data> resultDataList = (ArrayList<Data>)getIntent().getSerializableExtra("resultDataList");

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultDataRecyclerView.setLayoutManager(layoutManager);
        adapter = new ResultBookAdapter(SearchResult.this, resultDataList);

        resultDataRecyclerView.setAdapter(adapter);




    }
}