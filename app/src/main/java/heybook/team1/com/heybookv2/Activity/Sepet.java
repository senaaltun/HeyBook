package heybook.team1.com.heybookv2.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import heybook.team1.com.heybookv2.Adapter.FavoritesAdapter;
import heybook.team1.com.heybookv2.Adapter.SepetAdapter;
import heybook.team1.com.heybookv2.HeyBook;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Model.Favorite;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

public class Sepet extends BaseActivity {
    private RecyclerView recyclerView;

    private Button recycleAllButton;
    private Button payButton;
    private Button deleteBook;

    private TextView totalPayment;

    private SessionManager sessionManager;

    private String userId;

    private ArrayList<Data> sepetList = new ArrayList<>();

    private SepetAdapter sepetAdapter;

    private double totalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sepet);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            getSepetDetails();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder paymentDialogBuilder = new AlertDialog.Builder(Sepet.this);
                paymentDialogBuilder.setTitle("Lütfen Ödeme Bilgilerinizi Giriniz");
                paymentDialogBuilder.setView(R.layout.payment_view);
                paymentDialogBuilder.setPositiveButton("Tamamla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Sepet.this,Listen.class));
                            }
                        })
                        .setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog alertDialog = paymentDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        recyclerView = (RecyclerView) findViewById(R.id.sepetRecyclerView);
        recycleAllButton = (Button) findViewById(R.id.recycle);
        payButton = (Button) findViewById(R.id.pay);
        totalPayment = (TextView) findViewById(R.id.totalPrice);
        sessionManager = new SessionManager(this);

    }

    private void getSepetDetails() throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get("userId");
        Log.d("userIdFa", userId);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "user_cart");
        params.put("userId", userId);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        connection.connect();

        int responseCode = connection.getResponseCode();

        String book;

        StringBuilder result = new StringBuilder();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            while ((book = br.readLine()) != null) {
                result.append(book).append("\n");
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonData = new JSONObject(result.toString());
                final JSONArray sepet = jsonData.getJSONArray("data");

                for (int i = 0; i < sepet.length(); i++) {
                    JSONObject f = (JSONObject) sepet.get(i);
                    sepetList.add(new Data(f.getString("book_id"),f.getString("book_title"),f.getString("photo"),f.getString("author_title"),f.getString("duration"),f.getString("price"),f.getString("category_title")));
                    totalPrice+=Double.parseDouble(f.getString("price"));
                }
                sepetAdapter = new SepetAdapter(this,sepetList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(sepetAdapter);
                totalPayment.setText(String.format("%.2f",totalPrice) + " TL");
                sepetAdapter.notifyDataSetChanged();


            }
        }

    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
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
        result.append(params.get("userId"));

        return result.toString();
    }
}
