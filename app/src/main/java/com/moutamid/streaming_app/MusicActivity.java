package com.moutamid.streaming_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.webilisim.webplayer.WEBPlayer;
import net.webilisim.webplayer.WEBPlayerStd;

public class MusicActivity extends AppCompatActivity {
    WEBPlayerStd webPlayerStd;
    TextView link , name ;

    ImageView dec_sound , inc_sound;
    ImageView cast;
    ImageView stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        link = findViewById(R.id.link2);
        name = findViewById(R.id.name2);

        dec_sound = findViewById(R.id.dec_sound2);
        inc_sound = findViewById(R.id.inc_sound2);
        cast = findViewById(R.id.cast2);
        stop = findViewById(R.id.stop2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            link.setText(bundle.getString("link2"));
            name.setText(bundle.getString("name2"));
        }

        String link_text = link.getText().toString().trim();
        String name_text = name.getText().toString().trim();

        webPlayerStd = findViewById(R.id.webplayer2);
        webPlayerStd.setUp(link_text , name_text);

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        dec_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        inc_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.settings.CAST_SETTINGS"));
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MusicActivity.this , MainActivity.class);
                startActivity(intent);
                Toast.makeText(MusicActivity.this, "Stop", Toast.LENGTH_SHORT).show();
            }
        });

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