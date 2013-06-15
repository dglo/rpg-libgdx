package org.glowacki.rpg.core;

import org.glowacki.core.ComputerCharacter;
import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;
import org.glowacki.core.ILevel;
import org.glowacki.core.Level;
import org.glowacki.core.LevelException;
import org.glowacki.core.Map;
import org.glowacki.core.MapException;
import org.glowacki.core.PlayerCharacter;
import org.glowacki.core.dungen.GeneratorException;
import org.glowacki.core.dungen.IMapArray;
import org.glowacki.core.dungen.Room;
import org.glowacki.core.dungen.RoomGenerator2;
import org.glowacki.core.dungen.SimpleGenerator;
import org.glowacki.core.dungen.Tunneler;
import org.glowacki.core.util.IRandom;
import org.glowacki.core.util.Random;
import org.glowacki.rpg.event.CreateListener;
import org.glowacki.rpg.event.CreateMonsterEvent;
import org.glowacki.rpg.event.CreatePlayerEvent;

/**
 * Level which creates additional levels
 */
class DynamicLevel
    extends Level
{
    private static final int maxWidth = 35;
    private static final int maxHeight = 35;
    private static final int gridWidth = 3;
    private static final int gridHeight = 3;

    private static final int maxConnections = 4;
    private static final int maxLevels = 5;

    private IRandom random;
    private int level;
    private CreateListener creListener;

    DynamicLevel(IRandom random, int level, Map map, CreateListener creListener)
    {
        super("L" + level, map);

        this.random = random;
        this.level = level;
        this.creListener = creListener;
    }

    static Level OLDcreateLevel(IRandom random, int num,
                             CreateListener creListener)
        throws GeneratorException
    {
        Room[] rooms =
            RoomGenerator2.createRooms(random, maxWidth, maxHeight,
                                       gridWidth, gridHeight);
        RoomGenerator2.addStairs(rooms, random, true, num < maxLevels);

        Tunneler tunneler = new Tunneler(rooms, maxConnections);

        String[] strMap = tunneler.dig(maxWidth, maxHeight, random);

        Map map;
        try {
            map = new Map(strMap);
        } catch (MapException me) {
            throw new Error("Created bad map " + strMap, me);
        }

        DynamicLevel lvl = new DynamicLevel(random, num, map, creListener);

        //populate(lvl, random, 2, creListener);

        return lvl;
    }

    static Level createLevel(IRandom random, int num,
                             CreateListener creListener)
        throws GeneratorException
    {
        IMapArray charMap =
            SimpleGenerator.createMap(random, maxWidth, maxHeight, gridWidth,
                                      gridHeight, true, num < maxLevels);

        String[] strMap = charMap.getStrings();

        Map map;
        try {
            map = new Map(strMap);
        } catch (MapException me) {
            throw new Error("Created bad map " + strMap, me);
        }

        DynamicLevel lvl = new DynamicLevel(random, num, map, creListener);

        //populate(lvl, random, 2, creListener);

        return lvl;
    }

    public ILevel getNextLevel()
    {
        if (super.getNextLevel() == null) {
            try {
                addNextLevel(createLevel(random, level + 1, creListener));
            } catch (LevelException le) {
                throw new Error("Cannot add level " + (level + 1), le);
            } catch (GeneratorException ge) {
                throw new Error("Cannot add level " + (level + 1), ge);
            }
        }

        return super.getNextLevel();
    }

    private static void populate(Level lvl, IRandom random, int max,
                                 CreateListener creListener)
    {
        for (int i = 0; i < max; i++) {
            ComputerCharacter ch = new ComputerCharacter(random, 6, 6, 6, 6);

            try {
                ch.setLevel(lvl);
            } catch (CoreException ce) {
                throw new Error("Cannot add " + ch + " to " + lvl, ce);
            }

            creListener.send(new CreateMonsterEvent(ch, lvl, ch.getX(),
                                                    ch.getY()));
        }
    }
}

/**
 * Level builder
 */
public abstract class Builder
{
    /**
     * Build everything needed for the game
     *
     * @param creListener create event listener
     * @param seed random number seed
     *
     * @return new character
     *
     * @throws CoreException if there is a problem
     */
    public static ICharacter build(CreateListener creListener, long seed)
        throws CoreException
    {
        Random random = new Random(seed);

        PlayerCharacter ch = new PlayerCharacter("me", 10, 10, 10, 10);

        Level topLvl = DynamicLevel.createLevel(random, 1, creListener);
        topLvl.enterDown(ch);

        creListener.send(new CreatePlayerEvent(ch, topLvl, ch.getX(),
                                               ch.getY()));

        return ch;
    }
}
