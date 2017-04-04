package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
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

import heybook.team1.com.heybookv2.R;
import java.util.List;
import java.util.concurrent.TimeUnit;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listen extends BaseActivity {
    private Button b1,b2,b3,b4;
    private ImageView bookImage;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler handler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,listenBookName;

    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        bookImage = (ImageView)findViewById(R.id.imageView);

        tx1 = (TextView)findViewById(R.id.currentDuration);
        tx2 = (TextView)findViewById(R.id.totalDuration);
        listenBookName = (TextView)findViewById(R.id.listenBookName);
        seekbar = (SeekBar)findViewById(R.id.seekBarPlayer);
        seekbar.setClickable(false);
        b2.setEnabled(false);
        getBookInformation();

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Playing book",Toast.LENGTH_SHORT).show();
                ApiClientInterface apiService =
                        ApiClient.getClient().create(ApiClientInterface.class);

                Call<Book> call = apiService.getBook();

                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        Book book = response.body();
                        List<Data> data = book.getData();


                        Log.d("Audio",data.get(0).getAudio());

                            mediaPlayer = MediaPlayer.create(Listen.this,Uri.parse(data.get(0).getAudio()));
                            mediaPlayer.start();


                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();

                        if(oneTimeOnly == 0){
                            seekbar.setMax((int)finalTime);
                            oneTimeOnly = 1;
                        }

                        tx2.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime))));
                        tx1.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                startTime))));
                        seekbar.setProgress((int)startTime);
                        handler.postDelayed(UpdateSongTime,100);
                        b2.setEnabled(true);
                        b3.setEnabled(false);
                    }
                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        Log.e("MyApp", "onFailure: " + t.toString());
                    }
                });
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing book",Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };

    public void getBookInformation(){
        ApiClientInterface apiService =
                ApiClient.getClient().create(ApiClientInterface.class);

        Call<Book> call = apiService.getBook();

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                List<Data> data = book.getData();
                Intent intent = getIntent();
                int pos = intent.getIntExtra("BookPosition",0);
                listenBookName.setText(data.get(pos).getBook_title());
                getSupportActionBar().setTitle(data.get(pos).getBook_title());
                Glide.with(Listen.this)
                        .load(data.get(pos).getPhoto())
                        .into(bookImage);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });
    }


}
