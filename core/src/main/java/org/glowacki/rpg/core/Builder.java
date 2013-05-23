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
import org.glowacki.core.dungen.CharMap;
import org.glowacki.core.dungen.GeneratorException;
import org.glowacki.core.dungen.Room;
import org.glowacki.core.dungen.RoomGenerator2;
import org.glowacki.core.dungen.SimpleGenerator;
import org.glowacki.core.dungen.Tunneler;
import org.glowacki.rpg.event.CreateMonsterEvent;
import org.glowacki.rpg.event.CreatePlayerEvent;
import org.glowacki.rpg.event.CreateListener;

class DynamicLevel
    extends Level
{
    private static final int maxWidth = 35;
    private static final int maxHeight = 35;
    private static final int gridWidth = 3;
    private static final int gridHeight = 3;

    private static final int maxConnections = 4;
    private static final int maxLevels = 5;

    private Random random;
    private int level;
    private CreateListener creListener;

    DynamicLevel(Random random, int level, Map map, CreateListener creListener)
    {
        super("L" + level, map);

        this.random = random;
        this.level = level;
        this.creListener = creListener;
    }

    static Level OLDcreateLevel(Random random, int num,
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

    static Level createLevel(Random random, int num,
                             CreateListener creListener)
        throws GeneratorException
    {
        CharMap charMap =
            SimpleGenerator.createRooms(random, maxWidth, maxHeight,
                                       gridWidth, gridHeight,
                                       true, num < maxLevels);

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

    public Level getNextLevel()
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
}

public abstract class Builder
{
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

    private static void populate(Level lvl, Random random, int max,
                                 CreateListener creListener)
        throws CoreException
    {
        for (int i = 0; i < max; i++) {
            ComputerCharacter ch = new ComputerCharacter(6, 6, 6, 6,
                                                         random.nextLong());

            ch.setLevel(lvl);

            creListener.send(new CreateMonsterEvent(ch, lvl, ch.getX(),
                                                    ch.getY()));
        }
    }
}
