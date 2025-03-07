package com.util.stats;

import com.util.StaticStrings;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Responsible for reading and storing log of executed commands to file in hard disk.
 */
public class StatsStorage {

    // Format of file
    // 16/09/2019 | 22:30 | list | NA | NA
    // 24/09/2019 | 08:07 | delete | [T][v] read book | NA
    // 28/09/2019 | 10:23 | find | NA | [keyword1, keyword2]

    private String fp;
    private File f;
    private boolean doesFileExist;
    private static String STATSSTORAGE_FILENAME = "dukeStats.txt";
    private static String STORAGE_DIRNAME = "dukeData";

    public StatsStorage() {
        this.fp = getFilePath();
        this.f = new File(fp);
        this.doesFileExist = f.exists();
    }

    /**
     * Gets appropriate filepath to save data txt files to.
     * If application is not running on the developer's PC, save files
     * to location relative to where application is at.
     * @return location to save files
     */
    private String getFilePath() {
        String devDirFilePath = "F:\\CS2103\\duke\\data";
        File devFile = new File(devDirFilePath);
        // App running from dev pc (i.e. duke local repo), has data folder
        if (devFile.exists()) {
            return devDirFilePath + File.separator + STATSSTORAGE_FILENAME;
        }
        String currDir = System.getProperty("user.dir");
        // If data dir is not yet initialise, create it
        String dataDirFilePath = currDir + File.separator + STORAGE_DIRNAME;
        File dataDir = new File(dataDirFilePath);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        String dataFilePath = dataDirFilePath + File.separator + STATSSTORAGE_FILENAME;
        return dataFilePath;
    }

    public ArrayList<Log> load() {
        // If no such file, create one
        if (!doesFileExist) {
            ArrayList<Log> newLogArr = new ArrayList<Log>();
            save(newLogArr);
            return newLogArr;
        }
        try {
            // If file exists, read in logs
            Scanner input = new Scanner(f);

            ArrayList<Log> logArr = new ArrayList<Log>();
            ArrayList<String> oneLineTextArr;

            Log currLog;
            String date;
            String time;
            String commandWord;
            String task;
            ArrayList<String> keywords;

            while (input.hasNextLine()) {
                oneLineTextArr = new ArrayList<String>(
                        Arrays.asList(input.nextLine().split(" \\| "))
                );
                date = oneLineTextArr.get(0);
                time = oneLineTextArr.get(1);
                commandWord = oneLineTextArr.get(2);
                task = convertToNullIfNA(oneLineTextArr.get(3));
                keywords = convertToArrayList(convertToNullIfNA(oneLineTextArr.get(4)));
                currLog = new Log(date, time, commandWord, task, keywords);
                logArr.add(currLog);
            }
            return logArr;
        } catch (FileNotFoundException e) {
            return new ArrayList<Log>();
        }
    }

    public void save(ArrayList<Log> logArr) {
        try {
            FileWriter fw = new FileWriter(fp);
            for (Log currLog : logArr) {
                fw.write(currLog.getDate() + StaticStrings.STORAGE_SEPARATOR +
                        currLog.getTime() + StaticStrings.STORAGE_SEPARATOR +
                        currLog.getCommandWord() + StaticStrings.STORAGE_SEPARATOR +
                        currLog.getTask() + StaticStrings.STORAGE_SEPARATOR +
                        currLog.getKeywords()
                );
                int currLogIdx = logArr.indexOf(currLog);
                if (currLogIdx + 1 != logArr.size()) {
                    fw.write("\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            f = new File(fp);
        }
    }

    ////////////////////
    // HELPER METHODS //
    ////////////////////

    /**
     * Converts text to null if "NA", else returns it as is.
     * @param text
     * @return
     */
    private String convertToNullIfNA(String text) {
        return text.equals("NA") ? null : text;
    }

    /**
     * Converts string to array list of string.
     * @param text String of format "[A, B, C]"
     * @return
     */
    private ArrayList<String> convertToArrayList(String text) {
        if (text == null) {
            return null;
        }
        // Removed square brackets []
        String removedBracketsText = text.substring(1, text.length() - 1);
        ArrayList<String> output = new ArrayList<String>(
                Arrays.asList(removedBracketsText.split(", "))
        );
        return output;
    }

}
