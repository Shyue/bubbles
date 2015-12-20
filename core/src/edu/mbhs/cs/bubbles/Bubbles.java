package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Bubbles extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private static int BUBBLE_NUMBER = 20;
	private Bubble[] b = new Bubble[BUBBLE_NUMBER];
	private Stage stage;
	@Override
	public void create () {
		stage = new Stage();
	//	batch = new SpriteBatch();
		Gdx.app.log("AssetPath", Gdx.files.internal("assets/bubble.jpg").file().getAbsolutePath());
		//img = new Texture("badlogic.jpg");
		for(int i = 0; i<20; i++){
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
