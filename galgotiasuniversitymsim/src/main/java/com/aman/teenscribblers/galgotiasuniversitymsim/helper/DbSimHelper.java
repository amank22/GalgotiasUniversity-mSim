package com.aman.teenscribblers.galgotiasuniversitymsim.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseLongArray;

import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.ResultParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SeatingPlanParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SimParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SubjectParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.TimeTableParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.TodayAttParcel;

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
    static final String SeatingTableName = "Seating_Plan_Table";
    static final String ResultTableName = "Result_Table";

    // columns-common
    static final String colID = "ID";

    // columns-subjects
    static final String colSubj = "Subject";
    static final String colSubjFromToDate = "fromToDate";
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
    static final String colSession = "Session";
    static final String colProgram = "Program";
    static final String colTimeSlot = "Time_Slot";
    static final String colClassType = "Class_Type";
    static final String colStatus = "Status";

    /* for timetable table */
    static final String colDay = "day";
    static final String colFaculty = "faculty";
    static final String colGroup = "class_group";
    static final String colHallno = "Hallno";

    /* for seating plan table */
    static final String colExamDate = "colExamDate";
    static final String colExamTime = "examTime";
    static final String colSubjectCode = "subjectCode";
    static final String colRoomNumber = "roomNumber";
    static final String colSeatNumber = "seatNumber";
    //   static final String colTimeSlot = "Time_Slot";
    //   static final String colSubj = "Subject";

    /*for News Table*/
    static final String colNote = "note";
    static final String colImageUrl = "image_url";
    static final String colAuthor = "author";
    static final String colAuthorEmail = "a_email";
    static final String colAuthorPic = "a_pic";

    /*for Results Table*/
    static final String colGrade = "grade";
    //semester is the 3rd column

    static final String subjtablesql = "CREATE TABLE " + SubjTableName + " ("
            + colID + " INTEGER PRIMARY KEY , " + colSubjFromToDate + " TEXT, " + colSem + " TEXT," + colSubj + " TEXT, "
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
            + " (" + colID + " INTEGER PRIMARY KEY , " + colSession + " TEXT, " + colProgram + " TEXT, "
            + colSem + " TEXT, " + colSubj + " TEXT, "
            + colTimeSlot + " TEXT, " + colClassType + " TEXT, " + colStatus
            + " TEXT);";

    static final String timetablesql = "CREATE TABLE " + TimeTableTableName + " ("
            + colID + " INTEGER PRIMARY KEY , " + colSubj + " TEXT, "
            + colDay + " TEXT, " + colGroup
            + " TEXT, " + colFaculty + " TEXT, " + colTimeSlot
            + " TEXT, " + colHallno + " TEXT);";

    static final String resulttablesql = "CREATE TABLE " + ResultTableName + " ("
            + colID + " INTEGER PRIMARY KEY , " + colSubj + " TEXT, "
            + colGrade + " TEXT, " + colSem + " TEXT);";

    static final String Newssql = "CREATE TABLE " + NewsTableName + " ("
            + colID + " INTEGER PRIMARY KEY, " + colNote + " TEXT, "
            + colImageUrl + " TEXT, " + colAuthor + " TEXT, " + colAuthorEmail + " TEXT, " + colAuthorPic
            + " TEXT);";

    static final String seatingPlansql = "CREATE TABLE " + SeatingTableName + " ("
            + colID + " INTEGER PRIMARY KEY, " + colExamDate + " TEXT, "
            + colExamTime + " TEXT, " + colSubjectCode + " TEXT, " + colRoomNumber + " TEXT, " + colSeatNumber
            + " TEXT);";

    private static final String TAG = "DBHelperClass";
    private static final int DATABASE_VERSION = 20;
    private static DbSimHelper mInstance = null;

    private DbSimHelper(Context context) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    public static DbSimHelper getInstance() {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DbSimHelper(GUApp.getInstance());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(subjtablesql);
        db.execSQL(monthlytablesql);
        db.execSQL(semtablesql);
        db.execSQL(todaystablesql);
        db.execSQL(timetablesql);
        db.execSQL(resulttablesql);
        db.execSQL(Newssql);
        db.execSQL(seatingPlansql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SubjTableName);
        db.execSQL("DROP TABLE IF EXISTS " + MonthlyTableName);
        db.execSQL("DROP TABLE IF EXISTS " + SemTableName);
        db.execSQL("DROP TABLE IF EXISTS " + TodayTableName);
        db.execSQL("DROP TABLE IF EXISTS " + TimeTableTableName);
        db.execSQL("DROP TABLE IF EXISTS " + ResultTableName);
        db.execSQL("DROP TABLE IF EXISTS " + NewsTableName);
        db.execSQL("DROP TABLE IF EXISTS " + SeatingTableName);
        onCreate(db);
    }

    // Adding new values
    // others table operations
    public void addnewtoday(TodayAttParcel parcel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubj, parcel.getSubject());
        cv.put(colTimeSlot, parcel.getTimeSlot());
        cv.put(colClassType, parcel.getAttendanceType());
        cv.put(colStatus, parcel.getStatus());
        cv.put(colSession, parcel.getSession());
        cv.put(colProgram, parcel.getProgram());
        cv.put(colSem, parcel.getSemester());
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

    public void addnewsubj(SubjectParcel parcel, String subjFromToDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubj, parcel.getSubject());
        cv.put(colPresent, parcel.getPresent());
        cv.put(colAbsent, parcel.getAbsent());
        cv.put(colPercnt, parcel.getPercentage());
        cv.put(colTotal, parcel.getTotal());
        cv.put(colSem, parcel.getSemester());
        cv.put(colSubjFromToDate, subjFromToDate);
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

    public void addNewSeatingPlan(String examDate, String examTime, String subjectCode, String roomNumber, String seatNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colExamDate, examDate);
        cv.put(colExamTime, examTime);
        cv.put(colSubjectCode, subjectCode);
        cv.put(colRoomNumber, roomNumber);
        cv.put(colSeatNumber, seatNumber);
        db.insert(SeatingTableName, colSubjectCode, cv);
        db.close();
    }

    public void addNewResult(String subject, String grade, String semester) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colSubj, subject);
        cv.put(colGrade, grade);
        cv.put(colSem, semester);
        db.insert(ResultTableName, colSubj, cv);
        db.close();
    }

    public void addnewnews(String id, String note, String url, String author, String authorEmail, String authorPic) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(colID, id);
        cv.put(colNote, note);
        cv.put(colImageUrl, url);
        cv.put(colAuthor, author);
        cv.put(colAuthorEmail, authorEmail);
        cv.put(colAuthorPic, authorPic);
        db.insertOrThrow(NewsTableName, colNote, cv);
        db.close();
    }

    public List<NewsParcel> addnewnews(List<NewsParcel> parcelList) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        List<NewsParcel> insertedElementsList = new ArrayList<>();
        db.beginTransaction();
        for (NewsParcel parcel : parcelList) {
            ContentValues cv = new ContentValues();
            cv.put(colID, parcel.id);
            cv.put(colNote, parcel.note);
            cv.put(colImageUrl, parcel.getImage_url());
            cv.put(colAuthor, parcel.author);
            cv.put(colAuthorEmail, parcel.authorEmail);
            cv.put(colAuthorPic, parcel.authorPic);
            long d = db.insertWithOnConflict(NewsTableName, colNote, cv, SQLiteDatabase.CONFLICT_IGNORE);
            if (d != -1)
                insertedElementsList.add(parcel);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return insertedElementsList;
    }

    // int getArticlesCount() {
    // SQLiteDatabase db = this.getWritableDatabase();
    // Cursor cur = db.rawQuery("Select * from " + TableName, null);
    // int x = cur.getCount();
    // cur.close();
    // return x;
    // }

    public List<SimParcel> getSubjAttd(String fromDate, String toDate) {
        List<SimParcel> list = new ArrayList<SimParcel>();
        final String fromToDate = String.format("%s-%s", fromDate, toDate);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + SubjTableName + " WHERE " + colSubjFromToDate + " = '" + fromToDate + "'"
                + " ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new SimParcel(cur.getString(3), null, cur.getString(2), cur
                        .getInt(4), cur.getInt(5), cur.getInt(6), cur
                        .getFloat(7), null, null, null));
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
                list.add(new SimParcel(cur.getString(4), null, null, 0, 0, 0,
                        0.0f, cur.getString(5), cur.getString(7), cur
                        .getString(6)));
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

    public List<SeatingPlanParcel> getSeatingPlan() {
        List<SeatingPlanParcel> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + SeatingTableName
                + " ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new SeatingPlanParcel(cur.getString(1), cur.getString(2), cur.getString(3),
                        cur.getString(4), cur.getString(5)));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    public List<String> getTimeTableDays() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select DISTINCT " + colDay + " FROM " + TimeTableTableName + " ORDER BY " + colID + " DESC", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(cur.getString(0));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    public List<String> getSemestersForResult() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select DISTINCT " + colSem + " FROM " + ResultTableName, null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(cur.getString(0));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    public List<ResultParcel> getResults(String semester) {
        List<ResultParcel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + ResultTableName
                + " WHERE " + colSem + "='" + semester + "'", null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
                list.add(new ResultParcel(cur.getString(1), cur.getString(2), cur.getString(3)));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    public List<NewsParcel> getAllNews() {
        List<NewsParcel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * FROM " + NewsTableName + " ORDER BY "
                + colID, null);
        // looping through all rows and adding to list
        if (cur.moveToLast()) {
            do {
//                Log.d("Database", "id=" + cur.getInt(0) + ":note=" + cur.getString(1) + ":imageUrl=" + cur.getString(2));
                list.add(new NewsParcel(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5)));
            } while (cur.moveToPrevious());
        }
        // closing connection
        cur.close();
        db.close();
        return list;
    }

    //---deletes a particular News---
    public boolean deleteNews(String id) {
        Log.d(TAG, id);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NewsTableName, colID + "=" + id, null) > 0;
    }

    public boolean deleteAllNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NewsTableName, null, null) > 0;
    }


    public void deleteResult() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ResultTableName, null, null);
    }

    public void deletesubj() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SubjTableName, null, null);
    }

    public void deletetoday() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TodayTableName, null, null);
    }

    public void deleteSeatingPlan() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SeatingTableName, null, null);
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
