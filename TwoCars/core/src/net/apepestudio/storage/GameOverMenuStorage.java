package net.apepestudio.storage;

import net.apepestudio.gametwocars.But;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

//This static class stores all the information about Game Over Menu Screen

public class GameOverMenuStorage {
	
	public static But replay_button, home_button, ranking_button, share_button;
	

	public static BitmapFont font_gameover_other, font_gameover_title;
	
	static int replay_height;
	
	public static void load(int w, int h, float ratio){
		
		int height_buttons = (int) (h*0.071);	
		int separation = (int) (w*0.05);
		
		int height_buttons_gameover = (int) (h*0.075);
		int left_margin_buttons_gameover = (int) ((w - 3*height_buttons - 2*separation)/2);
		int top_margin_buttons_gameover = (int) (h*0.25);
		
		
		//REPLAY BUTTON
		replay_height = (int)  (h*0.142);
		replay_button = new But((w-replay_height)/2, (int) (h*0.354), replay_height, replay_height, new Texture("boton_reload.png"), w, h);
		
		//HOME BUTTON
		home_button = new But( left_margin_buttons_gameover, top_margin_buttons_gameover, height_buttons_gameover, height_buttons_gameover, new Texture("boton_home.png"), w, h);
		
		//RANKING BUTTON
		ranking_button = new But(left_margin_buttons_gameover + height_buttons + separation, top_margin_buttons_gameover, height_buttons_gameover, height_buttons_gameover, new Texture("boton_barras.png"), w, h);
		
		//SHARE BUTTON
		share_button = new But(left_margin_buttons_gameover + 2*height_buttons + 2*separation, top_margin_buttons_gameover, height_buttons_gameover, height_buttons_gameover, new Texture("boton_share.png"), w, h);
	
		
		font_gameover_title = new BitmapFont(Gdx.files.internal("font.fnt"));
		font_gameover_title.setScale((float) (0.82*ratio));

		font_gameover_other = new BitmapFont(Gdx.files.internal("font.fnt"));
		font_gameover_other.setScale((float) (0.45*ratio));
		
	
	}

}
