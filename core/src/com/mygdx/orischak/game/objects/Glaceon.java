package com.mygdx.orischak.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.orischak.game.Assets;
import com.mygdx.orischak.game.WorldController;
import com.mygdx.orischak.util.Constants;

public class Glaceon extends AbstractGameObject
{

	public static final String TAG = Glaceon.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING =
			JUMP_TIME_MAX - 0.018F;

	public enum VIEW_DIRECTION {LEFT, RIGHT}

	public enum JUMP_STATE {GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}

	private TextureRegion regHead;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasPlanetCookiePowerup;
	public float timeLeftPlanetCookiePowerup;
	
	public Fixture playerPhysicsFixture;
	public Fixture playerSensorFixture;
	

	public Glaceon()
	{
		init();
	}

	public void init() 
	{
		dimension.set(1, 1);
		regHead = Assets.instance.glaceon.glaceon;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
//		terminalVelocity.set(3.0f, 4.0f);
//		friction.set(12.0f, 0.0f);
//		acceleration.set(0.0f, -25.0f);
		/**
		 * Box2d stuff for Glaceon
		 */
		bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		Body box = WorldController.world.createBody(bdef);
		
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(0.5f, 0.5f);
		playerPhysicsFixture = box.createFixture(poly, 1);
		poly.dispose();
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasPlanetCookiePowerup = false;
		timeLeftPlanetCookiePowerup = 0;
		body = box;
	}

	public void setJumping(boolean jumpKeyPressed)
	{
		switch (jumpState)
		{
		case GROUNDED: // Character is standing on a platform
			if (jumpKeyPressed) 
			{
				// Start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		case JUMP_RISING: // Rising in the air
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			break;
		case FALLING:// Falling down
		case JUMP_FALLING: // Falling down after jump
			if (jumpKeyPressed && hasPlanetCookiePowerup)
			{
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		}
	}
	public void setPlanetCookiePowerup(boolean pickedUp)
	{
		hasPlanetCookiePowerup = pickedUp;
		if (pickedUp)
		{
			timeLeftPlanetCookiePowerup =
					Constants.ITEM_PLANETCOOKIE_POWERUP_DURATION;
		}
	}

	public boolean hasPlanetCookiePowerup()
	{
		return hasPlanetCookiePowerup && timeLeftPlanetCookiePowerup > 0;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		// Set special color when game object has a feather power-up
		if (hasPlanetCookiePowerup) 
		{
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
		}
		// Draw image
		reg = regHead;
		batch.draw(reg.getTexture(), position.x, position.y, origin.x,
				origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
				false);

		// Reset color to white
		batch.setColor(1, 1, 1, 1);
	}
	@Override
	public void update (float deltaTime) 
	{
		super.update(deltaTime);
		if (velocity.x != 0)
		{
			if (velocity.x < 0) viewDirection = VIEW_DIRECTION.LEFT;
			else viewDirection = VIEW_DIRECTION.RIGHT;
		}
		if (timeLeftPlanetCookiePowerup > 0)
		{
			timeLeftPlanetCookiePowerup -= deltaTime;
			if (timeLeftPlanetCookiePowerup < 0)
			{
				// disable power-up
				timeLeftPlanetCookiePowerup = 0;
				setPlanetCookiePowerup(false);
			}
		}
	}
//	@Override
//	protected void updateMotionY (float deltaTime) 
//	{
//		switch (jumpState) 
//		{
//		case GROUNDED:
//			jumpState = JUMP_STATE.FALLING;
//			break;
//		case JUMP_RISING:
//			// Keep track of jump time
//			timeJumping += deltaTime;
//			// Jump time left?
//			if (timeJumping <= JUMP_TIME_MAX)
//			{
//				// Still jumping
//				velocity.y = terminalVelocity.y;
//			}
//			break;
//		case FALLING:
//			break;
//		case JUMP_FALLING:
//			// Add delta times to track jump time
//			timeJumping += deltaTime;
//			// Jump to minimal height if jump key was pressed too short
//			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
//			{
//				// Still jumping
//				velocity.y = terminalVelocity.y;
//			}
//		}
//		if (jumpState != JUMP_STATE.GROUNDED)
//			super.updateMotionY(deltaTime);
//	}
}