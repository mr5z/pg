package com.nkraft.pogo.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mark on 20/07/2016.
 */
public class Debug {

    /**
     * We will just be needing to view the logs in Windows platform
     */
    public static final String LINE_SEPARATOR = "\r\n";
    private static final String TAG = "nkraft";
    public static boolean DEBUGGING = true;
    private static boolean enableLogging = true;
    private final Class<?> mCurrentClass;
    private Context mContext;
    private SimpleDateFormat mDateFormatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    /**
     * The <i>Debug</i> class that writes to external storage only
     *
     * @param currentClass Current class who uses this method
     */
    public Debug(Class<?> currentClass) {
        mCurrentClass = currentClass;
        mDateFormatter.setTimeZone(TimeZone.getDefault());
    }

    /**
     * The <i>Debug</i> class that writes to either external or internal storage
     *
     * @param currentClass Current class who uses this method
     * @param context      Application context that enables to locate where the log file should be put in place
     */
    public Debug(Class<?> currentClass, Context context) {
        mCurrentClass = currentClass;
        mContext = context;
        mDateFormatter.setTimeZone(TimeZone.getDefault());
    }

    /**
     * Uses the LogCat internally with a default tag of <i>kinpo</i>,
     * verbosity level of <i>debug</i> and the message is passed to {@link String#format(String, Object...)}
     *
     * @param msg  The message to be log
     * @param args Optional arguments
     */
    public static void log(String msg, Object... args) {

        if (!enableLogging) return;

        try {
            Log.d(TAG, String.format(msg, args));
        } catch (Exception e) {
            Log.d(TAG, "Error in Debug#log: " + e.getMessage());
        }
    }

    /**
     * Enable/Disable log functions
     *
     * @param debug If sets to <i style='color: #0000ff'>false</i>, all logging activities will be disabled,
     *              otherwise, logging of activities will continue it's default operation
     */
    public static void enable(boolean debug) {
        enableLogging = debug;
    }

    /**
     * @param cls         the class where we look the function
     * @param offsetIndex the stack trace's array index offset
     *                    to be subracted from the lookup index
     * @return
     */
    private static String getMethodName(Class<?> cls, int offsetIndex) {

        int clientCodeStackIndex = 0;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            clientCodeStackIndex++;
            if (ste.getClassName().equals(cls.getName())) {
                break;
            }
        }

        offsetIndex = offsetIndex > clientCodeStackIndex ? 0 : offsetIndex;

        clientCodeStackIndex = (clientCodeStackIndex <= 0) ? 1 : clientCodeStackIndex;

        return Thread.currentThread().getStackTrace()[clientCodeStackIndex - offsetIndex].getMethodName();

    }

    private static File createFileInExternalStorage(String fileName) throws IOException {

        File logFile = new File(Environment.getExternalStorageDirectory(), fileName);

        if (!logFile.exists()) {
            try {
                if (logFile.getParentFile() != null) {
                    logFile.getParentFile().mkdirs();
                }
                logFile.createNewFile();
            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }
        }

        return logFile;
    }

    /**
     * A logger function to be intendedly used by the <i>team CH</i>.
     * Writes a log to a file wherein the filename is the current day in the format of <b>YYYYmdd</b>,
     * and with a following log message format of:
     * <i>DateTime</i> -> <i>Class Name</i> -> <i>Calling Method</i> -> <i>Input parameters</i>
     *
     * @param offsetIndex offsetIndex the stack trace's array index offset
     *                    to be subracted from the lookup index
     * @param msg         The message to be logged. Note that it will be pass in {@link String#format(String, Object...)}
     * @param args        Optional arguments
     */
    public void logToFile(int offsetIndex, String msg, Object... args) {

        if (!enableLogging) return;

        Calendar c = Calendar.getInstance();
        String fileName = c.get(Calendar.YEAR) + "-" +
                (c.get(Calendar.MONTH) + 1) + "-" + // 0 based index
                c.get(Calendar.DAY_OF_MONTH) +
                ".log"; // file extension

        log(msg, args);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            writeToExternalStorage(offsetIndex, fileName, msg, args);
        } else if (mContext != null) {
            writeToInternalStorage(offsetIndex, fileName, msg, args);
        } else {
            log("Error logging to file. The reason is most likely to be in one of the following:\n" +
                    "1. SD Card is unmounted\n" +
                    "2. You have instantiated the wrong Debug constructor\n" +
                    "3. You have passed a null Context\n");
        }

    }

    /**
     * A logger function to be intendedly used by the <i>team CH</i>.
     * Writes a log to a file wherein the filename is the current day in the format of <b>YYYYmdd</b>,
     * and with a following log message format of:
     * <i>DateTime</i> -> <i>Class Name</i> -> <i>Calling Method</i> -> <i>Input parameters</i>
     *
     * @param msg  The message to be logged. Note that it will be pass in {@link String#format(String, Object...)}
     * @param args Optional arguments
     */
    public void logToFile(String msg, Object... args) {

        if (!enableLogging) return;

        Calendar c = Calendar.getInstance();
        String fileName = c.get(Calendar.YEAR) + "-" +
                (c.get(Calendar.MONTH) + 1) + "-" + // 0 based index
                c.get(Calendar.DAY_OF_MONTH) +
                ".log"; // file extension

        log(msg, args);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            writeToExternalStorage(1, fileName, msg, args);
        } else if (mContext != null) {
            writeToInternalStorage(1, fileName, msg, args);
        } else {
            log("Error logging to file. The reason is most likely to be in one of the following:\n" +
                    "1. SD Card is unmounted\n" +
                    "2. You have instantiated the wrong Debug constructor\n" +
                    "3. You have passed a null Context\n");
        }

    }

    /**
     * @param context client context
     * @return log file
     */
    public File getLogFile(Context context) {
        Calendar c = Calendar.getInstance();
        String fileName = c.get(Calendar.YEAR) + "-" +
                // 0 based index
                (c.get(Calendar.MONTH) + 1) + "-" +
                // day of month
                c.get(Calendar.DAY_OF_MONTH) +
                // file extension
                ".log";
        File file = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = createFileInExternalStorage(fileName);
            } else if (mContext != null) {
                file = context.getFileStreamPath(fileName);
            }
        } catch (Exception e) {
        }
        return file;
    }

    private void writeToInternalStorage(int offsetIndex, String fileName, String msg, Object... args) {

        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_APPEND);
            String date = mDateFormatter.format(new Date());
            fos.write(date.getBytes());
            fos.write(("\t").getBytes());
            fos.write(("[" + mCurrentClass.getName() + "]").getBytes());
            fos.write(("[" + getMethodName(mCurrentClass, offsetIndex) + "]").getBytes());
            fos.write(("[" + String.format(msg, args) + "]").getBytes());
            fos.write(LINE_SEPARATOR.getBytes());
            fos.close();
        } catch (Exception e) {
            log("Exception: %s", e.getMessage());
        }

    }

    private void writeToExternalStorage(int offsetIndex, String fileName, String msg, Object... args) {

        try {
            File logFile = createFileInExternalStorage(fileName);
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            String date = mDateFormatter.format(new Date());
            buf.append(date);
            buf.append("\t");
            buf.append("[" + mCurrentClass.getName() + "]");
            buf.append("[" + getMethodName(mCurrentClass, offsetIndex) + "]");
            buf.append("[" + String.format(msg, args) + "]");
            buf.append(LINE_SEPARATOR);
            buf.close();
        } catch (Exception e) {
            log("Exception: %s", e.getMessage());
        }

    }

    public void appendLog(int offsetIndex, File logFile, String msg, Object... args) {

        if (!enableLogging) return;

        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            String date = mDateFormatter.format(new Date());
            buf.append(date);
            buf.append("\t");
            buf.append("[" + mCurrentClass.getName() + "]");
            buf.append("[" + getMethodName(mCurrentClass, offsetIndex) + "]");
            buf.append("[" + String.format(msg, args) + "]");
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            log("Exception: %s", e.getMessage());
        }
    }
}
