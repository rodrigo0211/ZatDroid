package com.zatdroid.rodrigo;

import android.util.Log;

public class CalculationsOrbitSDP4_Deep {

	// Transforming Flags from original FORTRAN code into booleans
	static boolean lunar_terms_done_flag = false, do_loop_flag = false,
			resonance_flag = false, synchronous_flag = false,
			epoch_restart_flag = false;
	
	static double thgr=0, xnq=0, xqncl=0, omegaq=0, zmol = 0, zmos=0, savtsn=0, ee2 = 0, e3=0, 
		xi2=0, xl2=0, xl3=0, xl4=0, xgh2=0, xgh3=0, xgh4=0, xh2=0, xh3=0, sse = 0, ssi = 0, ssg = 0, 
		xi3=0, se2=0, si2=0, sl2=0, sgh2=0, sh2=0, se3=0, si3=0, sl3=0, sgh3=0, sh3=0, sl4=0, sgh4=0, 
		ssl = 0, ssh = 0, d3210=0, d3222=0, d4410=0, d4422=0, d5220=0, d5232=0, d5421=0, d5433=0, 
		del1=0, del2=0, del3=0, fasx2=0, fasx4=0, fasx6=0, xlamo = 0, xfact=0, xni=0, atime = 0, 
		stepp = 0, stepn=0, step2=0, preep=0, pl=0, sghs=0, xli=0, d2201=0, d2211=0, sghl=0, sh1=0,
		pinc=0, pe=0, shs=0, zsingl = 0, zcosgl = 0, zsinhl = 0, zcoshl = 0, zsinil = 0, zcosil = 0;
	// Constructor
	public CalculationsOrbitSDP4_Deep() {
	}	
	public void resetStaticVars() {
		
		lunar_terms_done_flag = false; do_loop_flag = false;
		resonance_flag = false; synchronous_flag = false;
		epoch_restart_flag = false;
		
		thgr=0; xnq=0; xqncl=0; omegaq=0; zmol = 0; zmos=0; savtsn=0; ee2 = 0; e3=0; 
		xi2=0; xl2=0; xl3=0; xl4=0; xgh2=0; xgh3=0; xgh4=0; xh2=0; xh3=0; sse = 0; ssi = 0; ssg = 0; 
		xi3=0; se2=0; si2=0; sl2=0; sgh2=0; sh2=0; se3=0; si3=0; sl3=0; sgh3=0; sh3=0; sl4=0; sgh4=0; 
		ssl = 0; ssh = 0; d3210=0; d3222=0; d4410=0; d4422=0; d5220=0; d5232=0; d5421=0; d5433=0; 
		del1=0; del2=0; del3=0; fasx2=0; fasx4=0; fasx6=0; xlamo = 0; xfact=0; xni=0; atime = 0; 
		stepp = 0; stepn=0; step2=0; preep=0; pl=0; sghs=0; xli=0; d2201=0; d2211=0; sghl=0; sh1=0;
		pinc=0; pe=0; shs=0; zsingl = 0; zcosgl = 0; zsinhl = 0; zcoshl = 0; zsinil = 0; zcosil = 0;
		
	}

	/* SDP4 routine to to add lunar and solar perturbation effects to deep-space */
	/* orbit objects (see thesis appendix A.4.2)	*/
	
	/* DEEP - SDP4
	* INPUTS:
	*	 	ientry: code to know switch-case methode to use
	*		epochJD: sat epoch in Julian Date
	*		CalculationsVarGlobal: global variables used in calculations. 
	*							   Definition in CalculationsOrbit.prepareTLEData()
	*		CalculationsVarSDP4: variables used in Deep and SPD4 at the same time.
	* OUTPUT: it changes global variables. 
	* 		  CalculationsVarSDP4: variables used in Deep and SPD4 at the same time.*/
	
	public CalculationsVarSDP4 deepCalc(int ientry, double epochJD, 
				CalculationsVarGlobal var, CalculationsVarSDP4 _varSDP4) {
	
		double a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, ainv2, aqnv, 
			sgh, sini2,  sh, si, day, bfact, c, 
			cc, cosq, ctem, f322, zx, zy, eoc, eq,  
			f220, f221, f311, f321, f330, f441, f442, f522, f523, 
			f542, f543, g200, g201, g211, s1, s2, s3, s4, s5, s6, s7,
			se,  g300, g310, g322, g410, g422, g520, g521, g532,
			g533, gam, sinq, sl, stem, temp, temp1, 
			x1, x2, x2li, x2omi, x3, x4, x5, x6, x7, x8, xl, xldot, xmao, 
			xnddt, xndot, xno2, xnodce, xnoi, xomi, xpidot, z1, z11, z12, z13, 
			z2, z21, z22, z23, z3, z31, z32, z33, ze, zmo, zn, 
			zsing, zsinh, zsini, zcosg, zcosh, zcosi, delt=0, ft=0;
	
		CalculationsTime CalcTime = new CalculationsTime();
		CalculationsVarSDP4 varSDP4 = new CalculationsVarSDP4();
		varSDP4.copyData(_varSDP4);
		
		switch (ientry) { 
	    case CalculationsVarSDP4.dpinit: /* entrance for deep space initialisation */ 
			thgr=CalcTime.ThetaG_JD(epochJD); 
			eq=var.getEo(); 
			xnq=varSDP4.getXnodp(); 
			aqnv=1/varSDP4.getAodp(); 
			xqncl=var.getXincl(); 
			xmao=var.getXmo(); 
			xpidot=varSDP4.getOmgdot()+varSDP4.getXnodot(); 
			sinq=Math.sin(var.getXnodeo()); 
			cosq=Math.cos(var.getXnodeo()); 
			omegaq=var.getOmegao();
			
			/* initialize lunar solar terms */ 
			day=varSDP4.getDs50()+18261.5; /* days since 1900 Jan 0.5 */
			
			if (day!=preep) { 
				preep=day; 
				xnodce=4.5236020-9.2422029E-4*day; 
				stem=Math.sin(xnodce); 
				ctem=Math.cos(xnodce); 
				zcosil=0.91375164-0.03568096*ctem; 
				zsinil=Math.sqrt(1-zcosil*zcosil); 
				zsinhl=0.089683511*stem/zsinil; 
				zcoshl=Math.sqrt(1-zsinhl*zsinhl);
				c=4.7199672+0.22997150*day; 
				gam=5.8351514+0.0019443680*day; 
				zmol=(c-gam) % (2*Math.PI);
				zx=0.39785416*stem/zsinil; 
				zy=zcoshl*ctem+0.91744867*zsinhl*stem;
				zx=Math.atan2(zx,zy);
				zx=gam+zx-xnodce;
				zcosgl=Math.cos(zx);
				zsingl=Math.sin(zx);
				zmos=6.2565837+0.017201977*day;
				zmos=(zmos) % (2*Math.PI);
				}
				/* do solar terms */
				savtsn=1E20;
				zcosg=k.ZCOSGS;
				zsing=k.ZSINGS;
				zcosi=k.ZCOSIS; 
				zsini=k.ZSINIS;
				zcosh=cosq;
				zsinh= sinq;
				cc=k.C1SS;
				zn=k.ZNS;
				ze=k.ZES;
				zmo=zmos;
				xnoi=1/xnq;
				/* loop breaks when Solar terms are done a second */
				/* time, after Lunar terms are initialized	*/
				for (;;) { /* solar terms done again after Lunar terms are done */ 
					a1=zcosg*zcosh+zsing*zcosi*zsinh;
					a3=-zsing*zcosh+zcosg*zcosi*zsinh;
					a7=-zcosg*zsinh+zsing*zcosi*zcosh;
					a8=zsing*zsini;
					a9=zsing*zsinh+zcosg*zcosi*zcosh;
					a10=zcosg*zsini;
					a2=varSDP4.getCosio()*a7+varSDP4.getSinio()*a8; 
					a4=varSDP4.getCosio()*a9+varSDP4.getSinio()*a10; 
					a5=-varSDP4.getSinio()*a7+varSDP4.getCosio()*a8; 
					a6=-varSDP4.getSinio()*a9+varSDP4.getCosio()*a10; 
					x1=a1*varSDP4.getCosg()+a2*varSDP4.getSing(); 
					x2=a3*varSDP4.getCosg()+a4*varSDP4.getSing(); 
					x3=-a1*varSDP4.getSing()+a2*varSDP4.getCosg(); 
					x4=-a3*varSDP4.getSing()+a4*varSDP4.getCosg(); 
					x5=a5*varSDP4.getSing(); 
					x6=a6*varSDP4.getSing(); 
					x7=a5*varSDP4.getCosg(); 
					x8=a6*varSDP4.getCosg(); 
					z31=12*x1*x1-3*x3*x3; 
					z32=24*x1*x2-6*x3*x4; 
					z33=12*x2*x2-3*x4*x4; 
					z1=3*(a1*a1+a2*a2)+z31*varSDP4.getEosq(); 
					z2=6*(a1*a3+a2*a4)+z32*varSDP4.getEosq(); 
					z3=3*(a3*a3+a4*a4)+z33*varSDP4.getEosq(); 
					z11=-6*a1*a5+varSDP4.getEosq()*(-24*x1*x7-6*x3*x5); 
					z12=-6*(a1*a6+a3*a5)+varSDP4.getEosq()*(-24*(x2*x7+x1*x8)-6*(x3*x6 +x4*x5)); 
					z13=-6*a3*a6+varSDP4.getEosq()*(-24*x2*x8-6*x4*x6); 
					z21=6*a2*a5+varSDP4.getEosq()*(24*x1*x5-6*x3*x7); 
					z22=6*(a4*a5+a2*a6)+varSDP4.getEosq()*(24*(x2*x5+x1*x6)-6*(x4*x7+ x3*x8)); 
					z23=6*a4*a6+varSDP4.getEosq()*(24*x2*x6-6*x4*x8); 
					z1=z1+z1+varSDP4.getBetao2()*z31; 
					z2=z2+z2+varSDP4.getBetao2()*z32; 
					z3=z3+z3+varSDP4.getBetao2()*z33; 
					s3=cc*xnoi; s2=-0.5*s3/varSDP4.getBetao(); 
					s4=s3*varSDP4.getBetao(); 
					s1=-15*eq*s4; 
					s5=x1*x3+x2*x4; 
					s6=x2*x3+x1*x4; 
					s7=x2*x4-x1*x3; 
					se=s1*zn*s5; 
					si=s2*zn*(z11+z13); 
					sl=-zn*s3*(z1+z3-14-6*varSDP4.getEosq()); 
					sgh=s4*zn*(z31+z33-6); 
					sh=-zn*s2*(z21+z23);
					
					if (xqncl<5.2359877E-2) sh=0;
					
					ee2=2*s1*s6; 
					e3=2*s1*s7; 
					xi2=2*s2*z12; 
					xi3=2*s2*(z13-z11); 
					xl2=-2*s3*z2; 
					xl3=-2*s3*(z3-z1); 
					xl4=-2*s3*(-21-9*varSDP4.getEosq())*ze; 
					xgh2=2*s4*z32;
					xgh3=2*s4*(z33-z31); xgh4=-18*s4*ze; 
					xh2=-2*s2*z22; xh3=-2*s2*(z23-z21);
					
					if (lunar_terms_done_flag) break;
					
					/* do lunar terms */ 
					sse=se;
					ssi=si; 
					ssl=sl; 
					ssh=sh/varSDP4.getSinio(); 
					ssg=sgh-varSDP4.getCosio()*ssh; 
					se2=ee2; 
					si2=xi2; 
					sl2=xl2; 
					sgh2=xgh2; 
					sh2=xh2; 
					se3=e3; 
					si3=xi3; 
					sl3=xl3; 
					sgh3=xgh3; 
					sh3=xh3; 
					sl4=xl4; 
					sgh4=xgh4; 
					zcosg=zcosgl; 
					zsing=zsingl; 
					zcosi=zcosil; 
					zsini=zsinil; 
					zcosh=zcoshl*cosq+zsinhl*sinq; 
					zsinh=sinq*zcoshl-cosq*zsinhl; 
					zn=k.ZNL; 
					cc=k.C1L; 
					ze=k.ZEL; 
					zmo=zmol; 
					lunar_terms_done_flag = true;
				}
				sse=sse+se; 
				ssi=ssi+si; 
				ssl=ssl+sl; 
				ssg=ssg+sgh-varSDP4.getCosio()/varSDP4.getSinio()*sh; 
				ssh=ssh+sh/varSDP4.getSinio();
				/* geopotential resonance initialisation for 12 hour orbits */ 
				//ClearFlag(RESONANCE_FLAG);
				//ClearFlag(SYNCHRONOUS_FLAG);
				resonance_flag = false;
				synchronous_flag = false;
				
				if (!((xnq<0.0052359877) && (xnq>0.0034906585))) { 
					if ((xnq<0.00826) || (xnq>0.00924)) return varSDP4;
					if (eq<0.5) return varSDP4;
					//SetFlag(RESONANCE_FLAG); 
					resonance_flag = true;
					eoc=eq*varSDP4.getEosq(); 
					g201=-0.306-(eq-0.64)*0.440;
					if (eq<=0.65) {
						g211=3.616-13.247*eq+16.290*varSDP4.getEosq(); 
						g310=-19.302+117.390*eq-228.419*varSDP4.getEosq()+156.591*eoc; 
						g322=-18.9068+109.7927*eq-214.6334*varSDP4.getEosq()+146.5816*eoc; 
						g410=-41.122+242.694*eq-471.094*varSDP4.getEosq()+313.953*eoc; 
						g422=-146.407+841.880*eq-1629.014*varSDP4.getEosq()+1083.435 *eoc; 
						g520=-532.114+3017.977*eq-5740*varSDP4.getEosq()+3708.276*eoc;
					}
					else {
						g211=-72.099+331.819*eq-508.738*varSDP4.getEosq()+266.724*eoc; 
						g310=-346.844+1582.851*eq-2415.925*varSDP4.getEosq()+1246.113*eoc; 
						g322=-342.585+1554.908*eq-2366.899*varSDP4.getEosq()+1215.972*eoc; 
						g410=-1052.797+4758.686*eq-7193.992*varSDP4.getEosq()+3651.957*eoc; 
						g422=-3581.69+16178.11*eq-24462.77*varSDP4.getEosq()+12422.52*eoc;
						if (eq<=0.715) g520=1464.74-4664.75*eq+3763.64*varSDP4.getEosq();
						else g520=-5149.66+29936.92*eq-54087.36*varSDP4.getEosq()+31324.56*eoc;
					}
					if (eq<0.7) {
						g533=-919.2277+4988.61*eq-9064.77*varSDP4.getEosq()+5542.21*eoc; 
						g521=-822.71072+4568.6173*eq-8491.4146*varSDP4.getEosq()+5337.524*eoc; 
						g532=-853.666+4690.25*eq-8624.77*varSDP4.getEosq()+5341.4*eoc;
					}
					else {
						g533=-37995.78+161616.52*eq-229838.2*varSDP4.getEosq()+109377.94*eoc; 
						g521 =-51752.104+218913.95*eq-309468.16*varSDP4.getEosq()+146349.42*eoc; 
						g532 =-40023.88+170470.89*eq-242699.48*varSDP4.getEosq()+115605.82*eoc;
					}
					sini2=varSDP4.getSinio()*varSDP4.getSinio(); 
					f220=0.75*(1+2*varSDP4.getCosio()+varSDP4.getTheta2()); 
					f221=1.5*sini2; f321=1.875*varSDP4.getSinio()*(1-2*varSDP4.getCosio()-3*varSDP4.getTheta2()); 
					f322=-1.875*varSDP4.getSinio()*(1+2*varSDP4.getCosio()-3*varSDP4.getTheta2()); 
					f441=35*sini2*f220;
					f442=39.3750*sini2*sini2; 
					f522=9.84375*varSDP4.getSinio()*(sini2*(1-2*varSDP4.getCosio()-5* varSDP4.getTheta2())+
							0.33333333*(-2+4*varSDP4.getCosio()+6*varSDP4.getTheta2()));
					f523=varSDP4.getSinio()*(4.92187512*sini2*(-2-4*varSDP4.getCosio()+10* varSDP4.getTheta2())+
							6.56250012*(1+2*varSDP4.getCosio()-3*varSDP4.getTheta2())); 
					f542=29.53125*varSDP4.getSinio()*(2-8*varSDP4.getCosio()+
							varSDP4.getTheta2()* (-12+8*varSDP4.getCosio()+10*varSDP4.getTheta2()));
					f543=29.53125*varSDP4.getSinio()*(-2-8*varSDP4.getCosio()+varSDP4.getTheta2()* 
							(12+8*varSDP4.getCosio()-10*varSDP4.getTheta2()));
					xno2=xnq*xnq; ainv2=aqnv*aqnv; 
					temp1=3*xno2*ainv2; 
					temp=temp1*k.ROOT22;
					d2201=temp*f220*g201; 
					d2211=temp*f221*g211; 
					temp1=temp1*aqnv; 
					temp=temp1*k.ROOT32; 
					d3210=temp*f321*g310; 
					d3222=temp*f322*g322;
					temp1=temp1*aqnv; 
					temp=2*temp1*k.ROOT44; 
					d4410=temp*f441*g410; 
					d4422=temp*f442*g422; 
					temp1=temp1*aqnv; 
					temp=temp1*k.ROOT52; 
					d5220=temp*f522*g520;
					d5232=temp*f523*g532; 
					temp=2*temp1*k.ROOT54; 
					d5421=temp*f542*g521; 
					d5433=temp*f543*g533; 
					xlamo=xmao+var.getXnodeo()+var.getXnodeo()-thgr-thgr; 
					bfact=varSDP4.getXmdot()+varSDP4.getXnodot()+varSDP4.getXnodot()-k.THDT-k.THDT; 
					bfact=bfact+ssl+ssh+ssh;
				}
				else { 
					//SetFlag(RESONANCE_FLAG);
					//SetFlag(SYNCHRONOUS_FLAG);
					resonance_flag = true;
					synchronous_flag = true;
					
					/* synchronous resonance terms initialisation */ 
					g200=1+varSDP4.getEosq()*(-2.5+0.8125*varSDP4.getEosq()); 
					g310=1+2*varSDP4.getEosq(); 
					g300=1+varSDP4.getEosq()*(-6+6.60937*varSDP4.getEosq()); 
					f220=0.75*(1+varSDP4.getCosio())*(1+varSDP4.getCosio());
					f311=0.9375*varSDP4.getSinio()*varSDP4.getSinio()*(1+3*varSDP4.getCosio()) 
							-0.75*(1+varSDP4.getCosio());
					f330=1+varSDP4.getCosio();
					f330=1.875*f330*f330*f330; 
					del1=3*xnq*xnq*aqnv*aqnv;
					del2=2*del1*f220*g200*k.Q22; 
					del3=3*del1*f330*g300*k.Q33*aqnv; 
					del1=del1*f311*g310*k.Q31*aqnv;
					fasx2=0.13130908; 
					fasx4=2.8843198;
					fasx6=0.37448087;
					xlamo=xmao+var.getXnodeo()+var.getOmegao()-thgr;
					bfact=varSDP4.getXmdot()+xpidot-k.THDT; 
					bfact=bfact+ssl+ssg+ssh;
				} 
				
				xfact=bfact-xnq;
				
				/* initialize integrator */ 
				xli=xlamo; 
				xni=xnq; 
				atime=0;
				stepp=720; 
				stepn=-720; 
				step2=259200;
				
				return varSDP4;
			
		case CalculationsVarSDP4.dpsec: /* entrance for deep space secular effects */ 
			varSDP4.setXll(varSDP4.getXll()+ssl*varSDP4.getT()); 
			varSDP4.setOmgadf(varSDP4.getOmgadf()+ssg*varSDP4.getT()); 
			varSDP4.setXnode(varSDP4.getXnode()+ssh*varSDP4.getT()); 
			varSDP4.setEm(var.getEo()+sse*varSDP4.getT()); 
			varSDP4.setXinc(var.getXincl()+ssi*varSDP4.getT());
			if (varSDP4.getXinc()<0) {
				varSDP4.setXinc(varSDP4.getXinc()-varSDP4.getXinc()); 
				varSDP4.setXnode(varSDP4.getXnode()+Math.PI);
				varSDP4.setOmgadf(varSDP4.getOmgadf()-Math.PI);
			}
			//if (isFlagClear(RESONANCE_FLAG)) return;
			if (!resonance_flag) return varSDP4;
			
			do {
				if ( (atime==0) || ((varSDP4.getT()>=0) && (atime<0) ) || ( (varSDP4.getT()<0) && (atime>=0) ) ) {
					/* epoch restart */ 
					if (varSDP4.getT()>=0) delt=stepp; 
					else delt=stepn;
					
					atime=0;
					xni=xnq;
					xli=xlamo;
				}
				else { 
					if (Math.abs(varSDP4.getT())>=Math.abs(atime)) { 
						if (varSDP4.getT()>0) delt=stepp; 
						else delt=stepn; 
					}
				}
				do { 
					if (Math.abs(varSDP4.getT()-atime)>=stepp) { 
						//SetFlag(DO_LOOP_FLAG);
						do_loop_flag = true; 
						//ClearFlag(EPOCH_RESTART_FLAG); 
						epoch_restart_flag = false;
					}
					else {
						ft=varSDP4.getT()-atime; 
						do_loop_flag = false; 
					}
					if (Math.abs(varSDP4.getT())<Math.abs(atime)) { 
						if (varSDP4.getT()>=0) delt=stepn;
						else delt=stepp;
						
						//SetFlag(DO_LOOP_FLAG | EPOCH_RESTART_FLAG);
						do_loop_flag = true;
						epoch_restart_flag = true;
					}
					/* dot terms calculated */ 
					if (synchronous_flag) { 
						xndot=del1*Math.sin(xli-fasx2)+del2*Math.sin(2*(xli-fasx4))+
								del3* Math.sin(3*(xli-fasx6)); 
						xnddt=del1*Math.cos(xli-fasx2)+2*del2*Math.cos(2*(xli-fasx4))+
								3*del3* Math.cos(3*(xli-fasx6));
					}
					else { 
						xomi=omegaq+varSDP4.getOmgdot()*atime;
						x2omi=xomi+xomi;
						x2li=xli+xli;
						xndot=d2201*Math.sin(x2omi+xli-k.G22)+
								d2211*Math.sin(xli-k.G22)+
								d3210 *Math.sin(xomi+xli-k.G32)+
								d3222*Math.sin(-xomi+xli-k.G32)+
								d4410* Math.sin(x2omi+x2li-k.G44)+
								d4422*Math.sin(x2li-k.G44)+
								d5220*Math.sin(xomi+xli-k.G52)+
								d5232* Math.sin(-xomi+xli-k.G52)+
								d5421*Math.sin(xomi+x2li-k.G54)+
								d5433*Math.sin(-xomi+x2li-k.G54);
						xnddt=d2201*Math.cos(x2omi+xli-k.G22)+
								d2211*Math.cos(xli-k.G22)+
								d3210*Math.cos(xomi+xli-k.G32)+ 
								d3222*Math.cos(-xomi+xli-k.G32)+
								d5220*Math.cos(xomi+xli-k.G52)+
								d5232*Math.cos(-xomi+xli-k.G52)+
								2*(d4410*Math.cos(x2omi+x2li-k.G44)+
								d4422*Math.cos(x2li-k.G44)+
								d5421*Math.cos(xomi+x2li-k.G54) 
								+d5433*Math.cos(-xomi+x2li-k.G54));
					}
					
					xldot=xni+xfact;
					xnddt=xnddt*xldot;
					
					if (do_loop_flag) {
						xli=xli+xldot*delt+xndot*step2; 
						xni=xni+xndot*delt+xnddt*step2; 
						atime=atime+delt;
					}
		//		} while (isFlagSet(DO_LOOP_FLAG) && isFlagClear(EPOCH_RESTART_FLAG));
		//	} while (isFlagSet(DO_LOOP_FLAG) && isFlagSet(EPOCH_RESTART_FLAG));
				} while (do_loop_flag && !epoch_restart_flag);
			} while (do_loop_flag && epoch_restart_flag);
			
			varSDP4.setXn(xni+xndot*ft+xnddt*ft*ft*0.5);
			xl=xli+xldot*ft+xndot*ft*ft*0.5;
			temp=-varSDP4.getXnode()+thgr+varSDP4.getT()*k.THDT;
			if (!synchronous_flag) varSDP4.setXll(xl+temp+temp);
			else varSDP4.setXll(xl-varSDP4.getOmgadf()+temp);
			return varSDP4;
			
		case CalculationsVarSDP4.dpper: /* entrance for lunar-solar periodics */ 
				
			varSDP4 = dpper(var,varSDP4);
			
			return varSDP4; 
			
		} // end switch 
		return varSDP4;
	} //end Deep

	/* This third case of switch is out of the method because 
	* it throws an error if the method is too large
	* INPUTS:		
	* 		CalculationsVarGlobal: global variables used in calculations. 
	*							   Definition in CalculationsOrbit.prepareTLEData()
	*		CalculationsVarSDP4: variables used in Deep and SPD4 at the same time.
	* OUTPUT: it changes global variables. 
	* 		CalculationsVarSDP4: variables used in Deep and SPD4 at the same time.*/
	
	public CalculationsVarSDP4 dpper(CalculationsVarGlobal var, CalculationsVarSDP4 _varSDP4) {
		
		double  alfdp, sinis, sinok, sil, betdp, dalf,
		 cosis, cosok, dbet, dls, f2, f3, xnoh, 
		 pgh, ph, sel, ses, xls,
		 sinzf, sis, sll, sls, zf, zm;
		
		CalculationsVarSDP4 varSDP4 = new CalculationsVarSDP4();
		varSDP4.copyData(_varSDP4);
		
		sinis=Math.sin(varSDP4.getXinc()); 
		cosis=Math.cos(varSDP4.getXinc());
		if (Math.abs(savtsn-varSDP4.getT())>=30) { 
			savtsn=varSDP4.getT(); 
			zm=zmos+k.ZNS*varSDP4.getT(); 
			zf=zm+2*k.ZES*Math.sin(zm);
			sinzf=Math.sin(zf); 
			f2=0.5*sinzf*sinzf-0.25; 
			f3=-0.5*sinzf*Math.cos(zf); 
			ses=se2*f2+se3*f3; 
			sis=si2*f2+si3*f3;
			sls=sl2*f2+sl3*f3+sl4*sinzf;
			sghs=sgh2*f2+sgh3*f3+sgh4*sinzf;
			shs=sh2*f2+sh3*f3;
			zm=zmol+k.ZNL*varSDP4.getT();
			zf=zm+2*k.ZEL*Math.sin(zm);
			sinzf=Math.sin(zf);
			f2=0.5*sinzf*sinzf-0.25;
			f3=-0.5*sinzf*Math.cos(zf);
			sel=ee2*f2+e3*f3;
			sil=xi2*f2+xi3*f3;
			sll=xl2*f2+xl3*f3+xl4*sinzf;
			sghl=xgh2*f2+xgh3*f3+xgh4*sinzf;
			sh1=xh2*f2+xh3*f3;
			pe=ses+sel;
			pinc=sis+sil;
			pl=sls+sll;
		}
		pgh=sghs+sghl;
		ph=shs+sh1;
		varSDP4.setXinc(varSDP4.getXinc()+pinc);
		varSDP4.setEm(varSDP4.getEm()+pe);
		
		if (xqncl>=0.2) { 
			/* apply periodics directly */
			ph=ph/varSDP4.getSinio();
			pgh=pgh-varSDP4.getCosio()*ph;
			varSDP4.setOmgadf(varSDP4.getOmgadf()+pgh);
			varSDP4.setXnode(varSDP4.getXnode()+ph);
			varSDP4.setXll(varSDP4.getXll()+pl);
		}
		else { 
			/* apply periodics with Lyddane modification */
			sinok=Math.sin(varSDP4.getXnode());
			cosok=Math.cos(varSDP4.getXnode());
			alfdp=sinis*sinok;
			betdp=sinis*cosok; 
			dalf=ph*cosok+pinc*cosis*sinok;
			dbet=-ph*sinok+pinc*cosis*cosok; 
			alfdp=alfdp+dalf;
			betdp=betdp+dbet;
			varSDP4.setXnode((varSDP4.getXnode()) % (2*Math.PI));
			xls=varSDP4.getXll()+varSDP4.getOmgadf()+cosis*varSDP4.getXnode(); 
			dls=pl+pgh-pinc*varSDP4.getXnode()*sinis;
			xls=xls+dls;
			xnoh=varSDP4.getXnode(); 
			varSDP4.setXnode(Math.atan2 (alfdp,betdp));/// AcTan atan2
			
			/* this is a patch to Lyddane modification */ 
			if (Math.abs(xnoh-varSDP4.getXnode())>Math.PI) {
				if (varSDP4.getXnode()<xnoh) varSDP4.setXnode(varSDP4.getXnode() + k.TWOPI);
				else varSDP4.setXnode(varSDP4.getXnode() - k.TWOPI);
			}
			varSDP4.setXll(varSDP4.getXll()+pl); 
			varSDP4.setOmgadf(xls-varSDP4.getXll()-Math.cos(varSDP4.getXinc())*varSDP4.getXnode()); 
		} 
		return varSDP4;
	}
	
}

	