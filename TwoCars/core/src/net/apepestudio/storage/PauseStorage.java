package net.apepestudio.storage;

import net.apepestudio.gametwocars.But;

import com.badlogic.gdx.graphics.Texture;

//This class stores all the variables for pause screen

public class PauseStorage {
	
	public static But home_pause_button, play_pause_button;
	
	public static void load(int w, int h, float ratio){
		
		 
		 home_pause_button = new But((float) (w*0.273), (float) (h*0.446), (float) (h*0.104), (float) (h*0.104),  new Texture("boton_home.png"), w, h);

		 play_pause_button = new But((float) (w*0.539), (float) (h*0.446), (float) (h*0.104), (float) (h*0.104), new Texture("boton_play.png"), w, h);
		
	}

}
