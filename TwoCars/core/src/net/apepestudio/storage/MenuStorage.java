package net.apepestudio.storage;

import net.apepestudio.gametwocars.But;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

//This class stores all the variables for Menu Screen

public class MenuStorage {
	
	
	
	public static Sprite background_game_sprite, car_blue_sprite, car_red_sprite, circle_red_sprite, square_red_sprite, circle_blue_sprite, 
	square_blue_sprite, background_transparent_sprite, pause_sprite;
	
	public static But play_button, ranking_button, rate_button, music_button, sound_button, rankingGameOver_button, share_button, play_pause_button, home_pause_button;
	
	public static BitmapFont font2cars;
	
	public static int replay_height;
	
	public static Preferences prefs;
	
	
	public static void load(int w, int h, float ratio){
		
		
		int height_buttons = (int) (h*0.071);
	
		int separation = (int) (w*0.05);
		int left_margin_buttons = (int) ((w - 4*height_buttons - 3*separation)/2);
		int left_margin_buttons_gameover = (int) ((w - 3*height_buttons - 2*separation)/2);
		int top_margin_buttons = (int) (h*0.3375);
		int top_margin_buttons_gameover = (int) (h*0.25);
		

		prefs = Gdx.app.getPreferences("Prefs");
		

		
		//PLAY BUTTON
		play_button = new But((int) (w-h*0.16)/2,(int) (h*0.47), (int)  (h*0.16), (int)  (h*0.16),  new Texture("boton_play.png"), w, h);
		

		//RANKING BUTTON
		ranking_button = new But(left_margin_buttons, top_margin_buttons, height_buttons, height_buttons, new Texture("boton_barras.png"), w, h);
		
		//RATE BUTTON
		rate_button = new But(left_margin_buttons + height_buttons + separation, top_margin_buttons, height_buttons, height_buttons, new Texture("boton_estrella.png"), w, h);
		
		//MUSIC BUTTON
		if(prefs.getInteger("music",1)==1)
			music_button = new But(left_margin_buttons + 2*height_buttons + 2*separation, top_margin_buttons, height_buttons, height_buttons, new Texture("boton_musica.png"), w, h);

		else music_button = new But(left_margin_buttons + 2*height_buttons + 2*separation, top_margin_buttons, height_buttons, height_buttons, new Texture("boton_musica_off.png"), w, h);;
		
		//SOUND BUTTON
		if(prefs.getInteger("sound",1)==1)
		sound_button = new But(left_margin_buttons + 3*height_buttons + 3*separation, top_margin_buttons, height_buttons, height_buttons, new Texture("boton_audio.png"), w, h);
			else sound_button = new But(left_margin_buttons + 3*height_buttons + 3*separation, top_margin_buttons, height_buttons, height_buttons, new Texture("boton_audio_off.png"), w, h);
	
		//2 CARS FONT
		font2cars = new BitmapFont(Gdx.files.internal("font.fnt"));
		font2cars.setScale((float) (1*ratio));

		
		
		
		
		
		
		
	}

}
