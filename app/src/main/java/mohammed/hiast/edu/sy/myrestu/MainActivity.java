package mohammed.hiast.edu.sy.myrestu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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
}
