package com.mygdx.orischak.game;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.orischak.util.Constants;
/**
 * 
 * @author co0168
 *
 */
public class WorldRenderer implements Disposable
{
	private static final boolean DEBUG_BOX2D = true;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private Box2DDebugRenderer debug;

	public WorldRenderer (WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}

	private void renderWorld (SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		
		if (DEBUG_BOX2D)
		{
			debug.render(WorldController.world, camera.combined);
		}
	}

	private void init()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
				Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		debug = new Box2DDebugRenderer();
	}

	public void render()
	{
		renderWorld(batch);
	}

//	private void renderTestObjects()
//	{
//		worldController.cameraHelper.applyTo(camera);
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		for(Sprite sprite : worldController.testSprites)
//		{
//			sprite.draw(batch);
//		}
//		batch.end();
//	}
	public void resize(int width, int height)
	{
		camera.viewportWidth = 
				(Constants.VIEWPORT_HEIGHT / height) *
				width;
		camera.update();
	}
	@Override
	public void dispose()
	{
		batch.dispose();
	}

}
