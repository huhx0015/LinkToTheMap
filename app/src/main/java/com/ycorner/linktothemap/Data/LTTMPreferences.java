package com.ycorner.linktothemap.Data;

import android.content.Context;
import android.content.SharedPreferences;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMPreferences] CLASS
 *  DESCRIPTION: LTTMPreferences is a class that contains functionality that pertains to the use and
 *  manipulation of shared preferences data.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMPreferences {

    /** SHARED PREFERENCES FUNCTIONALITY _______________________________________________________ **/

    // initializePreferences(): Initializes and returns the SharedPreferences object.
    public static SharedPreferences initializePreferences(String prefType, Context context) {
        return context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
    }

    /** GET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // getCurrentSong(): Retrieves the "lttp_current_song" value from preferences.
    public static String getCurrentSong(SharedPreferences preferences) {
        return preferences.getString("lttp_current_song", "STOPPED"); // Retrieves the current song name from preferences.
    }

    // getCurrentWorld(): Retrieves the "lttp_current_world" value from preferences.
    public static int getCurrentWorld(SharedPreferences preferences) {
        return preferences.getInt("lttp_current_world", 0); // Retrieves the current world value from preferences.
    }

    // getGraphicsMode(): Retrieves the "lttp_graphics_mode" value from preferences.
    public static int getGraphicsMode(SharedPreferences preferences) {
        return preferences.getInt("lttp_graphics_mode", 2); // Retrieves the graphics mode setting.
    }

    // getLabelOn(): Retrieves the "lttp_overlay_on" value from preferences.
    public static Boolean getLabelOn(SharedPreferences preferences) {
        return preferences.getBoolean("lttp_overlay_on", false); // Retrieves the label mode setting.
    }

    // getMapReload(): Retrieves the "lttp_map_reload" value from preferences.
    public static Boolean getMapReload(SharedPreferences preferences) {
        return preferences.getBoolean("lttp_map_reload", true); // Retrieves the map reload setting.
    }

    // getMatrixRecalculate(): Retrieves the "lttp_matrix_recalculate" value from preferences.
    public static Boolean getMatrixRecalculate(SharedPreferences preferences) {
        return preferences.getBoolean("lttp_matrix_recalculate", true); // Retrieves the matrix recalculate setting.
    }

    // getMusicOn(): Retrieves the "lttp_music_on" value from preferences.
    public static Boolean getMusicOn(SharedPreferences preferences) {
        return preferences.getBoolean("lttp_music_on", true); // Retrieves the music option setting.
    }

    // getSelectedGame(): Retrieves the "lttp_selected_game" value from preferences.
    public static String getSelectedGame(SharedPreferences preferences) {
        return preferences.getString("lttp_selected_game", "lttp1_nes"); // Retrieves the selected game value.
    }

    // getSoundOn(): Retrieves the "lttp_sound_on" value from preferences.
    public static Boolean getSoundOn(SharedPreferences preferences) {
        return preferences.getBoolean("lttp_sound_on", true); // Retrieves the sound option setting.
    }

    /** SET PREFERENCES FUNCTIONALITY __________________________________________________________ **/

    // setCurrentSong(): Sets the name of the currently playing song to preferences.
    public static void setCurrentSong(String songName, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("lttp_current_song", songName); // Sets the name of the currently playing song to preferences.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setCurrentWorld(): Sets the "lttp_current_world" value to preferences.
    public static void setCurrentWorld(int value, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putInt("lttp_current_world", value); // Sets the current world value to preferences.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setErrorCode(): Sets the "lttp_error" value to preferences.
    public static void setErrorCode(String code, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("lttp_error", code); // Sets the error code into preferences.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setErrorMode(): Sets the "lttp_error" value to preferences.
    public static void setErrorMode(boolean mode, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("lttp_error_mode", mode); // Sets the error mode into preferences.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setMapReload(): Sets the "lttp_map_reload" value to preferences.
    public static void setMapReload(boolean isReload, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("lttp_map_reload", isReload); // Sets the map reload setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setMatrixRecalculate(): Sets the "lttp_matrix_recalculate" value to preferences.
    public static void setMatrixRecalculate(boolean isRecalculate, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("lttp_matrix_recalculate", isRecalculate); // Sets the matrix recalculation setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setReturnCall(): Sets the "lttp_return_call" value to preferences.
    public static void setReturnCall(boolean isReturn, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("lttp_return_call", isReturn); // Sets the return call setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setSelectedGame(): Sets the "lttp_selected_game" value to preferences.
    public static void setSelectedGame(String gameName, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putString("lttp_selected_game", gameName); // Sets the name of the selected game for use in DQWorldView activities.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setSongPosition(): Sets the "lttp_song_position" value to preferences.
    public static void setSongPosition(int position, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putInt("lttp_song_position", position); // Sets the current song position.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }

    // setWorldView(): Sets the "lttp_worldview_on" value to preferences.
    public static void setWorldView(boolean worldViewOn, SharedPreferences preferences) {

        // Prepares the SharedPreferences object for editing.
        SharedPreferences.Editor prefEdit = preferences.edit();

        prefEdit.putBoolean("lttp_worldview_on", worldViewOn); // Sets the world view setting.
        prefEdit.apply(); // Applies the changes to SharedPreferences.
    }
}
