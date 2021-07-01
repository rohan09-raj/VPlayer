package com.example.VPlayer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.example.VPlayer.VideoListActivity.mVideoArrayList;

@RequiresApi(api = Build.VERSION_CODES.Q)

public class VideoPlayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    public static final int PERMISSION_READ = 0;
    VideoView mVideoView;
    int mVideoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_play);

        if (checkPermission()) {
            setVideo();
        }
    }

    public void setVideo() {
        mVideoView = findViewById(R.id.video_view);

        mVideoIndex = getIntent().getIntExtra("position", 0);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.setOnCompletionListener(this);

        playVideo(mVideoIndex);
    }

    public void playVideo(int pos) {
        try {
            mVideoView.setVideoURI(mVideoArrayList.get(pos).getVideoUri());
            mVideoView.start();
            mVideoIndex = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Playback Finished!");
        alertDialog.setIcon(R.mipmap.ic_video_player_alert);
        VideoClickListener m = new VideoClickListener();
        alertDialog.setPositiveButton("Replay", m);
        alertDialog.setNegativeButton("Next", m);
        alertDialog.setMessage("Want to replay or play next video?");
        alertDialog.show();
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_READ) {
            if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                } else {
                    playVideo(mVideoIndex);
                }
            }
        }
    }

    class VideoClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                mVideoView.seekTo(0);
                mVideoView.start();
            } else {
                mVideoIndex++;
                if (mVideoIndex >= mVideoArrayList.size())
                    mVideoIndex = 0;
                playVideo(mVideoIndex);
            }
        }
    }
}