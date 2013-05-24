package org.glowacki.rpg.event;

/**
 * Base create event
 */
public abstract class CreateEvent
{
    /** All possible event types */
    public enum Type {
        /** Create monster event */
        CREATE_MONSTER,
        /** Create player event */
        CREATE_PLAYER,
    };

    private Type type;

    /**
     * Base create event
     *
     * @param type event type
     */
    CreateEvent(Type type)
    {
        this.type = type;
    }

    /**
     * Get the event type
     *
     * @return event type
     */
    public Type getType()
    {
        return type;
    }
}
