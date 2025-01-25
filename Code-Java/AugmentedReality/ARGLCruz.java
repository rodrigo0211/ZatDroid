package com.zatdroid.rodrigo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ARGLCruz {
private FloatBuffer vertBuff;
	
	private short[] pIndex = { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 };
	
	private ShortBuffer pBuff;
		
	public void update (float[] pto, float[] sat){
		
	}
	public ARGLCruz() {
		
		//eje y a la izda, eje z arriba
		float tam = 1.5f;
		float tam2 = 1f;
		float vertices[] = {
			0f,-tam,tam,
		    0f,tam,tam,
		    
		    0f,-tam,-tam,
		    0f,tam,-tam,
		    
		    0f,tam, tam,
		    0f,tam, -tam,
		    
		    0f,-tam, tam,
		    0f,-tam, -tam,
		    
		    0f, 0f, tam,
		    0f, 0f, tam + tam2,
		    
		    0f, 0f, -tam,
		    0f, 0f, -tam - tam2,
		    
		    0f, tam,0f,
		    0f, tam + tam2,0f,
		    
		    0f, -tam,0f,
		    0f, -tam - tam2,0f,
		    
			};
		
		ByteBuffer bBuff = ByteBuffer.allocateDirect(vertices.length * 4);
		bBuff.order(ByteOrder.nativeOrder());
		vertBuff = bBuff.asFloatBuffer();
		vertBuff.put(vertices);
		vertBuff.position(0);
		
		ByteBuffer pbBuff = ByteBuffer.allocateDirect(pIndex.length * 2);
		pbBuff.order(ByteOrder.nativeOrder());
		pBuff = pbBuff.asShortBuffer();
		pBuff.put(pIndex);
		pBuff.position(0);		
	}
	
	public void draw(GL10 gl){
		
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
		
		gl.glDrawElements(GL10.GL_LINES, pIndex.length, GL10.GL_UNSIGNED_SHORT, pBuff);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
	}
	
	
}
