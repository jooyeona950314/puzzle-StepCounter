package com.localhost.jooyeon.final_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.localhost.jooyeon.final_project.GoalActivity.sharedPreferences;
import static com.localhost.jooyeon.final_project.MainActivity.database;

/**
 * Created by asmwj on 2017-06-15.
 */

public class AddKgActivity extends AppCompatActivity {
    ImageView add_kgIv;
    Button add_kgBtn;
    EditText add_kgEt;
    LinearLayout add_kgLL;



    String addKg;
    String todayDate;

    String todayDate_photo;

    DatabaseReference myRef;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kg);


        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String user_id = String.valueOf(sharedPreferences.getLong("user_id", 0));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user_id);


        // 오늘 날짜를 todayDate에 String형으로 저장
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df_photo = new SimpleDateFormat("yyyyMMddhm");

        todayDate = df.format(date);
        todayDate_photo = df_photo.format(date);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("camera").child(todayDate_photo +".jpg");



        add_kgLL = (LinearLayout)findViewById(R.id.add_kgLL);
        add_kgBtn = (Button)findViewById(R.id.add_kgBtn);
        add_kgIv = (ImageView)findViewById(R.id.add_kgIv);
        add_kgEt = (EditText)findViewById(R.id.add_kgEt);



        // 이미지 뷰를 클릭하였을 경우
        add_kgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 1);
            }
        });

        // save 버튼을 눌렀을 경우
        add_kgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addKg = add_kgEt.getText().toString();
                myRef.child("date").child(todayDate).child("kg").setValue(addKg);

                finish();
            }
        });

        add_kgLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
    } // onCreate() 끝


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {

            myRef.child("date").child(todayDate).child("uri").setValue(String.valueOf(data.getData()));

            Glide.with(this)
                    .load(data.getData())
                    .centerCrop()
                    .override(600, 800)
                    .into(add_kgIv);
            /*add_kgIv.setImageURI(data.getData());*/


            add_kgIv.setDrawingCacheEnabled(true);
            add_kgIv.buildDrawingCache();
            Bitmap bitmap = add_kgIv.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();
            UploadTask uploadTask = (UploadTask) storageReference.putBytes(datas)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddKgActivity.this, "성공", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    } // onActivityResult() 끝

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
