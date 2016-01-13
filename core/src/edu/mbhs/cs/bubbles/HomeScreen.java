package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Eyob on 1/13/2016.
 */
public class HomeScreen implements Screen {

	private Game game;
	private SpriteBatch batch;
	private Stage stage;
	private BitmapFont font;
	private GlyphLayout layout;
	private TextButtonStyle textButtonStyle;

	public HomeScreen(Game g) {
		game = g;
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		layout = new GlyphLayout();
		layout.setText(font, "Bubbles");
	}

	@Override
	public void show() {
		stage = new Stage();
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		TextButton b = new TextButton("play", textButtonStyle);
		b.setSize(50, 50);
		b.setBounds(stage.getWidth() / 2 - widthOf("play") / 2, stage.getHeight() / 2 - 50, widthOf("play"), 50);
		System.out.println(widthOf("play"));
		b.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.setScreen(new Bubbles());
			}
		});
		stage.addActor(b);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		batch.begin();
		font.draw(batch, "Bubbles", Gdx.graphics.getWidth() / 2 - widthOf("Bubbles") / 2,
				Gdx.graphics.getHeight() / 2 + 50);
		batch.end();
		stage.draw();
	}

	private float widthOf(String s) {
		GlyphLayout dummy = new GlyphLayout();
		dummy.setText(font, s);
		return dummy.width;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
