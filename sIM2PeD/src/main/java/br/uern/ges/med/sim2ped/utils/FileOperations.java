package br.uern.ges.med.sim2ped.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


import br.uern.ges.med.sim2ped.beans.Context;

/**
 * Created by Rodrigo on 06/10/2014 (22:46).
 *
 *
 * Package: br.uern.ges.med.sim2ped.utils
 */
public class FileOperations {

    /** Method to check whether external media available and writable. This is adapted from
     http://developer.android.com/guide/topics/data/data-storage.html#filesExternal */

    public int checkExternalMedia(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            return 2;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            return 1;
        } else {
            // Can't read or write
            return -1;
        }
    }

    /** Method to write ascii text characters to file on SD card. Note that you must add a
     WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     a FileNotFound Exception because you won't have write permission. */

    public boolean writeToSDFile(String fileName, String fileContent){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
        File dir = new File (root.getAbsolutePath() + "/SIM2PeD");
        dir.mkdirs();
        File file = new File(dir, fileName);

        try {
/*
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            //pw.append(fileContent);
            pw.println(fileContent);
            pw.flush();
            pw.close();
            f.close(); */

            PrintStream p = new PrintStream(new BufferedOutputStream(new FileOutputStream(file, true)));
            p.println(fileContent);
            p.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(Constant.TAG_LOG, "File not found. Did you add a WRITE_EXTERNAL_STORAGE " +
                    "permission to the manifest?");
            return false;
        } catch (Exception ignored){
            return false;
        }

        return true;
    }
}
