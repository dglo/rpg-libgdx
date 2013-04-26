package org.glowacki.rpg.core;

import java.util.Random;

import org.glowacki.core.ComputerCharacter;
import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;
import org.glowacki.core.Level;
import org.glowacki.core.Map;
import org.glowacki.core.PlayerCharacter;
import org.glowacki.core.dungen.Room;
import org.glowacki.core.dungen.RoomGenerator2;
import org.glowacki.core.dungen.Tunneler;

public abstract class Builder
{
    private static final String[] LEVEL_1 = new String[] {
        "           ---------",
        "           |.......|",
        "           |.......|",
        "           |.......|",
        "           ---+-----",
        "              #",
        "              #",
        "              ###",
        "                #",
        "------          #",
        "|....|          #       ----------",
        "|....+#######   #       |.....>..|",
        "|.<..|      ############+........|",
        "|....|                  |........|",
        "------                  ----------",
    };

    private static final String[] LEVEL_2 = new String[] {
        "----------------------------",
        "|.....................>....|",
        "|..........................|     -----",
        "|..........................|     |...|",
        "|..........................+#####+.<.|",
        "|..........................|     |...|",
        "|..........................|     -----",
        "----------------------------",
    };

    private static final String[] LEVEL_3 = new String[] {
        "-----",
        "|...|",
        "|...----",
        "|......|",
        "|..<...|",
        "|......--------",
        "|.............|",
        "-----.........|",
        "    |.........|",
        "    -----.....|",
        "        |.....|",
        "        |.....|",
        "        -------",
    };

    public static ICharacter build(long seed)
        throws CoreException
    {
        Random random = new Random(seed);

        final int maxWidth = 35;
        final int maxHeight = 35;
        final int gridWidth = 3;
        final int gridHeight = 3;

        final int maxLevels = 5;
        final int maxConnections = 4;

        Level topLvl = null;
        Level prev = null;

        for (int i = 0; i < maxLevels; i++) {
            Room[] rooms =
                RoomGenerator2.createRooms(random, maxWidth, maxHeight,
                                           gridWidth, gridHeight);
            RoomGenerator2.addStairs(rooms, random, true, true);

            Tunneler tunneler = new Tunneler(rooms, maxConnections, random);

            String[] strMap = tunneler.dig(maxWidth, maxHeight);
            Level lvl = new Level("L" + i, new Map(strMap));

            //populate(lvl, random, 2);

            if (prev == null) {
                topLvl = lvl;
            } else {
                prev.addNextLevel(lvl);
            }
            prev = lvl;
        }

        PlayerCharacter ch = new PlayerCharacter("me", 10, 10, 10, 10);
        topLvl.enterDown(ch);

        return ch;
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
