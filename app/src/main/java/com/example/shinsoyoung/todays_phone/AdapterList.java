package com.example.shinsoyoung.todays_phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AdapterList extends AppCompatActivity {
    ArrayList<String> arrlist = new ArrayList<String>();
    ArrayList<String> moviList = new ArrayList<String>(Arrays.asList("OOO라는 영화가 있는데 평점이 좋은 것 같아요.", "OOO가 이번에 시즌 2로 나온데요."
    ,"OOO라는 영화를 봤는데 정말 감동적인 영화였어요.","역사 사건을 배경으로 한 영화를 개봉했다던데 그 소식 들었어요 ?","저한테 영화 티켓이 생겼는데 같이 보러 가실래요 ?"));
    ArrayList<String> lifeList = new ArrayList<String>(Arrays.asList("추석 연휴가 끝나고 나서 코스모스가 활짝 피니까 가을이 깊어가는 것 같아요.", "비온다는 소식이 있던데 우산 챙기고 외출하세요."
    ,"설악산으로 단풍보러 가실래요 ?","대하가 요즘 제철 음식이라던데 같이 드시러 가실래요 ?","BAS 탈취제에 있는 입자가 악취 원인이 되는 물질에 달라붙어 냄새를 없애준데요. " +
                    "집에 애완동물이 있으니 사용하면 좋을 것 같아요."));
    ArrayList<String> healthList = new ArrayList<String>(Arrays.asList("강황, 비타민이 노화 방지에 좋은 영양소래요.", "요즘 어디 아프신 곳은 없으세요?, 같이 병원가드릴까요 ?." +
            "유산소 운동만 해도 지방을 연소시키는 효과를 가져온데요.","등산이 건강에 좋다고 하던데 저랑 같이 등산 가실래요 ?","마이크로 버블샤워기라는게 있는데 아토비와 지루성 두피염에 효과적이래요."
    ,"웃으면 독감 예방주사 효과가 높아진데요. 항상 웃고 다니세요"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter);

        Intent intent = getIntent();
       //아래가 null가 null로 나옵니다. info에서 엑스트라 데이터하고요
        String life = intent.getStringExtra("life");
        String movie  = intent.getStringExtra("movie");
        String health = intent.getStringExtra("health");

        if (health == "1")
            arrlist.addAll(healthList);
        if (movie == "1")
            arrlist.addAll(moviList);
        if (life == "1")
            arrlist.addAll(lifeList);

        long seed = System.nanoTime();
        Collections.shuffle(arrlist, new Random(seed));

        ArrayAdapter<String> Adapter;
        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrlist);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(Adapter);

    }
}
