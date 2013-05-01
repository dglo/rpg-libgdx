package org.glowacki.rpg.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;

public class RPG
    implements ApplicationListener
{
    private static final int TILE_WIDTH = 16;
    private static final int TILE_HEIGHT = 16;

    private float speed = 120;

    private GdxView view;
    private GdxControl control;
    private ICharacter player;

    private float time;

    @Override
    public void create()
    {
        view = new GdxView(TILE_WIDTH, TILE_HEIGHT);
        view.setSpeed(speed);

        control = new GdxControl(TILE_WIDTH, TILE_HEIGHT);

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

    public void processArgs(String[] args)
    {
        if (args.length > 0) {
            try {
                float val = Float.parseFloat(args[0]);
                speed = val;
            } catch (NumberFormatException nfe) {
                throw new Error("Bad speed \"" + args[0] + "\"");
            }
        }
    }

    @Override
    public void render()
    {
        float delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());

        time += delta;
        if (time >= 0.1f) {
            time = 0;

            if (control.update(player)) {
                System.out.print("!");
            } else {
                System.out.print(".");
            }
            System.out.flush();
        }

        view.render(player);
    }

    @Override
    public void resize(int width, int height)
    {
        view.resize(width, height);
    }

    @Override
    public void resume()
    {
        System.out.println("Resume is not implemented");
    }
}
