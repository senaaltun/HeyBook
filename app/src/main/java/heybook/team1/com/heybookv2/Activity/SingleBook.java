package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

import java.io.IOException;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
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
    private TextView singleBook;
    private TextView addToFav;

    private Button buyButton;
    private Button addToChartButton;

    private RatingBar ratingBar;

    private ImageButton show;
    private ImageButton hide;

    private Switch favButton;

    private boolean isPlaying = false;

    SessionManager sessionManager;
    MediaPlayer mp = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);

        sessionManager = new SessionManager(getApplicationContext());

       /* buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sessionManager.isLoggedIn()) {
                    Toast.makeText(getApplicationContext(),
                            "Satın alabilme işlemini gerçekleştirebilmeniz için giriş yapmanız gerekmektedir.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SingleBook.this, Login.class));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Satın Alma ekranı", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        getBookDetail();
        /*final View contentView = this.findViewById(R.id.content_single_book);

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    addToChartButton.setVisibility(View.GONE);
                    buyButton.setVisibility(View.GONE);
                } else {
                    if (show.getVisibility() == View.VISIBLE) {
                        addToChartButton.setVisibility(View.VISIBLE);
                        buyButton.setVisibility(View.VISIBLE);
                    } else {
                        addToChartButton.setVisibility(View.GONE);
                        buyButton.setVisibility(View.GONE);
                    }


                }
            }
        });*/


    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();

        coverPhoto = (ImageView) findViewById(R.id.coverPhoto);
        description = (TextView) findViewById(R.id.description_text);
        singleBookAuthor = (TextView) findViewById(R.id.singleBookAuthor);
        singleBookPrice = (TextView) findViewById(R.id.singleBookPrice);
        //buyButton = (Button) findViewById(R.id.buy);
        playPreListen = (ImageView) findViewById(R.id.playPreListen);
        loggedUser = (TextView) findViewById(R.id.loggedUserName);
        //ratingBar = (RatingBar) findViewById(R.id.rating);
        addToChartButton = (Button) findViewById(R.id.addToChart);
        favButton = (Switch) findViewById(R.id.fav);
        show = (ImageButton) findViewById(R.id.show);
        hide = (ImageButton) findViewById(R.id.hide);
        addToFav = (TextView) findViewById(R.id.addToFav);
        singleBook = (TextView) findViewById(R.id.singleBookName);
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
                final int pos = intent.getIntExtra("Position", 0);

                Glide.with(SingleBook.this)
                        .load(data.get(pos).getPhoto())
                        .into(coverPhoto);
                description.setText(data.get(pos).getDescription());
                singleBookAuthor.setText(data.get(pos).getAuthor_title());
                singleBook.setText(data.get(pos).getBook_title());
                singleBookPrice.setText(data.get(pos).getPrice() + " TL");
                getSupportActionBar().setTitle(data.get(pos).getBook_title());


                playPreListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            if (isPlaying) {
                                Glide.with(SingleBook.this)
                                        .load(R.drawable.play)
                                        .into(playPreListen);
                                mp.stop();
                                mp.reset();
                                mp.release();
                            } else {
                                Glide.with(SingleBook.this)
                                        .load(R.drawable.pause)
                                        .into(playPreListen);
                                mp.setDataSource(data.get(pos).getAudio());
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        mp.start();
                                    }
                                });
                                mp.prepareAsync();
                            }
                            isPlaying = !isPlaying;


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.setVisibility(View.INVISIBLE);
                        hide.setVisibility(View.VISIBLE);
                        description.setMaxLines(Integer.MAX_VALUE);
                        description.setText(data.get(pos).getDescription());
                        favButton.setVisibility(View.GONE);
                        addToFav.setVisibility(View.GONE);
                        singleBookPrice.setVisibility(View.GONE);
                        //buyButton.setVisibility(View.GONE);
                        addToChartButton.setVisibility(View.GONE);
                    }
                });

                hide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hide.setVisibility(View.INVISIBLE);
                        show.setVisibility(View.VISIBLE);
                        description.setMaxLines(5);
                        favButton.setVisibility(View.VISIBLE);
                        addToFav.setVisibility(View.VISIBLE);
                        singleBookPrice.setText(View.VISIBLE);
                        //buyButton.setVisibility(View.VISIBLE);
                        addToChartButton.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.e("MyApp", "onFailure: " + t.toString());
            }
        });

        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(getApplicationContext(),
                        "Değerlendirme başarıyla verildi: " + v,
                        Toast.LENGTH_SHORT).show();
            }
        });*/

    }


}
