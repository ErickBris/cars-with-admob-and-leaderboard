package net.apepestudio.gametwocars;

import java.util.Random;

import net.apepestudio.draw.GameOverDraw;
import net.apepestudio.draw.GameOverMenuDraw;
import net.apepestudio.draw.MenuDraw;
import net.apepestudio.draw.PauseDraw;
import net.apepestudio.draw.RunningDraw;
import net.apepestudio.draw.SplashDraw;
import net.apepestudio.storage.GameOverMenuStorage;
import net.apepestudio.storage.GeneralStorage;
import net.apepestudio.storage.MenuStorage;
import net.apepestudio.storage.PauseStorage;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

//This is the principal class of the game. It controls the game life, calling the stores and the draw classes

public class GameScreen extends ApplicationAdapter {
	SpriteBatch batch;
	
	BitmapFont font_points, font_score, font_score_points, font_best, font_best_points, font_2cars, font_gameover;
	
	public static boolean pressed, first=true, letClick=true, parpadear=false, loaded_pause=false, loaded_gameover=false;
	
	public static int points, last_sended_color, color_square_past, pixels_move, i, square_past, best, w,h, last_red, last_blue;

	int last_amount_touchs=0, current_amount_touchs=0, acumulator=0, first_x, first_y;
	
	float ratio, separation, time, acu, time_acu_gameover, time_acu_splash=0;
	double mult, h_game=0.8;
	public static GameState state;
	
	public static boolean show_red_particles = false;

	public static boolean show_blue_particles = false;
	
	Preferences prefs;
	Sound sound_ok, sound_crash;
	Music background_music;
	public static ActionResolver actionResolver;
	
	public enum GameState {
		   SPLASH, RUNNING, GAMEOVER, MENU, GAMEOVER_MENU, PAUSE
		 }
	
	public static ParticleEffect p,  p2;

	
	//The action resolver allows to call android methods
	public GameScreen(ActionResolver actionResolver) {
		    this.actionResolver = actionResolver;
		  }
	
	
	//This method is called at the beginning of the game, after splash screen
	@Override
	public void create () {
				
		
		loaded_gameover = false;
		loaded_pause = false;
		
		//Screen Sizes. DON'T CHANGE THEM!
		 w = Gdx.graphics.getWidth();
		 h = Gdx.graphics.getHeight();
		 
		 //Ratio is used for text size
			float ratio1 = (float) (w / 1080.0);
			float ratio2 = (float) (h / 1920.0);

			//The ratio is used for text size
			if (ratio1 > ratio2)
				ratio = ratio2;
			else
				ratio = ratio1;
		/////////////END SIZE VARIABLES DECLARATION////////////////////////
		

		GeneralStorage.load(w,h,ratio);
		SplashDraw.time_acu_splash=0;

		//The game begins with splash screen
		state = GameState.SPLASH;
		
		
		//Loading particle effects
		p = new ParticleEffect();
		p2= new ParticleEffect();
		p.load(Gdx.files.internal("effects/particles_red"), Gdx.files.internal("effects"));
		p2.load(Gdx.files.internal("effects/particles_blue"), Gdx.files.internal("effects"));
		
		
			prefs = Gdx.app.getPreferences("Prefs");
		
			//Music and sounds variables
			sound_ok = Gdx.audio.newSound(Gdx.files.internal("ok.mp3"));
			sound_crash = Gdx.audio.newSound(Gdx.files.internal("crash.mp3"));
			background_music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
			background_music.setLooping(true);


		batch = new SpriteBatch();
	 
				//Playing the music
				if(prefs.getInteger("music",1)==1)
					background_music.play();
	 
	}

	//This method is called a lot of times per second, it calls to the Draw classes to draw the screens and control the screen touches
	@Override
	public void render () {
		
		
		//This switch control the game and show one or other screen
		switch(state){
		
		case SPLASH:
			
			SplashDraw.draw(batch, w, h, ratio);
			
			break;
		
			
			
		case RUNNING:
			
			RunningDraw.draw(batch, w, h, ratio);
			
			
			//Storing the current number of touches on the screen
		while(Gdx.input.isTouched(acumulator)){
		acumulator++;
		}
		
	current_amount_touchs = acumulator;
		
	
			//If the screen is touched
			for(i=last_amount_touchs;i<current_amount_touchs;i++)
				
				//If pause button has been pressed
				if(Gdx.input.getX(i)>=GeneralStorage.pause_sprite.getX() && Gdx.input.getX(i)<=GeneralStorage.pause_sprite.getX() + GeneralStorage.pause_sprite.getWidth() && 
				Gdx.input.getY(i)>=h - GeneralStorage.pause_sprite.getY() - GeneralStorage.pause_sprite.getHeight() && Gdx.input.getY(i)<= h - GeneralStorage.pause_sprite.getY()){
					state = GameState.PAUSE;
				first=true;	
				}

			//If user has touched left side red car is moved
				else	if(Gdx.input.getX(i)<=w*0.5)			
			GeneralStorage.car_red.change_side();
			
			//If user has touched right side blue car is moved
				else	GeneralStorage.car_blue.change_side();
		
		//Moving both cars
		GeneralStorage.car_red.move(Gdx.graphics.getDeltaTime());
		GeneralStorage.car_blue.move(Gdx.graphics.getDeltaTime());
		 
	

		
		//////////////////////////////////////////////////////////////MOVEMENT////////////////////////////////////////////////////////////////////
		
		//Putting new figures in movement
		put_new_figures_in_movement();
	
		
		//Calculating the number of pixels that have to been moved each figure
		acu+=Gdx.graphics.getDeltaTime()*mult;
		pixels_move=(int) (acu);
		acu-=(int) (acu);
		
		
		//Moving the figures, checking the collisions with the car, getting points, etc.
		for(i=0;i<3;i++){
			//The three figures are check. If the current figure is being moved its position is changed
			if(GeneralStorage.array_red[i].move){
				
				
				//Red circle
				if(GeneralStorage.array_red[i].type==1){
				
				if(GeneralStorage.array_red[i].sp.getY() <= GeneralStorage.static_height_car + GeneralStorage.height_car - GeneralStorage.width_car * 0.65 && GeneralStorage.array_red[i].sp.getY()>=GeneralStorage.static_height_car-GeneralStorage.width_car) 
					
					
					if(GeneralStorage.array_red[i].side==1){
					
						if(GeneralStorage.car_red.sp.getX()<GeneralStorage.four_positions_figures[0]+GeneralStorage.width_car-GeneralStorage.width_car*0.4){
							GeneralStorage.array_red[i].move=false;
							points++;
							if(prefs.getInteger("sound",1)==1)
							sound_ok.play();
						}
					}else{
						if(GeneralStorage.car_red.sp.getX()+GeneralStorage.width_car>(GeneralStorage.four_positions_figures[1]+GeneralStorage.width_car*0.4)){
							GeneralStorage.array_red[i].move=false;
							points++;
							if(prefs.getInteger("sound",1)==1)
							sound_ok.play();
						}
					}
				
				
				if(GeneralStorage.array_red[i].sp.getY() + GeneralStorage.width_car < GeneralStorage.car_red.sp.getY()){
					
					actionResolver.submitScore(points);
					
					state = GameState.GAMEOVER;
					GameOverDraw.time_acu_gameover=0;
					
					color_square_past=1;
					square_past = i;
					
					show_blue_particles=false;
					show_red_particles=false;
					
					parpadear = true;
			
					
					//Storing best score
					if(points>prefs.getInteger("best",0)){
						prefs.putInteger("best", points);
						prefs.flush();
					}
					
					best = prefs.getInteger("best",0);
				}
				}
				
				
				//Red square
				else{
					if(GeneralStorage.array_red[i].sp.getY() <= GeneralStorage.static_height_car + GeneralStorage.height_car*0.98 && GeneralStorage.array_red[i].sp.getY() >= GeneralStorage.static_height_car - GeneralStorage.width_car * 1.04){
						
						
						//Crash with a red square
						if(GeneralStorage.array_red[i].side==1 && GeneralStorage.car_red.sp.getX() <= GeneralStorage.four_positions_figures[0] + GeneralStorage.width_car * 0.96){
							actionResolver.submitScore(points);
							state = GameState.GAMEOVER;
							GameOverDraw.time_acu_gameover=0;
							
							if(prefs.getInteger("sound",1)==1)
							sound_crash.play();
						
							p.setPosition(GeneralStorage.array_red[i].sp.getX() + GeneralStorage.array_red[i].sp.getWidth()/2, GeneralStorage.array_red[i].sp.getY());
							p.start();
							
							show_blue_particles=false;
							show_red_particles=true;
							parpadear=false;
							GeneralStorage.array_red[i].move=false;
						
							
							//Storing best score
							if(points>prefs.getInteger("best",0)){
								prefs.putInteger("best", points);
								prefs.flush();
							}
							
							best = prefs.getInteger("best",0);
						}
						
						//Not crash
						else if(GeneralStorage.array_red[i].side==2 && GeneralStorage.car_red.sp.getX() + GeneralStorage.width_car >= GeneralStorage.four_positions_figures[1] + GeneralStorage.width_car*0.04){
							actionResolver.submitScore(points);
							state = GameState.GAMEOVER;
							GameOverDraw.time_acu_gameover=0;
							
							if(prefs.getInteger("sound",1)==1)
							sound_crash.play();
			
							p.setPosition(GeneralStorage.array_red[i].sp.getX() + GeneralStorage.array_red[i].sp.getWidth()/2, GeneralStorage.array_red[i].sp.getY());
							p.start();
							
							show_blue_particles=false;
							show_red_particles=true;
							parpadear=false;
							GeneralStorage.array_red[i].move=false;
				
							
							//Storing best score
							if(points>prefs.getInteger("best",0)){
								prefs.putInteger("best", points);
								prefs.flush();
							}
							
							best = prefs.getInteger("best",0);
						}
						
					}
					//Checking if the current square has arrived to the end of the screen
					else if(GeneralStorage.array_red[i].sp.getY()<= -GeneralStorage.width_car){
							//The figure stops its movement
							GeneralStorage.array_red[i].move=false;
							//The figure position is changed by the original
							GeneralStorage.array_red[i].sp.setPosition(GeneralStorage.four_positions_figures[0],(int) (h*1.1));
				
						}
	
				}
				
				GeneralStorage.array_red[i].sp.setPosition(GeneralStorage.array_red[i].sp.getX(), (int) (GeneralStorage.array_red[i].sp.getY()-pixels_move));


			}
		
		
		
	
			//The four figures are check. If the current figure is being moved its position is changed
			if(GeneralStorage.array_blue[i].move){
				
				
				//Blue circle
				if(GeneralStorage.array_blue[i].type==1){
				
				if(GeneralStorage.array_blue[i].sp.getY() <= GeneralStorage.static_height_car + GeneralStorage.height_car - GeneralStorage.width_car * 0.65 && GeneralStorage.array_blue[i].sp.getY()>=GeneralStorage.static_height_car-GeneralStorage.width_car) 
						
					
					if(GeneralStorage.array_blue[i].side==1){
						
						if(GeneralStorage.car_blue.sp.getX()<GeneralStorage.four_positions_figures[2]+GeneralStorage.width_car-GeneralStorage.width_car*0.4){
							GeneralStorage.array_blue[i].move=false;
							points++;
							if(prefs.getInteger("sound",1)==1)
							sound_ok.play();
						}
					}else{
						if(GeneralStorage.car_blue.sp.getX()+GeneralStorage.width_car>(GeneralStorage.four_positions_figures[3]+GeneralStorage.width_car*0.4)){
							GeneralStorage.array_blue[i].move=false;
							points++;
							if(prefs.getInteger("sound",1)==1)
							sound_ok.play();
						}
					}
				
				if(GeneralStorage.array_blue[i].sp.getY() + GeneralStorage.width_car < GeneralStorage.car_blue.sp.getY()){
					actionResolver.submitScore(points);
					state = GameState.GAMEOVER;
					GameOverDraw.time_acu_gameover=0;
					
					color_square_past=2;
					square_past = i;
					parpadear=true;
					show_blue_particles=false;
					show_red_particles=false;
					
					
					//Storing best score
					if(points>prefs.getInteger("best",0)){
						prefs.putInteger("best", points);
						prefs.flush();
					}
					
					best = prefs.getInteger("best",0);
				}
				}
				
				
				//Blue square
				else{
					if(GeneralStorage.array_blue[i].sp.getY() <= GeneralStorage.static_height_car + GeneralStorage.height_car*0.98 && GeneralStorage.array_blue[i].sp.getY() >= GeneralStorage.static_height_car - GeneralStorage.width_car * 1.04){
						
						//Crash with a blue square
						if(GeneralStorage.array_blue[i].side==1 && GeneralStorage.car_blue.sp.getX() <= GeneralStorage.four_positions_figures[2] + GeneralStorage.width_car * 0.96){
							actionResolver.submitScore(points);
							state = GameState.GAMEOVER;
							GameOverDraw.time_acu_gameover=0;
							
							if(prefs.getInteger("sound",1)==1)
							sound_crash.play();
							
							p2.setPosition(GeneralStorage.array_blue[i].sp.getX() + GeneralStorage.array_blue[i].sp.getWidth()/2, GeneralStorage.array_blue[i].sp.getY());
							p2.start();
							parpadear=false;
							show_blue_particles=true;
							show_red_particles=false;
							
							GeneralStorage.array_blue[i].move=false;
						
							
							//Storing best score
							if(points>prefs.getInteger("best",0)){
								prefs.putInteger("best", points);
								prefs.flush();
							}
							
							best = prefs.getInteger("best",0);
						}
						
						else if(GeneralStorage.array_blue[i].side==2 && GeneralStorage.car_blue.sp.getX() + GeneralStorage.width_car >= GeneralStorage.four_positions_figures[3]  + GeneralStorage.width_car*0.04){
							actionResolver.submitScore(points);
							state = GameState.GAMEOVER;
							GameOverDraw.time_acu_gameover=0;
							
							if(prefs.getInteger("sound",1)==1)
							sound_crash.play();
						
							
							p2.setPosition(GeneralStorage.array_blue[i].sp.getX() + GeneralStorage.array_blue[i].sp.getWidth()/2, GeneralStorage.array_blue[i].sp.getY());
							p2.start();
							
							show_blue_particles=true;
							show_red_particles=false;
							parpadear=false;
							GeneralStorage.array_blue[i].move=false;
							
							//Storing best score
							if(points>prefs.getInteger("best",0)){
								prefs.putInteger("best", points);
								prefs.flush();
							}
							
							best = prefs.getInteger("best",0);
						}
						
					}
					
					//Checking if the current square figure has arrived to the end of the screen
					else  if(GeneralStorage.array_blue[i].sp.getY()<= - GeneralStorage.width_car){
						//The figure stops its movement
						GeneralStorage.array_blue[i].move=false;
						//The figure position is changed by the original
						GeneralStorage.array_blue[i].sp.setPosition((int) ((w-h*0.1)/2),(int) (h*1.1));
						
						
					
					}
					
					

				}
				
				GeneralStorage.array_blue[i].sp.setPosition(GeneralStorage.array_blue[i].sp.getX(), (int) (GeneralStorage.array_blue[i].sp.getY()-pixels_move));
				

		}
		}
		
		///////////////////////////////////////////////////////////END OF MOVEMENT////////////////////////////////////////////////////////////////
		


		
		
		
		
		///////////////////////////////////////////////////INCREASING DIFFICULTY///////////////////////////////////////////////////////////////
		
		if(points>=300){
			time=(float) 0.95;
			mult = h*h_game/time;}
		else if(points>=150){
			time=(float) 1;
			mult = h*h_game/time;}
		else if(points>=100){
			time=(float) 1.1;
			mult = h*h_game/time;}
		else if(points>=75){
			time=(float) 1.2;
			mult = h*h_game/time;
			}
		else if(points>=45){
			time=(float) 1.3;
			mult = h*h_game/time;}
		else if(points>=30){
			time=(float) 1.45;
			mult = h*h_game/time;}
		else if(points>=15){
			time=(float) 1.6;
			mult = h*h_game/time;}
		else if(points>=5){
			time=(float) 1.7;
			mult = h*h_game/time;
			}
		
		
		
		
		////////////////////////////////////////////////////////END INCREASING DIFFICULTY//////////////////////////////////////////////////////
		
		
		
		last_amount_touchs = current_amount_touchs;
		acumulator=0;
		
		
		break;
		
		
		
		///////////////////////////////////////////////////////////////MENU////////////////////////////////////////////////////////////
		
		
		case MENU:
			
			 //Drawing the menu
			 MenuDraw.draw(batch, w, h ,ratio);
			
			//Storing if the screen is touched
			pressed = Gdx.input.isTouched();
			
			if(pressed && first==true){
				//Getting where has been touched the screen
					first_x = Gdx.input.getX();
					first_y = Gdx.input.getY();
					first=false;
				
			}
			
			if(!pressed && letClick){
				
				if(first==false){
			
			
			//play_button onClick
			if(Gdx.input.getX()>=MenuStorage.play_button.x_from &&  Gdx.input.getX()<=MenuStorage.play_button.x_to_click &&  Gdx.input.getY()>=MenuStorage.play_button.y_from_click &&  Gdx.input.getY()<=MenuStorage.play_button.y_to_click &&
					first_x>=MenuStorage.play_button.x_from &&  first_x<=MenuStorage.play_button.x_to_click &&  first_y>=MenuStorage.play_button.y_from_click &&  first_y<=MenuStorage.play_button.y_to_click){
				state = GameState.RUNNING;
				
				if(!loaded_pause){
					PauseStorage.load(w, h, ratio);
					loaded_pause=true;
				}
				
				prepare_game();
				first=true;
		
				restore_sizes_menu();

			}
			
			
			//ranking_button onClick
			if(Gdx.input.getX()>=MenuStorage.ranking_button.x_from &&  Gdx.input.getX()<=MenuStorage.ranking_button.x_to_click &&  Gdx.input.getY()>=MenuStorage.ranking_button.y_from_click &&  Gdx.input.getY()<=MenuStorage.ranking_button.y_to_click &&
					first_x>=MenuStorage.ranking_button.x_from &&  first_x<=MenuStorage.ranking_button.x_to_click &&  first_y>=MenuStorage.ranking_button.y_from_click &&  first_y<=MenuStorage.ranking_button.y_to_click){
				
				actionResolver.showScores();
				first=true;
				restore_sizes_menu();
			}
			
			//rate_button onClick
			if(Gdx.input.getX()>=MenuStorage.rate_button.x_from &&  Gdx.input.getX()<=MenuStorage.rate_button.x_to_click &&  Gdx.input.getY()>=MenuStorage.rate_button.y_from_click &&  Gdx.input.getY()<=MenuStorage.rate_button.y_to_click &&
					first_x>=MenuStorage.rate_button.x_from &&  first_x<=MenuStorage.rate_button.x_to_click &&  first_y>=MenuStorage.rate_button.y_from_click &&  first_y<=MenuStorage.rate_button.y_to_click){
				
				actionResolver.rate();
				
				first=true;
				restore_sizes_menu();

			}
			
			//music_button onClick
			if(Gdx.input.getX()>=MenuStorage.music_button.x_from &&  Gdx.input.getX()<=MenuStorage.music_button.x_to_click &&  Gdx.input.getY()>=MenuStorage.music_button.y_from_click &&  Gdx.input.getY()<=MenuStorage.music_button.y_to_click &&
					first_x>=MenuStorage.music_button.x_from &&  first_x<=MenuStorage.music_button.x_to_click &&  first_y>=MenuStorage.music_button.y_from_click &&  first_y<=MenuStorage.music_button.y_to_click){
				
				if(prefs.getInteger("music",1)==1){
				MenuStorage.music_button.sp.setTexture(new Texture("boton_musica_off.png"));
				prefs.putInteger("music", 0);
				prefs.flush();
				
				background_music.pause();
				}
				else{			
						MenuStorage.music_button.sp.setTexture(new Texture("boton_musica.png"));
						prefs.putInteger("music", 1);
						prefs.flush();
						background_music.play();
				}
				
				first=true;
				

			}
			
			//sound_button onClick
			if(Gdx.input.getX()>=MenuStorage.sound_button.x_from &&  Gdx.input.getX()<=MenuStorage.sound_button.x_to_click &&  Gdx.input.getY()>=MenuStorage.sound_button.y_from_click &&  Gdx.input.getY()<=MenuStorage.sound_button.y_to_click &&
					first_x>=MenuStorage.sound_button.x_from &&  first_x<=MenuStorage.sound_button.x_to_click &&  first_y>=MenuStorage.sound_button.y_from_click &&  first_y<=MenuStorage.sound_button.y_to_click){
				
				if(prefs.getInteger("sound",1)==1){
				MenuStorage.sound_button.sp.setTexture(new Texture("boton_audio_off.png"));
				prefs.putInteger("sound", 0);
				prefs.flush();
				}
				else{			
						MenuStorage.sound_button.sp.setTexture(new Texture("boton_audio.png"));
						prefs.putInteger("sound", 1);
						prefs.flush();
						
				}
				
				first=true;
				

			}
			
				}
			}
			
			//If the screen is touched
			else{
				
				MenuStorage.play_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				MenuStorage.ranking_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				MenuStorage.rate_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				MenuStorage.music_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				MenuStorage.sound_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
			}
			
			//Decrease size of the button if the screen is not touched
			if(!pressed){
				MenuStorage.play_button.decrease_size(Gdx.graphics.getDeltaTime());
				MenuStorage.ranking_button.decrease_size(Gdx.graphics.getDeltaTime());
				MenuStorage.rate_button.decrease_size(Gdx.graphics.getDeltaTime());
				MenuStorage.music_button.decrease_size(Gdx.graphics.getDeltaTime());
				MenuStorage.sound_button.decrease_size(Gdx.graphics.getDeltaTime());
			}

			
			
			
			if(!pressed)
				first=true;

			
			break;
			
		case GAMEOVER:
			
			first=true;
			
			//Drawing the game over
			GameOverDraw.draw(batch, w, h, ratio, Gdx.graphics.getDeltaTime(), square_past, color_square_past, parpadear);
			
			break;
			
			
		case GAMEOVER_MENU:
		
			//Drawing game over menu
			GameOverMenuDraw.draw(batch, w, h, ratio);
			
			//Storing if the screen is touched
			pressed = Gdx.input.isTouched();
			
			
			if(pressed && first==true){
				//Getting where has been touched the screen
					first_x = Gdx.input.getX();
					first_y = Gdx.input.getY();
					first=false;
				
			}
			
			if(!pressed && letClick){
				
				if(first==false){
			
			
			//replay_button onClick
			if(Gdx.input.getX()>=GameOverMenuStorage.replay_button.x_from &&  Gdx.input.getX()<=GameOverMenuStorage.replay_button.x_to_click &&  Gdx.input.getY()>=GameOverMenuStorage.replay_button.y_from_click &&  Gdx.input.getY()<=GameOverMenuStorage.replay_button.y_to_click &&
					first_x>=GameOverMenuStorage.replay_button.x_from &&  first_x<=GameOverMenuStorage.replay_button.x_to_click &&  first_y>=GameOverMenuStorage.replay_button.y_from_click &&  first_y<=GameOverMenuStorage.replay_button.y_to_click){
				state = GameState.RUNNING;
				prepare_game();
				first=true;
				restore_sizes_menu_gameover();

			}
			
			//home_button onClick
			if(Gdx.input.getX()>=GameOverMenuStorage.home_button.x_from &&  Gdx.input.getX()<=GameOverMenuStorage.home_button.x_to_click &&  Gdx.input.getY()>=GameOverMenuStorage.home_button.y_from_click &&  Gdx.input.getY()<=GameOverMenuStorage.home_button.y_to_click &&
					first_x>=GameOverMenuStorage.home_button.x_from &&  first_x<=GameOverMenuStorage.home_button.x_to_click &&  first_y>=GameOverMenuStorage.home_button.y_from_click &&  first_y<=GameOverMenuStorage.home_button.y_to_click){
				state = GameState.MENU;
				first=true;

				prepare_game();
				restore_sizes_menu_gameover();
			}
			
			//ranking_button onClick
			if(Gdx.input.getX()>=GameOverMenuStorage.ranking_button.x_from &&  Gdx.input.getX()<=GameOverMenuStorage.ranking_button.x_to_click &&  Gdx.input.getY()>=GameOverMenuStorage.ranking_button.y_from_click &&  Gdx.input.getY()<=GameOverMenuStorage.ranking_button.y_to_click &&
					first_x>=GameOverMenuStorage.ranking_button.x_from &&  first_x<=GameOverMenuStorage.ranking_button.x_to_click &&  first_y>=GameOverMenuStorage.ranking_button.y_from_click &&  first_y<=GameOverMenuStorage.ranking_button.y_to_click){
			
				first=true;
				actionResolver.showScores();

			}
			
			
			//share_button onClick
			if(Gdx.input.getX()>=GameOverMenuStorage.share_button.x_from &&  Gdx.input.getX()<=GameOverMenuStorage.share_button.x_to_click &&  Gdx.input.getY()>=GameOverMenuStorage.share_button.y_from_click &&  Gdx.input.getY()<=GameOverMenuStorage.share_button.y_to_click &&
					first_x>=GameOverMenuStorage.share_button.x_from &&  first_x<=GameOverMenuStorage.share_button.x_to_click &&  first_y>=GameOverMenuStorage.share_button.y_from_click &&  first_y<=GameOverMenuStorage.share_button.y_to_click){
			
				first=true;
				
				actionResolver.share(points);

			}
			
	
			
				}
			}
			
			
			else{
				
				GameOverMenuStorage.replay_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				GameOverMenuStorage.home_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				GameOverMenuStorage.ranking_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				GameOverMenuStorage.share_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
		
				
			}
			
			if(!pressed){
				GameOverMenuStorage.replay_button.decrease_size(Gdx.graphics.getDeltaTime());
				GameOverMenuStorage.home_button.decrease_size(Gdx.graphics.getDeltaTime());
				GameOverMenuStorage.ranking_button.decrease_size(Gdx.graphics.getDeltaTime());
				GameOverMenuStorage.share_button.decrease_size(Gdx.graphics.getDeltaTime());
			
			}
			

			
			if(!pressed)
				first=true;
		
		break;
		
		case PAUSE:
		
			
			//Drawing pause menu
			PauseDraw.draw(batch, w, h, ratio);
			 

			 pressed = Gdx.input.isTouched();
			 
				if(pressed && first==true){
					//Getting where has been touched the screen
						first_x = Gdx.input.getX();
						first_y = Gdx.input.getY();
						first=false;
					
				}
			
				if(!pressed && letClick){
					
					if(first==false){
				
				
				//home_pause_button onClick
				if(Gdx.input.getX()>=PauseStorage.home_pause_button.x_from &&  Gdx.input.getX()<=PauseStorage.home_pause_button.x_to_click &&  Gdx.input.getY()>=PauseStorage.home_pause_button.y_from_click &&  Gdx.input.getY()<=PauseStorage.home_pause_button.y_to_click &&
						first_x>=PauseStorage.home_pause_button.x_from &&  first_x<=PauseStorage.home_pause_button.x_to_click &&  first_y>=PauseStorage.home_pause_button.y_from_click &&  first_y<=PauseStorage.home_pause_button.y_to_click){
					
					
					state = GameState.MENU;
					first=true;
					prepare_game();
					
				}
					
					

					//play_pause_button onClick
					if(Gdx.input.getX()>=PauseStorage.play_pause_button.x_from &&  Gdx.input.getX()<=PauseStorage.play_pause_button.x_to_click &&  Gdx.input.getY()>=PauseStorage.play_pause_button.y_from_click &&  Gdx.input.getY()<=PauseStorage.play_pause_button.y_to_click &&
							first_x>=PauseStorage.play_pause_button.x_from &&  first_x<=PauseStorage.play_pause_button.x_to_click &&  first_y>=PauseStorage.play_pause_button.y_from_click &&  first_y<=PauseStorage.play_pause_button.y_to_click){
						
						state = GameState.RUNNING;
						first=true;
						
					}
					
					
					
					}
					
				}
				
				else{
					
					PauseStorage.home_pause_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
					PauseStorage.play_pause_button.increase_or_decrease(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getDeltaTime());
				}
				
				if(!pressed){
					PauseStorage.home_pause_button.decrease_size(Gdx.graphics.getDeltaTime());
					PauseStorage.play_pause_button.decrease_size(Gdx.graphics.getDeltaTime());
					first=true;
				}
		
			
			
			break;
			
	}
		
		
	}
	
	
	//This method prepares the game
	public void prepare_game(){
		
		last_red = 0;
		last_blue = 0;
		//Initializing points
		points=0;
		//Initializing separation between figures
		separation = (float) (h*0.2);
		//Initializing time
		time = (float) 1.8;
		mult = h*h_game/time;
		
		
		//Initializing the figures and positioning it
		 for(i=0;i<3;i++){
			 
			 
			 //Figures texture
			 GeneralStorage.array_red[i].sp.setTexture(GeneralStorage.circle_red_texture);
			 GeneralStorage.array_blue[i].sp.setTexture(GeneralStorage.circle_blue_texture);
			 
			 //Positioning the figures
			 GeneralStorage.array_red[i].sp.setPosition(GeneralStorage.four_positions_figures[0], (int) (h*1.1));
			 GeneralStorage.array_blue[i].sp.setPosition(GeneralStorage.four_positions_figures[2], (int) (h*1.1 + (separation+GeneralStorage.width_car)/2));
			 
			 
			 GeneralStorage.array_red[i].side=1;
			 GeneralStorage.array_blue[i].side=1;
			 
			 GeneralStorage.array_red[i].sp.setSize(GeneralStorage.width_car, GeneralStorage.width_car);
			 GeneralStorage.array_blue[i].sp.setSize(GeneralStorage.width_car, GeneralStorage.width_car);
			 
			 
			 //First figure of each color
			 if(i==0){
			 GeneralStorage.array_red[i].move=true;
			 GeneralStorage.array_blue[i].move=true;
			 
		
			 GeneralStorage.array_red[i].type=1;
			 GeneralStorage.array_blue[i].type=1;
			 
			 }
			 else{
				 GeneralStorage.array_red[i].move=false;
				 GeneralStorage.array_blue[i].move=false;
			 }
			 GeneralStorage.array_red[i].speed=10;
			 GeneralStorage.array_blue[i].speed=10;
			 
			 
		 }
		 
		 //Positioning the cars
		 GeneralStorage.car_red.sp.setPosition(GeneralStorage.four_positions_figures[0], (int) (GeneralStorage.static_height_car));
		 GeneralStorage.car_blue.sp.setPosition(GeneralStorage.four_positions_figures[3], (int) (GeneralStorage.static_height_car));
		 
		 GeneralStorage.car_blue.movement=2;
		 GeneralStorage.car_red.movement=1;
		 
		 GeneralStorage.car_blue.restore();
		 GeneralStorage.car_red.restore();

		 
 

		
	}
	
	///////////////////////////////////////////PUT NEW FIGURES IN MOVEMENT//////////////////////////////////////////////////
	
	public void	put_new_figures_in_movement(){
		
		//////////////////////////////////////////////////////RED////////////////////////////////////////////////////////////
		
		//If last_red figure sent was first, second or third
		if(last_red<2){
			//If last_red figure sent has passed the separation established 
			if(GeneralStorage.array_red[last_red].sp.getY()<=(h-GeneralStorage.width_car-separation) && GeneralStorage.array_red[last_red+1].move==false){
				//The next figure that is waiting to be moved begin its movement
				GeneralStorage.array_red[last_red+1].move=true;
				//Random figure
				if(Math.random()<=0.5){
					GeneralStorage.array_red[last_red+1].sp.setTexture(GeneralStorage.circle_red_texture);
					GeneralStorage.array_red[last_red+1].type=1;
				}
				else {GeneralStorage.array_red[last_red+1].sp.setTexture(GeneralStorage.square_red_texture);
				GeneralStorage.array_red[last_red+1].type=2;
				}
				
				if(Math.random()<=0.5){
					GeneralStorage.array_red[last_red+1].sp.setPosition(GeneralStorage.four_positions_figures[0], (float) (h*1.1));
					GeneralStorage.array_red[last_red+1].setSide(1);
				}else{
					GeneralStorage.array_red[last_red+1].sp.setPosition(GeneralStorage.four_positions_figures[1], (float) (h*1.1));
					GeneralStorage.array_red[last_red+1].setSide(2);
				}
				
				//Incrementing last_red figure that has began its movement
				last_red=last_red+1;
			}
			//last red figure sent has been the last red in the red array
		}else {
			//If last red figure sent has passed the separation established 
			if(GeneralStorage.array_red[2].sp.getY()<=(h-GeneralStorage.width_car-separation) && GeneralStorage.array_red[0].move==false){
				//Random figure
				if(Math.random()<=0.5){
					GeneralStorage.array_red[0].sp.setTexture(GeneralStorage.circle_red_texture);
					GeneralStorage.array_red[0].type=1;
				}
				else {GeneralStorage.array_red[0].sp.setTexture(GeneralStorage.square_red_texture);
				GeneralStorage.array_red[0].type=2;
				}
				
				if(Math.random()<=0.5){
					GeneralStorage.array_red[0].sp.setPosition(GeneralStorage.four_positions_figures[0], (float) (h*1.1));
					GeneralStorage.array_red[0].setSide(1);
				}else{
					GeneralStorage.array_red[0].sp.setPosition(GeneralStorage.four_positions_figures[1], (float) (h*1.1));
					GeneralStorage.array_red[0].setSide(2);
				}
				
				//The next figure that is waiting to be moved begin its movement
				GeneralStorage.array_red[0].move=true;
				last_red=0;
		}
		}
		
		//////////////////////////////////////////////////////////////END RED/////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//////////////////////////////////////////////////////////////BLUE/////////////////////////////////////////////////////////
		
		
		//If last_blue figure sent was first, second or third
		if(last_blue<2){
			//If last_blue figure sent has passed the separation established 
			if(GeneralStorage.array_blue[last_blue].sp.getY()<=(h-GeneralStorage.width_car-separation) && GeneralStorage.array_blue[last_blue+1].move==false){
				//The next figure that is waiting to be moved begin its movement
				GeneralStorage.array_blue[last_blue+1].move=true;
				//Random figure
				if(Math.random()<=0.5){
					GeneralStorage.array_blue[last_blue+1].sp.setTexture(GeneralStorage.circle_blue_texture);
					GeneralStorage.array_blue[last_blue+1].type=1;
				}
				else {GeneralStorage.array_blue[last_blue+1].sp.setTexture(GeneralStorage.square_blue_texture);
				GeneralStorage.array_blue[last_blue+1].type=2;
				}
				
				if(Math.random()<=0.5){
					GeneralStorage.array_blue[last_blue+1].sp.setPosition(GeneralStorage.four_positions_figures[2], (float) (h*1.1));
					GeneralStorage.array_blue[last_blue+1].setSide(1);
				}else{
					GeneralStorage.array_blue[last_blue+1].sp.setPosition(GeneralStorage.four_positions_figures[3], (float) (h*1.1));
					GeneralStorage.array_blue[last_blue+1].setSide(2);
				}
				
				//Incrementing last_blue figure that has began its movement
				last_blue=last_blue+1;
			}
			//last blue figure sent has been the last_blue in the blue array
		}else {
			//If last_blue figure sent has passed the separation established 
			if(GeneralStorage.array_blue[2].sp.getY()<=(h-GeneralStorage.width_car-separation) && GeneralStorage.array_blue[0].move==false){
				//Random figure
				if(Math.random()<=0.5){
					GeneralStorage.array_blue[0].sp.setTexture(GeneralStorage.circle_blue_texture);
					GeneralStorage.array_blue[0].type=1;
				}
				else {GeneralStorage.array_blue[0].sp.setTexture(GeneralStorage.square_blue_texture);
				GeneralStorage.array_blue[0].type=2;
				}
				
				if(Math.random()<=0.5){
					GeneralStorage.array_blue[0].sp.setPosition(GeneralStorage.four_positions_figures[2], (float) (h*1.1 ));
					GeneralStorage.array_blue[0].setSide(1);
				}else{
					GeneralStorage.array_blue[0].sp.setPosition(GeneralStorage.four_positions_figures[3], (float) (h*1.1));
					GeneralStorage.array_blue[0].setSide(2);
				}
				
				//The next figure that is waiting to be moved begin its movement
				GeneralStorage.array_blue[0].move=true;
				last_blue=0;
		}
		}
		
		
		//////////////////////////////////////////////////////////////END BLUE////////////////////////////////////////////////////////
		
	}
	
	//Restoring menu button size
	public void restore_sizes_menu(){
		
		MenuStorage.play_button.restore_size();
		MenuStorage.ranking_button.restore_size();
		MenuStorage.rate_button.restore_size();
		MenuStorage.music_button.restore_size();
		MenuStorage.sound_button.restore_size();
		
	}
	
	
	//Restoring game over menu buttons size
	public void restore_sizes_menu_gameover(){
		GameOverMenuStorage.replay_button.restore_size();
		PauseStorage.home_pause_button.restore_size();
		GameOverMenuStorage.home_button.restore_size();
		PauseStorage.play_pause_button.restore_size();
	}
	
	//Generating a random number
	public static int randomNum(int min, int max) {
		Random rand = new Random();
		int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
		return random;
	}
	
}
