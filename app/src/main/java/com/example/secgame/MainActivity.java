package com.example.secgame;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView musicList;
    RecyclerView.LayoutManager layoutManager;
    String mp3Path = Environment.getExternalStorageDirectory().getPath() + "/Music/";
    ProgressBar musicPb;
    MediaPlayer mPlayer;
    TextView musicTitle;
    ImageButton musicPlay;
    Thread thread;
    public MyDBHelper myHelper;
    SQLiteDatabase musicDB;
    int index = 0;
    int exam[];
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("노래 목록");

        musicList = findViewById(R.id.music_recyclerview);
        musicList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        musicList.setLayoutManager(layoutManager);
        musicPb = findViewById(R.id.music_progressbar);
        musicTitle = findViewById(R.id.music_title);
        musicPlay = findViewById(R.id.music_play);
        items = new ArrayList<String>();

        //permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        //음악재생관련
        final ArrayList<MusicInfo> mp3List = new ArrayList<>();

        final Music_RecyclerAdapter rAdapter = new Music_RecyclerAdapter(mp3List);
        musicList.setAdapter(rAdapter);

        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;

        myHelper = new MyDBHelper(this);
        musicDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(musicDB,0,1);

        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if (extName.equals("mp3")){
                mp3List.add(new MusicInfo(R.drawable.music, fileName));
                items.add(fileName);
                //DB처리
                musicDB.execSQL("INSERT INTO musicInfo VALUES ( ' " + index++ + "' , '" + fileName + "' , '" + mp3Path+ fileName + "');");
            }
        }
        musicDB.close();
        mPlayer = new MediaPlayer();

        //recyclerView AdapterItemClickEvent
        rAdapter.setOnItemClickListener(new Music_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                try {
                    mPlayer.reset();
                    final MusicInfo selected = rAdapter.getItem(pos);
                    mPlayer.setDataSource(mp3Path + selected.getName());
                    mPlayer.prepare();
                    mPlayer.start();
                    musicPb.setMax(mPlayer.getDuration());
                    musicTitle.setText("재생중인 음악 : "+ selected.getName());
                    //이부분은 도려내보자
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

    //다이알로그
    public void Dialog(View view){
        //난이도 하드코딩한거 바꿔
        final String[] Difficulty = {"쉬움 (5초}", "보통 (3초)", "어려움 (1초)"};
        final Integer[] sec = {5,3,1};
        final ArrayList<Integer> selectedItem = new ArrayList<Integer>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("난이도 설정");
        selectedItem.add(5);

        //랜덤
        CreateExam ce =new CreateExam();
        exam = new int[5];
        exam = ce.create(index-1);

        builder.setSingleChoiceItems(Difficulty, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem.clear();
                selectedItem.add(sec[which]);
            }
        });

        builder.setPositiveButton("시작하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPlayer.stop();
                Intent gamePage = new Intent(getApplicationContext(), GamePage.class);
                gamePage.putExtra("DBsize",index-1);
                gamePage.putExtra("sec",selectedItem.get(0));
                gamePage.putExtra("exam",exam);
                gamePage.putStringArrayListExtra("items",items);
                startActivity(gamePage);
                //난이도 넘겨주기
            }
        });

        builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //창 닫기
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void customitem(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정");

        builder.setMessage("곡 이름 : ");

        final EditText ed = new EditText(MainActivity.this);
        builder.setView(ed);

        builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //수정하고 db에 반영하기
            }
        });

        builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //창 닫기
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}