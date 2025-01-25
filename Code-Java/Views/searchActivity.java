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
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.zatdroid.rodrigo.visibleSats.MiTarea;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class searchActivity extends ListActivity { 
	
	ArrayList<String> sUrlTLE = new ArrayList<String>(); // Save URL from the type of sat chosen with TLE
	ArrayList<String> alistaSat = new ArrayList<String>();
	public static String fileName = "myFile";
	private static String FILENAME_XML = "TLE_all.xml";
	private static String FILENAME_XSD = "TLE_all.xsd";
	public static boolean isXMLCreated= false;
	
	public static BufferedWriter xml = null, xsd = null;
	public static BufferedReader in = null;  
	
	SharedPreferences sharedType;
	int iSharedType;
	
	private String query; 
	sat sat;
	boolean m = false;
	String errorURL;
	
	Boolean error_urlCelestrack = false;
	Context context = null;
	
   public void onCreate(Bundle savedInstanceState) { 
      super.onCreate(savedInstanceState); 
      
      	final float height = getResources().getDisplayMetrics().heightPixels; // Scale for dif screens sizes
		final float width = getResources().getDisplayMetrics().widthPixels; // Scale for dif screens sizes
		
		context = this.getApplicationContext();
		
		setContentView(R.layout.list_sats);
		
		ViewGroup mContainer = (ViewGroup) findViewById(R.id.container);
		mContainer.addView(breadCrumbs(height, width));
      
      handleIntent(getIntent()); 
   } 
 
   public void onNewIntent(Intent intent) { 
      setIntent(intent); 
      handleIntent(intent); 
   } 
 
   public void onListItemClick(ListView l, 
      View v, int position, long id) { 

	  iSharedType =70; // no type showed in BreadCrumbs. just blanks in Menu Options
	   
	  sharedType = getSharedPreferences(listaTipoSat2.fileName, 0);
	  SharedPreferences.Editor editor = sharedType.edit();
	  editor.putInt("tipoSatEspecifico", iSharedType);
	  editor.commit();
	  
	  String elegido = alistaSat.get(position);
	  Context context = null;

	  // Save the name of the sat chosen in Shared Preferences
	  SharedPreferences satNamePicked = getSharedPreferences(listaSats.fileName, 0);
	  SharedPreferences.Editor editor2 = satNamePicked.edit();
	  editor2.putString("satElegido", elegido);
	  editor2.commit();

		try {
			// Prepare SAXParser
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			// Android.SAX to search into the XML the chosen sat
			satElegidoSAXHandler dataHandler = new satElegidoSAXHandler();
			xr.setContentHandler(dataHandler);
			context = this.getApplicationContext();
			dataHandler.parse(new FileInputStream(getFilesDir()
					+ File.separator + FILENAME_XML), context);
			
			//Save data of the chosen sat, in the satElegido variable type "sat"
			sat = dataHandler.getData();
			
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
 
   private void handleIntent(Intent intent) { 
      if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
         String query = 
               intent.getStringExtra(SearchManager.QUERY); 
         this.query = query;
         doSearch(query); 
      } 
   }    
 
   private void doSearch(String queryStr) { 

	   if (!isXMLCreated) { 
			isXMLCreated = true;
			sUrlTLE = loadUrlTLE(); // array with all TLE types (url)
			/////////////////////////////////////////////////////////////////////
			/////// ASYNCTASK: Download txt, Write XML, Write XSD //////////////
			String sTipoGuardado = "";
			ProgressDialog dialog = new ProgressDialog(this);
			Resources res = getResources();
			dialog.setMessage(res.getString(R.string.searchingAllTLE) + " \"" +  query +"\"");
			new MiTarea(dialog).execute(sTipoGuardado);
			/////////////////////////////////////////////////////////////////////
		} else {
			// If new searches are performed during session, this allows to search the already 
			// downloaded file, without download again txt
			// Retrieve allsats names from XML
  			alistaSat = buscarSatsEnXML();
  			
  			if (alistaSat.size()==0) {
  				mostrarAlerta(getResources().getString(R.string.searchEmpty1)
  	   					, getResources().getString(R.string.searchEmpty2)
  	   					, getResources().getString(R.string.searchEmpty3));
  			}
  			
  			// Create the list for the listView to show it on the screen  
  			setListAdapter(new ArrayAdapter<String>(searchActivity.this,
  					android.R.layout.simple_list_item_1, alistaSat));
		}
	}

	private LinearLayout breadCrumbs(float height, float width) {
		
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
		
		// BreadCrumbs button 2 (fill)
		Button  bBreadCrumbsEnd = new Button(this);
		bBreadCrumbsEnd.setBackgroundColor(getResources().getColor(R.color.BreadCrumbs2));
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT );
		params2.setMargins(0, 0, 0, 0);
		bBreadCrumbsEnd.setLayoutParams(params2);
		
		l1.addView(bBreadCrumbsEnd);
		
		// Setting onClickListener to bBreadCrumbs (home)
		bBreadCrumbs1.setOnClickListener(new Button.OnClickListener() {  
	        public void onClick(View v) {
	        	// Back Home
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
			searchListaSatsSAXHandler dataHandler = new searchListaSatsSAXHandler(query);
			xr.setContentHandler(dataHandler);
			
			FileInputStream fs=null;
			if (isXMLCreated) {
			fs = new FileInputStream(getFilesDir()
						+ File.separator + FILENAME_XML);
			dataHandler.parse(fs);
			} else {
				dataHandler.parse(fs);
			}

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

	@SuppressWarnings("null")
	private ArrayList<String> loadUrlTLE() {
		
		ArrayList<String> aux = new ArrayList<String>();
		///////////////////// Special Interest /////////////////////////////////
			aux.add(0,"http://celestrak.com/NORAD/elements/tle-new.txt");
			aux.add(1,"http://celestrak.com/NORAD/elements/stations.txt");
			aux.add(2,"http://celestrak.com/NORAD/elements/visual.txt");
		///////////////////// Weather and Earth Resources //////////////////////////////
			aux.add(3,"http://celestrak.com/NORAD/elements/weather.txt");
			aux.add(4,"http://celestrak.com/NORAD/elements/noaa.txt");
			aux.add(5,"http://celestrak.com/NORAD/elements/goes.txt");
			aux.add(6,"http://celestrak.com/NORAD/elements/resource.txt");
			aux.add(7,"http://celestrak.com/NORAD/elements/sarsat.txt");
			aux.add(8,"http://celestrak.com/NORAD/elements/dmc.txt");
			aux.add(9,"http://celestrak.com/NORAD/elements/tdrss.txt");
		///////////////////// Communications //////////////////////////////
			aux.add(10,"http://celestrak.com/NORAD/elements/geo.txt");
			aux.add(11,"http://celestrak.com/NORAD/elements/intelsat.txt");
			aux.add(12,"http://celestrak.com/NORAD/elements/gorizont.txt");
			aux.add(13,"http://celestrak.com/NORAD/elements/raduga.txt");
			aux.add(14,"http://celestrak.com/NORAD/elements/molniya.txt");
			aux.add(15,"http://celestrak.com/NORAD/elements/iridium.txt");
			aux.add(16,"http://celestrak.com/NORAD/elements/orbcomm.txt");
			aux.add(17,"http://celestrak.com/NORAD/elements/globalStar.txt");
			aux.add(18,"http://celestrak.com/NORAD/elements/amateur.txt");
			aux.add(19,"http://celestrak.com/NORAD/elements/other-comm.txt");
			aux.add(20,"http://celestrak.com/NORAD/elements/x-comm.txt");
		///////////////////// Navigation //////////////////////////////
			aux.add(21,"http://celestrak.com/NORAD/elements/gps-ops.txt");
			aux.add(22,"http://celestrak.com/NORAD/elements/glo-ops.txt");
			aux.add(23,"http://celestrak.com/NORAD/elements/galileo.txt");
			aux.add(24,"http://celestrak.com/NORAD/elements/sbas.txt");
			aux.add(25,"http://celestrak.com/NORAD/elements/nnss.txt");
			aux.add(26,"http://celestrak.com/NORAD/elements/musson.txt");
		///////////////////// Scientific //////////////////////////////
			aux.add(27,"http://celestrak.com/NORAD/elements/science.txt");
			aux.add(28,"http://celestrak.com/NORAD/elements/geodetic.txt");
			aux.add(29,"http://celestrak.com/NORAD/elements/engineering.txt");
			aux.add(30,"http://celestrak.com/NORAD/elements/education.txt");
		///////////////////// Miscellaneous //////////////////////////////
			aux.add(31,"http://celestrak.com/NORAD/elements/military.txt");
			aux.add(32,"http://celestrak.com/NORAD/elements/radar.txt");
			aux.add(33,"http://celestrak.com/NORAD/elements/cubesat.txt");
			aux.add(34,"http://celestrak.com/NORAD/elements/other.txt");
		return aux;
	}

	protected void headerXML (BufferedWriter xml) {
		
		// Head
		try {
		xml.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.newLine();
		xml.write("<TLE xmlns=\"http://www.w3schools.com\"");
		xml.newLine();
		xml.write("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		xml.newLine();
		xml.write("xsi:noNamespaceSchemaLocation=\"TLE.xsd\">");
		xml.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void footerXML (BufferedWriter xml) {
		
		String END = "</";
		// // Complex elements
		String TLE = "TLE>";
		
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
	
	protected void writeXML(BufferedWriter xml, BufferedReader in) {
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

	}

	// //////////////////////////////////////////

	private void writeXSD(BufferedWriter xsd) {
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
   		try {
   			// Read file from the internet and save it in an internal file      			
   			// Create file xml
   			File archivo_xml = new File(getFilesDir() + File.separator
   					+ FILENAME_XML);
   			xml = new BufferedWriter(new FileWriter(archivo_xml));
   			float x = 0;
   			
   			for (int i=0;i<35;i++) {
	   			// Create a URL for the desired page
	   			URL url = new URL(sUrlTLE.get(i));
	
	   			// Read all the text returned by the server
	   			in = new BufferedReader(new InputStreamReader(url.openStream()));
	   			
	   			// Check if Celestrack.com is working. If one txt is not ok, just skip it.
	   			in.mark(4);
	   			String a = in.readLine().toString();
	   			if ((a.equals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""))) {
	   				if (error_urlCelestrack) { // if more than one url from celestrack is not working, alert message
	   					m=true;
	   				} else { // if only one url is not working, continue
	   					error_urlCelestrack = true;
	   					errorURL = sUrlTLE.get(i); 
	   					continue;
	   				}
	   			} else
	   				in.reset();
	   			
	   			//Write xml file
	   			if (i==0) headerXML(xml);
	   			writeXML(xml, in);
	   			if (i==34) footerXML(xml);
	   			x = x + 95/35;
	   			publishProgress(x);
   			}
   			
   			//Write xsd file
   			File archivo_xsd = new  File(getFilesDir() + File.separator
   					+ FILENAME_XSD);
   			xsd = new BufferedWriter(new FileWriter(archivo_xsd));
   			writeXSD(xsd);
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

               // Alert message if more than 2 url from the celestrack are not available 
   		// it does not continue running the methode
   		if (error_urlCelestrack && m) {
   			mostrarAlerta(getResources().getString(R.string.errorCelestrack1)
   					, getResources().getString(R.string.errorCelestrack2)
   					, getResources().getString(R.string.errorCelestrack3));
   		} else {

   			// Retrieve all sats names from XML containing query
   			alistaSat = buscarSatsEnXML();
   			
  			if (alistaSat.size()==0) {
  				mostrarAlerta(getResources().getString(R.string.searchEmpty1)
  	   					, getResources().getString(R.string.searchEmpty2)
  	   					, getResources().getString(R.string.searchEmpty3));
  			}

   			// Create the list for the listView to show it on the screen  
   			setListAdapter(new ArrayAdapter<String>(searchActivity.this,
   					android.R.layout.simple_list_item_1, alistaSat));
   		}   
       }

	}
   
   
   
   
}
