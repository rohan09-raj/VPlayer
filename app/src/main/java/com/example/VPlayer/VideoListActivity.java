package com.example.VPlayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.Q)
@SuppressLint("DefaultLocale")

public class VideoListActivity extends AppCompatActivity {

    public static final int PERMISSION_READ = 0;
    public static ArrayList<Video> mVideoArrayList;
    RecyclerView mVideoListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (checkPermission()) {
            videoList();
        }
    }

    public void videoList() {
        mVideoListRecyclerView = findViewById(R.id.video_list_recycler_view);
        mVideoListRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mVideoArrayList = new ArrayList<>();
        getVideos();
    }

    public void getVideos() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        try (Cursor cursor = getContentResolver().query(uri, null, null,
                null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                    Video video = new Video();

                    video.setVideoTitle(title);
                    video.setVideoUri(Uri.parse(data));
                    video.setVideoDuration(timeConversion(Long.parseLong(duration)));

                    mVideoArrayList.add(video);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        VideoAdapter videoAdapter = new VideoAdapter(this, mVideoArrayList);
        mVideoListRecyclerView.setAdapter(videoAdapter);

        videoAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });

    }

    public String timeConversion(long value) {
        String videoTime;
        int duration = (int) value;
        int hours = (duration / 3600000);
        int minutes = (duration / 60000) % 60000;
        int seconds = duration % 60000 / 1000;

        if (hours > 0) {
            videoTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            videoTime = String.format("%02d:%02d", minutes, seconds);
        }
        return videoTime;
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
                    Toast.makeText(getApplicationContext(), "Please allow storage permission!", Toast.LENGTH_LONG).show();
                } else {
                    videoList();
                }
            }
        }
    }
}