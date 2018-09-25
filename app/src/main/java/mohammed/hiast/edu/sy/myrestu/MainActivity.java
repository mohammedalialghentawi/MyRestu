package mohammed.hiast.edu.sy.myrestu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mohammed.hiast.edu.sy.myrestu.Sample.SampleDataProvider;
import mohammed.hiast.edu.sy.myrestu.database.DBHelper;
import mohammed.hiast.edu.sy.myrestu.database.DataSource;
import mohammed.hiast.edu.sy.myrestu.model.DataItem;
import mohammed.hiast.edu.sy.myrestu.services.MyService;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST =1001 ;
    public static final String MY_GLOBAL_PREFS ="My_prefernce" ;
    public static final String MY_EMAIL_PREFS ="email_preference" ;
    public static final String JSON_URL=
            "https://mohammed.ugar-it.com/services/json/itemsfeed.php";

    private static final int REQUEST_PERMISSION_WRITE = 1000;
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private RecyclerView recyclerView;
    private boolean permissionGranted;

    boolean networkOk;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    String[] mCategories;
    

    DataSource mDataSource;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataItem[] dataItems = (DataItem[]) intent
                    .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            Toast.makeText(MainActivity.this,
                    "Received " + dataItems.length + " items from service",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedData(dataItemList);


        //      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = mCategories[position];
                Toast.makeText(MainActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                displayAllData(category);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
        //      end of navigation drawer


        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);

        boolean isGrid =
                settings.getBoolean(
                        getString(R.string.display_in_grid_pref),false);

        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if(isGrid){
            recyclerView.setLayoutManager(
                    new GridLayoutManager(this,3));
        }
        displayAllData(null);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }

    private void displayAllData(String category){
        List<DataItem> mList = mDataSource.getAllDataItems(category);
        DataItemAdapter adapter = new DataItemAdapter(this,mList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signin_action:{
                Intent intent = new Intent(this,SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
                return true;
            }
            case R.id.settings_action:
                Intent intent = new Intent(this,PrefsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_all_items:
                displayAllData(null);
                // display all items
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);

            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS , MODE_PRIVATE).edit();

            editor.putString(MY_EMAIL_PREFS,email);
            editor.apply();

            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();
        }

    }


}
