package element;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLArrayDataClient;
import com.jogamp.opengl.util.glsl.ShaderState;

public class cube extends element {
	
	public cube(GL4 gl,ShaderState st, int ID, float[] MC) {
		super(gl, st, ID, MC);
		Loader = new behavior();
		this.LoadOBJ("./src/resources/models/c.obj");
		
	
        
        if (this.GetDimention(0).MODEL_TEXTURE_COUNT > 0){
	        texturesVBO = GLArrayDataClient.createGLSL("vertexUV", 2, GL4.GL_FLOAT, false, this.GetDimention(0).TEXTURE_COUNT);
	        FloatBuffer texturesBuf = (FloatBuffer)texturesVBO.getBuffer();
	        texturesBuf.put(this.GetDimention(0).Textures.get(0));
	        texturesVBO.seal(gl, true);
	        texturesVBO.enableBuffer(gl, false);
	        st.ownAttribute(texturesVBO, true);
	    }
        
        if (this.GetDimention(0).Colors.get(0) != null) {
	        colorsVBO = GLArrayDataClient.createGLSL("vertexColor", 3, GL4.GL_FLOAT, false, this.GetDimention(0).VERTEX_COUNT);
	        FloatBuffer colorBuf = (FloatBuffer)colorsVBO.getBuffer();
	        colorBuf.put(this.GetDimention(0).Colors.get(0));
	        colorsVBO.seal(gl, true);
	        colorsVBO.enableBuffer(gl, false);
	        st.ownAttribute(colorsVBO, true);
        }	

        verticesVBO = GLArrayDataClient.createGLSL("mgl_Vertex", 3, GL4.GL_FLOAT, false, this.GetDimention(0).VERTEX_COUNT);
        FloatBuffer verticeBuf = (FloatBuffer)verticesVBO.getBuffer();
        verticeBuf.put(this.GetDimention(0).Vertexes.get(0));
        verticesVBO.seal(gl, true);
        verticesVBO.enableBuffer(gl, false);
        st.ownAttribute(verticesVBO, true);
        
	}

	@Override
	public void draw(GL4 gl,int TexID) {
		//Rotation.ReleaseMoving();
		if (this.GetDimention(0).MODEL_TEXTURE_COUNT > 0) {
			gl.glBindTexture(GL4.GL_TEXTURE_2D, this.GetDimention(0).ModelTexture.get(0).getTextureObject());
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		}
		
		verticesVBO.enableBuffer(gl, true);
		if (this.GetDimention(0).MODEL_TEXTURE_COUNT > 0) texturesVBO.enableBuffer(gl, true);
		if (this.GetDimention(0).VERTEX_COUNT > 0) colorsVBO.enableBuffer(gl, true);
		
		
		gl.glDrawArrays(this.GetDimention(0).POLYTYPE, 0, this.GetDimention(0).VERTEX_COUNT); 
		
	}
	
	
}
