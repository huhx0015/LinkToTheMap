package com.ycorner.linktothemap.Graphics;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMOpenGL] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMOpenGL class is used to determine the OpenGL capabilities of the Android device.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMOpenGL {

    /** HARDWARE FUNCTIONALITY _________________________________________________________________ **/

    // enableHardwareAcceleration(): Enables or disables hardware acceleration for the View.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void enableHardwareAcceleration(View activityView, boolean enableHWA) {

        // Enables hardware acceleration.
        if (enableHWA) { activityView.setLayerType(View.LAYER_TYPE_HARDWARE, null); }

        // Disables hardware acceleration.
        else { activityView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
    }
}