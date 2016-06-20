package com.ycorner.linktothemap.Graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMDisplay] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMDisplay class is a class that contains functions that are used to determine the
 *  screen attributes of the Android device.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMDisplay {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private static final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** DISPLAY FUNCTIONALITY ___________________________________________________________________**/

    // getDisplaySize(): Used for retrieving the display size of the device.
    public static int getDisplaySize(Point resolution, int currentOrientation) {

        int size = 0;

        // PORTRAIT MODE: Determines the display size from the resolution.x value.
        if (currentOrientation == 0) { size = resolution.x; }

        // LANDSCAPE MODE: Determines the display size from the resolution.y value.
        else if (currentOrientation == 1) { size = resolution.y; }

        return size;
    }

    // getOrientation(): Retrieves the device's current orientation.
    public static int getOrientation(Context context) {

        int currentOrientation = 0; // Return value for

        // Retrieves the device's current screen orientation.
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            currentOrientation = 0;
        }

        else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentOrientation = 1;
        }

        return currentOrientation;
    }

    // getResolution(): Retrieves the device's screen resolution (width and height).
    @SuppressLint("NewApi")
    public static Point getResolution(Display display) {

        Point displayDimen = new Point(); // Used for determining the device's display resolution.

        // If API Level is 13 and higher (HONEYCOMB_MR2>), use the new method.
        if (api_level > 12) { display.getSize(displayDimen); }

        // If API Level is less than 13 (HONEYCOMB_MR1<), use the depreciated method.
        else {
            displayDimen.x = display.getWidth();
            displayDimen.y = display.getHeight();
        }

        return displayDimen;
    }
}
