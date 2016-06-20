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

    // MAP VARIABLES
    public boolean animatedBG; // Used to determine if the background tiles need to be animated.
    public boolean isLarge; // Used for determining if the map image is larger than 2560x2560 in size.
    public boolean isWeb; // Used for determining if the map image needs to be downloaded from the Internet.
    public boolean labelsOn; // Stores the value of the map labels option.
    public int BGTimer; // Stores the value of the animated background timer.
    public int mapViewImage, mapOverlayImage, mapUnderlayImage; // Stores the map resource values.
    public int graphicsMode; // Stores the current graphics mode value.
    public int parentID; // Stores the spinner position of the parent map.
    public LinkedList<Integer> mapUnderlayImageSet; // Stores the list of map underlay resources.
    public String gameName; // Stores the name of the current game.
    public String mapLanguage; // Stores the language value to determine which map overlay to use.
    public String mapSong; // Stores the name of the song that corresponds with the map.
    public String mapType; // Stores the type of map that corresponds with the current map.
    public String parentMap; // Stores the name of the parent map.
    
    // TODO: New variables
    public boolean hasDungeons;
    //public LinkedList<LTTMMap> floorMapList; // Stores the list of maps.
    public LTTMMap floorMaps;

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // LTTMMapData(): Constructor for the LTTMMapData class.
    private final static LTTMMapData lttmMaps = new LTTMMapData();

    // LTTMMapData(): Deconstructor for the LTTMMapData class.
    private LTTMMapData() {}

    // getInstance(): Returns the lttmMap instance.
    public static LTTMMapData getInstance() {
        return lttmMaps;
    }

    // initializeLTTM(): Initializes the LTTMMapData class variables.
    public void initializeLTTM() {

        // Sets the default variables for the LTTMMapData class.
        gameName = "loz_alttp";
        graphicsMode = 2;
        labelsOn = true;
        mapLanguage = "English";
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
}