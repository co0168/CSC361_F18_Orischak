package com.mygdx.orischak.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.orischak.game.Assets;

public class PlanetCookie extends AbstractGameObject 
{
	private TextureRegion cookie;
	public boolean collected;

	public PlanetCookie()
	{
		init();
	}

	private void init () 
	{
		dimension.set(0.5f, 0.5f);
		cookie = Assets.instance.planetCookie.planetCookie;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	public void render (SpriteBatch batch) 
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = cookie;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	public int getScore() 
	{
		return 250;
	}


}
