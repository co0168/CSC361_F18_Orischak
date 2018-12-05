package com.mygdx.orischak.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.orischak.game.objects.AbstractGameObject;

/**
 * A class that allows the character to be followed by a 
 * camera and allows the user to switch between 
 * camera follow and free cam.
 * @author Connor Orischak
 *
 */
public class CameraHelper 
{
	private static final String TAG = CameraHelper.class.getName();
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	private final float FOLLOW_SPEED = 4.0f;
	private Vector2 position;
	private float zoom;

	private AbstractGameObject target;
	/**
	 * sets to default zoom
	 */
	public CameraHelper () 
	{
		position = new Vector2();
		zoom = 1.0f;
	}
	/**
	 * gets the position when the 
	 * target moves
	 * @param deltaTime
	 */
	public void update (float deltaTime) 
	{
		if (!hasTarget()) return;
		position.lerp(target.position, FOLLOW_SPEED * deltaTime);
		position.y = Math.max(-1f, position.y);
	}
	
	/**
	 * sets a new position
	 * @param x
	 * @param y
	 */
	public void setPosition (float x, float y) 
	{
		this.position.set(x, y);
	}
	
	/**
	 * 
	 * @return position - the camera's position
	 */
	public Vector2 getPosition ()
	{ 
		return position;
	}
	
	/**
	 * allows the camer to be zoomed in
	 * @param amount
	 */
	public void addZoom (float amount) 
	{ 
		setZoom(zoom + amount);
	}
	public void setZoom (float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	public float getZoom () 
	{ 
		return zoom; 
	}
	public void setTarget (AbstractGameObject target) 
	{ 
		this.target = target; 
	}
	public AbstractGameObject getTarget () 
	{ 
		return target; 
	}
	public boolean hasTarget () 
	{ 
		return target != null;
	}
	public boolean hasTarget (AbstractGameObject target) 
	{
		return hasTarget() && this.target.equals(target);
	}
	public void applyTo (OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
}
