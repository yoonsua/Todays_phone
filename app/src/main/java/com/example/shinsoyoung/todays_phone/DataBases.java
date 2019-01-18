package com.example.shinsoyoung.todays_phone;

/**
 * Created by ShinSoyoung on 2017-09-24.
 */

public class DataBases {
    public static final class myTable {
        public static final String tableName = "MyTable";
        public String strName;

        public String getStrName() {
            return strName;
        }

        public void setStrName(String strName) {
            this.strName = strName;
        }

        /**
         * 열이름들
         * strName          이름
         * strTel           전화번호
         * strGender        성별
         * strBirth         생일
         * strLoc           지역
         * strWeather       날씨
         * strMovie         영화
         * strHealth        건강
         */
        public static final class cols {
            public static final String strName = "name";
            public static final String strTel = "tel";
            public static final String strGender = "gender";
            public static final String strBirth = "birthday";
            public static final String strLoc = "location";
            public static final String strlife = "Life";
            public static final String strMovie = "movie";
            public static final String strHealth = "health";
        }

        public static final String _CREATE =
                "CREATE TABLE " + tableName + " (" +
                        cols.strName + " CHAR(20) PRIMARY KEY, " +
                        cols.strTel + " CHAR(20), " +
                        cols.strGender + " CHAR(1), " +
                        cols.strBirth + " text, " +
                        cols.strLoc + " CHAR(20), " +
                        cols.strlife + " char(1), " +
                        cols.strMovie + " char(1), " +
                        cols.strHealth + " char(1));";

        public static final String _SELECT_NAME =
                "SELECT distinct name, tel FROM " +
                        tableName +
                        " ORDER BY name ASC;";

        public static final String _SELECT_TABLE=
                "SELECT distinct tel, Life, movie, health FROM " + tableName +
                        " ORDER BY name ASC;";

    }
}
