package net.apepestudio.draw;

import net.apepestudio.gametwocars.GameScreen;
import net.apepestudio.storage.GeneralStorage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;


//This class draws the game on the screen

public class RunningDraw {
	
	public static void draw(Batch batch, int w, int h, float ratio){
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		batch.begin();
		
		//Drawing game background
		GeneralStorage.background_game_sprite.draw(batch);
		
		//Drawing the figures that are in movement
		for(int i = 0;i<3;i++){
			if(GeneralStorage.array_red[i].move)
				GeneralStorage.array_red[i].sp.draw(batch);
			
			if(GeneralStorage.array_blue[i].move)
				GeneralStorage.array_blue[i].sp.draw(batch);
		}
		
		//Drawing both cars
		GeneralStorage.car_blue.sp.draw(batch);
		GeneralStorage.car_red.sp.draw(batch);
		
		//Drawing points text
		GeneralStorage.font_points.draw(batch, String.valueOf(GameScreen.points), (int) (w - GeneralStorage.font_points.getBounds(String.valueOf(GameScreen.points)).width - w*0.04), (int) (h*0.96));
	
		
		//Drawing pause button
		GeneralStorage.pause_sprite.draw(batch);
		
		batch.end();
		
		
	}

}
