package com.ycorner.linktothemap.Audio;

import android.content.Context;
import com.huhx0015.hxaudio.audio.HXMusic;
import com.ycorner.linktothemap.R;
import java.util.ArrayList;
import java.util.List;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMAudio] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMAudio is a helper class used to support the background sound system for the
 *  application.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMAudio {

    /** AUDIO METHODS __________________________________________________________________________ **/

    // initSoundList(): Generates a list of sound resources to load into HXSound.
    public static List<Integer> initSoundList() {

        List<Integer> soundResourceList = new ArrayList<>();

        // Builds the sound resource list.
        soundResourceList.add(R.raw.alttp_text_done);
        soundResourceList.add(R.raw.alttp_error);
        soundResourceList.add(R.raw.alttp_savequit);
        soundResourceList.add(R.raw.alttp_menu_select);
        soundResourceList.add(R.raw.alttp_map);
        soundResourceList.add(R.raw.alttp_secret);

        return soundResourceList;
    }

    // playMusic(): Plays a music file based on the selected map (passed as mapName). The song is
    // changed if the song of the selected map does not match the current map song.
    public static String playMusic(String songName, String currentSong, Context context) {

        if (currentSong == null) { currentSong = ""; } // Checks if the currentSong is null or not.
        int songID = 0; // Used for storing the reference ID to the raw music resource object.

        if ( (songName.equals("overworld")) && (!currentSong.equals("overworld")) ) {
            songID = R.raw.overworld;
            currentSong = "overworld";
        }

        else if ( (songName.equals("darkworld")) && (!currentSong.equals("darkworld")) ) {
            songID = R.raw.darkworld;
            currentSong = "darkworld";
        }

        else if ( (songName.equals("hyrulecastle")) && (!currentSong.equals("hyrulecastle")) ) {
            songID = R.raw.hyrulecastle;
            currentSong = "hyrulecastle";
        }

        // Plays the music file from resources.
        if (songID != 0) {
            HXMusic.music()
                    .load(songID)
                    .title(currentSong)
                    .gapless(true)
                    .play(context);
        }

        return currentSong;
    }
}
