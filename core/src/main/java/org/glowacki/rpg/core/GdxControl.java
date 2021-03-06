package org.glowacki.rpg.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import org.glowacki.core.CoreException;
import org.glowacki.core.Direction;
import org.glowacki.core.ICharacter;
import org.glowacki.core.IMapPoint;
import org.glowacki.core.util.IRandom;

/**
 * Point
 */
class MyPoint
    implements IMapPoint
{
    private int x;
    private int y;

    MyPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Get this point's X coordinate.
     *
     * @return X coordinate
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get this point's Y coordinate.
     *
     * @return Y coordinate
     */
    public int getY()
    {
        return y;
    }
}

/**
 * An InputProcessor is used to receive input events from the keyboard and the
 * touch screen (mouse on the desktop). For this it has to be registered with
 * the {@link Input#setInputProcessor(InputProcessor)} method. It will be
 * called each frame before the call to {@link ApplicationListener#render()}.
 * Each method returns a boolean in case you want to use this with the
 * {@link InputMultiplexer} to chain input processors.
 */
class Input
    implements InputProcessor
{
    private char lastKey;
    private int x;
    private int y;
    private boolean useTouch;

    private synchronized void clear()
    {
        lastKey = Keys.UNKNOWN;
        useTouch = false;
    }


    public synchronized InputAction getAction()
    {
        if (useTouch) {
            final int tmpX = x;
            final int tmpY = y;

            clear();

            return new InputAction(true, tmpX, tmpY, (char) 0);
        }

        if (lastKey != Keys.UNKNOWN) {
            final char key = lastKey;

            clear();

            return new InputAction(false, Integer.MIN_VALUE, Integer.MIN_VALUE,
                                   key);
        }

        return null;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     *
     * @return whether the input was processed
     */
    public synchronized boolean keyDown(int keycode)
    {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     *
     * @return whether the input was processed
     */
    public synchronized boolean keyTyped(char character)
    {
        lastKey = character;
        return true;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     *
     * @return whether the input was processed
     */
    public synchronized boolean keyUp(int keycode)
    {
        if (keycode == Keys.LEFT) {
            lastKey = 'h';
        } else if (keycode == Keys.RIGHT) {
            lastKey = 'l';
        } else if (keycode == Keys.DOWN) {
            lastKey = 'j';
        } else if (keycode == Keys.UP) {
            lastKey = 'k';
        } else {
            return false;
        }

        return true;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed.
     * Will not be called on Android.
     *
     * @return whether the input was processed
     */
    public boolean mouseMoved(int x, int y)
    {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on Android.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the
     *               wheel was scrolled.
     *
     * @return whether the input was processed
     */
    public boolean scrolled(int amount)
    {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed.
     * The button parameter will be {@link Buttons#LEFT} on Android.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     *
     * @return whether the input was processed
     */
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param pointer the pointer for the event.
     *
     * @return whether the input was processed
     */
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released.
     * The button parameter will be {@link Buttons#LEFT} on Android.
     *
     * @param pointer the pointer for the event.
     * @param button the button
     *
     * @return whether the input was processed
     */
    public synchronized boolean touchUp(int screenX, int screenY, int pointer,
                                        int button)
    {
        clear();

        x = screenX;
        y = screenY;
        useTouch = true;

        return true;
    }
}

/**
 * Handle the control of the game
 */
public class GdxControl
{
    private int tileWidth;
    private int tileHeight;
    private IRandom random;

    private Input input;

    public GdxControl(int tileWidth, int tileHeight, IRandom random)
    {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.random = random;

        input = new Input();
        Gdx.input.setInputProcessor(input);
    }

    /**
     * Update this player
     *
     * @param player player
     *
     * @return <tt>true</tt> if the player has been updated
     */
    public boolean update(ICharacter player)
    {
        InputAction action = input.getAction();
        if (action == null) {
            return false;
        }

        if (action.useCoord) {
            final int screenWidth = Gdx.graphics.getWidth();
            final int screenHeight = Gdx.graphics.getHeight();

            final float moveX = action.x - ((float) screenWidth / 2.0f);
            final float moveY = ((float) screenHeight / 2.0f) - action.y;

            final int mapX =
                (int) (player.getX() + (moveX / (float) tileWidth));
            final int mapY =
                1 + (int) (player.getY() - (moveY / (float) tileHeight));

            if (!player.isSeen(mapX, mapY)) {
                System.err.format("Position [%d,%d] is not visible\n",
                                  mapX, mapY);
                return false;
            }

            if (mapX == player.getX() && mapY == player.getY()) {
                // clicked on current position
                if (player.onStaircase()) {
                    try {
                        int numTurns = player.useStaircase();
                    } catch (CoreException ce) {
                        ce.printStackTrace();
                        return false;
                    }

                    player.clearPath();
                    return true;
                }

                return false;
            }

            ICharacter occupant;
            try {
                occupant = player.getOccupant(mapX, mapY);
            } catch (CoreException ce) {
                ce.printStackTrace();
                occupant = null;
            }

            if (occupant != null) {
                // interact with another character
                if (!occupant.isPlayer()) {
                    try {
                        player.attack(random, occupant);
                    } catch (CoreException ce) {
                        ce.printStackTrace();
                        return false;
                    }

                    return true;
                }
            }

            try {
                player.buildPath(new MyPoint((int) mapX, (int) mapY));
                int numTurns = player.movePath();
            } catch (CoreException ce) {
                ce.printStackTrace();
                return false;
            }

            return true;
        }

        Direction dir;
        if (action.key == 'b') {
            dir = Direction.LEFT_DOWN;
        } else if (action.key == 'h') {
            dir = Direction.LEFT;
        } else if (action.key == 'j') {
            dir = Direction.DOWN;
        } else if (action.key == 'k') {
            dir = Direction.UP;
        } else if (action.key == 'l') {
            dir = Direction.RIGHT;
        } else if (action.key == 'n') {
            dir = Direction.RIGHT_DOWN;
        } else if (action.key == 'u') {
            dir = Direction.RIGHT_UP;
        } else if (action.key == 'y') {
            dir = Direction.LEFT_UP;
        } else if (action.key == '<') {
            dir = Direction.CLIMB;
        } else if (action.key == '>') {
            dir = Direction.DESCEND;
        } else if (action.key == 'q') {
            Gdx.app.exit();
            dir = Direction.UNKNOWN;
        } else {
            System.out.format("Ignore #%c\n", action.key);
            dir = Direction.UNKNOWN;
        }

        if (dir != Direction.UNKNOWN) {
            try {
                int numTurns = player.move(dir);
                player.clearPath();
                return true;
            } catch (CoreException ce) {
                // XXX
                ce.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
