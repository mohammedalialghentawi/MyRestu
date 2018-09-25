package mohammed.hiast.edu.sy.myrestu.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import mohammed.hiast.edu.sy.myrestu.model.DataItem;
import mohammed.hiast.edu.sy.myrestu.utils.HttpHelper;

public class MyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public static final String MY_SERVICE_MESSAGE="My_Service_Message";
    public static final String MY_SERVICE_PAYLOAD="My_Service_Payload";

    public static final String TAG = "MyService";
    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri uri = intent.getData();

         String response="";
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Log.i(TAG, "onHandleIntent: "+uri.toString());


        Gson gson = new Gson();
        DataItem[] items =gson.fromJson(response,DataItem [].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD,items);
        LocalBroadcastManager manger =
                LocalBroadcastManager.getInstance(getApplicationContext());

        manger.sendBroadcast(messageIntent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
