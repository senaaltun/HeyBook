package heybook.team1.com.heybookv2.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.*;
import android.provider.Settings;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import heybook.team1.com.heybookv2.Adapter.CoverFlowAdapter;
import heybook.team1.com.heybookv2.Model.VitrinBookEntity;
import heybook.team1.com.heybookv2.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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

    private RequestQueue queue;

    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        queue = Volley.newRequestQueue(this);
        getBookData();

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        //recyclerView = (RecyclerView) findViewById(R.id.bookList);
    }


    public void getBookData() {

        try {
            URL url = new URL("https://heybook.online/api.php");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            HashMap<String, String> params = new HashMap<>();
            params.put("request", "request");
            params.put("requestValue","books");

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

            if(responseCode == HttpsURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

                while((book = br.readLine()) != null){
                    result.append(book).append("\n");
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    JSONObject jsonData = new JSONObject(result.toString());
                    final JSONArray bookD = jsonData.getJSONArray("data");
                    for(int i=0; i<bookD.length();i++){
                        JSONObject b = (JSONObject) bookD.get(i);
                        Data bookData = new Data(b.getString("book_id"),b.getString("book_title"),b.getString("photo"),b.getString("author_title"));
                        vitrinData.add(new VitrinBookEntity(bookData.getBook_id(),bookData.getPhoto(),bookData.getBook_title()));
                    }

                    setContentView(R.layout.activity_vitrin);
                    coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);
                    coverFlowAdapter = new CoverFlowAdapter(getApplicationContext(), vitrinData);
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
                            Intent intent = new Intent(Vitrin.this, SingleBook.class);
                            intent.putExtra("bookId", vitrinData.get(position).getBookId());
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

                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (int i = 0; i < params.size(); i++) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
        }

        return result.toString();
    }



}
