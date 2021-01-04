package ml.pisek.wow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.FileProvider;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateAppAndStart extends AsyncTask<String, String, Void> {

    private static final String FILE_NAME = "update_ottwow.apk";

    private Activity activity;
    private TextView statusField;

    public UpdateAppAndStart(Activity activity, TextView statusField) {
        this.activity = activity;
        this.statusField = statusField;
    }

    @Override
    protected Void doInBackground(String... args) {
        try {
            URL url = new URL(args[0]);
            HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
            long size = c.getContentLengthLong();

            String path = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
            File file = new File(path);
            file.mkdirs();
            File outputFile = new File(file, FILE_NAME);

            if (size != outputFile.length()) {

                if (outputFile.exists()) {
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                long downloaded = 0;
                int readLen = 0;
                while ((readLen = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, readLen);
                    downloaded += readLen;
                    publishProgress(downloaded + "b / " + size + "b");
                }
                fos.close();
                is.close();
                c.disconnect();

            }

            publishProgress("Installing...");
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", outputFile);
            install.setData(fileUri);
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            activity.startActivity(install);

            publishProgress("Installed - please restart app");

        } catch (Exception e) {
            Log.e("UpdateAPP", "Update error! " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        for (String value : values) {
            statusField.setText(value);
        }
    }
}