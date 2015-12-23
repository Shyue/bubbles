package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Main Application: Bubbles Game
 */
public class Bubbles extends ApplicationAdapter {
	private static int BUBBLE_NUMBER = 20;
	private Bubble[] b = new Bubble[BUBBLE_NUMBER];
	private Stage stage;

	@Override
	public void create () {
		stage = new Stage();
		Gdx.app.log("AssetPath", Gdx.files.internal("assets/bubble.jpg").file().getAbsolutePath());
		for(int i = 0; i < 20; i++){
			b[i] = new Bubble();
			stage.addActor(b[i]);
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
	}
}
