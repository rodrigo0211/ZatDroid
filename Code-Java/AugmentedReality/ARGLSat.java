package com.zatdroid.rodrigo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class ARGLSat {
	private FloatBuffer vertBuff;
	private FloatBuffer texBuffer; // buffer holding the texture coordinates
	private short[] pIndex = { 0,1,2,3 };

	private ShortBuffer pBuff;

	private int[] textureIDs = new int[1];

	public void loadGLTexture(GL10 gl, Context context) {
		
		gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]); // Bind to texture
																// ID
		// Set up texture filters
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// Construct an input stream to texture image "res\drawable\icono.png"
		InputStream istream = context.getResources().openRawResource(
				R.drawable.sat2);
		Bitmap bitmap;
		try {
			// Read and decode input as bitmap
			bitmap = BitmapFactory.decodeStream(istream);
		} finally {
			try {
				istream.close();
			} catch (IOException e) {
			}
		}

		// Build Texture from loaded bitmap for the currently-bind texture ID
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}


	public void Update (float[] sat, float[] vVertical) {

		float tam = 1.5f; //icon size
		/* sat[0] is always 0, represented in both vector bases, because is the value perpendicular to the screen
		 * vertices is represented in the original base
		 * Change vertices to the new vector base, with vVerticalNormalizado and vVerticalNormalizadoPerp
		 * in normal axis */
		
		double ang = Math.atan(vVertical[1] / vVertical[2]);
		if ( vVertical[2] < 0 ) { // Update angle due to tangent periodicity
		ang =  Math.PI - ang;
		} 
		
  		float vertices[] = { sat[0], (sat[1] + tam)*(float)Math.cos(ang) +  (sat[2] - tam)*(float)Math.sin(ang), - (sat[1] + tam)*(float)Math.sin(ang) +  (sat[2] - tam)*(float)Math.cos(ang) , //left bottom
							 sat[0], (sat[1] + tam)*(float)Math.cos(ang) +  (sat[2] + tam)*(float)Math.sin(ang), - (sat[1] + tam)*(float)Math.sin(ang) +  (sat[2] + tam)*(float)Math.cos(ang),//left-top	
							 sat[0], (sat[1] - tam)*(float)Math.cos(ang) +  (sat[2] - tam)*(float)Math.sin(ang), - (sat[1] - tam)*(float)Math.sin(ang) +  (sat[2] - tam)*(float)Math.cos(ang), //bottom right
						     sat[0], (sat[1] - tam)*(float)Math.cos(ang) +  (sat[2] + tam)*(float)Math.sin(ang), - (sat[1] - tam)*(float)Math.sin(ang) +  (sat[2] + tam)*(float)Math.cos(ang)//top right
		};
		
		float texCoords[] = {
				// Mapping coordinates for the texture
				// Difficult points order.
				// Always value 1, so that only one icon is drawn
				0.0f, 1.0f,     // top left     
				0.0f, 0.0f,     // bottom left  
				1.0f, 1.0f,     // top right    
			    1.0f, 0.0f      // bottom right

		};
		
		//cuadrado 
		ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length * 4);
		bBuff.order(ByteOrder.nativeOrder());
		vertBuff = bBuff.asFloatBuffer();
		vertBuff.put(vertices);
		vertBuff.position(0);
		 
		// icono
		ByteBuffer bBuff2 = ByteBuffer.allocateDirect(texCoords.length * 4);
		bBuff2.order(ByteOrder.nativeOrder());
		texBuffer = bBuff2.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.position(0);

		// Para ejecutar glDrawElements. glDrawArrays es m‡s sencillo
		ByteBuffer pbBuff = ByteBuffer.allocateDirect(pIndex.length * 2);
		pbBuff.order(ByteOrder.nativeOrder());
		pBuff = pbBuff.asShortBuffer();
		pBuff.put(pIndex);
		pBuff.position(0);

	}
	
	public ARGLSat() {
		
	}

	public void draw(GL10 gl) {
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array 
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer

	    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array 
		gl.glDeleteTextures(1, textureIDs, 0); //Para que no de error por tanta textura cargada

	}

}
