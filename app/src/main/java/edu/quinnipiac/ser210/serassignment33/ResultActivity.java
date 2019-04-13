/*
The ResultActivity Class
This class displays the results of the search in a list,
 and handles the operation of the item being clicked
Date:3/21/19
Author:James Jacobson
 */
package edu.quinnipiac.ser210.serassignment33;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import javax.xml.transform.Result;

public class ResultActivity extends AppCompatActivity implements ResultFragment.OnFragmentInteractionListener {

    //Instance Variables
    String[] showNames;
    JSONObject[] showNamesJSON;
    int REQUEST_CODE = 0;
    LinearLayout l;
    LinearLayout lContain;//The layout that contains the fragment
    ListView list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Toolbar implementation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Sets the color of the background
        ResultFragment result=(ResultFragment)getSupportFragmentManager().getFragments().get(0);
        l = (LinearLayout) result.getView();
        lContain=(LinearLayout)findViewById(R.id.result_activity_linear);
        setBackgroundColor(MainActivity.backgroundColor);

        //Gets the results from the MainActivity search
        Intent intent = getIntent();
        showNames = intent.getStringArrayExtra("Show Titles");
        showNamesJSON = new JSONObject[showNames.length];
        String temp[] = (intent.getStringArrayExtra("JSON Shows"));
        try {
            for (int i = 0; i < temp.length; i++) {
                //Converts String to JSON object
                showNamesJSON[i] = new JSONObject(temp[i]);
            }
        } catch (Exception e) {
            Log.e("Error", "A show is null");
        } finally {

            //Creates an ArrayAdapter that takes the list of titles as its list
            //Adapts the list from the ResultFragment
            list = findViewById(android.R.id.list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, showNames);
            list.setAdapter(adapter);


        }
    }


    //This comes from the ResultFragment class.
    @Override
    public void onFragmentInteraction(int position) {
        try {
            //Sends out the info to the SelectActivity
            Intent intent = new Intent(ResultActivity.this, SelectActivity.class);
            //Gets the requested details of the show at the items position
            JSONObject show = showNamesJSON[position].getJSONObject("show");
            intent.putExtra("name", show.getString("name"));
            intent.putExtra("rating", show.getJSONObject("rating").getString("average"));
            intent.putExtra("status", show.getString("status"));
            intent.putExtra("premiered", show.getString("premiered"));
            intent.putExtra("runtime", show.getString("runtime"));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("Error", "JSON Object property does not exist");
            Log.e("Error", e.getMessage());
        }

    }
    //Used for setting the color if this activity is returned to
    @Override
    protected void onRestart() {
        setBackgroundColor(MainActivity.backgroundColor);
        super.onRestart();
    }


    //Used to change the background color
    public void setBackgroundColor(int color) {
        if (color == 0) {
            lContain.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundWhite));
        } else if (color == 1) {
            lContain.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRed));
        } else if (color == 2) {
            lContain.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundGreen));
        } else if (color == 3) {
            lContain.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundBlue));
        }
    }


    //Toolbar methods
    //Up-Button implementation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Used after the SettingActivity has ended, changing the background color
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Checks if the result code and request code are as expected, and if there is a color chosen
        if(resultCode == RESULT_OK  && requestCode == REQUEST_CODE&&data.hasExtra("color")){
            //changes the backgroundColor variable
            MainActivity.backgroundColor=data.getIntExtra("color",0);
            setBackgroundColor(MainActivity.backgroundColor);
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    //Creates icons for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Triggered when a toolbar option is hit, responds with appropriate action
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //If the setting button was hit, starts the Settings Activity to change the background color
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingIntent, REQUEST_CODE);
                return true;
            //If the share button was hit, share it with an implict activity
            case R.id.action_share:
                Resources resources = getResources();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.maze_tv_promo));
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                return true;
                //If the help button was hit, display a Toast with a helpful description
            case R.id.action_help:
                Toast.makeText(ResultActivity.this, getString(R.string.api_description_help_base) + getString(R.string.api_description_help_detail), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


}