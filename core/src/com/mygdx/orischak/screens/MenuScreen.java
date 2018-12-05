package com.mygdx.orischak.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.orischak.game.Assets;
import com.mygdx.orischak.game.Assets.*;
import com.mygdx.orischak.util.Constants;

public class MenuScreen extends AbstractGameScreen 
{
	private Stage stage;
	private Skin skinIceHorse;
	private Skin skinLibgdx;
	//THE ONE AND ONLY PLAY BUTTON
	// Main menu objects
	private Image imgBackground;
	private Button btnMenuPlay;
	//debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	private void rebuildStage()
	{
		skinIceHorse = new Skin(
				Gdx.files.internal(Constants.SKIN_ICEHORSE_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerLogos = buildLogosLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerLogos);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
	}

	private Table buildObjectsLayer () {
		Table layer = new Table();
		return layer;
	}
	private Table buildLogosLayer () {
		Table layer = new Table();
		return layer;
	}
	private Table buildControlsLayer ()
	{
		Table layer = new Table();
		layer.right().bottom();

		// Add play button
		btnMenuPlay = new Button(skinIceHorse, "play");
		layer.add(btnMenuPlay);

		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) 
			{
				onPlayClicked();
			}
		});
		layer.row();

//		// Add options button
//		btnMenuOptions = new Button(skinIceHorse, "options");
//		layer.add(btnMenuOptions);
//
//		btnMenuOptions.addListener(new ChangeListener() {
//			@Override
//			public void changed (ChangeEvent event, Actor actor)
//			{
//				onOptionsClicked();
//			}
//		});

		if (debugEnabled)
			layer.debug();

		return layer;
	}
	private Table buildOptionsWindowLayer () {
		Table layer = new Table();
		return layer;
	}

	/**
	 * Begins the game.
	 */
	private void onPlayClicked () 
	{
		game.setScreen(new GameScreen(game));
		Assets.instance.music.song02.stop();
		Assets.instance.music.song01.play();
	}
	private static final String TAG = MenuScreen.class.getName();

	public MenuScreen (Game game)
	{
		super(game);
	}

	@Override
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (debugEnabled)
		{
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) 
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
	}

	private Table buildBackgroundLayer () 
	{
		Table layer = new Table();
		
		// + Background
		imgBackground = new Image(skinIceHorse, "background");
		layer.add(imgBackground);
		
		return layer;
	}
	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() 
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}

	@Override
	public void hide() 
	{
		stage.dispose();
		skinIceHorse.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

}
