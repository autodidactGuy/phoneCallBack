package br.android.cericatto.phonecallback;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

/**
 * MainActivity.java.
 *
 * @author Rodrigo Cericatto
 * @since Nov 19, 2016
 */
public class MainActivity extends AppCompatActivity {

    //--------------------------------------------------
    // Constants
    //--------------------------------------------------

    private static final String[] PERMISSIONS = {
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION };
    private static final int PERMISSION_REQUEST = 100;

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    private TelephonyManager mTelephonyManager;

    //--------------------------------------------------
    // Activity Life Cycle
    //--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    //--------------------------------------------------
    // Permissions
    //--------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    Boolean permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (permissionGranted) {
                        // Permission was granted.
                        callPhoneManager();
                    } else {
                        // Permission denied.
                        PermissionUtils.alertAndFinish(this);
                    }
                }
                return;
            }
        }
    }

    private void checkPermissions() {
        // Checks the Android version of the device.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Boolean canWriteExternalStorage = PermissionUtils.canReadPhoneState(this);
            Boolean canReadExternalStorage = PermissionUtils.canAccessCoarseLocation(this);
            if (!canWriteExternalStorage || !canReadExternalStorage) {
                requestPermissions(PERMISSIONS, PERMISSION_REQUEST);
            } else {
                // Permission was granted.
                callPhoneManager();
            }
        } else {
            // Version is below Marshmallow.
            callPhoneManager();
        }
    }

    private void callPhoneManager() {
        TextView textView = (TextView)findViewById(R.id.id_text_view);
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTelephonyManager.listen(new PhoneCallback(textView), PhoneStateListener.LISTEN_CALL_STATE
            | PhoneStateListener.LISTEN_CELL_INFO // Requires API 17
            | PhoneStateListener.LISTEN_CELL_LOCATION
            | PhoneStateListener.LISTEN_DATA_ACTIVITY
            | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
            | PhoneStateListener.LISTEN_SERVICE_STATE
            | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
            | PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
            | PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);
    }
}