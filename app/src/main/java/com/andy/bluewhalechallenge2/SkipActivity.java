package com.andy.bluewhalechallenge2;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


/**
 * Created by Admin on 25/10/2017.
 */

public class SkipActivity extends AppCompatActivity implements RewardedVideoAdListener{
    private RewardedVideoAd skipbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);

        skipbtn = MobileAds.getRewardedVideoAdInstance(this);
        skipbtn.setRewardedVideoAdListener(this);

        loadad();
    }

    private void loadad() {
        skipbtn.loadAd("ca-app-pub-3588586943214333/7492081600", new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
        if(Globals.task_number < 49){
            Globals.task_number ++;
        }
        if(Globals.photoAnswer[Globals.task_number] == true){
            startActivity(new Intent(SkipActivity.this, MainActivity.class));
        } else if(Globals.photoAnswer[Globals.task_number] == false){
            startActivity(new Intent(SkipActivity.this, TextActivity.class));
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        if(Globals.photoAnswer[Globals.task_number] == true){
            startActivity(new Intent(SkipActivity.this, MainActivity.class));
        } else if(Globals.photoAnswer[Globals.task_number] == false){
            startActivity(new Intent(SkipActivity.this, TextActivity.class));
        }
    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(Globals.photoAnswer[Globals.task_number] == true){
            startActivity(new Intent(SkipActivity.this, MainActivity.class));
        } else if(Globals.photoAnswer[Globals.task_number] == false){
            startActivity(new Intent(SkipActivity.this, TextActivity.class));
        }
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        if(Globals.photoAnswer[Globals.task_number] == true){
            startActivity(new Intent(SkipActivity.this, MainActivity.class));
        } else if(Globals.photoAnswer[Globals.task_number] == false){
            startActivity(new Intent(SkipActivity.this, TextActivity.class));
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        skipbtn.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }
}

