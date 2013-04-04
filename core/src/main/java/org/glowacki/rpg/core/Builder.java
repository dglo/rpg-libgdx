package org.glowacki.rpg.core;

import java.util.Random;

import org.glowacki.core.ComputerCharacter;
import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;
import org.glowacki.core.Level;
import org.glowacki.core.Map;
import org.glowacki.core.PlayerCharacter;

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

        Level lvl = new Level("Top", new Map(LEVEL_1));
        populate(lvl, random, 2);

        Level l2 = new Level("Middle", new Map(LEVEL_2));
        populate(l2, random, 3);

        lvl.addNextLevel(l2);

        Level l3 = new Level("Bottom", new Map(LEVEL_3));
        populate(l3, random, 4);

        l2.addNextLevel(l3);

        PlayerCharacter ch = new PlayerCharacter("me", 10, 10, 10);
        lvl.enterDown(ch);

        return ch;
    }

    private static void populate(Level lvl, Random random, int max)
        throws CoreException
    {
        for (int i = 0; i < max; i++) {
            ComputerCharacter ch = new ComputerCharacter(6, 6, 6,
                                                         random.nextLong());
            ch.setLevel(lvl);
        }
    }
}
