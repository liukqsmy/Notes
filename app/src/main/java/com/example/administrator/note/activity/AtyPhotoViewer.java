package com.example.administrator.note.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.administrator.note.R;

import java.io.File;

public class AtyPhotoViewer extends AppCompatActivity {

    private ImageView iv;
    public static final String EXTRA_PATH = "path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_photo_viewer);
        iv = (ImageView) findViewById(R.id.photoViewer);

        //iv = new ImageView(this);
        //setContentView(iv);


        String path = getIntent().getStringExtra(EXTRA_PATH);
        //Log.e("pthfdfdpath", "adffffffffffffffffffffffff_:" + path);
        if(path != null)
        {
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = 4;
            Bitmap bm = BitmapFactory.decodeFile(path,option);
            iv.setImageBitmap(bm);
            Log.e("pthfdfdpath", "adffffffffffffffffffffffff_:" + path);
            //iv.setImageURI(Uri.fromFile(new File(path)));

        }else
        {
            finish();
        }
    }
}
