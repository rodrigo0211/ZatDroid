package com.zatdroid.rodrigo;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class listaTipoSat2 extends Activity implements View.OnClickListener {

	int selected = 0;
	ArrayList<String> alistaTipoSat = new ArrayList<String>();
	String sNombreListaElegida;
	public static String fileName = "myFile";	
	Button bLast30, bSpaceStations, b100Brightest, bBreadCrumbs, bBreadCrumbs2, bBreadCrumbsEnd;
	SharedPreferences tipoGuardado;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final float height = getResources().getDisplayMetrics().heightPixels; // Scale for dif screens sizes
		final float width = getResources().getDisplayMetrics().widthPixels; // Scale for dif screens sizes
		
//		String[] sListaTipoSat;
		
		// Retrieve sat type chosen from Preferences
	//	tipoGuardado = getSharedPreferences(listaTipoSat.nombreArchivo, 0);
	//	String sTipoGuardado = tipoGuardado.getString("tipoSatGeneral",
	//			"No se pudo cargar el dato");
		
		tipoGuardado = getSharedPreferences(listaTipoSat.fileName, 0);
		int iTipoGuardado = tipoGuardado.getInt("tipoSatGeneral",0);
		
		setContentView(layoutCode(width, height, iTipoGuardado));
	}

	@Override
	public void onClick(View v) {
		
//		SharedPreferences typeSaved;
//		String[] sListaTipoSat; // save array in resources
//		int position = 20;
//		String elegido="";
		
		tipoGuardado = getSharedPreferences(listaTipoSat.fileName, 0);
		int iTipoGuardado = tipoGuardado.getInt("tipoSatGeneral",0);

		if (v.getId()==1000) {
			finish(); // bBreadCrumbs back home
		} else { // save type chosen in an int
			int elegido = saveType(iTipoGuardado,v);
			saveData_openList(elegido);
		} //end if
		
	}
	
	private int saveType(int iTipoGuardado, View v) {
		
		int elegido= 0;
		
		switch (iTipoGuardado) {
		case 1: // Special Interest
			//Retrieve type list from resources
	//		sListaTipoSat = getResources().getStringArray(R.array.listaSpecialInterest);
			switch (v.getId()) {
				case 11: //bLast30
					elegido = 11;
					break;
				case 12: // R.id.bSpaceStations:
					elegido = 12;
					break;
				case 13: //R.id.b100Brightest:
					elegido = 13;
					break;
			}
		case 2: // Weather & Earth
			
			switch (v.getId()) {
				case 21: //bWeather
					elegido = 21;
					break;
				case 22: // NOAA:
					elegido = 22;
					break;
				case 23: // GOES:
					elegido = 23;
					break;
				case 24: //Earth&Resources:
					elegido = 24;
					break;
				case 25: //Search&Rescue
					elegido = 25;
					break;
				case 26: //DisastMonit
					elegido = 26;
					break;
				case 27: //TrackData
					elegido = 27;
					break;
			}
		case 3: // Communications
			
			switch (v.getId()) {
				case 31: // GEo
					elegido = 31;
					break;
				case 32: // IntelSat
					elegido = 32;
					break;
				case 33: //Gorizont
					elegido = 33;
					break;
				case 34: //Raduga
					elegido = 34;
					break;
				case 35: //Molniya
					elegido = 35;
					break;
				case 36: //Iridium
					elegido = 36;
					break;
				case 37: //Orbcom
					elegido = 37;
					break;
				case 38: //GlobalStar
					elegido = 38;
					break;
				case 39: //AmateurRadio
					elegido = 39;
					break;
				case 391: //Experimental
					elegido = 391;
					break;
				case 392: //Other
					elegido = 392;
					break;
			}
		case 4: // Navigation
			
			switch (v.getId()) {
				case 41: //GPS
					elegido = 41;
					break;
				case 42: // Glonass
					elegido = 42;
					break;
				case 43: // Galileo
					elegido = 43;
					break;
				case 44: //WAAS/EGNOS/MSAS
					elegido = 44;
					break;
				case 45: //NavyNavegat
					elegido = 45;
					break;
				case 46: //RussianLEO
					elegido = 46;
					break;
			}
		case 5: // Scientific
			
			switch (v.getId()) {
				case 51: //Space&Earth
					elegido = 51;
					break;
				case 52: // Geodetic
					elegido = 52;
					break;
				case 53: // Engineering
					elegido = 53;
					break;
				case 54: //Educational
					elegido = 54;
					break;
			}	
		case 6: // Miscellaneous
			
			switch (v.getId()) {
				case 61: //Military
					elegido = 61;
					break;
				case 62: // RadarCalibration
					elegido = 62;
					break;
				case 63: // Cubesats
					elegido = 63;
					break;
				case 64: //Other
					elegido = 64;
					break;
			}	
			
	} // end switch (v.getId())
	return elegido;
	}
	
	private void saveData_openList(int elegido) {
		
		tipoGuardado = getSharedPreferences(listaTipoSat2.fileName, 0);
		SharedPreferences.Editor editor = tipoGuardado.edit();
		editor.putInt("tipoSatEspecifico", elegido);
		editor.commit();
		
		// Open activity listaTipoSat2
		Intent startApp = new Intent("com.zatdroid.rodrigo.LISTASATS");
		startActivity(startApp);
	}

	private LinearLayout layoutCode(float width,float height, int typeSelected) {
		
		//base linearlayout vertical
		LinearLayout l0 = new LinearLayout(this);
		l0.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramsl0 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l0.setLayoutParams(paramsl0);
		
		// Add BreadCrumbs horizontal linearlayout to base linearlayout
		l0.addView(breadCrumbs(width, height,typeSelected));	
		
		// Linearlayout for sending to scrollview
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramsl1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		l1.setLayoutParams(paramsl1);
		

        // scrollview does not contain BreadcRumbs
		// It is usually not needed. Just in case and for future reuses of this module
		int sct = (int) (0.05 * height);
		ScrollView scrollView= new ScrollView(this);
		LinearLayout.LayoutParams paramsSc = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsSc.setMargins(0, sct, 0, 0);
		scrollView.setLayoutParams(paramsSc);
		
			// Three lines of icons (2icons per line)
		switch (typeSelected) {
		case 1: // Special Interest

			//Retrieve images of icons
			Drawable d11 = getResources().getDrawable(R.drawable.last30);
			Drawable d12 = getResources().getDrawable(R.drawable.spacestations);
			Drawable d13 = getResources().getDrawable(R.drawable.brightest);
			
			//Retrieve strings
			String s11 = getResources().getString(R.string.lsi_0);
			String s12 = getResources().getString(R.string.lsi_1);
			String s13 = getResources().getString(R.string.lsi_2);

			//Create buttons
			Button  bLast30 = new Button(this);
			Button  bSpaceStations = new Button(this);
			Button  bBrightest = new Button(this);
			
			// Adding buttons to the main layout
			l1.addView(iconsLine(false, bLast30, bSpaceStations,width, height, d11, d12, s11, s12, 11, 12 ));
			l1.addView(iconsLine(true, bBrightest, null, width, height, d13, null, s13, null, 13, 14 ));
			
			// Setting onClickListeners
			bLast30.setOnClickListener(this);
			bSpaceStations.setOnClickListener(this);
			bBrightest.setOnClickListener(this);
			
			break;
		
		case 2: // Weather and Earth
			
			//Retrieve images of icons
			Drawable d21 = getResources().getDrawable(R.drawable.weather);
			Drawable d22 = getResources().getDrawable(R.drawable.noaa);
			Drawable d23 = getResources().getDrawable(R.drawable.goes);
			Drawable d24 = getResources().getDrawable(R.drawable.earthresources);
			Drawable d25 = getResources().getDrawable(R.drawable.searchrescue);
			Drawable d26 = getResources().getDrawable(R.drawable.disastermonitoring);
			Drawable d27 = getResources().getDrawable(R.drawable.trackdata);
			
			//Retrieve strings
			String s21 = getResources().getString(R.string.lwe_0);
			String s22 = getResources().getString(R.string.lwe_1);
			String s23 = getResources().getString(R.string.lwe_2);
			String s24 = getResources().getString(R.string.lwe_3);
			String s25 = getResources().getString(R.string.lwe_4);
			String s26 = getResources().getString(R.string.lwe_5);
			String s27 = getResources().getString(R.string.lwe_6);

			//Create buttons
			Button  bWeather = new Button(this);
			Button  bNoaa = new Button(this);
			Button  bGoes = new Button(this);
			Button  bEarthResources = new Button(this);
			Button  bSearchRescue = new Button(this);
			Button  bDisastMonit = new Button(this);
			Button  bTrackData = new Button(this);
			
			// Adding buttons to the main layout
			l1.addView(iconsLine(false, bWeather, bNoaa,width, height, d21, d22, s21, s22, 21, 22 ));
			l1.addView(iconsLine(false, bGoes, bEarthResources, width, height, d23, d24, s23, s24, 23, 24 ));
			l1.addView(iconsLine(false, bSearchRescue, bDisastMonit, width, height, d25, d26, s25, s26, 25, 26 ));
			l1.addView(iconsLine(true, bTrackData, null, width, height, d27, null, s27, null, 27, 28 ));
			
			// Setting onClickListeners
			bWeather.setOnClickListener(this);
			bNoaa.setOnClickListener(this);
			bGoes.setOnClickListener(this);
			bEarthResources.setOnClickListener(this);
			bSearchRescue.setOnClickListener(this);
			bDisastMonit.setOnClickListener(this);
			bTrackData.setOnClickListener(this);
			
			break;
		case 3: // Communications

			//Retrieve images of icons
			Drawable d31 = getResources().getDrawable(R.drawable.geo);
			Drawable d32 = getResources().getDrawable(R.drawable.intelsat);
			Drawable d33 = getResources().getDrawable(R.drawable.gorizont);
			Drawable d34 = getResources().getDrawable(R.drawable.raduga);
			Drawable d35 = getResources().getDrawable(R.drawable.molniya);
			Drawable d36 = getResources().getDrawable(R.drawable.iridium);
			Drawable d37 = getResources().getDrawable(R.drawable.orbcom);
			Drawable d38 = getResources().getDrawable(R.drawable.globalstar);
			Drawable d39 = getResources().getDrawable(R.drawable.amateurradio);
			Drawable d391 = getResources().getDrawable(R.drawable.experimental);
			Drawable d392 = getResources().getDrawable(R.drawable.other);
			
			//Retrieve strings
			String s31 = getResources().getString(R.string.lc_0);
			String s32 = getResources().getString(R.string.lc_1);
			String s33 = getResources().getString(R.string.lc_2);
			String s34 = getResources().getString(R.string.lc_3);
			String s35 = getResources().getString(R.string.lc_4);
			String s36 = getResources().getString(R.string.lc_5);
			String s37 = getResources().getString(R.string.lc_6);
			String s38 = getResources().getString(R.string.lc_7);
			String s39 = getResources().getString(R.string.lc_8);
			String s391 = getResources().getString(R.string.lc_9);
			String s392 = getResources().getString(R.string.lc_10);

			//Create buttons
			Button  bGeo = new Button(this);
			Button  bIntelSat = new Button(this);
			Button  bGorizont = new Button(this);
			Button  bRaduga = new Button(this);
			Button  bMolniya = new Button(this);
			Button  bIridium = new Button(this);
			Button  bOrbcom = new Button(this);
			Button  bGlobalStar = new Button(this);
			Button  bAmateurRadio = new Button(this);
			Button  bExperimental = new Button(this);
			Button  bOther = new Button(this);
			
			// Adding buttons to the main layout
			l1.addView(iconsLine(false, bGeo, bIntelSat,width, height, d31, d32, s31, s32, 31, 32 ));
			l1.addView(iconsLine(false, bGorizont, bRaduga, width, height, d33, d34, s33, s34, 33, 34 ));
			l1.addView(iconsLine(false, bMolniya, bIridium, width, height, d35, d36, s35, s36, 35, 36 ));
			l1.addView(iconsLine(false, bOrbcom, bGlobalStar, width, height, d37, d38, s37, s38, 37, 38 ));
			l1.addView(iconsLine(false, bAmateurRadio, bExperimental, width, height, d39, d391, s39, s391, 39, 391 ));
			l1.addView(iconsLine(true, bOther, null, width, height, d392, null, s392, null, 392, 393));
			
			// Setting onClickListeners
			bGeo.setOnClickListener(this);
			bIntelSat.setOnClickListener(this);
			bGorizont.setOnClickListener(this);
			bRaduga.setOnClickListener(this);
			bMolniya.setOnClickListener(this);
			bIridium.setOnClickListener(this);
			bOrbcom.setOnClickListener(this);
			bGlobalStar.setOnClickListener(this);
			bAmateurRadio.setOnClickListener(this);
			bExperimental.setOnClickListener(this);
			bOther.setOnClickListener(this);
			break;
		case 4: //Navigation
			
			//Retrieve images of icons
			Drawable d41 = getResources().getDrawable(R.drawable.gps);
			Drawable d42 = getResources().getDrawable(R.drawable.glonass);
			Drawable d43 = getResources().getDrawable(R.drawable.galileo);
			Drawable d44 = getResources().getDrawable(R.drawable.waas);
			Drawable d45 = getResources().getDrawable(R.drawable.navynav);
			Drawable d46 = getResources().getDrawable(R.drawable.russian_leo);
			
			//Retrieve strings
			String s41 = getResources().getString(R.string.ln_0);
			String s42 = getResources().getString(R.string.ln_1);
			String s43 = getResources().getString(R.string.ln_2);
			String s44 = getResources().getString(R.string.ln_3);
			String s45 = getResources().getString(R.string.ln_4);
			String s46 = getResources().getString(R.string.ln_5);

			//Create buttons
			Button  bGps = new Button(this);
			Button  bGlonass = new Button(this);
			Button  bGalileo = new Button(this);
			Button  bWass = new Button(this);
			Button  bNavyNav = new Button(this);
			Button  bRussianLeo = new Button(this);
			
			// Adding buttons to the main layout
			l1.addView(iconsLine(false, bGps, bGlonass,width, height, d41, d42, s41, s42, 41, 42 ));
			l1.addView(iconsLine(false, bGalileo, bWass, width, height, d43, d44, s43, s44, 43, 44 ));
			l1.addView(iconsLine(false, bNavyNav, bRussianLeo, width, height, d45, d46, s45, s46, 45, 46 ));
			
			// Setting onClickListeners
			bGps.setOnClickListener(this);
			bGlonass.setOnClickListener(this);
			bGalileo.setOnClickListener(this);
			bWass.setOnClickListener(this);
			bNavyNav.setOnClickListener(this);
			bRussianLeo.setOnClickListener(this);
			break;
		case 5: // Scientific
			
			//Retrieve images of icons
			Drawable d51 = getResources().getDrawable(R.drawable.spaceearth);
			Drawable d52 = getResources().getDrawable(R.drawable.geodetic);
			Drawable d53 = getResources().getDrawable(R.drawable.engineering);
			Drawable d54 = getResources().getDrawable(R.drawable.education);
			
			//Retrieve strings
			String s51 = getResources().getString(R.string.ls_0);
			String s52 = getResources().getString(R.string.ls_1);
			String s53 = getResources().getString(R.string.ls_2);
			String s54 = getResources().getString(R.string.ls_3);

			//Create buttons
			Button  bSpaceEarth = new Button(this);
			Button  bGeodetic = new Button(this);
			Button  bEngineering = new Button(this);
			Button  bEducation = new Button(this);
			
			// Adding buttons to the main layout
			l1.addView(iconsLine(false, bSpaceEarth, bGeodetic,width, height, d51, d52, s51, s52, 51, 52 ));
			l1.addView(iconsLine(false, bEngineering, bEducation, width, height, d53, d54, s53, s54, 53, 54 ));
			
			// Setting onClickListeners
			bSpaceEarth.setOnClickListener(this);
			bGeodetic.setOnClickListener(this);
			bEngineering.setOnClickListener(this);
			bEducation.setOnClickListener(this);
			break;
		case 6: // Miscellaneous
			
			//Retrieve images of icons
			Drawable d61 = getResources().getDrawable(R.drawable.military);
			Drawable d62 = getResources().getDrawable(R.drawable.radarcalibration);
			Drawable d63 = getResources().getDrawable(R.drawable.cubesat);
			Drawable d64 = getResources().getDrawable(R.drawable.other);
			
			//Retrieve strings
			String s61 = getResources().getString(R.string.lm_0);
			String s62 = getResources().getString(R.string.lm_1);
			String s63 = getResources().getString(R.string.lm_2);
			String s64 = getResources().getString(R.string.lm_3);

			//Create buttons
			Button  bMilitary = new Button(this);
			Button  bRadarCalib = new Button(this);
			Button  bCubeSats = new Button(this);
			Button  bOther2 = new Button(this);
			
			// Adding buttons to the main layout
			l1.addView(iconsLine(false, bMilitary, bRadarCalib,width, height, d61, d62, s61, s62, 61, 62 ));
			l1.addView(iconsLine(false, bCubeSats, bOther2, width, height, d63, d64, s63, s64, 63, 64 ));
			
			// Setting onClickListeners
			bMilitary.setOnClickListener(this);
			bRadarCalib.setOnClickListener(this);
			bCubeSats.setOnClickListener(this);
			bOther2.setOnClickListener(this);
			break;
		case 20: // extra case
			break;
		}
			    
		// ScrollView does not contain BreadCrumbs.
		scrollView.addView(l1);
		l0.addView(scrollView);
		
		return l0;
	}
	
	private LinearLayout iconsLine( boolean oneIcon, Button b0, Button b1,
								float width,float height, Drawable d0, Drawable d1, 
								String s0, String s1, int id0, int id1) {

		// Layout from first 2 icons
		int h2 = (int) (0.18 * height);
		int m2_l, m2_r;
		// If oneIcon is true, the line has one icon. Margins change
		if (oneIcon) {  
			m2_l = (int) (0.35* width);
			m2_r = (int) (0.125* width);
		} else {
			m2_l = (int) (0.125* width);
			m2_r = (int) (0.125* width);
		}
		int m2_t = (int) (0.05 * height);
		
		LinearLayout l2 = new LinearLayout(this);
		l2.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramsl22 = new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsl22.height = h2;
		paramsl22.setMargins(m2_l,m2_t, m2_r, 0);
		l2.setLayoutParams(paramsl22);
		int w_icon = (int) (0.3* width);
		int m_l_icon = (int) (0.15 * width);
		
		/// ********************
		// modify margins
		LinearLayout.LayoutParams params_icon_l = new LinearLayout.LayoutParams(
				w_icon,	LinearLayout.LayoutParams.MATCH_PARENT );
		params_icon_l.setMargins(0, 0, 0, 0); // Center icons for different screen sizes
		
		// Icon0 
		b0.setLayoutParams(params_icon_l);
		b0.setText(s0);
		b0.setTextColor(Color.WHITE);
		int t_icon = (int) (0.12 * h2);

		// Fit text size to button size
		float sx = scaleButtonText(t_icon, w_icon, 0,s0);
		if (sx < 1.3) {
			b0.setTextScaleX(sx);
		} else {
			b0.setTextScaleX(1.3f);
		}
//		b0.setTextScaleX(scaleButtonText(t_icon, w_icon, 0,s0));
		b0.setTextSize(TypedValue.COMPLEX_UNIT_PX, t_icon);
		b0.setSingleLine(true);

		b0.setPadding(0, 0, 0, 0);
		b0.setGravity(Gravity.BOTTOM | Gravity.CENTER );
		b0.setBackgroundDrawable(d0);
		b0.setId(id0);
		l2.addView(b0);
		
		LinearLayout.LayoutParams params_icon_r = new LinearLayout.LayoutParams(
				w_icon,	LinearLayout.LayoutParams.MATCH_PARENT );
		params_icon_r.setMargins(m_l_icon, 0, 0, 0); 
		
		// Icon1
		if (!oneIcon) {
			b1.setLayoutParams(params_icon_r);
			b1.setText(s1);
			b1.setTextColor(Color.WHITE);
			// Fit text size to button size
			b1.setSingleLine(true);
			
			float sx1 = scaleButtonText(t_icon, w_icon, 0,s1);
			if (sx1 < 1.3) {
				b1.setTextScaleX(sx1);
			} else {
				b1.setTextScaleX(1.3f);
			}
			//b1.setTextScaleX(scaleButtonText(t_icon, w_icon, 0,s1));
			b1.setTextSize(TypedValue.COMPLEX_UNIT_PX, t_icon);
			
			b1.setPadding(0, 0, 0, 0);
			b1.setGravity(Gravity.BOTTOM | Gravity.CENTER);
			b1.setBackgroundDrawable(d1);
			b1.setId(id1);
			l2.addView(b1);	
		}
		
		return l2;
	}	
	
	private LinearLayout breadCrumbs( float width,float height, int typeSelected) {

		// First LinearLayout horizontal for BreadCrumbs
		int h = (int) (0.05 * height);
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramsl1 = new LinearLayout.LayoutParams(
			     ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsl1.setMargins(0, 0, 0, 0);
		paramsl1.height = h;
		l1.setLayoutParams(paramsl1);
		
		// BreadCrumbs button 1
		int w1 = (int) (0.20 * width);
		int h1 = (int) (0.8 * h);
		int p1 = (int) (0.15 * w1);
		int p1t = (int) (0.10 * h);
		String bText = getResources().getString(R.string.home);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				w1, LinearLayout.LayoutParams.MATCH_PARENT);
		Button  bBreadCrumbs = new Button(this);
		bBreadCrumbs.setText(bText);
		bBreadCrumbs.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs1));
		params1.setMargins(0,0,0,0); // Center icons for different screen sizes
		bBreadCrumbs.setTextColor(Color.WHITE);
		bBreadCrumbs.setPadding(0, 0, 0, 0);
		bBreadCrumbs.setGravity(Gravity.CENTER | Gravity.CENTER);
		bBreadCrumbs.setLayoutParams(params1);
		bBreadCrumbs.setId(1000);
			// Fit text to button
		bBreadCrumbs.setTextScaleX(scaleButtonText(h1-p1t, w1, p1,bText));
		bBreadCrumbs.setTextSize(TypedValue.COMPLEX_UNIT_PX, h1);
		bBreadCrumbs.setSingleLine(true);
		
		l1.addView(bBreadCrumbs);
		
		// BreadCrumbs button 2
		String[] sListaTipoSat = getResources().getStringArray(R.array.listaGeneral); 
		int w2 = (int) (0.40 * width);
		int h2 = (int) (0.8 * h);
		int p2 = (int) (0.10 * w2);
		int p2t = (int) (0.10 * h);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				w2, LinearLayout.LayoutParams.MATCH_PARENT);
		Button  bBreadCrumbs2 = new Button(this);
		bBreadCrumbs2.setText(sListaTipoSat[typeSelected - 1]);
		bBreadCrumbs2.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs2));
		params2.setMargins(0,0,0,0); // Center icons for different screen sizes
		bBreadCrumbs2.setTextColor(Color.WHITE);
		bBreadCrumbs2.setPadding(0, 0, 0, 0);
		bBreadCrumbs2.setGravity(Gravity.CENTER | Gravity.CENTER);
		bBreadCrumbs2.setLayoutParams(params2);
			// Fit text to button
		float sx = scaleButtonText(h2-p2t, w2, p2,sListaTipoSat[typeSelected - 1]);
		if (sx < 1.3) {
			bBreadCrumbs2.setTextScaleX(sx);
		} else {
			bBreadCrumbs2.setTextScaleX(1.3f);
		}
		bBreadCrumbs2.setTextSize(TypedValue.COMPLEX_UNIT_PX, h2);
		bBreadCrumbs2.setSingleLine(true);
		
		l1.addView(bBreadCrumbs2);
		
		// BreadCrumbs button 3 (fill)
		Button  bBreadCrumbsEnd = new Button(this);
		bBreadCrumbsEnd.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs3));
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT );
		params3.setMargins(0, 0, 0, 0);
		bBreadCrumbsEnd.setLayoutParams(params3);
		
		l1.addView(bBreadCrumbsEnd);
		
		// Setting onClickListener to bBreadCrumbs (home)
		bBreadCrumbs.setOnClickListener(this);
		
	return l1;
		
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
