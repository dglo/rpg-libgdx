package org.glowacki.rpg.core;

import java.util.Random;

import org.glowacki.core.ComputerCharacter;
import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;
import org.glowacki.core.Level;
import org.glowacki.core.LevelException;
import org.glowacki.core.Map;
import org.glowacki.core.MapException;
import org.glowacki.core.PlayerCharacter;
import org.glowacki.core.dungen.Room;
import org.glowacki.core.dungen.RoomGenerator2;
import org.glowacki.core.dungen.Tunneler;

class DynamicLevel
    extends Level
{
    private Random random;
    private int level;

    DynamicLevel(Random random, int level, Map map)
    {
        super("L" + level, map);

        this.random = random;
    }

    public Level getNextLevel()
    {
        if (super.getNextLevel() == null) {
            try {
                addNextLevel(Builder.createLevel(random, level + 1));
            } catch (LevelException le) {
                throw new Error("Cannot add level " + (level + 1), le);
            }
        }

        return super.getNextLevel();
    }
}

public abstract class Builder
{
    private static final int maxWidth = 35;
    private static final int maxHeight = 35;
    private static final int gridWidth = 3;
    private static final int gridHeight = 3;

    private static final int maxLevels = 5;
    private static final int maxConnections = 4;

    public static ICharacter build(long seed)
        throws CoreException
    {
        Random random = new Random(seed);

        PlayerCharacter ch = new PlayerCharacter("me", 10, 10, 10, 10);

        Level topLvl = createLevel(random, 1);
        topLvl.enterDown(ch);

        return ch;
    }

    static Level createLevel(Random random, int num)
    {
        Room[] rooms =
            RoomGenerator2.createRooms(random, maxWidth, maxHeight,
                                       gridWidth, gridHeight);
        RoomGenerator2.addStairs(rooms, random, true, true);

        Tunneler tunneler = new Tunneler(rooms, maxConnections, random);

        String[] strMap = tunneler.dig(maxWidth, maxHeight);

        Map map;
        try {
            map = new Map(strMap);
        } catch (MapException me) {
            throw new Error("Created bad map " + strMap, me);
        }

        DynamicLevel lvl = new DynamicLevel(random, num, map);

        //populate(lvl, random, 2);

        return lvl;
    }

    private static void populate(Level lvl, Random random, int max)
        throws CoreException
    {
        for (int i = 0; i < max; i++) {
            ComputerCharacter ch = new ComputerCharacter(6, 6, 6, 6,
                                                         random.nextLong());
            ch.setLevel(lvl);
        }
    }
}
