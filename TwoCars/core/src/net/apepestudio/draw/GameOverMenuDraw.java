package net.apepestudio.draw;

import net.apepestudio.gametwocars.GameScreen;
import net.apepestudio.storage.GameOverMenuStorage;
import net.apepestudio.storage.GeneralStorage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;


//This class draws the Game Over Menu on the screen

public class GameOverMenuDraw {
	
	public static void draw(Batch batch, int w, int h, float ratio){
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		
		batch.begin();
		
		
		//Drawing background game
		GeneralStorage.background_game_sprite.draw(batch);
		
		
		//Drawing the figures
		for(int i=0;i<3;i++){
			
			if(GeneralStorage.array_blue[i].move==true)
				GeneralStorage.array_blue[i].sp.draw(batch);
			
			if(GeneralStorage.array_red[i].move==true)
				GeneralStorage.array_red[i].sp.draw(batch);
			
		}
		
		
		//Drawing both cars
		GeneralStorage.car_blue.sp.draw(batch);
		GeneralStorage.car_red.sp.draw(batch);
		
		//Drawing points text
		GeneralStorage.font_points.draw(batch, String.valueOf(GameScreen.points), (int) (w - GeneralStorage.font_points.getBounds(String.valueOf(GameScreen.points)).width - w*0.04), (int) (h*0.96));
		
		//Drawing pause button
		GeneralStorage.pause_sprite.draw(batch);
		
		//Drawing transparent background
		GeneralStorage. background_transparent_sprite.draw(batch);
		
		//Drawing game over menu buttons
		GameOverMenuStorage.replay_button.sp.draw(batch);
		GameOverMenuStorage.home_button.sp.draw(batch);
		GameOverMenuStorage.ranking_button.sp.draw(batch);
		GameOverMenuStorage.share_button.sp.draw(batch);
	
		
		
		//Drawing game over menu texts
		GameOverMenuStorage.font_gameover_other.draw(batch, "SCORE", (int) (w*0.27), (int) (h - h*0.322));
		GameOverMenuStorage.font_gameover_other.draw(batch, String.valueOf(GameScreen.points), (int) (w*0.73 - GameOverMenuStorage.font_gameover_other.getBounds(String.valueOf(GameScreen.points)).width), (int) (h - h*0.322));
		
		GameOverMenuStorage.font_gameover_other.draw(batch, "BEST", (int) (w*0.27), (int) (h - h*0.322 - GameOverMenuStorage.font_gameover_other.getBounds(String.valueOf(GameScreen.points)).height - h*0.02));
		GameOverMenuStorage.font_gameover_other.draw(batch, String.valueOf(GameScreen.best), (int) (w*0.73 - GameOverMenuStorage.font_gameover_other.getBounds(String.valueOf(GameScreen.best)).width), 
				(int) (h - h*0.322 - GameOverMenuStorage.font_gameover_other.getBounds(String.valueOf(GameScreen.points)).height - h*0.02));
		
		GameOverMenuStorage.font_gameover_title.draw(batch, "GAME OVER", (float) ((w - GameOverMenuStorage.font_gameover_title.getBounds("GAME OVER").width)/2), (float) (h - h*0.322 + h*0.125));
		

		batch.end();
		
	}

}
