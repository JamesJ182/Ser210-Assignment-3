/*
The MainActivity Class
This class searches for a show inputed by the user, receives the results from the API,
    and creates 2 arrays to display in future activities
Date:3/21/19
Author:James Jacobson
 */



package edu.quinnipiac.ser210.serassignment33;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener{

    //Instance Variables
    private final String PART_OF_URL = "https://tvjan-tvmaze-v1.p.rapidapi.com/search/shows?q=";
    private String search;
    private String[] showNames;
    private String[] showNameJSON;
    public static  final int REQUEST_CODE = 0;
    public static int backgroundColor=0;
    private ConstraintLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar implementation
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //setting the color of the layout
        l=(ConstraintLayout) getSupportFragmentManager().getFragments().get(0).getView();
        setBackgroundColor(backgroundColor);

        //Adding the mazeTV logo
        ImageView logo=(ImageView)findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.maze_tv);

    }

    //Used for setting the color if this activity is returned to
    @Override
    protected void onRestart() {
        setBackgroundColor(backgroundColor);
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

    //Removed the onClick method, this is an interaction betweeen the Search Fragment
    //and the Main activity, using an interface. Begins the searching process
    @Override
    public void onFragmentInteraction() {
        EditText searchText = (EditText) findViewById(R.id.editText);
        search=searchText.getText().toString();//User Input
        //Have to replace all spaces with "%20" due to API URL
        while (search.contains(" ")) {
            search = search.replace(" ", "%20");
        }
        //Executes the search
        new FetchShows().execute(search);

    }

    //Used after the SettingActivity has ended, changing the background color
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Checks if the result code and request code are as expected, and if there is a color chosen
        if(resultCode == RESULT_OK  && requestCode == REQUEST_CODE&&data.hasExtra("color")){
            //changes the backgroundColor variable
            backgroundColor=data.getIntExtra("color",0);
            setBackgroundColor(backgroundColor);
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    //Toolbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Triggered when a toolbar option is hit, responds with appropriate action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {
            //If the setting button was hit, starts the Settings Activity to change the background color
            case R.id.action_settings:
                Intent settingIntent=new Intent(this,SettingsActivity.class);
                startActivityForResult(settingIntent,REQUEST_CODE);
                return true;
            //If the share button was hit, share it with an implict activity
            case R.id.action_share:
                Resources resources = getResources();
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,resources.getString(R.string.maze_tv_promo));
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                return true;
            //If the help button was hit, display a Toast with a helpful description
            case R.id.action_help:
                Toast.makeText(MainActivity.this,getString(R.string.api_description_help_base)+getString(R.string.api_description_help_main),Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }





    //Private class to get shows from API
    private class FetchShows extends AsyncTask<String, Void, String> {

        //Gets API JSON data while working in the background
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(PART_OF_URL + search);//Creates complete search URL

                //Opens connection to API
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key", "2cf6f93237mshc56bd9d910ab8ffp137878jsn13d36c0c4b24");
                urlConnection.connect();

                //Gets JSON from API
                InputStream in = urlConnection.getInputStream();
                if (in == null) {
                    Toast.makeText(MainActivity.this,getString(R.string.no_shows_found),Toast.LENGTH_SHORT).show();
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(in));

                //Gets data into a JSON string, then goes to the handler to receive JSON in an organized, useful way
                String showJsonString = getBufferStringFromBuffer(reader).toString();
                ShowSpinnerHandler SSH=new ShowSpinnerHandler();

                //Handler gets the name of the shows, and the JSON Object which contain the details
                showNames=SSH.createArrayOfShows(showJsonString);
                showNameJSON=new String[showNames.length];
                JSONObject[] temp=SSH.getshowNamesJSON();
                for(int i=0;i<temp.length;i++)
                {
                    //Converts the Json objects to Strings
                    showNameJSON[i]=temp[i].toString();
                }




            } catch (Exception e) {
                Log.e("Error",e.getMessage());
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        return null;
                    }
                }
            }

            return "";//Did not need to return string
        }


        //After the information is collected, starts the ResultActivity
        @Override
        protected void onPostExecute(String s) {

            Intent intent=new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("Show Titles",showNames);//Just the titles
            intent.putExtra("JSON Shows",showNameJSON);//Titles with details
            startActivity(intent);
            super.onPostExecute(s);
        }
    }
    //Converts stream to String
    private StringBuffer getBufferStringFromBuffer(BufferedReader br) throws Exception{
        StringBuffer buffer = new StringBuffer();

        String line;
        while((line = br.readLine()) != null){
            buffer.append(line + '\n');
        }

        if (buffer.length() == 0)
            return null;

        return buffer;
    }





}
