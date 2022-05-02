package net.apepestudio.storage;

import net.apepestudio.gametwocars.Car;
import net.apepestudio.gametwocars.Figure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

//This class stores the variables that are used for more than one screen

public class GeneralStorage {
	
	public static Sprite background_game_sprite;
	
	public static int static_height_car, height_car, width_car;
	
	public static Car car_red, car_blue;
	
	public static Texture circle_red_texture, circle_blue_texture, square_red_texture, square_blue_texture;
	
	public static Integer[] four_positions_figures;
	
	public static Sprite circle_red_sprite, circle_blue_sprite, square_red_sprite, square_blue_sprite, background_transparent_sprite,
	pause_sprite, splash_sprite;
	
	public static BitmapFont font_points;
	
	public static Figure[] array_red, array_blue;
	
	
	public static void load(int w, int h, float ratio){
		
		
		//SPLASH BACKGROUND IMAGE
		splash_sprite = new Sprite(new Texture(Gdx.files.internal("splash.png")));
		splash_sprite.setSize(w, h);
		splash_sprite.setPosition(0, 0);
		
		 //BACKGROUND GAME IMAGE
		 background_game_sprite = new Sprite(new Texture("fondo.png"));
		 background_game_sprite.setPosition(0,0);
		 background_game_sprite.setSize(w, h);
		 
		 //CAR HEIGHT POSITION
		 static_height_car = (int) (h*0.22);
		 
		 //HEIGHT AND WIDHT FOR THE CARS
		 height_car = (int) (h*0.109);
		 width_car = height_car / 2;
		 
		 //THE FOUR POSITIONS POSIBLES FOR CARS AND FIGURES
		 four_positions_figures = new Integer[4];
		 four_positions_figures[0] = (int) ((w * 0.2463 - width_car)/2);
		 four_positions_figures[1] = (int) (w * 0.2536 + ((w*0.2463 - width_car)/2));
		 four_positions_figures[2] = (int) (w * 0.5059 + ((w*0.2463 - width_car)/2));
		 four_positions_figures[3] = (int) ((w * 0.7536 + ((w*0.2463 - width_car)/2)));
		 
		 //RED CAR
		 car_red = new Car(new Texture("car_red.png"), 1, four_positions_figures[0], four_positions_figures[1]);
		 car_red.sp.setPosition(four_positions_figures[0], (int) (static_height_car));
		 car_red.sp.setSize((int) (width_car), (int) (height_car));
		 
			
		 //BLUE CAR
		 car_blue = new Car( new Texture("car_blue.png"), 2, four_positions_figures[2], four_positions_figures[3]);
		 car_blue.sp.setPosition(four_positions_figures[3],(int) (static_height_car));
		 car_blue.sp.setSize((int) (width_car), (int) (height_car));
		 
		 
		 //RED CIRCLE
		 circle_red_texture = new Texture("circle_red.png");
	 circle_red_sprite = new Sprite(circle_red_texture);
	 circle_red_sprite.setPosition((int) (w*0.6),(int) (h*0.1));
	 circle_red_sprite.setSize((int) (h*0.07), (int) (h*0.14));
		 
		 
		 //RED SQUARE
	 square_red_texture = new Texture("square_red.png");
	 square_red_sprite = new Sprite(square_red_texture);
	 square_red_sprite.setPosition((int) (w*0.6),(int) (h*0.1));
	 square_red_sprite.setSize((int) (h*0.07), (int) (h*0.14));
	 
		 
	//BLUE SQUARE				 
	 square_blue_texture = new Texture("square_blue.png");
	 square_blue_sprite = new Sprite(square_blue_texture);
	 square_blue_sprite.setPosition((int) (w*0.6),(int) (h*0.1));
	 square_blue_sprite.setSize((int) (h*0.07), (int) (h*0.14));
		 
	 
	//BLUE CIRCLE
	 circle_blue_texture = new Texture("circle_blue.png");
	 circle_blue_sprite = new Sprite(circle_blue_texture);
	 circle_blue_sprite.setPosition((int) (w*0.6),(int) (h*0.1));
	 circle_blue_sprite.setSize((int) (h*0.07), (int) (h*0.14));
	 
	 
	//BACKGROUND BLACK TRANSPARENT
	 background_transparent_sprite = new Sprite(new Texture ("background_transparent.png"));
	 background_transparent_sprite.setSize(w, h);
	 background_transparent_sprite.setPosition(0, 0);
	 
	 
	 //PAUSE BUTTON
	 pause_sprite = new Sprite(new Texture("pause.png"));
	 pause_sprite.setSize((float) (h*0.0412*0.6125), (float) (h*0.0412));
	 pause_sprite.setPosition((float) (w*0.032),(float) (h*0.9375));
	 
	 
		//POINTS TEXT FOR THE GAME
		font_points = new BitmapFont(Gdx.files.internal("font.fnt"));
		font_points.setScale((float) (0.7*ratio));
		
		array_red = new Figure[3];
		array_blue = new Figure[3];
		
		for(int i=0; i<3; i++){
			
			array_red[i] = new Figure(circle_red_texture);
			array_blue[i] = new Figure(circle_blue_texture);
			
		}
		 
		
	}

}
