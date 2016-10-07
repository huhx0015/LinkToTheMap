package com.ycorner.linktothemap.Data;

import com.ycorner.linktothemap.Objects.LTTMMap;
import com.ycorner.linktothemap.R;
import java.util.LinkedList;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMMapData] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMMapData class is used to determine the maps and resources for the application.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMMapData {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // INSTANCE VARIABLES
    private static LTTMMapData lttmMaps; // LTTMMapData instance variable.

    // MAP VARIABLES
    private boolean animatedBG; // Used to determine if the background tiles need to be animated.
    private boolean isLarge; // Used for determining if the map image is larger than 2560x2560 in size.
    private boolean isWeb; // Used for determining if the map image needs to be downloaded from the Internet.
    private boolean labelsOn; // Stores the value of the map labels option.
    private int BGTimer; // Stores the value of the animated background timer.
    private int mapViewImage, mapOverlayImage, mapUnderlayImage; // Stores the map resource values.
    private int graphicsMode; // Stores the current graphics mode value.
    private int parentID; // Stores the spinner position of the parent map.
    private LinkedList<Integer> mapUnderlayImageSet; // Stores the list of map underlay resources.
    private String gameName; // Stores the name of the current game.
    private String mapSong; // Stores the name of the song that corresponds with the map.
    private String mapType; // Stores the type of map that corresponds with the current map.
    private String parentMap; // Stores the name of the parent map.
    private boolean hasDungeons;
    //private LinkedList<LTTMMap> floorMapList; // Stores the list of maps.
    private LTTMMap floorMaps;

    /** INSTANCE FUNCTIONALITY _________________________________________________________________ **/

    // getInstance(): Returns the lttmMap instance.
    public static LTTMMapData getInstance() {
        if (lttmMaps == null) {
            lttmMaps = new LTTMMapData();
        }
        return lttmMaps;
    }

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // initializeLTTM(): Initializes the LTTMMapData class variables.
    public void initializeLTTM() {

        // Sets the default variables for the LTTMMapData class.
        gameName = "loz_alttp";
        graphicsMode = 2;
        labelsOn = true;
        mapSong = "";
        mapOverlayImage = R.drawable.lttm_transparent_tile;
        mapViewImage = R.drawable.lttm_transparent_tile;
        mapUnderlayImage = R.drawable.lttm_transparent_tile;

        resetMapSettings(); // Resets the map settings.
    }

    /** MAP FUNCTIONALITY ______________________________________________________________________ **/

    // mapSelector(): Sets the appropriate map based on the map string passed in.
    public void mapSelector(String map) {

        resetMapSettings(); // Resets map variables to default settings before selecting a new map.

        // -------------------------------------------------------------------------------------- //
        // THE LEGEND OF ZELDA: A LINK TO THE PAST [SNES]
        // -------------------------------------------------------------------------------------- //
        if (gameName.equals("loz_alttp")) {

            // The Legend of Zelda: A Link to the Past [SNES]
            if (map.equals("Light World")) {

                isLarge = true; // Indicates that the map is larger than 2048x2048 and needs to be downscaled if 'LOW' GRAPHICS mode is enabled.
                mapViewImage = R.drawable.loz_alttp_light_world;

                // Sets the map and background tile images appropriate to the selection.
                mapOverlayImage = R.drawable.lttm_transparent_tile;
                mapUnderlayImage = R.drawable.lttm_transparent_tile;

                // Sets the attributes for the map.
                mapSong = "overworld"; // Sets the song appropriate to the map.
                mapType = "WORLD"; // Sets the map type.
                parentMap = "ROOT"; // Sets the parent map name.
            }

            // The Legend of Zelda: A Link to the Past [SNES]
            else if (map.equals("Dark World")) {

                isLarge = true; // Indicates that the map is larger than 2048x2048 and needs to be downscaled if 'LOW' GRAPHICS mode is enabled.
                mapViewImage = R.drawable.loz_alttp_dark_world;

                // Sets the map and background tile images appropriate to the selection.
                mapOverlayImage = R.drawable.lttm_transparent_tile;
                mapUnderlayImage = R.drawable.lttm_transparent_tile;

                // Sets the attributes for the map.
                mapSong = "darkworld"; // Sets the song appropriate to the map.
                mapType = "WORLD"; // Sets the map type.
                parentMap = "ROOT"; // Sets the parent map name.
            }

            // The Legend of Zelda: A Link to the Past [SNES]
            else if (map.equals("Hyrule Castle Dungeon")) {
                hasDungeons = true;

                // TODO: Insert logic to deal with floor maps here.
                floorMaps = new LTTMMap(R.drawable.loz_alttp_hyrule_castle_1f, -1, -1, -1, -1, -1, -1, -1, -1, -1);

                mapViewImage = R.drawable.loz_alttp_hyrule_castle_1f;

                // Sets the map and background tile images appropriate to the selection.
                mapOverlayImage = R.drawable.lttm_transparent_tile;
                mapUnderlayImage = R.drawable.lttm_dark_transparent_tile;

                // Sets the attributes for the map.
                mapSong = "hyrulecastle"; // Sets the song appropriate to the map.
                mapType = "WORLD"; // Sets the map type.
                parentMap = "ROOT"; // Sets the parent map name.
            }
        }

        // -------------------------------------------------------------------------------------- //

        // FAIL CASE: If an invalid map name is selected, sets the background map and underlay map
        // to black.
        else {
            mapOverlayImage = R.drawable.lttm_transparent_tile;
            mapViewImage = R.drawable.black_background;
            mapUnderlayImage = R.drawable.black_background;
        }
    }

    // resetMapSettings(): Resets map variables to default settings.
    private void resetMapSettings() {

        animatedBG = false; // Clears the animated background value.
        BGTimer = 0; // Resets the BGTimer value.
        isLarge = false; // Resets the large map indicator before a map is selected.
        isWeb = false; // Resets the Internet indicator before a map is selected.
        mapType = ""; // Clears the map type.
        mapUnderlayImageSet = new LinkedList<Integer>(); // Clears the map underlay image set.
        parentID = 0; // Resets the parent ID to 0.
        parentMap = ""; // Clears the parent map.
        
        // TODO: New variables
        hasDungeons = false;
        //floorMapList.clear();
        floorMaps = new LTTMMap();
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public boolean isAnimatedBG() {
        return animatedBG;
    }

    public boolean isLarge() {
        return isLarge;
    }

    public boolean isWeb() {
        return isWeb;
    }

    public boolean isLabelsOn() {
        return labelsOn;
    }

    public int getBGTimer() {
        return BGTimer;
    }

    public int getMapViewImage() {
        return mapViewImage;
    }

    public int getMapOverlayImage() {
        return mapOverlayImage;
    }

    public int getMapUnderlayImage() {
        return mapUnderlayImage;
    }

    public int getGraphicsMode() {
        return graphicsMode;
    }

    public int getParentID() {
        return parentID;
    }

    public LinkedList<Integer> getMapUnderlayImageSet() {
        return mapUnderlayImageSet;
    }

    public String getGameName() {
        return gameName;
    }

    public String getMapSong() {
        return mapSong;
    }

    public String getMapType() {
        return mapType;
    }

    public String getParentMap() {
        return parentMap;
    }

    public boolean isHasDungeons() {
        return hasDungeons;
    }

    public LTTMMap getFloorMaps() {
        return floorMaps;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setFloorMaps(LTTMMap floorMaps) {
        this.floorMaps = floorMaps;
    }

    public void setAnimatedBG(boolean animatedBG) {
        this.animatedBG = animatedBG;
    }

    public void setLarge(boolean large) {
        isLarge = large;
    }

    public void setWeb(boolean web) {
        isWeb = web;
    }

    public void setLabelsOn(boolean labelsOn) {
        this.labelsOn = labelsOn;
    }

    public void setBGTimer(int BGTimer) {
        this.BGTimer = BGTimer;
    }

    public void setMapViewImage(int mapViewImage) {
        this.mapViewImage = mapViewImage;
    }

    public void setMapOverlayImage(int mapOverlayImage) {
        this.mapOverlayImage = mapOverlayImage;
    }

    public void setMapUnderlayImage(int mapUnderlayImage) {
        this.mapUnderlayImage = mapUnderlayImage;
    }

    public void setGraphicsMode(int graphicsMode) {
        this.graphicsMode = graphicsMode;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public void setMapUnderlayImageSet(LinkedList<Integer> mapUnderlayImageSet) {
        this.mapUnderlayImageSet = mapUnderlayImageSet;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setMapSong(String mapSong) {
        this.mapSong = mapSong;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public void setParentMap(String parentMap) {
        this.parentMap = parentMap;
    }

    public void setHasDungeons(boolean hasDungeons) {
        this.hasDungeons = hasDungeons;
    }
}