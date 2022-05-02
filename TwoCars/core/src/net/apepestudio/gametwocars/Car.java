package net.apepestudio.gametwocars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

//This class store all the information about a car

public class Car {

	
	public Sprite sp;
	int movement;
	float time_move = (float) 0.18, x_left, x_right, pixels_per_second, distance_x, rotation_max=110;
		
	
	//Constructor
	public Car(Texture t, int m, float x_l, float x_r){
		
		sp = new Sprite(t);
		
		//1 left    2 right 
		movement = m;
		
		x_left = x_l;
		
		x_right = x_r;
		
		pixels_per_second = (float) ((x_r-x_l)/time_move);
		
	}
	
	
	//This method move the car from left to right or from right to left
	public void move(float delta){
		
		if(movement == 1 && sp.getX()!=x_left){
			if((sp.getX() - pixels_per_second*delta) < x_left)
				sp.setPosition(x_left, sp.getY());
			else 
				sp.setPosition(sp.getX()-pixels_per_second*delta, sp.getY());
		}
		
		if(movement == 2 && sp.getX()!=x_right)
			if((sp.getX() + pixels_per_second*delta) > x_right)
				sp.setPosition(x_right, sp.getY());
			else 
				sp.setPosition(sp.getX()+pixels_per_second*delta, sp.getY());
		
		distance_x = x_right - x_left;
		
		sp.setOriginCenter();
		
		rotate();
	}
	
	//This method change the side of the car
	public void change_side(){
		if(movement == 1)
			movement=2;
		else movement=1;
	}
	
	
	//This method rotates the car when it is in movement
	public void rotate(){
		sp.setRotation(0);
		
			if(movement==1 && sp.getX()!=x_left){
				if(sp.getX() - x_left > distance_x/2){
					sp.rotate((float) (rotation_max * (1 - (sp.getX() - x_left + distance_x/2)/distance_x/2)));
				}
				
				else{
					sp.rotate(rotation_max*(sp.getX() - x_left)*100/distance_x/2/100);
				}
			}
			
			else if(movement == 2 && sp.getX() != x_right){
				
				if(x_right - sp.getX() > distance_x/2){
					sp.rotate(-(float) (rotation_max * (1 - (x_right - sp.getX() + distance_x/2)/distance_x/2)));
				}
				
				else{
					sp.rotate(-rotation_max*(x_right - sp.getX())*100/distance_x/2/100);
				}
				
			}
		
	}
	
	
	//Restoring the rotation
	public void restore(){
		
		sp.setRotation(0);
		
		
	}
	
}
