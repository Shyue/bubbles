package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;

/**
 * Class representing the player, a small circle in the sea of bubbles
 */
public class PlayerAI extends Actor {

	public static final float RADIUS = 5f;
	public static final float METERS_TO_PIXELS = Bubble.METERS_TO_PIXELS;

	private static final float BASE_SPEED = 900;

	private float x, y;
	private Body body;
	private Fixture fixture;
	private TextureRegion texture = new TextureRegion();

	/**
	 * Makes a new player
	 */
	public PlayerAI(World w) {
		texture.setTexture(new Texture(Gdx.files.internal("mandleplayer.png")));
		texture.setRegion(texture.getTexture());

		x = Gdx.graphics.getWidth() / 2;
		y = Gdx.graphics.getHeight() / 2;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x/ METERS_TO_PIXELS, y/ METERS_TO_PIXELS);
		body = w.createBody(bodyDef);

		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);
		FixtureDef def = new FixtureDef();
		def.shape = shape;
		def.density = 0.09f;
		def.restitution = 0.77f;
		def.friction = 0.8f;

		fixture = body.createFixture(def);
		fixture.setUserData(Arrays.asList("Player", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		shape.dispose();
	}

	@Override
	public void act(float delta) {
		fixture.setUserData(Arrays.asList("Player", String.valueOf(body.getPosition().x), String.valueOf(body.getPosition().y)));
		updateBounds();
	}

	public Body getBody() {
		return body;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(
				texture,
				(body.getPosition().x - RADIUS) * METERS_TO_PIXELS,
				(body.getPosition().y - RADIUS) * METERS_TO_PIXELS,
				518f / texture.getRegionWidth()* RADIUS * METERS_TO_PIXELS,
				(686f - 340f) / texture.getRegionHeight() * RADIUS * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				RADIUS * 2 * METERS_TO_PIXELS,
				1f,
				texture.getRegionHeight() / texture.getRegionWidth(),
				body.getLinearVelocity().angle() - 90,
				true
		);
	}

	private void updateBounds() {
		this.setBounds(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}

	public Rectangle getBounds(){
		return new Rectangle(x, y, RADIUS * 2 * METERS_TO_PIXELS, RADIUS * 2 * METERS_TO_PIXELS);
	}
}
