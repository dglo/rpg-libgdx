package org.glowacki.rpg.event;

import org.glowacki.core.ICharacter;
import org.glowacki.core.ILevel;

/**
 * Create monster event
 */
public class CreateMonsterEvent
    extends CreateEvent
{
    private ICharacter eChar;
    private ILevel level;
    private int x;
    private int y;

    /**
     * Create a monster creation event
     *
     * @param eChar character
     * @param level level
     * @param x X coordinate
     * @param y Y coordinate
     */
    public CreateMonsterEvent(ICharacter eChar, ILevel level, int x, int y)
    {
        super(Type.CREATE_MONSTER);

        this.eChar = eChar;
        this.level = level;
        this.x = x;
        this.y = y;
    }

    /**
     * Get the monster character
     *
     * @return character
     */
    public ICharacter getCharacter()
    {
        return eChar;
    }

    /**
     * Get the monster's level
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
