package com.ycorner.linktothemap.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.huhx0015.hxaudio.audio.HXSound;
import com.huhx0015.hxaudio.utils.HXAudioPlayerUtils;
import com.ycorner.linktothemap.Audio.LTTMAudio;
import com.ycorner.linktothemap.Data.LTTMMemory;
import com.ycorner.linktothemap.Data.LTTMPreferences;
import com.ycorner.linktothemap.R;
import com.ycorner.linktothemap.UI.LTTMFont;
import butterknife.BindView;
import butterknife.ButterKnife;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMTitleScreenActivity] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMTitleScreenActivity class is the launching activity and displays the main menu
 *  screen.
 *  -----------------------------------------------------------------------------------------------
 */
public class LTTMTitleScreenActivity extends Activity {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // PREFERENCES VARIABLES
    private SharedPreferences LTTM_temps; // SharedPreferences objects that store the settings for the application.
    private static final String LTTM_TEMPS = "lttp_temps";

    // SYSTEM VARIABLES
    private boolean isConfigChange = false;

    // VIEW INJECTION VARIABLES
    @BindView(R.id.title_start_button) Button startButton;

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AUDIO INITIALIZATION:
        HXSound.load(LTTMAudio.initSoundList(), this);

        // ACTIVITY INITIALIZATION: Initializes the activity and sets the XML layout.
        setupLayout();
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    protected void onResume() {
        super.onResume();
        HXAudioPlayerUtils.enableSystemSound(false, this); // Temporarily disables the physical button's sound effects.
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    protected void onPause(){
        super.onPause();
        HXAudioPlayerUtils.enableSystemSound(true, this); // Re-enables the physical button's sound effects.
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Recycles all View objects to free up memory resources.
        LTTMMemory.recycleMemory(findViewById(R.id.title_screen_layout), this);
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Clears the HXMusic and HXSound instances.
        HXMusic.clear();
        HXSound.clear();
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // setupLayout(): Sets up the layout for the activity.
    private void setupLayout() {
        setContentView(R.layout.lttm_title_screen);
        ButterKnife.bind(this);

        // Sets the custom font on the start button.
        startButton.setTypeface(LTTMFont.getInstance().getTypeFace(this));

        // Sets a listener for the start button.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HXSound.sound()
                        .load(R.raw.alttp_menu_select)
                        .play(LTTMTitleScreenActivity.this);

                prepareWorldPreferences("loz_alttp"); // Prepares the temporary preference values related to LTTMWorldView activity.

                Intent i = new Intent(LTTMTitleScreenActivity.this, LTTMWorldViewActivity.class);
                startActivity(i); // Begins the next activity.
                finish();
            }
        });
    }

    /** PREFERENCE FUNCTIONALITY _______________________________________________________________ **/

    // prepareWorldPreferences(): Prepares the temporary preference values related to LTTMWorldView activity.
    private void prepareWorldPreferences(String gameName) {

        // Sets the temporary preference values before continuing onto the LTTMWorldView activity.
        LTTM_temps = LTTMPreferences.initializePreferences(LTTM_TEMPS, this); // Temporary preferences object.
        LTTMPreferences.setDefaultPreferences(LTTM_TEMPS, true, this); // Resets the temporary preferences.
        LTTMPreferences.setSelectedGame(gameName, LTTM_temps); // Saves the name of the selected game for use in LTTMWorldView activities.
        LTTMPreferences.setWorldView(true, LTTM_temps); // Sets the boolean value to specify that the focus has shifted to LTTMWorldView.
        LTTMPreferences.setCurrentWorld(0, LTTM_temps); // Resets the lttm_current_world value to "0".
        LTTMPreferences.setMapReload(false, LTTM_temps); // Resets the lttm_map_reload value to "false".
        LTTMPreferences.setMatrixRecalculate(false, LTTM_temps); // Resets the lttm_matrix_recalculate value to "false".
        LTTMPreferences.setReturnCall(false, LTTM_temps); // Resets the lttm_return_call to be "false".
    }
}