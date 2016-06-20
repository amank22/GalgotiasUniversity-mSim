package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.content.Context;

import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.DateParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.Pojo.SubjectBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aman on 9/6/16.
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static synchronized void createFile(Context c, String FILENAME, String input) throws Exception {
        //Delete previous file
        File dir = c.getFilesDir();
        File[] files = dir.listFiles();
        if (files.length >= 15) {
            for (File oldFile : files) {
                if (oldFile.getName().startsWith(AppConstants.FILE_NAME_DATE) || oldFile.getName().startsWith(AppConstants.FILE_NAME_SUBJECT)) {
                    oldFile.delete();
                }
            }
        }
        FileOutputStream fos;
        try {
            fos = c.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            throw new Exception("Sorry,File couldnot be found to store your data.");
        }
        try {
            fos.write(input.getBytes());
        } catch (IOException e) {
            throw new Exception("Sorry,My pen broke down.I couldn't write your data.");
        }
        fos.close();
    }

    public static synchronized String readFile(Context c, String FILENAME) throws Exception {
        FileInputStream in = c.openFileInput(FILENAME);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static synchronized List<SubjectBean> filetoSubsList(Context c, String FILENAME) throws Exception {
        String input = readFile(c, FILENAME);
        Type listType = new TypeToken<List<SubjectBean>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(input, listType);
    }

    public static synchronized List<DateParcel> filetoDateList(Context c, String FILENAME) throws Exception {
        String input = readFile(c, FILENAME);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DateParcel>>() {
        }.getType();
        return gson.fromJson(input, listType);
    }
}
