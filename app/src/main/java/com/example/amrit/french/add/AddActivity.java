package com.example.amrit.french.add;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.amrit.french.R;

public class AddActivity extends AppCompatActivity {

    EditText etfrench, etmeaning;
    String french, meaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etfrench = (EditText) findViewById(R.id.french);
        etmeaning = (EditText) findViewById(R.id.meaning);
    }

    public void addWord(View view) {
        french = etfrench.getText().toString();
        meaning = etmeaning.getText().toString();
        if (TextUtils.isEmpty(french) || TextUtils.isEmpty(meaning)) {
            if (TextUtils.isEmpty(french)) {
                etfrench.setError("This field can't be empty.");
            } else {
                etmeaning.setError("This field can't be empty.");
            }
        } else {
            String type = "addWord";
            etfrench.setText("");
            etmeaning.setText("");
            AddWord addWord = new AddWord(this);
            addWord.execute(type, french, meaning);
            //toastIt(french+meaning);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
