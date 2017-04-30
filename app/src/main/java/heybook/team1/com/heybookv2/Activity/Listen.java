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

import de.hdodenhof.circleimageview.CircleImageView;
import heybook.team1.com.heybookv2.Activity.Utility.Utilities;
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

public class Listen extends BaseActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener  {
    private static final String BIG_FILE = "http://heybook.online/audio/engeregin-gozu.mp3";
    private Button play;
    private Button downloadButton;

    private CircleImageView listenBookPhoto;
    private ImageView playPauseButton;

    private TextView listenBookTitle;
    private TextView listenBookAuthor;
    private TextView publisherTitle;
    private TextView narratorTitle;
    private TextView songCurrentDurationLabel;
    private TextView duration;
    private TextView author;

    private Utilities utilities = new Utilities();

    private Handler mHandler = new Handler();

    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;


    private SeekBar seekBar;

    private DownloadManager downloadManager;
    private long downloadReference;

    private MediaPlayer mediaPlayer;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        getSupportActionBar().setTitle("Kitabı Dinle");
        mediaPlayer = new MediaPlayer();

        intent = getIntent();

        if(intent != null){
            Glide.with(Listen.this)
                    .load(intent.getStringExtra("bookPhoto"))
                    .into(listenBookPhoto);
            listenBookTitle.setText(intent.getStringExtra("bookTitle"));
            listenBookAuthor.setText(intent.getStringExtra("bookAuthor"));
            publisherTitle.setText("Yayınevi: " + intent.getStringExtra("publisherTitle"));
            narratorTitle.setText("Seslendiren: " + intent.getStringExtra("bookNarrator"));
            author.setText("Yazar: " + intent.getStringExtra("bookAuthor"));
            seekBar.setMax(Integer.parseInt(intent.getStringExtra("duration"))/60);

        }

        playPauseButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);



        /*downloadButton = (Button)findViewById(R.id.downloadBook);
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
        });*/

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        listenBookPhoto = (CircleImageView)findViewById(R.id.listenBookImage);
        listenBookTitle = (TextView)findViewById(R.id.listenBookName);
        listenBookAuthor = (TextView)findViewById(R.id.listenAuthorTitle);
        publisherTitle = (TextView)findViewById(R.id.listenPublisher);
        narratorTitle = (TextView)findViewById(R.id.listenNarrator);
        duration = (TextView)findViewById(R.id.listenDuration);
        author = (TextView)findViewById(R.id.listenAuthor);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        playPauseButton = (ImageView)findViewById(R.id.listenPlay);
        songCurrentDurationLabel = (TextView)findViewById(R.id.duration);



    }

    private void playSong() throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(intent.getStringExtra("bookAudio"));
        mediaPlayer.prepare();
        mediaPlayer.start();
        playPauseButton.setImageResource(R.drawable.listen_pause);
        seekBar.setProgress(0);
        seekBar.setMax(100);
        updateProgressBar();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.listenPlay:
                try {
                    if(mediaPlayer.isPlaying() && mediaPlayer!=null){
                        mediaPlayer.pause();
                        playPauseButton.setImageResource(R.drawable.listen_play);
                    }else{
                        if(mediaPlayer != null){
                            mediaPlayer.start();
                            playPauseButton.setImageResource(R.drawable.listen_pause);
                        }
                    }
                    playSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.listenBack:
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
                break;
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+utilities.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utilities.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utilities.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
}
