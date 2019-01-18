package com.example.shinsoyoung.todays_phone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    private Button context;

    LinearLayout baseLayout, rowLayout;
    RelativeLayout tile;

    //DB관련
    private Context mContext;
    private SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        CallAnalysis callAnalysis = new CallAnalysis(this);
        int i = 0;
        //빈 LinearLayout 연결
        baseLayout = (LinearLayout) findViewById(R.id.baseLayout);
        mContext = getApplicationContext();
        //DB 생성자
        myDB = new DBOpenHelper(mContext).getWritableDatabase();

        //DB에서 데이터를 읽어 뷰를 만든다.
        Cursor cursor;
        String strName, strPhone;

        cursor = myDB.rawQuery(DataBases.myTable._SELECT_NAME, null);

        while (cursor.moveToNext()) {
            i++;
            //새로운 LinearLayout 생성
            if (i == 1) {
                rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                rowLayout.setGravity(Gravity.CENTER);
            }

            strName = cursor.getString(0);
            strPhone = cursor.getString(1);

            //새로운 RelativeLayout 생성
            tile = new RelativeLayout(this);
            //tile.setPadding(10, 40, 10, 10);
            tile.setBackgroundResource(R.drawable.heart_back);
            tile.setGravity(Gravity.CENTER);

            //새로운 이미지 버튼 생성
            final Button Btn1 = new Button(this);

            Btn1.setBackgroundColor(Color.WHITE);
            Btn1.setTextSize(20);
            Btn1.setText(strName);

            //이번달 합
            int callSum = (callAnalysis.callThisMonthAnalyse(this, strPhone) * 100) / 36;

            Btn1.setBackgroundResource(R.drawable.clip_drawable);
            ClipDrawable drawable = (ClipDrawable) Btn1.getBackground();
            drawable.setLevel(drawable.getLevel() + callSum);

            Btn1.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = Btn1.getText().toString();
                    goContextinfo(view, name);
                }
            });
            tile.addView(Btn1);

            tile.setLayoutParams(new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT, 1f));
            rowLayout.addView(tile);
            if (i == 3) {
                baseLayout.addView(rowLayout);
                i = 0;
            }
        }
        if (i != 0) {
            baseLayout.addView(rowLayout);
            i = 0;
        }
        cursor.close();
        myDB.close();

        context = (Button) findViewById(R.id.contextadd);

        context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Context_Add.class);
                startActivityForResult(intent, 1);
                finish();
            }

        });

    }

    public void goContextinfo(View view, String name) {
        Intent intent = new Intent(MainActivity.this, Context_info.class);

        intent.putExtra("name", name);

        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.toolbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        return true;
    }

}
