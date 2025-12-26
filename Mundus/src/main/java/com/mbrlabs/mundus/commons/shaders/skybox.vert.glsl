
attribute vec3 a_position;

uniform mat4 u_transMatrix;
uniform mat4 u_projViewMatrix;

varying vec3 v_cubeMapUV;

void main() {
    vec4 pos = u_projViewMatrix * u_transMatrix * vec4(a_position, 1.0);
    v_cubeMapUV = a_position;

    // https://learnopengl.com/Advanced-OpenGL/Cubemaps
    // Tricks the depth buffer into believing the skybox is at the maximum depth of 1.0
    gl_Position = pos.xyww;
}