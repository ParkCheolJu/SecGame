package com.example.secgame.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secgame.application.CreateExam;
import com.example.secgame.dto.MusicInfo;
import com.example.secgame.adapter.Music_RecyclerAdapter;
import com.example.secgame.database.MyDBHelper;
import com.example.secgame.R;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //music 리사이클러뷰 관련
    RecyclerView musicList;
    RecyclerView.LayoutManager layoutManager;
    Music_RecyclerAdapter rAdapter;
    ArrayList<MusicInfo> mp3List;

    //데이터베이스 관련
    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/Music/";
    public MyDBHelper myHelper;
    SQLiteDatabase musicDB;

    //mediaplayer관련
    ProgressBar musicPb;
    Thread thread;
    MediaPlayer mPlayer;
    TextView musicTitle;
    ImageButton musicPlay;

    int index, version;
    int exam[];
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicList = findViewById(R.id.music_recyclerview);
        musicList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        musicList.setLayoutManager(layoutManager);
        musicPb = findViewById(R.id.music_progressbar);
        musicTitle = findViewById(R.id.music_title);
        musicPlay = findViewById(R.id.music_play);
        items = new ArrayList<String>();

        myHelper = new MyDBHelper(this);
        musicDB = myHelper.getWritableDatabase();

        mp3List = new ArrayList<>();
        rAdapter = new Music_RecyclerAdapter(mp3List);
        musicList.setAdapter(rAdapter);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        mPlayer = new MediaPlayer();

        musicDB = myHelper.getWritableDatabase();
        Cursor cursor = musicDB.rawQuery("Select * from musicInfo;", null);

        items.clear();

        while(cursor.moveToNext()){
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            mp3List.add(new MusicInfo(R.drawable.music, name, address));
            items.add(name);
            index++;
        }

        rAdapter.notifyDataSetChanged();

        musicDB.close();
        //recyclerView AdapterItemClickEvent
        rAdapter.setOnItemClickListener(new Music_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                try {
                    mPlayer.reset();
                    MusicInfo selected = rAdapter.getItem(pos);
                    mPlayer.setDataSource(selected.getAddress());
                    mPlayer.prepare();
                    mPlayer.start();
                    musicPb.setMax(mPlayer.getDuration());
                    musicTitle.setText("재생중인 음악 : "+ selected.getName());
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(mPlayer==null)
                                return;
                            while(mPlayer.isPlaying()){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        musicPlay.setBackgroundResource(R.drawable.pause);
                                        musicPb.setProgress(mPlayer.getCurrentPosition());
                                    }
                                });
                                SystemClock.sleep(200);
                            }
                        }
                    });
                    thread.start();
                    //여기까지
                } catch (IOException e) {
                }
            }

            @Override
            public void onItemLongClick(View v, final int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String name = rAdapter.getItem(pos).getName();
                builder.setMessage("수정");
                final EditText ed = new EditText(MainActivity.this);
                builder.setView(ed);
                ed.setText(name);

                builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = ed.getText().toString();
                        items.set(pos,newName);
                        rAdapter.getItem(pos).setCustomName(newName);
                        rAdapter.notifyDataSetChanged();
                        musicDB = myHelper.getWritableDatabase() ;
                        musicDB.execSQL("UPDATE musicInfo SET mCustomName = '" + newName + "' WHERE mIndex = " + pos+";");
                        musicDB.close();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(pos);
                        mp3List.remove(pos);
                        rAdapter.notifyItemRangeChanged(pos, items.size());
                        musicDB = myHelper.getWritableDatabase() ;
                        musicDB.execSQL("DELETE FROM musicInfo WHERE mCustomName = '" + name+"';");
                        musicDB.close();
                        index--;
                            for(int i = 0; i < items.size();i++){
                                String n = items.get(i);
                                Log.d("test", n);

                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    //음악일시정지
    public void onClick(View view){
        if(mPlayer.isPlaying()){
            mPlayer.pause();
            musicPlay.setBackgroundResource(R.drawable.play);
        }
        else{
            mPlayer.start();
            musicPlay.setBackgroundResource(R.drawable.pause);
        }
    }

    public void SearchMusic(View view){
//        Intent searchPage = new Intent(getApplicationContext(), MusicSearch.class);
//        startActivityForResult(searchPage, 1);
    }

    //다이알로그
    public void Dialog(View view){
        final String[] Difficulty = {"쉬움 (5초}", "보통 (3초)", "어려움 (1초)"};
        final Integer[] sec = {5,3,1};
        final ArrayList<Integer> selectedItem = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#F44336'>시작하기</font>"));
        selectedItem.add(5);

        builder.setSingleChoiceItems(Difficulty, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem.clear();
                selectedItem.add(sec[which]);
            }
        });

        builder.setPositiveButton(Html.fromHtml("<font color='#F44336'>시작하기</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(rAdapter.getItemCount()<5)
                {
                    //문항수가 너무 적은 경우
                    Toast.makeText(MainActivity.this, "음악이 너무 적습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //랜덤
                    CreateExam ce =new CreateExam();
                    exam = new int[5];
                    exam = ce.create(index-1);

                    mPlayer.stop();
                    Intent gamePage = new Intent(getApplicationContext(), GamePage.class);
                    gamePage.putExtra("DBsize",index-1);
                    gamePage.putExtra("sec",selectedItem.get(0));
                    gamePage.putExtra("exam",exam);
                    gamePage.putStringArrayListExtra("items",items);
                    startActivity(gamePage);}
            }
        });

        builder.setNegativeButton(Html.fromHtml("<font color='#FF7171'>돌아가기</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //창 닫기
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.stop();
        musicPlay.setBackgroundResource(R.drawable.play);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                ; //이부분에 검색결과를 리스트에 추가하고 디베에 넣는 부분
                break;
            default:
                ;
                break;
        }
    }
}