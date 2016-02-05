package com.amaze.main;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This is a class responsible for holding a window for aMaze
 */

public class Window extends RenderWindow{

    private int screenWidth;
    private int screenHeight;

    Sound sound;                                        //Sound to be played

    ArrayList<Displayable> scenes = new ArrayList<>();  //Holds all the scenes. This ArrayList is populated from the Main function.

    /*******************************************************************************************************
     *                                       !IMPORTANT!                                                   *
     *                                                                                                     *
     * Since scenes are stored in the arrayList, they are accessed                                         *
     * through indexing (e.g - scenes.get(scene)). Therefore, it is                                       *
     * important to remember that with the current logic Menu is scenes.get(0)                             *
     * because it was a first element that was saved to an ArrayList and Game                              *
     * will be scenes.get(1).                                                                              *
     *                                                                                                     *
     * Since Menu is not yet fully developed, new scenes will be added.                                    *
     * Example:                                                                                            *
     *                                                                                                     *
     *        scenes for different menu options (E.G settings, credits, about, leaderboards, etc...)       *
     *        These scenes will be placed right after the Menu (1,2,3,4,5....)                             *
     *        This will be constantly shifting the Game index.                                             *
     *                                                                                                     *
     * At the moment, it should be 1.                                                                      *
     *                                                                                                     *
     *******************************************************************************************************/

    int currentScene = 0;

    /**
     *
     * @param resolutionX Width in pixels(physical size of window)
     * @param resolutionY Height in pixels(physical size of window)
     *
     * Example:
     *                maze(1920,1080,25,25)
     *                This would create a window of 1920x1080 pixels but with 25 by 25
     *                grid to place objects into a maze
     */

    public Window(int resolutionX, int resolutionY) throws IOException {
        //Instantiate
        this.screenWidth = resolutionX;
        this.screenHeight = resolutionY;

        // Creating a new window
        this.create(new VideoMode(screenWidth, screenHeight), "aMaze", WindowStyle.DEFAULT);
        this.setFramerateLimit(60);

        //Load Sounds
        SoundBuffer soundBuffer = new SoundBuffer();
        soundBuffer.loadFromFile(Paths.get("res/beep.wav"));

        //Create a sound and set its buffer
        sound = new Sound();
        sound.setBuffer(soundBuffer);
    }

    /**
     * Used to start displaying the window. Once this is called nothing can be changed.
     */

    public void displayThis() {
        while (this.isOpen()) {
            //Shows the scene that was selected on the menu.
            scenes.get(currentScene).display(this);
        }
    }

    /**
     * Adds one or more scenes to ArrayList
     * @param scenes - visual representation of a certain aMaze game part.
     */
    public void addScenes(Displayable... scenes) {
        Collections.addAll(this.scenes, scenes);
    }

    public int getScreenHeight() { return screenHeight; }

    public int getScreenWidth() { return screenWidth; }

    /**
     * Obtains a scene's Displayable interface from ArrayList
     * @param i - the index of the scene in the ArrayList
     */
    public Displayable getScene(int i) { return scenes.get(i); }

    public int getCurrentScene() {return currentScene; }

    /**
     * Sets scene variable which is responsible for selecting appropriate scene to be displayed.
     * @param i - used as an ArrayList index.
     */
    public void setScene(int i) { currentScene = i; }

    public ArrayList getArrayList() { return scenes;}
}