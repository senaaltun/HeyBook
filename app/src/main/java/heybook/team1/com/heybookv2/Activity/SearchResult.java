package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import heybook.team1.com.heybookv2.R;
import java.util.ArrayList;

import heybook.team1.com.heybookv2.Adapter.ResultBookAdapter;
import heybook.team1.com.heybookv2.Model.Data;

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