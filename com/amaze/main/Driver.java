package com.amaze.main;
        import org.jsfml.graphics.Drawable;
        import org.jsfml.graphics.CircleShape;
        import org.jsfml.graphics.ConstView;
        import org.jsfml.graphics.FloatRect;
        import org.jsfml.graphics.View;
        import org.jsfml.system.*;
        import java.io.IOException;
import com.amaze.main.Window;


/**
 * This class is responsible for creating an instance of a Maze.
 */


class Driver{
    private static int BLOCK_SIZE;
    private static int WINDOW_SIZE;

    public static void main(String[] args) throws Exception{
        WINDOW_SIZE = 800;

        com.amaze.main.Window window = new Window(WINDOW_SIZE,WINDOW_SIZE,25,25);

        MenuScene menu = new MenuScene("Main Menu",window);
        LevelReader level = new LevelReader();
        BLOCK_SIZE = WINDOW_SIZE / level.getSizeOfMaze();

        GameScene game = new GameScene("Game",window,WINDOW_SIZE,WINDOW_SIZE,level.getSizeOfMaze(),level.getSizeOfMaze(), BLOCK_SIZE,
                level.getLevel());
        window.addScene(menu);
        window.addScene(game);

        //Start Displaying
        window.displayThis();

    }
}