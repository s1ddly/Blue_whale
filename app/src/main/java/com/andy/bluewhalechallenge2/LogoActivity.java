package com.andy.bluewhalechallenge2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by STAR on 5/11/2017.
 */
public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Random rand = new Random();
        int selector = rand.nextInt(50);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {

                startActivity(new Intent(LogoActivity.this, IntroActivity.class));
                finish();
            }

        }, 1500);
    }
}