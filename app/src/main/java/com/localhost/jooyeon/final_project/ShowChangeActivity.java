package com.localhost.jooyeon.final_project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

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
 * Created by asmwj on 2017-06-16.
 */

public class ShowChangeActivity extends AppCompatActivity {

    Long get_walkingNum=0L;
    RecyclerView recyclerView;
    TextView restKgTv;

    String get_kg;
    String get_date;

    Float goal_kg, current_kg, result_kg;
    String todayDate;



    LinearLayoutManager mLayoutManager;
    DatabaseReference myRef;
    ShowChangeAdapter adapter;
    ArrayList<CurrentUser> currentUserArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showchange);

        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));

        // sharedPreferences 에 저장된 목표 몸무게 가져오기
        goal_kg = sharedPreferences.getFloat("kg", 0);


        currentUserArrayList = new ArrayList<CurrentUser>();
        currentUserArrayList.clear();

        restKgTv = (TextView)findViewById(R.id.showChange_tv);

        recyclerView = (RecyclerView)findViewById(R.id.showChange_RV);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ShowChangeAdapter(this, R.layout.activity_item_layout, currentUserArrayList);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user_id);


        // 오늘 날짜를 가져오는 date 변수
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(date);


        // 오늘 날짜의 몸무게를 가져오기 위한 listener
        myRef.child("date").child(todayDate).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("kg")) {
                    current_kg = Float.valueOf(dataSnapshot.getValue().toString());
                    result_kg = current_kg-goal_kg;

                    if(result_kg > 0){
                        String result = String.format("목표 몸무게 까지 - %3.1fkg !!", result_kg);
                        restKgTv.setText(result);
                    }
                    else if(result_kg == 0){
                        String result = String.format("축하드립니다!! 목표 달성!!");
                        restKgTv.setText(result);
                    }
                    else if (result_kg < 0){
                        float absR=Math.abs(result_kg);
                        String result = String.format("목표 몸무게까지 +%3.1fkg!!", absR);
                        restKgTv.setText(result);
                    }
                }
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

        // 오늘 몸무게를 추가시킬 때 리스트도 추가되도록 하는 listener
        myRef.child("date").addChildEventListener(new ChildEventListener() {
            // 여기서 datasnapshot은 2017-xx-xx 를 가리킴
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ㅁㄴㅇㄹㅇㄴㅁㄹ","ㅁㄴㅇㄻㄴㄹ");
                get_date = dataSnapshot.getKey().toString();
                // kg, walkcnt 각각을 childDataSnapshot으로
                for(DataSnapshot childDataSnapshot:dataSnapshot.getChildren()) {
                    if (childDataSnapshot.getKey().equals("kg")) {
                        get_kg = childDataSnapshot.getValue().toString();
                    }
                    if (childDataSnapshot.getKey().equals("walkingNum")) {
                        get_walkingNum = (Long)childDataSnapshot.getValue();

                    }
                }
                currentUserArrayList.add(new CurrentUser(get_date, get_kg, get_walkingNum.toString()));
                adapter.notifyDataSetChanged();

                Log.d("추가","추가");
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
