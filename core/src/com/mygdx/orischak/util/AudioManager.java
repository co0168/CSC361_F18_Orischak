package com.mygdx.orischak.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager 
{
	public static final AudioManager instance = new AudioManager();
	private Music playingMusic;
	// singleton: prevent instantiation from other classes
	private AudioManager () { }
	public void play (Sound sound)
	{
		sound.play();
	}

	public void play (Music music)
	{
		stopMusic();
		playingMusic = music;

		music.setLooping(true);

		music.play();

	}
	public void stopMusic () 
	{
		if (playingMusic != null) playingMusic.stop();
	}
}