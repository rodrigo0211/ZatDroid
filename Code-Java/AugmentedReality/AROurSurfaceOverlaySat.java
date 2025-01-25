package com.zatdroid.rodrigo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

public class AROurSurfaceOverlaySat extends GLSurfaceView {

	public AROurSurfaceOverlaySat(Context context, ARGLRenderOverlayMovSat _ARGLRenderOverlayMovSat) {
		super(context);
		
		/* If it is executed with a xml layout, a second atribute is needed in th constructor
		 * public ourSurface(Context context, AttributeSet attributeSet) {
		 * super(context, attributeSet); */
		
		// To make the surface transparent and be able to add it to another one without covering it.
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSPARENT);  //TRANSLUCENT same as TRANSPARENT
		setRenderer(_ARGLRenderOverlayMovSat);
		setZOrderOnTop(true);   // Without this line, the first time that it is executed this surfaceview is not shown
	}
}