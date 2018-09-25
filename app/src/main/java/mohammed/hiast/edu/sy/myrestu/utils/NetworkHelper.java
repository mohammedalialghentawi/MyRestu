package mohammed.hiast.edu.sy.myrestu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

    public static boolean hasAccessToNetwork(Context context){

        ConnectivityManager cm =(ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            return networkInfo!=null &&
                    networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }
}
