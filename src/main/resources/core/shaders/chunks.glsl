#vert
#version 330

in vec3 vertex;
in vec2 uv;
in int light;

out vec2 passUV;
out float passLight;

uniform mat4 transMatrix;
uniform mat4 proMatrix;
uniform mat4 viewMatrix;

void main(void){
    gl_Position = proMatrix * viewMatrix * transMatrix * vec4(vertex, 1.0);
    passUV = uv;
    passLight = float(light) / 16.0;
}

    #frag
    #version 330


out vec4 outColor;
in vec2 passUV;
in float passLight;

uniform sampler2D diffuseMap;
void main(void){
    vec4 color = texture(diffuseMap, passUV);
    if (color.a == 0)
        discard;

    outColor = vec4(passLight, passLight, passLight, 1.0) * color;

}
