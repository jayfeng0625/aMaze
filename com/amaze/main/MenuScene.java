package com.amaze.main;
import org.jsfml.audio.Music;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * This class will handle the menu and buttons associated with it
 */
public class MenuScene extends Scene {

	private final int NUMBER_OF_ITEMS = 6;                  // Number of items available to select in the menu
	private Button buttons[] = new Button[NUMBER_OF_ITEMS];  // Array which holds buttons(items).
	private int currentButton = 0;                          // Track currently selected item in the menu.
	private Title title;
	private Background background;
	private Music music;
	private Music click;
	private boolean state = true;

	/**
	 * Constructs buttons to be displayed on the main window.
	 *
	 * @param sceneTitle - title of the Scene
	 * @param window - object reference to the main window.
	 */

	public MenuScene(String sceneTitle, Window window) throws IOException {
		super(sceneTitle, window);

		float titleHeight = window.getScreenHeight() / 3;
		float titleWidth  = window.getScreenWidth();
		float titleXCord  = window.getScreenWidth() / 2 - (titleWidth / 2);
		float titleYCord  = window.getScreenHeight() / 4  - (titleHeight / 1.5F);

		float itemHeight = window.getScreenHeight() / 6;
		float itemWidth  = window.getScreenWidth() / 1.75F;
		float itemXCoord = window.getScreenWidth() / 2 - (itemWidth / 2);
		float itemYCoord = window.getScreenHeight() / (NUMBER_OF_ITEMS - 2);

		float musicButtonHeight = window.getScreenHeight() / 12;
		float musicButtonWidth = window.getScreenHeight() / 12;

		float webButtonHeight = window.getScreenHeight() / 12;
		float webButtonWidth = window.getScreenHeight() / 12;

		background = new Background(window.getScreenWidth(), window.getScreenHeight());

		title = new Title                  (titleXCord, titleYCord,        titleWidth, titleHeight);
		buttons[0] = new PlayButton        (itemXCoord, itemYCoord,        itemWidth, itemHeight,  window, this);
		buttons[1] = new MapMakerButton    (itemXCoord, itemYCoord * 1.6F, itemWidth, itemHeight,  window, this, background.getCurrentTheme());
		buttons[2] = new InstructionsButton(itemXCoord, itemYCoord * 2.2F, itemWidth, itemHeight,  window, this, background.getCurrentTheme());
		buttons[3] = new ExitButton        (itemXCoord, itemYCoord * 2.8F, itemWidth, itemHeight,  window, this);

		buttons[4] = new MusicButton       (itemXCoord * 4.0F, itemYCoord * 3.5F, musicButtonWidth, musicButtonHeight, window, this);
		buttons[5] = new WebButton         (itemXCoord * 0.2F, itemYCoord * 3.5F, webButtonWidth, webButtonHeight, window, this);

        music = new Music();
        click = new Music();
        try {
            music.openFromFile(Paths.get("res/music/Chilled Music.wav"));
            click.openFromFile(Paths.get("res/music/Click.wav"));
        } catch (IOException e) {
            System.out.println("There was a problem loading the background music or click music.");
        }

		music.play();
		music.setLoop(true);
		buttons[0].setSelected(true);
	}

	/**
	 * This function is triggered when user presses arrow key up.
	 * Every time this function is called, next item on the menu will be selected.
	 * Corresponding boolean variable, as well as the color of the item will change.
	 */
	public void arrowKeyUp() {
		clicked();
		if (currentButton == 0) {
			buttons[currentButton].setSelected(false);
			buttons[3].setSelected(true);
			currentButton = 3;
		} else {
			buttons[currentButton].setSelected(false);
			buttons[--currentButton].setSelected(true);
		}
	}

	/**
	 * This function is triggered when user presses arrow key down.
	 * Every time this function is called, next item on the menu will be selected.
	 * Corresponding boolean variable, as well as the color of the item will change.
	 */
	public void arrowKeyDown() {
		clicked();
		if(currentButton == 3) {
			buttons[currentButton].setSelected(false);
			buttons[0].setSelected(true);
			currentButton = 0;
		} else {
			buttons[currentButton].setSelected(false);
			buttons[++currentButton].setSelected(true);
		}
	}

	/**
	 * This function is triggered when user presses enter button.
	 * This function will check which button is currently selected.
	 * Based on the button, a specific function will be invoked.
	 */
	public void enterPressed() {
		for (Button b: buttons) {
			if (b.isSelected()) {
				if (b instanceof MusicButton) {
					state = !state;
					musicPlaying(state);
				} else {
					b.performAction();
				}
			}
		}
	}

	public Button[] getButtons() { return buttons; }

	/**
	 * This function handles event based on their type.
	 *
	 * @param event - event to be handled.
	 * test.
	 */
	public void executeEvent(Event event) {
		switch(event.type) {
			case CLOSED:
				music.stop();
				systemExit();
				break;
			case KEY_PRESSED:
				switch (event.asKeyEvent().key) {
					case UP: arrowKeyUp(); break;
					case DOWN: arrowKeyDown(); break;
					case M:
						state = !state;
						musicPlaying(state); break;
					case N: buttons[5].performAction(); break;
					case RETURN: enterPressed(); break;
				}
				break;
			case JOYSTICK_BUTTON_PRESSED:
				System.out.println(event.asJoystickButtonEvent().button);
				switch (event.asJoystickButtonEvent().button) {
					case 1: arrowKeyDown();break;
					case 3: arrowKeyUp();break;
					case 9:
						state = !state;
						musicPlaying(state); break;
					case 13: enterPressed(); break;
					case 8: buttons[5].performAction(); break;
				}
				break;
			case MOUSE_MOVED:
				for (int i = 0; i < buttons.length; i++) {
					if (buttons[i].isMouseOn()) {
						buttons[i].setSelected(true);
						currentButton = i;
					} else {
						buttons[i].setSelected(false);
					}
					if (i != currentButton) buttons[i].setSelected(false);
				}
				break;
			case MOUSE_BUTTON_PRESSED:
				switch (event.asMouseButtonEvent().button) {
					case LEFT: enterPressed(); break;
				}
				break;
		}
	}

	/**
	 * Draws items associated with MenuScene in the main Window.
	 * @param window - reference to the window.
	 */
	public void drawGraphics(RenderWindow window) {
		window.draw(background);
		for (Button b : getButtons()) {
			window.draw(b);
		}
		window.draw(title);
	}

	public void musicPlaying(boolean state) {
		if (!state) {
			music.pause();
			buttons[4].setSelected(true);
			try {
				buttons[4].getDefaultIcon().loadFromFile(Paths.get("res/menuGraphics/musicOff.png"));
				buttons[4].getSelectedIcon().loadFromFile(Paths.get("res/menuGraphics/musicOffsel.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			buttons[4].setSelected(false);
			music.play();
			try {
				buttons[4].getDefaultIcon().loadFromFile(Paths.get("res/menuGraphics/musicOn.png"));
				buttons[4].getSelectedIcon().loadFromFile(Paths.get("res/menuGraphics/musicOnsel.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void clicked() {
		click.play();
	}

	public Music getMusic() {
		return music;
	}
	public static String hello() {return  "Hello";}

}
