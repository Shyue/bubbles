package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HomeScreen implements Screen {

	private Game game;
	private SpriteBatch batch;
	private Stage stage;
	private BitmapFont font;
	private Button playButton;
	private Drawable play, playClicked, playHovered;

	public HomeScreen(Game g) {
		game = g;
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, "Bubbles");

		play = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play.jpg"))));
		playClicked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play-clicked.jpg"))));
		playHovered = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play-hovered.jpg"))));
	}

	@Override
	public void show() {
		ButtonStyle style = new ButtonStyle();
		style.down = playClicked;
		style.up = play;

		stage = new Stage();
		playButton = new Button(style);
		playButton.setBounds(stage.getWidth() / 2 - 50, stage.getHeight() / 2 - 50, 100, 50);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.setScreen(new Bubbles());
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				changeButton(playHovered);
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				changeButton(play);
			}
		});
		stage.addActor(playButton);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, "Bubbles",
				Gdx.graphics.getWidth() / 2 - widthOf("Bubbles") / 2,
				Gdx.graphics.getHeight() / 2 - heightOf("Bubbles") / 2);
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	private void changeButton(Drawable d) {
		playButton.getStyle().up = d;
	}

	private float widthOf(String s) {
		return new GlyphLayout(font, s).width;
	}

	private float heightOf(String s) {
		return new GlyphLayout(font, s).height;
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
