package com.example.administrator.note.activity;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.note.R;
import com.example.administrator.note.db.NoteDB;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AtyEditNote extends ListActivity {

    private int noteId = -1;
    public static final String EXTRA_NOTE_ID = "noteId";
    public static final String EXTRA_NOTE_NAME = "noteName";
    public static final String EXTRA_NOTE_CONTENT = "noteContent";

    public static final int REQUEST_CODE_GET_PHOTO = 1;
    public static final int REQUEST_CODE_GET_VIDEO = 2;

    private EditText etName, etContent;
    private MediaAdapter adapter;

    private NoteDB db;
    private SQLiteDatabase dbRead,dbWrite;
    private  String currentPath = null;

    Intent i;
    File f;

    private View.OnClickListener btnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnSave:
                    saveMedia(saveNote());
                    setResult(RESULT_OK);
                    finish();
                    break;
                case R.id.btnCancel:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case R.id.btnAddPhoto:
                    i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    f = new File(getMediaDir(), System.currentTimeMillis() + ".jpg");
                    if(!f.exists()){
                        try {
                            f.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    currentPath = f.getAbsolutePath();
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(i,REQUEST_CODE_GET_PHOTO);
                    break;
                case R.id.btnAddVideo:
                    i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    f = new File(getMediaDir(), System.currentTimeMillis() + ".mp4");
                    if(!f.exists()){
                        try {
                            f.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    currentPath = f.getAbsolutePath();
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(i,REQUEST_CODE_GET_VIDEO);
                    break;
            }
        }
    };

    public File getMediaDir(){
        File dir = new File(Environment.getExternalStorageDirectory(), "NotesMedia");
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_edit_note);

        db = new NoteDB(this);
        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();

        adapter = new MediaAdapter(this);
        setListAdapter(adapter);

        etName = (EditText)findViewById(R.id.etName);
        etContent = (EditText)findViewById(R.id.etContent);
        noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

        if(noteId > -1){
            etName.setText(getIntent().getStringExtra(EXTRA_NOTE_NAME));
            etContent.setText(getIntent().getStringExtra(EXTRA_NOTE_CONTENT));

            Cursor c = dbRead.query(NoteDB.TABLE_MEDIA,null, NoteDB.COLUMN_MEDIA_OWNER_ID + "=?", new String[] {noteId+""},null,null,null);
            while (c.moveToNext())
            {
                adapter.add(new MediaListCellData(c.getString(c.getColumnIndex(NoteDB.COLUMN_MEDIA_PATH)),c.getInt(c.getColumnIndex(NoteDB.COLUMN_ID))));
            }
            adapter.notifyDataSetChanged();
        }

        findViewById(R.id.btnSave).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnCancel).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnAddVideo).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnAddPhoto).setOnClickListener(btnClickHandler);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQUEST_CODE_GET_PHOTO:
            case REQUEST_CODE_GET_VIDEO:
                if(resultCode == RESULT_OK){
                    adapter.add(new MediaListCellData(currentPath));
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    public void saveMedia(int nId){

        MediaListCellData data;
        ContentValues cv;

        for (int i=0; i<adapter.getCount(); i++)
        {
            data = adapter.getItem(i);
            if(data.id <= -1){
                cv = new ContentValues();
                cv.put(NoteDB.COLUMN_MEDIA_PATH, data.path);
                cv.put(NoteDB.COLUMN_MEDIA_OWNER_ID, noteId);
                dbWrite.insert(NoteDB.TABLE_MEDIA,null, cv);
            }
        }
    }

    public int saveNote(){
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.COLUMN_NOTE_NAME, etName.getText().toString());
        cv.put(NoteDB.COLUMN_NOTE_CONTENT,etContent.getText().toString());
        cv.put(NoteDB.COLUMN_NOTE_DATE, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

        if(noteId > -1){
            dbWrite.update(NoteDB.TABLE_NOTES, cv, NoteDB.COLUMN_ID + "=?", new String[] {noteId + ""});
            return noteId;
        }
        else
        {
            return (int)dbWrite.insert(NoteDB.TABLE_NOTES, null, cv);
        }
    }
    @Override
    protected void onDestroy() {
        dbRead.close();
        dbWrite.close();
        super.onDestroy();
    }

    static class MediaAdapter extends BaseAdapter{

        private Context context;
        private List<MediaListCellData> list = new ArrayList<MediaListCellData>();

        public MediaAdapter(Context context)
        {
            this.context = context;
        }

        public void add(MediaListCellData data){
            list.add(data);
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public MediaListCellData getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                view = LayoutInflater.from(context).inflate(R.layout.media_list_cell,null);

            }

            MediaListCellData data = getItem(i);

            ImageView ivIcon = (ImageView)view.findViewById(R.id.ivIcon);
            TextView tvPath = (TextView)view.findViewById(R.id.tvPath);

            ivIcon.setImageResource(data.iconId);
            tvPath.setText(data.path);

            return view;
        }
    }
    static class MediaListCellData{
        public  MediaListCellData(String path)
        {
            this.path = path;

            if(path.endsWith(".jpg")){
                iconId = R.drawable.icon_photo;
            }
            else if(path.endsWith(".mp4")){
                iconId = R.drawable.icon_video;
            }
        }
        public  MediaListCellData(String path, int id)
        {
            this(path);
            this.id = id;
        }
        int id = -1;
        String path = "";
        int iconId = R.mipmap.ic_launcher;
    }

}
