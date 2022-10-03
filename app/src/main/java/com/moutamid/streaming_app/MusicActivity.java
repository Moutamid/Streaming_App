package com.moutamid.streaming_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import net.webilisim.webplayer.WEBPlayer;
import net.webilisim.webplayer.WEBPlayerStd;

public class MusicActivity extends AppCompatActivity {

    WEBPlayerStd webPlayerStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        webPlayerStd = findViewById(R.id.webplayer);
        webPlayerStd.setUp("https://hls-01-radiorecord.hostingradio.ru/record/96/playlist.m3u8" , "Name of Radio");

    }

    @Override
    public void onBackPressed() {
        if (WEBPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        WEBPlayer.releaseAllVideos();
    }
}