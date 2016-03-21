package element;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.GLArrayDataClient;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.texture.Texture;

import loader.ModelLoaderOBJ;
import loader.iLoadBehavior;
import loader.MtlLoader.mtexture;
import moution.iMoutionBehavior;
import rotate.iRotationBehavior;

@SuppressWarnings("unused")
public abstract class element {
	private int ID;
	private int MatrixID;
	private int Type;
	private int Select;
	private boolean Damanged;
	private float x,y,z,w;
	private int ShaderProgramID = 0;
	private ShaderState ShaderSt = null;
	protected GLArrayDataClient verticesVBO = null;
	protected GLArrayDataClient texturesVBO = null;
	protected GLArrayDataClient colorsVBO = null;
	
	
	public class Dimension{
		public Dimension(){
			BasicMatrix = new Matrix4();
		}
		
		public int NORMAL_COUNT = 0;
		public int TEXTURE_COUNT = 0;
		public int MODEL_TEXTURE_COUNT = 0;
		public int VERTEX_COUNT = 0;
		public int POLYTYPE = 0;
		public int TYPE = 0;
		public ArrayList<float[]> Normals = null;
		public ArrayList<float[]> Vertexes = null;
		public ArrayList<float[]> Colors = null;
		public ArrayList<float[]> Textures = null;
		public ArrayList<Texture> ModelTexture = null;
		public Matrix4 BasicMatrix = null;
		
	}
	
	private ArrayList<Dimension> Dimensions = null;
	private ArrayList<Float> BoundingBox = null;
	private ArrayList<Integer> CollisionList = null;
	
	public iMoutionBehavior Moution;
	public iRotationBehavior Rotation;
	public iLoadBehavior Loader;
	
	public element(GL4 gl,ShaderState st,int ID, float[] MC){
		Dimensions = new ArrayList<Dimension>();
		BoundingBox = new ArrayList<Float>();
		CollisionList = new ArrayList<Integer>();
		ShaderSt = st;
		this.ID = ID;
		this.x = MC[0];
		this.y = MC[1];
		this.z = MC[2];
		this.w = MC[3];
	};

	public int GetID(){
		return ID;
	}
	
	public void SetMatrixID(int MID){
		this.MatrixID = MID;
	};
	public int GetMatrixID(){
		return MatrixID;
	};

	public void SetType(int T){
		this.Type = T;
	};
	public int GetType(){
		return Type;
	};
	
	public void SetMainPoint(float[] MPC){
		this.x = MPC[0];
		this.y = MPC[0];
		this.z = MPC[0];
		this.w = MPC[0];
	};
	public float[] GetMainPoint() {
		float[] MPC = new float[]{ x,y,z,w};
		return MPC;
	}

	public void SetSelect(int S){
		this.Select = S;
	};
	public int GetSelect(){
		return Select;
	};
	
	public void SetDamanged(boolean D){
		this.Damanged = D;
	};
	public boolean GetDamanged(){
		return Damanged;
	};
	
	public void AddDimention(Dimension Dm){
		this.Dimensions.add(Dm);
	};
	
	public Dimension GetDimention(int index){
		return this.Dimensions.get(index);
	};
	
	public void SetBoundingBox(ArrayList<Float> BB){
		this.BoundingBox = BB;
	};
	public ArrayList<Float> GetBoundingBox(){
		return this.BoundingBox;
	};
	
	public void LoadOBJ(String FileName){
		Loader.GetQuantityObjects(FileName);
		//for (int i = 0;i<Loader.GetQuantityObjects(FileName);i++){
			Dimension Dm = new Dimension();
			Dm.Normals = Loader.LoadNormals();
			if (ModelLoaderOBJ.model.MODEL_TEXTURE_COUNT >0 )Dm.Textures = Loader.LoadTexrures();
			Dm.Vertexes = Loader.LoadVertexes();
			Dm.ModelTexture = Loader.LoadModelTexures();
			if (ModelLoaderOBJ.model.VERTEX_COUNT >0) Dm.Colors = Loader.LoadColors();
			Dm.NORMAL_COUNT = ModelLoaderOBJ.model.NORMAL_COUNT;
			Dm.TEXTURE_COUNT = ModelLoaderOBJ.model.TEXTURE_COUNT;
			Dm.MODEL_TEXTURE_COUNT = ModelLoaderOBJ.model.MODEL_TEXTURE_COUNT;
			Dm.VERTEX_COUNT = ModelLoaderOBJ.model.VERTEX_COUNT;
			Dm.POLYTYPE = ModelLoaderOBJ.model.POLYTYPE;
			Dimensions.add(Dm);
		//}
	}
	
	public abstract void draw(GL4 gl,int TexID);

}
