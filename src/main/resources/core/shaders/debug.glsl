#vert
#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoords;

out vec2 texCoords;

void main()
{
    gl_Position = vec4(aPos, 0.0f, 1.0f);
    texCoords = aTexCoords;
}

    #frag
    #version 330 core
out vec4 outColor;
in  vec2 texCoords;

uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gDiffuse;

void main(void){
    outColor = vec4(texture(gDiffuse, texCoords).rgb, 1.0);
}
