package loader;
import java.io.*;

import com.jogamp.common.nio.Buffers;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;

public class ModelLoaderOBJ {
	public static GLModel model = null;
	
	public static GLModel LoadModel(String objPath, String mtlPath)
	{
		try {
			FileInputStream r_path1 = new FileInputStream(objPath);
			BufferedReader b_read1 = new BufferedReader(new InputStreamReader(
					r_path1));
		    
			System.out.println("Object file " + objPath + " loaded");
			
		    model = new GLModel(b_read1, true,
					mtlPath);
			
			System.out.println("MTL file " + mtlPath + " loaded");
			
			r_path1.close();
			b_read1.close();

		} catch (Exception e) {
			System.out.println("LOADING ERROR" + e);
		}

		System.out.println("ModelLoaderOBJ init() done"); 
		return model;
	}
}
