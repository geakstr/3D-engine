#version 120
// The color of the vertex
varying vec4 vColor;

// The texture
uniform sampler2D texture;

const vec4 fogcolor = vec4(0.2, 0.2, 0.2, 1);
const float fogdensity = .0005;

void main()
{
    // Calculate the texture color
    vec4 texColor = texture2D(texture, gl_TexCoord[0].st);


    // Combine the texture color and the vertex color
    vec4 color = vec4(texColor.rgb * vColor.rgb, texColor.a * vColor.a);

    float z = gl_FragCoord.z / gl_FragCoord.w;
    float fog = clamp(exp(-fogdensity * z * z), 0.2, 1);
    gl_FragColor = mix(fogcolor, color, fog);
}