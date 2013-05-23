package org.glowacki.rpg.event;

import org.glowacki.core.ICharacter;
import org.glowacki.core.ILevel;

public class CreatePlayerEvent
    extends CreateEvent
{
    private ICharacter eChar;
    private ILevel level;
    private int x;
    private int y;

    public CreatePlayerEvent(ICharacter eChar, ILevel level, int x, int y)
    {
        super(Type.CREATE_PLAYER);

        this.eChar = eChar;
        this.level = level;
        this.x = x;
        this.y = y;
    }

    public ICharacter getCharacter()
    {
        return eChar;
    }

    public ILevel getLevel()
    {
        return level;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String toString()
    {
        return String.format("Create[%s %s:%d,%d]", eChar.getName(),
                             level.getName(), x, y);
    }
}
