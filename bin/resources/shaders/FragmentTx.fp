in vec2 UV;
in vec3 VC;
out vec3 mgl_FragColor;
uniform sampler2D myTextureSampler;
void main (void) {
	mgl_FragColor = ((texture2D( myTextureSampler,UV).rgb * VC) != vec3(0,0,0))? texture( myTextureSampler,UV).rgb * VC : VC ; 
//	mgl_FragColor = texture( myTextureSampler,UV).rgb; 
} 
