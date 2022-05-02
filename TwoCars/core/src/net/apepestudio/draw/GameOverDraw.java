package net.apepestudio.draw;

import net.apepestudio.storage.*;
import C.C;
import net.apepestudio.gametwocars.GameScreen;
import net.apepestudio.gametwocars.GameScreen.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


//This class if for drawing the Game Over on the screen, before showing the Game Over Menu

public class GameOverDraw {
	
	public static float time_acu_gameover;
	
	public static void draw(Batch batch, int w, int h, float ratio, float deltaTime, int square_past, int color_square_past,
			boolean parpadear){
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 Gdx.gl.glEnable(GL20.GL_BLEND);
		 
		batch.begin();
		
		//Game Background
		GeneralStorage.background_game_sprite.draw(batch);
		
		//Storing time between game over and game over menu
		time_acu_gameover+=Gdx.graphics.getDeltaTime();
		
		
		//Drawing the figures on the screen. If you hasn't catch a circle, it appears and disappears
		for(int i=0;i<3;i++){

			if(GeneralStorage.array_blue[i].move==true){
				if(parpadear==false)
					GeneralStorage.array_blue[i].sp.draw(batch);
				else 	if(color_square_past!=2 || square_past!=i)
					GeneralStorage.array_blue[i].sp.draw(batch);
				
			else if((time_acu_gameover>=0.2 && time_acu_gameover<=0.4) || (time_acu_gameover>=0.6 && time_acu_gameover<=0.8) || (time_acu_gameover>=1 && time_acu_gameover<=1.2) || 
					(time_acu_gameover>=1.4 && time_acu_gameover<=1.6) && !GameScreen.show_blue_particles && !GameScreen.show_red_particles)
				GeneralStorage.array_blue[i].sp.draw(batch);
			}
			
			if(GeneralStorage.array_red[i].move==true){ 
					if(parpadear==false)
						GeneralStorage.array_red[i].sp.draw(batch);
					else 	if(color_square_past!=1 || square_past!=i)
						GeneralStorage.array_red[i].sp.draw(batch);
			else if((time_acu_gameover>=0.2 && time_acu_gameover<=0.4) || (time_acu_gameover>=0.6 && time_acu_gameover<=0.8) || (time_acu_gameover>=1 && time_acu_gameover<=1.2) || 
					(time_acu_gameover>=1.4 && time_acu_gameover<=1.6) && !GameScreen.show_blue_particles && !GameScreen.show_red_particles)
				GeneralStorage.array_red[i].sp.draw(batch);
			}
		}
		
		//Drawing cars
		GeneralStorage.car_blue.sp.draw(batch);
		GeneralStorage.car_red.sp.draw(batch);
		
		//Drawing points text
		GeneralStorage.font_points.draw(batch, String.valueOf(GameScreen.points), (int) (w - GeneralStorage.font_points.getBounds(String.valueOf(GameScreen.points)).width - w*0.04), (int) (h*0.96));
		
		//Drawing pause button
		GeneralStorage.pause_sprite.draw(batch);
		
		
		//After 1.6 seconds, appears the game over menu
		if(time_acu_gameover>1.6){
			
			if(!GameScreen.loaded_gameover){
			GameOverMenuStorage.load(w, h, ratio);
			GameScreen.loaded_gameover=true;
			}
			
			//Activating Game Over Mode
			GameScreen.state = GameState.GAMEOVER_MENU;
			

			//Showing the interstitial after 0.5 seconds
			if(GameScreen.randomNum(0, 101)<=C.interstitial_percentage){
			
			Timer.schedule(new Task(){
			    @Override
			    public void run() {
			        // Do your work
			    	GameScreen.actionResolver.showOrLoadInterstital();
			    }
			}, (float) 0.5);
	
		}
		}
		
		
		//If you have crashed with a red square the red particles are shown
		if(GameScreen.show_red_particles)
			GameScreen.p.draw(batch, Gdx.graphics.getDeltaTime());
		
		//If you have crashed with a blue square the blue particles are shown
		if(GameScreen.show_blue_particles)
			GameScreen.p2.draw(batch, Gdx.graphics.getDeltaTime());
			
		
		batch.end();
		
	}

}
