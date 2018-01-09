package com.example.grocerizersql;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.JsonArray;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper groceryDb;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        groceryDb = new DatabaseHelper(this);

        // Parse weekly ad
        //parseAd();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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

    /** Called when the user taps the Add button */
    public void inputKeyword(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String input = editText.getText().toString();

        groceryDb.addKeyword(input);
        // Do..... stuff
        // Display list?
    }

    // Parse weekly ad JSON
    public void parseAd() {
        // TODO: find/generate this URL from search
        String url = "https://wklyads.fredmeyer.com/flyer_data/1484730?locale=en-US";

        JSONParser jp = new JSONParser();
        jp.doTheThing(url); // For now this just prints the items
        // Later it will make SQL
    }
}
