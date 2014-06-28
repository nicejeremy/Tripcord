package com.jeremy.tripcord.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by asura1983 on 2014. 6. 18..
 */
public class ByteUtil {

    public static byte[] read(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;

        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {

            }
        }

        return ous.toByteArray();
    }

}
