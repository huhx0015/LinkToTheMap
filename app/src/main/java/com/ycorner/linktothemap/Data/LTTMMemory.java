package com.ycorner.linktothemap.Data;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMMemory] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMMemory class is a class that handles memory-related functionality, such as
 *  determining the device's total memory capacity, as well as unbinding View groups that are no
 *  longer needed by activities.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMMemory {

    /** RECYCLE FUNCTIONALITY __________________________________________________________________ **/

    // recycleMemory(): Recycles View objects to clear up memory resources.
    public static void recycleMemory(View layout, Activity activity) {

        // Unbinds all Drawable objects attached to the current layout.
        try { unbindDrawables(layout); }

        // Null pointer exception catch.
        catch (NullPointerException e) {
            e.printStackTrace(); // Prints error message.
            String errorString = "DQMEMORY\nNULL POINTER EXCEPTION OCCURRED WHILE ATTEMPTING TO RECYCLE MEMORY!";
            LTTMError.handleError(errorString, activity); // Initiates error handling.
        }
    }

    // unbindDrawables(): Unbinds all Drawable objects attached to the view layout by setting them
    // to null, freeing up memory resources and preventing Context-related memory leaks. This code
    // is borrowed from Roman Guy at www.curious-creature.org.
    private static void unbindDrawables(View view) {

        // If the View object's background is not null, a Callback is set to render them null.
        if (view.getBackground() != null) { view.getBackground().setCallback(null); }

        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {

            int i = 0;
            for (int x : new int[((ViewGroup) view).getChildCount()]) {
                unbindDrawables(((ViewGroup) view).getChildAt(i++));
            }

            ((ViewGroup) view).removeAllViews(); // Removes all View objects in the ViewGroup.
        }
    }
}