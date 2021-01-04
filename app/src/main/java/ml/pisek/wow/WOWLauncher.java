package ml.pisek.wow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

public class WOWLauncher extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("pl.festival.mobile.android");
//        if (launchIntent != null) {
//            startActivity(launchIntent);
//        } else {
            UpdateApp updateApp = new UpdateApp(this);
            updateApp.execute("https://serverurl/appfile.apk");
//        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
