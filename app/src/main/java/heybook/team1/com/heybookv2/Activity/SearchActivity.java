package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.Button;
import android.widget.EditText;

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

import heybook.team1.com.heybookv2.Adapter.VitrinBookAdapter;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;

public class SearchActivity extends BaseActivity {
    private EditText searchInput;
    private String input;
    private Button searchButton;
    private ArrayList<Data> searchResults = new ArrayList<>();
    private VitrinBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Heybook'ta Ara");




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    input = searchInput.getText().toString();
                    getSearchResult();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SearchActivity.this, SearchResult.class);
                intent.putExtra("resultDataList", searchResults);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        searchInput = (EditText)findViewById(R.id.searchInput);
        searchButton = (Button)findViewById(R.id.searchButton);
    }

    private void getSearchResult() throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);



        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "books");


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
                Log.d("jsonData",jsonData.toString());
                final JSONArray searchData = jsonData.getJSONArray("data");

                for(int i=0; i<searchData.length();i++){
                    JSONObject search = (JSONObject) searchData.get(i);
                    if(search.getString("book_title").equalsIgnoreCase(input)){
                        Log.d("asdasda","asdkjhasdkjah");
                        searchResults.add(new Data(search.getString("book_id"),search.getString("book_title"),search.getString("photo"),
                                search.getString("author_title"),search.getString("duration"),search.getString("price"),search.getString("category_title")));
                    }

                }




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


        return result.toString();
    }
}
