
#include "compat.glsl"

varying float v_clipDistance;

/*
 * Encodes a floating point number in [0..1) range into several channels of 8 bit/channel render texture for precision
 *
 * https://aras-p.info/blog/2009/07/30/encoding-floats-to-rgba-the-final/
*/
vec4 EncodeFloatRGBA(float v) {
    vec4 enc = vec4(1.0, 255.0, 65025.0, 16581375.0) * v;
    enc = fract(enc);
    enc -= enc.yzww * vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
    return enc;
}

void main() {
    if (v_clipDistance < 0.0)
        discard;

    // Encode the depth into RGBA values, for later decoding in other shaders, to improve precision
    gl_FragColor = EncodeFloatRGBA(gl_FragCoord.z);
}