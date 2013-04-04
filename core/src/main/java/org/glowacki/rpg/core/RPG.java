package org.glowacki.rpg.core;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class RPG
    implements ApplicationListener
{
    private static final int SCREEN_WIDTH = 320;
    private static final int SCREEN_HEIGHT = 480;

    private OrthographicCamera camera;
    private Texture floor;
    private SpriteBatch batch;

    @Override
    public void create()
    {
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH * 0.5f, SCREEN_HEIGHT * 0.5f, 0);

        floor = new Texture(Gdx.files.internal("floor.png"));
System.out.format("Floor %s is %dx%d%s", floor, floor.getWidth(), floor.getHeight(), (floor.isManaged() ? "" : " !!NOT MANAGED!!"));
        batch = new SpriteBatch();
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
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(floor, 0, 0);
        batch.draw(floor, 18, 0);
        batch.draw(floor, 0, 18);
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        System.out.println("Resize is not implemented");
    }

    @Override
    public void resume ()
    {
        System.out.println("Resume is not implemented");
    }
}
