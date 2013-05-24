package org.glowacki.rpg.core;

/**
 * Encapsulate an input action
 */
public class InputAction
{
    boolean useCoord;
    int x;
    int y;
    char key;

    InputAction(boolean useCoord, int x, int y, char key)
    {
        this.useCoord = useCoord;
        this.x = x;
        this.y = y;
        this.key = key;
    }
}
