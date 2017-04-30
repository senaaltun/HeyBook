package heybook.team1.com.heybookv2.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import javax.net.ssl.HttpsURLConnection;

public class Onay extends AppCompatActivity implements View.OnClickListener {
    private TextView onayTotalPrice;
    private TextView timer;

    private Button aprove;

    private RelativeLayout contentView;
    private RelativeLayout rootView;
    private RelativeLayout onayView;
    private RelativeLayout secondaryContent;

    private EditText secEditText;

    private Button directUserBooks;

    private String userId;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onay);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userId = user.get("userId");
        Log.d("userId", String.valueOf(userId));

        onayTotalPrice = (TextView) findViewById(R.id.onayTotalPrice);
        aprove = (Button) findViewById(R.id.completePayment);
        contentView = (RelativeLayout) findViewById(R.id.content);
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        onayView = (RelativeLayout) findViewById(R.id.onayView);
        secondaryContent = (RelativeLayout) findViewById(R.id.secondaryContent);
        secEditText = (EditText) findViewById(R.id.secEditText);
        directUserBooks = (Button) findViewById(R.id.directUserBooks);
        timer = (TextView) findViewById(R.id.timer);

        aprove.setOnClickListener(this);
        directUserBooks.setOnClickListener(this);


        new CountDownTimer(200000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timer.setText("" + String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("Ödemeniz başarısız oldu. Lütfen tekrar deneyiniz");
            }
        }.start();


        Intent intent = getIntent();
        double totalPrice = intent.getDoubleExtra("totalPrice", 0);
        onayTotalPrice.setText((String.format("%.2f", totalPrice) + " TL"));
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.completePayment) {
            try {
                if (!secEditText.getText().toString().equals("") && getIntent().getIntExtra("sepetDolu", 0) == 1) {
                    makePayment();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.directUserBooks) {
            startActivity(new Intent(Onay.this, UserBooks.class));
        }


    }

    private void makePayment() throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "user_cart-pay");

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
                final JSONObject jsonData = new JSONObject(result.toString());
                Log.d("jsonData", jsonData.toString());
                if (jsonData.getString("response").equals("success")) {
                    onayTotalPrice.setText("00.00 TL");
                    final ProgressDialog dialog = new ProgressDialog(Onay.this);
                    dialog.setTitle("Ödeme");
                    dialog.setMessage("İşleminiz gerçekleştiriliyor lütfen bekleyiniz.");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(false);
                    dialog.show();

                    long delayInMillis = 4000;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, delayInMillis);

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            rootView.removeView(contentView);
                            onayView.setVisibility(View.VISIBLE);
                            try {
                                Toast.makeText(Onay.this,
                                        jsonData.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    final AlertDialog dialog = new AlertDialog.Builder(Onay.this).setTitle("Uyarı")
                            .setMessage("Bu kitap, satın aldığınız kitap listesinde mevcut.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
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
        result.append("&");
        result.append("payment_hash=ok");

        return result.toString();
    }
}
