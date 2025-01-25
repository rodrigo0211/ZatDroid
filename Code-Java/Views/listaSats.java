package com.zatdroid.rodrigo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class listaSats extends ListActivity {
	
	ArrayList<String> alistaSat = new ArrayList<String>();
	SharedPreferences satNamePicked, SpecificTypeSaved;
	public static String fileName = "myFile";	

	String FILENAME_XML = "TLE.xml";
	String FILENAME_XSD = "TLE.xsd";
	String sUrlTLE; // Save URL from the type of sat chosen with TLE
	sat sat, sat2;
	double ctimeJD;
	Boolean error_urlCelestrack = false;
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// It is executed after onCreate, and takes the value of the sat Chosen
		super.onListItemClick(l, v, position, id);

		String elegido = alistaSat.get(position);
		Context context = null;

		// Save the name of the sat chosen in Shared Preferences
		satNamePicked = getSharedPreferences(listaSats.fileName, 0);
		SharedPreferences.Editor editor = satNamePicked.edit();
		editor.putString("satElegido", elegido);
		editor.commit();

		try {
			// Prepare SAXParser
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			// Android.SAX to search into the XML the chosen sat and save all its data
			satElegidoSAXHandler dataHandler = new satElegidoSAXHandler();
			xr.setContentHandler(dataHandler);
			context = this.getApplicationContext();
			dataHandler.parse(new FileInputStream(getFilesDir()
					+ File.separator + FILENAME_XML), context);
			
			//Save data of the chosen sat, in the satElegido variable type "sat"
			sat = dataHandler.getData();
			
			/*
			//Testing MathFunctions
			CalculationsMaths MathFunctions = new CalculationsMaths();
			MathFunctions.test();
			
			//Testing Time Functions 
			CalculationsTime TimeFunctions = new CalculationsTime();
			TimeFunctions.test();
			*/
			
			/* ********* TEST SGP4 FROM GPredict ****************
			 TLE:
			   	TEST SAT SGP 001
					1 88888U          80275.98708465  .00073094  13844-3  66816-4 0     9
					2 88888  72.8435 115.9689 0086731  52.6988 110.5714 16.05824518   103
			 Results for tsince = 0 seconds.
			 r: 2328.97048951, -5995.22076416, 1719.97067261
			 v: 2.91207230, -0.98341546, -7.09081703
			 
			sat.setInclination(72.8435);
			sat.setRaan(115.9689);
			sat.setEccentricity(0.0086731);
			sat.setArgumentPerigee(52.6988);
			sat.setMeanAnomaly(110.5714);
			sat.setMeanMotion(16.05824518);
			sat.setMeanMotionDerivate(0.00073094);
			sat.setEpochYear(80);
			sat.setEpoch(275.98708465);
			sat.setBstarDragTerm(0.66816E-4);
			sat.showLog();
			 ***************************************************** */
			
		} catch (ParserConfigurationException pce) {
			Log.e("SAX XML", "sax parse error", pce);
		} catch (SAXException se) {
			Log.e("SAX XML", "sax error", se);
		} catch (FileNotFoundException a) {
			Log.e("SAX XML", "sax parse FileNotFound error", a);
		}

		 // Open activity menuOpciones
		 // and pass the class object sat with all data from the chosen sat
		 Intent startApp = new Intent("com.zatdroid.rodrigo.MENUOPCIONES");
		 startApp.putExtra("datosSatElegido", sat);		 
		 startActivity(startApp);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final float height = getResources().getDisplayMetrics().heightPixels; // Scale for dif screens sizes
		final float width = getResources().getDisplayMetrics().widthPixels; // Scale for dif screens sizes
		
		// Retrieve SpecificTypeSaved, which also tells about GeneralTypeSaved
		SpecificTypeSaved = getSharedPreferences(listaTipoSat2.fileName, 0);
		int iSpecificTypeSaved = SpecificTypeSaved.getInt("tipoSatEspecifico",0);
		
		setContentView(R.layout.list_sats);
		
		ViewGroup mContainer = (ViewGroup) findViewById(R.id.container);
		mContainer.addView(breadCrumbs(height, width, iSpecificTypeSaved));
		
		// Select URL according to chosen sat 
		if ((sUrlTLE = seleccionarUrlTLE(iSpecificTypeSaved)).equals("error")) {
			mostrarAlerta(getResources().getString(R.string.errorURL1)
					, getResources().getString(R.string.errorURL2)
					, getResources().getString(R.string.errorURL3));
		}
		/////////////////////////////////////////////////////////////////////
		/////// ASYNCTASK: Download txt, Write XML, Write XSD //////////////
		String sTipoGuardado = "";
		ProgressDialog dialog = new ProgressDialog(this);
		Resources res = getResources();
		dialog.setMessage(res.getString(R.string.loading));
		new MiTarea(dialog).execute(sTipoGuardado);
		/////////////////////////////////////////////////////////////////////
	}

	private LinearLayout breadCrumbs(float height, float width, int iSpecificTypeSaved) {
		
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
		Button bBreadCrumbs3 =  breadCrumbsButton(0f, h,
				0.400f, 0.20f, 0.20f, sSpecificTypeSaved, 
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
		// Setting onClickListener to bBreadCrumbs1 (General list)
		bBreadCrumbs2.setOnClickListener(new Button.OnClickListener() {  
			public void onClick(View v) {
	    		// Back General list
				finish();
			}
		});

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
		/* select = 0: returns iGeneralTypeSaved
		 * select = 1: returns iSpecificTypeSaved 
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
			
		} // end switch (v.getId())
		
		String aux;
		
		if (select == 0) { 
			String sGeneralTypeSaved[] = getResources().getStringArray(R.array.listaGeneral_s); 
			aux =  sGeneralTypeSaved[general - 1];
		} else {
			aux = sSpecific;
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
	

	private void mostrarAlerta(String titulo, String mensaje1, String mensaje2) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(titulo);
		alertDialog.setMessage(mensaje1 + "\n" + mensaje2);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}

	private ArrayList<String> buscarSatsEnXML() {

		try {
			// Preparar SAXParser
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			// ANDROID.sax para buscar dentro del XML
			listaSatsSAXHandler dataHandler = new listaSatsSAXHandler();
			xr.setContentHandler(dataHandler);
			dataHandler.parse(new FileInputStream(getFilesDir()
					+ File.separator + FILENAME_XML));

			return dataHandler.getData();

		} catch (ParserConfigurationException pce) {
			Log.e("SAX XML", "sax parse error", pce);
		} catch (SAXException se) {
			Log.e("SAX XML", "sax error", se);
		} catch (FileNotFoundException a) {
			Log.e("SAX XML", "sax parse FileNotFound error", a);
		} catch (Exception e) {
			Log.e("SAX XML", "sax parse error", e);
		}

		return null;
	}

	private String seleccionarUrlTLE(int iTipoGuardado) {
		
		String aux="";
		switch (iTipoGuardado) {
		///////////////////// Special Interest /////////////////////////////////
		case 11:
			aux = "http://celestrak.com/NORAD/elements/tle-new.txt";
			break;
		case 12:
			aux = "http://celestrak.com/NORAD/elements/stations.txt";
			break;
		case 13:
			aux = "http://celestrak.com/NORAD/elements/visual.txt";
			break;
			
		///////////////////// Weather and Earth Resources //////////////////////////////
		case 21:
			aux = "http://celestrak.com/NORAD/elements/weather.txt";
			break;
		case 22:
			aux = "http://celestrak.com/NORAD/elements/noaa.txt";
			break;
		case 23:
			aux = "http://celestrak.com/NORAD/elements/goes.txt";
			break;
		case 24:
			aux = "http://celestrak.com/NORAD/elements/resource.txt";
			break;
		case 25:
			aux = "http://celestrak.com/NORAD/elements/sarsat.txt";
			break;
		case 26:
			aux = "http://celestrak.com/NORAD/elements/dmc.txt";
			break;
		case 27:
			aux = "http://celestrak.com/NORAD/elements/tdrss.txt";
			break;
			
		///////////////////// Communications //////////////////////////////
		case 31:
			aux = "http://celestrak.com/NORAD/elements/geo.txt";
			break;
		case 32:
			aux = "http://celestrak.com/NORAD/elements/intelsat.txt";
			break;
		case 33:
			aux = "http://celestrak.com/NORAD/elements/gorizont.txt";
			break;
		case 34:
			aux = "http://celestrak.com/NORAD/elements/raduga.txt";
			break;
		case 35:
			aux = "http://celestrak.com/NORAD/elements/molniya.txt";
			break;
		case 36:
			aux = "http://celestrak.com/NORAD/elements/iridium.txt";
			break;
		case 37:
			aux = "http://celestrak.com/NORAD/elements/orbcomm.txt";
			break;
		case 38:
			aux = "http://celestrak.com/NORAD/elements/globalStar.txt";
			break;
		case 39:
			aux = "http://celestrak.com/NORAD/elements/amateur.txt";
			break;
		case 391:
			aux = "http://celestrak.com/NORAD/elements/other-comm.txt";
			break;
		case 392:
			aux = "http://celestrak.com/NORAD/elements/x-comm.txt";
			break;
		///////////////////// Navigation //////////////////////////////
		case 41:
			aux = "http://celestrak.com/NORAD/elements/gps-ops.txt";
			break;
		case 42:
			aux = "http://celestrak.com/NORAD/elements/glo-ops.txt";
			break;
		case 43:
			aux = "http://celestrak.com/NORAD/elements/galileo.txt";
			break;
		case 44:
			aux = "http://celestrak.com/NORAD/elements/sbas.txt";
			break;
		case 45:
			aux = "http://celestrak.com/NORAD/elements/nnss.txt";
			break;
		case 46:
			aux = "http://celestrak.com/NORAD/elements/musson.txt";
			break;
		
		///////////////////// Scientific //////////////////////////////
		case 51:
			aux = "http://celestrak.com/NORAD/elements/science.txt";
			break;
		case 52:
			aux = "http://celestrak.com/NORAD/elements/geodetic.txt";
			break;
		case 53:
			aux = "http://celestrak.com/NORAD/elements/engineering.txt";
			break;
		case 54:
			aux = "http://celestrak.com/NORAD/elements/education.txt";
			break;
		
		///////////////////// Miscellaneous //////////////////////////////
		case 61:
			aux = "http://celestrak.com/NORAD/elements/military.txt";
			break;
		case 62:
			aux = "http://celestrak.com/NORAD/elements/radar.txt";
			break;
		case 63:
			aux = "http://celestrak.com/NORAD/elements/cubesat.txt";
			break;
		case 64:
			aux = "http://celestrak.com/NORAD/elements/other.txt";
			break;
		}
		return aux;
	}

	protected void escribirXML(BufferedWriter xml, BufferedReader in) {

		StringBuilder linea1;
		String linea2, linea3;
		String aux = null;
		String[] splitLinea2 = null, splitLinea3 = null;
		Boolean continuar;

		String INI = "<";
		String END = "</";
		// // Complex elements
		String TLE = "TLE>";
		String sat = "sat>";
		String keplerianElements = "keplerianElements>";
		String info = "info>";
		String otherParameters = "otherParameters>";
		String appData = "appData>";
		// // Single Elements
		String name = "name>";
		String number = "number>";
		String epochYear = "epochYear>";
		String epoch = "epoch>";
		String meanMotionDerivate = "meanMotionDerivate>";
		String bstarDragTerm = "bstarDragTerm>";
		String ephemeridesType = "ephemeridesType>";
		String inclination = "inclination>";
		String raan = "raan>";
		String eccentricity = "eccentricity>";
		String argumentPerigee = "argumentPerigee>";
		String meanAnomaly = "meanAnomaly>";
		String meanMotion = "meanMotion>";

		try {
			aux = in.readLine();
			// Head
			xml.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xml.newLine();
			xml.write("<TLE xmlns=\"http://www.w3schools.com\"");
			xml.newLine();
			xml.write("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			xml.newLine();
			xml.write("xsi:noNamespaceSchemaLocation=\"TLE.xsd\">");
			xml.newLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (Exception e) {
			Log.d("escribir xml", "error");
		}

		continuar = true;
		while (continuar) {

			// //Read txt file and parse it //////////////
			linea1 = new StringBuilder(aux);
			// //// Erase spaces after the name
			while (linea1.toString().endsWith(" ")) {
				linea1.delete(linea1.toString().length() - 1, linea1.toString()
						.length());
			}
			// & simbol give an error in the name. Erase it.
			if (linea1.toString().contains("&") ){
				linea1.deleteCharAt((linea1.toString().indexOf("&")));	
			}
			// second line
			try {
				linea2 = in.readLine();
				// read the whole line
				linea2 = linea2.trim().replaceAll(" +", " "); // Leave only one space
				splitLinea2 = linea2.split(" "); // parse it
				// Third line
				linea3 = in.readLine(); // read the whole line
				linea3 = linea3.trim().replaceAll(" +", " "); // Leave only one space
				splitLinea3 = linea3.split(" "); // parse it
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// ////////////////////////////////////////////////////
			// //// FORMAT XML FILE ///////////

			// / Data
			try {
				xml.write(INI + sat);
				xml.newLine();
				xml.write(INI + info);
				xml.newLine();
				xml.write(INI + name + linea1.toString() + END + name);
				xml.newLine();
				xml.write(INI + number + splitLinea2[1] + END + number);
				xml.newLine();
				xml.write(END + info);
				xml.newLine();
				xml.write(INI + keplerianElements);
				xml.newLine();
				xml.write(INI + inclination + splitLinea3[2] + END
						+ inclination);
				xml.newLine();
				xml.write(INI + raan + splitLinea3[3]
						+ END + raan);
				xml.newLine();
				xml.write(INI + eccentricity + "0." + splitLinea3[4] + END
						+ eccentricity);
				xml.newLine();
				xml.write(INI + argumentPerigee + splitLinea3[5] + END
						+ argumentPerigee);
				xml.newLine();
				xml.write(INI + meanAnomaly + splitLinea3[6] + END
						+ meanAnomaly);
				xml.newLine();
				if (splitLinea3[7].length() == 11) { // Sometimes MeanMotion has one digit missing
					xml.write(INI + meanMotion
							+ splitLinea3[7].substring(0, 11) + END
							+ meanMotion);
					xml.newLine();
				} else {
					xml.write(INI + meanMotion
							+ splitLinea3[7].substring(0, 10) + END
							+ meanMotion);
					xml.newLine();
				}
				xml.write(INI + epochYear + splitLinea2[3].substring(0, 2)
						+ END + epochYear);
				xml.newLine();
				xml.write(INI + epoch
						+ splitLinea2[3].substring(2, splitLinea2[3].length())
						+ END + epoch);
				xml.newLine();

				xml.write(END + keplerianElements);
				xml.newLine();
				xml.write(INI + otherParameters);
				xml.newLine();
				xml.write(INI + meanMotionDerivate + splitLinea2[4] + END
						+ meanMotionDerivate);
				xml.newLine();
				
				if (splitLinea2[6].substring(0,1).equals("-")) {
				xml.write(INI
						+ bstarDragTerm + "-0."
						+ splitLinea2[6].substring(1,
								splitLinea2[6].length() - 3) + "E"
						+ splitLinea2[6].substring(splitLinea2[6].length() - 2)
						+ END + bstarDragTerm);	

				} else {
				xml.write(INI
						+ bstarDragTerm + "0."
						+ splitLinea2[6].substring(0,
								splitLinea2[6].length() - 2) + "E"
						+ splitLinea2[6].substring(splitLinea2[6].length() - 2)
						+ END + bstarDragTerm);

				}
				xml.newLine();
				xml.write(INI + ephemeridesType + splitLinea2[7] + END
						+ ephemeridesType);
				xml.newLine();
				xml.write(END + otherParameters);
				xml.newLine();
				xml.write(END + sat);
				xml.newLine();

				// End of file
				aux = in.readLine();
				if (aux == null) {
					continuar = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // end while

		try {
			xml.write(END + TLE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception io) {
			// TODO Auto-generated catch block
			Log.e("XML", "write xml error", io);
		}
	}

	private void escribirXSD(BufferedWriter xsd) {
		// TODO Auto-generated method stub

		// //CREATE XSD FILE /////////////////
		// Head
		try {
			xsd.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			xsd.newLine();
			xsd.write("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
			xsd.newLine();
			xsd.write("targetNamespace=\"http://www.w3schools.com\"");
			xsd.newLine();
			xsd.write("xmlns=\"http://www.w3schools.com\">");
			xsd.newLine();
			// Simple Elements
			xsd.write("<!-- definition of simple elements -->");
			xsd.newLine();
			xsd.write("<xs:element name=\"name\" type=\"xs:string\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"number\" type=\"xs:string\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"inclination\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"raan\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"eccentricity\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"argumentPerigee\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"meanAnomaly\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"meanMotion\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"epochYear\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"epoch\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"semiejeMayor\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"meanMotionDerivate\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"bstarDragTerm\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"ephemeridesType\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"latitud\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"longitud\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"azimut\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"elevacion\" type=\"xs:double\"/>");
			xsd.newLine();
			xsd.write("<xs:element name=\"altitude\" type=\"xs:double\"/>");
			xsd.newLine();
			// Complex Elements
			xsd.write("<!-- definition of complex elements -->");
			xsd.newLine();
			xsd.write("<xs:element name=\"info\">");
			xsd.newLine();
			xsd.write("<xs:complexType>");
			xsd.newLine();
			xsd.write("<xs:sequence>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"name\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"number\"/>");
			xsd.newLine();
			xsd.write("</xs:sequence>");
			xsd.newLine();
			xsd.write("</xs:complexType>");
			xsd.newLine();
			xsd.write("</xs:element>");
			xsd.newLine();

			xsd.write("<xs:element name=\"keplerianElements\">");
			xsd.newLine();
			xsd.write("<xs:complexType>");
			xsd.newLine();
			xsd.write("<xs:sequence>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"inclination\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"raan\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"eccentricity\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"argumentPerigee\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"meanAnomaly\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"meanMotion\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"epochYear\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"epoch\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"semiejeMayor\" minOccurs=\"0\" />");
			xsd.newLine();
			xsd.write("</xs:sequence>");
			xsd.newLine();
			xsd.write("</xs:complexType>");
			xsd.newLine();
			xsd.write("</xs:element>");
			xsd.newLine();

			xsd.write("<xs:element name=\"otherParameters\">");
			xsd.newLine();
			xsd.write("<xs:complexType>");
			xsd.newLine();
			xsd.write("<xs:sequence>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"meanMotionDerivate\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"bstarDragTerm\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"ephemeridesType\"/>");
			xsd.newLine();
			xsd.write("</xs:sequence>");
			xsd.newLine();
			xsd.write("</xs:complexType>");
			xsd.newLine();
			xsd.write("</xs:element>");
			xsd.newLine();

			xsd.write("<xs:element name=\"sat\">");
			xsd.newLine();
			xsd.write("<xs:complexType>");
			xsd.newLine();
			xsd.write("<xs:sequence>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"info\"/>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"keplerianElements\" />");
			xsd.newLine();
			xsd.write("<xs:element ref=\"otherParameters\" />");
			xsd.newLine();

			xsd.write("</xs:sequence>");
			xsd.newLine();
			xsd.write("</xs:complexType>");
			xsd.newLine();
			xsd.write("</xs:element>");
			xsd.newLine();

			xsd.write("<xs:element name=\"TLE\">");
			xsd.newLine();
			xsd.write("<xs:complexType>");
			xsd.newLine();
			xsd.write("<xs:sequence>");
			xsd.newLine();
			xsd.write("<xs:element ref=\"sat\" maxOccurs=\"unbounded\" />");
			xsd.newLine();
			xsd.write("</xs:sequence>");
			xsd.newLine();
			xsd.write("</xs:complexType>");
			xsd.newLine();
			xsd.write("</xs:element>");
			xsd.newLine();

			xsd.write("</xs:schema>");
			xsd.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception io) {
			// TODO Auto-generated catch block
			Log.e("XSD", "escribir xsd  error", io);
		}

		// //////////////////////////////////////
	}

	public class MiTarea extends AsyncTask<String, Float, Integer>{
	   protected ProgressDialog dialog;
	   public MiTarea(ProgressDialog dialog) {
			this.dialog = dialog;
		}

       protected void onPreExecute() {
    	   dialog.setCancelable(true);
    	   dialog.setProgress(0);
           dialog.setMax(100);
           dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           dialog.show(); // Show Dialog before starting
        }
        protected Integer doInBackground(String... sTipoGuardado) {
          
        	// Load file txt from www.celestrack.com of the sat type chosen

    		// Create xml & xsd files
    		BufferedWriter xml = null, xsd = null;
    		BufferedReader in = null;      //, in2 = null;

    		try {
    			// Read file from the internet and save it in an internal file  

    			// Create a URL for the desired page
    			URL url = new URL(sUrlTLE);
    			publishProgress(10f);

    			// Read all the text returned by the server
    			in = new BufferedReader(new InputStreamReader(url.openStream()));
    			publishProgress(30f);
    			
    			// Check if Celestrack.com is working
    			in.mark(4);
    			String a = in.readLine().toString();
    			if ((a.equals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""))) {
    				error_urlCelestrack = true;
    			} else
    				in.reset();
    			
    			//Write xml file
    			File archivo_xml = new File(getFilesDir() + File.separator
    					+ FILENAME_XML);
    			xml = new BufferedWriter(new FileWriter(archivo_xml));
    			publishProgress(45f);
    			escribirXML(xml, in);
    			publishProgress(60f);
    			
    			//Write xsd file
    			File archivo_xsd = new  File(getFilesDir() + File.separator
    					+ FILENAME_XSD);
    			xsd = new BufferedWriter(new FileWriter(archivo_xsd));
    			publishProgress(65f);
    			escribirXSD(xsd);
    			publishProgress(100f);
    			
    		} catch (IOException e) {
    			Log.d("Error Cargar TLE", e.toString());
    			e.printStackTrace();
    		} catch (Exception e) {
    			Log.d("Error Cargar TLE", e.toString());
    		} finally {
    			try {
    				in.close();
    				xml.close();
    				xsd.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
        		
                return 100;
            }
  
            protected void onProgressUpdate (Float... valores) {
                int p = Math.round(valores[0]);
                dialog.setProgress(p);
            }
            protected void onPostExecute(Integer bytes) {
            	dialog.dismiss();

                // Alert message if the celestrack url is not available 
    		// it does not continue running the methode
    		if (error_urlCelestrack) {
    			mostrarAlerta(getResources().getString(R.string.errorCelestrack1)
    					, getResources().getString(R.string.errorCelestrack2)
    					, getResources().getString(R.string.errorCelestrack3));
    		} else {
    			// Search into the XML the list of the sat names to show it in the ListView 
    			alistaSat = buscarSatsEnXML();
    			
    			// Create the list for the listView to show it on the screen  
    			setListAdapter(new ArrayAdapter<String>(listaSats.this,
    					android.R.layout.simple_list_item_1, alistaSat));
    		}   
        }

	}
}


