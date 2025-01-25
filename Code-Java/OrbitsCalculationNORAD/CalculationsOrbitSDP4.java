package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsOrbitSDP4 {
	
	static double  x3thm1, c1, x1mth2, c4, xnodcf, t2cof, xlcof, aycof, x7thm1;
	
	// Constructor
	public CalculationsOrbitSDP4() {
	}
	
	public void resetStaticVars() {
		x3thm1 = 0; c1 = 0; x1mth2 = 0; c4 = 0; xnodcf = 0; t2cof = 0; 
		xlcof = 0; aycof = 0; x7thm1 = 0;
	}
	
	/* ********* getOrbit(double tsince, sat sat) SDP 4 implementation ***
	* INPUTS:tsince is time since epoch in minutes
	*	 	sat is the data of the satellite from xml
	*		CalculationsVarGlobal: global variables used in calculations. 
	*							   Definition in CalculationsOrbit.prepareTLEData()
	* OUTPUT: it returns sat object containing :
		 	sat containing ECI satellite
				position (km) r1,r2,r3
				velocity (km/s) rdot1, rdot2, rdot3 */

	public sat getOrbit(double tsince, sat sat, CalculationsVarGlobal var ) { 

	int i;
	
	double a, axn, ayn, aynl, beta, betal, capu, cos2u, cosepw, cosik,
	       cosnok, cosu, cosuk, ecose, elsq, epw, esine, pl, theta4, 
		   rdot, rdotk, rfdot, rfdotk, rk, sin2u, sinepw, sinik, sinnok, 
		   sinu, sinuk, tempe, templ, tsq, u, uk, ux, uy, uz, vx, vy, vz, 
		   xinck, xl, xlt, xmam, xmdf, xmx, xmy, xnoddf, xnodek, xll, a1, 
		   a3ovk2, ao, c2, coef, coef1, x1m5th, xhdot1, del1, r, delo, 
		   eeta, eta, etasq, perigee, psisq, tsi, qoms24, s4, pinvsq, 
		   temp, tempa, temp1, temp2, temp3, temp4, temp5, temp6;
	double epochJD=0;
	CalculationsVarSDP4 varSDP4 = new CalculationsVarSDP4();
	CalculationsOrbitSDP4_Deep deep = new CalculationsOrbitSDP4_Deep();

	
	//if (isFlagClear(SDP4_INITIALIZED_FLAG)) { SetFlag(SDP4_INITIALIZED_FLAG);
	/* recover original mean motion (xnodp) and	*
	 * semimajor axis (aodp) from input elements. */

	a1 = Math.pow(k.XKE/var.getXno(),k.TOTHRD);
	varSDP4.setCosio(Math.cos(var.getXincl()));
	varSDP4.setTheta2(varSDP4.getCosio()*varSDP4.getCosio());
	x3thm1=3*varSDP4.getTheta2()-1;
	varSDP4.setEosq(var.getEo()*var.getEo());
	varSDP4.setBetao2(1-varSDP4.getEosq());
	varSDP4.setBetao(Math.sqrt(varSDP4.getBetao2()));
	del1=1.5*k.CK2*x3thm1/(a1*a1*varSDP4.getBetao()*varSDP4.getBetao2());
	ao=a1*(1-del1*(0.5*k.TOTHRD+del1*(1+134/81*del1)));
	delo=1.5*k.CK2*x3thm1/(ao*ao*varSDP4.getBetao()*varSDP4.getBetao2());
	varSDP4.setXnodp(var.getXno()/(1+delo));
	varSDP4.setAodp(ao/(1-delo));
	/* for perigee below 156 km, the values 
	 * of s and qoms2t are altered.	*/
	s4=k.S;
	qoms24=k.QOMS2T;
	perigee=(varSDP4.getAodp()*(1-var.getEo())-k.AE)*k.XKMPER;
	if (perigee<156.0) {
		if (perigee<=98.0) s4=20.0;
		else s4=perigee-78.0;
		qoms24=Math.pow((120-s4)*k.AE/k.XKMPER,4);
		s4=s4/k.XKMPER+k.AE;
	}
	pinvsq=1/(varSDP4.getAodp()*varSDP4.getAodp()*varSDP4.getBetao2()*varSDP4.getBetao2());
	varSDP4.setSing(Math.sin(var.getOmegao()));
	varSDP4.setCosg(Math.cos(var.getOmegao()));
	tsi=1/(varSDP4.getAodp()-s4);
	eta=varSDP4.getAodp()*var.getEo()*tsi; 
	etasq=eta*eta; 
	eeta=var.getEo()*eta; 
	psisq=Math.abs(1-etasq); 
	coef=qoms24*Math.pow(tsi,4); 
	coef1=coef/Math.pow(psisq,3.5); 
	c2=coef1*varSDP4.getXnodp()*(varSDP4.getAodp()*(1+1.5*etasq+eeta*(4+etasq))+
			0.75*k.CK2* tsi/psisq*x3thm1*(8+3*etasq*(8+etasq)));
	c1=var.getBstar()*c2; 
	varSDP4.setSinio(Math.sin(var.getXincl())); 
	a3ovk2=-k.XJ3/k.CK2*Math.pow(k.AE,3); 
	x1mth2=1-varSDP4.getTheta2(); 
	c4=2*varSDP4.getXnodp()*coef1*varSDP4.getAodp()*varSDP4.getBetao2()*(eta*(2+0.5*etasq)+ 
			var.getEo()*(0.5+2*etasq)-
			2*k.CK2*tsi/(varSDP4.getAodp()*psisq)*(-3*x3thm1*(1-2*eeta+etasq* (1.5-0.5*eeta))+
			0.75*x1mth2*(2*etasq-eeta*(1+etasq))*Math.cos(2*var.getOmegao()))); 
	theta4=varSDP4.getTheta2()*varSDP4.getTheta2();
	temp1=3*k.CK2*pinvsq*varSDP4.getXnodp(); 
	temp2=temp1*k.CK2*pinvsq;
	temp3=1.25*k.CK4*pinvsq*pinvsq*varSDP4.getXnodp(); 
	varSDP4.setXmdot(varSDP4.getXnodp()+0.5*temp1*varSDP4.getBetao()*x3thm1+
			0.0625*temp2* varSDP4.getBetao()*(13-78*varSDP4.getTheta2()+137*theta4)); 
	x1m5th=1-5*varSDP4.getTheta2(); 
	varSDP4.setOmgdot(-0.5*temp1*x1m5th+0.0625*temp2*(7-114*varSDP4.getTheta2()+395*theta4)+ 
			temp3*(3-36*varSDP4.getTheta2()+49*theta4));
	xhdot1=-temp1*varSDP4.getCosio(); 
	varSDP4.setXnodot(xhdot1+(0.5*temp2*(4-19*varSDP4.getTheta2())+
			2*temp3*(3-7*varSDP4.getTheta2()))* varSDP4.getCosio()); 
	xnodcf=3.5*varSDP4.getBetao2()*xhdot1*c1; 
	t2cof=1.5*c1; 
	xlcof=0.125*a3ovk2*varSDP4.getSinio()*(3+5*varSDP4.getCosio())/(1+varSDP4.getCosio()); 
	aycof=0.25*a3ovk2*varSDP4.getSinio(); 
	x7thm1=7*varSDP4.getTheta2()-1;
	
	/* initialise DEEP */
	epochJD = sat.getEpochJD();
	/* Deep-space initialization code */
	varSDP4 = deep.deepCalc(CalculationsVarSDP4.dpinit, epochJD, var, varSDP4);

	
	/* update for secular gravity and atmospheric drag */ 
	xmdf=var.getXmo()+varSDP4.getXmdot()*tsince; 
	varSDP4.setOmgadf(var.getOmegao()+varSDP4.getOmgdot()*tsince); 
	xnoddf=var.getXnodeo()+varSDP4.getXnodot()*tsince;
	tsq=tsince*tsince;
	varSDP4.setXnode(xnoddf+xnodcf*tsq); 
	tempa=1-c1*tsince; 
	tempe=var.getBstar()*c4*tsince; 
	templ=t2cof*tsq; 
	varSDP4.setXn(varSDP4.getXnodp());
	/* update for deep-space secular effects */ 
	varSDP4.setXll(xmdf);
	varSDP4.setT(tsince);
	
	/* Deep-space secular code	*/
	varSDP4 = deep.deepCalc(CalculationsVarSDP4.dpsec, epochJD, var, varSDP4);
	
	xmdf=varSDP4.getXll(); 
	a=Math.pow(k.XKE/varSDP4.getXn(),k.TOTHRD)*tempa*tempa; 
	varSDP4.setEm(varSDP4.getEm()-tempe); 
	xmam=xmdf+varSDP4.getXnodp()*templ;
	/* update for deep-space periodic effects */
	varSDP4.setXll(xmam);
	
	/* Deep-space periodic code	*/
	varSDP4 = deep.deepCalc(CalculationsVarSDP4.dpper, epochJD, var, varSDP4);
	
	xmam=varSDP4.getXll(); 
	xl=xmam+varSDP4.getOmgadf()+varSDP4.getXnode(); 
	beta=Math.sqrt(1-varSDP4.getEm()*varSDP4.getEm());
	varSDP4.setXn(k.XKE/Math.pow(a,1.5));
	
	/* long period periodics */
	axn=varSDP4.getEm()*Math.cos(varSDP4.getOmgadf());
	temp=1/(a*beta*beta); 
	xll=(temp*xlcof*axn);
	aynl=temp*aycof; 
	xlt=xl+xll; 
	ayn=varSDP4.getEm()*Math.sin(varSDP4.getOmgadf())+aynl;
	
	/* solve KeplerÕs equation */ 
	capu=(xlt-varSDP4.getXnode()) % (2*Math.PI); 
	temp2=capu;
	i=0;
	do { 
		sinepw=Math.sin(temp2); 
		cosepw=Math.cos(temp2); 
		temp3=axn*sinepw; 
		temp4=ayn*cosepw; 
		temp5=axn*cosepw; 
		temp6=ayn*sinepw; 
		epw=(capu-temp4+temp3-temp2)/(1-temp5-temp6)+temp2;
	if (Math.abs(epw-temp2)<=k.E6A) break;
	temp2=epw;
	} while (i++<10);
	/* short period preliminary quantities */ 
	ecose=temp5+temp6; 
	esine=temp3-temp4; 
	elsq=axn*axn+ayn*ayn;
	temp=1-elsq; 
	pl=a*temp; 
	r=a*(1-ecose); 
	temp1=1/r; 
	rdot=k.XKE*Math.sqrt(a)*esine*temp1; 
	rfdot=k.XKE*Math.sqrt(pl)*temp1; 
	temp2=a*temp1; 
	betal=Math.sqrt(temp); 
	temp3=1/(1+betal); 
	cosu=temp2*(cosepw-axn+ayn*esine*temp3); 
	sinu=temp2*(sinepw-ayn-axn*esine*temp3); 
	u=Math.atan2 (sinu,cosu);/// AcTan = Java.atan2
	sin2u=2*sinu*cosu; 
	cos2u=2*cosu*cosu-1; 
	temp=1/pl; 
	temp1=k.CK2*temp; 
	temp2=temp1*temp;
	/* update for short periodics */ 
	rk=r*(1-1.5*temp2*betal*x3thm1)+0.5*temp1*x1mth2*cos2u; 
	uk=u-0.25*temp2*x7thm1*sin2u; 
	xnodek=varSDP4.getXnode()+1.5*temp2*varSDP4.getCosio()*sin2u; 
	xinck=varSDP4.getXinc()+1.5*temp2*varSDP4.getCosio()*varSDP4.getSinio()*cos2u; 
	rdotk=rdot-varSDP4.getXn()*temp1*x1mth2*sin2u; 
	rfdotk=rfdot+varSDP4.getXn()*temp1*(x1mth2*cos2u+1.5*x3thm1);
	/* orientation vectors */ 
	sinuk=Math.sin(uk); 
	cosuk=Math.cos(uk); 
	sinik=Math.sin(xinck); 
	cosik=Math.cos(xinck); 
	sinnok=Math.sin(xnodek); 
	cosnok=Math.cos(xnodek); 
	xmx=-sinnok*cosik; 
	xmy=cosnok*cosik; 
	ux=xmx*sinuk+cosnok*cosuk; 
	uy=xmy*sinuk+sinnok*cosuk; 
	uz=sinik*sinuk; 
	vx=xmx*cosuk-cosnok*sinuk; 
	vy=xmy*cosuk-sinnok*sinuk; 
	vz=sinik*cosuk;
	
	sat sat2 = new sat();
	sat2.copyData(sat);
	/* position and velocity */ 
	sat2.setR1(rk*ux);
	sat2.setR2(rk*uy);
	sat2.setR3(rk*uz);
	
	sat2.setRdot1(rdotk*ux+rfdotk*vx);
	sat2.setRdot2(rdotk*uy+rfdotk*vy);
	sat2.setRdot3(rdotk*uz+rfdotk*vz);

	//scale r and rdot
	sat2.setR1(k.XKMPER*sat2.getR1()); //in km
	sat2.setR2(k.XKMPER*sat2.getR2());
	sat2.setR3(k.XKMPER*sat2.getR3());
	
	sat2.setRdot1((k.XKMPER*k.XMNPDA/k.SECDAY)*sat2.getRdot1());  //in Km/s
	sat2.setRdot2((k.XKMPER*k.XMNPDA/k.SECDAY)*sat2.getRdot2());
	sat2.setRdot3((k.XKMPER*k.XMNPDA/k.SECDAY)*sat2.getRdot3());
	
	// Reset static variables for next calculation
	deep.resetStaticVars();
	
	return sat2;
}

}
	