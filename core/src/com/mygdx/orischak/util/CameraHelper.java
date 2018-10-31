package com.mygdx.orischak.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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
	private Vector2 position;
	private float zoom;

	private Sprite target;
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
		position.x = target.getX() + target.getOriginX();
		position.y = target.getY() + target.getOriginY();
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
	public void setTarget (Sprite target) 
	{ 
		this.target = target; 
	}
	public Sprite getTarget () 
	{ 
		return target; 
	}
	public boolean hasTarget () 
	{ 
		return target != null;
	}
	public boolean hasTarget (Sprite target) 
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
