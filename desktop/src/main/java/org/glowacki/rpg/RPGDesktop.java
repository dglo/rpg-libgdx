package org.glowacki.rpg.java;

import org.glowacki.rpg.core.RPG;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class RPGDesktop
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration cfg =
            new LwjglApplicationConfiguration();
        cfg.title = "RPG";
        cfg.useGL20 = true;
        cfg.width = 800;
        cfg.height = 480;
        new LwjglApplication(new RPG(), cfg);
    }
}
