package loader;

import java.util.ArrayList;
import com.jogamp.opengl.util.texture.Texture;

public interface iLoadBehavior {
	public abstract int GetQuantityObjects(String FileName);
	public abstract ArrayList<float[]> LoadNormals();
	public abstract ArrayList<float[]> LoadTexrures();
	public abstract ArrayList<float[]> LoadVertexes();
	public abstract ArrayList<float[]> LoadColors();
	public abstract ArrayList<Texture> LoadModelTexures();
	public abstract void Dummy();
}
