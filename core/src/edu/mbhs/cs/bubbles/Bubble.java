package edu.mbhs.cs.bubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bubble extends Actor{
	private float x = 0;
	private float y = 0;
	private float xVelocity;
	private float yVelocity;
	private int radius = 256/4;//pic size
	private float r,g, b;//color floats
	private float r_vel,g_vel,b_vel;//color velocities
	
	Texture texture = new Texture(Gdx.files.internal("bubble.png"));
	
	public Bubble(){
		this.setBounds(x, y, radius*2, radius*2);
		//This is a TERRIBLE way of doing things
		r=(float)Math.pow(Math.random(),1.0/5);
		g=(float)Math.pow(Math.random(),1.0/5);
		b=(float)Math.pow(Math.random(),1.0/5);
		xVelocity = (float)Math.random()*10;
		yVelocity = (float)Math.random()*10;
	}
	
	@Override
	public void act(float delta){
		x+=xVelocity*delta;
		y+=yVelocity*delta;
		if(x>=this.getStage().getWidth()-130 || x<=0){
			xVelocity*=-1;
		}
		if(y>=this.getStage().getHeight()-130 || y<=0){
			yVelocity*=-1;
		}
		//somebody make a method
		xVelocity+=200*Math.pow(1-Math.random(),3)*delta;
		yVelocity+=200*Math.pow(1-Math.random(),3)*delta;
		System.out.println(xVelocity);
		
	}
	public void draw(Batch batch, float alpha){
		batch.setColor(r,g,b,0.3f);
		batch.draw(texture, x, y, radius*2,radius*2);
		
	}
}
