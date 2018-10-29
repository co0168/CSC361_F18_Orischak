package com.mygdx.orischak;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.mygdx.orischak.game.WorldController;
import com.mygdx.orischak.game.WorldRenderer;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MainGame implements ApplicationListener 
{
	private static final String TAG = MainGame.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	@Override
	public void create() 
	{
		//set libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);

	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render() 
	{
		worldController.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		worldRenderer.render();
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub

	}

}
