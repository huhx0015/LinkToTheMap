package com.ycorner.linktothemap.Graphics;

import com.ycorner.linktothemap.R;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMSprites] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMSprites class is used for loading the game sprites for display in various
 *  activities and fragments for the application.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMSprites {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LTTMWorldView Activity Sprites:
    public int globeIcon; // Stores the reference ID of globe icon.
    public int loadingName; // Stores the reference ID of the loading screen.
    public int treasureName; // Stores the reference ID of the treasure icon (closed).
    public int treasureNameOpen; // Stores the reference ID of the treasure icon (opened).
    public int mapName; // Stores the reference ID of the map button icon.
    public int mapNameHD; // Stores the reference ID of the 1080p version of the map button icon.
    public int travelName; // Stores the reference ID of the travel door icon.
    public int travelNameHD; // Stores the reference ID of the 1080p version of the travel door icon.
    public int treasureNameHD; // Stores the reference ID of the 1080p version of the treasure icon (closed).
    public int treasureNameOpenHD; // Stores the reference ID of the 1080p version of the treasure icon (opened).

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // LTTMSprites(): Constructor for the LTTMSprites class.
    private final static LTTMSprites LTTM_SPRITES = new LTTMSprites();

    // LTTMSprites(): Deconstructor for the LTTMSprites class.
    private LTTMSprites() {}

    // getInstance(): Returns the LTTM_SPRITES instance.
    public static LTTMSprites getInstance() {
        return LTTM_SPRITES;
    }

    // initializeLTTM(): Initializes the LTTMSprites class variables.
    public void initializeLTTM() {

        // Sets the default variables for the LTTMSprites class.
        mapName = R.drawable.loz_alttp_map_icon; // Stores the reference ID of the map button icon.
        mapNameHD = R.drawable.loz_alttp_map_icon; // Stores the reference ID of the 1080p version of the map button icon.
        globeIcon = R.drawable.loz_alttp_map_icon; // Stores the reference ID of the globe icon.
        travelName = R.drawable.loz_alttp_mirror_icon; // Stores the reference ID of the travel door icon.
        travelNameHD = R.drawable.loz_alttp_mirror_icon; // Stores the reference ID of the 1080p version of the travel door icon.
        treasureNameHD = R.drawable.loz_alttp_treasure_icon; // Stores the reference ID of the 1080p version of the treasure icon.
        treasureNameOpenHD = R.drawable.loz_alttp_treasure_open_icon; // Stores the reference ID of the 1080p version of the treasure icon.
        treasureName = R.drawable.loz_alttp_treasure_icon; // Stores the reference ID of the treasure open icon.
        treasureNameOpen = R.drawable.loz_alttp_treasure_open_icon; // Stores the reference ID of the treasure open icon.
    }

    /** SPRITE FUNCTIONALITY ___________________________________________________________________ **/

    // loadGameSprites(): Loads the game sprite resources based on the selected game and language.
    public void loadGameSprites(String gameName, String language) {

        // LTTMWorldView Sprites: Sets the loading screen and sprites for the LTTMWorldView activity.
        if (gameName.equals("loz_alttp")) {
            loadingName = R.drawable.zelda_background;
        }
    }
}