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

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button m_btnSend;
    Button m_btnSkip;
    ImageButton m_btnCamera;
    ImageButton m_btnInfo;

    Uri fileURI;
    String file_path = "";
    String filename = "";
    int CAMERA = 100;
    int IMAGE = 300;
    Bitmap mLocalBmp;
    byte imageBytes[];

    ImageView mPhoto;
    TextView m_sQuestion;
    TextView m_sProof;
    TextView m_sTaskName;
    Cursor c1;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db=openOrCreateDatabase("timeDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timemanage7(settime Integer,curtime Integer,curday Integer,curtask Integer,curpage Integer );");
        db.execSQL("UPDATE timemanage7 SET curpage=7");

       // Create Admob Banner
        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        m_btnSkip = (Button)findViewById(R.id.skipbtn);
        m_btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SkipActivity.class));
            }
        });

        m_btnInfo = (ImageButton)findViewById(R.id.m_btnInfoID);
        m_btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
                /////////////
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

        m_btnCamera = (ImageButton)findViewById(R.id.m_bCameraID);
        m_btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //image upload from camera or Gallary
                CharSequence[] str = { "Camera", "Pictures", "Cancel" };
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT);
                alertDialogBuilder
                        .setItems(str, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        String fName = DateFormat.getDateTimeInstance().format(new Date()) + ".jpg";
                                        ContentValues values = new ContentValues();
                                        values.put(MediaStore.Images.Media.TITLE, fName);
                                        fileURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                                        Intent iintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        iintent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);

                                        if (iintent.resolveActivity(getPackageManager()) != null)
                                            startActivityForResult(iintent, CAMERA);
                                        break;
                                    case 1:
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        if (intent.resolveActivity(getPackageManager()) != null)
                                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE);
                                        break;
                                    default:
                                        break;

                                }
                            }
                        });
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.show();
            }
        });

        //initalization
        mPhoto = (ImageView)findViewById(R.id.m_iPhotoID);
        m_sQuestion = (TextView)findViewById(R.id.m_sQuestionID);
        m_sProof = (TextView)findViewById(R.id.m_sProofID);
        m_sTaskName = (TextView)findViewById(R.id.m_sTaskID);
        m_sTaskName.setText(String.format("Task %d", Globals.task_number+1));

        m_sQuestion.setText(Globals.tasks[Globals.task_number]);
        m_sProof.setText(Globals.proof[Globals.task_number]);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
    }
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA) {

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(fileURI, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                file_path = cursor.getString(column_index_data);
                filename = file_path.substring(file_path.lastIndexOf("/") + 1);

                Bitmap myBitmap = BitmapFactory.decodeFile(file_path);
//              ivuser.setImageBitmap(myBitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageBytes = baos.toByteArray();

                mLocalBmp = myBitmap;
                mPhoto.setImageBitmap(mLocalBmp);

//                encodeBitmapAndSaveToFirebase();

            } else if (requestCode == IMAGE) {

                Uri selectedImageUri = data.getData();
                file_path = getPath(selectedImageUri);

                if(file_path == null) {
                    Log.e("EditAccountActivity","Null Point Error!!!!");
                    Log.e("EditAccountActivity",selectedImageUri.getPath());
                    Toast.makeText(this, "Unkown Error!", Toast.LENGTH_LONG);
                    return;
                }
                filename = file_path.substring(file_path.lastIndexOf("/") + 1);


                Bitmap myBitmap = BitmapFactory.decodeFile(file_path);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imageBytes = baos.toByteArray();

                mLocalBmp = myBitmap;
                mPhoto.setImageBitmap(mLocalBmp);
            }
        }
    }
    public String getPath(Uri uri) {
        // just some safety built in

        if (uri == null) {
            return null;
        }
        String result;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
