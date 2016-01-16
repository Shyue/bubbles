package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Game;

/**
 * Created by Eyob on 1/13/2016.
 */
public class BubblesMain extends Game {

	@Override
	public void create() {
		setScreen(new HomeScreen(this));
	}
}
