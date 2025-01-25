package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsVarGlobal {
	
	private double xnodeo;  	// RAAN
	private double omegao;    	// ArgumentPerigee
	private double xmo; 		// MeanAnomaly
	private double xincl; 		// Inclination
	private double xno;  		// MeanMotion
	private double xndt2o;  	// MeanMotionDerivate
	private double bstar; 		// Drag Term
	private double eo; 			// Eccentricity
	
	// Constructor All values initializes to 0
	public CalculationsVarGlobal() {
		this.xnodeo = 0;
		this.omegao = 0;
		this.xmo = 0;
		this.xincl = 0;
		this.xno = 0;
		this.xndt2o = 0;
		this.bstar = 0;
		this.eo = 0;
	}
	
	public CalculationsVarGlobal( double xnodeo, double omegao,
			double xmo, double xincl, double xno, double xndt2o,
			int bstar,double eo) {

		this.xnodeo = xnodeo;
		this.omegao = omegao;
		this.xmo = xmo;
		this.xincl = xincl;
		this.xno = xno;
		this.xndt2o = xndt2o;
		this.bstar = bstar;
		this.eo = eo;
	}

	// Getter and setter methods
   public double getXnodeo() {
	   return xnodeo;
   }
   public double getOmegao() {
	   return omegao;
   }
   public double getXmo() {
	   return xmo;
   }
   public double getXincl() {
	   return xincl;
   }
   public double getXno() {
	   return xno;
   }
   public double getXndt2o() {
	   return xndt2o;
   }
   public double getBstar() {
	   return bstar;
   }
   public double getEo() {
	   return eo;
   }
   //////////////////////////////////////
   
   public void setXnodeo(double xnodeo) {
	   this.xnodeo = xnodeo;
   }
   public void setOmegao(double omegao) {
	   this.omegao =  omegao;
   }
   public void setXmo(double xmo) {
	   this.xmo = xmo;
   }
   public void setXincl(double xincl) {
	   this.xincl = xincl;
   }
   public void setXno(double xno) {
	   this.xno = xno;
   }
   public void setXndt2o(double xndt2o) {
	   this.xndt2o = xndt2o;
   }
   public void setBstar(double bstar) {
	   this.bstar = bstar;
   }
   public void setEo(double eo) {
	   this.eo = eo;
   }
	
	// Copy the data from another object to this sat object
	public void copyData(CalculationsVarGlobal s) {
		this.xnodeo = s.xnodeo;
		this.omegao = s.omegao;
		this.xmo = s.xmo;
		this.xincl = s.xincl;
		this.xno = s.xno;
		this.xndt2o = s.xndt2o;
		this.bstar = s.bstar;
		this.eo = s.eo;
	}
	
	public void showLog() {
		Log.d("xnodeo",String.valueOf(this.xnodeo));
		Log.d("omegao",String.valueOf(this.omegao));
		Log.d("xmo",String.valueOf(this.xmo));
		Log.d("xincl",String.valueOf(this.xincl));
		Log.d("xno",String.valueOf(this.xno));
		Log.d("xndt2o",String.valueOf(this.xndt2o));
		Log.d("bstar",String.valueOf(this.bstar));
		Log.d("eo",String.valueOf(this.eo));
	}
	
}
