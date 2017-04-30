package heybook.team1.com.heybookv2.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

import heybook.team1.com.heybookv2.HeyBook;
import heybook.team1.com.heybookv2.Model.Data;
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

    private double totalPrice = 0;

    private boolean isLastItem = false;

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

                if(sepetList!=null){
                    Intent intent = new Intent(Sepet.this,Odeme.class);
                    intent.putExtra("totalPrice",totalPrice);
                    intent.putExtra("sepetDolu",1);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        recyclerView = (RecyclerView) findViewById(R.id.sepetRecyclerView);
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
                    sepetList.add(new Data(f.getString("book_id"), f.getString("book_title"), f.getString("photo"), f.getString("author_title"), f.getString("duration"), f.getString("price"), f.getString("category_title")));
                    totalPrice += Double.parseDouble(f.getString("price"));
                }
                Log.d("sepetList", String.valueOf(sepetList.size()));
                sepetAdapter = new SepetAdapter(this, sepetList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(sepetAdapter);
                totalPayment.setText(String.format("%.2f", totalPrice) + " TL");
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

    private void deleteSepetItem(String bookId) throws IOException, JSONException {
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
        params.put("requestValue", "user_cart-delete");
        params.put("userId", userId);
        params.put("bookId", bookId);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(deleteSepetItemRow(params));

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
                Log.d("sepet", jsonData.toString());
            }
        }
    }

    private String deleteSepetItemRow(HashMap<String, String> params) throws UnsupportedEncodingException {
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
        result.append("&");
        result.append("book_id=");
        result.append(params.get("bookId"));

        Log.d("result", result.toString());


        return result.toString();
    }

    public class SepetAdapter extends RecyclerView.Adapter<SepetAdapter.ItemViewHolder> {
        private ArrayList<Data> sepetArrayList;
        private Context context;
        private LayoutInflater inflater;
        private double totalPrice = 0;

        public SepetAdapter(Context context, ArrayList<Data> sepetArrayList) {
            this.context = context;
            this.sepetArrayList = sepetArrayList;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public SepetAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View root = inflater.inflate(R.layout.item_sepet_row, parent, false);
            ItemViewHolder holder = new ItemViewHolder(root);
            Data sepetBookData = sepetArrayList.get(viewType);
            holder.sepetBookTitle.setText(sepetBookData.getBook_title());
            holder.sepetBookPrice.setText(sepetBookData.getPrice() + " TL");
            Glide.with(context)
                    .load(sepetBookData.getPhoto())
                    .into(holder.sepetBookImage);
            holder.deleteBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
                        //totalPrice = totalPrice - Double.parseDouble(sepetArrayList.get(viewType).getPrice());
                        //totalPayment.setText(String.format("%.2f", totalPrice) + " TL");
                        deleteSepetItem(sepetArrayList.get(viewType).getBook_id());

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sepetArrayList.remove(viewType);
                    notifyItemRemoved(viewType);
                    notifyItemRangeChanged(viewType, sepetArrayList.size());
                }
            });


            return holder;
        }

        @Override
        public void onBindViewHolder(final SepetAdapter.ItemViewHolder holder, final int position) {
            Data sepetBookData = sepetArrayList.get(position);

            holder.sepetBookTitle.setText(sepetBookData.getBook_title());
            holder.sepetBookPrice.setText(sepetBookData.getPrice() + " TL");
            Glide.with(holder.sepetBookImage.getContext())
                    .load(sepetBookData.getPhoto())
                    .into(holder.sepetBookImage);
            holder.deleteBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        deleteSepetItem(sepetArrayList.get(position).getBook_id());
                        totalPrice-= Double.parseDouble(sepetArrayList.get(position).getPrice());
                        totalPayment.setText(String.format("%.2f", totalPrice) + " TL");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sepetArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, sepetArrayList.size());
                }
            });

        }

        @Override
        public int getItemCount() {
            return sepetArrayList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView sepetBookTitle;
            TextView sepetBookPrice;

            ImageView sepetBookImage;

            ImageView deleteBook;


            public ItemViewHolder(View view) {
                super(view);

                sepetBookTitle = (TextView) view.findViewById(R.id.sepetBookTitle);
                sepetBookPrice = (TextView) view.findViewById(R.id.sepetBookPrice);
                sepetBookImage = (ImageView) view.findViewById(R.id.sepetBookImage);
                deleteBook = (ImageView) view.findViewById(R.id.deleteBook);

            }

            @Override
            public void onClick(View v) {
            }
        }
    }

    private void deleteSepetAll() throws IOException, JSONException {
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
        params.put("requestValue", "user_cart-clear");
        params.put("userId", userId);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(deleteAll(params));
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
            }
        }

    }

    private String deleteAll(HashMap<String, String> params) throws UnsupportedEncodingException {
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


        Log.d("result", result.toString());


        return result.toString();
    }


}
