package com.example.root.tappydefender;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("High Score",MODE_PRIVATE);
        final Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(this);
        TextView fastest = findViewById(R.id.highScore);
        long fastestTime = sharedPreferences.getLong("fastest Time", 1000000);
        fastest.setText("Fastest Time:"+ fastestTime);

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}
