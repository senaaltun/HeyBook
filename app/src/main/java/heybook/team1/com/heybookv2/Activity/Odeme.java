package heybook.team1.com.heybookv2.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import heybook.team1.com.heybookv2.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Odeme extends BaseActivity implements View.OnClickListener {
  private TextView odemeTotalPrice;

  private Spinner monthSpinner;
  private Spinner yearSpinner;

  private Button paymentButton;

  private EditText cardInfo;
  private EditText cardNumber;
  private EditText secNumber;

  private List<String> monthArray = new ArrayList<>();
  private List<String> yearArray = new ArrayList<>();

  private int sepetDolu;

  private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
  private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
  private static final int DIVIDER_MODULO = 5;
  // means divider position is every 5th symbol beginning with 1
  private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1;
  // means divider position is every 4th symbol beginning with 0
  private static final char DIVIDER = '-';

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_odeme);

    Intent intent = getIntent();
    sepetDolu = intent.getIntExtra("sepetDolu",0);
    double totalPrice = intent.getDoubleExtra("totalPrice", 0);
    odemeTotalPrice.setText(String.format("%.2f", totalPrice) + " TL");
    paymentButton.setOnClickListener(this);

    cardNumber.addTextChangedListener(new TextWatcher() {

      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // noop
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        // noop
      }

      @Override public void afterTextChanged(Editable s) {
        if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
          s.replace(0, s.length(),
              buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
        }
      }

      private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo,
          char divider) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
        for (int i = 0; i < s.length(); i++) { // chech that every element is right
          if (i > 0 && (i + 1) % dividerModulo == 0) {
            isCorrect &= divider == s.charAt(i);
          } else {
            isCorrect &= Character.isDigit(s.charAt(i));
          }
        }
        return isCorrect;
      }

      private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
          if (digits[i] != 0) {
            formatted.append(digits[i]);
            if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
              formatted.append(divider);
            }
          }
        }

        return formatted.toString();
      }

      private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
          char current = s.charAt(i);
          if (Character.isDigit(current)) {
            digits[index] = current;
            index++;
          }
        }
        return digits;
      }
    });

    fillMonthSpinner();
    fillYearSpinner();
  }

  @Override public void onContentChanged() {
    super.onContentChanged();
    odemeTotalPrice = (TextView) findViewById(R.id.odemeTotalPrice);
    monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);
    yearSpinner = (Spinner) findViewById(R.id.spinnerYear);
    paymentButton = (Button) findViewById(R.id.aprovePayment);
    cardInfo = (EditText) findViewById(R.id.odemeUserInfoEditText);
    cardNumber = (EditText) findViewById(R.id.cardNumberEditText);
    secNumber = (EditText) findViewById(R.id.cvcNumberEditText);
  }

  private void fillMonthSpinner() {
    monthArray.add("Ay");
    monthArray.add("Ocak");
    monthArray.add("Şubat");
    monthArray.add("Mart");
    monthArray.add("Nisan");
    monthArray.add("Mayıs");
    monthArray.add("Haziran");
    monthArray.add("Temmuz");
    monthArray.add("Ağustos");
    monthArray.add("Eylül");
    monthArray.add("Ekim");
    monthArray.add("Kasım");
    monthArray.add("Aralık");

    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthArray);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    monthSpinner.setAdapter(adapter);
  }

  private void fillYearSpinner() {
    yearArray.add("Yıl");
    yearArray.add("2017");
    yearArray.add("2018");
    yearArray.add("2019");
    yearArray.add("2020");
    yearArray.add("2021");
    yearArray.add("2022");

    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearArray);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    yearSpinner.setAdapter(adapter);
  }

  @Override public void onClick(View v) {
    String nameSurname = cardInfo.getText().toString();
    String cardNo = cardNumber.getText().toString();
    String cvcNumber = secNumber.getText().toString();
    if (nameSurname.equals("") || cardNo.equals("") || cvcNumber.equals("")) {
      final AlertDialog dialog = new AlertDialog.Builder(Odeme.this).setTitle("Uyarı")
          .setMessage("Alan boş bırakılamaz.")
          .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
          })
          .show();
    } else if( monthSpinner.getSelectedItem().toString()=="Ay" || yearSpinner.getSelectedItem().toString() == "Yıl"){
      final AlertDialog dialog = new AlertDialog.Builder(Odeme.this).setTitle("Uyarı")
          .setMessage("Son kullanma bilgilerini doğru girdiğinizden emin olun.")
          .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
          })
          .show();
    } else {
      final ProgressDialog dialog = new ProgressDialog(Odeme.this);
      dialog.setTitle("Ödeme");
      dialog.setMessage("İşleminiz gerçekleştiriliyor lütfen bekleyiniz.");
      dialog.setIndeterminate(true);
      dialog.setCancelable(false);
      dialog.show();

      long delayInMillis = 5000;
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override public void run() {
          dialog.dismiss();
        }
      }, delayInMillis);

      dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override public void onDismiss(DialogInterface dialog) {
          Intent intent = new Intent(Odeme.this, Onay.class);
          intent.putExtra("totalPrice", getIntent().getDoubleExtra("totalPrice", 0));
          intent.putExtra("sepetDolu",sepetDolu);
          startActivity(intent);
        }
      });
    }
  }
}
