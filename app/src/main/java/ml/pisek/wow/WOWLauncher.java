package ml.pisek.wow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class WOWLauncher extends Activity {

    private static final String PACKAGE_OTTWOW = "pl.festival.mobile.android";
    private static final String WOW_APK_LINK = "https://raw.githubusercontent.com/pisek/WOWLauncher/master/apks/pl.festival.mobile.android_032.apk";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextView statusField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        statusField = new TextView(this);
        statusField.setText("Initializing...");
        layout.addView(statusField);
        layout.setGravity(Gravity.CENTER);
        this.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));



        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(PACKAGE_OTTWOW);
        if (launchIntent == null) {
            verifyStoragePermissions(this);
        } else {
            statusField.setText("Starting...");
            startActivity(launchIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateWowApp();
            }
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            updateWowApp();
        }
    }

    private void updateWowApp() {
        UpdateAppAndStart updateApp = new UpdateAppAndStart(this, statusField);
        updateApp.execute(WOW_APK_LINK);
    }

}
