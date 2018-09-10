package mohammed.hiast.edu.sy.myrestu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mohammed.hiast.edu.sy.myrestu.Sample.SampleDataProvider;
import mohammed.hiast.edu.sy.myrestu.model.DataItem;

public class MainActivity extends AppCompatActivity {

    List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Collections.sort(dataItemList, new Comparator<DataItem>() {
            @Override
            public int compare(DataItem o1, DataItem o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });
        DataItemAdapter adapter = new DataItemAdapter(this,dataItemList);
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.signin_action:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();
        }*/

    }
}
