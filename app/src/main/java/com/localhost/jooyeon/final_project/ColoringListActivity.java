package com.localhost.jooyeon.final_project;

/**
 * Created by yeona on 2017. 5. 27..
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;
import java.util.Random;

import static com.localhost.jooyeon.final_project.GoalActivity.sharedPreferences;

public class ColoringListActivity extends Activity {
    /** Called when the activity is first created. */

    String user_id;
    ImageView [] imageView;
    ImageView galleryView;
    int position=0;
    Button pickPuzzle;
    Long puzzleCount;
    boolean isStart = false;

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

    int puzzleArray[][]=new int[4][12];

    //파이어베이스 연동 및 레퍼런스
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mpuzzleCountRef;
    DatabaseReference mpuzzleGameRef;

    @Override
    protected void onStart() {
        super.onStart();
        mpuzzleCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            //퍼즐 뽑기 여부 확인을 위한 값 초기화
            public void onDataChange(DataSnapshot dataSnapshot) {
                puzzleCount = (Long)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mpuzzleGameRef.addValueEventListener(new ValueEventListener() {
            @Override
            //파이어베이스에 저장된 퍼즐별 사용자가 갖고 있는 퍼즐 종류를 가져옴
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap puzzleGameMap = (HashMap)dataSnapshot.getValue();

                HashMap picture1Map = (HashMap)puzzleGameMap.get("picture1");
                HashMap picture2Map = (HashMap)puzzleGameMap.get("picture2");
                HashMap picture3Map = (HashMap)puzzleGameMap.get("picture3");
                HashMap picture4Map = (HashMap)puzzleGameMap.get("picture4");

                HashMap userPuzzle1Map = (HashMap)picture1Map.get("userPuzzle");
                HashMap userPuzzle2Map = (HashMap)picture2Map.get("userPuzzle");
                HashMap userPuzzle3Map = (HashMap)picture3Map.get("userPuzzle");
                HashMap userPuzzle4Map = (HashMap)picture4Map.get("userPuzzle");


                for(int i=0;i<12;i++){
                    Long puzzle = ((Long)userPuzzle1Map.get("puzzle"+Integer.toString(i+1)));
                    if(puzzle==null)
                        return;
                    puzzleArray[0][i] = puzzle.intValue();
                }
                for(int i=0;i<12;i++){
                    Long puzzle = ((Long)userPuzzle2Map.get("puzzle"+Integer.toString(i+1)));
                    if(puzzle==null)
                        return;
                    puzzleArray[1][i] = ((Long)userPuzzle2Map.get("puzzle"+Integer.toString(i+1))).intValue();
                }
                for(int i=0;i<12;i++){
                    Long puzzle = ((Long)userPuzzle3Map.get("puzzle"+Integer.toString(i+1)));
                    if(puzzle==null)
                        return;
                    puzzleArray[2][i] = ((Long)userPuzzle3Map.get("puzzle"+Integer.toString(i+1))).intValue();
                }
                for(int i=0;i<12;i++){
                    Long puzzle = ((Long)userPuzzle4Map.get("puzzle"+Integer.toString(i+1)));
                    if(puzzle==null)
                        return;
                    puzzleArray[3][i] = ((Long)userPuzzle4Map.get("puzzle"+Integer.toString(i+1))).intValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloring_list);

        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));


        mpuzzleCountRef = mRootRef.child(user_id+"/puzzleGame/puzzleCount");
        mpuzzleGameRef = mRootRef.child(user_id+"/puzzleGame");

        pickPuzzle = (Button)findViewById(R.id.pickPuzzle);
        galleryView = (ImageView)findViewById(R.id.galleryView);
        Gallery g = (Gallery) findViewById(R.id.gallery);
        final ImageAdapter imageAdapter=new ImageAdapter(this);
        g.setAdapter(imageAdapter);
        galleryView.setImageResource(imageAdapter.getImageId(0));

        //갤러리에서 선택한 퍼즐에 대한 이미지 출력
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                galleryView.setImageResource(imageAdapter.getImageId(position));
                ColoringListActivity.this.position=position;
            }
        });

        //선택한 퍼즐로 시작
        galleryView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(ColoringListActivity.this, ColoringActivity.class);
                 intent.putExtra("position", position);

                 startActivity(intent);
             }
         });

        //퍼즐 뽑기 버튼 클릭 시 일정 횟수 이상 시 퍼즐을 뽑고, 안되면 못뽑음
        pickPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(puzzleCount>=100){
                    Toast.makeText(getApplicationContext(), puzzleCount.toString(), Toast.LENGTH_SHORT).show();
                    mpuzzleCountRef.setValue(puzzleCount-100);

                    Random random = new Random(System.currentTimeMillis());

                    AlertDialog.Builder pickDialog = new AlertDialog.Builder(ColoringListActivity.this);
                    pickDialog.setTitle("축하합니다!!");

                    //사용자가 갖고 있지 않은 퍼즐을 알아내여 그 중 랜덤으로 퍼즐을 제공
                    while(true){
                        int pictureNum = Math.abs(random.nextInt())%4;
                        int puzzleNum = Math.abs(random.nextInt())%12;

                        if(puzzleArray[pictureNum][puzzleNum]==0){
                            mpuzzleGameRef.child("picture"+Integer.toString(pictureNum+1)).child("userPuzzle").child("puzzle"+Integer.toString(puzzleNum+1)).setValue(1);
                            View view = View.inflate(ColoringListActivity.this,R.layout.pick_puzzle_dialog,null);
                            ImageView pickPuzzleImageView = (ImageView)view.findViewById(R.id.pickPuzzleImageView);
                            pickPuzzleImageView.setImageResource(imageResId[pictureNum][puzzleNum]);
                            pickPuzzleImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            pickDialog.setView(view);
                            pickDialog.show();
                            break;
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "100번 이상 걸어야 뽑을 수 있습니다! (현재 누적 횟수: "+Long.toString(puzzleCount)+")", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}