package com.example.administrator.note.activity;

import android.app.ListActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.note.R;

public class MainActivity extends ListActivity {

    private SimpleCursorAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
