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
	private static final boolean DEBUG_BOX2D = false;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private Box2DDebugRenderer debug;
	private OrthographicCamera cameraGUI;

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
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		debug = new Box2DDebugRenderer();
	}

	public void render()
	{
		renderWorld(batch);
		renderGui(batch);
	}


	public void resize(int width, int height)
	{
		camera.viewportWidth = 
				(Constants.VIEWPORT_HEIGHT / height) *
				width;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
				/ (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2,
				cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}
	@Override
	public void dispose()
	{
		batch.dispose();
	}

	/**
	 * calls methods that render the amount
	 * of lives left and the current score.
	 * @param batch
	 */
	private void renderGui (SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		// draw collected gold coins icon + text
		// (anchored to top left edge)
		renderGuiScore(batch);
		// draw extra lives icon + text (anchored to top right edge)
		renderGuiExtraLive(batch);
		// draw the time left on the planet cookie
		renderGuiPlanetCookiePowerup(batch);
		batch.end();
	}

	private void renderGuiScore(SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, .35f, -.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x+75, y+37);
	}

	private void renderGuiPlanetCookiePowerup (SpriteBatch batch) {
		float x = -15;
		float y = 30;
		float timeLeftCookiePowerup =
				worldController.level.glaceon.timeLeftPlanetCookiePowerup;
		if (timeLeftCookiePowerup > 0) {
			// Start icon fade in/out if the left power-up time
			// is less than 4 seconds. The fade interval is set
			// to 5 changes per second.
			if (timeLeftCookiePowerup < 4) {
				if (((int)(timeLeftCookiePowerup * 5) % 2) != 0) {
					batch.setColor(1, 1, 1, 0.5f);
				}
			}
			batch.draw(Assets.instance.planetCookie.planetCookie,
					x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
			Assets.instance.fonts.defaultSmall.draw(batch,
					"" + (int)timeLeftCookiePowerup, x + 60, y + 57);
		}
	}
	private void renderGuiExtraLive (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 50 -
				Constants.LIVES_START * 50;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++) {
			if (worldController.lives <= i)
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			batch.draw(Assets.instance.glaceon.glaceon,
					x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}
}
