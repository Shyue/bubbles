package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Collections;
import java.util.List;

/**
 * Main Game Screen
 */
public class Bubbles implements Screen {

	private static int BUBBLE_NUMBER = 50;
	private static int FLAG_NUMBER = 5;
	private static float PUSH_SPEED = 10000;

	private Bubble[] b = new Bubble[BUBBLE_NUMBER];
	private Flag[] flags = new Flag[FLAG_NUMBER];
	private Player p;
	private Stage stage;
	private World world;
	private OrthographicCamera cam;
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private Texture text;
	private BitmapFont font = new BitmapFont();
	private int score = 0;
	private Game game;
	public Bubbles(Game g){
		game = g;
	}
	
	@Override
	public void show() {
		// set up stage and world (actor drawing manager, physics manager
		stage = new Stage();
		world = new World(new Vector2(0, 0), false);

		// add bubbles to world and stage
		for(int i = 0; i < BUBBLE_NUMBER; i++){
			b[i] = new Bubble(world, i > 6);
			stage.addActor(b[i]);
		}

		// add flags to world and stage
		for(int i = 0; i < FLAG_NUMBER; i++){
			flags[i] = new Flag(stage);
			stage.addActor(flags[i]);
		}

		// add player to world and stage
		p = new Player(world);
		stage.addActor(p);

		// add background
		text = new Texture(Gdx.files.internal("buble.png"));
		text.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		//setting up the camera
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width/2, height/2);
		cam.position.set(width/2, height/2 , 0);
		cam.update();
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		setupBorderPhysics();

		//setting listener for bubble/player colliding
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				List aUserData = (List) contact.getFixtureA().getUserData();
				List bUserData = (List) contact.getFixtureB().getUserData();
				Body aBody = contact.getFixtureA().getBody();
				Body bBody = contact.getFixtureB().getBody();

				if (aUserData.get(0).equals("Player") && bUserData.get(0).equals("Bubble")) {
					if(bBody.getMass() > 9){
						game.setScreen(new HomeScreen(game, score));
					}
					aBody.applyForceToCenter(new Vector2(
							PUSH_SPEED * (Float.parseFloat((String) aUserData.get(1)) - Float.parseFloat((String) bUserData.get(1))),
							PUSH_SPEED * (Float.parseFloat((String) aUserData.get(2)) - Float.parseFloat((String) bUserData.get(2)))
					), true);
					
				}
				if (bUserData.get(0).equals("Player") && aUserData.get(0).equals("Bubble")) {
					if(aBody.getMass() > 9){
						game.setScreen(new HomeScreen(game, score));
					}
					bBody.applyForceToCenter(new Vector2(
							PUSH_SPEED * (Float.parseFloat((String) bUserData.get(1)) - Float.parseFloat((String) aUserData.get(1))),
							PUSH_SPEED * (Float.parseFloat((String) bUserData.get(2)) - Float.parseFloat((String) aUserData.get(2)))
					), true);
				}
			}

			@Override
			public void endContact(Contact contact) {}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {}

			@Override
			public void preSolve(Contact arg0, Manifold arg1) {}
		});
	}

	@Override
	public void render(float delta) {
		final float GAME_WIDTH = stage.getWidth() * 4;
		final float GAME_HEIGHT = stage.getHeight() * 4;

		// do physics
		world.step(1f / 60f, 6, 2);
		stage.act(Gdx.graphics.getDeltaTime());

		// update camera position
		updateCam();
		cam.update();

		// setup drawing tool
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.setColor(1f, 1f, 1f, 1f);

		// draw borders
		batch.draw(text, 0, 0, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, GAME_WIDTH, 0, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, 0, GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, 0, -GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, -GAME_WIDTH, 0, GAME_WIDTH, GAME_HEIGHT);

		batch.draw(text, GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, -GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, GAME_WIDTH, -GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(text, -GAME_WIDTH, -GAME_HEIGHT, GAME_WIDTH, GAME_HEIGHT);

		// draw player
		p.draw(batch, Gdx.graphics.getDeltaTime());

		// draw flags and check if flag was obtained
		for (int i = 0; i < FLAG_NUMBER; i++) {
			flags[i].draw(batch, Gdx.graphics.getDeltaTime());
			if(Intersector.overlaps(p.getBounds(), flags[i].getBounds())) {
				flags[i].init();
				score++;
			}
		}

		// draw bubbles
		for (int i = 0; i < BUBBLE_NUMBER; i++) {
			b[i].draw(batch, Gdx.graphics.getDeltaTime());
		}

		// draw score
		font.draw(batch, String.format("Score: %d", score), p.getX() - 120, p.getY() + 100);

		// stop picture drawing tool
		batch.end();

		updateRadar();
	}

	/**
	 * Sets up the top, bottom, left, and right border bodies
	 */
	private void setupBorderPhysics() {
		float realGameWidthPixels = Gdx.graphics.getWidth() / Bubble.METERS_TO_PIXELS * 4;
		float realGameHeightPixels = Gdx.graphics.getHeight() / Bubble.METERS_TO_PIXELS * 4;

		// bottom border
		BodyDef bottom = new BodyDef();
		bottom.type = BodyDef.BodyType.StaticBody;
		bottom.position.set(0, 0);
		FixtureDef bottomFixtureDef = new FixtureDef();
		EdgeShape bottomShape = new EdgeShape();
		bottomShape.set(new Vector2(-realGameWidthPixels, 0), new Vector2(realGameWidthPixels, 0));
		bottomFixtureDef.shape = bottomShape;
		Body bottomEdge = world.createBody(bottom);
		Fixture bottomFixture = bottomEdge.createFixture(bottomFixtureDef);
		bottomFixture.setUserData(Collections.singletonList("bot"));

		// top border
		BodyDef top = new BodyDef();
		top.type = BodyDef.BodyType.StaticBody;
		top.position.set(0, stage.getHeight() * 4 / Bubble.METERS_TO_PIXELS);
		FixtureDef topFixtureDef = new FixtureDef();
		EdgeShape topEdge = new EdgeShape();
		topEdge.set(new Vector2(-realGameWidthPixels, 0), new Vector2(realGameWidthPixels, 0));
		topFixtureDef.shape = topEdge;
		Body topBorder = world.createBody(top);
		Fixture topFixture = topBorder.createFixture(topFixtureDef);
		topFixture.setUserData(Collections.singletonList("top"));

		// left border
		BodyDef left = new BodyDef();
		left.type = BodyDef.BodyType.StaticBody;
		left.position.set(0, 0);
		FixtureDef leftFixtureDef = new FixtureDef();
		EdgeShape leftEdge = new EdgeShape();
		leftEdge.set(new Vector2(0, -realGameHeightPixels), new Vector2(0, realGameHeightPixels));
		leftFixtureDef.shape = leftEdge;
		Body leftBorder = world.createBody(left);
		Fixture leftFixture = leftBorder.createFixture(leftFixtureDef);
		leftFixture.setUserData(Collections.singletonList("left"));

		// right border
		BodyDef right = new BodyDef();
		right.type = BodyDef.BodyType.StaticBody;
		right.position.set(stage.getWidth() * 4 / Bubble.METERS_TO_PIXELS, 0);
		FixtureDef rightFixtureDef = new FixtureDef();
		EdgeShape rightEdge = new EdgeShape();
		rightEdge.set(new Vector2(0, -realGameHeightPixels), new Vector2(0, realGameHeightPixels));
		rightFixtureDef.shape = rightEdge;
		Body rightBorder = world.createBody(right);
		Fixture rightFixture = rightBorder.createFixture(rightFixtureDef);
		rightFixture.setUserData(Collections.singletonList("right"));

		bottomShape.dispose();
		rightEdge.dispose();
		leftEdge.dispose();
		topEdge.dispose();
	}

	/**
	 * Update camera position. Center camera on player's position
	 */
	public void updateCam() {
		cam.position.set(
				p.getBody().getPosition().x * Bubble.METERS_TO_PIXELS,
				p.getBody().getPosition().y * Bubble.METERS_TO_PIXELS,
				0
		);
	}

	/**
	 * Update the enemy/flag radar
	 */
	public void updateRadar() {
		final float RADIUS = cam.viewportWidth / 10;
		final float LITTLE_RADIUS = RADIUS / 10;
		final float CENTER_X = cam.position.x + cam.viewportWidth / 2 - RADIUS - 5;
		final float CENTER_Y = cam.position.y + cam.viewportHeight / 2 - RADIUS - 5;

		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeRenderer.ShapeType.Filled);

		// radar background
		renderer.setColor(Color.BLACK);
		renderer.circle(CENTER_X, CENTER_Y, RADIUS);

		// player indicator
		renderer.setColor(Color.BLUE);
		renderer.circle(CENTER_X, CENTER_Y, LITTLE_RADIUS);

		// other bubbles
		
		float dist, theta, dx, dy;
		for (Bubble bubble : b) {
			renderer.setColor(Color.WHITE);
			if(!bubble.isFriend()) renderer.setColor(Color.RED);
			dx = cam.position.x / Bubble.METERS_TO_PIXELS - (bubble.getBody().getPosition().x + Bubble.RADIUS / 2);
			dy = cam.position.y / Bubble.METERS_TO_PIXELS - (bubble.getBody().getPosition().y + Bubble.RADIUS / 2);
			dist = (float) Math.hypot(dx, dy);
			if (dist < stage.getWidth() / 5) {
				theta = (float) Math.atan2(dy, dx);
				dist = dist / stage.getWidth() * RADIUS * Bubble.METERS_TO_PIXELS;
				renderer.circle(CENTER_X + dist * (float) -Math.cos(theta), CENTER_Y + dist * (float) -Math.sin(theta), LITTLE_RADIUS / 2);
			}
		}

		// flags
		renderer.setColor(Color.YELLOW);
		for (Flag flag : flags) {
			dx = cam.position.x - flag.getX();
			dy = cam.position.y - flag.getY();
			dist = (float) Math.hypot(dx, dy) / Bubble.METERS_TO_PIXELS;
			if (dist < stage.getWidth() / 5) {
				theta = (float) Math.atan2(dy, dx);
				dist = dist / stage.getWidth() * RADIUS * Bubble.METERS_TO_PIXELS;
				renderer.circle(CENTER_X + dist * (float) -Math.cos(theta), CENTER_Y + dist * (float) -Math.sin(theta), LITTLE_RADIUS / 2);
			}
		}

		renderer.end();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}

}
