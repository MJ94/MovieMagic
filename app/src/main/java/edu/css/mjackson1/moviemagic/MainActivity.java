package edu.css.mjackson1.moviemagic;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText etMovieTitle;


    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMovieTitle = (EditText) findViewById(R.id.etMovieTitle);

    }

    public void onClick (View v) {
        // declare an intent for our new activity
        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra("Title", etMovieTitle.getText().toString());
        startActivity(i); // display new activity
    }
}
