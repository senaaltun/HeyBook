package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import heybook.team1.com.heybookv2.R;

public class Register extends BaseActivity {
    private EditText userTitle;
    private EditText userEmail;
    private EditText password;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userTitle = (EditText)findViewById(R.id.userName);
        userEmail = (EditText)findViewById(R.id.mail);
        password = (EditText)findViewById(R.id.passwordRegister);
        registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userTitle.getText().toString();
                String uMail = userEmail.getText().toString();
                String pwd = password.getText().toString();


            }
        });

    }

}
