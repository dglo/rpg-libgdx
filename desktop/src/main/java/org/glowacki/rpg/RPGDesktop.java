package org.glowacki.rpg;

import org.glowacki.rpg.core.RPG;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class RPGDesktop
{
    /**
     * Process command-line arguments
     *
     * @param args command-line arguments
     */
    private static void processArgs(String[] args, RPG rpg)
    {
        if (args.length > 0) {
            try {
                rpg.setSpeed(Float.parseFloat(args[0]));
            } catch (NumberFormatException nfe) {
                throw new Error("Bad speed \"" + args[0] + "\"");
            }
        }
    }

    public static void main(String[] args)
    {
        RPG rpg = new RPG();
        processArgs(args, rpg);

        LwjglApplicationConfiguration cfg =
            new LwjglApplicationConfiguration();
        cfg.title = "RPG";
        cfg.useGL20 = true;
        cfg.width = 800;
        cfg.height = 480;
        new LwjglApplication(rpg, cfg);
    }
}
