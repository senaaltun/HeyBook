package heybook.team1.com.heybookv2.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import heybook.team1.com.heybookv2.Adapter.CoverFlowAdapter;
import heybook.team1.com.heybookv2.Model.VitrinBookEntity;
import heybook.team1.com.heybookv2.R;

import java.util.ArrayList;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Adapter.VitrinBookAdapter;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vitrin extends BaseActivity {
    private RecyclerView recyclerView;
    private VitrinBookAdapter adapter;
    private ArrayList<Data> dataList;
    private ArrayList<VitrinBookEntity> vitrinData = new ArrayList<>();
    private TextSwitcher title;
    private CoverFlowAdapter coverFlowAdapter;
    private FeatureCoverFlow coverFlow;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBookData();

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        //recyclerView = (RecyclerView) findViewById(R.id.bookList);
    }


    public void getBookData() {

        ApiClientInterface apiService =
                ApiClient.getClient().create(ApiClientInterface.class);

        Call<Book> call = apiService.getAllBooks();

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                List<Data> data = book.getData();

                dataList = new ArrayList<>(data);


                for(int i = 0 ; i< dataList.size() ; i++){
                    vitrinData.add(new VitrinBookEntity(dataList.get(i).getPhoto(),dataList.get(i).getBook_title()));
                }

                setContentView(R.layout.activity_vitrin);

                coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
                coverFlowAdapter = new CoverFlowAdapter(getApplicationContext(),vitrinData);
                coverFlow.setAdapter(coverFlowAdapter);


                title = (TextSwitcher) findViewById(R.id.title);
                title.setFactory(new ViewSwitcher.ViewFactory() {
                    @Override
                    public View makeView() {
                        LayoutInflater inflater = LayoutInflater.from(Vitrin.this);
                        TextView textView = (TextView) inflater.inflate(R.layout.item_title, null);
                        return textView;
                    }
                });

                Animation in = AnimationUtils.loadAnimation(Vitrin.this, R.anim.slide_in_top);
                Animation out = AnimationUtils.loadAnimation(Vitrin.this, R.anim.slide_out_bottom);
                title.setInAnimation(in);
                title.setOutAnimation(out);



                coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Vitrin.this,SingleBook.class);
                        intent.putExtra("Position",position);
                        startActivity(intent);
                    }
                });

                coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                    @Override
                    public void onScrolledToPosition(int position) {
                        title.setText(vitrinData.get(position).getTitle());
                    }

                    @Override
                    public void onScrolling() {
                        title.setText("");
                    }
                });


                /*final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new VitrinBookAdapter(Vitrin.this, dataList);

                recyclerView.setAdapter(adapter);*/
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("MyApp", "onFailure: " + t.toString());
            }
        });
    }
}
