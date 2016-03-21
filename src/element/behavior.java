package element;

import java.util.ArrayList;

import com.jogamp.opengl.util.texture.Texture;

import loader.GLModel;
import loader.ModelLoaderOBJ;
import loader.iLoadBehavior;
import moution.iMoutionBehavior;
import rotate.iRotationBehavior;

public class behavior implements iMoutionBehavior, iRotationBehavior, iLoadBehavior {
	
	private GLModel model;	
	
	@Override
	public void ReleaseMoving() {
	}

	@Override
	public int GetQuantityObjects(String FileName) {
		model = ModelLoaderOBJ.LoadModel(FileName,FileName.replace(".obj", ".mtl"));
		model.POLYTYPE = model.MakeMeshes();
		return 0;
	}

	@Override
	public ArrayList<float[]> LoadNormals() {
		return model.Normals;
	}

	@Override
	public ArrayList<float[]> LoadTexrures() {
		return model.Textures;
	}

	@Override
	public ArrayList<float[]> LoadVertexes() {
		return model.Vertexes;
	}
	
	@Override
	public void Dummy() {
		System.out.println("It's work!");
		
	}

	@Override
	public ArrayList<Texture> LoadModelTexures() {
		return model.Tx;
	}

	@Override
	public ArrayList<float[]> LoadColors() {
		return model.Colors;
		
	}
}
