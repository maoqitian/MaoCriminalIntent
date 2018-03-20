package mao.com.maocriminalintent.database;

/**
 * Created by maoqi on 2018/3/12 0012.
 * 数据库表类
 */

public class CrimeDbSchema {
    public static final class CrimeTable{ //定义描述数据表元素的String常量
        public static final String NAME = "crimes";//数据库表名

        public static final class Cols{ //表中字段
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
            public static final String SUSPECTCONTACT = "suspectcontact";
        }
    }

}
