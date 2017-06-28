package com.localhost.jooyeon.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;

import static com.localhost.jooyeon.final_project.GoalActivity.sharedPreferences;

public class ColoringActivity extends AppCompatActivity {

    //파이어베이스 연동 및 레퍼런스
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mpuzzleGameRef;
    DatabaseReference mUserPuzzle;
    DatabaseReference mUserSetPuzzle;

    int[] arr = new int[12];
    HashMap userPuzzleMap = null;
    int[][] imageResId = {
        {R.drawable.piece1_1,R.drawable.piece1_2,R.drawable.piece1_3,R.drawable.piece1_4,R.drawable.piece1_5,R.drawable.piece1_6,
                R.drawable.piece1_7,R.drawable.piece1_8,R.drawable.piece1_9,R.drawable.piece1_10,R.drawable.piece1_11,R.drawable.piece1_12},

            {R.drawable.piece2_1,R.drawable.piece2_2,R.drawable.piece2_3,R.drawable.piece2_4,R.drawable.piece2_5,R.drawable.piece2_6,
                    R.drawable.piece2_7,R.drawable.piece2_8,R.drawable.piece2_9,R.drawable.piece2_10,R.drawable.piece2_11,R.drawable.piece2_12},

            {R.drawable.piece3_1,R.drawable.piece3_2,R.drawable.piece3_3,R.drawable.piece3_4,R.drawable.piece3_5,R.drawable.piece3_6,
                    R.drawable.piece3_7,R.drawable.piece3_8,R.drawable.piece3_9,R.drawable.piece3_10,R.drawable.piece3_11,R.drawable.piece3_12},

            {R.drawable.piece4_1,R.drawable.piece4_2,R.drawable.piece4_3,R.drawable.piece4_4,R.drawable.piece4_5,R.drawable.piece4_6,
                    R.drawable.piece4_7,R.drawable.piece4_8,R.drawable.piece4_9,R.drawable.piece4_10,R.drawable.piece4_11,R.drawable.piece4_12}
    };
    @Override
    protected void onStart() {
        super.onStart();

        //사용자가 갖고 있는 퍼즐의 종류 파악
        mUserPuzzle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap map = (HashMap)dataSnapshot.getValue();
                userPuzzleMap = map;

                for(int i = 0 ; i < 12 ; i++)
                {
                    Long userPuzzle = (Long)map.get("puzzle"+Integer.toString(i+1));
                    if(userPuzzle==1){
                        arr[i] = 1;
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //이전에 설정한 퍼즐 배치를 가져옴
        mUserSetPuzzle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap map = (HashMap)dataSnapshot.getValue();
                userPuzzleMap = map;

                for(int i = 0 ; i < 12 ; i++)
                {
                    Long userPuzzle = (Long)map.get("puzzle"+Integer.toString(i+1));
                    if(userPuzzle!=0){
                        imgArr[i].setImageResource(userPuzzle.intValue());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Integer[] mImageIds = {
            R.drawable.puzzle1,
            R.drawable.puzzle2,
            R.drawable.puzzle3,
            R.drawable.puzzle4
    };
    Integer position;
    ImageView [] imgArr = new ImageView[12];
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));


        setContentView(R.layout.coloring_page);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0)+1;
        mpuzzleGameRef = mRootRef.child(user_id+"/puzzleGame/picture"+position.toString());

        Button backButton = (Button)findViewById(R.id.backButton);
        final ImageButton puzzleButton1 = (ImageButton)findViewById(R.id.puzzleButton1);
        final ImageButton puzzleButton2 = (ImageButton)findViewById(R.id.puzzleButton2);
        final ImageButton puzzleButton3 = (ImageButton)findViewById(R.id.puzzleButton3);
        final ImageButton puzzleButton4 = (ImageButton)findViewById(R.id.puzzleButton4);
        final ImageButton puzzleButton5 = (ImageButton)findViewById(R.id.puzzleButton5);
        final ImageButton puzzleButton6 = (ImageButton)findViewById(R.id.puzzleButton6);
        final ImageButton puzzleButton7 = (ImageButton)findViewById(R.id.puzzleButton7);
        final ImageButton puzzleButton8 = (ImageButton)findViewById(R.id.puzzleButton8);
        final ImageButton puzzleButton9 = (ImageButton)findViewById(R.id.puzzleButton9);
        final ImageButton puzzleButton10 = (ImageButton)findViewById(R.id.puzzleButton10);
        final ImageButton puzzleButton11 = (ImageButton)findViewById(R.id.puzzleButton11);
        final ImageButton puzzleButton12 = (ImageButton)findViewById(R.id.puzzleButton12);
        position--;
        imgArr[0] = puzzleButton1;imgArr[1] = puzzleButton2;imgArr[2] = puzzleButton3;
        imgArr[3] = puzzleButton4;imgArr[4] = puzzleButton5;imgArr[5] = puzzleButton6;
        imgArr[6] = puzzleButton7;imgArr[7] = puzzleButton8;imgArr[8] = puzzleButton9;
        imgArr[9] = puzzleButton10;imgArr[10] = puzzleButton11;imgArr[11] = puzzleButton12;

        mUserPuzzle = mpuzzleGameRef.child("userPuzzle");
        mUserSetPuzzle = mpuzzleGameRef.child("userSetPuzzle");
        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼1에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                puzzleButton1.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼2에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle2").setValue(imageResId[position][index]);
                                puzzleButton2.setImageResource(imageResId[position][index]);

                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle2").setValue(R.drawable.foot);
                        puzzleButton2.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });


                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼3에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle3").setValue(imageResId[position][index]);
                                puzzleButton3.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle3").setValue(R.drawable.foot);
                        puzzleButton3.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼4에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle4").setValue(imageResId[position][index]);
                                puzzleButton4.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle4").setValue(R.drawable.foot);
                        puzzleButton4.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼5에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle5").setValue(imageResId[position][index]);
                                puzzleButton5.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle5").setValue(R.drawable.foot);
                        puzzleButton5.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼6에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle6").setValue(imageResId[position][index]);
                                puzzleButton6.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle6").setValue(R.drawable.foot);
                        puzzleButton6.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼7에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle7").setValue(imageResId[position][index]);
                                puzzleButton7.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle7").setValue(R.drawable.foot);
                        puzzleButton7.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼8에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle8").setValue(imageResId[position][index]);
                                puzzleButton8.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle8").setValue(R.drawable.foot);
                        puzzleButton8.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼9에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle9").setValue(imageResId[position][index]);
                                puzzleButton9.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle9").setValue(R.drawable.foot);
                        puzzleButton9.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼10에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle10").setValue(imageResId[position][index]);
                                puzzleButton10.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle10").setValue(R.drawable.foot);
                        puzzleButton10.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼11에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle11").setValue(imageResId[position][index]);
                                puzzleButton11.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle11").setValue(R.drawable.foot);
                        puzzleButton11.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //퍼즐 클릭 시 대화상자에 사용자가 갖고 있는 퍼즐 종류를 출력 후 선택한 퍼즐을 퍼즐버튼12에 위치시키고 그 값을 파이어베이스에 저장
        puzzleButton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ColoringActivity.this);
                View view = View.inflate(ColoringActivity.this,R.layout.puzzle_dialog,null);
                final AlertDialog ad = ab.create();
                LinearLayout dialog_linear = (LinearLayout)view.findViewById(R.id.dialog_linear);
                for(int i = 0 ; i < 12 ; i++){
                    if(arr[i]==1){
                        final ImageView imageView = new ImageView(ColoringActivity.this);
                        imageView.setImageResource(imageResId[position][i]);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,300);
                        lp.setMargins(5,0,0,5);
                        imageView.setLayoutParams(lp);
                        dialog_linear.addView(imageView);
                        final int index = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mpuzzleGameRef.child("userSetPuzzle/puzzle12").setValue(imageResId[position][index]);
                                puzzleButton12.setImageResource(imageResId[position][index]);
                                ad.dismiss();
                            }
                        });
                    }
                }
                ImageView foot_image = (ImageView)view.findViewById(R.id.foot_image);

                foot_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mpuzzleGameRef.child("userSetPuzzle/puzzle12").setValue(R.drawable.foot);
                        puzzleButton12.setImageResource(R.drawable.foot);
                        ad.dismiss();
                    }
                });

                ad.setView(view);
                ad.show();
            }
        });

        //뒤로가기 버튼 누를 경우 뒤로가기 기능 구현
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
