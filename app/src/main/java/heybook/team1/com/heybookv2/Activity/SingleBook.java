package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import heybook.team1.com.heybookv2.HeyBook;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import heybook.team1.com.heybookv2.Model.Data;

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
    private boolean isFavorite = false;

    private int pos = 0;

    private ArrayList<Data> favoriteBooksList;
    private ArrayList<Data> chartList = new ArrayList<>();
    private List<Data> data;

    private String bookID = null;
    private String userId;
    private String bookName;
    private String bookAuthor;
    private String bookDuration;
    private String bookPhoto;
    private String bookPrice;

    SessionManager sessionManager;
    MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);

        getSupportActionBar().hide();

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get("userId");
        final Intent intent = getIntent();
        pos = intent.getIntExtra("Position", 0);
        bookID = getIntent().getStringExtra("bookId");

        if(intent.getBooleanExtra("isUserComingFromBooks",false)){
            addToChartButton.setText("Kitabı Dinle");
        }


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int ratingValue = (int) rating;
                try {
                    setRating(ratingValue);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavorite = sessionManager.isFavorite();
                } else {
                    favButton.setChecked(true);
                    sessionManager.saveFavorite();
                }

                if (!sessionManager.isLoggedIn()) {
                    Toast.makeText(SingleBook.this, "Önce giriş yapmanız gerekmektedir.", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(SingleBook.this,LoginActivity.class);
                    intent.putExtra("clickedBook",bookID);
                    startActivity(intent);
                } else {
                    HashMap<String, String> user = sessionManager.getUserDetails();
                    userId = user.get("userId");
                    try {
                        setFavorites(userId, bookID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            getBookDetail();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addToChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.isLoggedIn() && !intent.getBooleanExtra("isUserComingFromBooks",false)){
                    try {
                        URL url = new URL("https://heybook.online/api.php");
                        HttpsURLConnection connection = null;
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.setReadTimeout(10000);
                        connection.setConnectTimeout(15000);
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        bookID = getIntent().getStringExtra("bookId");
                        Log.d("bookId", bookID);
                        HashMap<String, String> params = new HashMap<>();
                        params.put("request", "request");
                        params.put("requestValue", "user_cart-add");
                        params.put("user_id", userId);
                        params.put("bookId", bookID);

                        OutputStream os = connection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getChart(params));
                        writer.flush();
                        writer.close();
                        os.close();
                        connection.connect();
                        int responseCode = connection.getResponseCode();

                        StringBuilder result = new StringBuilder();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                            String singleBookJson;
                            while ((singleBookJson = br.readLine()) != null) {
                                result.append(singleBookJson).append("\n");
                            }

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                JSONObject jsonData = new JSONObject(result.toString());
                                Log.d("jsonData", jsonData.toString());
                                if(jsonData.getString("response").equals("success")){
                                    Toast.makeText(SingleBook.this,
                                            jsonData.getString("message"),Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(SingleBook.this,
                                            "Kitap sepetinizde zaten mevcut.",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(sessionManager.isLoggedIn() && intent.getBooleanExtra("isUserComingFromBooks",false)){
                    startActivity(new Intent(SingleBook.this,Listen.class));
                }  else{
                    Toast.makeText(SingleBook.this,
                            "Önce giriş yapmanız gerekmektedir.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SingleBook.this,LoginActivity.class));
                }
            }
        });
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
        ratingBar = (RatingBar) findViewById(R.id.rating);
        addToChartButton = (Button) findViewById(R.id.addToChart);
        favButton = (Switch) findViewById(R.id.fav);
        show = (ImageButton) findViewById(R.id.show);
        hide = (ImageButton) findViewById(R.id.hide);
        addToFav = (TextView) findViewById(R.id.addToFav);
        singleBook = (TextView) findViewById(R.id.singleBookName);
    }

    private void getBookDetail() throws IOException, JSONException {

        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "book");
        params.put("book_id", getIntent().getStringExtra("bookId"));

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        connection.connect();

        int responseCode = connection.getResponseCode();

        StringBuilder result = new StringBuilder();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String singleBookJson;
            while ((singleBookJson = br.readLine()) != null) {
                result.append(singleBookJson).append("\n");
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonData = new JSONObject(result.toString());
                final JSONObject singleData = jsonData.getJSONObject("data");
                bookName = singleData.getString("book_title");
                bookAuthor = singleData.getString("author_title");
                bookDuration = singleData.getString("duration");
                bookPhoto = singleData.getString("photo");
                bookPrice = singleData.getString("price");

                Glide.with(SingleBook.this).load(singleData.getString("photo")).into(coverPhoto);
                description.setText(singleData.getString("description"));
                singleBookAuthor.setText(singleData.getString("author_title"));
                singleBook.setText(singleData.getString("book_title"));
                singleBookPrice.setText(singleData.getString("price"));
                getSupportActionBar().setTitle(singleData.getString("book_title"));

                playPreListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            if (isPlaying) {
                                Glide.with(SingleBook.this).load(R.drawable.play).into(playPreListen);
                                mp.stop();
                                mp.reset();
                                mp.release();
                            } else {
                                Glide.with(SingleBook.this).load(R.drawable.pause).into(playPreListen);
                                mp.setDataSource(singleData.getString("audio"));
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
                        } catch (JSONException e) {
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
                        try {
                            description.setText(singleData.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        favButton.setVisibility(View.GONE);
                        addToFav.setVisibility(View.GONE);
                        singleBookPrice.setVisibility(View.GONE);
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
                        addToChartButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    private void setFavorites(String userId, String bookID) throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        bookID = getIntent().getStringExtra("bookId");
        Log.d("bookId", bookID);
        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "user_favorites-add");
        params.put("user_id", userId);
        params.put("bookId", bookID);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getFavQuery(params));
        writer.flush();
        writer.close();
        os.close();
        connection.connect();

        int responseCode = connection.getResponseCode();

        StringBuilder result = new StringBuilder();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String singleBookJson;
            while ((singleBookJson = br.readLine()) != null) {
                result.append(singleBookJson).append("\n");
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonData = new JSONObject(result.toString());
                Log.d("jsonData", jsonData.toString());

                if (jsonData.getString("response").equals("success")) {
                    Toast.makeText(SingleBook.this, jsonData.getString("message"), Toast.LENGTH_SHORT);
                }
            }
        }
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (int i = 0; i < params.size(); i++) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
            result.append("&");
            result.append("book_id=");
            result.append(bookID);
        }

        return result.toString();
    }

    private String getFavQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (int i = 0; i < params.size(); i++) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
            result.append("&");
            result.append("user_id=");
            result.append(params.get("user_id"));
            result.append("&");
            result.append("book_id=");
            result.append(params.get("bookId"));
        }

        return result.toString();
    }

    private void setRating(int rating) throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "user_stars-add");
        params.put("user_id", userId);
        params.put("book_id", bookID);
        params.put("star", String.valueOf(rating));

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getRating(params));
        writer.flush();
        writer.close();
        os.close();
        connection.connect();

        int responseCode = connection.getResponseCode();

        StringBuilder result = new StringBuilder();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String singleBookJson;
            while ((singleBookJson = br.readLine()) != null) {
                result.append(singleBookJson).append("\n");
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonData = new JSONObject(result.toString());
                Log.d("jsonData", jsonData.toString());
            }
        }
    }

    private String getRating(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        if (first) {
            first = false;
        } else {
            result.append("&");
        }
        result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
        result.append("&");
        result.append("user_id=");
        result.append(userId);
        result.append("&");
        result.append("book_id=");
        result.append(bookID);
        result.append("&");
        result.append("star=");
        result.append(params.get("star"));

        return result.toString();
    }

    private String getChart(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (int i = 0; i < params.size(); i++) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
            result.append("&");
            result.append("user_id=");
            result.append(params.get("user_id"));
            result.append("&");
            result.append("book_id=");
            result.append(params.get("bookId"));
        }
        return result.toString();
    }
}

