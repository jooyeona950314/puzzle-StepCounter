package com.localhost.jooyeon.final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by asmwj on 2017-05-23.
 */


// 초기 화면, 목표를 설정하는 화면

public class GoalActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText goal_kgEt;
    EditText goal_nameEt;
    LinearLayout goal_dateLL;
    TextView goal_dateTv;
    Button goal_saveBtn;
    LinearLayout activity_goal;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int year, month, day;


    long user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //SharedPreferences를 사용하기 위한 객체 가져오기
        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //isFirstRun 변수에 초기 false값 저장
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun",false);

        //isFirstRun이 true일 경우 곧바로 MainActivity 실행
        if(isFirstRun==true) {
            Intent newIntent = new Intent(GoalActivity.this, MainActivity.class);
            startActivity(newIntent);
        }
        //true로 변경
        sharedPreferences.edit().putBoolean("isFirstRun",true).apply();

        // id 값 부여
        activity_goal = (LinearLayout)findViewById(R.id.activity_goal);
        goal_nameEt = (EditText)findViewById(R.id.goal_nameEt);
        goal_kgEt = (EditText)findViewById(R.id.goal_kgET);
        goal_dateLL = (LinearLayout)findViewById(R.id.goal_dateLL);
        goal_saveBtn = (Button)findViewById(R.id.goal_saveBtn);
        goal_dateTv = (TextView)findViewById(R.id.goal_dateTv);

        // 키보드가 올라올 시 화면 누르면 키보드가 다시 숨겨지도록
        activity_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        // 오늘 날짜 가져오기
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        user_id = System.currentTimeMillis();


        // 목표기간을 눌렀을 경우 datepickerdialog가 나타나도록
        goal_dateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(GoalActivity.this, year, month, day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#009688"));
                datePickerDialog.setTitle("목표 기간을 선택해주세요.");
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });



    }

    // 날짜 선택 시
    @Override
    public void onDateSet(DatePickerDialog view, final int year, final int monthOfYear, final int dayOfMonth) {
        String date = year +"년 "+ Integer.valueOf(monthOfYear+1) + "월 " + dayOfMonth + "일";

        goal_saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String name = goal_nameEt.getText().toString();
                final float goal_kg = Float.valueOf(goal_kgEt.getText().toString());


                if (goal_nameEt.getText().equals("")|| goal_kgEt.getText().equals("")){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(GoalActivity.this)
                            .setMessage("이름을 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder2.show();
                }

                else if(goal_kg > 200){
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(GoalActivity.this)
                            .setMessage("몸무게 다시 정해라.")
                            .setPositiveButton("알겠어", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder3.show();
                }
                else{
                    // save 버튼 누를 시에 dialog 확인 창
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this)
                            .setMessage(year +"년 "+ Integer.valueOf(monthOfYear+1) + "월 " + dayOfMonth + "일까지" + goal_kg + "kg 가능하니?")
                            .setPositiveButton("당연하지!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent i = new Intent(GoalActivity.this, MainActivity.class);
                                    startActivity(i);


                                    // SharedPreferences editor 에 이름, 목표몸무게, 목표날짜 데이터를 put한다.
                                    editor.putString("name", name);
                                    editor.putFloat("kg", goal_kg);
                                    editor.putInt("goal_year", year);
                                    editor.putInt("goal_monthOfYear", monthOfYear+1);
                                    editor.putInt("goal_dayOfMonth", dayOfMonth);
                                    editor.putLong("user_id", user_id);

                                    editor.commit();

                                    finish();

                                }
                            })
                            .setNegativeButton("모르겠어", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            }
        });
        goal_dateTv.setText(date);
    }

    // 키보드를 숨기는 메소드
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    } //hideKeyboard() 끝

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
