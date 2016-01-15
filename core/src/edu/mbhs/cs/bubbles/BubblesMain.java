package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Game;

public class BubblesMain extends Game {

	public BubblesMain() {
	}

	@Override
	public void create() {
		setScreen(new HomeScreen(this));
	}
}
