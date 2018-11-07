package com.mygdx.orischak.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.orischak.game.Assets;
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
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasPlanetCookiePowerup = false;
		timeLeftPlanetCookiePowerup = 0;
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

	}

	public boolean hasPlanetCookiePowerup()
	{

	}

	@Override
	public void render(SpriteBatch batch)
	{

	}

}
