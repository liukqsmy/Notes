package com.example.administrator.note.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import java.io.File;

public class AtyVideoViewer extends AppCompatActivity {

    private VideoView vv;
    public static final String EXTRA_PATH = "path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vv = new VideoView(this);
        setContentView(vv);

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
