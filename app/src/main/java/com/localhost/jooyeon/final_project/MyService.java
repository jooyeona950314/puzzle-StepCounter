package com.localhost.jooyeon.final_project;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.localhost.jooyeon.final_project.GoalActivity.sharedPreferences;

public class MyService extends Service implements SensorEventListener {

    String user_id;

    int cnt;
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

    //파이어베이스 연동 및 레퍼런스
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDateRef;
    DatabaseReference mPuzzleCountRef;

    Long puzzleCount;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));

        //파이어베이스 레퍼런스 등록
        mDateRef = mRootRef.child(user_id+"/date");
        mPuzzleCountRef = mRootRef.child(user_id+"/puzzleGame/puzzleCount");

        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);

        //사용자가 처음 어플을 실행하였을 경우 레퍼런스로 참조가능한 데이터가 없기 때문에 이에 대한 값을 세팅
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

        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(today.getTime());

        //백그라운드 상태에서도 만보기 값이 증가
        mDateRef.child(day+"/walkingNum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long walkingNum = (Long)dataSnapshot.getValue();
                if(walkingNum!=null){
                    cnt=walkingNum.intValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //x, y, z값의 변화를 이용하여 사용자의 만보기 횟수 및 퍼즐 뽑기 여부를 확인할 포인트를 증가시킴
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[DATA_X];
                y = event.values[DATA_Y];
                z = event.values[DATA_Z];

                speed = ((Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000));

                if (speed > SHAKE_THRESHOLD) {
                    if(flag) {
                        Calendar today = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String day = sdf.format(today.getTime());
                        mDateRef.child(day+"/walkingNum").setValue(++cnt);
                        if(puzzleCount!=null) {
                            mPuzzleCountRef.setValue((++puzzleCount));
                        }else{
                            mPuzzleCountRef.setValue(1);
                        }

                        flag=false;
                    }
                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
                flag = true;
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
