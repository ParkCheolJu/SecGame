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
import android.widget.Toast;

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
    int index, version;
    int exam[];
    ArrayList<String> items;
    Music_RecyclerAdapter rAdapter;
    ArrayList<MusicInfo> mp3List;

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

        myHelper = new MyDBHelper(this);
        musicDB = myHelper.getWritableDatabase();

        mp3List = new ArrayList<>();
        rAdapter = new Music_RecyclerAdapter(mp3List);
        musicList.setAdapter(rAdapter);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);


        mPlayer = new MediaPlayer();

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
            public void onItemLongClick(View v, int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final String name = rAdapter.getItem(pos).getName();
                builder.setMessage("수정");
                final int position = pos;
                final EditText ed = new EditText(MainActivity.this);
                builder.setView(ed);
                ed.setText(name);

                builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = ed.getText().toString();
                        MusicInfo musicInfo = new MusicInfo(R.drawable.music,newName, mp3Path + name);
                        items.set(position,newName);
                        rAdapter.musicInfoArrayList.set(position, musicInfo);
                        rAdapter.notifyItemChanged(position);
                        musicDB = myHelper.getWritableDatabase();
                        musicDB.execSQL("UPDATE musicInfo SET mCustomName = '" + newName + "' WHERE mIndex = " + position+";");
                        musicDB.close();
                        dialog.dismiss();
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

    public void LoadMusic(View view){
        musicDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(musicDB,version,++version);

        mp3List.clear();

        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;

        for (File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3);
            if (extName.equals("mp3")){
                mp3List.add(new MusicInfo(R.drawable.music, fileName, mp3Path + fileName));
                items.add(fileName);
                //DB처리
                musicDB.execSQL("INSERT INTO musicInfo VALUES ( ' " + index++ + "' , '" + fileName + "' , '" + mp3Path+ fileName + "');");
            }
        }
        musicDB.close();
        rAdapter.notifyDataSetChanged();
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
                if(rAdapter.getItemCount()<5)
                {
                    //문항수가 너무 적은 경우
                    Toast.makeText(MainActivity.this, "음악이 너무 적습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    mPlayer.stop();
                    Intent gamePage = new Intent(getApplicationContext(), GamePage.class);
                    gamePage.putExtra("DBsize",index-1);
                    gamePage.putExtra("sec",selectedItem.get(0));
                    gamePage.putExtra("exam",exam);
                    gamePage.putStringArrayListExtra("items",items);
                    startActivity(gamePage);}
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