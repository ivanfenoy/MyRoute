package ar.com.ivanfenoy.myroute.activities;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ar.com.ivanfenoy.myroute.R;
import ar.com.ivanfenoy.myroute.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    private String[] mPermissions = new String[]{
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermissions();
    }

    private void checkPermissions() {
        boolean wAllPermissionsGranted = true;
        for(int i = 0; i< mPermissions.length; i++) {
            if(!Utils.PermissionGranted(mPermissions[i], SplashActivity.this)) wAllPermissionsGranted = false;
        }
        if(!wAllPermissionsGranted) {
            ActivityCompat.requestPermissions(SplashActivity.this, mPermissions, REQUEST_PERMISSIONS);
            return;
        }

        callLogin();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] perms, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, mPermissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS) {
            for(int i=0; i<perms.length;i++) {
                if(!Utils.PermissionGranted(perms[i], SplashActivity.this)) {
                    permissionDenied(perms[i]);
                    return;
                }
            }
            callLogin();
        }
    }

    private void permissionDenied(String permission) {
        Toast.makeText(SplashActivity.this, getString(R.string.permission_denied),
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void callLogin() {
        Intent wI = new Intent(this, LoginActivity.class);
        startActivity(wI);
        finish();
    }
}
