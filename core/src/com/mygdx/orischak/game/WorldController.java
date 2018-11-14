package com.mygdx.orischak.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.orischak.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
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
import com.mygdx.orischak.game.objects.Glaceon.JUMP_STATE;
import com.mygdx.orischak.game.objects.Glaceon.MOVE_STATE;
import com.mygdx.orischak.game.objects.*;
/**
 * This class allows the player to use computer
 * controlls to control the main characters movement
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
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	public static World world;


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

	private void onCollisionGlaceonWithIce(Shelf shelf) 
	{
		Glaceon g = level.glaceon;
		float heightDif = Math.abs(g.position.y - (shelf.position.y +
				shelf.bounds.height));
		if (heightDif > 0.25f)
		{
			boolean hitRightEdge = g.position.x > (shelf.position.x +
					shelf.bounds.width/2.0f);
			if (hitRightEdge)
			{
				g.position.x = shelf.position.x + shelf.bounds.width;
			}
			else
			{
				g.position.x = shelf.position.x - g.bounds.width;
			}
			return;
		}
		switch (g.jumpState)
		{
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			g.position.y = shelf.position.y+g.bounds.height + g.origin.y;
			break;
		case JUMP_RISING:
			g.position.y = shelf.position.y +
			g.bounds.height + g.origin.y;
			break;
		}
	}
	private void onCollisionGlaceonWithGoldCoin(GoldCoin coin)
	{
		coin.collected = true;
		score += coin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}

	private void onCollisionGlaceonWithPlanetCookie(PlanetCookie cookie)
	{
		cookie.collected = true;
		score += cookie.getScore();
		level.glaceon.setPlanetCookiePowerup(false);
		Gdx.app.log(TAG, "Planet cookie collected");
	}

	private void testCollisions () 
	{
		r1.set(level.glaceon.position.x, level.glaceon.position.y,
				level.glaceon.bounds.width, level.glaceon.bounds.height);
		// Test collision: Glaceon <-> Rocks
		for (Shelf shelf : level.ice) 
		{
			r2.set(shelf.position.x, shelf.position.y, shelf.bounds.width,
					shelf.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionGlaceonWithIce(shelf);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}
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
	 * Constructor of the WordController
	 */
	public WorldController()
	{
		init();
	}

	/**
	 * creates test objects and sets the input processor
	 * as this class as it contains the logic to control the 
	 * sprites.
	 */
	private void init()
	{
		world = new World(new Vector2(0,-9.81f), true);
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
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

		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
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

		return false;
	}
	/**
	 * allows the bunny to move
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime) 
	{
		if (cameraHelper.hasTarget(level.glaceon)) 
		{
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.glaceon.move_state = MOVE_STATE.LEFT;
				level.glaceon.body.applyForce(new Vector2(-10,0), level.glaceon.body.getWorldCenter(), true);
			} 
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.glaceon.move_state = MOVE_STATE.RIGHT;
				level.glaceon.body.applyForce(new Vector2(10,0), level.glaceon.body.getWorldCenter(), true);
			} 
			else if (Gdx.input.isKeyPressed(Keys.DOWN))
			{
				level.glaceon.move_state = MOVE_STATE.STOP;
				
			}
			// Bunny Jump
			if (Gdx.input.isTouched() ||
					Gdx.input.isKeyPressed(Keys.SPACE))
			{
				level.glaceon.body.applyLinearImpulse(new Vector2(0,1), level.glaceon.body.getWorldCenter(), true);
			} 
			else 
			{
				level.glaceon.setJumping(false);
			}
		}
	}
}
