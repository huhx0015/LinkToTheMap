package com.ycorner.linktothemap.UI;

import android.content.Context;
import android.graphics.Typeface;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMFont] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMFont class is used to set custom font types to Android layout objects. As
 *  Android devices installed with Android OS versions before Ice Cream Sandwich (4.0) are prone
 *  to a memory leak bug with Typeface objects, this class avoids such issue by using only a single
 *  instance. This code is borrowed from LeoCardz at http://lab.leocardz.com/android-custom-font-without-memory-leak
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMFont {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private final Context context;
    private static LTTMFont instance;

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    // LTTMFont(): Constructor for the LTTMFont class.
    private LTTMFont(Context context) {
        this.context = context;
    }

    // getInstance(): Creates an instance of the LTTMFont class.
    public static LTTMFont getInstance(Context context) {
        synchronized (LTTMFont.class) {
            if (instance == null)
                instance = new LTTMFont(context);
            return instance;
        }
    }

    // getTypeFace(): Retrieves the custom font family (Return of Gannon) from resources.
    public Typeface getTypeFace() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/return_of_gannon.ttf");
    }
}
