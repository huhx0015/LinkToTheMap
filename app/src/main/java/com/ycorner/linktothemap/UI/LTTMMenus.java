package com.ycorner.linktothemap.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.ycorner.linktothemap.Graphics.LTTMImages;
import com.ycorner.linktothemap.R;
import it.sephiroth.android.library.picasso.Picasso;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMMenus] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMMenus class is used to create different types of customized dialog and spinner
 *  windows for the application.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMMenus {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** MENU FUNCTIONALITY _____________________________________________________________________ **/

    // createMapSpinner(): Sets up the custom layout for the spinner dropdown object.
    @SuppressLint("NewApi")
    public void createMapSpinner(final Context con, Spinner spin, final String maps[]) {

        final Typeface spinnerFont = LTTMFont.getInstance(con).getTypeFace();

        // Initializes and creates a new ArrayAdapter object for the spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(con,
                R.layout.lttm_world_spinner_textview, maps) {

            // getView(): Sets up the spinner view attributes.
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }

            // getView(): Sets up the spinner attributes for the drop down list.
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {

                // Sets up the infrastructure for the drop down list.
                LayoutInflater inflater = LayoutInflater.from(con);
                View worldSpinnerDrop = inflater.inflate(R.layout.lttm_world_spinner, parent, false);

                // Decodes the mapIcon ImageView object for memory optimization purposes.
                ImageView mapIcon = (ImageView) worldSpinnerDrop.findViewById(R.id.lttm_world_spinner_icon);
                Picasso.with(con.getApplicationContext())
                        .load(R.drawable.loz_alttp_map_icon)
                        .withOptions(LTTMImages.setBitmapOptions())
                        .into(mapIcon);

                // Sets up the custom font attributes for the spinner's drop down list.
                TextView mapChoice = (TextView) worldSpinnerDrop.findViewById(R.id.lttm_world_spinner_choice);
                mapChoice.setTypeface(spinnerFont);
                mapChoice.setText(maps[position]);
                mapChoice.setTextSize(20); // Sets the font size to 20sp.

                return worldSpinnerDrop;
            }
        };

        // If the current device runs on Android 4.1 or greater, sets the spinner background to transparent.
        if (api_level > 15) { spin.setPopupBackgroundResource(R.drawable.lttm_transparent_tile); }

        spin.setAdapter(adapter); // Sets the new spinner object.
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // setMapList(): Sets up the list of maps for the spinner, based on the selected game and
    // current language settings.
    public String[] setMapList(Context con, String spinnerList) {

        String[] mapList = new String[0]; // Stores the string array list map values.

        // The Legend of Zelda: A Link to the Past [SNES]
        if (spinnerList.equals("loz_alttp")) {

            // Retrieves map list array from defined XML.
            mapList = con.getResources().getStringArray(R.array.loz_alttp_list);
        }

        return mapList;
    }
}