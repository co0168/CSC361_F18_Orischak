package com.mygdx.orischak.game;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.orischak.util.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
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
	private Color highScoreColor = Color.BLUE;
	private Color exitColor = Color.CHARTREUSE;
	private int index;

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
	private void renderGui (SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		// draw collected gold coins icon + text
		// (anchored to top left edge)
		renderGuiScore(batch);
		// draw extra lives icon + text (anchored to top right edge)
		renderGuiExtraLive(batch);
		// draw the time left on the planet cookie
		renderGuiPlanetCookiePowerup(batch);
		// draw the game over text when the game is over.
		renderGuiGameOverMessage(batch);
		batch.end();
	}

	private void renderGuiGameOverMessage (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth / 2;
		float y = cameraGUI.viewportHeight / 2;
		int nextLine = 40;
		int start = 75;
		if (worldController.isGameOver()) 
		{
			for (int i=0;i<worldController.turn;i++)
			{
				Assets.instance.scores.putInteger("hs" + (1+i), worldController.scores[i]);
			}

			//			Assets.instance.scores.putInteger("hs1", 100);
			//			Assets.instance.scores.putInteger("hs2", 200);
			//			Assets.instance.scores.putInteger("hs3", 300);
			Assets.instance.scores.flush();
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(1, 0.75f, 0.25f, 1);
			fontGameOver.draw(batch, "GAME OVER", x, start, 0,
					Align.center, false);
			//			int lowestScore = findLowestScore();
			//			if (worldController.getHighScore(worldController.scores)>lowestScore)
			//			{
			//				Assets.instance.scores.putInteger("hs"+(index), 
			//						worldController.getHighScore(worldController.scores));	
			//				Assets.instance.scores.flush();
			//
			//			}
			fontGameOver.setColor(1, 1, 1, 1);
			for (int j=0;j<4;j++)
			{
				fontGameOver.setColor(1, 1, 1, 1);
				fontGameOver.draw(batch, "Round "+(j+1)+": " + Assets.instance.scores.getInteger("hs"+(1+j)), x, start+nextLine, 
						0, Align.center, false);
				start+=nextLine;
			}
			fontGameOver.setColor(highScoreColor);
			fontGameOver.draw(batch, "YOUR HIGH SCORE: " + worldController.getHighScore(worldController.scores),
					x, start+nextLine, 0, Align.center, false);
			fontGameOver.setColor(exitColor);
			fontGameOver.draw(batch, "Press ESC to return to the main menu.",
					x, start+2*nextLine, 0, Align.center, false);
			fontGameOver.setColor(1, 1, 1, 1);

		}
	}
	public int findLowestScore()
	{
		//index = 0;
		int lowest = 100000;
		for (int i=0;i<3;i++)
		{
			if (Assets.instance.scores.getInteger("hs" +(1+i)) < lowest) 
			{	
				lowest = Assets.instance.scores.getInteger("hs" +(1+i));
				index = i+1;
			}
		}
		return lowest;
	}

	private void renderGuiScore(SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, .35f, -.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x+75, y+37);
	}

	/**
	 * shows the amount of time left on the planet cookie power up
	 * @param batch
	 */
	private void renderGuiPlanetCookiePowerup (SpriteBatch batch)
	{
		float x = -15;
		float y = 30;
		float timeLeftCookiePowerup =
				worldController.level.glaceon.timeLeftPlanetCookiePowerup;
		if (timeLeftCookiePowerup > 0)
		{
			// Start icon fade in/out if the left power-up time
			// is less than 4 seconds. The fade interval is set
			// to 5 changes per second.
			if (timeLeftCookiePowerup < 4) 
			{
				if (((int)(timeLeftCookiePowerup * 5) % 2) != 0) 
				{
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
	private void renderGuiExtraLive (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 -
				Constants.LIVES_START * 50;
		float y = -15;
		for (int i = 0; i < Constants.LIVES_START; i++)
		{
			if (worldController.lives <= i)
				batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
			batch.draw(Assets.instance.glaceon.glaceon,
					x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
			batch.setColor(1, 1, 1, 1);
		}
		if (worldController.lives>= 0
				&&worldController.livesVisual>worldController.lives) {
			int i = worldController.lives;
			float alphaColor = Math.max(0, worldController.livesVisual
					- worldController.lives - 0.5f);
			float alphaScale = 0.35f * (2 + worldController.lives
					- worldController.livesVisual) * 2;
			float alphaRotate = -45 * alphaColor;
			batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
			batch.draw(Assets.instance.glaceon.glaceon,
					x + i * 50, y, 50, 50, 120, 100, alphaScale, -alphaScale,
					alphaRotate);
			batch.setColor(1, 1, 1, 1);
		}
	}
}
