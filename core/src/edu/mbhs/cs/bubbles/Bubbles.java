package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Main Application: Bubbles Game
 */
public class Bubbles implements Screen {
	private static int BUBBLE_NUMBER = 80;
	private Bubble[] b = new Bubble[BUBBLE_NUMBER];
	private Stage stage;
	private World world;
	private OrthographicCamera cam;
	private static float PUSH_SPEED = 10000;
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private Texture text;
	Player p;

	@Override
	public void show() {
		stage = new Stage();
		world = new World(new Vector2(0,0), false);
		//Gdx.app.log("AssetPath", Gdx.files.internal("assets/bubble.jpg").file().getAbsolutePath());
		for(int i = 0; i < BUBBLE_NUMBER; i++){
			b[i] = new Bubble(world, stage.getWidth()*4, stage.getHeight()*4);

			stage.addActor(b[i]);
		}
		//b[0] = new Bubble(world);
		//stage.addActor(b[0]);
		p = new Player(world, stage.getWidth(), stage.getHeight());
		stage.addActor(p);
		text = new Texture(Gdx.files.internal("buble.png"));
		text.setWrap(TextureWrap.Repeat,TextureWrap.Repeat);
		
		//setting up the camera

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w/2, h/2);
		System.out.println(w+" "+h);
		cam.position.set(w/2, h/2 , 0);
		cam.update();
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		//stage.getViewport().setCamera(cam);

		//this edge detection doesn't work somebody fix it
		LinkedList<String> holder;
		float with = Gdx.graphics.getWidth()/Bubble.METERS_TO_PIXELS*4;
        float hite = Gdx.graphics.getHeight()/Bubble.METERS_TO_PIXELS*4;
	    BodyDef bottom = new BodyDef();
	    bottom.type = BodyDef.BodyType.StaticBody;
	    bottom.position.set(0,0);
	    FixtureDef bot = new FixtureDef();
	    EdgeShape but = new EdgeShape();
	    but.set(new Vector2(-with, 0), new Vector2(with, 0));
	    bot.shape = but;
	    Body botEdge = world.createBody(bottom);
	    Fixture botFixture = botEdge.createFixture(bot);
		holder = new LinkedList<String>(); holder.add("bot");
		botFixture.setUserData(holder);

	    BodyDef top = new BodyDef();
	    top.type = BodyDef.BodyType.StaticBody;
	    top.position.set(0, stage.getHeight()*4 / Bubble.METERS_TO_PIXELS);
	    FixtureDef tip = new FixtureDef();
	    EdgeShape tup = new EdgeShape();
	    tup.set(new Vector2(-with, 0), new Vector2(with, 0));
	    tip.shape = tup;
	    Body topEdge = world.createBody(top);
	    Fixture topFixture = topEdge.createFixture(tip);
		holder = new LinkedList<String>(); holder.add("top");
		topFixture.setUserData(holder);
	    
	    BodyDef left = new BodyDef();
	    left.type = BodyDef.BodyType.StaticBody;
	    left.position.set(0, 0);
	    FixtureDef lift = new FixtureDef();
	    EdgeShape luft = new EdgeShape();
	    luft.set(new Vector2(0, -hite), new Vector2(0, hite));
	    lift.shape = luft;
	    Body leftEdge = world.createBody(left);
	    Fixture leftFixture = leftEdge.createFixture(lift);
		holder = new LinkedList<String>(); holder.add("left");
		leftFixture.setUserData(holder);
	    
	    BodyDef right = new BodyDef();
	    right.type = BodyDef.BodyType.StaticBody;
	    right.position.set(stage.getWidth()*4 / Bubble.METERS_TO_PIXELS,0);
	    FixtureDef rot = new FixtureDef();
	    EdgeShape rut = new EdgeShape();
	    rut.set(new Vector2(0, -hite), new Vector2(0, hite));
	    rot.shape = rut;
	    Body rightEdge = world.createBody(right);
	    Fixture rightFixture = rightEdge.createFixture(rot);
		holder = new LinkedList<String>(); holder.add("right");
	  	rightFixture.setUserData(holder);
	    
	    but.dispose();
	    rut.dispose();
	    luft.dispose();
	    tup.dispose();

		//setting listener for bubble/player colliding
		world.setContactListener(new ContactListener() {

			boolean Colliding;
			@Override
			public void beginContact(Contact contact) {

				if (((LinkedList)contact.getFixtureA().getUserData()).get(0)=="Player" && ((LinkedList)contact.getFixtureB().getUserData()).get(0)=="Bubble"){
					//System.out.println(contact.getFixtureA().getUserData()+" "+contact.getFixtureB().getUserData());

					contact.getFixtureA().getBody().applyForceToCenter(new Vector2(
							PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(1))-Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(1)))
							,
							PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(2))-Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(2)))
					), true);
					//System.out.println(PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(1))-Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(1)))+" "+PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(2))-Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(2))));
				}
				if (((LinkedList)contact.getFixtureB().getUserData()).get(0)=="Player" && ((LinkedList)contact.getFixtureA().getUserData()).get(0)=="Bubble"){
					//System.out.println(contact.getFixtureA().getUserData()+" "+contact.getFixtureB().getUserData());

					contact.getFixtureB().getBody().applyForceToCenter(new Vector2(
							PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(1))-Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(1)))
							,
							PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(2))-Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(2)))
					), true);
					//System.out.println(PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(1))-Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(1)))+" "+PUSH_SPEED*(Float.parseFloat((String)((LinkedList)contact.getFixtureB().getUserData()).get(2))-Float.parseFloat((String)((LinkedList)contact.getFixtureA().getUserData()).get(2))));

			}

			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void preSolve(Contact arg0, Manifold arg1) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void render(float delta) {

		float w = stage.getWidth()*4;
		float h = stage.getHeight()*4;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.step(1f/60f, 6, 2);
		stage.act(Gdx.graphics.getDeltaTime());
		updateCam();
		cam.update();
		batch.setProjectionMatrix(cam.combined);
        //stage.draw();
		batch.begin();
		batch.setColor(1f, 1f, 1f, 1f);
		batch.draw(text, -0,-0 ,w,h);
		batch.draw(text, w,-0 ,w,h);
		batch.draw(text, 0,h ,w,h);
		batch.draw(text, 0,-h ,w,h);
		batch.draw(text, -w,-0 ,w,h);

		batch.draw(text, w,h ,w,h);
		batch.draw(text, -w,h ,w,h);
		batch.draw(text, w,-h ,w,h);
		batch.draw(text, -w,-h ,w,h);
		p.draw(batch, Gdx.graphics.getDeltaTime());
		for (int i = 0; i<BUBBLE_NUMBER;i++){
			b[i].draw(batch, Gdx.graphics.getDeltaTime());
		}
		batch.end();

		updateRadar();

	}
	public void updateCam(){
		//cam.position.set((p.getBody().getPosition().x-Player.RADIUS) * Bubble.METERS_TO_PIXELS, (p.getBody().getPosition().y-Player.RADIUS)* Bubble.METERS_TO_PIXELS, 0);
		cam.position.set((p.getBody().getPosition().x) * Bubble.METERS_TO_PIXELS, (p.getBody().getPosition().y)* Bubble.METERS_TO_PIXELS, 0);
		System.out.println("cam "+cam.position.x+" "+cam.position.y);
		//System.out.println(p.getBody().getPosition().x+" "+p.getBody().getPosition().y);
	}

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
		renderer.setColor(Color.RED);
		float dist, theta, dx, dy;
		for (Bubble bubble : b) {
			dx = cam.position.x / Bubble.METERS_TO_PIXELS - (bubble.getBody().getPosition().x + bubble.radius / 2);
			dy = cam.position.y / Bubble.METERS_TO_PIXELS - (bubble.getBody().getPosition().y + bubble.radius / 2);
			dist = (float) Math.hypot(dx, dy);
			if (dist < stage.getWidth() / 5) {
				System.out.println("hi");
				theta = (float) Math.atan2(dy, dx);
				dist = dist / stage.getWidth() * RADIUS * Bubble.METERS_TO_PIXELS;
				renderer.circle(CENTER_X + dist * (float) -Math.cos(theta), CENTER_Y + dist * (float) -Math.sin(theta), LITTLE_RADIUS / 2);
			}
		}
		renderer.end();
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
