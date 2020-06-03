package com.andy.bluewhalechallenge2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Timer;

/**
 * Created by STAR on 5/11/2017.
 */
public class SelectActivity extends AppCompatActivity {

    Button m_btnYES;
    Button m_btnNO;
    ImageButton m_btnInfo;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //Create Admob Banner
       // MobileAds.initialize(getApplicationContext(), "ca-app-pub-3588586943214333/8701350408");
////////////////////////////

        db=openOrCreateDatabase("timeDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timemanage7(settime Integer,curtime Integer,curday Integer,curtask Integer,curpage Integer );");
        db.execSQL("UPDATE timemanage7 SET curpage=2");


        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        m_btnYES = (Button)findViewById(R.id.m_btnYESID);
        m_btnNO = (Button)findViewById(R.id.m_btnNOID);

        m_btnInfo = (ImageButton)findViewById(R.id.m_btnInfoID);
        m_btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SelectActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Blue Whale Challenge");
                dialog.setMessage("The rules are simple, there will be 50 tasks in total, you will be given a task every 24 hours after completing the previous task, to prove the task has been complete you are required to upload a picture or text which will then be verified for you to progress onto the next task." );
                dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });



        final Timer time = new Timer();

        m_btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*
                if (Globals.photoAnswer[Globals.task_number])
                {
                        time.schedule(new TimerTask() {
                            public void run() {

                                startActivity(new Intent(SelectActivity.this, MainActivity.class));
                                finish();
                            }

                        }, 1500);

                }else {
                        time.schedule(new TimerTask() {
                            public void run() {

                                startActivity(new Intent(SelectActivity.this, TextActivity.class));
                                finish();
                            }

                        }, 1500);
                    }
                    */
                if (Globals.photoAnswer[Globals.task_number])
                {
                            startActivity(new Intent(SelectActivity.this, MainActivity.class));
                            finish();

                }else {

                            startActivity(new Intent(SelectActivity.this, TextActivity.class));
                            finish();
                        }


                }
            });
        m_btnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("UPDATE timemanage7 SET curpage=10");
                startActivity(new Intent(SelectActivity.this, IntroActivity.class));
            }
        });
    }
}