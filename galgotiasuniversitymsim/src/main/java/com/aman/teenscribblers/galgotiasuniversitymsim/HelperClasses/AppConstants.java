package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import java.util.List;

public class AppConstants {
    public static final String LoginString = "http://182.71.87.38/iSIM/Login";
    public static final String HomeString = "http://182.71.87.38/iSIM/Home";
    public static final String AttendanceString = "http://182.71.87.38/iSIM/Student/TodayAttendence";
    public static final String AlertString = "http://182.71.87.38/iSIM/Student/Alerts";
    public static final String TimeTableString = "http://182.71.87.38/iSIM/Student/TimeTable";
    public static final String ResultString = "http://182.71.87.38/iSIM/Student/ExamResult";
    public static final String PersonalInfoString = "http://182.71.87.38/iSIM/Student/Course";
    public static final String StudentImagebase = "http://182.71.87.38/iSIM/studentimages/";
    public static String viewstate = "";
    public static String eventvalidate = "";
    public static List<String> ids = null;
    public static List<String> idvalues = null;
    public static List<String> titletext = null;

    public static final int PRIORITY1 = 1;
    public static final int PRIORITY2 = 2;
    public static final int PRIORITY3 = 3;
    public static final int PRIORITY4 = 4;

    public static final String GroupAttendance = "Attendance";
    public static final String GroupTimeTable = "TimeTable";

    //GCM
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String[] TOPICS = {"Admin","TechOnly","StudioD","Placements","BfuBox","Kalakriti"};

}
