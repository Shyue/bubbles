package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by Eyob on 1/13/2016.
 */
public class BubblesMain extends Game {

	public BubblesMain() {
	}

	@Override
	public void create() {
		setScreen(new HomeScreen(this));
	}
}
