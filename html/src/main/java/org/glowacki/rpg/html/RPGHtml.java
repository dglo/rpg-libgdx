package org.glowacki.rpg.html;

import org.glowacki.rpg.core.RPG;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class RPGHtml extends GwtApplication
{
    @Override
    public ApplicationListener getApplicationListener ()
    {
        return new RPG();
    }

    @Override
    public GwtApplicationConfiguration getConfig ()
    {
        return new GwtApplicationConfiguration(480, 320);
    }
}
