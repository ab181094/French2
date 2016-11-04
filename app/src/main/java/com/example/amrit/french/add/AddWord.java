package com.example.amrit.french.add;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Amrit on 26-10-2016.
 */

public class AddWord extends AsyncTask<String, String, String> {
    public static final String SAVED_SUCCESSFULLY = "Word saved successfully.";
    Context context;
    String insert_url = "http://www.shaheedabdurrabhall.netne.net/android/insert.php";
    ProgressDialog progressDialog;

    public AddWord(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Saving Data");
        progressDialog.setMessage("Loading... Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        if (type.equals("addWord")) {
            String french = params[1];
            String meaning = params[2];
            try {
                URL url = new URL(insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter
                        (new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("french", "UTF-8") + "=" + URLEncoder.encode(french, "UTF-8") + "&" +
                        URLEncoder.encode("meaning", "UTF-8") + "=" + URLEncoder.encode(meaning, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "URL Error. Try again later.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Connection Error. Try again later.";
            }
        }
        return SAVED_SUCCESSFULLY;
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();
        if (result.equals(SAVED_SUCCESSFULLY)) {
            toastIt(SAVED_SUCCESSFULLY);
        } else {
            toastIt(result);
        }
    }

    private void toastIt(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}