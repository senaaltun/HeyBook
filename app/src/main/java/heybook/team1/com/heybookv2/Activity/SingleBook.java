package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Model.User;
import heybook.team1.com.heybookv2.Model.UserData;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleBook extends BaseActivity {
    private ImageView coverPhoto;
    private TextView description;
    private ImageView playPause;
    private MediaPlayer mediaPlayer;
    private Button listenButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);

        getBookDetail();
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();

        coverPhoto = (ImageView) findViewById(R.id.coverPhoto);
        description = (TextView) findViewById(R.id.description);
        listenButton = (Button)findViewById(R.id.listenButton);

    }

    public void getBookDetail() {
        ApiClientInterface apiService =
                ApiClient.getClient().create(ApiClientInterface.class);

        Call<Book> call = apiService.getBook();

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                final Book book = response.body();
                final List<Data> data = book.getData();

                Intent intent = getIntent();
                final int pos = intent.getIntExtra("Position",0);

                Glide.with(SingleBook.this)
                            .load(data.get(pos).getThumb())
                            .into(coverPhoto);
                    description.setText(data.get(pos).getDescription());
                    getSupportActionBar().setTitle(data.get(pos).getBook_title());

                listenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SingleBook.this,"Kitabı dinleyebilmeniz için önce giriş yapmanız gerekmektedir",Toast.LENGTH_SHORT).show();

                            Intent loginIntent = new Intent(SingleBook.this,Login.class);
                            loginIntent.putExtra("bookPos",pos);
                            startActivity(loginIntent);


                    }
                });




            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("MyApp", "onFailure: " + t.toString());
            }
        });
    }

}
