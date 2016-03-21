package frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLUniformData;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLArrayDataClient;
import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

import element.cube;
import element.element;

import com.jogamp.opengl.math.Matrix4;
import static java.awt.event.KeyEvent.*;
@SuppressWarnings("unused")
public class Scene extends GLCanvas 
	implements 
	GLEventListener, 
	KeyListener, 
	MouseListener, 
	MouseMotionListener
	{
	private static final long serialVersionUID = 968882202253258274L;
	protected static final int NO_OF_INSTANCE = 1;
	private float angle = 0f;
	public int TextureID = 0;
	
	//Shaders block
	protected ShaderState st;
	private int ShaderProgramID;
	private int vertShader;
	private int fragShader;
	private int projectionMatrixLocation;
	private int transformMatrixLocation;
	
	//Projection block
	private int VERTEX_COUNT = 0;
	protected PMVMatrix projectionMatrix;
	protected GLUniformData projectionMatrixUniform;
	protected GLUniformData transformMatrixUniform;
	//WINSCALE H E R E
	protected float winScale = 0.1f;
	
	private final ArrayList<Matrix4> ModelMatrix = new ArrayList<Matrix4>();
	protected GLArrayDataServer interleavedVBO;
	protected GLArrayDataClient verticesVBO;
	protected GLArrayDataClient colorsVBO;
	
/*	
	private boolean isLightOn = false;
	private boolean blendingEnabled =  false;
	
	private static float angleX = 0.0f; // rotational angle for x-axis in degree
	private static float angleY = 0.0f; // rotational angle for y-axis in degree 
	private static float angleZ = 0.0f; // rotational angle for z-axis in degree
*/	
	private ArrayList<element> SceneObjects = null;
	private static float rotateSpeedX = 0.0f; // rotational speed for y-axis
	private static float rotateSpeedY = 0.0f; // rotational speed for y-axis
	private static float rotateSpeedZ = 0.0f; // rotational speed for z-axis
	
	private static float rotateSpeedXIncrement = 0.1f;
	private static float rotateSpeedYIncrement = 0.1f;
	private static float rotateSpeedZIncrement = 0.1f;
	
	private static boolean RotationCameraEnable = false;
	static final int NOTHING = 0, UPDATE = 1, SELECT = 2;

	private float A,H,W;
	private int cmd = UPDATE;
	private int mouse_x, mouse_y;
	private int CurrentObject = 0;
 	
	private GLU glu = new GLU();
	
	//private FloatBuffer g_vertex_buffer_data;

	public Scene(){
	      this.addGLEventListener(this);
	      this.addKeyListener(this);
	      this.addMouseListener(this);
	      this.addMouseMotionListener(this);
	      this.setFocusable(true);
	      this.requestFocus();
	      this.SceneObjects = new ArrayList<element>();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	     cmd = SELECT;
	     mouse_x = e.getX();
	     mouse_y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	      switch (keyCode) {
	         case VK_UP:   // decrease rotational speed in x
	            rotateSpeedX -= rotateSpeedXIncrement;
	            break;
	         case VK_DOWN: // increase rotational speed in x
	            rotateSpeedX += rotateSpeedXIncrement;
	            break;
	         case VK_LEFT:  // decrease rotational speed in y
	            rotateSpeedY -= rotateSpeedYIncrement;
	            break;
	         case VK_RIGHT: // increase rotational speed in y
	            rotateSpeedY += rotateSpeedYIncrement;
	            break;
	         case VK_O:  // decrease rotational speed in y
		        rotateSpeedZ -= rotateSpeedZIncrement;
		        break;
		     case VK_P: // increase rotational speed in y
		        rotateSpeedZ += rotateSpeedZIncrement;
		        break;
	         case VK_L: // Start/Stop Rotation Camera
		            if (RotationCameraEnable) 
		              RotationCameraEnable = false;
		            else
		              RotationCameraEnable = true;
		        break;
	            
	      }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		
		ShaderProgramID = initShader(gl);
		
		projectionMatrix = new PMVMatrix();
		projectionMatrixUniform = new GLUniformData("mgl_PMatrix", 4, 4, projectionMatrix.glGetPMatrixf());
		st.ownUniform(projectionMatrixUniform);
		if(!st.uniform(gl, projectionMatrixUniform)) {
		    throw new GLException("Error setting mgl_PMatrix in shader: " + st);
		} 
		
		TextureID  = gl.glGetUniformLocation(ShaderProgramID, "myTextureSampler");
		//System.out.println(TextureID);
		
		SceneObjects.add(new cube(gl, st, 0,new float[]{0f,0f,0f,0f}));
	    st.useProgram(gl, false);
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();  // get the OpenGL 4 graphics context
        gl.glClearColor(1f, 1f, 1f, 1.0f);
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT); // | GL4.GL_DEPTH_BUFFER_BIT
        gl.glEnable(GL4.GL_DEPTH_TEST);
        gl.glDepthFunc(GL4.GL_LESS);
        // Default GL State
  
        gl.glUseProgram(ShaderProgramID);
	    
		projectionMatrix.glMatrixMode(GL2.GL_PROJECTION);
		projectionMatrix.glPushMatrix();
		projectionMatrix.glRotatef(angle, 1f, 0f, 0f);
		projectionMatrix.glRotatef(angle, 0f, 1f, 0f);
		projectionMatrix.glRotatef(angle, 0f, 0f, 1f);
		if (angle>359) angle = 0; else angle++;
		projectionMatrix.glScalef(winScale, winScale, winScale);
		projectionMatrix.update();
		
		gl.glUniformMatrix4fv(projectionMatrixLocation, 1, false, projectionMatrix.glGetPMatrixf());
		projectionMatrix.glPopMatrix();

		SceneObjects.get(0).draw(gl,TextureID);
		
		gl.glBindVertexArray(0);
		gl.glUseProgram(0);
	
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glUseProgram(0);
		
		//gl.glDeleteBuffers(2, vbo, 0);
		
		//gl.glDetachShader(shaderProgram, vertShader);
		//gl.glDeleteShader(vertShader);
		//gl.glDetachShader(shaderProgram, fragShader);
		//gl.glDeleteShader(fragShader);
		//gl.glDeleteProgram(shaderProgram);
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = drawable.getGL().getGL4();
		gl.glViewport(0, 0, width, height);
		float aspect = (float) width / (float) height;
		
		projectionMatrix.glMatrixMode(GL2.GL_PROJECTION);
		projectionMatrix.glLoadIdentity();
		projectionMatrix.gluPerspective(45, aspect, 0.001f, 20f);
		projectionMatrix.gluLookAt(0, 0, -10, 0, 0, 0, 0, 1, 0);	
		projectionMatrix.glMatrixMode(GL2.GL_MODELVIEW); //Умножим на матрицу модели
		projectionMatrix.glLoadIdentity();
	}
	
	protected int initShader(GL4 gl) {
      
	  ShaderCode vp0 = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass(),
              "resources/shaders", "shader/bin", "VertexTx", true);
      
      ShaderCode fp0 = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(),
              "resources/shaders", "shader/bin", "FragmentTx", true);
      
      vp0.defaultShaderCustomization(gl, true, true);
      fp0.defaultShaderCustomization(gl, true, true);

      //vp0.dumpShaderSource(System.out);

      // Create & Link the shader program
      ShaderProgram sp = new ShaderProgram();
      sp.add(vp0);
      sp.add(fp0);
      if(!sp.link(gl, System.err)) {
          throw new GLException("Couldn't link program: "+sp);
      }

      // Let's manage all our states using ShaderState.
      st = new ShaderState();
      st.attachShaderProgram(gl, sp, true);
      
      return sp.id();
      
  }
	


} 


