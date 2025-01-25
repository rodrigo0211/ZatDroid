package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsVarSDP4 {
	
	public final static int dpinit = 1;  // Deep-space initialization code 
	public final static int dpsec  = 2;  // Deep-space secular code	
	public final static int dpper  = 3;  // Deep-space periodic code 
 	/* Used by dpinit part of DEEP */
	private double eosq;  	
	private double sinio;    	
	private double cosio; 		
	private double betao; 		
	private double aodp;  		
	private double theta2;  
	private double sing; 		
	private double cosg; 	
	private double betao2; 		
	private double xmdot;  		
	private double omgdot;  
	private double xnodot; 		
	private double xnodp;
	/* Used by dpsec and dpper parts of DEEP */
	private double xll; 		
	private double omgadf;  		
	private double xnode;  
	private double em; 		
	private double xinc;
	private double xn;  
	private double t; 	
	/* Used by thetg and DEEP */
	private double ds50;
	
	// Constructor All values initializes to 0
	public CalculationsVarSDP4() {
		/* Used by dpinit part of DEEP */
		eosq   = 0;  	
		sinio  = 0;    	
		cosio  = 0; 		
		betao  = 0; 		
		aodp   = 0;  		
		theta2 = 0;  
		sing   = 0; 		
		cosg   = 0; 	
		betao2 = 0; 		
		xmdot  = 0;  		
		omgdot = 0;  
		xnodot = 0; 		
		xnodp  = 0;
		/* Used by dpsec and dpper parts of DEEP */
		xll    = 0; 		
		omgadf = 0;  		
		xnode  = 0;  
		em     = 0; 		
		xinc   = 0;
		xn     = 0;  
		t      = 0;
		/* Used by thetg and DEEP */
		ds50   = 0;
	}

	// Getter and setter methods
 	/* Used by dpinit part of DEEP */
   public double getEosq() {
	   return eosq;
   }
   public double getSinio() {
	   return sinio;
   }
   public double getCosio() {
	   return cosio;
   }
   public double getBetao() {
	   return betao;
   }
   public double getAodp() {
	   return aodp;
   }
   public double getTheta2() {
	   return theta2;
   }
   public double getSing() {
	   return sing;
   }
   public double getCosg() {
	   return cosg;
   }
   public double getBetao2() {
	   return betao2;
   }
   public double getXmdot() {
	   return xmdot;
   }
   public double getOmgdot() {
	   return omgdot;
   }
   public double getXnodot() {
	   return xnodot;
   }
   public double getXnodp() {
	   return xnodp;
   }
	/* Used by dpsec and dpper parts of DEEP */
   public double getXll() {
	   return xll;
   }
   public double getOmgadf() {
	   return omgadf;
   }
   public double getXnode() {
	   return xnode;
   }
   public double getEm() {
	   return em;
   }
   public double getXinc() {
	   return xinc;
   }
   public double getXn() {
	   return xn;
   }
   public double getT() {
	   return t;
   }
	/* Used by thetg and DEEP */
   public double getDs50() {
	   return ds50;
   }
   /* ***************************************** */
	/* Used by dpinit part of DEEP */
   public void setEosq(double eosq) {
	   this.eosq = eosq;
   }
   public void setSinio(double sinio) {
	   this.sinio =  sinio;
   }
   public void setCosio(double cosio) {
	   this.cosio = cosio;
   }
   public void setBetao(double betao) {
	   this.betao = betao;
   }
   public void setAodp(double aodp) {
	   this.aodp = aodp;
   }
   public void setTheta2(double theta2) {
	   this.theta2 = theta2;
   }
   public void setSing(double sing) {
	   this.sing = sing;
   }
   public void setCosg(double cosg) {
	   this.cosg = cosg;
   }
   public void setBetao2(double betao2) {
	   this.betao2 =  betao2;
   }
   public void setXmdot(double xmdot) {
	   this.xmdot = xmdot;
   }
   public void setOmgdot(double omgdot) {
	   this.omgdot = omgdot;
   }
   public void setXnodot(double xnodot) {
	   this.xnodot = xnodot;
   }
   public void setXnodp(double xnodp) {
	   this.xnodp = xnodp;
   }
	/* Used by dpsec and dpper parts of DEEP */
   public void setXll(double xll) {
	   this.xll = xll;
   }
   public void setOmgadf(double omgadf) {
	   this.omgadf = omgadf;
   }
   public void setXnode(double xnode) {
	   this.xnode = xnode;
   }
   public void setEm(double em) {
	   this.em = em;
   }
   public void setXinc(double xinc) {
	   this.xinc = xinc;
   }
   public void setXn(double xn) {
	   this.xn = xn;
   }
   public void setT(double t) {
	   this.t = t;
   }
	/* Used by thetg and DEEP */
   public void setDs50(double ds50) {
	   this.ds50 = ds50;
   }
	
	// Copy the data from another object to this sat object
	public void copyData(CalculationsVarSDP4 s) {
	 	/* Used by dpinit part of DEEP */
		this.eosq = s.eosq;
		this.sinio = s.sinio;
		this.cosio = s.cosio;
		this.betao = s.betao;
		this.aodp = s.aodp;
		this.theta2 = s.theta2;
		this.sing = s.sing;
		this.cosg = s.cosg;
		this.betao2 = s.betao2;
		this.xmdot = s.xmdot;
		this.omgdot = s.omgdot;
		this.xnodot = s.xnodot;
		this.xnodp = s.xnodp;
		/* Used by dpsec and dpper parts of DEEP */
		this.xll = s.xll;
		this.omgadf = s.omgadf;
		this.xnode = s.xnode;
		this.em = s.em;
		this.xinc = s.xinc;
		this.xn = s.xn;
		this.t = s.t;
		/* Used by thetg and DEEP */
		this.ds50 = s.ds50;
	}
	
	public void showLog() {
	 	/* Used by dpinit part of DEEP */
		Log.d("eosq",String.valueOf(this.eosq));
		Log.d("sinio",String.valueOf(this.sinio));
		Log.d("cosio",String.valueOf(this.cosio));
		Log.d("betao",String.valueOf(this.betao));
		Log.d("aodp",String.valueOf(this.aodp));
		Log.d("theta2",String.valueOf(this.theta2));
		Log.d("sing",String.valueOf(this.sing));
		Log.d("cosg",String.valueOf(this.cosg));
		Log.d("betao2",String.valueOf(this.betao2));
		Log.d("xmdot",String.valueOf(this.xmdot));
		Log.d("omgdot",String.valueOf(this.omgdot));
		Log.d("xnodot",String.valueOf(this.xnodot));
		Log.d("xnodp",String.valueOf(this.xnodp));
		/* Used by dpsec and dpper parts of DEEP */
		Log.d("xll",String.valueOf(this.xll));
		Log.d("omgadf",String.valueOf(this.omgadf));
		Log.d("xnode",String.valueOf(this.xnode));
		Log.d("em",String.valueOf(this.em));
		Log.d("xinc",String.valueOf(this.xinc));
		Log.d("xn",String.valueOf(this.xn));
		Log.d("t",String.valueOf(this.t));
		/* Used by thetg and DEEP */
		Log.d("ds50",String.valueOf(this.ds50));
	}
}
