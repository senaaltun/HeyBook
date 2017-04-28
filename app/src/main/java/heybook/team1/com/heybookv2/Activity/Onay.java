package heybook.team1.com.heybookv2.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import heybook.team1.com.heybookv2.R;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.w3c.dom.Text;

public class Onay extends AppCompatActivity implements View.OnClickListener {
  private TextView onayTotalPrice;
  private TextView timer;

  private Button aprove;

  private RelativeLayout contentView;
  private RelativeLayout rootView;
  private RelativeLayout onayView;
  private RelativeLayout secondaryContent;

  private EditText secEditText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onay);

    onayTotalPrice = (TextView)findViewById(R.id.onayTotalPrice);
    aprove = (Button)findViewById(R.id.completePayment);
    contentView = (RelativeLayout)findViewById(R.id.content);
    rootView = (RelativeLayout)findViewById(R.id.rootView);
    onayView = (RelativeLayout)findViewById(R.id.onayView);
    secondaryContent = (RelativeLayout)findViewById(R.id.secondaryContent);
    secEditText = (EditText)findViewById(R.id.secEditText);
    timer = (TextView)findViewById(R.id.timer);

    aprove.setOnClickListener(this);


    new CountDownTimer(200000, 1000) { // adjust the milli seconds here

      public void onTick(long millisUntilFinished) {
        timer.setText(""+String.format("%d : %d ",
            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
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

  @Override public void onClick(View v) {

    makePayment();

    if(!secEditText.getText().toString().equals("")){
      final ProgressDialog dialog = new ProgressDialog(Onay.this);
      dialog.setTitle("Ödeme");
      dialog.setMessage("İşleminiz gerçekleştiriliyor lütfen bekleyiniz.");
      dialog.setIndeterminate(true);
      dialog.setCancelable(false);
      dialog.show();

      long delayInMillis = 4000;
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override public void run() {
          dialog.dismiss();
        }
      }, delayInMillis);

      dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override public void onDismiss(DialogInterface dialog) {
          rootView.removeView(contentView);
          onayView.setVisibility(View.VISIBLE);
        }
      });
    } else{

    }
  }

  private void makePayment(){

  }
}
