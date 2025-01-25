package com.zatdroid.rodrigo;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.content.Context;
import android.content.SharedPreferences;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class satElegidoSAXHandler extends DefaultHandler {

	SharedPreferences tipoGuardado;
	String satElegido;
	boolean s; // switch the sat chosen between the one saved in SharedPreferences and the one passed as parameter
	
	private sat datosSatElegido;

	public sat getData() {
		return datosSatElegido;
	}
	
	public satElegidoSAXHandler(String satElegido){
		this.satElegido =  satElegido;
		s=true;
	}
	public satElegidoSAXHandler(){
		s=false;
	}

	public void parse(InputStream is, Context context ) {
		
		String NAMESPACE = "http://www.w3schools.com";

		if (!s) {
			// Retrieve the sat type chosen in Preferences
			// To use getSharedPreferences Context must be passed from the activity to here
			tipoGuardado = context.getSharedPreferences(listaTipoSat.fileName, 0);
			satElegido = tipoGuardado.getString("satElegido",
					"No se pudo cargar el dato");
		}

		RootElement root = new RootElement(NAMESPACE, "TLE");

		Element E_sat = root.getChild(NAMESPACE, "sat");
		
		Element E_info = E_sat.getChild(NAMESPACE, "info");
		Element E_name = E_info.getChild(NAMESPACE, "name");
		Element E_number = E_info.getChild(NAMESPACE, "number");
		
		Element E_keplerianElements = E_sat.getChild(NAMESPACE,"keplerianElements");
		Element E_inclination = E_keplerianElements.getChild(NAMESPACE,"inclination");
		Element E_raan = E_keplerianElements.getChild(NAMESPACE,"raan");
		Element E_eccentricity = E_keplerianElements.getChild(NAMESPACE,"eccentricity");		
		Element E_argumentPerigee = E_keplerianElements.getChild(NAMESPACE,"argumentPerigee");
		Element E_meanAnomaly = E_keplerianElements.getChild(NAMESPACE,"meanAnomaly");
		Element E_meanMotion = E_keplerianElements.getChild(NAMESPACE,"meanMotion");
		Element E_epochYear = E_keplerianElements.getChild(NAMESPACE,"epochYear");
		Element E_epoch = E_keplerianElements.getChild(NAMESPACE,"epoch");
			
		Element E_otherParameters = E_sat.getChild(NAMESPACE, "otherParameters");
		Element E_meanMotionDerivate = E_otherParameters.getChild(NAMESPACE, "meanMotionDerivate");
		Element E_bstarDragTerm = E_otherParameters.getChild(NAMESPACE, "bstarDragTerm");
		Element E_ephemeridesType = E_otherParameters.getChild(NAMESPACE, "ephemeridesType");
		
		// create the structure, save each component and finally check it. 
		// If it is not what we were searching, leave it
		
		datosSatElegido = new sat();
		datosSatElegido.setName("..........");
		datosSatElegido.setEncontrado(0);
		
		E_name.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
					String aux = body;
					if ((aux.equals(satElegido)) && datosSatElegido.getEncontrado() == 0) {
						datosSatElegido.setName(aux);
						datosSatElegido.setEncontrado(1);
					}
		}
		});
		E_number.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setNumber(body);
				}
		}
		});
		E_inclination.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setInclination(Double.parseDouble(body));
				}
		}
		});
		E_raan.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setRaan(Double.parseDouble(body));
				}
		}
		});	
		E_eccentricity.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setEccentricity(Double.parseDouble(body));
				}
		}
		});	
		E_argumentPerigee.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setArgumentPerigee(Double.parseDouble(body));
				}
		}
		});	
		E_meanAnomaly.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1){
					datosSatElegido.setMeanAnomaly(Double.parseDouble(body));
				}
		}
		});	
		E_meanMotion.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setMeanMotion(Double.parseDouble(body));
				}
		}
		});	
		E_epochYear.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setEpochYear(Integer.parseInt(body));
				}
		}
		});	
		E_epoch.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setEpoch(Double.parseDouble(body));
				}
		}
		});	
		E_meanMotionDerivate.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setMeanMotionDerivate(Double.parseDouble(body));
				}
		}
		});	
		E_bstarDragTerm.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setBstarDragTerm(Double.parseDouble(body));
				}
		}
		});	
		E_ephemeridesType.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { 
				if (datosSatElegido.getEncontrado() == 1) {
					datosSatElegido.setEphemeridesType(Double.parseDouble(body));
					datosSatElegido.setEncontrado(0);
				}
		}
		});			
		
		try {
			Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());				
		} catch (SAXException e) {
			Log.e("SAX XML", "sax xml.parse ", e);
		} catch (IOException io) {
			// TODO Auto-generated catch block
			Log.e("SAX XML", "sax parse io error", io);
		} catch (Exception io) {
			// TODO Auto-generated catch block
			Log.e("SAX XML", "sax parse error", io);
		}
	}
}
