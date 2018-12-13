package com.mygdx.orischak.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.orischak.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
/**
 * This class manages the assets
 * and organizes them.
 * @author Connor
 *
 */
public class Assets implements Disposable, AssetErrorListener 
{

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public AssetGlaceon glaceon;
	public AssetShelf shelf;
	public AssetGoldCoin goldCoin;
	public AssetPlanetCookie planetCookie;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	public Preferences scores;
	// singleton: prevent instantiation from other classes
	private Assets () {}
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
				TextureAtlas.class);
		// load sounds
		assetManager.load("sounds/jump.wav", Sound.class);
		assetManager.load("sounds/jump_with_feather.wav", Sound.class);
		assetManager.load("sounds/pickup_coin.wav", Sound.class);
		assetManager.load("sounds/pickup_feather.wav", Sound.class);
		assetManager.load("sounds/live_lost.wav", Sound.class);
		// load music
		assetManager.load("music/Mt. Coronet (Remastered)  Pokémon Temporal Diamond  Spatial Pearl.mp3",
				Music.class);
		assetManager.load("music/Title Screen - Pokémon Omega Ruby  Alpha Sapphire Music.mp3",
				Music.class);
		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "
				+ assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
		{
			Gdx.app.debug(TAG, "asset: " + a);
		}


		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		// create game resource objects
		fonts = new AssetFonts();
		glaceon = new AssetGlaceon(atlas);
		shelf = new AssetShelf(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		planetCookie = new AssetPlanetCookie(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
		scores = Gdx.app.getPreferences(Constants.SCORES);

	}

	public AssetSounds sounds;
	public AssetMusic music;

	public class AssetSounds 
	{
		public final Sound jump;
		public final Sound jumpWithFeather;
		public final Sound pickupCoin;
		public final Sound pickupFeather;
		public final Sound liveLost;
		public AssetSounds (AssetManager am)
		{
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithFeather = am.get("sounds/jump_with_feather.wav",
					Sound.class);
			pickupCoin = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupFeather = am.get("sounds/pickup_feather.wav",
					Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}
	public class AssetMusic 
	{
		public final Music song01;
		public final Music song02;
		public AssetMusic (AssetManager am)
		{
			song01 = am.get("music/Mt. Coronet (Remastered)  Pokémon Temporal Diamond  Spatial Pearl.mp3",
					Music.class);
			song02 = am.get("music/Title Screen - Pokémon Omega Ruby  Alpha Sapphire Music.mp3", Music.class);
		}
	}
	/**
	 * Finds the glaceon picture in the atlas.
	 * @author Connor
	 *
	 */
	public class AssetGlaceon
	{
		public final AtlasRegion glaceon;

		public AssetGlaceon(TextureAtlas atlas)
		{
			glaceon = atlas.findRegion("glaceon");
		}
	}

	/**
	 * Find the ice rocks.
	 * @author Connor
	 *
	 */
	public class AssetShelf
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;

		public AssetShelf(TextureAtlas atlas)
		{
			edge = atlas.findRegion("ice_edge");
			middle = atlas.findRegion("ice_middle");
		}
	}

	/**
	 * Finds the gold coin
	 * @author Connor
	 *
	 */
	public class AssetGoldCoin
	{
		public final AtlasRegion goldCoin;
		public final Animation animGoldCoin;
		public AssetGoldCoin (TextureAtlas atlas) 
		{
			goldCoin = atlas.findRegion("item_gold_coin");
			// Animation: Gold Coin
			Array<AtlasRegion> regions =
					atlas.findRegions("anim_gold_coin");
			AtlasRegion region = regions.first();
			for (int i = 0; i < 10; i++)
				regions.insert(0, region);
			animGoldCoin = new Animation(1.0f / 20.0f, regions,
					Animation.PlayMode.LOOP_PINGPONG);
		}
	}

	/**
	 * Finds the planet cookie
	 * @author Connor
	 *
	 */
	public class AssetPlanetCookie
	{
		public final AtlasRegion planetCookie;

		public AssetPlanetCookie(TextureAtlas atlas)
		{
			planetCookie = atlas.findRegion("planet_cookie");
		}
	}

	/**
	 * Finds all the level decorations
	 * @author Connor
	 *
	 */
	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		public AssetLevelDecoration (TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
		}
	}

	public class AssetFonts 
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		public AssetFonts () 
		{
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
					Gdx.files.internal("fonts/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("fonts/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal("fonts/arial-15.fnt"), true);
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
					TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	@Override
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '"
				+ asset.fileName + "'", (Exception)throwable);
	}

	@Override
	public void dispose()
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

}







