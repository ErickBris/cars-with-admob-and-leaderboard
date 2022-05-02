package net.apepestudio.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;

import net.apepestudio.gametwocars.GameScreen;
import net.apepestudio.storage.*;

//This class draws the menu on the screen

public class MenuDraw {
	
	public static void draw(Batch batch, int w, int h, float ratio){
		
		
		
		Gdx.gl.glClearColor(1, 0, 0, (float) 0.3);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		
		batch.begin();
		
		
		//Drawing game background
		 GeneralStorage.background_game_sprite.draw(batch);
		 
		 //Drawing pause button
		 GeneralStorage.pause_sprite.draw(batch);
		 
		 //Drawing both cars
		 GeneralStorage.car_blue.sp.draw(batch);
		 GeneralStorage.car_red.sp.draw(batch);
		 
		 //Drawing points font
		 GeneralStorage.font_points.draw(batch, String.valueOf(GameScreen.points), (int) (w - GeneralStorage.font_points.getBounds(String.valueOf(GameScreen.points)).width - w*0.04), (int) (h*0.96));
		 
		 //Drawing transparent background
		 GeneralStorage.background_transparent_sprite.draw(batch);
		 
		 //Drawing title
		 MenuStorage.font2cars.draw(batch, "2CARS", (float) ((w-MenuStorage.font2cars.getBounds("2CARS").width)/2), (float) (h*0.8));
		
		 
		 //Drawing menu buttons
		 MenuStorage.play_button.sp.draw(batch);
		 MenuStorage.ranking_button.sp.draw(batch);
		 MenuStorage.rate_button.sp.draw(batch);
		 MenuStorage.music_button.sp.draw(batch);
		 MenuStorage.sound_button.sp.draw(batch);
		
		
		batch.end();
		
	}

}
