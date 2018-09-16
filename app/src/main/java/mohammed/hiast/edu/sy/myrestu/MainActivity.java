package mohammed.hiast.edu.sy.myrestu;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import mohammed.hiast.edu.sy.myrestu.model.DataItem;
import mohammed.hiast.edu.sy.myrestu.utils.JSONHelper;

public class MainActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST =1001 ;
    public static final String MY_GLOBAL_PREFS ="My_prefernce" ;
    public static final String MY_EMAIL_PREFS ="email_preference" ;
    private static final int REQUEST_PERMISSION_WRITE = 1000;
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private RecyclerView recyclerView;
    private boolean permissionGranted;

    ListView mDrawerList;
    DrawerLayout mDrawerLayout;
    String[] mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
//      end of navigation drawer

        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });


        DataItemAdapter adapter = new DataItemAdapter(this,dataItemList);

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
        recyclerView.setAdapter(adapter);
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
            case R.id.export_to_json_action: {
                boolean result = JSONHelper.exportToJson(this, dataItemList);
                if (result) {
                    Toast.makeText(this, "File Exported", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "File NOT Exported", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
                case R.id.import_from_json_action:{
                    List<DataItem> importedList = JSONHelper.importFromJson(this);

                    for (DataItem ite:importedList  ) {
                        Log.i(JSONHelper.TAG,ite.getItemName());
                    }
                    return true;
                }
            case R.id.action_all_items:
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

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageReadable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
