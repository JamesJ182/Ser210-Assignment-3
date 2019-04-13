/*
The SettingActivity Class
This class displays the options to change the background color to the user
Date:3/21/19
Author:James Jacobson
 */

package edu.quinnipiac.ser210.serassignment33;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SettingsActivity extends ListActivity {

    //Instance Variables
    Class source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Gets the sender class for context
        source=getIntent().getClass();
    }

    //When a color is click, it will be sent to the activity it came from, and set as the background color
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent=new Intent(this,source);
        //Position is used as the "key" of the color
        intent.putExtra("color",position);
        setResult(RESULT_OK,intent);
        this.finish();//Ends the activity
        super.onListItemClick(l, v, position, id);
    }


}
