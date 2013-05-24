package org.glowacki.rpg.event;

/**
 * Listen for events
 */
public interface CreateListener
{
    /**
     * Receive an event
     *
     * @param evt event
     */
    void send(CreateEvent evt);
}
