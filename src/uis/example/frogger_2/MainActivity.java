package uis.example.frogger_2;

import java.io.IOException;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends Activity {

	SoundPool soundPool;
	int mySound = 0; //-1 means no sound
	
	GameView gv;
	Paint drawPaint = new Paint();
	Paint rectPaint = new Paint();
	Paint labelPaint = new Paint();
	
	Bitmap background, frog1, frog2, frog3, frogDead, carRow1, carRow2, carRow3, carRow4, turtleShells1, turtleShells2, turtleShells3;
	Bitmap smallLog1, smallLog2, smallLog3, largeLog1, largeLog2, largeLog3, mediumLog1, mediumLog2, mediumLog3, turtleShellsRow2_1, turtleShellsRow2_2, turtleShellsRow2_3;
	
	//For updating coordinates
	float futureX, futureY, currentX, currentY;
	
	boolean noLateralMovement = false;
	
	//Speeds
	int carSpeed = 20;
	int turtleShellsSpeed = 15;
	int smallLogSpeed = 15;
	int largeLogSpeed = 15;
	int mediumLogSpeed = 15;

	//Starting positions for frogs
	float frog1X = 180, frog1Y = 1600; 
	float frog2X = 90, frog2Y = 1600;
	float frog3X = 0, frog3Y = 1600;
	
	//Starting positions for cars
	int carRow1X = 0, carRow1Y = 1400;
	int carRow2X = 1400, carRow2Y = 1275;
	int carRow3X = -300, carRow3Y = 1150;
	int carRow4X = 1100, carRow4Y = 1025;
	
	//Starting position for turtle shells
	int turtleShells1X = 1400, turtleShells2X = 1900, turtleShells3X =  2400;
	int turtleShellsY = 750;
	
	//Starting position for small logs
	int smallLog1X = -20, smallLog2X = -520, smallLog3X = -920;
	int smallLogY = 650;
	
	//Starting position for medium logs
	int mediumLog1X = -200, mediumLog2X = -600, mediumLog3X = -1000;
	int mediumLogY = 450;
	
	//Starting position large logs
	int largeLog1X = 1000, largeLog2X = 1500, largeLog3X = 2000;
	int largeLogY = 550;
	
	//Starting position for second row of turtle shells
	int turtleShellsRow2_1X = 500, turtleShellsRow2_2X = 1000, turtleShellsRow2_3X = 1500;
	int turtleShellsRow2Y = 350;
	
	//Which frog is currently active
	boolean frog1Active = true, frog2Active = false, frog3Active = false;
	
	//Keep track of frog's movements through the items in water so that 
	//the frog's coordinates will match that of the item in the water (logs, turtle shells, etc)
	int onShells = 0, onSmallLog = 0, onMediumLog = 0, onLargeLog = 0, onShellsRow2;
	
	boolean shellsMutex = false, smallLogsMutex = false, mediumLogsMutex = false, largeLogsMutex = false, shellsRow2Mutex = false;
	
	boolean frog1OnTurtleShells1 = false, frog1OnTurtleShells2 = false, frog1OnTurtleShells3 = false;
	boolean frog2OnTurtleShells1 = false, frog2OnTurtleShells2 = false, frog2OnTurtleShells3 = false;
	boolean frog3OnTurtleShells1 = false, frog3OnTurtleShells2 = false, frog3OnTurtleShells3 = false;
	
	boolean frog1OnSmallLog1 = false, frog1OnSmallLog2 = false, frog1OnSmallLog3 = false;
	boolean frog2OnSmallLog1 = false, frog2OnSmallLog2 = false, frog2OnSmallLog3 = false;
	boolean frog3OnSmallLog1 = false, frog3OnSmallLog2 = false, frog3OnSmallLog3 = false;
	
	boolean frog1OnMediumLog1 = false, frog1OnMediumlLog2 = false, frog1OnMediumLog3 = false;
	boolean frog2OnMediumLog1 = false, frog2OnMediumlLog2 = false, frog2OnMediumLog3 = false;
	boolean frog3OnMediumLog1 = false, frog3OnMediumlLog2 = false, frog3OnMediumLog3 = false;
		
	boolean frog1OnLargeLog1 = false, frog1OnLargeLog2 = false, frog1OnLargeLog3 = false;
	boolean frog2OnLargeLog1 = false, frog2OnLargeLog2 = false, frog2OnLargeLog3 = false;
	boolean frog3OnLargeLog1 = false, frog3OnLargeLog2 = false, frog3OnLargeLog3 = false;
	
	boolean frog1OnTurtleShellsRow2_1 = false, frog1OnTurtleShellsRow2_2 = false, frog1OnTurtleShellsRow2_3 = false;
	boolean frog2OnTurtleShellsRow2_1 = false, frog2OnTurtleShellsRow2_2 = false, frog2OnTurtleShellsRow2_3 = false;
	boolean frog3OnTurtleShellsRow2_1 = false, frog3OnTurtleShellsRow2_2 = false, frog3OnTurtleShellsRow2_3 = false;
	
	boolean winner = false; 
	
	//Score
	int score = 0;
	
	//Turn
	int turn = 1;
	
	//How far to move on each onTouch event
	int movement = 30;
	
	//Rectangle to detect collisions
	Rect frog1Rect, frog2Rect, frog3Rect;
	Rect carRow1Rect, carRow2Rect, carRow3Rect, carRow4Rect; 
	Rect turtleShells1Rect, turtleShells2Rect, turtleShells3Rect;
	Rect smallLog1Rect, smallLog2Rect, smallLog3Rect;
	Rect mediumLog1Rect, mediumLog2Rect, mediumLog3Rect;
	Rect largeLog1Rect, largeLog2Rect, largeLog3Rect;
	Rect turtleShellsRow2_1Rect, turtleShellsRow2_2Rect, turtleShellsRow2_3Rect;
	Rect winnerSquareRect1, winnerSquareRect2, winnerSquareRect3, winnerSquareRect4, winnerSquareRect5;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//How many sounds, stream music, source quality (0 is best)
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		AssetManager assetManager = getAssets();
		
		try {
			AssetFileDescriptor descriptor  = assetManager.openFd("gameover.wav");
			mySound = soundPool.load(descriptor, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        //setContentView(R.layout.activity_main);
        gv = new GameView(this);
        this.setContentView(gv);
        
       background = BitmapFactory.decodeResource(getResources(), R.drawable.background_big);
       frog1 = BitmapFactory.decodeResource(getResources(), R.drawable.frog_up);
       frog2 = BitmapFactory.decodeResource(getResources(), R.drawable.frog_up);
       frog3 = BitmapFactory.decodeResource(getResources(), R.drawable.frog_up);
       frogDead = BitmapFactory.decodeResource(getResources(), R.drawable.frog_dead);
       carRow1 = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_car_right);
       carRow2 = BitmapFactory.decodeResource(getResources(), R.drawable.blue_car_left);
       carRow3 = BitmapFactory.decodeResource(getResources(), R.drawable.white_car_right);
       carRow4 = BitmapFactory.decodeResource(getResources(), R.drawable.red_car_left);
       turtleShells1 = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_shells);
       turtleShells2 = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_shells);
       turtleShells3 = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_shells);
       smallLog1 = BitmapFactory.decodeResource(getResources(), R.drawable.sml_log);
       smallLog2 = BitmapFactory.decodeResource(getResources(), R.drawable.sml_log);
       smallLog3 = BitmapFactory.decodeResource(getResources(), R.drawable.sml_log);
       mediumLog1 = BitmapFactory.decodeResource(getResources(), R.drawable.mdm_log);
       mediumLog2 = BitmapFactory.decodeResource(getResources(), R.drawable.mdm_log);
       mediumLog3 = BitmapFactory.decodeResource(getResources(), R.drawable.mdm_log);
       largeLog1 = BitmapFactory.decodeResource(getResources(), R.drawable.large_log);
       largeLog2 = BitmapFactory.decodeResource(getResources(), R.drawable.large_log);
       largeLog3 = BitmapFactory.decodeResource(getResources(), R.drawable.large_log);
       turtleShellsRow2_1 = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_shells_two);
       turtleShellsRow2_2 = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_shells_two);
       turtleShellsRow2_3 = BitmapFactory.decodeResource(getResources(), R.drawable.turtle_shells_two);
  
    }

    public void playSound(View view) {
		soundPool.play(mySound, 1, 1, 0, 0, 1);
	}

    @Override
    protected void onPause() {
    	super.onPause();
    	gv.pause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	gv.resume();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    public class GameView extends SurfaceView implements Runnable {

    	Thread ViewThread = null;
    	SurfaceHolder holder;
    	boolean threadOK = true;
    	
		public GameView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = this.getHolder();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			//Lock and post to prevent screen flicker
			while (threadOK == true) {
				if (!holder.getSurface().isValid()) {
					continue;
				}
				
				Canvas gameCanvas = holder.lockCanvas();

				//Set up frog rectangles
				frog1Rect = new Rect((int)frog1X, (int)frog1Y, (int)frog1X + (int)frog1.getWidth(), (int)frog1Y + (int)frog1.getHeight());
				frog2Rect = new Rect((int)frog2X, (int)frog2Y, (int)frog2X + (int)frog2.getWidth(), (int)frog2Y + (int)frog2.getHeight());
				frog3Rect = new Rect((int)frog3X, (int)frog3Y, (int)frog3X + (int)frog3.getWidth(), (int)frog3Y + (int)frog3.getHeight());
				
				//Set up car rectangles
				carRow1Rect = new Rect((int)carRow1X, (int)carRow1Y, (int)carRow1X + (int)carRow1.getWidth(), (int)carRow1Y + (int)carRow1.getHeight());
				carRow2Rect = new Rect((int)carRow2X, (int)carRow2Y, (int)carRow2X + (int)carRow2.getWidth(), (int)carRow2Y + (int)carRow2.getHeight());
				carRow3Rect = new Rect((int)carRow3X, (int)carRow3Y, (int)carRow3X + (int)carRow3.getWidth(), (int)carRow3Y + (int)carRow3.getHeight());
				carRow4Rect = new Rect((int)carRow4X, (int)carRow4Y, (int)carRow4X + (int)carRow4.getWidth(), (int)carRow4Y + (int)carRow4.getHeight());
				
				//Set up turtle shell rectangles
				turtleShells1Rect = new Rect ((int)turtleShells1X, (int)turtleShellsY, (int)turtleShells1X + (int)turtleShells1.getWidth(), (int)turtleShellsY + (int)turtleShells1.getHeight());
				turtleShells2Rect = new Rect ((int)turtleShells2X, (int)turtleShellsY, (int)turtleShells2X + (int)turtleShells2.getWidth(), (int)turtleShellsY + (int)turtleShells2.getHeight());
				turtleShells3Rect = new Rect ((int)turtleShells3X, (int)turtleShellsY, (int)turtleShells3X + (int)turtleShells3.getWidth(), (int)turtleShellsY + (int)turtleShells3.getHeight());
				
				//Set up small log rectangles
				smallLog1Rect = new Rect ((int)smallLog1X, (int)smallLogY, (int)smallLog1X + (int)smallLog1.getWidth(), (int)smallLogY + (int)smallLog1.getHeight());
				smallLog2Rect = new Rect ((int)smallLog2X, (int)smallLogY, (int)smallLog2X + (int)smallLog2.getWidth(), (int)smallLogY + (int)smallLog2.getHeight());
				smallLog3Rect = new Rect ((int)smallLog3X, (int)smallLogY, (int)smallLog3X + (int)smallLog3.getWidth(), (int)smallLogY + (int)smallLog3.getHeight());
				
				//Set up medium logs
				mediumLog1Rect = new Rect((int)mediumLog1X, (int)mediumLogY, (int)mediumLog1X + (int)mediumLog1.getWidth(), (int)mediumLogY + (int)mediumLog1.getHeight());
				mediumLog2Rect = new Rect((int)mediumLog2X, (int)mediumLogY, (int)mediumLog2X + (int)mediumLog2.getWidth(), (int)mediumLogY + (int)mediumLog2.getHeight());
				mediumLog3Rect = new Rect((int)mediumLog3X, (int)mediumLogY, (int)mediumLog3X + (int)mediumLog3.getWidth(), (int)mediumLogY + (int)mediumLog3.getHeight());

				//Set up large log rectangles
				largeLog1Rect = new Rect((int)largeLog1X, (int)largeLogY, (int)largeLog1X + (int)largeLog1.getWidth(), (int)largeLogY + (int)largeLog1.getHeight());
				largeLog2Rect = new Rect((int)largeLog2X, (int)largeLogY, (int)largeLog2X + (int)largeLog2.getWidth(), (int)largeLogY + (int)largeLog2.getHeight());
				largeLog3Rect = new Rect((int)largeLog3X, (int)largeLogY, (int)largeLog3X + (int)largeLog3.getWidth(), (int)largeLogY + (int)largeLog3.getHeight());
				
				//Set up turtle shell rectangles
				turtleShellsRow2_1Rect = new Rect ((int)turtleShellsRow2_1X, (int)turtleShellsRow2Y, (int)turtleShellsRow2_1X + (int)turtleShellsRow2_1.getWidth(), (int)turtleShellsRow2Y + (int)turtleShellsRow2_1.getHeight());
				turtleShellsRow2_2Rect = new Rect ((int)turtleShellsRow2_2X, (int)turtleShellsRow2Y, (int)turtleShellsRow2_2X + (int)turtleShellsRow2_2.getWidth(), (int)turtleShellsRow2Y + (int)turtleShellsRow2_2.getHeight());
				turtleShellsRow2_3Rect = new Rect ((int)turtleShellsRow2_3X, (int)turtleShellsRow2Y, (int)turtleShellsRow2_3X + (int)turtleShellsRow2_3.getWidth(), (int)turtleShellsRow2Y + (int)turtleShellsRow2_3.getHeight());

				//Set up winner's squares (if the frog makes it here, it wins that round)
				winnerSquareRect1 = new Rect(20, 200, 120, 310); 
				winnerSquareRect2 = new Rect(260, 200, 360, 310);
				winnerSquareRect3 = new Rect(490, 200, 590, 310);
				winnerSquareRect4 = new Rect(720, 200, 820, 310);
				winnerSquareRect5 = new Rect(950, 200, 1050, 310);
				
				onDraw(gameCanvas);
				holder.unlockCanvasAndPost(gameCanvas);
			}
		}
		
		protected void onDraw(Canvas canvas) {
			//Draw background
			drawPaint.setAlpha(255);
		    background = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true);
			canvas.drawBitmap(background,0, 0, drawPaint);
			
			//Draw score label
			labelPaint.setAlpha(255);
			labelPaint.setColor(Color.WHITE);
			labelPaint.setTextSize(70);
			canvas.drawText("Score: " + score, 100, 100, labelPaint);
			
			//Show rectangle
			rectPaint.setAlpha(255);
			rectPaint.setColor(Color.WHITE);
			
			//canvas.drawRect(winnerSquareRect1, rectPaint);
			//canvas.drawRect(winnerSquareRect2, rectPaint);
			//canvas.drawRect(winnerSquareRect3, rectPaint);
			//canvas.drawRect(winnerSquareRect4, rectPaint);
			//canvas.drawRect(winnerSquareRect5, rectPaint);

			//canvas.drawRect(frog1Rect, rectPaint);
			//canvas.drawRect(smallLog1Rect, rectPaint);
			//canvas.drawRect(turtleShells1Rect, rectPaint);
			//canvas.drawRect(turtleShells2Rect, rectPaint);
			//canvas.drawRect(turtleShells3Rect, rectPaint);
			//canvas.drawRect(frog1Rect, rectPaint);
			//canvas.drawRect(frog3Rect, rectPaint);
			//canvas.drawRect(largeLog1Rect, rectPaint);
			//canvas.drawRect(largeLog2Rect, rectPaint);

			
			
			carRow1X += carSpeed; //Move to the right
			carRow2X -= carSpeed; //Move to the left
			carRow3X += carSpeed; //Move to the right
			carRow4X -= carSpeed; //Move to the left
			
			turtleShells1X -= turtleShellsSpeed; //Move to the left
			turtleShells2X -= turtleShellsSpeed; //Move to the left
			turtleShells3X -= turtleShellsSpeed; //Move to the left
			
			smallLog1X += smallLogSpeed;//Move to the right
			smallLog2X += smallLogSpeed;//Move to the right
			smallLog3X += smallLogSpeed;//Move to the right
			
			largeLog1X -= largeLogSpeed; //Move to the left
			largeLog2X -= largeLogSpeed; //Move to the left
			largeLog3X -= largeLogSpeed; //Move to the left
			
			mediumLog1X += mediumLogSpeed; //Move to the right
			mediumLog2X += mediumLogSpeed; //Move to the right
			mediumLog3X += mediumLogSpeed; //Move to the right
			
			turtleShellsRow2_1X -= turtleShellsSpeed; //Move to the left
			turtleShellsRow2_2X -= turtleShellsSpeed; //Move to the left
			turtleShellsRow2_3X -= turtleShellsSpeed; //Move to the left
			
			detectIntersections(canvas);
			
			//Car movement
			if (carRow1X > canvas.getWidth()) {
				carRow1X = 0;
			}
			
			if (carRow2X < 0 ) {
				carRow2X = 1400;
			}
			
			if (carRow3X > canvas.getWidth()) {
				carRow3X = -300;
			}
			
			if (carRow4X < 0 ) {
				carRow4X = 1100;
			}
			
			//Turtle shells movement
			if(turtleShells1X < -200) {
				turtleShells1X = canvas.getWidth()+400;
			}
			
			if(turtleShells2X < -200) {
				turtleShells2X = canvas.getWidth()+400;
			}
			
			if(turtleShells3X < -200) {
				turtleShells3X = canvas.getWidth()+400;
			}
			
			//Small Logs movement
			if (smallLog1X > canvas.getWidth()){
				smallLog1X = -100;
			}
			if (smallLog2X > canvas.getWidth()){
				smallLog2X = -100;
			}
			if (smallLog3X > canvas.getWidth()){
				smallLog3X = -100;
			}
			
			//Large logs movement
			if (largeLog1X < -200) {
				largeLog1X = canvas.getWidth()+400;
			}
			if (largeLog2X < -200) {
				largeLog2X = canvas.getWidth()+400;
			}
			if (largeLog3X < -200) {
				largeLog3X = canvas.getWidth()+400;
			}
			
			//medium logs movement
			if (mediumLog1X > canvas.getWidth()) {
				mediumLog1X = -200;
			} 
			if (mediumLog2X > canvas.getWidth()) {
				mediumLog2X = -200;
			}
			if (mediumLog3X > canvas.getWidth()) {
				mediumLog3X = -200;
			}
			
			//Turtle shells row 2 movement
			if(turtleShellsRow2_1X < -200) {
				turtleShellsRow2_1X = canvas.getWidth()+400;
			}
			
			if(turtleShellsRow2_2X < -200) {
				turtleShellsRow2_2X = canvas.getWidth()+400;
			}
			
			if(turtleShellsRow2_3X < -200) {
				turtleShellsRow2_3X = canvas.getWidth()+400;
			}
			
			
			setFrogCoordinates();
			
			//Draw frogs            
			if (frog1Active) {
				canvas.drawBitmap(frog1, frog1X, frog1Y, drawPaint);
				canvas.drawBitmap(frog2, frog2X, frog2Y, drawPaint);
				canvas.drawBitmap(frog3, frog3X, frog3Y, drawPaint);
			} else if (frog2Active) {
				canvas.drawBitmap(frog2, frog2X, frog2Y, drawPaint);
				canvas.drawBitmap(frog3, frog3X, frog3Y, drawPaint);
			} else if (frog3Active) {
				canvas.drawBitmap(frog3, frog3X, frog3Y, drawPaint);
			}
			
			//Draw Cars
			canvas.drawBitmap(carRow1, carRow1X, carRow1Y, drawPaint);
			canvas.drawBitmap(carRow2, carRow2X, carRow2Y, drawPaint);
			canvas.drawBitmap(carRow3, carRow3X, carRow3Y, drawPaint);
			canvas.drawBitmap(carRow4, carRow4X, carRow4Y, drawPaint);
			
			//Draw turtle shells
			canvas.drawBitmap(turtleShells1, turtleShells1X, turtleShellsY, drawPaint);
			canvas.drawBitmap(turtleShells2, turtleShells2X, turtleShellsY, drawPaint);
			canvas.drawBitmap(turtleShells3, turtleShells3X, turtleShellsY, drawPaint);
			
			//Draw small Logs
			canvas.drawBitmap(smallLog1, smallLog1X, smallLogY, drawPaint);
			canvas.drawBitmap(smallLog2, smallLog2X, smallLogY, drawPaint);
			canvas.drawBitmap(smallLog3, smallLog3X, smallLogY, drawPaint);
			
			//Draw large logs
			canvas.drawBitmap(largeLog1, largeLog1X, largeLogY, drawPaint);
			canvas.drawBitmap(largeLog2, largeLog2X, largeLogY, drawPaint);
			canvas.drawBitmap(largeLog3, largeLog3X, largeLogY, drawPaint);
			
			//Draw medium logs
			canvas.drawBitmap(mediumLog1, mediumLog1X, mediumLogY, drawPaint);
			canvas.drawBitmap(mediumLog2, mediumLog2X, mediumLogY, drawPaint);
			canvas.drawBitmap(mediumLog3, mediumLog3X, mediumLogY, drawPaint);
			
			//Draw turtle shells row 2
			canvas.drawBitmap(turtleShellsRow2_1, turtleShellsRow2_1X, turtleShellsRow2Y, drawPaint);
			canvas.drawBitmap(turtleShellsRow2_2, turtleShellsRow2_2X, turtleShellsRow2Y, drawPaint);
			canvas.drawBitmap(turtleShellsRow2_3, turtleShellsRow2_3X, turtleShellsRow2Y, drawPaint);			
			
		}
		
		public void setFrogCoordinates() {
			//#############   FROG 1 ###################
			if (frog1Active) {
				if (onShells == 1) {
					if (frog1OnTurtleShells1) {
						frog1X =  turtleShells1X;
					} else if (frog1OnTurtleShells2) {
						frog1X =  turtleShells2X;
					} else {
						frog1X =  turtleShells3X;
					}
					frog1Y = turtleShellsY;
					//Log.w("Variable", "onShells = " + onShells);
				} 
				
				if (onSmallLog == 1) {
					if (frog1OnSmallLog1) {
						frog1X = smallLog1X;
					} else if (frog1OnSmallLog2) {
						frog1X = smallLog2X;
					} else if (frog1OnSmallLog3) {
						frog1X = smallLog3X;
					}
					frog1Y = smallLogY;
					//Log.w("Variable", "onSmallLog = " + onSmallLog);
				}
				//Log.w("Variable", "onSmallLog = " + onSmallLog);
				if (onLargeLog == 1) {
					if (frog1OnLargeLog1) {
						frog1X = largeLog1X;
					} else if (frog1OnLargeLog2) {
						frog1X = largeLog2X;
					} else if (frog1OnLargeLog3) {
						frog1X = largeLog3X;
					}
					frog1Y = largeLogY;
				}
				if (onMediumLog == 1) {
					if (frog1OnMediumLog1) {
						frog1X = mediumLog1X;
					} else if (frog1OnMediumlLog2) {
						frog1X = mediumLog2X;
					} else if (frog1OnMediumLog3) {
						frog1X = mediumLog3X;
					}
					frog1Y = mediumLogY;
				}
				
				if (onShellsRow2 == 1) {
					if (frog1OnTurtleShellsRow2_1) {
						frog1X =  turtleShellsRow2_1X;
					} else if (frog1OnTurtleShellsRow2_2) {
						frog1X =  turtleShellsRow2_2X;
					} else {
						frog1X =  turtleShellsRow2_3X;
					}
					frog1Y = turtleShellsRow2Y;
					//Log.w("Variable", "onShells = " + onShells);
				} 
			}
			
			//#######################################################
			
			
			//############## FROG 2 #################################
			if (frog2Active) {
				if (onShells == 1) {
					if (frog2OnTurtleShells1) {
						frog2X =  turtleShells1X;
					} else if (frog2OnTurtleShells2) {
						frog2X =  turtleShells2X;
					} else {
						frog2X =  turtleShells3X;
					}
					frog2Y = turtleShellsY;
					//Log.w("Variable", "onShells = " + onShells);
				} 
				
				if (onSmallLog == 1) {
					if (frog2OnSmallLog1) {
						frog2X = smallLog1X;
					} else if (frog2OnSmallLog2) {
						frog2X = smallLog2X;
					} else if (frog2OnSmallLog3) {
						frog2X = smallLog3X;
					}
					frog2Y = smallLogY;
					//Log.w("Variable", "onSmallLog = " + onSmallLog);
				}
				//Log.w("Variable", "onSmallLog = " + onSmallLog);
				if (onLargeLog == 1) {
					if (frog2OnLargeLog1) {
						frog2X = largeLog1X;
					} else if (frog2OnLargeLog2) {
						frog2X = largeLog2X;
					} else if (frog2OnLargeLog3) {
						frog2X = largeLog3X;
					}
					frog2Y = largeLogY;
				}
				if (onMediumLog == 1) {
					if (frog2OnMediumLog1) {
						frog2X = mediumLog1X;
					} else if (frog2OnMediumlLog2) {
						frog2X = mediumLog2X;
					} else if (frog2OnMediumLog3) {
						frog2X = mediumLog3X;
					}
					frog2Y = mediumLogY;
				}
				
				if (onShellsRow2 == 1) {
					if (frog2OnTurtleShellsRow2_1) {
						frog2X =  turtleShellsRow2_1X;
					} else if (frog2OnTurtleShellsRow2_2) {
						frog2X =  turtleShellsRow2_2X;
					} else {
						frog2X =  turtleShellsRow2_3X;
					}
					frog2Y = turtleShellsRow2Y;
					//Log.w("Variable", "onShells = " + onShells);
				} 
			}
			
			//############################################################
			
			//#################  FROG 3 ###################################
			if (frog3Active) {
				if (onShells == 1) {
					if (frog3OnTurtleShells1) {
						frog3X =  turtleShells1X;
					} else if (frog3OnTurtleShells2) {
						frog3X =  turtleShells2X;
					} else {
						frog3X =  turtleShells3X;
					}
					frog3Y = turtleShellsY;
					//Log.w("Variable", "onShells = " + onShells);
				} 
				
				if (onSmallLog == 1) {
					if (frog3OnSmallLog1) {
						frog3X = smallLog1X;
					} else if (frog3OnSmallLog2) {
						frog3X = smallLog2X;
					} else if (frog3OnSmallLog3) {
						frog3X = smallLog3X;
					}
					frog3Y = smallLogY;
					//Log.w("Variable", "onSmallLog = " + onSmallLog);
				}
				//Log.w("Variable", "onSmallLog = " + onSmallLog);
				if (onLargeLog == 1) {
					if (frog3OnLargeLog1) {
						frog3X = largeLog1X;
					} else if (frog3OnLargeLog2) {
						frog3X = largeLog2X;
					} else if (frog3OnLargeLog3) {
						frog3X = largeLog3X;
					}
					frog3Y = largeLogY;
				}
				if (onMediumLog == 1) {
					if (frog3OnMediumLog1) {
						frog3X = mediumLog1X;
					} else if (frog3OnMediumlLog2) {
						frog3X = mediumLog2X;
					} else if (frog3OnMediumLog3) {
						frog3X = mediumLog3X;
					}
					frog3Y = mediumLogY;
				}
				
				if (onShellsRow2 == 1) {
					if (frog3OnTurtleShellsRow2_1) {
						frog3X =  turtleShellsRow2_1X;
					} else if (frog3OnTurtleShellsRow2_2) {
						frog3X =  turtleShellsRow2_2X;
					} else {
						frog3X =  turtleShellsRow2_3X;
					}
					frog3Y = turtleShellsRow2Y;
					//Log.w("Variable", "onShells = " + onShells);
				} 
			}
		}
		
		public void detectIntersections(Canvas canvas) {
		//###########################################################	
		//##############     FROG 1       ###########################
		//###########################################################
			
			if (frog1Active) {
				if (Rect.intersects(frog1Rect, carRow1Rect) || Rect.intersects(frog1Rect, carRow2Rect) || Rect.intersects(frog1Rect, carRow3Rect) || Rect.intersects(frog1Rect, carRow4Rect)) {
					frog1Active = false;
					frog2Active = true;
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
					playSound(this);
					
				} 
				
				//Log.w("Variable", "frog1Y, turtleShellsY = " + frog1Y + ", " + turtleShellsY);
				
				//If frog1 intersects with one of the turtle shells
				//I needed the shellsMutex so that onShells could only be set once. 
				if (Rect.intersects(frog1Rect, turtleShells1Rect)){ 
					if (!shellsMutex){
						frog1OnTurtleShells1 = true;
						onShells = 1;
						shellsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "Intersected with shells1");
					}
				} else if (Rect.intersects(frog1Rect, turtleShells2Rect)) {
					if (!shellsMutex) {
						frog1OnTurtleShells2 = true;
						onShells = 1;
						shellsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "Intersected with shells2");

					}
				} else if (Rect.intersects(frog1Rect, turtleShells3Rect)) {
					if (!shellsMutex) {
						frog1OnTurtleShells3 = true;
						onShells = 1;
						shellsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "Intersected with shells3");

					}
				} else if (frog1Y <= turtleShellsY + 10 && !shellsMutex) { //Jumps in water
					frog1Active = false;
					frog2Active = true;
					onShells = 0;
					noLateralMovement = false;
					resetMutexes();
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
					Log.w("Variable", "Frog 1 died in the water");
				}
				
				//if frog1 intersects with one of the small logs
				if (Rect.intersects(frog1Rect, smallLog1Rect)) {
					if (!smallLogsMutex) {
						frog1OnSmallLog1 = true;
						onSmallLog = 1;
						smallLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with small log 1");
					}
				} else if (Rect.intersects(frog1Rect, smallLog2Rect)) {
					if (!smallLogsMutex) {
						frog1OnSmallLog2 = true;
						onSmallLog = 1;
						smallLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with small log 2");
					}
				} else if (Rect.intersects(frog1Rect, smallLog3Rect)) {
					if (!smallLogsMutex) {
						frog1OnSmallLog3 = true;
						onSmallLog = 1;
						smallLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with small log 3");
					}
				} else if (frog1Y <= smallLogY && !smallLogsMutex) {//Jumps in water
					frog1Active = false;
					frog2Active = true;
					onSmallLog = 0;
					noLateralMovement = false;
					resetMutexes();
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
					Log.w("Variable", "frog1 dies in last else if of smallLog");
				} 
				
				//If frog 1 intersects with a large log
				if (Rect.intersects(frog1Rect, largeLog1Rect)) {
					if (!largeLogsMutex) {
						frog1OnLargeLog1 = true;
						onLargeLog = 1;
						largeLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with large log 1");

					}
				} else if (Rect.intersects(frog1Rect, largeLog2Rect)) {
					if (!largeLogsMutex) {
						frog1OnLargeLog2 = true;
						onLargeLog = 1;
						largeLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with large log 2");

					}
				} else if (Rect.intersects(frog1Rect, largeLog3Rect)) {
					if (!largeLogsMutex) {
						frog1OnLargeLog3 = true;
						onLargeLog = 1;
						largeLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with large log 3");

					}
				} else if (frog1Y <= largeLogY && !largeLogsMutex) {//Jumps in water
					frog1Active = false;
					frog2Active = true;
					onLargeLog = 0;
					noLateralMovement = false;
					resetMutexes();
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
					Log.w("Variable", "Frog1 died in last else if og large logs");
				}
				
				//If frog 1 intersects with a medium log
				if (Rect.intersects(frog1Rect, mediumLog1Rect)) {
					if (!mediumLogsMutex) {
						frog1OnMediumLog1 = true;
						onMediumLog = 1;
						mediumLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with medium log 1");

					}
				} else if (Rect.intersects(frog1Rect, mediumLog2Rect)) {
					if (!mediumLogsMutex) {
						frog1OnMediumlLog2 = true;
						onMediumLog = 1;
						mediumLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with medium log 2");

					}
				} else if (Rect.intersects(frog1Rect, mediumLog3Rect)) {
					if (!mediumLogsMutex) {
						frog1OnMediumLog3 = true;
						onMediumLog = 1;
						mediumLogsMutex = true;
						noLateralMovement = true;
						Log.w("Variable", "frog1 intersected with medium log 3");

					}
				} else if (frog1Y <= mediumLogY && !mediumLogsMutex) {//Jumps in water
					frog1Active = false;
					frog2Active = true;
					onMediumLog = 0;
					noLateralMovement = false;
					resetMutexes();
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
					Log.w("Variable", "Frog1 died in last else if og medium logs");
				}
				
				//If frog 1 intersects with turtle shells row 2
				if (Rect.intersects(frog1Rect, turtleShellsRow2_1Rect)){ 
					if (!shellsRow2Mutex){
						frog1OnTurtleShellsRow2_1 = true;
						onShellsRow2 = 1;
						shellsRow2Mutex = true;
						noLateralMovement = true;
						Log.w("Variable", "Intersected with shellsRow21");
					}
				} else if (Rect.intersects(frog1Rect, turtleShellsRow2_2Rect)) {
					if (!shellsRow2Mutex) {
						frog1OnTurtleShellsRow2_2 = true;
						onShellsRow2 = 1;
						shellsRow2Mutex = true;
						noLateralMovement = true;
						Log.w("Variable", "Intersected with shellsRow2_2");

					}
				} else if (Rect.intersects(frog1Rect, turtleShellsRow2_3Rect)) {
					if (!shellsRow2Mutex) {
						frog1OnTurtleShellsRow2_3 = true;
						onShellsRow2 = 1;
						shellsRow2Mutex = true;
						noLateralMovement = true;
						Log.w("Variable", "Intersected with shellsRow2_3");

					}
				} else if (frog1Y <= turtleShellsRow2Y + 10 && !shellsRow2Mutex) { //Jumps in water
					frog1Active = false;
					frog2Active = true;
					onShellsRow2 = 0;
					noLateralMovement = false;
					resetMutexes();
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
					Log.w("Variable", "Frog 1 died in the water Row2 turtle shells");
				}
				
				//If frog makes it to a winner's square
				if (Rect.intersects(frog1Rect, winnerSquareRect1) || Rect.intersects(frog1Rect, winnerSquareRect2) || Rect.intersects(frog1Rect, winnerSquareRect3) || Rect.intersects(frog1Rect, winnerSquareRect4) || Rect.intersects(frog1Rect, winnerSquareRect5) ) {
					frog1Active = false;
					frog2Active = true;
					resetMutexes();
					carSpeed += 10;
					turtleShellsSpeed += 5;
					smallLogSpeed += 5;
					largeLogSpeed += 5;
					mediumLogSpeed += 5;
					score += 10000;

				}
				
				//If frog rides off the screen, it's dead
				if (frog1X < 0 || frog1X > canvas.getWidth()) {
					frog1Active = false;
					frog2Active = true;
					resetMutexes();
					canvas.drawBitmap(frogDead, frog1X, frog1Y, drawPaint);
				}
			} 
			//######################## END FROG 1 ###########################################
			
			
			//###########################################################	
			//##############     FROG 2       ###########################
			//###########################################################
				
				if (frog2Active) {
					if (Rect.intersects(frog2Rect, carRow1Rect) || Rect.intersects(frog2Rect, carRow2Rect) || Rect.intersects(frog2Rect, carRow3Rect) || Rect.intersects(frog2Rect, carRow4Rect)) {
						frog2Active = false;
						frog3Active = true;
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
					} 
					
					//Log.w("Variable", "frog2Y, turtleShellsY = " + frog2Y + ", " + turtleShellsY);
					
					//If frog2 intersects with one of the turtle shells
					//I needed the shellsMutex so that onShells could only be set once. 
					if (Rect.intersects(frog2Rect, turtleShells1Rect)){ 
						if (!shellsMutex){
							frog2OnTurtleShells1 = true;
							onShells = 1;
							shellsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "Intersected with shells1");
						}
					} else if (Rect.intersects(frog2Rect, turtleShells2Rect)) {
						if (!shellsMutex) {
							frog2OnTurtleShells2 = true;
							onShells = 1;
							shellsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "Intersected with shells2");

						}
					} else if (Rect.intersects(frog2Rect, turtleShells3Rect)) {
						if (!shellsMutex) {
							frog2OnTurtleShells3 = true;
							onShells = 1;
							shellsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "Intersected with shells3");

						}
					} else if (frog2Y <= turtleShellsY + 10 && !shellsMutex) { //Jumps in water
						frog2Active = false;
						frog2Active = true;
						onShells = 0;
						noLateralMovement = false;
						resetMutexes();
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
						Log.w("Variable", "Frog 2 died in the water");
					}
					
					//if frog2 intersects with one of the small logs
					if (Rect.intersects(frog2Rect, smallLog1Rect)) {
						if (!smallLogsMutex) {
							frog2OnSmallLog1 = true;
							onSmallLog = 1;
							smallLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with small log 1");
						}
					} else if (Rect.intersects(frog2Rect, smallLog2Rect)) {
						if (!smallLogsMutex) {
							frog2OnSmallLog2 = true;
							onSmallLog = 1;
							smallLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with small log 2");
						}
					} else if (Rect.intersects(frog2Rect, smallLog3Rect)) {
						if (!smallLogsMutex) {
							frog2OnSmallLog3 = true;
							onSmallLog = 1;
							smallLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with small log 3");
						}
					} else if (frog2Y <= smallLogY && !smallLogsMutex) {//Jumps in water
						frog2Active = false;
						frog2Active = true;
						onSmallLog = 0;
						noLateralMovement = false;
						resetMutexes();
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
						Log.w("Variable", "frog2 dies in last else if of smallLog");
					} 
					
					//If frog 2 intersects with a large log
					if (Rect.intersects(frog2Rect, largeLog1Rect)) {
						if (!largeLogsMutex) {
							frog2OnLargeLog1 = true;
							onLargeLog = 1;
							largeLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with large log 1");

						}
					} else if (Rect.intersects(frog2Rect, largeLog2Rect)) {
						if (!largeLogsMutex) {
							frog2OnLargeLog2 = true;
							onLargeLog = 1;
							largeLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with large log 2");

						}
					} else if (Rect.intersects(frog2Rect, largeLog3Rect)) {
						if (!largeLogsMutex) {
							frog2OnLargeLog3 = true;
							onLargeLog = 1;
							largeLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with large log 3");

						}
					} else if (frog2Y <= largeLogY && !largeLogsMutex) {//Jumps in water
						frog2Active = false;
						frog2Active = true;
						onLargeLog = 0;
						noLateralMovement = false;
						resetMutexes();
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
						Log.w("Variable", "frog2 died in last else if og large logs");
					}
					
					//If frog 2 intersects with a medium log
					if (Rect.intersects(frog2Rect, mediumLog1Rect)) {
						if (!mediumLogsMutex) {
							frog2OnMediumLog1 = true;
							onMediumLog = 1;
							mediumLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with medium log 1");

						}
					} else if (Rect.intersects(frog2Rect, mediumLog2Rect)) {
						if (!mediumLogsMutex) {
							frog2OnMediumlLog2 = true;
							onMediumLog = 1;
							mediumLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with medium log 2");

						}
					} else if (Rect.intersects(frog2Rect, mediumLog3Rect)) {
						if (!mediumLogsMutex) {
							frog2OnMediumLog3 = true;
							onMediumLog = 1;
							mediumLogsMutex = true;
							noLateralMovement = true;
							Log.w("Variable", "frog2 intersected with medium log 3");

						}
					} else if (frog2Y <= mediumLogY && !mediumLogsMutex) {//Jumps in water
						frog2Active = false;
						frog2Active = true;
						onMediumLog = 0;
						noLateralMovement = false;
						resetMutexes();
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
						Log.w("Variable", "frog2 died in last else if og medium logs");
					}
					
					//If frog 2 intersects with turtle shells row 2
					if (Rect.intersects(frog2Rect, turtleShellsRow2_1Rect)){ 
						if (!shellsRow2Mutex){
							frog2OnTurtleShellsRow2_1 = true;
							onShellsRow2 = 1;
							shellsRow2Mutex = true;
							noLateralMovement = true;
							Log.w("Variable", "Intersected with shellsRow21");
						}
					} else if (Rect.intersects(frog2Rect, turtleShellsRow2_2Rect)) {
						if (!shellsRow2Mutex) {
							frog2OnTurtleShellsRow2_2 = true;
							onShellsRow2 = 1;
							shellsRow2Mutex = true;
							noLateralMovement = true;
							Log.w("Variable", "Intersected with shellsRow2_2");

						}
					} else if (Rect.intersects(frog2Rect, turtleShellsRow2_3Rect)) {
						if (!shellsRow2Mutex) {
							frog2OnTurtleShellsRow2_3 = true;
							onShellsRow2 = 1;
							shellsRow2Mutex = true;
							noLateralMovement = true;
							Log.w("Variable", "Intersected with shellsRow2_3");

						}
					} else if (frog2Y <= turtleShellsRow2Y + 10 && !shellsRow2Mutex) { //Jumps in water
						frog2Active = false;
						frog2Active = true;
						onShellsRow2 = 0;
						noLateralMovement = false;
						resetMutexes();
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
						Log.w("Variable", "Frog 1 died in the water Row2 turtle shells");
					}
					
					//If frog makes it to a winner's square
					if (Rect.intersects(frog2Rect, winnerSquareRect1) || Rect.intersects(frog2Rect, winnerSquareRect2) || Rect.intersects(frog2Rect, winnerSquareRect3) || Rect.intersects(frog2Rect, winnerSquareRect4) || Rect.intersects(frog2Rect, winnerSquareRect5) ) {
						frog2Active = false;
						frog3Active = true;
						resetMutexes();
						carSpeed += 10;
						turtleShellsSpeed += 5;
						smallLogSpeed += 5;
						largeLogSpeed += 5;
						mediumLogSpeed += 5;
						score += 10000;
					}
					
					//If frog rides off the screen, it's dead
					if (frog2X < 0 || frog2X > canvas.getWidth()) {
						frog2Active = false;
						frog3Active = true;
						resetMutexes();
						canvas.drawBitmap(frogDead, frog2X, frog2Y, drawPaint);
					}
				} 
				//######################## END FROG 2 ###########################################
			
				
				//###########################################################	
				//##############     FROG 3       ###########################
				//###########################################################
					
					if (frog3Active) {
						if (Rect.intersects(frog3Rect, carRow1Rect) || Rect.intersects(frog3Rect, carRow2Rect) || Rect.intersects(frog3Rect, carRow3Rect) || Rect.intersects(frog3Rect, carRow4Rect)) {
							frog3Active = false;
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
						} 
						
						//Log.w("Variable", "frog3Y, turtleShellsY = " + frog3Y + ", " + turtleShellsY);
						
						//If frog3 intersects with one of the turtle shells
						//I needed the shellsMutex so that onShells could only be set once. 
						if (Rect.intersects(frog3Rect, turtleShells1Rect)){ 
							if (!shellsMutex){
								frog3OnTurtleShells1 = true;
								onShells = 1;
								shellsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "Intersected with shells1");
							}
						} else if (Rect.intersects(frog3Rect, turtleShells2Rect)) {
							if (!shellsMutex) {
								frog3OnTurtleShells2 = true;
								onShells = 1;
								shellsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "Intersected with shells2");

							}
						} else if (Rect.intersects(frog3Rect, turtleShells3Rect)) {
							if (!shellsMutex) {
								frog3OnTurtleShells3 = true;
								onShells = 1;
								shellsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "Intersected with shells3");

							}
						} else if (frog3Y <= turtleShellsY + 10 && !shellsMutex) { //Jumps in water
							frog3Active = false;
							frog2Active = true;
							onShells = 0;
							noLateralMovement = false;
							resetMutexes();
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
							Log.w("Variable", "Frog 1 died in the water");
						}
						
						//if frog3 intersects with one of the small logs
						if (Rect.intersects(frog3Rect, smallLog1Rect)) {
							if (!smallLogsMutex) {
								frog3OnSmallLog1 = true;
								onSmallLog = 1;
								smallLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with small log 1");
							}
						} else if (Rect.intersects(frog3Rect, smallLog2Rect)) {
							if (!smallLogsMutex) {
								frog3OnSmallLog2 = true;
								onSmallLog = 1;
								smallLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with small log 2");
							}
						} else if (Rect.intersects(frog3Rect, smallLog3Rect)) {
							if (!smallLogsMutex) {
								frog3OnSmallLog3 = true;
								onSmallLog = 1;
								smallLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with small log 3");
							}
						} else if (frog3Y <= smallLogY && !smallLogsMutex) {//Jumps in water
							frog3Active = false;
							frog2Active = true;
							onSmallLog = 0;
							noLateralMovement = false;
							resetMutexes();
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
							Log.w("Variable", "frog3 dies in last else if of smallLog");
						} 
						
						//If frog 1 intersects with a large log
						if (Rect.intersects(frog3Rect, largeLog1Rect)) {
							if (!largeLogsMutex) {
								frog3OnLargeLog1 = true;
								onLargeLog = 1;
								largeLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with large log 1");

							}
						} else if (Rect.intersects(frog3Rect, largeLog2Rect)) {
							if (!largeLogsMutex) {
								frog3OnLargeLog2 = true;
								onLargeLog = 1;
								largeLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with large log 2");

							}
						} else if (Rect.intersects(frog3Rect, largeLog3Rect)) {
							if (!largeLogsMutex) {
								frog3OnLargeLog3 = true;
								onLargeLog = 1;
								largeLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with large log 3");

							}
						} else if (frog3Y <= largeLogY && !largeLogsMutex) {//Jumps in water
							frog3Active = false;
							frog2Active = true;
							onLargeLog = 0;
							noLateralMovement = false;
							resetMutexes();
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
							Log.w("Variable", "frog3 died in last else if og large logs");
						}
						
						//If frog 1 intersects with a medium log
						if (Rect.intersects(frog3Rect, mediumLog1Rect)) {
							if (!mediumLogsMutex) {
								frog3OnMediumLog1 = true;
								onMediumLog = 1;
								mediumLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with medium log 1");

							}
						} else if (Rect.intersects(frog3Rect, mediumLog2Rect)) {
							if (!mediumLogsMutex) {
								frog3OnMediumlLog2 = true;
								onMediumLog = 1;
								mediumLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with medium log 2");

							}
						} else if (Rect.intersects(frog3Rect, mediumLog3Rect)) {
							if (!mediumLogsMutex) {
								frog3OnMediumLog3 = true;
								onMediumLog = 1;
								mediumLogsMutex = true;
								noLateralMovement = true;
								Log.w("Variable", "frog3 intersected with medium log 3");

							}
						} else if (frog3Y <= mediumLogY && !mediumLogsMutex) {//Jumps in water
							frog3Active = false;
							frog2Active = true;
							onMediumLog = 0;
							noLateralMovement = false;
							resetMutexes();
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
							Log.w("Variable", "frog3 died in last else if og medium logs");
						}
						
						//If frog 1 intersects with turtle shells row 2
						if (Rect.intersects(frog3Rect, turtleShellsRow2_1Rect)){ 
							if (!shellsRow2Mutex){
								frog3OnTurtleShellsRow2_1 = true;
								onShellsRow2 = 1;
								shellsRow2Mutex = true;
								noLateralMovement = true;
								Log.w("Variable", "Intersected with shellsRow21");
							}
						} else if (Rect.intersects(frog3Rect, turtleShellsRow2_2Rect)) {
							if (!shellsRow2Mutex) {
								frog3OnTurtleShellsRow2_2 = true;
								onShellsRow2 = 1;
								shellsRow2Mutex = true;
								noLateralMovement = true;
								Log.w("Variable", "Intersected with shellsRow2_2");

							}
						} else if (Rect.intersects(frog3Rect, turtleShellsRow2_3Rect)) {
							if (!shellsRow2Mutex) {
								frog3OnTurtleShellsRow2_3 = true;
								onShellsRow2 = 1;
								shellsRow2Mutex = true;
								noLateralMovement = true;
								Log.w("Variable", "Intersected with shellsRow2_3");

							}
						} else if (frog3Y <= turtleShellsRow2Y + 10 && !shellsRow2Mutex) { //Jumps in water
							frog3Active = false;
							onShellsRow2 = 0;
							noLateralMovement = false;
							resetMutexes();
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
							Log.w("Variable", "Frog 1 died in the water Row2 turtle shells");
						}
						
						//If frog makes it to a winner's square
						if (Rect.intersects(frog3Rect, winnerSquareRect1) || Rect.intersects(frog3Rect, winnerSquareRect2) || Rect.intersects(frog3Rect, winnerSquareRect3) || Rect.intersects(frog3Rect, winnerSquareRect4) || Rect.intersects(frog3Rect, winnerSquareRect5) ) {
							frog3Active = false;
							resetMutexes();
							carSpeed += 10;
							turtleShellsSpeed += 5;
							smallLogSpeed += 5;
							largeLogSpeed += 5;
							mediumLogSpeed += 5;
							score += 10000;

						}
						
						//If frog rides off the screen, it's dead
						if (frog3X < 0 || frog3X > canvas.getWidth()) {
							frog3Active = false;
							resetMutexes();
							canvas.drawBitmap(frogDead, frog3X, frog3Y, drawPaint);
						}
					} 
					//######################## END FROG 3 ###########################################
			
			
		}
		
		public void pause() {
			threadOK = false; //Not okay to run a thread when paused
			while (true) {
				try {
					ViewThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			ViewThread = null;
		}
		
		public void resume() {
			threadOK = true;
			ViewThread = new Thread(this);
			ViewThread.start();
		}
		
		public boolean onTouchEvent(MotionEvent event) {
			
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				if (onShells == 1) {
					onShells = 0;
				} 
				
				if (onSmallLog == 1){
					onSmallLog = 0;
				}
				
				if (onLargeLog == 1) {
					onLargeLog = 0;
				}
				
				if (onMediumLog == 1) {
					onMediumLog = 0;
				}
				
				if (onShellsRow2 == 1) {
					onShellsRow2 = 0;
				}
					

				//Where the user touched
				futureX = event.getRawX();
				futureY = event.getRawY()-180;
			
				if (frog1Active) {
					if (frog1Y < carRow4Y + 10) {
						movement = 80;
					}
					changeBooleans();
					//Log.w("Variable", "In onTouch, onShells = " + onShells);
				
					currentX = frog1X;
					currentY = frog1Y;
					
					//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
					///Log.w("Variable", "Future coords are " + String.valueOf(futureX) + ", " + String.valueOf(futureY));
					if (!noLateralMovement) {
						if (futureX > currentX + 150) {
							frog1X += movement;
							return true;
						} else if ( futureX < currentX - 150) {
							frog1X -= movement;
							return true;
						} else if (futureY > currentY + 75) {
							score -= 100;
							frog1Y += movement;
							//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
							//Log.w("Variable", "FutureY is more than current, frog1Y should be " + String.valueOf(frog1Y));
							return true;
						} else if (futureY < currentY - 75) {
							score += 100;
							frog1Y -= movement;
							//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
							//Log.w("Variable", "FutureY is less than current, frog1Y should be " + String.valueOf(frog1Y));
							return true;
						}
					} else if (futureY < currentY - 75) {
						score += 100;
						frog1Y -= movement;
						return true;
					} 
				}
				
				if (frog2Active) {
					if (frog2Y < carRow4Y + 10) {
						movement = 80;
					}
					changeBooleans();
					//Log.w("Variable", "In onTouch, onShells = " + onShells);
				
					currentX = frog2X;
					currentY = frog2Y;
					
					//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
					///Log.w("Variable", "Future coords are " + String.valueOf(futureX) + ", " + String.valueOf(futureY));
					if (!noLateralMovement) {
						if (futureX > currentX + 150) {
							frog2X += movement;
							return true;
						} else if ( futureX < currentX - 150) {
							frog2X -= movement;
							return true;
						} else if (futureY > currentY + 75) {
							score -= 100;
							frog2Y += movement;
							//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
							//Log.w("Variable", "FutureY is more than current, frog2Y should be " + String.valueOf(frog2Y));
							return true;
						} else if (futureY < currentY - 75) {
							score += 100;
							frog2Y -= movement;
							//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
							//Log.w("Variable", "FutureY is less than current, frog2Y should be " + String.valueOf(frog2Y));
							return true;
						}
					} else if (futureY < currentY - 75) {
						score += 100;
						frog2Y -= movement;
						return true;
					}
				}
					
				if (frog3Active) {
					if (frog3Y < carRow4Y + 10) {
						movement = 80;
					}
					changeBooleans();
					//Log.w("Variable", "In onTouch, onShells = " + onShells);
				
					currentX = frog3X;
					currentY = frog3Y;
					
					//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
					///Log.w("Variable", "Future coords are " + String.valueOf(futureX) + ", " + String.valueOf(futureY));
					if (!noLateralMovement) {
						if (futureX > currentX + 150) {
							frog3X += movement;
							return true;
						} else if ( futureX < currentX - 150) {
							frog3X -= movement;
							return true;
						} else if (futureY > currentY + 75) {
							score -= 100;
							frog3Y += movement;
							//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
							//Log.w("Variable", "FutureY is more than current, frog3Y should be " + String.valueOf(frog3Y));
							return true;
						} else if (futureY < currentY - 75) {
							score += 100;
							frog3Y -= movement;
							//Log.w("Variable", "Current coords are " + String.valueOf(currentX) + ", " + String.valueOf(currentY));
							//Log.w("Variable", "FutureY is less than current, frog3Y should be " + String.valueOf(frog3Y));
							return true;
						}
					} else if (futureY < currentY - 75) {
						score += 100;
						frog3Y -= movement;
						return true;
					}
			
				}
				
			}
			return true;
		}
		
		public void changeBooleans() {
			if (frog1OnTurtleShells1) { frog1OnTurtleShells1 = false; }
			if (frog1OnTurtleShells2) { frog1OnTurtleShells2 = false; }
			if (frog1OnTurtleShells3) { frog1OnTurtleShells3 = false; }
			if (frog2OnTurtleShells1) { frog2OnTurtleShells1 = false; }
			if (frog2OnTurtleShells2) { frog2OnTurtleShells2 = false; }
			if (frog2OnTurtleShells3) { frog2OnTurtleShells3 = false; }
			if (frog3OnTurtleShells1) { frog3OnTurtleShells1 = false; }
			if (frog3OnTurtleShells2) { frog3OnTurtleShells2 = false; }
			if (frog3OnTurtleShells3) { frog3OnTurtleShells3 = false; }
			
			if (frog1OnSmallLog1) { frog1OnSmallLog1 = false; }
			if (frog1OnSmallLog2) { frog1OnSmallLog2 = false; }
			if (frog1OnSmallLog3) { frog1OnSmallLog3 = false; }
			if (frog2OnSmallLog1) { frog2OnSmallLog1 = false; }
			if (frog2OnSmallLog2) { frog2OnSmallLog2 = false; }
			if (frog2OnSmallLog3) { frog2OnSmallLog3 = false; }
			if (frog3OnSmallLog1) { frog3OnSmallLog1 = false; }
			if (frog3OnSmallLog2) { frog3OnSmallLog2 = false; }
			if (frog3OnSmallLog3) { frog3OnSmallLog3 = false; }
			
			if (frog1OnLargeLog1) { frog1OnLargeLog1 = false; }
			if (frog1OnLargeLog2) { frog1OnLargeLog2 = false; }
			if (frog1OnLargeLog3) { frog1OnLargeLog3 = false; }
			if (frog2OnLargeLog1) { frog2OnLargeLog1 = false; }
			if (frog2OnLargeLog2) { frog2OnLargeLog2 = false; }
			if (frog2OnLargeLog3) { frog2OnLargeLog3 = false; }
			if (frog3OnLargeLog1) { frog3OnLargeLog1 = false; }
			if (frog3OnLargeLog2) { frog3OnLargeLog2 = false; }
			if (frog3OnLargeLog3) { frog3OnLargeLog3 = false; }
			
			if (frog1OnMediumLog1) { frog1OnMediumLog1 = false; }
			if (frog1OnMediumlLog2) { frog1OnMediumLog1 = false; }
			if (frog1OnMediumLog3) { frog1OnMediumLog1 = false; }
			if (frog2OnMediumLog1) { frog1OnMediumLog1 = false; }
			if (frog2OnMediumlLog2) { frog1OnMediumLog1 = false; }
			if (frog2OnMediumLog3) { frog1OnMediumLog1 = false; }
			if (frog3OnMediumLog1) { frog1OnMediumLog1 = false; }
			if (frog3OnMediumlLog2) { frog1OnMediumLog1 = false; }
			if (frog3OnMediumLog3) { frog1OnMediumLog1 = false; }
			
			if (frog1OnTurtleShellsRow2_1) { frog1OnTurtleShellsRow2_1 = false; }
			if (frog1OnTurtleShellsRow2_2) { frog1OnTurtleShellsRow2_2 = false; }
			if (frog1OnTurtleShellsRow2_3) { frog1OnTurtleShellsRow2_3 = false; }
			if (frog2OnTurtleShellsRow2_1) { frog2OnTurtleShellsRow2_1 = false; }
			if (frog2OnTurtleShellsRow2_2) { frog2OnTurtleShellsRow2_2 = false; }
			if (frog2OnTurtleShellsRow2_3) { frog2OnTurtleShellsRow2_3 = false; }
			if (frog3OnTurtleShellsRow2_1) { frog3OnTurtleShellsRow2_1 = false; }
			if (frog3OnTurtleShellsRow2_2) { frog3OnTurtleShellsRow2_2 = false; }
			if (frog3OnTurtleShellsRow2_3) { frog3OnTurtleShellsRow2_3 = false; }
		}
		
		public void resetMutexes() {
			onShells = 0;
			onSmallLog = 0;
			onMediumLog = 0;
			onLargeLog = 0;
			onShellsRow2 = 0;
			
			shellsMutex = false;
			smallLogsMutex = false;
			mediumLogsMutex = false;
			largeLogsMutex = false;
			shellsRow2Mutex = false;
			noLateralMovement = false;
		}
    	
    }

    
}

