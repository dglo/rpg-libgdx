package org.glowacki.rpg.core;

import com.badlogic.gdx.Gdx;

import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;
import org.glowacki.core.ILevel;
import org.glowacki.core.VisibleMap;
import org.glowacki.core.event.CoreEvent;
import org.glowacki.core.event.ChangeLevelEvent;
import org.glowacki.core.event.EventListener;
import org.glowacki.core.event.MoveEvent;
import org.glowacki.core.event.StateEvent;

public class ViewCharacter
    implements Comparable, EventListener
{
    private int id;
    private ICharacter ch;
    private ILevel level;
    private int x;
    private int y;
    private float speed;

    private int tileWidth;
    private int tileHeight;

    private boolean[][] seen;
    private VisibleMap vmap;

    private float realX;
    private float realY;
    private float goalX;
    private float goalY;

    public ViewCharacter(ICharacter ch, ILevel level, int x, int y,
                         float speed, int tileWidth, int tileHeight)
    {
        this.id = ch.getId();
        this.ch = ch;
        this.speed = speed;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        setLevel(level, x, y);

        ch.addEventListener(this);
    }

    public int compareTo(Object obj)
    {
        if (obj == null) {
            return 1;
        }

        if (!(obj instanceof ViewCharacter)) {
            return getClass().getName().compareTo(obj.getClass().getName());
        }

        ViewCharacter vc = (ViewCharacter) obj;
        return id - vc.id;
    }

    private float computePosition(float oldPos, float newPos, float dv)
    {
        if (Math.abs(newPos - oldPos) > 1) {
            if (oldPos < newPos) {
                final float pos = oldPos + dv;
                if (pos < newPos) {
                    return pos;
                }
            }

            if (oldPos > newPos) {
                final float pos = oldPos - dv;
                if (pos > newPos) {
                    return pos;
                }
            }
        }

        return newPos;
    }

    private float convertXToReal(int pos)
    {
        return pos * tileWidth;
    }

    private float convertYToReal(int pos)
    {
        final int screenHeight = Gdx.graphics.getHeight();

        return screenHeight - ((pos + 1) * tileHeight);
    }

    public boolean equals(Object obj)
    {
        return compareTo(obj) == 0;
    }

    public int getId()
    {
        return id;
    }

    public ILevel getLevel()
    {
        return level;
    }

    public boolean[][] getSeen()
    {
        return seen;
    }

    public boolean[][] getVisible()
    {
        return vmap.getVisible(x, y, ch.getSightDistance());
    }

    public float getRealX()
    {
        return realX;
    }

    public float getRealY()
    {
        return realY;
    }

/*
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
*/

    public int hashCode()
    {
        return id;
    }

    public boolean move()
    {
        if (realX != (int) goalX || realY != (int) goalY) {
            final float dv = Gdx.graphics.getDeltaTime() * speed;

            realX = computePosition(realX, goalX, dv);
            realY = computePosition(realY, goalY, dv);
            return true;
        }

        if (ch.hasPath()) {
            try {
                int rtnval = ch.movePath();
System.out.format("MovePath %d -> %d,%d \n",rtnval,x,y);
            } catch (CoreException ce) {
                ce.printStackTrace();
            }

            return false;
        }

        return false;

    }

    public void send(CoreEvent evt)
    {
        switch (evt.getType()) {
        case CHANGE_LEVEL:
            {
                ChangeLevelEvent chgEvt = (ChangeLevelEvent) evt;

                if (chgEvt.getFromLevel() != level) {
                    final ICharacter nch = chgEvt.getCharacter();
                    final ILevel nlevel = chgEvt.getFromLevel();

                    throw new Error(String.format("Expected %d(#%d) to be on" +
                                                  " %s, not %s",
                                                  nch.getName(), nch.getId(),
                                                  nlevel.getName(),
                                                  level.getName()));
                } else if (chgEvt.getFromX() != x || chgEvt.getFromY() != y) {
                    final ICharacter ch = chgEvt.getCharacter();

                    throw new Error(String.format("Expected %d(#%d) to be at" +
                                                  " %d,%d, not %d,%d",
                                                  ch.getName(), ch.getId(),
                                                  chgEvt.getFromX(),
                                                  chgEvt.getFromY(), x, y));
                }

                setLevel(chgEvt.getToLevel(), chgEvt.getToX(),
                         chgEvt.getToY());
            }
            break;
        case MOVE:
            {
                MoveEvent mvEvt = (MoveEvent) evt;

                if (mvEvt.getFromX() != x || mvEvt.getFromY() != y) {
                    final ICharacter ch = mvEvt.getCharacter();

                    throw new Error(String.format("Expected %d(#%d) to be at" +
                                                  " %d,%d, not %d,%d",
                                                  ch.getName(), ch.getId(),
                                                  mvEvt.getFromX(),
                                                  mvEvt.getFromY(), x, y));
                }

                x = mvEvt.getToX();
                y = mvEvt.getToY();

                goalX = convertXToReal(x);
                goalY = convertYToReal(y);
            }
            break;
        case STATE:
            StateEvent stEvt = (StateEvent) evt;
            System.out.format("%s(#%d) state changed from %s to %s\n",
                              stEvt.getCharacter().getName(),
                              stEvt.getCharacter().getId(),
                              stEvt.getFromState(), stEvt.getToState());
            break;
        default:
            throw new Error("Unknown event " + evt);
        }
    }

    public void setGoal(int x, int y)
    {
        throw new Error("Unimplemented");
    }

    private void setLevel(ILevel level, int x, int y)
    {
        this.level = level;
        this.x = x;
        this.y = y;

        seen = ch.getSeenArray();
        vmap = new VisibleMap(level.getMap());

        realX = convertXToReal(x);
        realY = convertYToReal(y);

        goalX = realX;
        goalY = realY;
    }
}
