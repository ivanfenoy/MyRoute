package ar.com.ivanfenoy.myroute;

import android.app.Application;

import ar.com.ivanfenoy.myroute.data.DataBaseConnection;

/**
 * Created by ivanj on 29/10/2016.
 */

public class App extends Application {

    private static DataBaseConnection mDB;

    public static DataBaseConnection DB(){
        if (mDB == null){
            mDB = new DataBaseConnection();
        }
        return mDB;
    }

}
