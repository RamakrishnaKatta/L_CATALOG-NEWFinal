package com.immerrsionslabs.l_catalog_new.Utils;


import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ImageUtils {

    // SD card image directory
    private static final String PHOTO_ALBUM = "/L_CATALOGUE/Screenshots/";

    private static final String TAG = "Utils";

    // supported file formats
    private static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

    private Context _context;
    private File directory;

    // constructor
    public ImageUtils(Context context) {
        this._context = context;
    }

    /*
     * Reading file paths from SDCard
     */
    public ArrayList<String> getFilePaths() {

        ArrayList<String> filePaths = new ArrayList<>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "ERROR, No Card Found------");
        } else {
            directory = new File(android.os.Environment.getExternalStorageDirectory() + PHOTO_ALBUM);
            directory.mkdirs();
        }

        // check for directory
        if (directory.isDirectory()) {

            // getting list of file paths
            File[] listOfFiles = directory.listFiles();
            //Log.e(TAG, "List Of files------" + Arrays.toString(listOfFiles));

            // Check for count
            if (listOfFiles.length > 0) {

                // loop through all files
                for (File listOfFile : listOfFiles) {

                    // get file path
                    String filePath = listOfFile.getAbsolutePath();
                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                        filePaths.add(filePath);
                    }
                }
            } else {
                // image directory is empty
                Toast.makeText(_context, PHOTO_ALBUM + " is empty. Please load some images in it !", Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(PHOTO_ALBUM + " directory path is not valid! Please set the image directory name AppConstant.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    /*
     * Check supported file extensions
     * @returns boolean
     */
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());

        if (FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;
    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
