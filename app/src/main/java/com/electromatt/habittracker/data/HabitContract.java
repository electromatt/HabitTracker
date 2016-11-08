package com.electromatt.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by Matthew on 11/8/2016.
 */

public class HabitContract {

    public static abstract class HabitEntry implements BaseColumns {

        public static final String TABLE_NAME = "habits";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT_NAME = "name";
        public static final String COLUMN_HABIT_DATE = "date";


    }
}
