package com.frevocomunicacao.tracker.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * Created by Vinicius on 20/03/16.
 */
public class ImageUtils {

    // default
    private static final int MEDIA_TYPE = 1;
    private static final String EXTENSION = ".jpg";
    private static final String IMAGE_FOLDER = "tracker/images";

    // debug
    private static final String TAG = "ImageUtils";

    public static File saveImage(Bitmap bmp) {
        File mediaStorageDir            = new File(Environment.getExternalStorageDirectory(), IMAGE_FOLDER);
        FileOutputStream outputStream   = null;
        String filename                 = md5(String.valueOf(new Date()));

        // check directory exists
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // create image file
        File imageFile       = new File(mediaStorageDir.getPath() + File.separator + filename + EXTENSION);

        // save file
        try {
            outputStream    = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imageFile.exists() ? imageFile.getAbsoluteFile() : null;
    }

    private static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
