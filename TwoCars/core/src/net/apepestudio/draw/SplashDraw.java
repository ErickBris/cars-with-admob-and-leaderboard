package net.apepestudio.draw;

import net.apepestudio.gametwocars.GameScreen;


import net.apepestudio.gametwocars.GameScreen.GameState;
import net.apepestudio.storage.GeneralStorage;
import net.apepestudio.storage.MenuStorage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;


//This class draws the splash on the screen

public class SplashDraw {
	
	public static float time_acu_splash = 0;
	
	public static void draw(Batch batch, int w, int h, float ratio){
		
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		batch.begin();
		
		//Drawing splash background
		GeneralStorage.splash_sprite.draw(batch);
		
		//Storing time of splash
		time_acu_splash += Gdx.graphics.getDeltaTime();
		
		//After 3 seconds the game change to the menu
		if(time_acu_splash>=3){
			
			//Loading Menu Variables
			MenuStorage.load(w, h, ratio);
			GameScreen.state = GameState.MENU;
			
		}
		
		batch.end();
	}

}
