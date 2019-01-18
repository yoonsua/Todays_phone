package com.example.shinsoyoung.todays_phone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;


public class Context_Add extends AppCompatActivity {
    //DB관련
    private Context mContext;
    private SQLiteDatabase myDB;

    public String strLife, strMovie, strHealth;

    //화면관련
    Button btnAdd;
    EditText edtName, edtTel, edtBirth, edtLoc;
    CheckBox chkLife, chkMovie, chkHealth;
    RadioButton rButtonM;
    Toast tMsg;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //불러준 Activity
        intent = getIntent();

        mContext = getApplicationContext();
        //DB 생성자
        myDB = new DBOpenHelper(mContext).getWritableDatabase();

        setContentView(R.layout.activity_context_add);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        edtName = (EditText)findViewById(R.id.edtName);
        edtTel = (EditText) findViewById(R.id.edtTel);
        edtBirth = (EditText) findViewById(R.id.edtBirth);
        edtLoc = (EditText)findViewById(R.id.edtLoc);
        chkLife= (CheckBox)findViewById(R.id.chkLife);
        chkMovie = (CheckBox) findViewById(R.id.chkMovie);
        chkHealth = (CheckBox) findViewById(R.id.chkHealth);
        rButtonM = (RadioButton) findViewById(R.id.rButtonM);


        //날짜
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Calendar today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            today = Calendar.getInstance();
            String StrDate = formatter.format(today.getTime());
            edtBirth.setText(String.valueOf(StrDate));
        }
        edtBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    Calendar today = Calendar.getInstance();
                    int iYear = today.get(Calendar.YEAR);
                    int iMonth = today.get(Calendar.MONTH);
                    int iDay = today.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(Context_Add.this, listener, iYear, iMonth, iDay).show();
                }
            }
        });
        edtBirth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                return false;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼을 누르면 저장하고 화면 닫고, 이전 화면 새로 고침
                String sql;
                String strName, strTel, strGender, strBirth, strLoc;
                Cursor cursor;

                //화면에 입력된 내용 가져오기
                strName = edtName.getText().toString();
                strTel = edtTel.getText().toString();
                strBirth = edtBirth.getText().toString();
                strLoc = edtLoc.getText().toString();

                if (rButtonM.isChecked())
                    strGender = "1";
                else
                    strGender = "0";
                if( chkLife.isChecked())
                    strLife= "1";
                else
                    strLife = "0";
                if( chkMovie.isChecked())
                    strMovie = "1";
                else
                    strMovie = "0";
                if( chkHealth.isChecked())
                    strHealth = "1";
                else
                    strHealth = "0";

                if ( ( strName.isEmpty()) || (strName.equals(""))) {
                    Toast.makeText(getApplicationContext(), "이름 필수", Toast.LENGTH_SHORT);
                } else {
                    //중복데이터 검색
                    sql = "SELECT * FROM " + DataBases.myTable.tableName + " WHERE name= '" + strName + "';";
                    cursor = myDB.rawQuery(sql, null);

                    if (cursor.moveToNext()) {
                        //중복인 경우 메세지만 출력
                        tMsg = Toast.makeText(getApplicationContext(), "입력된 이름", Toast.LENGTH_SHORT);
                        tMsg.setGravity(Gravity.CENTER, 0, 0);
                        tMsg.show();
                    } else {
                        //새로운 데이터인 경우 DB에 입력
                        sql = "INSERT INTO " + DataBases.myTable.tableName +
                                " VALUES ('" + strName + "', '" + strTel + "', '" +
                                strGender + "', '" + strBirth + "', '" + strLoc+ "', '" +
                                strLife+"', '" + strMovie + "',  '" + strHealth +"' );";
                        myDB.execSQL(sql);

                        tMsg = Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT);
                        tMsg.setGravity(Gravity.CENTER, 0, 0);
                        tMsg.show();
                    }
                    cursor.close();
                    myDB.close();

                    //table에 저장되면 화면 닫고 첫화면으로
                    Intent intent = new Intent(Context_Add.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.toolbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Context_Add.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            edtBirth.setText(String.valueOf(year)+"/"+ String.valueOf(monthOfYear+1)+"/"+ String.valueOf(dayOfMonth));
        }
    };
}
