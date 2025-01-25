package com.zatdroid.rodrigo;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapsActivity extends MapActivity {
		/** Called when the activity is first created. */
		
		MyLocationOverlay compass;
		MapView map;
	
		public static final int PREDICTIONS = 20; // Number of positions predicted. 
											// Half for the past and half to the future
		public static final double MINUTES_DELAY = 0.2; //minutes delay of every prediction
		public static final double SECOND_JD = 0.00001; //1 second in JD
		public static final double MINUTE_JD = 0.0069; //1 second in JD 
		public static final int  TRAJECT_SEC_DELAY = 1; // delay in Seconds to update trajectory in onDraw
												  // Drawing updated trajectory in onDraw method gives errors 
												  // because of the amount of calculations to do.
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			
			MapController controller;
			CalculationsTime TimeFunctions = new CalculationsTime();
			double currentTimeJD;
			sat sat;
		    
			super.onCreate(savedInstanceState);
			setContentView(R.layout.map);
			map = (MapView) findViewById(R.id.mapView);
			map.setBuiltInZoomControls(true);
			
			// Keeping screen alive
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			
			//// Retrieving sat data (latitude & longitude)
			
			// sat object retrieved
			Bundle data = getIntent().getExtras();
			sat = data.getParcelable("datosSatElegido");
			/* *******Current Position of satellite to center map ************* */
			//Current DATETIME in UTC (Some devices gain or lose some seconds) 
			Calendar ctime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			//Convert currentTime (Calendar) to JD
			currentTimeJD = TimeFunctions.Julian_Date(ctime);
			//Calculate Latitude and Longitude

			CalculationsSatPOS satPOS = new CalculationsSatPOS();
			sat = satPOS.satPOS(currentTimeJD,sat);

			GeoPoint center = new GeoPoint((int) (sat.getLat() * 1E6) , (int) (sat.getLon() * 1E6) );
			/* ****************************************************************** */
			
			//List of overlays
			List<Overlay> mapOverlays = map.getOverlays();
			
			/* ******************** Code for Marker. Cannot update with new  ************
			 * *******calculations of sat long-lat  with a MapsOverlayItem class********* */
	/*		//Array with OverlayItems. Icon, text, dialogs
			Drawable drawable = this.getResources().getDrawable(R.drawable.sat_map);
			MapsOverlayItem itemizedoverlay = new MapsOverlayItem(drawable, this);
			
			//Locating GeoPoint
			GeoPoint point = new GeoPoint((int) (sat.getLat() * 1E6) , (int) (sat.getLon() * 1E6) );
			OverlayItem overlayitem = new OverlayItem(point, "Sat:" + sat.getName() + "\n",
					"Latitud:" + String.valueOf(sat.getLat()) + "\n" + 
					"Longitude:" + String.valueOf(sat.getLon() ) );
			
			itemizedoverlay.addOverlayItem(overlayitem);
			mapOverlays.add(itemizedoverlay);
		    ********************************************************************************
			******************************************************************************** */
			
			//Center the map in the geopoint of the sat
			controller = map.getController();
			controller.animateTo(center);
			controller.setZoom(3); //See World Map

			//Compass custom and add to the overlay list
			compass = new MyLocationOverlay(MapsActivity.this, map);
			mapOverlays.add(compass);
     
	        // Overlay with current position in real time (updated every second)
			// Creating path with lat-lon of trajectory points
			MapOverlay mapOvlay = new MapOverlay(sat);
	        mapOverlays.add(mapOvlay);
			        
		}

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			compass.disableCompass();
			super.onPause();
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			compass.enableCompass();
			super.onResume();
		}

		@Override
		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}

// Overlay with updated position of sat dynamically showed every second	(real time)	
public class MapOverlay extends com.google.android.maps.Overlay {
	
	 CalculationsTime TimeFunctions = new CalculationsTime();
	  private sat sat; 
	  private double[] trajectPoints= new double[PREDICTIONS*2];
	  double start;
	  
	  protected MapOverlay(sat sat ) {
		  this.sat = sat;
	      //Current DATETIME in UTC
		  Calendar ctime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		  //Convert currentTime (Calendar) to JD
		  start = TimeFunctions.Julian_Date(ctime);
		  
		  // Initial trajectory calculation
	   	  trajectPoints = trajectory(sat);
      }
      
	  public double[] trajectory (sat sat) {
		  	
			double currentTimeJD;
			
			//Current DATETIME in UTC
			Calendar ctime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			//Convert currentTime (Calendar) to JD
			currentTimeJD = TimeFunctions.Julian_Date(ctime);
			
			// 
			int j=0; //index for trajectPoints array
			for (int i=(-PREDICTIONS/2)+1; i<=(PREDICTIONS/2); i++) {
				double timePredictionsJD = currentTimeJD + i * MINUTE_JD * MINUTES_DELAY ; //1 min = 0.0069 JD
			
				//Calculate Latitude and Longitude
				CalculationsSatPOS satPOS = new CalculationsSatPOS();
				sat = satPOS.satPOS(timePredictionsJD,sat);
				
				GeoPoint gp = new GeoPoint((int) (sat.getLat() * 1E6) , (int) (sat.getLon() * 1E6) );
				Point p = new Point();
		        Projection projection = map.getProjection();
		        projection.toPixels(gp, p);
				trajectPoints[j]   =  p.x;
	        	trajectPoints[j+1] =  p.y;
		        
	        	j+=2;    
			} //end for
			
			return trajectPoints;
			}
	  
      @Override
      public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
            long when) { //It is drawn automatically every second or less
         
    	   Paint paint;
    	   Path path;
    	   double currentTimeJD;
    	   
    	    //super.draw(canvas, mapView, shadow); //No need???????
         
         	//Current DATETIME in UTC
			Calendar ctime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			//Convert currentTime (Calendar) to JD
			currentTimeJD = TimeFunctions.Julian_Date(ctime);
			
			// Updating predicted trajectory every 1 Second
			// Updating with the onDraw Method cause errors in orbit calculations
			if ((currentTimeJD - start)>(TRAJECT_SEC_DELAY*SECOND_JD)) { //1sec = 0.00001 JD
		     	 trajectPoints = trajectory(sat);
		     	 
	         	//Current DATETIME in UTC
				Calendar ctime2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				//Convert currentTime (Calendar) to JD
				start = TimeFunctions.Julian_Date(ctime2);
			}
			
			//Calculate Latitude and Longitude (& altitude)
			CalculationsSatPOS satPOS = new CalculationsSatPOS();
			sat = satPOS.satPOS(currentTimeJD,sat);
         
	        GeoPoint currentSatGPoint = new GeoPoint((int) (sat.getLat() * 1E6) , (int) (sat.getLon() * 1E6) );
	         
		    paint = new Paint();
	        paint.setColor(Color.RED);
	        paint.setAntiAlias(true);
	        paint.setStyle(Style.STROKE);
	        paint.setStrokeWidth(2);
	        paint.setTextSize(25);	
	        Point currentSatPoint = new Point();

	        Projection projection = mapView.getProjection();
	        projection.toPixels(currentSatGPoint, currentSatPoint);
	         
	        //Creating path (lines) with points
	        path = new Path();
	        path.moveTo((float)trajectPoints[0], (float)trajectPoints[1]);
	        for (int i=2; i<trajectPoints.length; i+=2) {
	       	 path.lineTo((float)trajectPoints[i], (float)trajectPoints[i+1]);
	        }
	        canvas.drawPath(path, paint);
	        
	        //Draw the sat icon in real  time
	        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sat_map);
	        canvas.drawBitmap(bmp, new Rect(0,0,280,280), 
	       		 new Rect(currentSatPoint.x - 60,currentSatPoint.y - 60,
	       				 currentSatPoint.x + 40,currentSatPoint.y + 40), paint);
	         
	        //Show text with Long & lat & alt of the sat (real time)
	        canvas.drawText("Name: "+ sat.getName() , 130,40 , paint);
	        canvas.drawText("Long: "+ String.format( "%.2f", sat.getLon() ) , 130,70 , paint);
	        canvas.drawText("Lat: "+ String.format( "%.2f", sat.getLat() ), 130,100 , paint);
	        canvas.drawText("Alt: "+ String.format( "%.2f", sat.getAlt() ) + " km", 130,130 , paint);

	        paint=null;
	        path=null;
	        currentSatPoint=null;
			
	        return true;
      	}
}

	
		
}
