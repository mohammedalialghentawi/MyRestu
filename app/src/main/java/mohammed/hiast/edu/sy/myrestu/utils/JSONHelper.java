package mohammed.hiast.edu.sy.myrestu.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import mohammed.hiast.edu.sy.myrestu.DataItemAdapter;
import mohammed.hiast.edu.sy.myrestu.model.DataItem;

public class JSONHelper {
    public static final  String FILE_NAME = "menuitems.json";
    public static final  String TAG = "JSONHelper";

    public static boolean exportToJson(Context context, List<DataItem> mList){
        DataItems menuData = new DataItems();
        menuData.setDataItemsMenu(mList);

        Gson gson = new Gson() ;
        String jsonString = gson.toJson(menuData);

        Log.i(TAG,jsonString);

        FileOutputStream fileOutputStream = null;
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static List<DataItem> importFromJson(Context context){

        FileReader fileReader = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(),FILE_NAME);
            fileReader = new FileReader(file);
            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(fileReader,DataItems.class);

            return dataItems.getDataItemsMenu();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    static class DataItems {
        private List<DataItem> dataItemsMenu;

        public List<DataItem> getDataItemsMenu() {
            return dataItemsMenu;
        }

        public void setDataItemsMenu(List<DataItem> dataItemsMenu) {
            this.dataItemsMenu = dataItemsMenu;
        }
    }
}
