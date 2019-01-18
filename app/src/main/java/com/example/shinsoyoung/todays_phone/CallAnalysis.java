package com.example.shinsoyoung.todays_phone;

import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShinSoyoung on 2017-10-06.
 */

public class CallAnalysis {
    CheckPermission chk;
    Cursor curLog;
    String strOrder;

    public CallAnalysis(Context context) {
        chk = new CheckPermission(context);
        strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        curLog = context.getContentResolver().query(Uri.parse("content://call_log/calls"), null, null, null, strOrder);
        if (curLog == null || curLog.getCount() == 0)
            return;
    }

    public int callThisMonthAnalyse(Context context, String strPhone) {
        //지금 날짜
        Calendar oCalendar = Calendar.getInstance( );
        int month = (oCalendar.get(Calendar.MONTH) + 1);

        //callDate를 월 형식으로 바꿈
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        int sumDuration = 0;

        while (curLog.moveToNext()) {
            //날짜
            String callDate = curLog.getString(curLog.getColumnIndex(CallLog.Calls.DATE));
            //날짜 형식 변환
            int callMonth = Integer.parseInt(formatter.format(new Date(Long.parseLong(callDate))));
            //통화량
            int duration = curLog.getInt(curLog.getColumnIndex(CallLog.Calls.DURATION));
            String callNumber = curLog.getString(curLog.getColumnIndex(CallLog.Calls.NUMBER));

            if (strPhone.equals(callNumber)) {
                if (callMonth == month) sumDuration += duration;
                else if (callMonth < month) break;
            }
        }
        curLog = context.getContentResolver().query(Uri.parse("content://call_log/calls"), null, null, null, strOrder);
        return sumDuration;
    }

    public int[] call6MAnalyse(Context context, String strPhone){
        int[] sumDuration = new int[6];
        //TextView로 보여줄 달의 갯수가 6개
        int i;//nowDate -i 할때와 sumDuration[i]로 사용

        //지금 날짜
        Calendar cal = Calendar.getInstance();
        int month = cal.get(cal.MONTH) + 1;

        //callDate를 월 형식으로 바꿈
        SimpleDateFormat formatter = new SimpleDateFormat("MM");

        while (curLog.moveToNext()) {

            //날짜
            String callDate = curLog.getString(curLog.getColumnIndex(CallLog.Calls.DATE));
            //날짜 형식 변환
            int callMonth = Integer.parseInt(formatter.format(new Date(Long.parseLong(callDate))));
            //통화량
            int duration = curLog.getInt(curLog.getColumnIndex(CallLog.Calls.DURATION));
            String callNumber = curLog.getString(curLog.getColumnIndex(CallLog.Calls.NUMBER));

            if (strPhone.equals(callNumber)) {
                i = 0;
                //이번달
                if (callMonth == month) {
                    sumDuration[i] += duration;
                }
                i++;
                //전달
                if (callMonth == month - i) {
                    sumDuration[i] += duration;
                }
                //전전달
                i++;
                if (callMonth == month - i) {
                    sumDuration[i] += duration;
                }
                i++;
                if (callMonth == month - i) {
                    sumDuration[i] += duration;
                }
                i++;
                if (callMonth == month - i) {
                    sumDuration[i] += duration;
                }
                i++;
                if (callMonth == month - i) {
                    sumDuration[i] += duration;
                }
            }
        }
        return sumDuration;
    }
}
