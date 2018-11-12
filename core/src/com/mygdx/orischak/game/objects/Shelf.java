package com.mygdx.orischak.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.orischak.game.Assets;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * A class that is the Shelf which is the platform.
 * @author Connor
 *
 */
public class Shelf extends AbstractGameObject
{
	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	private int length;

	public Shelf()
	{
		init();
	}

	private void init()
	{
		dimension.set(1, 1.5f);
		regEdge = Assets.instance.shelf.edge;
		regMiddle = Assets.instance.shelf.middle;

		// start length of this rock
		setLength(1);
	}

	/**
	 * sets the initial length of the shelf
	 * @param length
	 */
	public void setLength(int length)
	{
		this.length = length;
		// update bounding box for collision
		bounds.set(0, 0, dimension.x, dimension.y);
	}

	/**
	 * increases the length of the shelf.
	 * @param amount
	 */
	public void increaseLength(int amount)
	{
		setLength(length + amount);
	}
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		float relX = 0;
		float relY = 0;
		// Draw left edge
		reg = regEdge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), position.x + relX, position.y +
				relY, origin.x, origin.y, dimension.x / 4, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		// Draw middle
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX, position.y
					+ relY, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
		// draw right edge

		reg = regEdge;
		batch.draw(reg.getTexture(),position.x + relX, position.y +
				relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				true, false);
	}
}
