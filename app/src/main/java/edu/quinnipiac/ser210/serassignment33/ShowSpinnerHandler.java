/*
The ShowSpinnerHandler Class
This class handles the JSON strings and objects to be sent back to the MainActivity in
    a more organized manner
Date:3/21/19
Author:James Jacobson
 */
package edu.quinnipiac.ser210.serassignment33;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShowSpinnerHandler {

    //Instance Variables
    private JSONObject[] showNamesJSON;//The JSON objects that contain the details
    private String[] showNames;//The titles of the shows


    public String[]createArrayOfShows(String showString) throws org.json.JSONException
    {
        JSONArray allShows=new JSONArray(showString);//The entire JSON string, converted into a JSON array
        showNamesJSON=new JSONObject[allShows.length()];
        showNames=new String[allShows.length()];
        for(int i=0;i<allShows.length();i++)
        {
            JSONObject singleShow=allShows.getJSONObject(i);//Gets a show at position i
            JSONObject showName=singleShow.getJSONObject("show");//Gets the title of the show
            showNamesJSON[i]=singleShow;//Saves that show
            showNames[i]=showName.getString("name");//Saves the shows title
        }
        return showNames;//Returns the shows title
    }

    //Gets the shows and its details as a JSONObject array
    public JSONObject[] getshowNamesJSON()
    {
        return showNamesJSON;
    }

}
