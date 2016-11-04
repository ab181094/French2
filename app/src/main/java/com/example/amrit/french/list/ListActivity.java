package com.example.amrit.french.list;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amrit.french.R;
import com.example.amrit.french.about.AboutDialog;
import com.example.amrit.french.add.AddActivity;
import com.example.amrit.french.database.DatabaseHelper;
import com.example.amrit.french.details.DetailsActivity;
import com.example.amrit.french.list.MyCustomAdapter;
import com.example.amrit.french.list.Word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview = (ListView) findViewById(R.id.listView);

        boolean status = isNetworkAvailable();

        if (status) {
            ListBackground listBackground = new ListBackground(this);
            listBackground.execute();
        } else {
            toastIt("Please cheak internet connection");
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            ArrayList<Word> word = databaseHelper.getAllWord();
            showList(word);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = (Word) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
                intent.putExtra("french", word.getFrench());
                intent.putExtra("meaning", word.getMeaning());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(getSupportFragmentManager(), "about_dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ListBackground extends AsyncTask<Void, Void, String> {
        Context con;
        String getUrl = "http://shaheedabdurrabhall.netne.net/android/select.php";
        String JSON_STRING = null;
        ProgressDialog progressDialog;

        public ListBackground(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(con);
            progressDialog.setTitle("Retrieving Data");
            progressDialog.setMessage("Loading... Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(getUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Sorry! Can't Connect Now. Please Try Again Later";
            } catch (IOException e) {
                e.printStackTrace();
                return "Sorry! Can't Connect Now. Please Try Again Later";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            ArrayList<Word> arrayList = parseAll(s);
            showList(arrayList);
        }

        private ArrayList<Word> parseAll(String s) {
            String result = s;
            ArrayList<Word> list = new ArrayList<Word>();
            JSONObject jsonObject;
            JSONArray jsonArray;
            DatabaseHelper databaseHelper = new DatabaseHelper(con);
            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                for (int count = 0; count < jsonArray.length(); count++) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    String id = object.getString("ID");
                    String french = object.getString("French");
                    String meaning = object.getString("Meaning");
                    Word word = new Word(id, french, meaning);
                    long inserted = databaseHelper.insertWord(word);
                    list.add(word);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                toastIt("Error in parsing. Please try again");
            }
            return list;
        }
    }

    private void showList(ArrayList<Word> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            //CustomizedAdapter adapter = new CustomizedAdapter(this, arrayList);
            //listview.setAdapter(adapter);
            listview.setAdapter(new MyCustomAdapter(this, arrayList));
        } else if (arrayList.size() == 0) {
            toastIt("No data found");
        }
    }

    private void toastIt(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}