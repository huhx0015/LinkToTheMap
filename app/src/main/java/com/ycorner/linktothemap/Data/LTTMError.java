package com.ycorner.linktothemap.Data;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import com.ycorner.linktothemap.Activities.LTTMTitleScreenActivity;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMError] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMError class is a class that handles error related functionality.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMError {

    /** ERROR FUNCTIONALITY ____________________________________________________________________ **/

    // handleError(): A function that handles error codes to launch into LTTMTitleScreen activity to
    // display the appropriate error message.
    public static void handleError(String error, Activity activity) {

        // Initializes the SharedPreferences object and saves the error code and mode values into
        // preferences.
        SharedPreferences preferences = LTTMPreferences.initializePreferences("dq_temps", activity);
        LTTMPreferences.setErrorCode(error, preferences); // Sets the error message.
        LTTMPreferences.setErrorMode(true, preferences); // Enables error handling mode.

        Intent i = new Intent(activity, LTTMTitleScreenActivity.class); // Sets up the LTTMTitleScreen activity.
        activity.startActivityForResult(i, 0); // Begins the LTTMTitleScreen activity in error mode.
        activity.finish(); // The current activity is finished.
    }
}
