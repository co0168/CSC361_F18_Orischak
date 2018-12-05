package com.mygdx.orischak.game.objects;

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

	public enum VIEW_DIRECTION {LEFT, RIGHT}

	public enum JUMP_STATE {GROUNDED, JUMPING}
	
	public enum MOVE_STATE {LEFT, RIGHT, STOP}

	private TextureRegion regHead;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasPlanetCookiePowerup;
	public float timeLeftPlanetCookiePowerup;
	
	public Fixture playerPhysicsFixture;
	public Fixture playerSensorFixture;
	
	public MOVE_STATE move_state;
	public boolean isJumping;

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

		/**
		 * Box2d stuff for Glaceon
		 */
		bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		Body box = WorldController.world.createBody(bdef);
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(.5f, .5f, origin, 0);
		box.setFixedRotation(true);
		playerPhysicsFixture = box.createFixture(poly, 1);
		poly.dispose();
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		isJumping = false;
		jumpState = JUMP_STATE.GROUNDED;
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
				jumpState = JUMP_STATE.JUMPING;
				isJumping = true;
			}
		case JUMPING:
			if (!jumpKeyPressed)
			{
				jumpState = JUMP_STATE.GROUNDED;
				isJumping = false;
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
//		if (hasPlanetCookiePowerup) 
//		{
//			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
//		}
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
	
	public boolean isJumping()
	{
		return isJumping;
		
	}

}
