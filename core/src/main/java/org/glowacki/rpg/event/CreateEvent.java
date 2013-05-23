package org.glowacki.rpg.event;

public abstract class CreateEvent
{
    public enum Type {
        CREATE_MONSTER,
        CREATE_PLAYER,
    };

    private Type type;

    CreateEvent(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }
}
