package ar.com.ivanfenoy.myroute;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;

import ar.com.ivanfenoy.myroute.activities.LoginActivity;
import ar.com.ivanfenoy.myroute.data.DataBaseConnection;
import ar.com.ivanfenoy.myroute.model.User;

/**
 * Created by ivanj on 29/10/2016.
 */

public class App extends Application {
    public static final String SHARED_PREFERENCES = "ar.com.ivanfenoy.myroute.shared_preferences";
    public static final String FILE_LOCATIONS = Environment.getExternalStorageDirectory().toString() + "/MyRoute";
    private static DataBaseConnection mDB;
    private static Context mContext;
    public static User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Iconify.with(new FontAwesomeModule())
                .with(new MaterialCommunityModule());
    }

    public static DataBaseConnection DB(){
        if (mDB == null){
            mDB = new DataBaseConnection();
        }
        return mDB;
    }

    public static void sentToLogin(){
        Intent wI = new Intent(mContext, LoginActivity.class);
        wI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(wI);
    }

}
