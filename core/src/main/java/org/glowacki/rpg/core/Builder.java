package org.glowacki.rpg.core;

import java.util.Random;

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

        Level l2 = new Level("Middle", new Map(LEVEL_2));
        lvl.addNextLevel(l2);

        Level l3 = new Level("Bottom", new Map(LEVEL_3));
        l2.addNextLevel(l3);

        PlayerCharacter ch = new PlayerCharacter("me", 10, 10, 10);
        lvl.enterDown(ch);

        return ch;
    }
}
