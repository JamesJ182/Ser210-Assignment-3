/*
The SelectActivity Class
This class displays the details of the selected show
Date:3/21/19
Author:James Jacobson
 */

package edu.quinnipiac.ser210.serassignment33;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {


    LinearLayout l;
    private static final int REQUEST_CODE=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //Toolbar implemetation
        l=(LinearLayout)findViewById(R.id.layout_select);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Sets the background color
        setBackgroundColor(MainActivity.backgroundColor);

        //Gets the details of the show from the ResultClass's intent
        String title=getIntent().getStringExtra("name");
        String rating=getIntent().getStringExtra("rating");
        String premiered=getIntent().getStringExtra("premiered");
        String runtime=getIntent().getStringExtra("runtime");
        String status=getIntent().getStringExtra("status");

        //Displays them in a TExtView
        TextView titleText=(TextView)findViewById(R.id.title);
        TextView ratingText=(TextView)findViewById(R.id.rating);
        TextView premieredText=(TextView)findViewById(R.id.premiered);
        TextView runtimeText=(TextView)findViewById(R.id.runtime);
        TextView statusText=(TextView)findViewById(R.id.status);
        titleText.setText(titleText.getText()+title);
        ratingText.setText(ratingText.getText()+""+rating);
        premieredText.setText(premieredText.getText()+""+premiered);
        runtimeText.setText(runtimeText.getText()+""+runtime+" minutes");
        statusText.setText(statusText.getText()+""+status);


    }
    //Used for setting the color if this activity is returned to
    @Override
    protected void onRestart() {
        setBackgroundColor(MainActivity.backgroundColor);
        super.onRestart();
    }

    //Used to change the background color
    public void setBackgroundColor(int color)
    {
        if(color==0)
        {
            l.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundWhite));
        }
        else if(color==1)
        {
            l.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundRed));
        }
        else if(color==2)
        {
            l.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundGreen));
        }
        else if(color==3)
        {
            l.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundBlue));
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
                Toast.makeText(SelectActivity.this, getString(R.string.api_description_help_base) + getString(R.string.api_description_help_select), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
