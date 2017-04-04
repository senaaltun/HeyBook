package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

public class Logout extends BaseActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        sessionManager = new SessionManager(getApplicationContext());

        sessionManager.logoutUser();

        Toast.makeText(getApplicationContext(),
                "Başarıyla çıkış yapıldı.",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Logout.this,Vitrin.class));

    }

}
