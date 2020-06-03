package com.andy.bluewhalechallenge2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.StrictMath.abs;

/**
 * Created by STAR on 5/12/2017.
 */
public class ResultActivity extends AppCompatActivity {

    ProgressBar  m_pChecking;
    TextView m_tState;
    TextView m_tTimer;
    ImageView m_iCheckMark;
    ImageButton m_btnInfo;
    TextView textView3;
    int dayTime = 86400;
    int getDay;
    int curDay;
    //int dayTime = 10;
    int hh = 0;
    int mm = 0;
    int ss = 0;
    Cursor c1;
    SQLiteDatabase db;
    String str;
    String curmyDate;
    String curDaystr;
    Calendar cal;
    int second;
    int minute;
    int hour;
    Timer timer;
    int curallsec;
    TextView textView2;
    int tertime=1000;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            m_pChecking.setVisibility(View.INVISIBLE);
            m_iCheckMark.setVisibility(View.VISIBLE);
            m_tState.setText("ANSWER VERIFIED!");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);



        //Create Admob Banner
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3588586943214333/8701350408");

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
//////////////////////////////////////////////////////////////////////////


         cal = Calendar.getInstance();
         second = cal.get(Calendar.SECOND);
         minute = cal.get(Calendar.MINUTE);
         hour = cal.get(Calendar.HOUR_OF_DAY);
        curDay = cal.get(Calendar.DAY_OF_YEAR);
         curallsec=hour*3600+minute*60+second;
        curmyDate=Integer.toString(curallsec);
        curDaystr=Integer.toString(curDay);
/////////////////////////////////////////////////////////////////////////////
        db=openOrCreateDatabase("timeDB",Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timemanage7(settime Integer,curtime Integer,curday Integer,curtask Integer,curpage Integer );");

        ///////////////////////////////////////

        /////////////////////////////////
        Intent i = getIntent();

        String State = i.getStringExtra("State");
        if (State.equals("NextState")){
            dayTime=86400;
        }
        else {
            c1=db.rawQuery("SELECT * FROM timemanage7", null);
            if(c1.getCount()==1) {
                c1.moveToLast();
                dayTime = c1.getInt(0);//.parseInt(c1.getString(0).toString());

                int getoldtime = c1.getInt(1);
                int oldday = c1.getInt(2);
                Globals.task_number = c1.getInt(3);
                curDay = cal.get(Calendar.DAY_OF_YEAR);//.DATE);//
                if (curDay > oldday) {
                    curallsec = (hour + (curDay - oldday) * 24) * 3600 + minute * 60 + second;
                } else {
                    curallsec = hour * 3600 + minute * 60 + second;
                }

                int deffitime = curallsec - getoldtime;
                String s = Integer.toString(Globals.task_number);
                textView2 = (TextView) findViewById(R.id.textView2);
                textView2.setText(s);

                textView3 = (TextView) findViewById(R.id.textView3);
                textView3.setText(Integer.toString(deffitime) + "dayTime=" + Integer.toString(dayTime));
                if (abs(deffitime) > abs(dayTime)) {
                    Log.d("dayTime", Integer.toString(dayTime));
                    textView3.setText(Integer.toString(deffitime) + "dayTime=" + Integer.toString(dayTime));
                    dayTime = 1;
                } else {
                    dayTime = dayTime - abs(deffitime);
                }
            }
    }
        /////////////////////////////////////

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        m_tTimer = (TextView) findViewById(R.id.textView);
                        // task to be done every 1000 milliseconds

                        dayTime = dayTime - 1;
                        if (dayTime == 0){
                                Globals.task_number++;
                                if (Globals.task_number > 47){
                                    Globals.task_number = 47;
                                }

                            hh = dayTime/3600;
                            mm = (dayTime%3600)/60;
                            ss = (dayTime%3600)%60;
                            m_tTimer.setText(String.format("%02d:%02d:%02d", hh, mm, ss));
                            str=Integer.toString(dayTime);
                            String strtask=Integer.toString(Globals.task_number);
                            db.execSQL("UPDATE timemanage7 SET settime=86400,curtime="+curmyDate+",curday="+curDaystr+",curtask="+strtask);//,curpage=1");

                                startActivity(new Intent(ResultActivity.this, OperlogoActivity.class));
                                finish();
                        }else {
                            hh = dayTime / 3600;
                            mm = (dayTime % 3600) / 60;
                            ss = (dayTime % 3600) % 60;
                            m_tTimer.setText(String.format("%02d:%02d:%02d", hh, mm, ss));

                            str = Integer.toString(dayTime);
                            String strtask = Integer.toString(Globals.task_number);
                            db.execSQL("UPDATE timemanage7 SET settime=" + str + ",curtime=" + curmyDate + ",curday=" + curDaystr + ",curtask=" + strtask);//,curpage=1");

                            //textView2.setText("settime=" + str + "\n,curtime=" + curmyDate + "\n,curday=" + curDaystr + "\n,curtask=" + strtask + "\n,curpage=1");

                        }

                    }
                });

            }
        }, 0, tertime);

        m_btnInfo = (ImageButton)findViewById(R.id.m_btnInfoID);
        m_btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ResultActivity.this);
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
        m_pChecking = (ProgressBar)findViewById(R.id.progress1);
        m_pChecking.setVisibility(View.VISIBLE);
        m_tState = (TextView)findViewById(R.id.m_sCheckingID);
        m_iCheckMark = (ImageView)findViewById(R.id.m_iCheckID);

        Random random = new Random();
        int rand_time = random.nextInt(6900000);
        int selected_time = rand_time + 300000;

        handler.postDelayed(runnable, selected_time);
        //Local Notification

  //     Intent intent = new Intent(ResultActivity.this, MainActivity.class);
  //      PendingIntent contentIntent = PendingIntent.getActivity(ResultActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        NotificationCompat.Builder b = new NotificationCompat.Builder(ResultActivity.this);

//      b.setAutoCancel(true)
//            .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.check)
//                .setTicker("Hearty365")
//                .setContentTitle("Default notification")
//                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
//               .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
//                .setContentIntent(contentIntent)
//                .setContentInfo("Info");


//        NotificationManager notificationManager = (NotificationManager) ResultActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, b.build());
    }

    @Override
    public void onBackPressed(){

    }
}