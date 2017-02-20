package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import heybook.team1.com.heybookv2.R;

public class Logout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
        finish();

        Toast.makeText(getApplicationContext(),
                "Başarıyla çıkış yapıldı.",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Logout.this,Vitrin.class));

    }

}
