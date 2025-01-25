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

public class ARGLFlecha {
	private FloatBuffer vertBuff;
	private FloatBuffer texBuffer; // buffer holding the texture coordinates
	private short[] pIndex = { 0,1 };

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

	}


	public void Update (float[] sat) { 

     float vertices[] = { 
		  sat[0],
		  sat[1],
		  sat[2],
		  0,0,0
		};
		
		
		//linea 
		ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length * 4);
		bBuff.order(ByteOrder.nativeOrder());
		vertBuff = bBuff.asFloatBuffer();
		vertBuff.put(vertices);
		vertBuff.position(0);

		// Para ejecutar glDrawElements. glDrawArrays es m‡s sencillo
		ByteBuffer pbBuff = ByteBuffer.allocateDirect(pIndex.length * 2);
		pbBuff.order(ByteOrder.nativeOrder());
		pBuff = pbBuff.asShortBuffer();
		pBuff.put(pIndex);
		pBuff.position(0);

	}
	
	public ARGLFlecha() {
		
	}

	public void draw(GL10 gl) {
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
		//gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array 
	    //gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer

	    gl.glDrawElements(GL10.GL_LINES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		//gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array 
		//gl.glDeleteTextures(1, textureIDs, 0); //Para que no de error por tanta textura cargada

	}

}