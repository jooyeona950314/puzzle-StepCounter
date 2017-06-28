package com.localhost.jooyeon.final_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.localhost.jooyeon.final_project.GoalActivity.sharedPreferences;
import static com.localhost.jooyeon.final_project.MainActivity.database;

/**
 * Created by asmwj on 2017-06-17.
 */

public class SeekbarActivity extends AppCompatActivity {

    ImageView seekbar_iv;
    SeekBar seekBar;
    TextView seekbar_tv;
    TextView seekbar_date;

    ArrayList<com.localhost.jooyeon.final_project.SeekbarInfo> seekbarInfos;

    String today_kg;
    String today_img;
    String today_date;

    DatabaseReference myRef;

    int i = 0;

    String todayDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);

        // user_id 값 가져오기
        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user_id);

        seekbarInfos = new ArrayList<com.localhost.jooyeon.final_project.SeekbarInfo>();

        // 오늘 날짜를 가져오는 date 변수
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(date);

        seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekbar_iv = (ImageView)findViewById(R.id.seekbar_iv);
        seekbar_tv = (TextView)findViewById(R.id.seekbar_kg);
        seekbar_date = (TextView)findViewById(R.id.seekbar_date);


        myRef.child("date").addChildEventListener(new ChildEventListener() {

            // datasnapshot  = 2017xxxx, 2017xxxx
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                today_date = dataSnapshot.getKey().toString();
                for(DataSnapshot childDataSnapshot:dataSnapshot.getChildren()) {
                    if (childDataSnapshot.getKey().equals("kg")) {
                        today_kg = childDataSnapshot.getValue().toString();
                    }

                    if(childDataSnapshot.getKey().equals("uri")){
                        today_img = childDataSnapshot.getValue().toString();
                    }
                } // for문 끝

                seekbarInfos.add(new com.localhost.jooyeon.final_project.SeekbarInfo(today_date, today_img, today_kg));

                seekBar.setMax(i);

                i++;


                // seekbar listener
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        seekbar_tv.setText(seekbarInfos.get(progress).getKg());

                        seekbar_date.setText(seekbarInfos.get(progress).getDate());
                        if(seekbarInfos.get(progress).getUri() != null){
                            Glide.with(SeekbarActivity.this)
                                    .load(Uri.parse(seekbarInfos.get(progress).getUri()))
                                    .centerCrop()
                                    .override(600, 800)
                                    .into(seekbar_iv);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        seekbar_tv.setText(seekbarInfos.get(0).getKg());

                        seekbar_date.setText(seekbarInfos.get(0).getDate());
                        if(seekbarInfos.get(0).getUri() != null){
                            Glide.with(SeekbarActivity.this)
                                    .load(Uri.parse(seekbarInfos.get(0).getUri()))
                                    .centerCrop()
                                    .override(600, 800)
                                    .into(seekbar_iv);
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}