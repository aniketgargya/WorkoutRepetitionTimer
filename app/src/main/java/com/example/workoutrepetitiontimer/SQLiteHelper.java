package com.example.workoutrepetitiontimer;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    private static final String DATABASE_NAME = "RunningStopwatch";
    private static String TABLE_NAME;
    private static String REP_CYCLE_NAME_COLUMN;
    private static String REP_CYCLE_ID_COLUMN;
    private static String REP_CYCLE_LIST_COLUMN;
    private static String REP_CYCLE_REPETITIONS_COLUMN;

    public SQLiteHelper(Context context, String tableName, String repCycleNameColumn, String repCycleIdColumn, String repCycleListColumn, String repCycleRepetitionsColumn) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME = tableName;
        REP_CYCLE_NAME_COLUMN = repCycleNameColumn;
        REP_CYCLE_ID_COLUMN = repCycleIdColumn;
        REP_CYCLE_LIST_COLUMN = repCycleListColumn;
        REP_CYCLE_REPETITIONS_COLUMN = repCycleRepetitionsColumn;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s text, %s text, %s text);", TABLE_NAME, REP_CYCLE_ID_COLUMN, REP_CYCLE_NAME_COLUMN, REP_CYCLE_LIST_COLUMN, REP_CYCLE_REPETITIONS_COLUMN));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertRepCycle(RepCycleModel repCycleModel){
        database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(REP_CYCLE_NAME_COLUMN, repCycleModel.getName());
        contentValues.put(REP_CYCLE_LIST_COLUMN, repCycleModel.getList());
        contentValues.put(REP_CYCLE_REPETITIONS_COLUMN, repCycleModel.getRepetitions());

        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public void updateRepCycle(RepCycleModel repCycleModel){
        database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(REP_CYCLE_NAME_COLUMN, repCycleModel.getName());
        contentValues.put(REP_CYCLE_LIST_COLUMN, repCycleModel.getList());
        contentValues.put(REP_CYCLE_REPETITIONS_COLUMN, repCycleModel.getRepetitions());

        database.update(TABLE_NAME, contentValues, REP_CYCLE_ID_COLUMN + " = ?", new String[]{repCycleModel.getId()});
        database.close();
    }

    public void deleteRepCycle(RepCycleModel repCycleModel){
        Log.d("Debugging", (REP_CYCLE_ID_COLUMN + " = ?"));
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, REP_CYCLE_ID_COLUMN + " = ?", new String[]{repCycleModel.getId()});
        database.close();
    }

    public ArrayList<RepCycleModel> getRepCycles(){
        database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME, null, null,null,null,null, null, null);

        int RepCycleId = cursor.getColumnIndex(REP_CYCLE_ID_COLUMN);
        int RepCycleName = cursor.getColumnIndex(REP_CYCLE_NAME_COLUMN);
        int RepCycleList = cursor.getColumnIndex(REP_CYCLE_LIST_COLUMN);
        int RepCycleRepetitions = cursor.getColumnIndex(REP_CYCLE_REPETITIONS_COLUMN);

        ArrayList<RepCycleModel> repCycles = new ArrayList<RepCycleModel>();
        RepCycleModel repCycleModel;

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {
                repCycleModel = new RepCycleModel(cursor.getString(RepCycleName), cursor.getString(RepCycleList), cursor.getString(RepCycleRepetitions), String.valueOf(cursor.getInt(RepCycleId)));
                repCycles.add(repCycleModel);
            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();
        return repCycles;
    }

}
