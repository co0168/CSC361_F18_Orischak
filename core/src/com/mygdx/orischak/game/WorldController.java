package com.mygdx.orischak.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.orischak.util.CameraHelper;
import com.mygdx.orischak.game.objects.Shelf;
import com.mygdx.orischak.util.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.orischak.game.objects.Glaceon.MOVE_STATE;
import com.mygdx.orischak.game.objects.Glaceon.VIEW_DIRECTION;
import com.mygdx.orischak.game.objects.*;
import com.badlogic.gdx.Game;
import com.mygdx.orischak.screens.MenuScreen;
import com.mygdx.orischak.util.AudioManager;
/**
 * This class allows the player to use computer
 * controls to control the main characters movement
 * as well as handle physics of game objects.
 * @author co0168
 *
 */
public class WorldController extends InputAdapter
{

	/**
	 * Variables to test movement of objects
	 */
	//	public Sprite[] testSprites;
	//	public int selectedSprite;
	public CameraHelper cameraHelper; 
	// new vars
	public Level level;
	public int lives;
	public int score;
	public int[] scores;
	public int turn;
	public final int COIN_SCORE = 100;
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	public float livesVisual;
	public float scoreVisual;

	//Box2D world
	public static World world;
	private Game game;

	public WorldController (Game game)
	{
		this.game = game;
		init();
	}


	private void backToMenu()
	{
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
		Assets.instance.music.song01.stop();
		Assets.instance.music.song02.play();
	}
	public boolean isGameOver()
	{
		return lives < 0;
	}
	public boolean isPlayerInWater()
	{
		return level.glaceon.position.y < -5;
	}

	private void initPhysics()
	{

		// Rocks
		Vector2 origin = new Vector2();
		for (Shelf shelf : level.ice)
		{
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(shelf.position);
			Body body = world.createBody(bodyDef);
			shelf.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = shelf.bounds.width / 2.0f;
			origin.y = shelf.bounds.height / 2.0f;
			polygonShape.setAsBox(shelf.bounds.width / 2.0f,
					shelf.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
	}
	/**
	 * Changes glaceons jump state when he jumps or touches the ice.
	 * @param shelf
	 */

	//	private void onCollisionGlaceonWithIce(Shelf shelf) 
	//	{
	//		Glaceon g = level.glaceon;
	//		float heightDif = Math.abs(g.position.y - (shelf.position.y +
	//				shelf.bounds.height));
	//		if (heightDif > 0.25f)
	//		{
	//			boolean hitRightEdge = g.position.x > (shelf.position.x +
	//					shelf.bounds.width/2.0f);
	//			if (hitRightEdge)
	//			{
	//				g.position.x = shelf.position.x + shelf.bounds.width;
	//			}
	//			else
	//			{
	//				g.position.x = shelf.position.x - g.bounds.width;
	//			}
	//			return;
	//		}
	//		switch (g.jumpState)
	//		{
	//		case GROUNDED:
	//			break;
	//		case FALLING:
	//		case JUMP_FALLING:
	//			g.position.y = shelf.position.y+g.bounds.height + g.origin.y;
	//			break;
	//		case JUMP_RISING:
	//			g.position.y = shelf.position.y +
	//			g.bounds.height + g.origin.y;
	//			break;
	//		}
	//	}
	private void onCollisionGlaceonWithGoldCoin(GoldCoin coin)
	{
		coin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		if (level.glaceon.hasPlanetCookiePowerup) doubleScore();
		else score += coin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}

	private void onCollisionGlaceonWithPlanetCookie(PlanetCookie cookie)
	{
		cookie.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += cookie.getScore();
		level.glaceon.setPlanetCookiePowerup(true);
		Gdx.app.log(TAG, "Planet cookie collected");
	}

	/**
	 * This method checks to see if glaceon
	 * has the planet cookie powerup and doubles
	 * the score for a short time.
	 */
	public void doubleScore()
	{
		score += 2*(COIN_SCORE);
	}
	private void testCollisions () 
	{
		r1.set(level.glaceon.position.x, level.glaceon.position.y,
				level.glaceon.bounds.width, level.glaceon.bounds.height);
		// Test collision: Glaceon <-> Gold Coins
		for (GoldCoin goldcoin : level.coins) 
		{
			if (goldcoin.collected) continue;
			r2.set(goldcoin.position.x, goldcoin.position.y,
					goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionGlaceonWithGoldCoin(goldcoin);
			break;
		}
		// Test collision: Glaceon <-> Planet Cookies
		for (PlanetCookie cookie : level.cookies)
		{
			if (cookie.collected) continue;
			r2.set(cookie.position.x, cookie.position.y,
					cookie.bounds.width, cookie.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionGlaceonWithPlanetCookie(cookie);
			break;
		}
	}

	private void initLevel()
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.glaceon);
		initPhysics();
	}

	private static final String TAG = WorldController.class.getName();



	/**
	 * creates test objects and sets the input processor
	 * as this class as it contains the logic to control the 
	 * sprites.
	 */
	private void init()
	{
		world = new World(new Vector2(0,-15f), true);
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		scores = new int[lives+1];
		initLevel();
	}

	/**
	 * creates test objects to make sure we can move them around
	 */


	/**
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		handleInputGame(deltaTime);
		world.step(deltaTime, 8, 3);


		if (isGameOver())
		{

		}
		else
		{
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (isPlayerInWater())
		{
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			scores[turn] = score;
			turn++;
			initLevel();
		}
		//	level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual> lives)
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		if (scoreVisual< score)
			scoreVisual = Math.min(score, scoreVisual
					+ 250 * deltaTime);
	}

	/**
	 * gets the highest score of over all attempts.
	 * @param a
	 * @return
	 */
	public int getHighScore(int[] a)
	{
		int highScore = 0;
		for (int i=0;i<a.length;i++)
		{
			if (a[i]>highScore) highScore = a[i];
		}
		return highScore;
	}
	/**
	 * 
	 * @param deltaTime
	 */



	private void handleDebugInput (float deltaTime) 
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		// Selected Sprite Controls
		if (!cameraHelper.hasTarget(level.glaceon))
		{
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=
					camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,
					0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,
					0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,
					-camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
			// Camera Controls (zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *=
					camZoomSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.COMMA))
				cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(
					-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
		}

	}

	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}


	@Override
	public boolean keyUp (int keycode) 
	{
		// Reset game world
		if (keycode == Keys.R) 
		{
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) 
		{

			cameraHelper.setTarget(cameraHelper.hasTarget()
					? null: level.glaceon);
			Gdx.app.debug(TAG, "Camera follow enabled: "
					+ cameraHelper.hasTarget());
		}
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) 
			backToMenu();

		return false;
	}
	/**
	 * uses move states to decide how to apply force to
	 * glaceon using the applyForce() from Box2d
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime) 
	{
		Glaceon g = level.glaceon;
		Vector2 vel = g.body.getLinearVelocity();
		float force = 0;
		level.glaceon.move_state = MOVE_STATE.STOP;
		if (cameraHelper.hasTarget(level.glaceon)) 
		{
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				g.move_state = MOVE_STATE.LEFT;
				g.viewDirection = VIEW_DIRECTION.LEFT;
			} 
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				g.move_state = MOVE_STATE.RIGHT;
				g.viewDirection = VIEW_DIRECTION.RIGHT;
			} 
			switch(g.move_state)
			{
			case LEFT:
				if (vel.x > -5) force = -20; break;
			case STOP:
				force = vel.x * -10; break;
			case RIGHT:
				if (vel.x < 5) force = 20; break;
			}
			g.body.applyForce(new Vector2(force, 0), g.body.getWorldCenter(), true);
		}
		// Glaceon jump
		if(Gdx.input.isKeyPressed(Keys.SPACE))
		{
			if(!g.isJumping() && vel.y == 0) // disables multiple jumps.
			{
				AudioManager.instance.play(Assets.instance.sounds.jump);
				g.body.applyLinearImpulse(new Vector2(0,6), g.body.getWorldCenter(), true);
				g.isJumping = true;
			}
		} 
		else 
		{
			g.setJumping(false);
		}
	}
}

