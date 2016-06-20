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
 *  [DQImages] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: DQImages class is used to provide advanced image decoding and loading functionality
 *  for all activity classes.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMImages {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private static final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // fixBackgroundRepeat(): This function is only used to fix a bug that is present in Android 3.0
    // and below where a Bitmap may lose it's tileMode properties. This function is originally from
    // StackOverflow: http://stackoverflow.com/questions/4077487/background-image-not-repeating-in-android-layout
    public static void fixBackgroundRepeat(View view) {

        Drawable background = view.getBackground(); // Retrieves the background of the View.

        if (background != null) {
            if (background instanceof BitmapDrawable) {
                BitmapDrawable bmp = (BitmapDrawable) background; // Creates a new BitmapDrawable instance.
                bmp.mutate(); // Makes sure the state isn't being shared anymore.
                bmp.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT); // Sets the tile mode.
            }
        }
    }

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