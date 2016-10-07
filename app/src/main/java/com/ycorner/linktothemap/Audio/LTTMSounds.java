package com.ycorner.linktothemap.Audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;
import com.ycorner.linktothemap.R;

/** -----------------------------------------------------------------------------------------------
 *  [LTTMSounds] CLASS
 *  PROGRAMMER: Huh X0015
 *  DESCRIPTION: LTTMSounds class is used to support the background sound system for the
 *  application. It is used to play songs and various sound effects in the background.
 *  -----------------------------------------------------------------------------------------------
 */

public class LTTMSounds {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // INSTANCE VARIABLES
    private static LTTMSounds lttm_sounds; // LTTMSounds instance variable.

    // AUDIO VARIABLES:
    private AudioManager soundManager; // AudioManager variable for sound effects.
    private MediaPlayer mapSong; // MediaPlayer variable for map song.
    private SoundPool lttm_soundpool; // SoundPool variable for sound effects.
    private SparseIntArray soundEffectMap; // Hash map for sound effects.
    private String currentSong; // Used for determining what song is playing in the background.
    private boolean isPaused; // Used for determining if a song has been paused.
    private int songPosition; // Used for resuming playback on a song that was paused.
    private boolean musicOn; // Used for determining whether music is playing in the background.
    private boolean soundOn; // Used for determining if sound option is enabled or not.
    private final int MAX_SIMULTANEOUS_SOUNDS = 16; // Can output twenty-four sound effects simultaneously.
    private final int MAX_SOUND_EVENTS = 25; // Maximum number of sound events before the SoundPool object is reset. Android 2.3 (GINGERBREAD) only.
    private int soundEventCount = 0; // Used to count the number of sound events that have occurred.

    // SYSTEM VARIABLES:
    private final int api_level = android.os.Build.VERSION.SDK_INT; // Used to determine the device's Android API version.

    /** INSTANCE FUNCTIONALITY _________________________________________________________________ **/

    // getInstance(): Returns the lttm_sounds instance.
    public static LTTMSounds getInstance() {
        if (lttm_sounds == null) {
            lttm_sounds = new LTTMSounds();
        }
        return lttm_sounds;
    }

    /** INITIALIZATION FUNCTIONALITY ___________________________________________________________ **/

    // initializeLTTM(): Initializes the LTTMSounds class variables.
    public void initializeLTTM() {

        mapSong = new MediaPlayer();
        soundEffectMap = new SparseIntArray();
        isPaused = false;
        musicOn = true;
        soundOn = true;
        currentSong = "STOPPED";
        songPosition = 0;

        setUpSoundPool(); // Initializes the SoundPool object.
    }

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    // setUpSoundPool(): Initializes the SoundPool object. Depending on the Android version of the
    // device, the SoundPool object is created using the appropriate methods.
    private void setUpSoundPool() {

        if (api_level > 20) { lttm_soundpool = constructSoundPool(); } // Android 5.0 and above.
        else { lttm_soundpool = new SoundPool(MAX_SIMULTANEOUS_SOUNDS, AudioManager.STREAM_MUSIC, 0); } // Android 2.3 - 4.4
    }

    // constructSoundPool(): Builds the SoundPool object. This implementation is only used on
    // devices running Android 5.0 and later.
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool constructSoundPool() {

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA) // Sets the audio type to USAGE_GAME.
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        // Initializes the SoundPool.Builder object.
        SoundPool soundBuilder = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(MAX_SIMULTANEOUS_SOUNDS) // Sets the maximum number of audio streams.
                .build();

        return soundBuilder; // Returns the newly created SoundPool object.
    }

    /** AUDIO FUNCTIONALITY ____________________________________________________________________ **/

    // disablePhysicalSounds(): Enables or disables the devices physical button's sound effects.
    public static void disablePhysSounds(Boolean mode, Context context) {
        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, mode);
    }

    // loadLTTMsounds(): Loads sound effects into the soundEffectMap hash map.
    public void loadLTTMsounds(Context context) {
        soundEffectMap.put(1, lttm_soundpool.load(context, R.raw.alttp_text_done, 1));
        soundEffectMap.put(2, lttm_soundpool.load(context, R.raw.alttp_error, 1));
        soundEffectMap.put(3, lttm_soundpool.load(context, R.raw.alttp_savequit, 1));
        soundEffectMap.put(4, lttm_soundpool.load(context, R.raw.alttp_menu_select, 1));
        soundEffectMap.put(5, lttm_soundpool.load(context, R.raw.alttp_map, 1));
        soundEffectMap.put(6, lttm_soundpool.load(context, R.raw.alttp_error, 1));
        soundEffectMap.put(7, lttm_soundpool.load(context, R.raw.alttp_secret, 1));
    }

    // playSoundEffect(): This is a threaded function that plays the selected sound effect.
    public void playSoundEffect(int index, Context context) {

        // Checks to see if the soundPool class has been instantiated first before playing a sound effect.
        // This is to prevent a rare null pointer exception bug.
        if (lttm_soundpool == null) {
            soundEffectMap = new SparseIntArray(); // Initializes a new SparseIntArray object.
            setUpSoundPool(); // Initializes the SoundPool object.
            loadLTTMsounds(context); // Loads the sound effect hash map.
        }

        else {

            // ANDROID 2.3 (GINGERBREAD): The SoundPool object is re-initialized if the sound event
            // counter has reached the MAX_SOUND_EVENT limit. This is to handle the AudioTrack
            // 1 MB buffer limit issue.
            if ((api_level < 11) && (soundEventCount >= MAX_SOUND_EVENTS)) {
                reinitializeSoundPool(context);
            }

            else {

                // Processes and plays the sound effect only if soundOn variable is set to true.
                if (soundOn) {

                    // Retrieves the current volume value.
                    if (soundManager == null) {
                        soundManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    }
                    float volume = soundManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                    // Checks to see if the SoundPool object is null first. If not, the sound effect
                    // is played.
                    if (lttm_soundpool != null) {
                        lttm_soundpool.play(soundEffectMap.get(index), volume, volume, 1, 0, 1.0f); // Plays the sound effect.
                        soundEventCount++; // Increments the sound event counter.
                    }
                }
            }
        }
    }

    // playMapSong(): Plays a music file based on the selected map (passed as mapName). The song is
    // changed if the song of the selected map does not match the current map song.
    public void playMapSong(String songName, Context context) {

        boolean musicFound = false; // Used for determining if a corresponding song for mapName was found or not.
        int songID = 0; // Used for storing the reference ID to the raw music resource object.

        // If the music option has been enabled, a song is selected based on the passed in songName string.
        if (musicOn) {

            if ( (songName.equals("overworld")) && (!currentSong.equals("overworld")) ) {

                // Sets the new map song.
                songID = R.raw.overworld;
                currentSong = "overworld";
                musicFound = true;
            }

            else if ( (songName.equals("darkworld")) && (!currentSong.equals("darkworld")) ) {

                // Sets the new map song.
                songID = R.raw.darkworld;
                currentSong = "darkworld";
                musicFound = true;
            }

            else if ( (songName.equals("hyrulecastle")) && (!currentSong.equals("hyrulecastle")) ) {

                // Sets the new map song.
                songID = R.raw.hyrulecastle;
                currentSong = "hyrulecastle";
                musicFound = true;
            }

            // ---------------------------------------------------------------------------------- //

            // If a map song match was found, play the music file from resources.
            if ( (musicFound) || (isPaused) ) {
                playSong(songID, context); // Calls playSong to create a MediaPlayer object and play the song.
            }
        }
    }

    // pauseSong(): Pauses any songs playing in the background and returns it's position.
    public void pauseSong() {

        // Checks to see if mapSong has been initialized first before saving the song position and pausing the song.
        if (mapSong != null) {

            songPosition = mapSong.getCurrentPosition(); // Retrieves the current song position and saves it.

            // Pauses the song only if there is a song is currently playing.
            if (mapSong.isPlaying()) { mapSong.pause(); }
            isPaused = true; // Indicates that the song is currently paused.
            currentSong = "PAUSED";
        }
    }

    //  playSong(): Sets up a MediaPlayer object and begins playing the song in the background thread.
    private void playSong(int songName, Context context) {

        // Checks to see if the MediaPlayer class has been instantiated first before playing a song.
        // This is to prevent a rare null pointer exception bug.
        if (mapSong == null) { mapSong = new MediaPlayer(); }

        else {

            // Stops any songs currently playing in the background.
            if (mapSong.isPlaying()) {
                mapSong.stop();
            }

            // Sets up the MediaPlayer object for the song to be played.
            releaseMedia(); // Releases MediaPool resources.
            mapSong = new MediaPlayer(); // Initializes the MediaPlayer.
            mapSong.setAudioStreamType(AudioManager.STREAM_MUSIC); // Sets the audio type for the MediaPlayer object.
            mapSong = MediaPlayer.create(context, songName); // Sets up the MediaPlayer for the song.
            mapSong.setLooping(true); // Enables infinite looping of music.

            // If the song was previously paused, resume the song at it's previous location.
            if (isPaused) {
                mapSong.seekTo(songPosition); // Jumps to the position where the song left off.
                songPosition = 0; // Resets songPosition variable after song's position has been set.
                isPaused = false; // Indicates that the song is no longer paused.
            }

            // Sets up the listener for the MediaPlayer object. Song playback begins immediately
            // once the MediaPlayer object is ready.
            mapSong.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start(); // Begins playing the song.
                }
            });
        }
    }

    // reinitializeSoundPool(): This method re-initializes the SoundPool object for devices running
    // on Android 2.3 (GINGERBREAD) and earlier. This is to help minimize the AudioTrack out of
    // memory error, which was limited to a small 1 MB size buffer.
    public void reinitializeSoundPool(Context context) {

        // GINGERBREAD: The SoundPool is released and re-initialized. This is done to minimize the
        // AudioTrack out of memory (-12) error.
        if (api_level < 11) {
            releaseAudio(); // Releases the SoundPool object.
            setUpSoundPool(); // Initializes the SoundPool object.
            loadLTTMsounds(context); // Loads the sound effect hash map.
            soundEventCount = 0; // Resets the sound event counter.
        }
    }

    // releaseAudio(): Used to free up memory resources when all audio effects are no longer needed.
    private void releaseAudio() {

        // Releases SoundPool resources.
        if (lttm_soundpool != null) {
            lttm_soundpool.release();
            lttm_soundpool = null;
        }
    }

    // releaseMediaPlayer(): Used to release the resources being used by mediaPlayer objects.
    private void releaseMedia() {

        // Releases MediaPool resources.
        if (mapSong != null) {
            mapSong.reset();
            mapSong.release();
            mapSong = null;
        }
    }

    //  stopSong(): Stops any songs playing in the background.
    public void stopSong() {

        // Checks to see if mapSong has been initiated first before stopping song playback.
        if (mapSong != null) {

            // Stops any songs currently playing in the background.
            if (mapSong.isPlaying()) { mapSong.stop(); }
            currentSong = "STOPPED";
        }
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public int getSongPosition() {
        return songPosition;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setSongPosition(int songPosition) {
        this.songPosition = songPosition;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }
}