package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
/**
 * Created by Eyob on 1/13/2016.
 */
public class BubblesMain extends Game {
	public Music music;
	@Override
	public void create() {
		setScreen(new HomeScreen(this));
		music = Gdx.audio.newMusic(Gdx.files.internal("Musick.mp3"));
		music.play();
		music.setLooping(true);
	}
	@Override
	public void pause() {
		music.pause();
	}

	@Override
	public void resume() {
		music.play();
	}
}
