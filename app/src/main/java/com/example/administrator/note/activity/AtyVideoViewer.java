package com.example.administrator.note.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.administrator.note.R;

import java.io.File;

public class AtyVideoViewer extends AppCompatActivity {

    private VideoView vv;
    public static final String EXTRA_PATH = "path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_video_viewer);
        vv = (VideoView)findViewById(R.id.videoViwer);
        vv.setMediaController(new MediaController(this));


        String path = getIntent().getStringExtra(EXTRA_PATH);
        if(path != null)
        {
            vv.setVideoPath(path);

        }else
        {
            finish();
        }
    }
}
