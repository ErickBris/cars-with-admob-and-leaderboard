package net.apepestudio.gametwocars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

//This is my personal button class. If you don't like it, you can look for other classes already creates by other people.
public class But {
	
	float x_from, y_from, width, height, x_to_click, y_to_click, y_from_click, width_change, height_change, seconds, width_second, height_second, width_original, height_original;

	
	//Button background
	public Sprite sp;
	//Texture for the button background
	Texture texture;
	
	public But(float x, float y, float width, float height, Texture texture, int w, int h){
		
		//x from for the button in px
		x_from = x;
		//y from for the button in px
		y_from = y;
		//y from the button that is compared with the screen pressed point
		y_from_click = h - y_from - height;
		//x to for the button (are the same that the click x)
		x_to_click =  x + width;
		//x to for the button (are the same that the click y)
		y_to_click = h - y_from;
		
		
		//Button width
		this.width=width;
		//Button height
		this.height = height;
		this.texture = texture;
		//Time that passes between the click and when the button gets its maximum sizes
		seconds= (float) 0.1;
		//Original width
		width_original = width;
		//Original height
		height_original = height;
		//Width changed in px when the button is clicked
		width_change = (float) (width*0.1);
		//Height changed in px when the button is clicked
		height_change = (float) (height*0.1);
		//Width changed per second
		width_second = width_change / seconds;
		//Height changed per second
		height_second = height_change / seconds;
		
		sp = new Sprite(texture);
		 sp.setPosition(x_from,y_from);
		 sp.setSize(width,height);
	}

	//This method increase the button size
	public void increase_size(float delta){
		
		if(width+delta*width_second > width_change+width_original)
			width = width_change + width_original;
		else width = width + delta*width_second;
		
		if(height+delta*height_second > height_change + height_original)
			height = height_change + height_original;
		else height = height + delta*height_second;
		
		sp.setSize(width,height);
		sp.setPosition(x_from - (width-width_original)/2, y_from - (height-height_original)/2);
		
	}
	
	//This method decrease the button size
	public void decrease_size(float delta){
		
		if(width-delta*width_second < width_original)
			width = width_original;
		else width = width - delta*width_second;
		
		if(height-delta*height_second < height_original)
			height = height_original;
		else height = height - delta*height_second;
		
		sp.setSize(width,height);
		sp.setPosition(x_from - (width-width_original)/2, y_from - (height-height_original)/2);
		
	}
	
	//This method return true if the button have to increase its size or false if decrease
	public boolean increase_or_decrease(int x, int y, float delta){
		
		if(x>=x_from && x<=x_to_click && y>=y_from_click && y<=y_to_click){
			increase_size(delta);
			return true;
		}
		else decrease_size(delta);
		
		return false;
		
	}
	
	//This method restores the button size
	public void restore_size(){
		width = width_original;
		height = height_original;
	}
}
