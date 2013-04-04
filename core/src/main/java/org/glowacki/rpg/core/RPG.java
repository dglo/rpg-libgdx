package org.glowacki.rpg.core;

import com.badlogic.gdx.ApplicationListener;

import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;

public class RPG
    implements ApplicationListener
{
    private GdxView view;
    private ICharacter player;

    @Override
    public void create()
    {
        view = new GdxView();

        try {
            player = Builder.build(123L);
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
    }

    @Override
    public void dispose()
    {
        System.out.println("Dispose is not implemented");
    }

    @Override
    public void pause()
    {
        System.out.println("Pause is not implemented");
    }

    @Override
    public void render()
    {
        view.render(player);
    }

    @Override
    public void resize(int width, int height)
    {
        view.resize(width, height);
    }

    @Override
    public void resume ()
    {
        System.out.println("Resume is not implemented");
    }
}
