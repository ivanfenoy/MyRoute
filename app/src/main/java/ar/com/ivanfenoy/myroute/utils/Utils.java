package ar.com.ivanfenoy.myroute.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.ivanfenoy.myroute.App;
import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.activities.TripActivity;

/**
 * Created by ivanj on 5/11/2016.
 */

public class Utils {
    public static final Pattern mValidEmailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final int TOAST_INFO = 1;
    public static final int TOAST_ERROR = 2;
    public static final int TOAST_WARNING = 3;

    public static void sendToast(Activity pAct, String pMsg, int pMessageType) {
        switch (pMessageType){
            case TOAST_INFO:
                showToast(pAct, pMsg, "{fa-phone}", pAct.getResources().getColor(R.color.colorPrimary), Toast.LENGTH_SHORT);
                break;
            case TOAST_ERROR:
                showToast(pAct, pMsg, "{fa-times}", pAct.getResources().getColor(R.color.red_google), Toast.LENGTH_SHORT);
                break;
            case TOAST_WARNING:
                showToast(pAct, pMsg, "{fa-warning}", pAct.getResources().getColor(R.color.yellow), Toast.LENGTH_SHORT);
                break;
        }
    }

    public static void sendToast(Activity pAct, int pMsg, int pMessageType) {
        switch (pMessageType){
            case TOAST_INFO:
                showToast(pAct, pAct.getString(pMsg), "{fa-phone}", pAct.getResources().getColor(R.color.colorPrimary), Toast.LENGTH_SHORT);
                break;
            case TOAST_ERROR:
                showToast(pAct, pAct.getString(pMsg), "{fa-times}", pAct.getResources().getColor(R.color.red_google), Toast.LENGTH_SHORT);
                break;
            case TOAST_WARNING:
                showToast(pAct, pAct.getString(pMsg), "{fa-warning}", pAct.getResources().getColor(R.color.yellow), Toast.LENGTH_SHORT);
                break;
        }
    }

    private static void showToast(Activity pAct, String pMsg, String pIcon,int pColor, int pDuration) {
        LayoutInflater wInflater = pAct.getLayoutInflater();
        View layout = wInflater.inflate(R.layout.element_toast, (ViewGroup) pAct.findViewById(R.id.toast_layout_root));

        IconTextView wIcon = (IconTextView) layout.findViewById(R.id.ico_image);
        wIcon.setText(pIcon);
        wIcon.setTextColor(pColor);
        TextView wText = (TextView) layout.findViewById(R.id.toast_text);
        wText.setText(pMsg);

        Toast wToast = new Toast(pAct.getApplicationContext());
        wToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        wToast.setDuration(Toast.LENGTH_LONG);
        wToast.setView(layout);
        wToast.setDuration(pDuration);
        wToast.show();
    }

    public static boolean PermissionGranted(String permission, Context context) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager wConMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        if (wConMgr.getActiveNetworkInfo() != null
                && wConMgr.getActiveNetworkInfo().isAvailable()
                && wConMgr.getActiveNetworkInfo().isConnected()) return true;
        return false;
    }

    public static boolean isUserLogged(Context context) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return wPrefs.getBoolean("USER_LOGGED", false);
    }

    public static void saveUserLogged(Context context, boolean logged) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor wPrefsEditor = wPrefs.edit();
        wPrefsEditor.putBoolean("USER_LOGGED", logged);
        wPrefsEditor.commit();
    }

    public static JSONObject getUserGoogle(Context context) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String wEmail = wPrefs.getString("GOOGLE_EMAIL", "");
        String wName = wPrefs.getString("GOOGLE_NAME", "");
        String wUrlPhoto = wPrefs.getString("GOOGLE_PHOTO", "");

        JSONObject userFacebook = new JSONObject();
        if (wEmail.equals("") && wName.equals("") && wUrlPhoto.equals("")) {
            return userFacebook;
        }
        try {
            userFacebook.put("email", wEmail);
            userFacebook.put("name", wName);
            userFacebook.put("urlPhoto", wUrlPhoto);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userFacebook;
    }

    public static void storeUserGoogle(Context context, String email, String name, String urlPhoto) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = wPrefs.edit();
        prefsEditor.putString("GOOGLE_EMAIL", email);
        prefsEditor.putString("GOOGLE_NAME", name);
        prefsEditor.putString("GOOGLE_PHOTO", urlPhoto);
        prefsEditor.commit();
    }

    public static JSONObject getUserFacebook(Context context) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String email = wPrefs.getString("FACEBOOK_EMAIL", "");
        String name = wPrefs.getString("FACEBOOK_NAME", "");
        String id = wPrefs.getString("FACEBOOK_ID", "");

        JSONObject userFacebook = new JSONObject();
        if (email.equals("") && name.equals("") && id.equals("")) {
            return userFacebook;
        }
        try {
            userFacebook.put("email", email);
            userFacebook.put("name", name);
            userFacebook.put("id", id);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userFacebook;
    }

    public static void storeUserFacebook(Context context, String email, String name, String id) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = wPrefs.edit();
        prefsEditor.putString("FACEBOOK_EMAIL", email);
        prefsEditor.putString("FACEBOOK_NAME", name);
        prefsEditor.putString("FACEBOOK_ID", id);
        prefsEditor.commit();
    }

    public static String parceDistance(double pDistance){
        String wRta;
        if (pDistance > 1000){
            wRta = String.format("%.2f %s", pDistance/1000, "kms");
        }
        else{
            wRta = String.format("%d %s", (int)pDistance, "mts");
        }
        return wRta;
    }

    public static String formatDecimal(float number) {
        return String.format("%10.2f", number).trim();
    }

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = mValidEmailRegex.matcher(emailStr);
        return matcher.find();
    }

    public static Date parseDate(String pDate){
        Date wDate = null;
        try {
            DateFormat wFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            wDate = wFormat.parse(pDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return wDate;
    }

    public static boolean isFirstOpen(Context context) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return wPrefs.getBoolean("FIRST_OPEN", true);
    }

    public static void saveFirstOpen(Context context, boolean logged) {
        final SharedPreferences wPrefs = context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = wPrefs.edit();
        prefsEditor.putBoolean("FIRST_OPEN", logged);
        prefsEditor.commit();
    }

    public static String getDate(long pTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        Calendar wCal = Calendar.getInstance(Locale.ENGLISH);
        wCal.setTimeInMillis(pTime);
        String date = formatter.format(wCal.getTime());
        return date;
    }

    public static String copyImage(Bitmap pImage, String pName) throws IOException {
        File wDestiny = new File(App.FILE_LOCATIONS);
        wDestiny.mkdirs();
        File wFile = new File(App.FILE_LOCATIONS, pName);
        if (wFile.exists()){
            wFile.delete ();
        }
        try {
            FileOutputStream wOut = new FileOutputStream(wFile);
            pImage.compress(Bitmap.CompressFormat.JPEG, 90, wOut);
            wOut.flush();
            wOut.close();
            return wFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void deteleImage(String pImage){
        File wImage = new File(pImage);
        if(wImage.exists()){
            wImage.delete();
        }
    }

//    public static LatLngBounds toBounds(LatLng center, double radius) {
//        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
//        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
//        return new LatLngBounds(southwest, northeast);
//    }
}
