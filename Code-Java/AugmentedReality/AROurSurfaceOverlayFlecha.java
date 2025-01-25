package com.zatdroid.rodrigo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

public class AROurSurfaceOverlayFlecha extends GLSurfaceView {

	public AROurSurfaceOverlayFlecha(Context context, ARGLRenderOverlayMovFlecha _ARGLRenderOverlayMovFlecha) {
		super(context);
		// Si se ejecuta con un layout en xml, es necesario el segundo atributo
		// en el constructor
		// public ourSurface(Context context, AttributeSet attributeSet) {
		// super(context, attributeSet);
		
		//Para hacer la superficie transparente y poder ponerla sobre otra si taparla
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSPARENT);  //TRANSLUCENT igual que TRANSPARENT
		setRenderer(_ARGLRenderOverlayMovFlecha);
		setZOrderOnTop(true);   //Si lo quito, la primera vez que se ejecuta no se ve esta surfaceView
	}
}