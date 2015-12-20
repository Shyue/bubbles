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
		r_vel = 0.5f;
		g_vel = 0.5f;
		b_vel=0.5f;
	}
	
	@Override
	public void act(float delta){
		x+=xVelocity*delta;
		y+=yVelocity*delta;
		r+=r_vel*delta;
		g+=g_vel*delta;
		b+=b_vel*delta;
		if(x>=this.getStage().getWidth()-140 || x<=0){
			if(x<=0){
				xVelocity = Math.abs(xVelocity);
			}
			else{
				xVelocity = -Math.abs(xVelocity);
			}
		}
		if(y>=this.getStage().getHeight()-170 || y<=0){
			if(y<=0){
				yVelocity = Math.abs(yVelocity);
			}
			else{
				yVelocity = -Math.abs(yVelocity);
			}
		}
		//somebody make a method
		xVelocity+=200*Math.pow(1-Math.random(),3)*delta;
		yVelocity+=200*Math.pow(1-Math.random(),3)*delta;
		r_vel+=Math.pow(1-Math.random(),3)*delta;
		g_vel+=Math.pow(1-Math.random(),3)*delta;
		b_vel+=Math.pow(1-Math.random(),3)*delta;
		if(r+g+b<1f){
			r_vel+=0.5f;
			g_vel+=0.5f;
			b_vel+=0.5f;
		}
		if(r<0 || r>1){
			r_vel*=-1;
		}
		if(g<0 || g>1){
			g_vel*=-1;
		}
		if(b<0 || b>1){
			b_vel*=-1;
		}
		System.out.println(xVelocity);
		
	}
	public void draw(Batch batch, float alpha){
		batch.setColor(r,g,b,0.3f);
		batch.draw(texture, x, y, radius*2,radius*2);
		
	}
}
