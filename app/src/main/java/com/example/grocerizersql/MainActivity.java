package com.example.grocerizersql;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper groceryDb;
    private String m_Text = "";

    // This URL must be manually updated every week.
    // TODO: Figure out how to get the URL automatically
    String url = "https://wklyads.fredmeyer.com/flyer_data/1503840?locale=en-US";

    getJsonTask gtask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groceryDb = new DatabaseHelper(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when the user taps the Enter button
    public void inputKeyword(View view) {
        EditText editText = findViewById(R.id.editText);
        String input = editText.getText().toString();

        groceryDb.addKeyword(input);
    }

    // Called when the user taps the Clear button
    public void clear(View view) {
        groceryDb.clearTable();
    }

    // Called when the user taps the Print button
    // Currently prints to console rather than the app screen
    public void print(View view) { groceryDb.printTable(); }

    public void fetch(View view) { // Parse weekly ad
        gtask = new getJsonTask();
        gtask.execute(url, groceryDb);
    }

    /*
    // Parse weekly ad JSON
    public void parseAd() {
        String url = "https://wklyads.fredmeyer.com/flyer_data/1484730?locale=en-US";

        JSONGetter jp = new JSONGetter();
        JsonArray items = jp.getItems(url); // Retrieves and prints items
        groceryDb.populateTable(items);
    }*/
}

class getJsonTask extends AsyncTask<Object, Void, JsonArray> {

    private Exception exc;
    private JSONGetter jp;

    // Won't let me set the return to void
    protected JsonArray doInBackground(Object... params) {
        try {
            String url = (String)params[0];
            System.out.println(url);
            jp = new JSONGetter();
            JsonArray items = jp.getItems(url);
            System.out.println("Got JSON!");
            ((DatabaseHelper)params[1]).populateTable(items);
            System.out.println("Table populated!");
        } catch (Exception e) {
            this.exc = e;
            Log.e("Exception", e.toString());
        }
        return null;
    }
}
