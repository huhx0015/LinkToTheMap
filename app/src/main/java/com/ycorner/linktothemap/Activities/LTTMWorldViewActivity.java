package com.ycorner.linktothemap.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import com.ycorner.linktothemap.Audio.LTTMSounds;
import com.ycorner.linktothemap.Data.LTTMError;
import com.ycorner.linktothemap.Data.LTTMMemory;
import com.ycorner.linktothemap.Data.LTTMPreferences;
import com.ycorner.linktothemap.Graphics.LTTMDisplay;
import com.ycorner.linktothemap.Graphics.LTTMOpenGL;
import com.ycorner.linktothemap.Graphics.LTTMSprites;
import com.ycorner.linktothemap.R;
import com.ycorner.linktothemap.UI.LTTMFont;
import com.ycorner.linktothemap.Graphics.LTTMImages;
import com.ycorner.linktothemap.Data.LTTMMapData;
import com.ycorner.linktothemap.UI.LTTMMenus;
import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Callback;
import it.sephiroth.android.library.picasso.Picasso;
import it.sephiroth.android.library.picasso.Target;
import java.lang.String;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMWorldViewActivity] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMWorldViewActivity class is an activity class that is used to display maps from
 *  The Legend of Zelda: Link to the Past. It is capable of switching maps "on-the-fly" whenever a
 *  user makes a map selection with the spinner at the top of the screen. Users can also use
 *  touch-based gestures (i.e. pinch in, pinch out, panning) to manipulate the loaded map.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMWorldViewActivity extends Activity implements View.OnTouchListener,
        AdapterView.OnItemSelectedListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // AUDIO VARIABLES
    private String currentSong; // Stores the name of the song associated the current map.

    // CLASS VARIABLES
    private LTTMMenus lttm_menus; // LTTMMenus class object that is used for menu-related functionality.

    // GESTURE VARIABLES
    private GestureDetector mapGestures; // Used for detecting gestures such as double-tap & long-press events.
    private float oldDist = 1f; // Stores the previous distance between fingers during an onTouch event.
    private final float[] matrixValues = new float[9]; // Used for setting panning limits when the map is dragged.
    private int mode = NONE; // Used for determining the current onTouch motion.
    private static final int NONE = 0; // NONE onTouch mode.
    private static final int DRAG = 1; // DRAG onTouch mode for dragging gestures.
    private static final int ZOOM = 2; // ZOOM onTouch mode for pinch gestures.
    private final PointF start = new PointF(); // Used for calculating DRAG and ZOOM onTouch events.
    private final PointF mid = new PointF(); // Used for calculating DRAG and ZOOM onTouch events.

    // LAYOUT VARIABLES
    private int loadingName; // Stores the reference ID for the loading, menu, search, and treasure box images.

    // MAP VARIABLES
    private boolean initialMapLoad = true; // Used for determining if the map being loaded is the first map.
    private boolean noTouch = false; // Used to determine if the user can interact with the map or buttons.
    private float mapWidth, mapHeight; // Used for storing the map's width and height values.
    private float maxScale, minScale; // Used for determining the maximum and minimum zoom values.
    private final Matrix mapMatrix = new Matrix(); // Matrix object for the map image.
    private final Matrix viewingMatrix = new Matrix(); // Matrix object for viewing display.
    private RectF viewScreen; // Viewing screen dimensions, should match the screen display.

    // PREFERENCES VARIABLES
    private SharedPreferences LTTM_prefs, LTTM_temps; // SharedPreferences objects that store settings for the application.
    private boolean isEnding = false; // Used to prevent the user from stopping the song by pressing the back button rapidly.
    private boolean isLoading = false; // Used to indicate that another activity is currently loading.
    private boolean map_reload = false; // Used for indicating if a map needs to be reloaded after leaving LTTMOptions.
    private boolean matrix_recalculate = false; // Used for indicating if a map's matrix needs to be reloaded after leaving LTTMOptions.
    private boolean pauseOn = false; // Used for indicating if the current activity has been paused or not.
    private boolean returnToTitle = false; // Used to indicate that screen focus is returning to the title screen.
    private int currentOrientation = 0; // Used to determine the device's orientation. (0: PORTRAIT / 1: LANDSCAPE)
    private int spinner_choice; // Used for saving the position of the spinner.
    private static final String LTTM_OPTIONS = "lttp_options"; // String name for the shared preferences object.
    private static final String LTTM_TEMPS = "lttp_temps"; // String name for the temporary preferences object.

    // SYSTEM VARIABLES
    private final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.
    private final int RESULT_CLOSE_ALL = 1; // Used for startActivityForResult activity termination.
    private int displaySize; // Stores the device's display size.
    private Point displayDimen = new Point(); // Used for determining the device's display resolution.

    // VIEW INJECTION VARIABLES
    @BindView(android.R.id.content) View worldView;
    @BindView(R.id.lttp_map_bar_bottom) FrameLayout bottomDisplay;
    @BindView(R.id.lttp_loading_display) FrameLayout loadingDisplay;
    @BindView(R.id.lttp_map_search_spinner_container) LinearLayout spinnerLayout;
    @BindView(R.id.lttp_map_icon) ImageButton mapButton;
    @BindView(R.id.lttp_map_search_icon) ImageButton globeButton;
    @BindView(R.id.lttp_loading_background) ImageView loadingBackground;
    @BindView(R.id.world_map_view) ImageView mapView;
    @BindView(R.id.world_map_overlay) ImageView mapOverlay;
    @BindView(R.id.world_map_underlay) ImageView mapUnderlay;
    @BindView(R.id.map_location_underlay) ImageView locationUnderlay;
    @BindView(R.id.lttp_map_bar_2_layer) LinearLayout topDisplayRow_2;
    @BindView(R.id.lttp_map_spinner) Spinner mapSpinner;
    @BindView(R.id.lttp_map_bar_top) TableLayout topDisplay;
    @BindView(R.id.lttp_map_name) TextView mapTitle;
    @BindView(R.id.lttm_map_1f_button) ImageButton map1FButton;
    @BindView(R.id.lttm_map_2f_button) ImageButton map2FButton;
    @BindView(R.id.lttm_map_b1_button) ImageButton mapB1Button;
    @BindView(R.id.lttm_map_bar_left) LinearLayout floorMapButtonLayout;

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ACTIVITY INITIALIZATION:
        super.onCreate(savedInstanceState);

        // CLASS INITIALIZATION: Initializes the class objects.
        lttm_menus = new LTTMMenus(); // Initializes the LTTMMenus class.

        // LAYOUT INITIALIZATION:
        updateDisplayLayout(); // Retrieves the screen attributes for the display's device.
        setContentView(R.layout.lttm_world_view); // Sets the XML layout file.
        ButterKnife.bind(this); // ButterKnife view injection initialization.

        // IMAGEVIEW INITIALIZATION:
        LTTMMapData.getInstance().initializeLTTM(); // Initializes the LTTMMapData class variable.
        clearMap(); // Assigns all map-related ImageViews to blank map images.

        // PREFERENCES INITIALIZATION: Retrieves the values from the shared preferences object.
        loadPreferences();

        // LAYOUT INITIALIZATION: Sets up the loading screen, buttons, and spinner objects.
        setUpButtons(); // Sets up the listeners and resources for the buttons.
        setUpLayout(LTTMMapData.getInstance().getGameName()); // Sets up the loading screen and top bar resources.
        setUpSpinner(map_reload); // Sets up the map spinner list.

        // GESTURE INITIALIZATION: Sets up onTouch and GestureListeners for screen touch events.
        setGestureListener(); // Sets up a GestureListener to detect alternative gestures like double-tap.
        mapView.setOnTouchListener(this); // Sets up an onTouch listener to detect simple gestures like pinch and drag.
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    protected void onResume() {

        super.onResume();

        LTTMSounds.disablePhysSounds(true, this); // Temporarily disables the physical button's sound effects.

        // Retrieves the current preferences from the shared preferences object.
        LTTM_prefs = LTTMPreferences.initializePreferences(LTTM_OPTIONS, this); // Shared preferences object.
        LTTM_temps = LTTMPreferences.initializePreferences(LTTM_TEMPS, this); // Temporary preferences object.

        // If the activity is resumed from an onPause() event, this sets the mapSpinner to the map
        // that was previously set before entering into onPause(). This is done to prevent the
        // mapSpinner from loading the first map on the mapList.
        if (pauseOn) {

            spinner_choice = LTTMPreferences.getCurrentWorld(LTTM_temps); // Retrieves the saved spinner position.
            mapSpinner.setSelection(spinner_choice); // Sets the mapSpinner position to it's previous position.

            // Resumes song where it left off before entering into onPause().
            LTTMSounds.getInstance().playMapSong(currentSong, this); // Plays the menu song for WorldView.
            pauseOn = false; // Indicates that the activity is no longer in resume mode.
        }
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    protected void onPause() {

        super.onPause();

        LTTMSounds.disablePhysSounds(false, this); // Re-enables the physical button's sound effects.

        // The activity is finished if activity focus has shifted to LTTMTitleScreen activity.
        if (returnToTitle) { finish(); }

        // All actions related to the activity are suspended when entering into the onPause state.
        else {

            // Saves the current properties of the WorldView class to preferences before pausing the activity.
            LTTMPreferences.setCurrentWorld(spinner_choice, LTTM_temps); // Current spinner selection.
            LTTMPreferences.setCurrentSong(currentSong, LTTM_temps); // Saves the name of the current song for use in LTTMOptions.
            LTTMPreferences.setSongPosition(LTTMSounds.getInstance().getSongPosition(), LTTM_temps);

            LTTMSounds.getInstance().pauseSong(); // Pauses the song.

            // Refreshes the SoundPool object for Android 2.3 (GINGERBREAD) devices.
            LTTMSounds.getInstance().reinitializeSoundPool(this);

            pauseOn = true; // Used to indicate that the current activity has entered into an onPause() state.
        }
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    // Calls recycleMemory() to free up memory allocation.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Recycles all View objects to free up memory resources.
        LTTMMemory.recycleMemory(findViewById(R.id.world_view_layout), this);
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // onActivityResult(): This function is used for assisting in total application termination.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode) {
            case RESULT_CLOSE_ALL:
                setResult(RESULT_CLOSE_ALL);
                finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // onConfigurationChanged(): If the screen orientation changes, this function loads the proper
    // layout, as well as updating all layout-related objects.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateDisplayLayout(); // Updates the screen resolution attributes.
        spinnerOrientation(); // Updates the mapSpinner's layout parameters based on current device orientation.
        loadMapMatrix(); // Updates the map matrix with the new screen resolution values.
    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {

        LTTMSounds.getInstance().playSoundEffect(1, this); // Plays the "lttm_menu" sound effect.

        // Loads the parent map if one exists. Otherwise, the activity is concluded.
        returnToWorld(LTTMMapData.getInstance().getParentMap());
    }

    // onKeyDown(): When a user presses the menu key, it displays the menu dialog.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        // SEARCH KEY: The map spinner list is displayed when the search key is pressed.
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {

            LTTMSounds.getInstance().playSoundEffect(1, this); // Plays the "Menu" sound effect.
            mapSpinner.performClick(); // Activates the map spinner list.
            return true; // Returns true to prevent further propagation of the key event.
        }

        // Allows the system to handle all other key events.
        return super.onKeyDown(keyCode, event);
    }

    /** LAYOUT FUNCTIONALITY ___________________________________________________________________ **/

    // displayLoadingScreen(): Makes the loading screen visible and hides the zoom buttons and the
    // mapUnderlay and mapOverlay objects.
    private void displayLoadingScreen() {

        // Hides the buttons, mapUnderlay, and mapOverlay objects.
        locationUnderlay.setVisibility(View.INVISIBLE);
        mapUnderlay.setVisibility(View.INVISIBLE); // Makes the mapUnderlay object invisible.
        mapOverlay.setVisibility(View.INVISIBLE); // Makes the mapUnderlay object invisible.
        bottomDisplay.setVisibility(View.INVISIBLE); // Hides the zoom buttons.
        topDisplayRow_2.setVisibility(View.INVISIBLE); // Hides the additional buttons.

        // If the device is in landscape mode, the top display will be hidden, as it overlaps with
        // the loading screen background.
        if (currentOrientation == 1) { topDisplay.setVisibility(View.INVISIBLE); }

        // Displays the loading screen objects.
        Picasso.with(this).load(loadingName).withOptions(LTTMImages.setBitmapOptions()).into(loadingBackground);
        loadingDisplay.setVisibility(View.VISIBLE); // Makes the loading screen visible.
    }

    // hideLoadingScreen(): Hides the loading screen and makes the mapUnderlay, mapOverlay, and zoom buttons
    // visible.
    private void hideLoadingScreen() {

        // Hides the loading screen objects.
        loadingDisplay.setVisibility(View.INVISIBLE); // Hides the loading screen.
        loadingBackground.setImageDrawable(null); // Sets the loadingBackground ImageView object to be null.

        // Makes the top/bottom display objects visible.
        bottomDisplay.setVisibility(View.VISIBLE); // Makes the bottom display visible.
        topDisplay.setVisibility(View.VISIBLE); // Makes the top display visible.
        topDisplayRow_2.setVisibility(View.VISIBLE); // Hides the additional buttons.

        // Loads the map overlay image. If the map is larger than 2048 x 2048 and LOW GRAPHICS mode
        // has been enabled, the map overlay image is scaled down.
        if ( (LTTMMapData.getInstance().getGraphicsMode() == 0) && (LTTMMapData.getInstance().isLarge()) ) {
            Picasso.with(this).load(LTTMMapData.getInstance().getMapOverlayImage())
                    .withOptions(LTTMImages.setBitmapOptions()).resize( (int) mapWidth, (int) mapHeight)
                    .into(mapOverlay);
        }

        // Loads the map overlay image normally.
        else {
            Picasso.with(this).load(LTTMMapData.getInstance().getMapOverlayImage())
                    .withOptions(LTTMImages.setBitmapOptions()).into(mapOverlay);
        }

        mapOverlay.setVisibility(View.VISIBLE); // Makes the mapOverlay layer visible.

        // Makes the mapUnderlay layer visible.
        locationUnderlay.setVisibility(View.VISIBLE);
        mapUnderlay.setBackgroundResource(LTTMMapData.getInstance().getMapUnderlayImage());
        mapUnderlay.setVisibility(View.VISIBLE);
    }

    // setUpLayout(): Initializes and sets up the resources for the top menu bar and loading screen objects.
    private void setUpLayout(String game) {

        // SPRITE VARIABLES:
        LTTMSprites.getInstance().initializeLTTM(); // Initializes the LTTMSprite object.
        LTTMSprites.getInstance().loadGameSprites(game); // Loads the sprite resources based on the selected game and language.

        // RESOURCE VARIABLES:
        loadingName = LTTMSprites.getInstance().getLoadingName(); // Stores the reference ID of the loading screen.

        // If the device is in landscape mode, the top display will initially be hidden, as it
        // overlaps with the loading screen background.
        if (currentOrientation == 1) { topDisplay.setVisibility(View.INVISIBLE); }

        spinnerOrientation(); // Sets the spinner bar attributes based on current screen orientation.

        // TEXT VARIABLES:
        mapTitle.setTypeface(LTTMFont.getInstance().getTypeFace(this)); // Sets custom font properties.
        TextView nowLoading = (TextView) findViewById(R.id.lttm_loading_notice);
        nowLoading.setTypeface(LTTMFont.getInstance().getTypeFace(this)); // Sets custom font properties.

        // Converts pixels into density pixels.
        float scale = getResources().getDisplayMetrics().density;
        int dpToPixels = (int) (10 * scale + 0.5f); // Converts 10dp into pixels.

        FrameLayout lttm_map_icon_container = (FrameLayout) findViewById(R.id.lttm_map_icon_container);
        lttm_map_icon_container.setPadding(dpToPixels, dpToPixels, dpToPixels, dpToPixels); // Sets the new padding size.

        // The loading screen, search, and additional button image references are assigned to the ImageView objects.
        Picasso.with(this).load(loadingName).noFade().withOptions(LTTMImages.setBitmapOptions()).into(loadingBackground);
    }

    // spinnerOrientation(): Checks the device's current orientation and sets the appropriate
    // attributes for the spinner bar. If the current orientation is in portrait mode, the spinner
    // spans the total screen width. If the current orientation is in landscape mode, the spinner
    // only wraps the selection list.
    private void spinnerOrientation() {

        // Checks orientation to see if it is in landscape mode or portrait mode.
        if (currentOrientation == 0) {
            spinnerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        else if (currentOrientation == 1) {
            spinnerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }

    // updateDisplayLayout(): Retrieves the device's screen resolution and current orientation.
    private void updateDisplayLayout() {
        Display display = getWindowManager().getDefaultDisplay(); // Retrieves device's display attributes.
        currentOrientation = LTTMDisplay.getOrientation(this); // Retrieves the device's orientation.
        displayDimen = LTTMDisplay.getResolution(display); // Retrieves the device's resolution attributes.
        displaySize = LTTMDisplay.getDisplaySize(displayDimen, currentOrientation); // Retrieves the device's display size attribute.
        viewScreen = new RectF(0, 0, displayDimen.x, displayDimen.y); // Sets screen dimensions to device stats.
    }

    /** USER INTERFACE FUNCTIONALITY ___________________________________________________________ **/

    // setUpButtons(): Sets up listeners for the menu, search, and zoom buttons, as well as actions
    // to perform when pressed.
    private void setUpButtons() {

        // BUTTON VARIABLES
        FrameLayout mapNameContainer = (FrameLayout) findViewById(R.id.lttm_map_name_container);
        Button zoomIn = (Button) findViewById(R.id.zoomInPlus);
        Button zoomOut = (Button) findViewById(R.id.zoomOutMinus);

        // Sets up activity for the map name container. Activates the map spinner list.
        mapNameContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!noTouch) {
                    LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the "Menu" sound effect.
                    mapSpinner.performClick(); // Activates the map spinner list.
                }
            }
        });

        // Sets up activity for the map button. Activates the map spinner list.
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!noTouch) {
                    LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the "Menu" sound effect.
                    mapSpinner.performClick(); // Activates the map spinner list.
                }
            }
        });

        // Sets up activity for the map character button. Activates the map spinner list.
        globeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!noTouch) {
                    LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the "Menu" sound effect.
                    mapSpinner.performClick(); // Activates the map spinner list.
                }
            }
        });

        // Sets up activity for the zoom in (+) button.
        zoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!noTouch) {
                    LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the "Menu" sound effect.
                    zoomOnPoint(1.5f, mid); // Performs zoom in on the midpoint of the map.
                }
            }
        });

        // Sets up activity for the zoom out (-) button.
        zoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!noTouch) {
                    LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the "Menu" sound effect.
                    zoomOnPoint(0.7f, mid); // Performs zoom out from the midpoint of the map.
                }
            }
        });
    }

    // setUSpinner(): Sets up the map spinner list for the map selection menu.
    private void setUpSpinner(boolean isResume) {

        String[] mapList = lttm_menus.setMapList(this, LTTMMapData.getInstance().getGameName()); // Sets up the lists for the mapSpinner object.
        lttm_menus.createMapSpinner(this, mapSpinner, mapList); // Creates a customized spinner for mapSpinner.
        if (isResume) { mapSpinner.setSelection(spinner_choice); } // Sets the mapSpinner position to it's previous position.
        mapSpinner.setOnItemSelectedListener(this); // Sets a listener to the mapSpinner object.
        mapSpinner.setOnTouchListener(spinnerOnTouch); // Sets up an onClick-like event for the mapSpinner object.
    }

    // spinnerOnTouch(): Captures the touch events for mapSpinner and is primarily used to play the
    // 'lttm_select' sound effect when the spinner is pressed. This is a workaround for onClick events
    // for spinners, as spinner objects do not support onClick events natively.
    private final View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            // When the spinner is pressed, it plays a sound effect.
            if (event.getAction() == MotionEvent.ACTION_UP) {
                LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the 'Menu' sound effect.
            }

            return false;
        }
    };

    // onItemSelected(): Override function for setOnTouchListener for the mapSpinner object. When
    // a map is selected from the spinner drop down list, the selected map is loaded. Current
    // application settings are also checked here, depending on the state of the application and enabled
    // map settings.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Checks to see if the WorldView activity has resumed after being in onPause(), as well as
        // if the user entered into LTTMOptions activity and toggled a setting that requires a map
        // reload.
        if (!pauseOn || map_reload) {

            try {

                // Checks to see if the spinner selection is null or not. Needed to resolve an
                // occasional null pointer bug when resuming from an onPause() state.
                if (mapSpinner.getSelectedItem() == null) { mapSpinner.setSelection(spinner_choice); }
                loadMap(mapSpinner.getSelectedItem().toString()); // Loads the selected map.
            }

            catch (NullPointerException e) {
                e.printStackTrace(); // Prints error message.
                String errorString = "LTTMWORLDVIEW ACTIVITY\nNULL POINTER EXCEPTION OCCURRED WHILE ATTEMPTING TO RETRIEVE THE MAP SELECTION FROM THE SPINNER!";
                prepareError(errorString); // Prepares the error handling.
            }
        }

        spinner_choice = mapSpinner.getSelectedItemPosition(); // Saves the spinner position for onResume events.
    }

    // onNothingSelected(): Override function for setOnTouchListener for the mapSpinner object.
    // Nothing occurs when no map is selected from the mapSpinner object.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /** GESTURE FUNCTIONALITY __________________________________________________________________ **/

    // draggingAction(): When a user pans/drags the map across the screen, draggingAction() is called,
    // which updates the viewing matrix values. This function also limits the amount of dragging/panning
    // that can be applied to the currently displayed map.
    private void draggingAction(MotionEvent rawEvent) {

        mapMatrix.set(viewingMatrix); // Sets the viewingMatrix matrix.
        mapMatrix.getValues(matrixValues); // Retrieves the current values from the mapMatrix matrix.

        // Values that are used to determine the current attributes of the image and matrix objects.
        float currentX = matrixValues[Matrix.MTRANS_X];
        float currentY = matrixValues[Matrix.MTRANS_Y];
        float currentScale = matrixValues[Matrix.MSCALE_X];
        float currentWidth = mapWidth * currentScale;
        float currentHeight = mapHeight * currentScale;
        float deltaX = rawEvent.getX() - start.x;
        float deltaY = rawEvent.getY() - start.y;
        float newX = currentX + deltaX;
        float newY = currentY + deltaY;
        float viewLimitOffset = 100f; // Used to correct the panning limit issue.

        // Values that are used to determine the dimensional limits in which the user may pan/drag
        // the map.
        RectF viewingLimit = new RectF(newX, newY, newX + currentWidth, newY + currentHeight);
        float upDiff = (Math.min(viewScreen.bottom - viewingLimit.bottom, viewScreen.top - viewingLimit.top)) - viewLimitOffset;
        float downDiff = (Math.max(viewScreen.bottom - viewingLimit.bottom, viewScreen.top - viewingLimit.top)) + viewLimitOffset;
        float leftDiff = (Math.min(viewScreen.left - viewingLimit.left, viewScreen.right - viewingLimit.right)) - viewLimitOffset;
        float rightDiff = (Math.max(viewScreen.left - viewingLimit.left, viewScreen.right - viewingLimit.right)) + viewLimitOffset;

        // Limits the amount of panning/dragging a user can move the map.
        if (upDiff > 0) { deltaY += upDiff; }
        if (downDiff < 0) { deltaY += downDiff; }
        if (leftDiff > 0) { deltaX += leftDiff; }
        if (rightDiff < 0) { deltaX += rightDiff; }

        mapMatrix.postTranslate(deltaX, deltaY);
    }

    // midPoint(): Calculates the midpoint between the two fingers for touch events.
    private void midPoint(PointF point, MotionEvent event) {

        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    // onTouch(): This function listens for any gestures (such as pinch and drag) and updates the map
    // accordingly, depending on the gesture event. This function is a modified code version of
    // ZDNet's Ed Burnette's "How to Use Multi-Touch" tutorial:
    // (http://www.zdnet.com/blog/burnette/how-to-use-multi-touch-in-android-2/1747)
    @Override
    public boolean onTouch(View v, MotionEvent rawEvent) {

        // Listens for gestures from the user, as long as the user is not being blocked from moving the map.
        if (!noTouch) {

            mapGestures.onTouchEvent(rawEvent); // Listens for alternative gestures such as double-tap.

            // Handle touch events here...
            switch (rawEvent.getAction() & MotionEvent.ACTION_MASK) {

                // 'DRAG': Sets the mode to 'DRAG' mode  and prepares the matrix.
                case MotionEvent.ACTION_DOWN:

                    viewingMatrix.set(mapMatrix);
                    start.set(rawEvent.getX(), rawEvent.getY());
                    mode = DRAG; // Sets the current touch mode to 'DRAG'.
                    break;

                // 'PINCH': Sets the mode to 'PINCH' mode' and prepares the matrix.
                case MotionEvent.ACTION_POINTER_DOWN:

                    oldDist = spacing(rawEvent);

                    if (oldDist > 10f) {
                        viewingMatrix.set(mapMatrix);
                        midPoint(mid, rawEvent); // Sets the midpoint of the screen to the location of the pinch action.
                        mode = ZOOM; // Sets the current touch mode to 'ZOOM'.
                    }

                    break;

                // MotionEvent is ignored, nothing happens here.
                case MotionEvent.ACTION_UP:

                    // MotionEvent is ignored, nothing happens here.
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;

                // If movement motion is detected, this case checks whether it should implement drag or
                // zoom behavior for the map image matrix.
                case MotionEvent.ACTION_MOVE:

                    // 'DRAG': Updates the map matrix when the user 'drags' the screen.
                    if (mode == DRAG) {
                        draggingAction(rawEvent); // Applies the dragging/panning motion.
                    }

                    // 'ZOOM': Updates the map matrix when the user 'pinches' the screen.
                    else if (mode == ZOOM) {

                        float newDist = spacing(rawEvent);

                        if (newDist > 10f) {

                            mapMatrix.set(viewingMatrix); // Sets the mapMatrix to match that of the viewingMatrix.
                            float scale = newDist / oldDist; // Calculates the new scaled value.

                            mapMatrix.getValues(matrixValues); // Retrieves the matrix values from mapMatrix.
                            float currentScale = matrixValues[Matrix.MSCALE_X]; // Sets the current scale.


                            // Limits how much the user can zoom in on the currently displayed map.
                            if (scale * currentScale > maxScale) { scale = maxScale / currentScale; }
                            else if (scale * currentScale < minScale) { scale = minScale / currentScale; }

                            mapMatrix.postScale(scale, scale, mid.x, mid.y); // Performs the post scale matrix operation.
                        }
                    }

                    break;
            }

            // Sets the final matrix operations for mapView and mapOverlay ImageView objects.
            mapView.setImageMatrix(mapMatrix);
            mapOverlay.setImageMatrix(mapMatrix);
        }

        return true;
    }

    // setGestureListener(): Sets up a GestureDetector for the WorldView class. This allows for the
    // detection of gestures (such as double-tap and long-press) and actions to take when such
    // gestures are detected.
    private void setGestureListener() {

        // Initializes the mapGestures GestureDetector object.
        mapGestures = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {

                    // When a double tap gesture is detected, the map zooms in on the point of the tap.
                    @Override
                    public boolean onDoubleTap(MotionEvent rawEvent) {

                        LTTMSounds.getInstance().playSoundEffect(1, LTTMWorldViewActivity.this); // Plays the "Menu" sound effect.

                        // Retrieves the x and y coordinates where double tap was detected.
                        float x = rawEvent.getX();
                        float y = rawEvent.getY();
                        PointF doubleTapPoint = new PointF(x, y);

                        // Performs the zoom on the double tap point coordinates.
                        zoomOnPoint(3.0f, doubleTapPoint);

                        return true;
                    }
                });
    }

    // spacing(): Determines the space distance between the two fingers for touch events.
    private float spacing(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    // zoomOnPoint(): Updates the matrix for zooming in/out on the specified point on the map.
    private void zoomOnPoint(float newScale, PointF point) {

        mapMatrix.getValues(matrixValues);
        float currentScale = matrixValues[Matrix.MSCALE_X];

        // Sets the limit range for zooming in and out.
        if (newScale * currentScale > maxScale) {
            newScale = maxScale / currentScale;
        }

        else if (newScale * currentScale < minScale) {
            newScale = minScale / currentScale;
        }

        // Performs the matrix operations and applies the new matrix.
        mapMatrix.postScale(newScale, newScale, point.x, point.y);
        mapView.setImageMatrix(mapMatrix);
        mapOverlay.setImageMatrix(mapMatrix);
    }

    /** IMAGE FUNCTIONALITY ____________________________________________________________________ **/

    // clearMap(): Clears the map and sets it to null. This function is primarily used to free up
    // memory resources being taken by mapView, mapOverlay, and mapUnderlay.
    @SuppressLint("NewApi")
    private void clearMap() {

        mapView.setImageDrawable(null); // Sets the ImageView Drawable to be null.
        mapOverlay.setImageDrawable(null); // Sets the ImageView Drawable to be null.
        mapUnderlay.setImageDrawable(null); // Sets the ImageView Drawable to be null.

        // Sets the ImageView Drawable to be null.
        if (api_level >= 16) { mapUnderlay.setBackground(null); } // API 16+: New method.
        else { mapUnderlay.setBackgroundDrawable(null); } // API 9-15: Depreciated method.
    }

    // loadImage(): This function loads the map image into the main ImageView object.
    private void loadImage(String mapName, Callback callback) {

        // Retrieves the map width and height.
        Point mapDimen = LTTMImages.getImageDimensions(getResources(), LTTMMapData.getInstance().getMapViewImage());
        mapWidth = mapDimen.x; // Map image's width property.
        mapHeight = mapDimen.y; // Map image's height property.

        // If "MEDIUM" graphics mode has been enabled and the map is larger than 2048 x 2048,
        // hardware acceleration is disabled for the map.
        if ( (LTTMMapData.getInstance().getGraphicsMode() == 1) && (LTTMMapData.getInstance().isLarge())) {

            // Disables hardware acceleration on the View container.
            if (api_level >= 11) { LTTMOpenGL.enableHardwareAcceleration(worldView, false); }
        }

        // Otherwise, hardware acceleration is enabled and the map is loaded normally.
        else {

            // Enables hardware acceleration on the View container.
            if (api_level >= 11) { LTTMOpenGL.enableHardwareAcceleration(worldView, true); }
        }

        Picasso.with(this)
                .load(LTTMMapData.getInstance().getMapViewImage())
                .placeholder(R.drawable.lttm_transparent_tile)
                .noFade()
                .withOptions(LTTMImages.setBitmapOptions())
                .into(mapView, callback);

        Target target = new Target() {

            // onBitmapLoaded(): Runs when the bitmap is loaded.
            @SuppressLint("NewApi")
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                locationUnderlay.setImageBitmap(bitmap);
            }

            // onBitmapFailed(): Runs when the bitmap failed to load.
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            // onPrepareLoad(): Runs prior to loading the bitmap.
            @SuppressLint("NewApi")
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };

        switch (mapName) {
            case "Light World":
                Picasso.with(this)
                        .load(R.drawable.loz_alttp_light_world_underlay)
                        .into(target);
                floorMapButtonLayout.setVisibility(View.GONE);
                break;

            case "Dark World":
                Picasso.with(this)
                        .load(R.drawable.loz_alttp_dark_world_underlay)
                        .into(target);
                floorMapButtonLayout.setVisibility(View.GONE);
                break;

            default:
                locationUnderlay.setImageDrawable(null);
                floorMapButtonLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    // loadMap(): This function is responsible for loading the map image, as well as performing
    // operations to take while the map is loading. It loads the map in the background using the
    // Picasso image library. While the map is loading, a loading screen is displayed. When the map has
    // been fully loaded, the loading screen will be rendered invisible and the map image will be shown.
    public void loadMap(final String mapName) {

        // Prepares the loading screen prior to loading the new map.
        clearMap(); // Sets the map resources to blank images to free up memory resources.
        displayLoadingScreen(); // Displays the loading screen objects.
        mapTitle.setText(mapName); // Sets the map name for the top bar.
        LTTMMapData.getInstance().mapSelector(mapName); // Retrieves the selected map from mapSelector in LTTMMapData.

        // Plays the appropriate song with the map.
        currentSong = LTTMMapData.getInstance().getMapSong(); // Retrieves the song associated with the map.
        LTTMSounds.getInstance().playMapSong(currentSong, this); // Plays the menu song for WorldView activity.

        // Plays the 'Stairs' sound effect on map change.
        if (!initialMapLoad && !map_reload) { LTTMSounds.getInstance().playSoundEffect(5, this); }

        // Callback object that is utilized with Picasso for loading the mapView image.
        Callback callback = new Callback() {

            // onSuccess(): This function executes if the image has loaded successfully.
            @Override
            public void onSuccess() { postLoadMap(); } // Loads the post map load functionality.

            // onError(): This function executes if the image has failed to load.
            @Override
            public void onError() {
                String errorString = "LTTMWORLDVIEW ACTIVITY\nMAP COULD NOT BE LOADED!";
                prepareError(errorString); // Prepares the error handling.
            }
        };

        loadImage(mapName, callback); // Loads the map image directly into the ImageView.
    }

    // loadMapMatrix(): Prepares the viewing matrix attributes for the map currently loaded.
    // It centers and scales the map accordingly, as well as updating the minScale and maxScale values
    // for the zoomIn and zoomOut buttons.
    private void loadMapMatrix() {

        int widthOffset = (int) (mapWidth * 0.10); // 10% of the map's width value for the offset correction.
        int heightOffset = (int) (mapHeight * 0.10); // 10% of the map's height value for the offset correction.
        float scaleX = displayDimen.x / (mapWidth + widthOffset);
        float scaleY = displayDimen.y / (mapHeight + heightOffset);

        // Updates the new minimum & maximum zoom scale factors for the current map.
        minScale = Math.min(scaleX, scaleY);
        maxScale = 4f;

        // Used to determine the center coordinates for the matrix.
        float centerX = (Math.max(0, minScale * mapWidth / 2f + 0.5f *
                (displayDimen.x - (minScale * mapWidth))));
        float centerY = (Math.max(0,  minScale * mapHeight / 2f + 0.5f *
                (displayDimen.y - (minScale * mapHeight))));

        // Sets the initial midpoint coordinates. Required to fix the zoom in/out bug.
        mid.x = centerX;
        mid.y = centerY;

        // The current map image is centered and scaled accordingly.
        viewingMatrix.reset(); // Resets the viewing matrix.
        viewingMatrix.postTranslate(-mapWidth / 2f, -mapHeight / 2f);
        viewingMatrix.postScale(minScale, minScale);
        viewingMatrix.postTranslate(centerX, centerY);

        // Sets the matrix of the current map to match that of the properly scaled and centered matrix.
        mapView.setImageMatrix(viewingMatrix);
        mapOverlay.setImageMatrix(viewingMatrix);
        mapMatrix.set(viewingMatrix);
    }

    // postLoadMap(): This function runs after the map image has successfully loaded. It retrieves
    // the width, height, and matrix of the new map, as well as hiding the load screen.
    private void postLoadMap() {

        // If the same map is being reloaded with different properties (language or overlay change)
        // and not a resolution change, matrix calculation is skipped.
        if (matrix_recalculate || !map_reload ) { loadMapMatrix(); }

        hideLoadingScreen(); // Displays mapOverlay and mapUnderlay objects and hides loading display objects.

        // Resets temporary preference values for initial map loading or map reload events.
        initialMapLoad = false;
        map_reload = false;
        matrix_recalculate = false;
        LTTMPreferences.setCurrentSong(currentSong, LTTM_temps); // Saves the name of the current song.
        LTTMPreferences.setMapReload(false, LTTM_temps); // Resets the map reload setting.
        LTTMPreferences.setMatrixRecalculate(false, LTTM_temps); // Resets the matrix recalculate setting.
    }

    /** PREFERENCES FUNCTIONALITY ______________________________________________________________ **/

    // loadPreferences(): Loads the values from the SharedPreferences objects.
    private void loadPreferences() {

        // PREFERENCES:
        LTTM_prefs = LTTMPreferences.initializePreferences(LTTM_OPTIONS, this); // Shared preferences object.

        // TEMPORARY PREFERENCES:
        LTTM_temps = LTTMPreferences.initializePreferences(LTTM_TEMPS, this); // Temporary preferences object.
        currentSong = LTTMPreferences.getCurrentSong(LTTM_temps); // Retrieves the string name of the current song playing.
        map_reload = LTTMPreferences.getMapReload(LTTM_temps); // MAP RELOAD setting.
        matrix_recalculate = LTTMPreferences.getMatrixRecalculate(LTTM_temps); // MATRIX RECALCULATE setting.
        spinner_choice = LTTMPreferences.getCurrentWorld(LTTM_temps);  // SPINNER POSITION setting.

        // Assigns the shared preference values to the LTTMMapData & LTTMSounds class variables.
        LTTMMapData.getInstance().setGameName(LTTMPreferences.getSelectedGame(LTTM_temps));
        LTTMMapData.getInstance().setGraphicsMode(LTTMPreferences.getGraphicsMode(LTTM_prefs));
        LTTMMapData.getInstance().setLabelsOn(LTTMPreferences.getLabelOn(LTTM_prefs));
        LTTMSounds.getInstance().setMusicOn(LTTMPreferences.getMusicOn(LTTM_prefs));
        LTTMSounds.getInstance().setSoundOn(LTTMPreferences.getSoundOn(LTTM_prefs));
        LTTMSounds.getInstance().setSongPosition(0); // Required to avoid audio resume playback bug.
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/

    // launchTitleScreen(): This function is used to launch the LTTMTitleScreen activity.
    private void launchTitle() {

        // If another activity is currently loading, another activity cannot be launched. This is to
        // prevent multiple activities from launching.
        if (!isLoading) {

            LTTMSounds.getInstance().playSoundEffect(3, this);

            // Sets the boolean value to specify that the LTTMWorldView activity is no longer in focus.
            LTTMPreferences.setWorldView(false, LTTM_temps);

            // Used to indicate that the LTTMWorldView activity will be finished when this activity is suspended.
            returnToTitle = true;

            // Sets up the intent to launch the LTTMTitleScreen activity.
            isLoading = true; // Indicates that another activity is currently loading.
            Intent i = new Intent(this, LTTMTitleScreenActivity.class);
            startActivity(i); // Begins the next activity.
        }
    }

    // returnToWorld(): Loads the parent map. If the parent map is "ROOT", the activity is concluded.
    private void returnToWorld(String parent) {

        // If the parent map is not "ROOT", the specified parent map is loaded.
        if ( !(parent.equals("ROOT")) ) { mapSpinner.setSelection(LTTMMapData.getInstance().getParentID()); }

        // Otherwise, the activity is concluded and focus returns to the MainActivity activity.
        else {

            // Checks to see if the user has already pressed the back button to prevent song playback
            // from stopping in MainActivity.
            if (!isEnding) {
                LTTMSounds.getInstance().stopSong(); // Terminate any playing songs.
                isEnding = true; // Sets the value to indicate that the activity is already ending.
            }

            launchTitle(); // Launches the LTTMTitleScreen activity.
        }
    }

    /** ERROR FUNCTIONALITY ___________________________________________________________________  **/

    // prepareError(): Used for preparing the application for error handling.
    private void prepareError(String error) {

        // Checks to see if the LTTMSounds audio object is null or not. This is to fix an null
        // pointer bug where the LTTMSounds audio object is recycled during a lengthy onPause() state
        // and needs to be re-initialized.
        if (LTTMSounds.getInstance() == null) {

            try {
                LTTMSounds.getInstance().initializeLTTM(); // Initializes the LTTMSounds class object.
                LTTMSounds.getInstance().loadLTTMsounds(this); // Loads the soundPool with a hash map of sound effects.
            }

            catch (NullPointerException e) { e.printStackTrace(); } // Null pointer exception handling.
        }

        LTTMSounds.getInstance().stopSong(); // Terminate any playing songs.
        LTTMSounds.getInstance().playSoundEffect(2, this); // Plays the sound effect.

        LTTMError.handleError(error, this); // Initiates error handling
    }
}