package net.apepestudio.gametwocars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

//This class stores all te information about a figure

public class Figure {
	
	public Sprite sp;
	public boolean move = false;
	int speed = 0;
	
	//1=circle 2=square
	int type;
	//1=left 2=right
	int side;
	
	
	//Constructor
	public Figure(Texture t){
		
		sp = new Sprite(t);
	
	}
	
	//The current speed of the figure
	public int getSpeed(){
		return speed;
	}
	
	//Changing the speed
	public void setSpeed(int s){
		speed = s;
	}
	
	
	//Changing the side
	public void setSide(int s){
		side = s;
	}

}
