
#include "compat.glsl"

varying vec2
v_texCoords0;

uniform float u_alphaTest;
uniform int u_useAlphaTest;
uniform sampler2D
u_diffuseTexture;

vec4 EncodeFloatRGBA(float v) {
    vec4 enc = vec4(1.0, 255.0, 65025.0, 16581375.0) * v;
    enc = fract(enc);
    enc -= enc.yzww * vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
    return enc;
}

void main() {
    if (u_useAlphaTest == 1) {
        if (texture2D(u_diffuseTexture, v_texCoords0).a < u_alphaTest) {
            discard;
        }
    }

    // Encode the depth into RGBA values, for later decoding in other shaders, to improve precision
    gl_FragColor = EncodeFloatRGBA(gl_FragCoord.z);
}