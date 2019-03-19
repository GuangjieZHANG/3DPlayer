package com.example.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class VideoPlayerActivity extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {

    private static final String TAG = VideoPlayerActivity.class.getSimpleName();

    private String route;
    private String name;
    private VideoView videoView;

    private MediaController mediaController;
    private GestureDetector gestureDetector;

    private int volumn = -1;
    private float brightness = -1f;

    private AudioManager audioManager;

    private int layout = VideoView.VIDEO_LAYOUT_ZOOM;

    private LinearLayout layout_volumn;
    private TextView volumn_number;
    private LinearLayout layout_brightness;
    private TextView brigntness_number;

    private boolean needResume = false;

    private class MyGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)
                onVolumnChange((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)
                onBrightressChange((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (layout == VideoView.VIDEO_LAYOUT_ZOOM)
                layout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                layout++;
            if (videoView != null)
                videoView.setVideoLayout(layout, 0);
            return true;
        }
    }

    private Handler dismissHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            layout_volumn.setVisibility(View.GONE);
            layout_brightness.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(!LibsChecker.checkVitamioLibs(this)){
            System.out.println("-----------LibsChecker failed!");
            finish();
            return;
        }

        setContentView(R.layout.vedioplayer);

        route = getIntent().getStringExtra("route");
        name = getIntent().getStringExtra("name");

        if(TextUtils.isEmpty(route)){
            finish();
            return;
        }

        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setOnCompletionListener(this);
        videoView.setOnInfoListener(this);

        layout_volumn = (LinearLayout) findViewById(R.id.video_volumn_layout);
        volumn_number = (TextView) findViewById(R.id.video_volumn_number);
        layout_brightness = (LinearLayout) findViewById(R.id.video_brightress_layout);
        brigntness_number = (TextView) findViewById(R.id.video_brightress_number);

/*        if(route.startsWith("http:")){
            videoView.setVideoURI(Uri.parse(route));
        }else{
            videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath()+route);
        }*/

        System.out.println(" ----------------set Route is "+Environment.getExternalStorageDirectory().getPath()+route);
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath()+route);

        mediaController = new MediaController(this);
        mediaController.setFileName(name);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        gestureDetector = new GestureDetector(this,new MyGesture());
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        System.out.println(" -------Test before start play-------");
        startPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onCompletion(MediaPlayer mp){

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what)
        {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if(isPlaying())
                {
                    stopPlayer();
                    needResume = true;
                }

                break;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (needResume)
                {
                    startPlayer();
                    needResume = false;
                }

                break;

            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                Log.i(TAG,"download:" + extra);

                break;

        }

        return true;
    }


    private void onVolumnChange(float p){
        if(volumn == -1){
            volumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            if(volumn < 0){
                volumn = 0;
            }
        }
        int maxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int index = (int)(p*maxVolumn) + volumn;
        if (index > 100){
            index = 100;
        }else if(index < 0){
            index = 0;
        }

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,index,0);
        layout_volumn.setVisibility(View.VISIBLE);
        volumn_number.setText(String.format("%d",(int)(index*100/maxVolumn)));
    }

    private void onBrightressChange(float p){
        if(brightness < 0){
            brightness = getWindow().getAttributes().screenBrightness;
            if(brightness <= 0.00f){
                brightness = 0.50f;
            }else if(brightness <= 0.01f){
                brightness = 0.01f;
            }
        }
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightness + p;
        if(layoutParams.screenBrightness > 1.0f){
            layoutParams.screenBrightness = 1.0f;
        }else if(layoutParams.screenBrightness<0.01f){
            layoutParams.screenBrightness = 0.01f;
        }
        getWindow().setAttributes(layoutParams);
        layout_brightness.setVisibility(View.VISIBLE);
        brigntness_number.setText(String.format("%d",(int)(layoutParams.screenBrightness*100/1)));
    }

    private void endGesture(){
        volumn = -1;
        brightness = -1f;
        dismissHandler.removeMessages(0);
        dismissHandler.sendEmptyMessageAtTime(0,500);
    }

    private void startPlayer(){
        if(videoView != null){
            System.out.println("Video view start -----------");
            videoView.start();
        }
    }

    private void stopPlayer(){
        if(videoView != null){
            videoView.pause();
        }
    }

    private boolean isPlaying(){
        return videoView!= null && videoView.isPlaying();
    }
}
