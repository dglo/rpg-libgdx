package org.glowacki.rpg.event;

import org.glowacki.core.ICharacter;
import org.glowacki.core.ILevel;

/**
 * Create monster event
 */
public class CreatePlayerEvent
    extends CreateEvent
{
    private ICharacter eChar;
    private ILevel level;
    private int x;
    private int y;

    /**
     * Create a player creation event
     *
     * @param eChar character
     * @param level level
     * @param x X coordinate
     * @param y Y coordinate
     */
    public CreatePlayerEvent(ICharacter eChar, ILevel level, int x, int y)
    {
        super(Type.CREATE_PLAYER);

        this.eChar = eChar;
        this.level = level;
        this.x = x;
        this.y = y;
    }

    /**
     * Get the player character
     *
     * @return character
     */
    public ICharacter getCharacter()
    {
        return eChar;
    }

    /**
     * Get the player's level
     *
     * @return level
     */
    public ILevel getLevel()
    {
        return level;
    }

    /**
     * Get the X coordinate
     *
     * @return X coordinate
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get the Y coordinate
     *
     * @return Y coordinate
     */
    public int getY()
    {
        return y;
    }

    /**
     * Return a debugging string.
     *
     * @return debugging string
     */
    public String toString()
    {
        return String.format("Create[%s %s:%d,%d]", eChar.getName(),
                             level.getName(), x, y);
    }
}
