package com.localhost.jooyeon.final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.localhost.jooyeon.final_project.GoalActivity.sharedPreferences;


public class MainActivity extends Activity  {

    String user_id;
    public static int cnt = 0;

    private TextView tView;

    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    boolean flag = false;

    private static final int SHAKE_THRESHOLD = 2800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    Long puzzleCount;

    //파이어베이스 연동 및 레퍼런스
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDateRef;
    DatabaseReference mPuzzleCountRef;

    private final long FINSH_INTERVAL_TIME = 2000; //2초
    private long backPressedTime = 0;

    Button showchangeBtn;
    Button showseekbarBtn;
    Button addkgBtn;

    TextView ddayTv;
    TextView welcomeuserTv;

    String user_name;
    Float goal_kg;
    int goal_year, goal_monthOfYear, goal_dayOfMonth;

    String todayDate;

    // D-day 변수
    static int tyear, tmonthOfYear, tdayOfMonth;
    long today, dday, goal;
    int resultNumber = 1;

    static FirebaseDatabase database;
    DatabaseReference myRef;

    String check;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Button puzzleButton = (Button)findViewById(R.id.goto_puzzle_game);
        tView = (TextView) findViewById(R.id.cntView);

        tView.setText("" + cnt);

        //퍼즐 액티비티로 이동
        puzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ColoringListActivity.class);
                startActivity(intent);
            }
        });

        startService(new Intent(MainActivity.this,MyService.class));
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(date);

        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user_id);
        check = myRef.child("date").getKey().toString();

        mDateRef = mRootRef.child(user_id+"/date");
        mPuzzleCountRef = mRootRef.child(user_id+"/puzzleGame/puzzleCount");

        Toast.makeText(this, check, Toast.LENGTH_SHORT);

        showchangeBtn = (Button)findViewById(R.id.goto_show_change);
        showseekbarBtn = (Button)findViewById(R.id.goto_show_seekbar);
        addkgBtn = (Button)findViewById(R.id.goto_add_kg);
        ddayTv = (TextView)findViewById(R.id.DdayTv);
        welcomeuserTv = (TextView)findViewById(R.id.welcomeUserTv);

        showchangeBtn.setOnClickListener(gotolistener);
        showseekbarBtn.setOnClickListener(gotolistener);
        addkgBtn.setOnClickListener(gotolistener);


        user_name = sharedPreferences.getString("name", "");
        goal_kg = sharedPreferences.getFloat("kg", 1);
        goal_year = sharedPreferences.getInt("goal_year", 0);
        goal_monthOfYear = sharedPreferences.getInt("goal_monthOfYear", 0);
        goal_dayOfMonth = sharedPreferences.getInt("goal_dayOfMonth", 0);

        String welcome = "반갑습니다! "+user_name+"님";
        welcomeuserTv.setText(welcome);

        //Dday 표시
        // 오늘 연, 월, 일 저장
        Calendar c = Calendar.getInstance();
        tyear = c.get(Calendar.YEAR);
        tmonthOfYear = c.get(Calendar.MONTH)+1;
        tdayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        c.set(tyear, tmonthOfYear, tdayOfMonth);

        // 목표 연, 월, 일 세팅
        Calendar c2 = Calendar.getInstance();
        c2.set(goal_year, goal_monthOfYear, goal_dayOfMonth);

        today = c.getTimeInMillis(); // 오늘 날짜를 밀리타임으로 바꿈
        goal = c2.getTimeInMillis(); // 목표 날짜를 밀리타임으로 바꿈
        dday = (goal-today)/(24*60*60*1000); // 목표 날짜에서 오늘날짜를 뺀 것을 '일' 단위로 바꿈
        resultNumber = (int)dday + 1;

        updateDisplay();
    }

    // gotolistener 정의
    View.OnClickListener gotolistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.goto_show_change:
                    Intent showChange_i = new Intent(MainActivity.this, ShowChangeActivity.class);
                    startActivity(showChange_i);
                    break;

                case R.id.goto_show_seekbar:
                    Intent seekbar_i = new Intent(MainActivity.this, SeekbarActivity.class);
                    startActivity(seekbar_i);
                    break;

                case R.id.goto_add_kg:
                    Intent addkg_i = new Intent(MainActivity.this, AddKgActivity.class);
                    startActivity(addkg_i);
                    break;
            }
        }
    }; //gotolistener 끝

    //디데이 날짜가 오늘날짜보다 뒤에오면 '-', 앞에오면 '+'를 붙인다
    private void updateDisplay(){
        if(resultNumber>=0){
            ddayTv.setText(String.format("D-%d", resultNumber));
        }
        else{
            int absR=Math.abs(resultNumber);
            ddayTv.setText(String.format("D+%d", absR));
        }
    } //updateDisplay() 끝

    // 뒤로 가기 버튼을 눌렀을 경우
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한 번 더 누르시면 종료됩니다.",
                    Toast.LENGTH_SHORT).show();
        }
    } // onBackPressed()끝

    @Override
    public void onStart() {
        super.onStart();

        //흔들감지 여부 확인 후 있을 경우


        //오늘의 날짜 저장
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(today.getTime());

        //파이어베이스에 저장된 오늘 걸은 수를 가져옴
        mDateRef.child(day+"/walkingNum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long walkingNum = (Long)dataSnapshot.getValue();
                if(walkingNum!=null){
                    cnt=walkingNum.intValue();
                    tView.setText(Integer.toString(cnt));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //파이어베이스에 저장된 퍼즐 뽑기 여부를 확인할 횟수를 가져옴
        mPuzzleCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                puzzleCount = (Long)dataSnapshot.getValue();
                if(puzzleCount==null){
                    mPuzzleCountRef.setValue(0);
                    for(int i = 1 ; i <= 4 ; i++) {
                        for(int j = 1 ; j <= 12 ; j++) {
                            mPuzzleCountRef.getParent().child("picture"+i+"/userPuzzle/puzzle"+j).setValue(0);
                            mPuzzleCountRef.getParent().child("picture"+i+"/userSetPuzzle/puzzle"+j).setValue(0);
                        }
                    }
                }
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
