package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

import javax.net.ssl.HttpsURLConnection;

import heybook.team1.com.heybookv2.Adapter.UserBooksAdapter;
import heybook.team1.com.heybookv2.Adapter.VitrinBookAdapter;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

public class UserBooks extends BaseActivity {
    private RecyclerView userBookRecyclerView;

    private ArrayList<Data> userBooksData = new ArrayList<>();

    private UserBooksAdapter adapter;

    private String userId;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        getSupportActionBar().setTitle("Kitaplarım");

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get("userId");

        try {
            getUserBooks();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        userBookRecyclerView = (RecyclerView) findViewById(R.id.userBookRecyclerView);
    }

    private void getUserBooks() throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "user_books");

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
                final JSONArray bookD = jsonData.getJSONArray("data");
                for (int i = 0; i < bookD.length(); i++) {
                    JSONObject b = (JSONObject) bookD.get(i);
                    userBooksData.add(new Data(b.getString("book_id"),b.getString("book_title"),b.getString("photo"),b.getString("author_title"),b.getString("duration"),b.getString("price"),b.getString("category_title")));
                }
                adapter = new UserBooksAdapter(getApplicationContext(),userBooksData);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                userBookRecyclerView.setLayoutManager(layoutManager);
                userBookRecyclerView.setItemAnimator(new DefaultItemAnimator());
                userBookRecyclerView.setAdapter(adapter);
            }
        }
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        if (first)
            first = false;
        else
            result.append("&");
        result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
        result.append("&");
        result.append("user_id=");
        result.append(userId);


        return result.toString();
    }
}
