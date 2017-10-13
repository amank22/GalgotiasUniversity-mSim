package com.aman.teenscribblers.galgotiasuniversitymsim.helper;

public class AppConstants {
    public static final String BaseUrl = "http://182.71.87.38/ISIM/";
    public static final String BaseUrlGCT = "http://43.250.253.66/isimgc/";
    public static final String ERROR_BASE_URL = BaseUrlGCT + "msg.htm";
    public static final String LoginString = BaseUrlGCT + "Login";
    public static final String HomeString = BaseUrlGCT + "Home";
    public static final String AttendanceString = BaseUrlGCT + "Student/TodayAttendence";
    public static final String AlertString = BaseUrlGCT + "Student/Alerts";
    public static final String TimeTableString = BaseUrlGCT + "Student/TimeTable";
    public static final String ResultString = BaseUrlGCT + "Student/ExamResult";
    public static final String PersonalInfoString = BaseUrlGCT + "Student/Course";
    public static final String OfficialInfoString = BaseUrlGCT + "Student/StudentOfficial";
    public static final String captchaurl = BaseUrlGCT + "Student/capimage";
    public static final String PERSONAL_HEADING = "~Heading~";
    public static final String OFFICIAL_HEADING = "~Heading~";
    public static final String ATT_TODAY = "Today Attendance";
    public static final String ATT_SUBJECT = "Subject Wise Attendance";
    //    public static final String ATT_DATE = "Date Wise";
    public static final String FILE_NAME_SUBJECT = "subjectsAtt";
    public static final String FILE_NAME_DATE = "dateAtt";
    public static final String FILE_NAME_PERSONAL = "personal.txt";
    public static final String FILE_NAME_OFFICIAL = "official.txt";
    public static final String FILE_NAME_TODAY = "todayAtt";
    public static final String ERROR_SESSION_EXPIRED = "Sorry,Your Session has expired.Let's Start again..";
    public static final String ERROR_CONTENT_FETCH = "Sorry,We encountered some error.Let's try again.";
    public static final String ERROR_NO_CONTENT = "No Record Found on WebSim";
    public static final String ERROR_INTERNAL_SERVER = "Sorry,Some internal problem.Let's try again.";
    public static final String ERROR_TIME_TABLE = "You might be having a holiday.You can try again.";
    public static final String ERROR_NETWORK = "Please Connect to Internet";
    public static final String ERROR_LOCAL_CACHE_NOT_FOUND = "Looking for data in WebSim";
    public static final int PRIORITY1 = 1;
    public static final int PRIORITY2 = 2;
    public static final int PRIORITY3 = 3;
    public static final int PRIORITY4 = 4;
    public static final String GroupAttendance = "Attendance";
    public static final String GroupTimeTable = "TimeTable";

    public static final String[] TOPICS = {"Admin", "TechOnly", "StudioD", "Placements", "BfuBox", "Kalakriti"};
    public static String viewstate = "";
    public static String eventvalidate = "";
    public static String viewStateGenerator = "";

}
