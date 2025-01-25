package com.zatdroid.rodrigo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class searchListaSatsSAXHandler extends DefaultHandler {

	private ArrayList<String> ListaNombres = new ArrayList<String>();
	private String searchString;

	public ArrayList<String> getData() {
		return ListaNombres;
	}

	public searchListaSatsSAXHandler (String searchString){
		this.searchString = searchString;
	}
	// Parse the XML with all txt TLEs and retrieve names of all sats that fits the query.
	public void parse(InputStream is) {

		String NAMESPACE = "http://www.w3schools.com";
		RootElement root = new RootElement(NAMESPACE, "TLE");

		// Just Name is read to show in the list
		Element E_sat = root.getChild(NAMESPACE, "sat");
		Element E_info = E_sat.getChild(NAMESPACE, "info");
		Element E_name = E_info.getChild(NAMESPACE, "name");
		
		E_name.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) { // body es la informacion de la
											// etiqueta
				if (body.contains(searchString.toUpperCase())) {
				ListaNombres.add(body);
				}
			}
		});

	
		try {
			Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (SAXException e) {
			Log.e("SAX XML", "sax xml.parse ", e);
			;
		} catch (IOException io) {
			// TODO Auto-generated catch block
			Log.e("SAX XML", "sax parse io error", io);
		} catch (Exception io) {
			// TODO Auto-generated catch block
			Log.e("SAX XML", io.toString());
		}
	}
}
