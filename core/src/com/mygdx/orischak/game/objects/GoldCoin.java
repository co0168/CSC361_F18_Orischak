package com.mygdx.orischak.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.orischak.game.Assets;

public class GoldCoin extends AbstractGameObject 
{

	private TextureRegion regGoldCoin;
	public boolean collected;

	public GoldCoin()
	{
		init();
	}

	private void init () 
	{
		dimension.set(0.5f, 0.5f);
		regGoldCoin = Assets.instance.goldCoin.goldCoin;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	@Override
	public void render(SpriteBatch batch)
	{

	}

}
