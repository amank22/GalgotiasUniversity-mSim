package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.NewsParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.SimParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.TimeTableParcel;

import java.util.ArrayList;
import java.util.List;

public class DbSimHelper extends SQLiteOpenHelper {
    // database name
    static final String dbName = "TeenScribblers.db";

    // Table names
    static final String SemTableName = "Sem_Attendance";
    static final String TodayTableName = "Today_Attendance";
    static final String SubjTableName = "Subjects_Attendance";
    static final String MonthlyTableName = "Monthly_Attendance";
    static final String TimeTableTableName = "TimeTable_Attendance";
    static final String NewsTableName = "News_Table";

    // columns-common
    static final String colID = "ID";

    // columns-subjects
    static final String colSubj = "Subject";
    // columns-monthly
    static final String colMonth = "Month";
    // for semester attendance
    static final String colSem = "Semester";
    /* for above three common set */
    static final String colPresent = "Present";
    static final String colAbsent = "Absent";
    static final String colTotal = "Total";
    static final String colPercnt = "Percentage";
    // change in pattern
    // for todays attendance
    static final String colTimeSlot = "Time_Slot";
    static final String colClassType = "Class_Type";
    static final String colStatus = "Status";

    /* for timetable table */
    static final String colDay = "day";
    static final String colFaculty = "faculty";
    static final String colGroup = "class_group";
    static final String colHallno = "Hallno";
    //   static final String colTimeSlot = "Time_Slot";
    //   static final String colSubj = "Subject";

    /*for News Table*/
    static final String colNote = "note";
    static final String colImageUrl = "image_url";
    static final String colAuthor = "author";

    static final String subjtablesql = "CREATE TABLE " + SubjTableName + " ("
            + colID + " INTEGER PRIMARY KEY , " + colSubj + " TEXT, "
            + colPresent + " INTEGER DEFAULT 0, " + colAbsent
            + " INTEGER DEFAULT 0, " + colTotal + " INTEGER DEFAULT 0, "
            + colPercnt + " REAL);";


    static final String monthlytablesql = "CREATE TABLE " + MonthlyTableName
            + " (" + colID + " INTEGER PRIMARY KEY , " + colMonth + " TEXT, "
            + colPresent + " INTEGER DEFAULT 0, " + colAbsent
            + " INTEGER DEFAULT 0, " + colTotal + " INTEGER DEFAULT 0, "
            + colPercnt + " REAL);";

    static final String semtablesql = "CREATE TABLE " + SemTableName + " ("
            + colID + " INTEGER PRIMARY KEY , " + colSem + " TEXT, "
            + colPresent + " INTEGER DEFAULT 0, " + colAbsent
            + " INTEGER DEFAULT 0, " + colTotal + " INTEGER DEFAULT 0, "
            + colPercnt + " REAL);";

    static final String todaystablesql = "CREATE TABLE " + TodayTableName
            + " (" + colID + " INTEGER PRIMARY KEY , " + colSubj + " TEXT, "
            + colTimeSlot + " TEXT, " + colClassType + " TEXT, " + colStatus
            + " TEXT);";

    static final String timetablesql = "CREATE TABLE " + TimeTableTableName + " ("
            + colID + " INTEGER PRIMARY KEY , " + colSubj + " TEXT, "
            + colDay + " TEXT, " + colGroup
            + " TEXT, " + colFaculty + " TEXT, " + colTimeSlot
            + " TEXT, " + colHallno + " TEXT);";

    static final String Newssql = "CREATE TABLE " + NewsTableName + " ("
            + colID + " INTEGER PRIMARY KEY, " + colNote + " TEXT, "
            + colImageUrl + " TEXT, " + colAuthor
            + " TEXT);";
    private static final String TAG = "DBHelperClass";

    private static DbSimHelper mInstance = null;
    private static final int DATABASE_VERSION = 12;

    public static DbSimHelper getInstance() {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DbSimHelper(GUApp.getInstance());
        }
        return mInstance;
    }

    private DbSimHelper(Context context) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(subjtablesql);
        db.execSQL(monthlytablesql);
        db.execSQL(semtablesql);
        db.execSQL(todaystablesql);
        db.execSQL(timetablesql);
        db.execSQL(Newssql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SubjTableName);
        db.execSQL("DROP TABLE IF EXISTS " + MonthlyTableName);
        db.execSQL("DROP TABLE IF EXISTS " + SemTableName);
        db.execSQL("DROP TABLE IF EXISTS " + TodayTableName);
        db.execSQL("DROP TABLE IF EXISTS " + TimeTableTableName);
        db.execSQL("DROP TABLE IF EXISTS " + NewsTableName);
        onCreate(db);
    }

    // Adding new values
    // others table operations
    public void addnewtoday(String subject, String timeslot, String type, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubj, subject);
        cv.put(colTimeSlot, timeslot);
        cv.put(colClassType, type);
        cv.put(colStatus, status);
        db.insert(TodayTableName, colSubj, cv);
        db.close();
    }


    public void addnewsem(String sem, int present, int absent, int total, float perc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSem, sem);
        cv.put(colPresent, present);
        cv.put(colAbsent, absent);
        cv.put(colPercnt, perc);
        cv.put(colTotal, total);
        db.insert(SemTableName, colSem, cv);
        db.close();
    }

    public void addnewsubj(String subject, int present, int absent, int total,
                           float perc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubj, subject);
        cv.put(colPresent, present);
        cv.put(colAbsent, absent);
        cv.put(colPercnt, perc);
        cv.put(colTotal, total);
        db.insert(SubjTableName, colSubj, cv);
        db.close();
    }

    public void addnewmonthly(String subject, int present, int absent, int total,
                              float perc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colMonth, subject);
        cv.put(colPresent, present);
        cv.put(colAbsent, absent);
        cv.put(colPercnt, perc);
        cv.put(colTotal, total);
        db.insert(MonthlyTableName, colSubj, cv);
        db.close();
    }

    public void addnewtimetable(String subject, String day, String group, String faculty, String timeslot, String hallno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubj, subject);
        cv.put(colTimeSlot, timeslot);
        cv.put(colDay, day);
        cv.put(colGroup, group);
        cv.put(colFaculty, faculty);
        cv.put(colHallno, hallno);
        db.insert(TimeTableTableName, colSubj, cv);
        db.close();
    }

    public void addnewnews(String note, String url, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colNote, note);
        cv.put(colImageUrl, url);
        cv.put(colAuthor, author);
        db.insert(NewsTableName, colNote, cv);
        db.close();
    }

    // int getArticlesCount() {
    // SQLiteDatabase db = this.getWritableDatabase();
    // Cursor cur = db.rawQuery("Select * from " + TableName, null);
    // int x = cur.getCount();
    // cur.close();
    // return x;
    // }

    public List<SimParcel> getSubjAttd() {
        List<SimParcel> list = new ArrayList<SimParcel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + SubjTableName
                + " ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new SimParcel(cur.getString(1), null, null, cur
                        .getInt(2), cur.getInt(3), cur.getInt(4), cur
                        .getFloat(5), null, null, null));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    public List<SimParcel> getMonthlyAttd() {
        List<SimParcel> list = new ArrayList<SimParcel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + MonthlyTableName
                + " ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new SimParcel(null, cur.getString(1), null, cur
                        .getInt(2), cur.getInt(3), cur.getInt(4), cur
                        .getFloat(5), null, null, null));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();

        return list;
    }

    public List<SimParcel> getSemAttd() {
        List<SimParcel> list = new ArrayList<SimParcel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + SemTableName + " ORDER BY "
                + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new SimParcel(null, null, cur.getString(1), cur
                        .getInt(2), cur.getInt(3), cur.getInt(4), cur
                        .getFloat(5), null, null, null));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();

        return list;
    }

    // static final String todaystablesql = "CREATE TABLE " + TodayTableName
    // + " (" + colID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colSubj
    // + " TEXT, " + colTimeSlot + " TEXT, " + colClassType + " TEXT, "
    // + colStatus + " TEXT)";

    public List<SimParcel> getTodaysAttd() {
        List<SimParcel> list = new ArrayList<SimParcel>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + TodayTableName
                + " ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new SimParcel(cur.getString(1), null, null, 0, 0, 0,
                        0.0f, cur.getString(2), cur.getString(4), cur
                        .getString(3)));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();

        return list;
    }

    public List<TimeTableParcel> getTimeTable(String day) {
        List<TimeTableParcel> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + TimeTableTableName
                + " WHERE " + colDay + "='" + day + "' ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new TimeTableParcel(cur.getString(1), cur.getString(2), cur.getString(3),
                        cur.getString(4), cur.getString(5), cur.getString(6)));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    public List<NewsParcel> getAllNews() {
        List<NewsParcel> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + NewsTableName + " ORDER BY "
                + colID, null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                Log.d("Database", "id=" + cur.getInt(0) + ":note=" + cur.getString(1)+ ":imageUrl=" + cur.getString(2));
                list.add(new NewsParcel(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3)));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    //---deletes a particular News---
    public boolean deleteNews(String id)
    {
        Log.d(TAG,id);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NewsTableName, colID + "=" + id, null) > 0;
    }


    public void deletesubj() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SubjTableName, null, null);
    }

    public void deletetoday() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TodayTableName, null, null);
    }

    public void deleteMonthly() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MonthlyTableName, null, null);
    }

    public void deleteSem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SemTableName, null, null);
    }

    public void deleteTimeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TimeTableTableName, null, null);
    }
}
