
attribute vec3 a_position;

uniform mat4 u_transMatrix;
uniform mat4 u_projViewMatrix;

// clipping plane
varying float v_clipDistance;
uniform vec4 u_clipPlane;

void main() {
    vec4 worldPos = u_transMatrix * vec4(a_position, 1.0);
    gl_Position = u_projViewMatrix * worldPos;

    // clipping plane
    v_clipDistance = dot(worldPos, u_clipPlane);
}