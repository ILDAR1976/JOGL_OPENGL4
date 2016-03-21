package loader;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;

public class GLModel{
	private final int gl_TRIANGLES = 4;
	private final int gl_QUADS = 7;
	private final int gl_POLIGON = 9;
	
	private float colorx = 0f;
	private float colory = 0f;
	private float colorz = 0f;
	
	public int NORMAL_COUNT = 0;
	public int TEXTURE_COUNT = 0;
	public int MODEL_TEXTURE_COUNT = 0;
	public int VERTEX_COUNT = 0;
	public int POLYTYPE = 0; 
	
	private ArrayList<Float> N = new ArrayList<Float>();
	private ArrayList<Float> T = new ArrayList<Float>();
	private ArrayList<Float> V = new ArrayList<Float>();
	private ArrayList<Float> C = new ArrayList<Float>();
	
	public ArrayList<float[]> Normals = new ArrayList<float[]>();
	public ArrayList<float[]> Textures = new ArrayList<float[]>();
	public ArrayList<float[]> Vertexes = new ArrayList<float[]>();
	public ArrayList<float[]> Colors = new ArrayList<float[]>();
	public ArrayList<Texture> Tx = new ArrayList<Texture>();
	
	private ArrayList<float[]> vertexsets;
	private ArrayList<float[]> vertexsetsnorms;
	private ArrayList<float[]> vertexsetstexs;
    private ArrayList<int[]> faces;
    private ArrayList<int[]> facestexs;
    private ArrayList<int[]> facesnorms;
    private ArrayList<String[]> mattimings;
    private MtlLoader materials;
    private int objectlist;
    private int numpolys;
    public float toppoint;
    public float bottompoint;
    public float leftpoint;
    public float rightpoint;
    public float farpoint;
    public float nearpoint;
    private String mtl_path;

	//THIS CLASS LOADS THE MODELS	
    public GLModel(BufferedReader ref, boolean centerit, String path){
        
        mtl_path=path;
        vertexsets = new ArrayList<float[]>();
        vertexsetsnorms = new ArrayList<float[]>();
        vertexsetstexs = new ArrayList<float[]>();
        faces = new ArrayList<int[]>();
        facestexs = new ArrayList<int[]>();
        facesnorms = new ArrayList<int[]>();
        mattimings = new ArrayList<String[]>();
        numpolys = 0;
        toppoint = 0.0F;
        bottompoint = 0.0F;
        leftpoint = 0.0F;
        rightpoint = 0.0F;
        farpoint = 0.0F;
        nearpoint = 0.0F;
        loadobject(ref);
        if(centerit)
            centerit();
        numpolys = faces.size();
    }

    private void cleanup(){
        vertexsets.clear();
        vertexsetsnorms.clear();
        vertexsetstexs.clear();
        faces.clear();
        facestexs.clear();
        facesnorms.clear();
    }

    private void loadobject(BufferedReader br){
    	int linecounter = 0;
    	int facecounter = 0;
        try{
            boolean firstpass = true;
            String newline;
            while((newline = br.readLine()) != null){
              	linecounter++;
                if(newline.length() > 0){
                    newline = newline.trim();
                    
                    //LOADS VERTEX COORDINATES
                    if(newline.startsWith("v ")){
                        float coords[] = new float[4];
                        String coordstext[] = new String[4];
                        newline = newline.substring(2, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        for(int i = 0; st.hasMoreTokens(); i++)
                            coords[i] = Float.parseFloat(st.nextToken());

                        if(firstpass){
                            rightpoint = coords[0];
                            leftpoint = coords[0];
                            toppoint = coords[1];
                            bottompoint = coords[1];
                            nearpoint = coords[2];
                            farpoint = coords[2];
                            firstpass = false;
                        }
                        if(coords[0] > rightpoint)
                            rightpoint = coords[0];
                        if(coords[0] < leftpoint)
                            leftpoint = coords[0];
                        if(coords[1] > toppoint)
                            toppoint = coords[1];
                        if(coords[1] < bottompoint)
                            bottompoint = coords[1];
                        if(coords[2] > nearpoint)
                            nearpoint = coords[2];
                        if(coords[2] < farpoint)
                            farpoint = coords[2];
                        vertexsets.add(coords);
                    }
                    else
                    
                    //LOADS VERTEX TEXTURE COORDINATES
                    if(newline.startsWith("vt")){
                        float coords[] = new float[4];
                        String coordstext[] = new String[4];
                        newline = newline.substring(3, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        for(int i = 0; st.hasMoreTokens(); i++)
                            coords[i] = Float.parseFloat(st.nextToken());

                        vertexsetstexs.add(coords);
                    }
                    else
                    
                    //LOADS VERTEX NORMALS COORDINATES
                    if(newline.startsWith("vn")){
                        float coords[] = new float[4];
                        String coordstext[] = new String[4];
                        newline = newline.substring(3, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        for(int i = 0; st.hasMoreTokens(); i++)
                            coords[i] = Float.parseFloat(st.nextToken());

                        vertexsetsnorms.add(coords);
                    }
                    else
                    
                    //LOADS FACES COORDINATES
                    if(newline.startsWith("f ")){
                    	facecounter++;
                        newline = newline.substring(2, newline.length());
                        StringTokenizer st = new StringTokenizer(newline, " ");
                        int count = st.countTokens();
                        int v[] = new int[count];
                        int vt[] = new int[count];
                        int vn[] = new int[count];
                        for(int i = 0; i < count; i++){
                            char chars[] = st.nextToken().toCharArray();
                            StringBuffer sb = new StringBuffer();
                            char lc = 'x';
                            for(int k = 0; k < chars.length; k++){
                                if(chars[k] == '/' && lc == '/')
                                    sb.append('0');
                                lc = chars[k];
                                sb.append(lc);
                            }

                            StringTokenizer st2 = new StringTokenizer
                            (sb.toString(), "/");
                            int num = st2.countTokens();
                            v[i] = Integer.parseInt(st2.nextToken());
                            if(num > 1)
                                vt[i] = Integer.parseInt(st2.nextToken());
                            else
                                vt[i] = 0;
                            if(num > 2)
                                vn[i] = Integer.parseInt(st2.nextToken());
                            else
                                vn[i] = 0;
                        }

                        faces.add(v);
                        facestexs.add(vt);
                        facesnorms.add(vn);
                    }
                    else
                    
                    //LOADS MATERIALS
                    if (newline.charAt(0) == 'm' && newline.charAt(1) == 't' && newline.charAt(2) == 'l' && newline.charAt(3) == 'l' && newline.charAt(4) == 'i' && newline.charAt(5) == 'b') {
						String[] coordstext = new String[3];
						coordstext = newline.split("\\s+");
						if(mtl_path!=null)
							loadmaterials();
					}
					else
					
					//USES MATELIALS
					if (newline.charAt(0) == 'u' && newline.charAt(1) == 's' && newline.charAt(2) == 'e' && newline.charAt(3) == 'm' && newline.charAt(4) == 't' && newline.charAt(5) == 'l') {
						String[] coords = new String[2];
						String[] coordstext = new String[3];
						coordstext = newline.split("\\s+");
						coords[0] = coordstext[1];
						coords[1] = facecounter + "";
						mattimings.add(coords);
						//System.out.println(coords[0] + ", " + coords[1]);
					}
                }
             }
            
        }
        catch(IOException e){
            System.out.println("Failed to read file: " + br.toString());
        }
        catch(NumberFormatException e){
            System.out.println("Malformed OBJ file: " + br.toString() + "\r \r"+ e.getMessage());
        }
    }
    
    private void loadmaterials() {
		FileReader frm;
		String refm = mtl_path;

		try {
			frm = new FileReader(refm);
			BufferedReader brm = new BufferedReader(frm);
			materials = new MtlLoader(brm,mtl_path);
			if (materials.Textures != null) MODEL_TEXTURE_COUNT = materials.Textures.size();
			frm.close();
		} catch (IOException e) {
			System.out.println("Could not open file: " + refm);
			materials = null;
		}
	}

    private void centerit(){
        float xshift = (rightpoint - leftpoint) / 2.0F;
        float yshift = (toppoint - bottompoint) / 2.0F;
        float zshift = (nearpoint - farpoint) / 2.0F;
        for(int i = 0; i < vertexsets.size(); i++){
            float coords[] = new float[4];
            coords[0] = ((float[])vertexsets.get(i))[0] - leftpoint - xshift;
            coords[1] = ((float[])vertexsets.get(i))[1] - bottompoint - yshift;
            coords[2] = ((float[])vertexsets.get(i))[2] - farpoint - zshift;
            vertexsets.set(i, coords);
        }
    }

    public float getXWidth(){
        float returnval = 0.0F;
        returnval = rightpoint - leftpoint;
        return returnval;
    }

    public float getYHeight(){
        float returnval = 0.0F;
        returnval = toppoint - bottompoint;
        return returnval;
    }

    public float getZDepth(){
        float returnval = 0.0F;
        returnval = nearpoint - farpoint;
        return returnval;
    }

    public int numpolygons(){
        return numpolys;
    }

    public int MakeMeshes(){
    	int nextmat = -1;
		int matcount = 0;
		int totalmats = mattimings.size();
		int polytype = 0;
		
		String[] nextmatnamearray = null;
		String nextmatname = null;
		
		if (totalmats > 0 && materials != null) {
			nextmatnamearray = (String[])(mattimings.get(matcount));
			nextmatname = nextmatnamearray[0];
			nextmat = Integer.parseInt(nextmatnamearray[1]);
		}
		
		for (int i=0;i<faces.size();i++) {
			if (i == nextmat) {
				colorx = (materials.getKd(nextmatname))[0];
				colory = (materials.getKd(nextmatname))[1];
				colorz = (materials.getKd(nextmatname))[2];
				matcount++;
				if (matcount < totalmats) {
					nextmatnamearray = (String[])(mattimings.get(matcount));
					nextmatname = nextmatnamearray[0];
					nextmat = Integer.parseInt(nextmatnamearray[1]);
				}
			}
			
			int[] tempfaces = (int[])(faces.get(i));
			int[] tempfacesnorms = (int[])(facesnorms.get(i));
			int[] tempfacestexs = (int[])(facestexs.get(i));
			
			
			
			if (tempfaces.length == 3) {
				polytype = gl_TRIANGLES;
			} else if (tempfaces.length == 4) {
				polytype = gl_QUADS;
			} else {
				polytype = gl_POLIGON;
			}
			
			for (int w=0;w<tempfaces.length;w++) {
				if (tempfacesnorms[w] != 0) {
					float normtempx = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[0];
					float normtempy = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[1];
					float normtempz = ((float[])vertexsetsnorms.get(tempfacesnorms[w] - 1))[2];
					N.add(normtempx);
					N.add(normtempy);
					N.add(normtempz);
				}
				
				if (tempfacestexs[w] != 0) {
					float textempx = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[0];
					float textempy = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[1];
					float textempz = ((float[])vertexsetstexs.get(tempfacestexs[w] - 1))[2];
					T.add(textempx);
					T.add(textempy);
					T.add(textempz);
				}
				
				float tempx = ((float[])vertexsets.get(tempfaces[w] - 1))[0];
				float tempy = ((float[])vertexsets.get(tempfaces[w] - 1))[1];
				float tempz = ((float[])vertexsets.get(tempfaces[w] - 1))[2];
				V.add(tempx);
				V.add(tempy);
				V.add(tempz);
				
				C.add(colorx);
				C.add(colory);
				C.add(colorz);


			}
		}
	
	cleanup();	
	
	NORMAL_COUNT = N.size();
	TEXTURE_COUNT = T.size();
	VERTEX_COUNT = V.size();
	
	Normals.add(new float[NORMAL_COUNT]);
	float[] Nb = Normals.get(0);
	for (int i = 0;i<NORMAL_COUNT;i++) Nb[i] = N.get(i); 
	
	Textures.add(new float[TEXTURE_COUNT]);
	int c = 0;
	float[] Tb = Textures.get(0);
	for (int i = 0;i<TEXTURE_COUNT;i++){
		if (T.get(i) != 0) {
			Tb[c] = T.get(i);
			c++;
		}
	}
	
	Vertexes.add(new float[VERTEX_COUNT]);
	float[] Vb = Vertexes.get(0);
	for (int i = 0;i<VERTEX_COUNT;i++) Vb[i] = V.get(i); 
	
 
	Colors.add(new float[VERTEX_COUNT]);
	float[] Cb = Colors.get(0);
	for (int i = 0;i<VERTEX_COUNT;i++) Cb[i] = C.get(i); 
	
	if (MODEL_TEXTURE_COUNT !=0) Tx.add(materials.Textures.get(0).glTexture);
	return polytype;
    }
}
