package ml.pisek.wow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateApp extends AsyncTask<String,Void,Void> {

    private Activity activity;

    private GridLayout gridLayout;

    public UpdateApp(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        gridLayout = new GridLayout(activity);
        TextView textView = new TextView(activity);
        textView.setText("DUPA");
        gridLayout.addView(textView);
        activity.addContentView(gridLayout, new GridLayout.LayoutParams());
    }

    @Override
    protected Void doInBackground(String... arg0) {
        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = "/mnt/sdcard/Download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "update.apk");
            if(outputFile.exists()){
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/Download/update.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
            activity.getApplicationContext().startActivity(intent);

        } catch (Exception e) {
            Log.e("UpdateAPP", "Update error! " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        gridLayout.removeAllViews();
    }
}