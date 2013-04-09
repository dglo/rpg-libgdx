package org.glowacki.rpg.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.glowacki.core.CoreException;
import org.glowacki.core.ICharacter;
import org.glowacki.core.Level;
import org.glowacki.core.Terrain;
import org.glowacki.core.VisibleMap;

class LevelTextures
{
    private TextureRegion[][] tiles;

    LevelTextures(String name, int tileWidth, int tileHeight)
    {
        Texture tileSheet = new Texture(Gdx.files.internal(name));
        tiles = TextureRegion.split(tileSheet, tileWidth, tileHeight);
    }

    TextureRegion getDoor()
    {
        return tiles[0][4];
    }

    TextureRegion getDownstairs()
    {
        return tiles[0][6];
    }

    TextureRegion getFloor()
    {
        return tiles[0][1];
    }

    TextureRegion getGreyedOut()
    {
        return tiles[0][8];
    }

    TextureRegion getTunnel()
    {
        return tiles[0][2];
    }

    TextureRegion getUnknown()
    {
        return tiles[0][0];
    }

    TextureRegion getUpstairs()
    {
        return tiles[0][5];
    }

    TextureRegion getWall()
    {
        return tiles[0][3];
    }

    TextureRegion getWater()
    {
        return tiles[0][7];
    }
}

public class GdxView
{
    private int tileWidth;
    private int tileHeight;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private LevelTextures textures;
    private TextureRegion[] playerTexture;
    private TextureRegion[] badguyTexture;

    private Level seenLevel;
    private boolean[][] seen;

    GdxView(int tileWidth, int tileHeight)
    {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(),
                                        Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() * 0.5f,
                            Gdx.graphics.getHeight() * 0.5f, 0);

        textures = new LevelTextures("tiles0.png", tileWidth, tileHeight);
        playerTexture = loadCharacter("player.png");
        badguyTexture = loadCharacter("badguy.png");

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0f, 0f,
                                                 Gdx.graphics.getWidth(),
                                                 Gdx.graphics.getHeight());

        camera.update();
    }

    private void drawLevel(ICharacter player)
    {
        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();

        final Level level = player.getLevel();

        if (seenLevel == null || seenLevel != level) {
            seen = player.getSeenArray();
            seenLevel = level;
        }

        final int DISTANCE = 7;

        VisibleMap vmap = new VisibleMap(level.getMap(), true);
        boolean[][] visible = vmap.getVisible(player.getX(), player.getY(),
                                              DISTANCE);

        for (int x = 0; x <= level.getMaxX(); x++) {
            for (int y = 0; y <= level.getMaxY(); y++) {
                Terrain terrain;
                try {
                    terrain = level.getTerrain(x, y);
                } catch (CoreException ce) {
                    ce.printStackTrace();
                    terrain = Terrain.UNKNOWN;
                }

                final int realX = x * tileWidth;
                final int realY = screenHeight - ((y + 1) * tileHeight);

                TextureRegion tr = getTerrainTexture(terrain);
                batch.draw(tr, realX, realY);

                TextureRegion overlay;
                if (visible[x][y]) {
                    if (!seen[x][y]) {
                        seen[x][y] = true;
                    }

                    overlay = null;
                } else if (terrain == Terrain.UNKNOWN) {
                    overlay = null;
                } else if (seen[x][y]) {
                    overlay = textures.getGreyedOut();
                } else {
                    overlay = textures.getUnknown();
                }

                if (overlay != null) {
                    batch.draw(overlay, realX, realY);
                }
            }
        }

        for (ICharacter ch : level.getCharacters()) {
            if (!visible[ch.getX()][ch.getY()]) {
                continue;
            }

            TextureRegion tr;
            if (ch.isPlayer()) {
                tr = playerTexture[0];
            } else {
                tr = badguyTexture[0];
            }

            batch.draw(tr, ch.getX() * tileWidth,
                       screenHeight - ((ch.getY() + 1) * tileHeight));
        }
    }
    private TextureRegion getTerrainTexture(Terrain terrain)
    {
        switch (terrain) {
        case DOOR:
            return textures.getDoor();
        case DOWNSTAIRS:
            return textures.getDownstairs();
        case FLOOR:
            return textures.getFloor();
        case TUNNEL:
            return textures.getTunnel();
        case UPSTAIRS:
            return textures.getUpstairs();
        case WALL:
            return textures.getWall();
        case WATER:
            return textures.getWater();
        default:
            return textures.getUnknown();
        }
    }

    private TextureRegion[] loadCharacter(String name)
    {
        Texture tileSheet = new Texture(Gdx.files.internal(name));
        TextureRegion[][] region = TextureRegion.split(tileSheet, 16, 16);
        if (region == null || region.length == 0) {
            throw new Error("Failed to load " + name);
        } else if (region.length != 1) {
            final String msg = String.format("Expected 1x? from %s, not %dx%d",
                                             name, region.length,
                                             region[0].length);
            throw new Error(msg);
        }

        return region[0];
    }

    public void render(ICharacter player)
    {
        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        batch.begin();
        drawLevel(player);
        batch.end();
    }

    public void resize(int width, int height)
    {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }
}
