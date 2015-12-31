package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class Player extends Actor {

	private static final float RADIUS = 20;

	private Color color;
	private float x, y;

	private ShapeRenderer sr = new ShapeRenderer();

	/**
	 * Makes a new chartreuse player
	 */
	public Player() {
		color = Color.CHARTREUSE;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		x = getStage().getWidth() / 2;
		y = getStage().getHeight() / 2;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.begin(ShapeRenderer.ShapeType.Filled);

		sr.setColor(color);
		sr.circle(x, y, RADIUS);
		sr.end();
	}
}