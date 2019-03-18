package com.example.player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import database.SQLiteHelper;
import database.Video;
import view.VideoAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView videoList;
    private Button selectVideo;
    private List<Video> videos;
    private VideoAdapter videoAdapter;

    private SQLiteHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = SQLiteHelper.getInstance(getApplicationContext());
        database.createSqliteDatabase(getApplicationContext());
        getData();

        selectVideo = (Button)findViewById(R.id.select_video);
        videoList = (ListView)findViewById(R.id.videolist);
        videoAdapter = new VideoAdapter(MainActivity.this,R.layout.videoitem,videos);
        videoList.setAdapter(videoAdapter);

        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video = (Video)videoAdapter.getItem(position);
                //System.out.println("You have clicked video "+video.getId());
                File dstFile = new File(video.getRoute());
                if(dstFile.exists()){
                    Intent intent = new Intent(MainActivity.this,VideoPlayerActivity.class);
                    intent.putExtra("name",video.getName());
                    intent.putExtra("route",video.getRoute());
                    startActivity(intent);
                }else {
                    System.out.println("This file doesn't exist.");
                }
            }
        });

        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent intent = new Intent(MainActivity.this,FileExplorerActivity.class);
                MainActivity.this.startActivity(intent);*/
            }
        });
    }

    private void getData(){
        videos = new ArrayList<Video>();
        database.printVideos();
        videos = database.getAllVideos();
    }
}
