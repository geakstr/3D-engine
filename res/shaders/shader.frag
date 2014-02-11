#version 120
// The color of the vertex
varying vec4 vColor;

// The texture
uniform sampler2D texture;

const vec4 fogcolor = vec4(0.9, 0.9, 0.9, 0.5);
const float fogdensity = 0.032;

float ComputeFogFactor(const in vec4 coord, const in float density) {
    const float LOG2 = 1.442695;
    float z = coord.z / coord.w;
    float fogFactor = exp2(-density * density * z * z * LOG2);
    return clamp(fogFactor, 0.0, 1.0);
}

void main()
{
    // Calculate the texture color
    vec4 texColor = texture2D(texture, gl_TexCoord[0].st);

    // Combine the texture color and the vertex color
    vec4 color = vec4(texColor.rgb * vColor.rgb, texColor.a * vColor.a);

    float fogFactor = ComputeFogFactor(gl_FragCoord, fogdensity);
    vec4 finalcolor = mix(fogcolor, color, fogFactor);
    finalcolor.a = 1.0;

    gl_FragColor = finalcolor;
}