package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
    private ImageView playPreListen;
    private TextView description;
    private TextView singleBookAuthor;
    private TextView singleBookPrice;
    private TextView loggedUser;
    private Button buyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                int num = sharedPreferences.getInt("isLogged",0);

                if(num==0){
                    Toast.makeText(getApplicationContext(),
                            "Satın alabilme işlemini gerçekleştirebilmeniz için giriş yapmanız gerekmektedir.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SingleBook.this,Login.class));
                }else{

                    Toast.makeText(getApplicationContext(),
                            "Satın Alma ekranı",Toast.LENGTH_SHORT).show();
                }
            }
        });

        getBookDetail();

    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();

        coverPhoto = (ImageView) findViewById(R.id.coverPhoto);
        description = (TextView) findViewById(R.id.description);
<<<<<<< HEAD
        //listenButton = (Button)findViewById(R.id.listenButton);
=======
        singleBookAuthor = (TextView)findViewById(R.id.singleBookAuthor);
        singleBookPrice = (TextView)findViewById(R.id.singleBookPrice);
        buyButton = (Button)findViewById(R.id.buy);
        playPreListen = (ImageView)findViewById(R.id.playPreListen);
        loggedUser = (TextView)findViewById(R.id.loggedUserName);

>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490

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
                            .load(data.get(pos).getPhoto())
                            .into(coverPhoto);
                description.setText(data.get(pos).getDescription());
                singleBookAuthor.setText("Yazar: " + data.get(pos).getAuthor_title());
                singleBookPrice.setText("Fiyat: "+data.get(pos).getPrice());
                    getSupportActionBar().setTitle(data.get(pos).getBook_title());

<<<<<<< HEAD
               /* listenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                        int num = sharedPreferences.getInt("isLogged",0);
                        if(num==0){
                            Toast.makeText(SingleBook.this,"Kitabı dinleyebilmeniz için önce giriş yapmanız gerekmektedir",Toast.LENGTH_SHORT).show();

                            Intent loginIntent = new Intent(SingleBook.this,Login.class);
                            loginIntent.putExtra("bookPos",pos);
                            startActivity(loginIntent);
                        }else{
                            Intent pos = getIntent();
                            int p = pos.getIntExtra("bookPos",0);
                            Intent intent = new Intent(SingleBook.this,Listen.class);
                            intent.putExtra("BookPosition",p);
                            startActivity(intent);
                        }



                    }
                });*/




=======

                playPreListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.setDataSource(data.get(pos).getAudio());
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mp.start();
                                }
                            });
                            mp.prepareAsync();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("MyApp", "onFailure: " + t.toString());
            }
        });
    }


}
