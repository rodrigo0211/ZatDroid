package com.zatdroid.rodrigo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class menuOpciones extends Activity implements View.OnClickListener {

	Button AR, Map;
	sat sat;
	boolean visited = false;
	ProgressDialog dialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final float height = getResources().getDisplayMetrics().heightPixels; // Scale for dif screens sizes
		final float width = getResources().getDisplayMetrics().widthPixels; // Scale for dif screens sizes
		
		// Retrieve SpecificTypeSaved, which also tells about GeneralTypeSaved
		SharedPreferences SpecificTypeSaved;
		SpecificTypeSaved = getSharedPreferences(listaTipoSat2.fileName, 0);
		int iSpecificTypeSaved = SpecificTypeSaved.getInt("tipoSatEspecifico",0);
		
		setContentView(R.layout.menu_apps);
		
		ViewGroup mContainer = (ViewGroup) findViewById(R.id.container);
		mContainer.addView(breadCrumbs(width, height, iSpecificTypeSaved));
		
		ViewGroup mContainer2 = (ViewGroup) findViewById(R.id.container2);
		mContainer2.addView(breadCrumbs2(width, height));

		identificarBotones();
		AR.setOnClickListener(this);
		Map.setOnClickListener(this);
	}

	private void identificarBotones() {
		AR = (Button) findViewById(R.id.bAR);
		Map = (Button) findViewById(R.id.bMap);
	}

	@Override
	public void onClick(View v) {
		
		//Retrieving data from Satellite
		Bundle data = getIntent().getExtras();
		sat sat = data.getParcelable("datosSatElegido");
		
		switch (v.getId()) {
		case R.id.bAR:

			// Dialog info Loading
			dialog = new ProgressDialog(this);
			Resources res = getResources();
			dialog.setMessage(res.getString(R.string.LoadingAR));
			dialog.setCancelable(true);
	 	    dialog.setProgress(0);
	        dialog.setMax(100);
	        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        dialog.show();
	        visited =  true;
	        
			Intent startApp = new Intent("com.zatdroid.rodrigo.ARINTRO");
			startApp.putExtra("datosSatElegido", sat);
			startActivity(startApp);
			
			break;
		case R.id.bMap:
			Intent startApp2 = new Intent("com.zatdroid.rodrigo.MAPSACTIVITY");
			startApp2.putExtra("datosSatElegido", sat);
			startActivity(startApp2);
			
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (visited) dialog.dismiss();
	}
	
	private LinearLayout breadCrumbs2 (float width, float height) {
		// New line with sat name
		
		SharedPreferences satNamePicked;
		// Retrieve SpecificTypeSaved, which also tells about GeneralTypeSaved
		satNamePicked = getSharedPreferences(listaSats.fileName, 0);
		String sSatName = satNamePicked.getString("satElegido","");
		
		//base linearlayout vertical
		LinearLayout l5 = new LinearLayout(this);
		l5.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramsl5 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l5.setLayoutParams(paramsl5);
		
		int h = (int) (0.05 * height);
		// BreadCrumbs button Sat Name
		Button bBreadCrumbs5 =  breadCrumbsButton(width, h,
								1f, 0.15f, 0f,sSatName,
								getResources().getColor(R.color.BreadCrumbs5));
		l5.addView(bBreadCrumbs5);
		
		return l5;
	}

	private LinearLayout breadCrumbs(float width, float height, int iSpecificTypeSaved) {
		
		// Retrieving info from types chosen 
		String sGeneralTypeSaved = setButtonNames(iSpecificTypeSaved, 0);
		String sSpecificTypeSaved = setButtonNames(iSpecificTypeSaved, 1);
		
		// First LinearLayout horizontal for BreadCrumbs
		int h = (int) (0.05 * height);
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramsl1 = new LinearLayout.LayoutParams(
			     ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsl1.setMargins(0, 0, 0, 0);
		paramsl1.height = h;
		l1.setLayoutParams(paramsl1);
	
		// BreadCrumbs button 1 (home)
		Button bBreadCrumbs1 =  breadCrumbsButton(width, h,
								0.20f, 0.15f, 0.10f, getResources().getString(R.string.home),
								getResources().getColor(R.color.BreadCrumbs1));
		l1.addView(bBreadCrumbs1);
	
		// BreadCrumbs button 2 (General Type)
		Button bBreadCrumbs2 =  breadCrumbsButton(width, h,
				0.40f, 0.20f, 0.20f, sGeneralTypeSaved,
				getResources().getColor(R.color.BreadCrumbs2));
		l1.addView(bBreadCrumbs2);
		
		// BreadCrumbs button 3 (SpecificType )
		Button bBreadCrumbs3 =  breadCrumbsButton(0, h,
				0.40f, 0.20f, 0.20f, sSpecificTypeSaved, 
				getResources().getColor(R.color.BreadCrumbs3));
		l1.addView(bBreadCrumbs3);
		
		// BreadCrumbs button 4 (fill)
		Button  bBreadCrumbsEnd = new Button(this);
		bBreadCrumbsEnd.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs4));
		LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT );
		params4.setMargins(0, 0, 0, 0);
		bBreadCrumbsEnd.setLayoutParams(params4);
		
		l1.addView(bBreadCrumbsEnd);
	
		// Setting onClickListener to bBreadCrumbs (home)
		bBreadCrumbs1.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	    		// Back home
	        	Intent a = new Intent("com.zatdroid.rodrigo.LISTATIPOSAT");
	            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // finish all activities between "this" and "home" 
	            startActivity(a);
	         }
	    });
		
		if (iSpecificTypeSaved != 70) {  // if we got here from searchActivity, no clickable BreadCrumbs are set 
			// Setting onClickListener to bBreadCrumbs1 (General list)
			bBreadCrumbs2.setOnClickListener(new Button.OnClickListener() {  
				public void onClick(View v) {
		    		// Back General list
					Intent a = new Intent("com.zatdroid.rodrigo.LISTATIPOSAT2");
		            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // finish all activities between "this" and "home" 
				    startActivity(a);
				}
			});
			// Setting onClickListener to bBreadCrumbs (Specific List)
			bBreadCrumbs3.setOnClickListener(new Button.OnClickListener() {  
		        public void onClick(View v) {
				    // Back home
					finish();
			     }
			});
		}
	
	return l1;
	}

	private Button breadCrumbsButton (float width, float h, float w_per,
			float p_per, float pt_per, String s, int color) {
		
		int w = (int) (w_per * width);
		int h_ = (int) (0.8 * h);
		int p = (int) (p_per * w);
		int pt = (int) (pt_per * h);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				w, LinearLayout.LayoutParams.MATCH_PARENT);
		if (width == 0f ) { 
			params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		}
		Button  bBreadCrumbs = new Button(this);
		bBreadCrumbs.setText(s);
		bBreadCrumbs.setBackgroundColor(color);
		params.setMargins(0,0,0,0); // Center icons for different screen sizes
		bBreadCrumbs.setTextColor(Color.WHITE);
		bBreadCrumbs.setPadding(0, 0, 0, 0);
		bBreadCrumbs.setGravity(Gravity.CENTER | Gravity.CENTER);
		bBreadCrumbs.setLayoutParams(params);
			// Fit text to button
		float sx = scaleButtonText(h_-pt, w, p,s);
		if (sx < 1.3) {
			bBreadCrumbs.setTextScaleX(sx);
		} else {
			bBreadCrumbs.setTextScaleX(1.3f);
		}
		bBreadCrumbs.setTextSize(TypedValue.COMPLEX_UNIT_PX, h_);
		bBreadCrumbs.setSingleLine(true);
		return bBreadCrumbs;
	}

	private String setButtonNames(int iType, int select) {
		/* select = 0: returns sGeneralTypeSaved
		 * select = 1: returns sSpecificTypeSaved 
		 */
		String sSpecific="";
		int general = 0;
		
		switch (iType) {
			
		// Special Interest
			case 11: //bLast30
				sSpecific = getResources().getString(R.string.lsi_0s); 
				general = 1;
				break;
			case 12: // R.id.bSpaceStations:
				sSpecific = getResources().getString(R.string.lsi_1s); 
				general = 1;
				break;
			case 13: //R.id.b100Brightest:
				sSpecific = getResources().getString(R.string.lsi_2s); 
				general = 1;
				break;
	    // Weather & Earth
		
			case 21: //bWeather
				sSpecific = getResources().getString(R.string.lwe_0s); 
				general = 2;
				break;
			case 22: // NOAA:
				sSpecific = getResources().getString(R.string.lwe_1s); 
				general = 2;
				break;
			case 23: // GOES:
				sSpecific = getResources().getString(R.string.lwe_2s); 
				general = 2;
				break;
			case 24: //Earth&Resources:
				sSpecific = getResources().getString(R.string.lwe_3s); 
				general = 2;
				break;
			case 25: //Search&Rescue
				sSpecific = getResources().getString(R.string.lwe_4s); 
				general = 2;
				break;
			case 26: //DisastMonit
				sSpecific = getResources().getString(R.string.lwe_5s); 
				general = 2;
				break;
			case 27: //TrackData
				sSpecific = getResources().getString(R.string.lwe_6s); 
				general = 2;
				break;
		 // Communications
		
			case 31: // GEo
				sSpecific = getResources().getString(R.string.lc_0s); 
				general = 3;
				break;
			case 32: // IntelSat
				sSpecific = getResources().getString(R.string.lc_1s); 
				general = 3;
				break;
			case 33: //Gorizont
				sSpecific = getResources().getString(R.string.lc_2s); 
				general = 3;
				break;
			case 34: //Raduga
				sSpecific = getResources().getString(R.string.lc_3s); 
				general = 3;
				break;
			case 35: //Molniya
				sSpecific = getResources().getString(R.string.lc_4s); 
				general = 3;
				break;
			case 36: //Iridium
				sSpecific = getResources().getString(R.string.lc_5s); 
				general = 3;
				break;
			case 37: //Orbcom
				sSpecific = getResources().getString(R.string.lc_6s); 
				general = 3;
				break;
			case 38: //GlobalStar
				sSpecific = getResources().getString(R.string.lc_7s); 
				general = 3;
				break;
			case 39: //AmateurRadio
				sSpecific = getResources().getString(R.string.lc_8s); 
				general = 3;
				break;
			case 391: //Experimental
				sSpecific = getResources().getString(R.string.lc_9s); 
				general = 3;
				break;
			case 392: //Other
				sSpecific = getResources().getString(R.string.lc_10s); 
				general = 3;
				break;
		
				// Navigation
		
			case 41: //GPS
				sSpecific = getResources().getString(R.string.ln_0s); 
				general = 4;
				break;
			case 42: // Glonass
				sSpecific = getResources().getString(R.string.ln_1s); 
				general = 4;
				break;
			case 43: // Galileo
				sSpecific = getResources().getString(R.string.ln_2s);
				general = 4;
				break;
			case 44: //WAAS/EGNOS/MSAS
				sSpecific = getResources().getString(R.string.ln_3s);
				general = 4;
				break;
			case 45: //NavyNavegat
				sSpecific = getResources().getString(R.string.ln_4s);
				general = 4;
				break;
			case 46: //RussianLEO
				sSpecific = getResources().getString(R.string.ln_5s);
				general = 4;
				break;
		 // Scientific
		
			case 51: //Space&Earth
				sSpecific = getResources().getString(R.string.ls_0s);
				general = 5;
				break;
			case 52: // Geodetic
				sSpecific = getResources().getString(R.string.ls_1s);
				general = 5;
				break;
			case 53: // Engineering
				sSpecific = getResources().getString(R.string.ls_2s);
				general = 5;
				break;
			case 54: //Educational
				sSpecific = getResources().getString(R.string.ls_3s);
				general = 5;
				break;
	
		// Miscellaneous
		
			case 61: //Military
				sSpecific = getResources().getString(R.string.lm_0s);
				general = 6;
				break;
			case 62: // RadarCalibration
				sSpecific = getResources().getString(R.string.lm_1s);
				general = 6;
				break;
			case 63: // Cubesats
				sSpecific = getResources().getString(R.string.lm_2s);
				general = 6;
				break;
			case 64: //Other
				sSpecific = getResources().getString(R.string.lm_3s);
				general = 6;
				break;
			case 70: // comes from search.
				general = 7;
		} // end switch (v.getId())
		
		String aux;
		if (general == 7) {
			aux = "";
		} else {
		
			if (select == 0) { 
				String sGeneralTypeSaved[] = getResources().getStringArray(R.array.listaGeneral_s); 
				aux =  sGeneralTypeSaved[general - 1];
			} else {
				aux = sSpecific;
			}
		}
		return aux;
	}


	private float scaleButtonText (int h, int w, int p, String bText) {
		
		Paint paintRectText = new Paint();
		paintRectText.setTextSize(h); // Units are pixels here
		paintRectText.setTextScaleX(1.0f);
		Rect bounds = new Rect();
	
		// ask the paint for the bounding rect if it were to draw this text.
		paintRectText.getTextBounds(bText, 0, bText.length(), bounds);
	
		// determine the width
		int wText = bounds.right - bounds.left;
	
		// Calculate the new scale in x direction to fit the text to the button width
		float TextScaleX = (float) (w - 2*p) / (float) wText; 
	
	return TextScaleX;
	}
}

