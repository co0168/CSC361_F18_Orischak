package com.mygdx.orischak.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.orischak.game.objects.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;


public class Level 
{
	public static final String TAG = Level.class.getName();

	public Glaceon glaceon;
	public Array<GoldCoin> coins;
	public Array<PlanetCookie> cookies;
	
	/**
	 * Finds the color in the level
	 * and specifies its requirements.
	 * @author Connor
	 *
	 */
	public enum BLOCK_TYPE
	{
		EMPTY(0, 0, 0), // black
		SHELF(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		ITEM_PLANETCOOKIE(255, 0, 255), // purple
		ITEM_GOLD_COIN(255, 255, 0); // yellow

		private int color;
		private BLOCK_TYPE (int r, int g, int b) 
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		public boolean sameColor (int color) 
		{
			return this.color == color;
		}
		public int getColor () 
		{
			return color;
		}
	}

	// objects
	public Array<Shelf> ice;
	// decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	public ParticleEffect snow = new ParticleEffect();
	public Level (String filename)
	{
		init(filename);
		glaceon.body.setTransform(glaceon.position, 0);
	}

	/**
	 * method creates the entire scene using the game 
	 * objects. Most code copied from Canyon bunny
	 * 
	 * @param filename
	 */
	private void init (String filename)
	{
		// player character
		glaceon = null;
		// objects
		ice = new Array<Shelf>();
		coins = new Array<GoldCoin>();
		cookies = new Array<PlanetCookie>();
		
		//particles
		snow.load(Gdx.files.internal("particles/snow.pfxx"), Gdx.files.internal("particles"));
		snow.setPosition(1,5);
        snow.start();
        
		// load image file that represents the level data

		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) 
		{
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					// do nothing
				}
				// rock
				else if (BLOCK_TYPE.SHELF.sameColor(currentPixel))
				{
					if (lastPixel != currentPixel)
					{
						obj = new Shelf();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								* heightIncreaseFactor + offsetHeight);
						ice.add((Shelf)obj);
					} 
					else 
					{
						ice.get(ice.size - 1).increaseLength(1);
					}
				}


				else if
				(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
				{
					obj = new Glaceon();
					offsetHeight = -3.0f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y + offsetHeight );
					glaceon = (Glaceon)obj;
				}
				// Planet cookie
				else if (BLOCK_TYPE.ITEM_PLANETCOOKIE.sameColor(currentPixel))
				{
					obj = new PlanetCookie();
					offsetHeight = -1.5f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y
							+ offsetHeight);
					cookies.add((PlanetCookie)obj);
				}
				// gold coin
				else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
				{
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX,baseHeight * obj.dimension.y
							+ offsetHeight);
					coins.add((GoldCoin)obj);
				}
				// unknown object/pixel color
				else 
				{
					int r = 0xff & (currentPixel >>> 24); //red color channel
					int g = 0xff & (currentPixel >>> 16); //green color channel
					int b = 0xff & (currentPixel >>> 8); //blue color channel
					int a = 0xff & currentPixel; //alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
							+ pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		// decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);
		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}
	public void render (SpriteBatch batch)
	{
		// Draw Mountains
		mountains.render(batch);
		// Draw glaciers
		for (Shelf shelf : ice)
			shelf.render(batch);
		// Draw Water Overlay
		waterOverlay.render(batch);
		// Draw Clouds
		clouds.render(batch);
		// draw coins
		for (GoldCoin coin : coins)
			coin.render(batch);
		// cookies
		for (PlanetCookie cookie : cookies)
			cookie.render(batch);
		// player char
		glaceon.render(batch);
		snow.draw(batch);
	}

	public void update (float deltaTime) 
	{
		glaceon.update(deltaTime);
		for(Shelf shelf : ice)
			shelf.update(deltaTime);
		for(GoldCoin coin : coins)
			coin.update(deltaTime);
		for(PlanetCookie cookie : cookies)
			cookie.update(deltaTime);
		clouds.update(deltaTime);
		snow.update(deltaTime);
		if(snow.isComplete()) snow.start();
		
	}

}
