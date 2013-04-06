package org.glowacki.rpg.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import org.glowacki.core.CoreException;
import org.glowacki.core.Direction;
import org.glowacki.core.ICharacter;

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
    private char thisKey;
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
/*
        if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
            shiftPressed = true;
        } else {
            clear();

            lastKey = thisKey;
            thisKey = keycode;
            useTouch = false;
        }

        return true;
*/
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     *
     * @return whether the input was processed
     */
    public boolean keyTyped(char character)
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
/*
        if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
            shiftPressed = false;
        } else {
            clear();

            lastKey = keycode;
            thisKey = Keys.UNKNOWN;
            useTouch = false;
        }

        return true;
*/
        return false;
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

public class GdxControl
{
    private Input input;

    public GdxControl()
    {
        input = new Input();
        Gdx.input.setInputProcessor(input);
    }

    public boolean update(ICharacter player)
    {
        InputAction action = input.getAction();
        if (action == null) {
            return false;
        }

        if (action.useCoord) {
            System.out.format("Plot path to %d,%d\n", action.x, action.y);
            return false;
        }

        Direction dir;
        if (/*action.key == Keys.LEFT || */action.key == 'h') {
            dir = Direction.LEFT;
        } else if (/*action.key == Keys.RIGHT || */action.key == 'i') {
            dir = Direction.RIGHT_UP;
        } else if (/*action.key == Keys.DOWN || */action.key == 'j') {
            dir = Direction.DOWN;
        } else if (/*action.key == Keys.UP || */action.key == 'k') {
            dir = Direction.UP;
        } else if (/*action.key == Keys.RIGHT || */action.key == 'l') {
            dir = Direction.RIGHT;
        } else if (action.key == 'm') {
            dir = Direction.RIGHT_DOWN;
        } else if (action.key == 'n') {
            dir = Direction.LEFT_DOWN;
        } else if (action.key == 'u') {
            dir = Direction.LEFT_UP;
        } else if (action.key == '<') {
            dir = Direction.CLIMB;
        } else if (action.key == '>') {
            dir = Direction.DESCEND;
        } else if (action.key == 'q') {
            dir = Direction.UNKNOWN;
System.out.println("QUIT ignored");
        } else {
System.out.format("Ignore #%c\n", action.key);
            dir = Direction.UNKNOWN;
        }

        if (dir != Direction.UNKNOWN) {
            try {
                player.move(dir);
                return true;
            } catch (CoreException ce) {
                // XXX
                ce.printStackTrace();
            }
        }

        return false;
    }
}
