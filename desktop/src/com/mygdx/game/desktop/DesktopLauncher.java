package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.orischak.MainGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher 
{
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;

	public static void main (String[] arg) 
	{
		if (rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "icehorse");
			TexturePacker.process(settings, "assets-raw/images-ui", "../core/assets/images", "icehorse-ui");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ice Horse";
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new MainGame(), config);
	}
}
