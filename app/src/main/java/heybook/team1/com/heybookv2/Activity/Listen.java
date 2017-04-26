package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.novoda.downloadmanager.DownloadManagerBuilder;
import com.novoda.downloadmanager.lib.DownloadManager;
import com.novoda.downloadmanager.lib.Request;
import com.novoda.downloadmanager.notifications.NotificationVisibility;

import heybook.team1.com.heybookv2.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listen extends BaseActivity {
    private static final String BIG_FILE = "http://heybook.online/audio/engeregin-gozu.mp3";
    private Button play;
    private Button downloadButton;

    private DownloadManager downloadManager;
    private long downloadReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        downloadButton = (Button)findViewById(R.id.downloadBook);
        downloadManager = DownloadManagerBuilder.from(Listen.this)
                .build();

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(BIG_FILE);
                final Request request = new Request(uri)
                        .setDestinationInInternalFilesDir(Environment.DIRECTORY_MUSIC,"heybook")
                        .setNotificationVisibility(NotificationVisibility.ACTIVE_OR_COMPLETE);
                downloadManager.enqueue(request);
                downloadReference = downloadManager.enqueue(request);
            }
        });

        findViewById(R.id.listenPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ReferenceIdP",String.valueOf(downloadManager.getUriForDownloadedFile(downloadReference)));
                MediaPlayer mPlayer = new MediaPlayer();
                Uri myUri = Uri.parse("file:///data/user/0/heybook.team1.com.heybookv2/files/Music/heybook-5");
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mPlayer.setDataSource(getApplicationContext(), myUri);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }



}
