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

    // INSTANCE VARIABLE
    private static LTTMSprites LTTM_SPRITES; // LTTMSprites instance variable.

    // SRPITE VARIABLES (LTTMWorldView):
    private int globeIcon; // Stores the reference ID of globe icon.
    private int loadingName; // Stores the reference ID of the loading screen.
    private int treasureName; // Stores the reference ID of the treasure icon (closed).
    private int treasureNameOpen; // Stores the reference ID of the treasure icon (opened).
    private int mapName; // Stores the reference ID of the map button icon.
    private int mapNameHD; // Stores the reference ID of the 1080p version of the map button icon.
    private int travelName; // Stores the reference ID of the travel door icon.
    private int travelNameHD; // Stores the reference ID of the 1080p version of the travel door icon.
    private int treasureNameHD; // Stores the reference ID of the 1080p version of the treasure icon (closed).
    private int treasureNameOpenHD; // Stores the reference ID of the 1080p version of the treasure icon (opened).

    /** INSTANCE FUNCTIONALITY _________________________________________________________________ **/

    // getInstance(): Returns the LTTM_SPRITES instance.
    public static LTTMSprites getInstance() {
        if (LTTM_SPRITES == null) {
            LTTM_SPRITES = new LTTMSprites();
        }
        return LTTM_SPRITES;
    }

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

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
    public void loadGameSprites(String gameName) {

        // LTTMWorldView Sprites: Sets the loading screen and sprites for the LTTMWorldView activity.
        if (gameName.equals("loz_alttp")) {
            loadingName = R.drawable.zelda_background;
        }
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public int getGlobeIcon() {
        return globeIcon;
    }

    public int getLoadingName() {
        return loadingName;
    }

    public int getTreasureName() {
        return treasureName;
    }

    public int getTreasureNameOpen() {
        return treasureNameOpen;
    }

    public int getMapName() {
        return mapName;
    }

    public int getMapNameHD() {
        return mapNameHD;
    }

    public int getTravelName() {
        return travelName;
    }

    public int getTravelNameHD() {
        return travelNameHD;
    }

    public int getTreasureNameHD() {
        return treasureNameHD;
    }

    public int getTreasureNameOpenHD() {
        return treasureNameOpenHD;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setGlobeIcon(int globeIcon) {
        this.globeIcon = globeIcon;
    }

    public void setLoadingName(int loadingName) {
        this.loadingName = loadingName;
    }

    public void setTreasureName(int treasureName) {
        this.treasureName = treasureName;
    }

    public void setTreasureNameOpen(int treasureNameOpen) {
        this.treasureNameOpen = treasureNameOpen;
    }

    public void setMapName(int mapName) {
        this.mapName = mapName;
    }

    public void setMapNameHD(int mapNameHD) {
        this.mapNameHD = mapNameHD;
    }

    public void setTravelName(int travelName) {
        this.travelName = travelName;
    }

    public void setTravelNameHD(int travelNameHD) {
        this.travelNameHD = travelNameHD;
    }

    public void setTreasureNameHD(int treasureNameHD) {
        this.treasureNameHD = treasureNameHD;
    }

    public void setTreasureNameOpenHD(int treasureNameOpenHD) {
        this.treasureNameOpenHD = treasureNameOpenHD;
    }
}