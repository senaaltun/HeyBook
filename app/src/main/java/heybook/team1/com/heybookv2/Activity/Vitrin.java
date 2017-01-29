package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.Adapter.VitrinBookAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vitrin extends BaseActivity {
    private RecyclerView recyclerView;
    private VitrinBookAdapter adapter;
    private ArrayList<Data> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrin);


        getBookData();


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        recyclerView = (RecyclerView)findViewById(R.id.bookList);


    }


    public void getBookData(){

        ApiClientInterface apiService =
                ApiClient.getClient().create(ApiClientInterface.class);

        Call<Book> call = apiService.getAllBooks();

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                List<Data> data = book.getData();

                dataList = new ArrayList<>(data);

                final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new VitrinBookAdapter(Vitrin.this, dataList);

                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("MyApp", "onFailure: " + t.toString());
            }
        });
    }
}
