package com.mygdx.orischak.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.orischak.game.Assets;
import com.badlogic.gdx.Screen;
/**
 * This class manages the main menu screen
 * as well as the buttons it offers
 * @author Connor
 *
 */
public abstract class AbstractGameScreen implements Screen 
{
	protected Game game;


	public AbstractGameScreen (Game game) 
	{
		this.game = game;
	}
	
	public abstract void render (float deltaTime);
	public abstract void resize (int width, int height);
	public abstract void show ();
	public abstract void hide ();
	public abstract void pause ();
	public void resume () 
	{
		Assets.instance.init(new AssetManager());
	}
	public void dispose ()
	{
		Assets.instance.dispose();
	}

}
