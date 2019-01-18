package com.example.shinsoyoung.todays_phone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Context_info extends AppCompatActivity {
    //DB관련
    private Context mContext;
    private SQLiteDatabase myDB;

    TextView tvName, tvDDay;

    Intent intent;

    String strPhone;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_info);

        String strName, sql;
        String strDay;

        intent = getIntent();
        strName = intent.getStringExtra("name");


        //화면에 이름 표시
        tvName = (TextView) findViewById(R.id.txtName);
        tvDDay = (TextView) findViewById(R.id.txtDday);
        tvName.setText(strName);

        mContext = getApplicationContext();
        //DB 생성자
        myDB = new DBOpenHelper(mContext).getWritableDatabase();

        //DB에서 데이터를 읽어 뷰를 만든다.
        Cursor cursor;

        sql = "SELECT tel, birthday FROM " + DataBases.myTable.tableName + " WHERE name = '" + strName + "';";
        cursor = myDB.rawQuery(sql, null);

        //call log 분석
        CallAnalysis callAnalysis = new CallAnalysis(this);

        while (cursor.moveToNext()) {
            strPhone = cursor.getString(0);

            //D-Day 표시

            strDay = cursor.getString(1);
            int iDDay, iMonth, iDay;
            iMonth = Integer.parseInt(strDay.substring(5, 7));
            iDay = Integer.parseInt(strDay.substring(8));
            iDDay = caldate(iMonth, iDay);

            tvDDay.setText(String.format("D-%d", iDDay));

            //Call Log 분석

            int[] sumDuration = callAnalysis.call6MAnalyse(this, strPhone);

            //Call Log 그래프 그리기
            LineChart lineChart = (LineChart) findViewById(R.id.chart);


            //
            ArrayList<Entry> entries = new ArrayList<>();
            for (int j = 0; j <= 5; j++) {
                entries.add(new Entry(((float) sumDuration[5 - j]) / 60, j));
            }

            LineDataSet dataset = new LineDataSet(entries, "각 달별 통화량");

            ArrayList<String> labels = new ArrayList<String>();

            labels.add("5달 전");
            labels.add("4달 전");
            labels.add("3달 전");
            labels.add("2달 전");
            labels.add("1달 전");
            labels.add("이번 달");


            LineData data = new LineData(labels, dataset);
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);

            lineChart.setData(data);
            lineChart.animateY(4000);
        }


        //이걸로 엑스트라 데이터를 넘겨요
        cursor = myDB.rawQuery(DataBases.myTable._SELECT_TABLE, null);
        String phone,health,movie,life;
        while (cursor.moveToNext()) {
          phone = cursor.getString(0);
            if (phone.equals(strPhone)) {
                life = cursor.getString(1);
                movie = cursor.getString(2);
                health =cursor.getString(3);
                Intent aintent = new Intent(Context_info.this, AdapterList.class);
                aintent.putExtra("life",life);
                aintent.putExtra("movie",movie);
                aintent.putExtra("health",health);
                break;
            }
        }

        cursor.close();
        myDB.close();
    }

    //전화 클릭
    public void callStart(View v) {
        //앱 위에 그리기 키는 거
        startService(new Intent(getApplicationContext(), AlwaysTopServiceTouch.class));
        Intent call = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + strPhone));
        startActivity(call);
    }

    public void strStart(View view) {
        startActivity(new Intent(getApplicationContext(), AdapterList.class));
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Context_info.this, MainActivity.class);
        startActivity(intent);
        //끄는 거
        stopService(new Intent(getApplicationContext(), AlwaysTopServiceTouch.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int caldate(int mmonth, int mday) {
        try {
            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            Calendar dday = Calendar.getInstance();  // D-day
            Calendar dday2 = Calendar.getInstance(); //내년 D-day

            int myear = today.get(Calendar.YEAR);
            int myear2 = myear + 1;

            dday.set(myear, mmonth - 1, mday);        // 올해의 D-day의 날짜를 입력합니다.
            dday2.set(myear2, mmonth - 1, mday);    //내년의 D-Day의 날짜를 입력합니다.

            long day = dday.getTimeInMillis();
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )
            long day2 = dday2.getTimeInMillis();

            long tday = today.getTimeInMillis();
            long count = (day - tday) / 86400000;  // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            long count2 = (day2 - tday) / 86400000;  // 오늘 날짜에서 dday2 날짜를 빼주게 됩니다.

            if (count >= 0)
                return (int) count;
            else
                return (int) count2;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}


