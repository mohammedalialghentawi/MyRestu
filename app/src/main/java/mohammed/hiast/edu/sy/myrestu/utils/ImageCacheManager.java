package mohammed.hiast.edu.sy.myrestu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mohammed.hiast.edu.sy.myrestu.model.DataItem;

public class ImageCacheManager {

    public static Bitmap getBitmap(Context context, DataItem dataItem) {

        String fileName = context.getCacheDir() + "/" + dataItem.getImage();
        File file = new File(fileName);
        if (file.exists()) {
            try {
                return BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static void putBitmap(Context context, DataItem dataItem,
                                 Bitmap bitmap) {
        String fileName = context.getCacheDir() + "/" + dataItem.getImage();
        File file = new File(fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85,
                    outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}