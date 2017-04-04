package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Adapter.CategoryAdapter;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category extends BaseActivity {
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Data> categoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        getCategoryDetails();

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        categoryRecyclerView = (RecyclerView)findViewById(R.id.categoryRecyclerView);
    }

    public void getCategoryDetails(){
        ApiClientInterface apiService =
                ApiClient.getClient().create(ApiClientInterface.class);

        Call<Book> call = apiService.getAllBooks();

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                List<Data> data = book.getData();

                categoryData = new ArrayList<>(data);

                final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                categoryRecyclerView.setLayoutManager(layoutManager);
                categoryAdapter = new CategoryAdapter(Category.this, categoryData);

                categoryRecyclerView.setAdapter(categoryAdapter);
            }
            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("MyApp", "onFailure: " + t.toString());
            }
        });
    }
}
