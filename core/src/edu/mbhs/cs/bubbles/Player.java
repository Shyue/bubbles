package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class Player extends Actor {

	private static final float RADIUS = 20;

	private Color color;
	private float x, y;
	private static float increment = 3;
	private Body body;
	public Body getBody(){
		return body;
	}
	public static final float METERS_TO_PIXELS = 8f;



	private ShapeRenderer sr = new ShapeRenderer();

	/**
	 * Makes a new chartreuse player
	 */
	public Player(World w, float maxWidth, float maxHeight) {
		color = Color.CHARTREUSE;
		x = Gdx.graphics.getWidth() / 2;
		y = Gdx.graphics.getHeight() / 2;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x/METERS_TO_PIXELS, y/METERS_TO_PIXELS);
		body = w.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS/METERS_TO_PIXELS);
		FixtureDef def = new FixtureDef();
		def.shape = shape;
		def.density = 0.005f;
		body.createFixture(def);
		shape.dispose();

	}

	@Override
	public void act(float delta) {
		//super.act(delta);
		//x = getStage().getWidth() / 2;
		//y = getStage().getHeight() / 2;
		move();
	}

	public void move(){
		switch (Gdx.app.getType()){
			case Android:
				break;
			case Desktop:
				if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
					y-=increment;
				}
				if (Gdx.input.isKeyPressed(Input.Keys.UP)){
					y+=increment;
				}
				if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
					x+=increment;
				}
				if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
					x-=increment;
				}
				break;
		}
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
