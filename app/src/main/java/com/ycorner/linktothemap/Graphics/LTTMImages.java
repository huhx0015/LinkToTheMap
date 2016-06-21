package com.ycorner.linktothemap.Graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMImages] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMImages class is used to provide advanced image decoding and loading
 *  functionality for all activity classes.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMImages {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private static final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // getImageDimensions(): Retrieves the image's width and height properties.
    public static Point getImageDimensions(Resources res, int mapId) {

        Point imageDimens = new Point(); // Stores the image dimension values.

        // First decode with inJustDecodeBounds = true to check dimensions.
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // Needs to be set to true, or OutOfMemory error is likely to occur.
        BitmapFactory.decodeResource(res, mapId, options);

        // Retrieves the image's width and height properties.
        imageDimens.x = options.outWidth;
        imageDimens.y = options.outHeight;

        return imageDimens;
    }

    // setBitmapOptions(): setBitmapOptions is a function that sets the default Bitmap options for
    // the image objects. It sets the color profile to RGB_565, as well as allowing the image object
    // to be purged for memory optimization. This function is used in conjunction with Picasso's
    // .withOptions function.
    public static BitmapFactory.Options setBitmapOptions() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = false; // Disables the dithering option.

        // NOTE: The inPurgeable option is only applied for Android versions less than API 21.
        // LOLLIPOP has depreciated inPurgeable and inInputSharable and ignores these options.
        if (api_level < 20) {
            options.inPurgeable = true; // Allocates Bitmap's pixels so that they may be purged if system needs to reclaim memory.
            options.inInputShareable = true; // Determines whether the bitmap can share a reference to the input data or if it must make a deep copy.
        }

        return options;
    }
}