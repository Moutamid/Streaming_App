package com.moutamid.streaming_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import net.webilisim.webplayer.WEBPlayer;
import net.webilisim.webplayer.WEBPlayerStd;

public class VedioActivity extends AppCompatActivity {

    WEBPlayerStd webPlayerStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);

        webPlayerStd = findViewById(R.id.webplayer);
        webPlayerStd.setUp("https://radio-live-mg.rtr-vesti.ru/live/smil:r1.smil/variant.m3u8" , "Name of Channel");
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