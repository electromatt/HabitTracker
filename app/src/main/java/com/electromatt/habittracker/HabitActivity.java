package com.electromatt.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.electromatt.habittracker.data.HabitContract.HabitEntry;
import com.electromatt.habittracker.data.HabitDbHelper;

public class HabitActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HabitDbHelper(this);
        displayDatabaseInfo();

    }

    private void displayDatabaseInfo() {
        Cursor cursor = read();
        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            displayView.setText("Number of rows in habits database table: " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_HABIT_NAME + " - " +
                    HabitEntry.COLUMN_HABIT_DATE + "\n");
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
            int dateColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DATE);

            while(cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentDate));
            }
        } finally {
            cursor.close();
        }
    }
    private Cursor read(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] project = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_NAME,
                HabitEntry.COLUMN_HABIT_DATE
        };
        Cursor cursor = db.query(HabitEntry.TABLE_NAME, project, null, null, null, null, null);
        return cursor;
    }
    private void insertPet(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c.getTime());

        values.put(HabitEntry.COLUMN_HABIT_NAME, "Do homework");
        values.put(HabitEntry.COLUMN_HABIT_DATE, formattedDate);

        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
        Log.v("CatalogActivity", "New row ID "+ newRowId);
    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}