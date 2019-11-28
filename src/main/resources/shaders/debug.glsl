#vert
#version 330

in vec3 vertex;
in vec3 color;
out vec3 oColor;

uniform mat4 proMatrix;
uniform mat4 viewMatrix;

void main(void){
    gl_Position = proMatrix * viewMatrix * vec4(vertex, 1.0);
    oColor = color;
}

    #frag
    #version 330


out vec4 outColor;
in vec3 oColor;

uniform sampler2D diffuseMap;
void main(void){
    outColor = vec4(oColor, 1.0);
}
