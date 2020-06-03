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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

/**
 * Created by STAR on 5/12/2017.
 */
public class TextActivity  extends AppCompatActivity {

    TextView m_sQuestion;
    TextView m_sProof;
    Button m_btnSend;
    Button m_btnSkip;
    TextView m_sTaskName;
    ImageButton m_btnInfo;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        //Create Admob Banner
        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3588586943214333/8701350408");

        db=openOrCreateDatabase("timeDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timemanage7(settime Integer,curtime Integer,curday Integer,curtask Integer,curpage Integer );");
        db.execSQL("UPDATE timemanage7 SET curpage=3");

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        m_btnSkip = (Button)findViewById(R.id.skipbtn);
        m_btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TextActivity.this, SkipActivity.class));
            }
        });

        m_btnInfo = (ImageButton)findViewById(R.id.m_btnInfoID);
        m_btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TextActivity.this);
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
        m_sQuestion = (TextView)findViewById(R.id.m_sQuestionID);
        m_sQuestion.setText(Globals.tasks[Globals.task_number]);
        m_sProof = (TextView)findViewById(R.id.m_sProofID);
        m_sProof.setText(Globals.proof[Globals.task_number]);

        m_sTaskName = (TextView)findViewById(R.id.m_sTaskID);
        m_sTaskName.setText(String.format("Task %d", Globals.task_number+1));

//        m_btnSkip = (Button)findViewById(R.id.m_btnSkipID);
//        m_btnSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Random rand1 = new Random();
//                int selector1 = rand1.nextInt(50);
//                m_sQuestion.setText(Globals.tasks[selector1]);
//                m_sProof.setText(Globals.proof[selector1]);
//                m_sTaskName.setText("Task " + selector1);
//            }
//        });

        m_btnSend = (Button)findViewById(R.id.m_bSENDID);
        m_btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send result

                Calendar cal = Calendar.getInstance();
                int second = cal.get(Calendar.SECOND);
                int minute = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int curDay = cal.get(Calendar.DAY_OF_YEAR);
                int curallsec=hour*3600+minute*60+second;
                String curmyDate=Integer.toString(curallsec);
                String curDaystr=Integer.toString(curDay);

                db.execSQL("UPDATE timemanage7 SET settime=86400,curtime="+curmyDate+",curday="+curDaystr+",curpage=1");
                Intent in = new Intent(getApplicationContext(), ResultActivity.class);
                in.putExtra("State", "NextState");
                startActivity(in);



            }
        });
    }

}