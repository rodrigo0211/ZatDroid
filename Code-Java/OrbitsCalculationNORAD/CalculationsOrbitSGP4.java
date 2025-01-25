package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsOrbitSGP4 {

	 static double aodp, aycof, c1, c4, c5, cosio, d2=0, d3=0, d4=0, delmo, omgcof, eta, omgdot,
				sinio, xnodp, sinmo, t2cof, t3cof=0, t4cof=0, t5cof=0, x1mth2, x3thm1, x7thm1, xmcof, xmdot,
				xnodcf, xnodot, xlcof;
	
	public CalculationsOrbitSGP4() {

	}
	public void resetStaticVars() {
		aodp = 0; aycof = 0; c1=0; c4 = 0; c5 = 0; cosio = 0; d2 = 0; d3 = 0; d4 = 0; 
		delmo = 0; omgcof = 0; eta = 0; omgdot = 0; sinio = 0; xnodp = 0; sinmo = 0; 
		t2cof = 0; t3cof = 0; t4cof = 0; t5cof=0; x1mth2 = 0; x3thm1 = 0; x7thm1 = 0; 
		xmcof = 0; xmdot = 0; xnodcf = 0; xnodot = 0; xlcof = 0;
	}
   /* INPUTS:tsince is time since epoch in minutes
	*	 	sat is the data of the satellite from xml
	* OUTPUT: it returns sat object containing :
		 	sat containing ECI satellite
				position (km) r1,r2,r3
				velocity (km/s) rdot1, rdot2, rdot3 */
	
	public sat getOrbit(double tsince, sat sat, CalculationsVarGlobal var) {
	   
		double cosuk, sinuk, rfdotk, vx, vy, vz, ux, uy, uz, xmy, xmx, cosnok, sinnok, cosik,
			sinik, rdotk, xinck, xnodek, uk, rk, cos2u, sin2u, u, sinu, cosu, betal, rfdot,
			rdot, r, pl, elsq, esine, ecose, epw, cosepw, x1m5th, xhdot1, tfour, sinepw, capu,
			ayn, xlt, aynl, xll, axn, xn, beta, xl, e, a, tcube, delm, delomg, templ, tempe,
			tempa, xnode, betao, etasq, temp3, tsq, xmp, omega, xnoddf, omgadf, xmdf, a1,
			a3ovk2, ao, betao2, c1sq, c2, c3, coef, coef1, del1, delo, eeta, eosq, perigee,
			pinvsq, psisq, qoms24, s4, temp, temp1, temp2, temp4, temp5, temp6, theta2,
			theta4, tsi;
		boolean perigeeGT220; 
		int i;
		/* initialization */
		/* recover original mean motion (xnodp) and	*/
		/* semimajor axis (aodp) from input elements. */
		a1=Math.pow(k.XKE/var.getXno(),k.TOTHRD);
		cosio=Math.cos(var.getXincl());
		theta2=cosio*cosio;
		x3thm1=3*theta2-1.0;
		eosq=var.getEo()*var.getEo();
		betao2=1.0-eosq;
		betao=Math.sqrt(betao2);
		del1=1.5*k.CK2*x3thm1/(a1*a1*betao*betao2);
		ao=a1*(1.0-del1*(0.5*k.TOTHRD+del1*(1.0+134.0/81.0*del1)));
		delo=1.5*k.CK2*x3thm1/(ao*ao*betao*betao2);
		xnodp=var.getXno()/(1.0+delo);
		aodp=ao/(1.0-delo);
		
		/* for perigee less than 220 kilometers, the "simple"	
		* flag is set and the equations are truncated to linear 
		* variation in Math.sqrt a and quadratic variation in mean	
		* anomaly. Also, the c3 term, the delta omega term, and 
		* the delta m term are dropped. (see thesis appendix A.4) */
		if ((aodp*(1-var.getEo())/k.AE)<(220/k.XKMPER+k.AE))
		 perigeeGT220 = false;
		else perigeeGT220 = true;
		
		/* for perigees below 156 km, the	
		 * values of s and qoms2t are altered. */
		s4 = k.S;
		qoms24=k.QOMS2T;
		perigee=(aodp*(1-var.getEo())-k.AE)*k.XKMPER;
		if (perigee<156.0) {
			s4=perigee-78.0;
			if (perigee<=98.0) { 
				s4=20;
			}
			qoms24=Math.pow((120-s4)*k.AE/k.XKMPER,4);
			s4=s4/k.XKMPER+k.AE;
		}
		
		pinvsq=1/(aodp*aodp*betao2*betao2);
		tsi=1/(aodp-s4);
		eta=aodp*var.getEo()*tsi;
		etasq=eta*eta;
		eeta=var.getEo()*eta;
		psisq=Math.abs(1-etasq);
		coef=qoms24*Math.pow(tsi,4);
		coef1=coef/Math.pow(psisq,3.5);
		c2=coef1*xnodp*(aodp*(1+1.5*etasq+eeta*(4+etasq))+ 0.75*k.CK2*tsi/psisq*x3thm1*(8+3*etasq*(8+etasq)));
		c1=var.getBstar()*c2;
		sinio=Math.sin(var.getXincl());
		a3ovk2=-k.XJ3/k.CK2*Math.pow(k.AE,3);
		c3=coef*tsi*a3ovk2*xnodp*k.AE*sinio/var.getEo();
		x1mth2=1-theta2;
		c4=2*xnodp*coef1*aodp*betao2*(eta*(2+0.5*etasq)+var.getEo()*(0.5+2*etasq)- 
				2*k.CK2*tsi/(aodp*psisq)*(-3*x3thm1*(1-2*eeta+etasq*(1.5-0.5*eeta))+ 
				0.75*x1mth2*(2*etasq-eeta*(1+etasq))*Math.cos(2*var.getOmegao())));
		c5=2*coef1*aodp*betao2*(1+2.75*(etasq+eeta)+eeta*etasq);
		theta4=theta2*theta2;
		temp1=3*k.CK2*pinvsq*xnodp;
		temp2=temp1*k.CK2*pinvsq;
		temp3=1.25*k.CK4*pinvsq*pinvsq*xnodp;
		xmdot=xnodp+0.5*temp1*betao*x3thm1+0.0625*temp2*betao*(13-78*theta2 +137*theta4);
		x1m5th=1-5*theta2;
		omgdot=-0.5*temp1*x1m5th+0.0625*temp2*(7-114*theta2+395*theta4)+temp3* (3-36*theta2+49*theta4);
		xhdot1=-temp1*cosio; xnodot=xhdot1+(0.5*temp2*(4-19*theta2)+2*temp3*(3-7*theta2))*cosio;
		omgcof=var.getBstar()*c3*Math.cos(var.getOmegao());
		xmcof=-k.TOTHRD*coef*var.getBstar()*k.AE/eeta;
		xnodcf=3.5*betao2*xhdot1*c1;
		t2cof=1.5*c1;
		xlcof=0.125*a3ovk2*sinio*(3+5*cosio)/(1+cosio);
		aycof=0.25*a3ovk2*sinio;
		delmo=Math.pow(1+eta*Math.cos(var.getXmo()),3);
		sinmo=Math.sin(var.getXmo());
		x7thm1=7*theta2-1;
		
		//Only for orbits Perigee > 220 Km
		if (perigeeGT220) {  
			c1sq=c1*c1;
			d2=4*aodp*tsi*c1sq;
			temp=d2*tsi*c1/3;
			d3=(17*aodp+s4)*temp;
			d4=0.5*temp*aodp*tsi*(221*aodp+31*s4)*c1;
			t3cof=d2+2*c1sq;
			t4cof=0.25*(3*d3+c1*(12*d2+10*c1sq));
			t5cof=0.2*(3*d4+12*c1*d3+6*d2*d2+15*c1sq*(2*d2+c1sq));
		}
		
		/* update for secular gravity and atmospheric drag. */
		xmdf=var.getXmo()+xmdot*tsince;
		omgadf=var.getOmegao()+omgdot*tsince;
		xnoddf=var.getXnodeo()+xnodot*tsince;
		omega=omgadf;
		xmp=xmdf;
		tsq=tsince*tsince;
		xnode=xnoddf+xnodcf*tsq;
		tempa=1-c1*tsince;
		tempe=var.getBstar()*c4*tsince;
		templ=t2cof*tsq;
		
		if (perigeeGT220) {
			delomg=omgcof*tsince;
			delm=xmcof*(Math.pow(1+eta*Math.cos(xmdf),3)-delmo);
			temp=delomg+delm;
			xmp=xmdf+temp;
			omega=omgadf-temp;
			tcube=tsq*tsince;
			tfour=tsince*tcube;
			tempa=tempa-d2*tsq-d3*tcube-d4*tfour;
			tempe=tempe+var.getBstar()*c5*(Math.sin(xmp)-sinmo);
			templ=templ+t3cof*tcube+tfour*(t4cof+tsince*t5cof);
		}
		
		a=aodp*Math.pow(tempa,2);
		e=var.getEo()-tempe;
		xl=xmp+omega+xnode+xnodp*templ;
		beta=Math.sqrt(1-e*e);
		xn=k.XKE/Math.pow(a,1.5);
		
		/* long period periodics */
		axn=e*Math.cos(omega);
		temp=1/(a*beta*beta);
		xll=temp*xlcof*axn;
		aynl=temp*aycof;
		xlt=xl+xll;
		ayn=e*Math.sin(omega)+aynl;
		
		/* solve Kepler’s equation (numerical) */
		capu=(xlt-xnode) % (2 * Math.PI);
		temp2=capu; 
		temp4 = 0;
	    temp5 = 0;
	    temp6 = 0;

	    cosepw = 0;
	    sinepw = 0;
	    
		for (i = 1;  i <= 10;  i++){
			sinepw=Math.sin(temp2);
			cosepw=Math.cos(temp2);
			temp3=axn*sinepw;
			temp4=ayn*cosepw;
			temp5=axn*cosepw;
			temp6=ayn*sinepw;
			epw=(capu-temp4+temp3-temp2)/(1-temp5-temp6)+temp2;
			if (Math.abs(epw-temp2)<= k.E6A) break;///// ???????
			temp2=epw;
		} 
		
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
		u=Math.atan2(sinu,cosu);
		sin2u=2*sinu*cosu;
		cos2u=2*cosu*cosu-1;
		temp=1/pl;
		temp1=k.CK2*temp;
		temp2=temp1*temp;
		
		/* update for short periodics */
		rk=r*(1-1.5*temp2*betal*x3thm1)+0.5*temp1*x1mth2*cos2u;
		uk=u-0.25*temp2*x7thm1*sin2u;
		xnodek=xnode+1.5*temp2*cosio*sin2u;
		xinck=var.getXincl()+1.5*temp2*cosio*sinio*cos2u;
		rdotk=rdot-xn*temp1*x1mth2*sin2u;
		rfdotk=rfdot+xn*temp1*(x1mth2*cos2u+1.5*x3thm1);
		
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
		
		return sat2;
	}
	
}
	