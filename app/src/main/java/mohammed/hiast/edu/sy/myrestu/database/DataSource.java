package mohammed.hiast.edu.sy.myrestu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mohammed.hiast.edu.sy.myrestu.model.DataItem;

public class DataSource {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mDBhelper;

    public DataSource(Context context){
        mContext=context;
        mDBhelper = new DBHelper(context);
        mDatabase = mDBhelper.getWritableDatabase();
    }
    public void open(){
        mDatabase = mDBhelper.getWritableDatabase();
    }

    public void close(){
        mDBhelper.close();
    }

    public DataItem createDataItem (DataItem item){


        ContentValues values = item.toValues();
        mDatabase.insert(ItemsTable.TABLE_ITEMS,null,values);
        return item;
    }

    public long getDataItemsCount(){
        return DatabaseUtils.queryNumEntries(mDatabase,ItemsTable.TABLE_ITEMS);
    }

    public  void seedData(List<DataItem> dataItemList){
        if(getDataItemsCount()==0){
            for (DataItem item: dataItemList) {

                try {
                    createDataItem(item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<DataItem> getAllDataItems(String category){
        List<DataItem> mList = new ArrayList<>();

        Cursor cursor = null;
        if(category==null){
             cursor = mDatabase.query(ItemsTable.TABLE_ITEMS,ItemsTable.ALL_COLUMNS,
                    null,null,null,null,ItemsTable.COLUMN_NAME );

        }else{
            String [] categories = {category};
            cursor = mDatabase.query(ItemsTable.TABLE_ITEMS,ItemsTable.ALL_COLUMNS,
                    ItemsTable.COLUMN_CATEGORY + "=?",categories,null,null,ItemsTable.COLUMN_NAME );

        }

        while (cursor.moveToNext()){
            DataItem item = new DataItem();
            item.setItemId(
                    cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_ID)
                    )
            );
            item.setItemName(
                    cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_NAME)
                    )
            );
            item.setPrice(
                    cursor.getDouble(
                            cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)
                    )
            );
            item.setSortPosition(
                    cursor.getInt(
                            cursor.getColumnIndex(ItemsTable.COLUMN_POSITION)
                    )
            );
            item.setImage(
                    cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)
                    )
            );
            item.setDescription(
                    cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)
                    )
            );
            item.setCategory(
                    cursor.getString(
                            cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)
                    )
            );
            mList.add(item);
        }
        cursor.close();

        return mList;
    }
}
