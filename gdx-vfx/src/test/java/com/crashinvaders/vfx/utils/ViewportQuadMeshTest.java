package com.crashinvaders.vfx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

public class ViewportQuadMeshTest {

    @Before
    public void setUp() {
        // Mock GL20 just in case something else still uses it
        GL20 gl = mock(GL20.class);
        Gdx.gl = gl;
        Gdx.gl20 = gl;
    }

    @Test
    public void testInitialization() {
        try (MockedConstruction<Mesh> mocked = mockConstruction(Mesh.class)) {
            ViewportQuadMesh quadMesh = new ViewportQuadMesh();
            assertNotNull(quadMesh.getMesh());
            assertEquals(1, mocked.constructed().size());

            Mesh mockMesh = mocked.constructed().get(0);
            verify(mockMesh).setVertices(any(float[].class));
        }
    }

    @Test
    public void testCustomAttributes() {
        try (MockedConstruction<Mesh> mocked = mockConstruction(Mesh.class)) {
            VertexAttribute attr = new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_pos");
            ViewportQuadMesh quadMesh = new ViewportQuadMesh(attr);
            assertNotNull(quadMesh.getMesh());
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    public void testRender() {
        try (MockedConstruction<Mesh> mocked = mockConstruction(Mesh.class)) {
            ViewportQuadMesh quadMesh = new ViewportQuadMesh();
            Mesh mockMesh = mocked.constructed().get(0);
            ShaderProgram shader = mock(ShaderProgram.class);

            quadMesh.render(shader);

            verify(mockMesh).render(eq(shader), eq(GL20.GL_TRIANGLE_FAN), eq(0), eq(4));
        }
    }

    @Test
    public void testDispose() {
        try (MockedConstruction<Mesh> mocked = mockConstruction(Mesh.class)) {
            ViewportQuadMesh quadMesh = new ViewportQuadMesh();
            Mesh mockMesh = mocked.constructed().get(0);

            quadMesh.dispose();

            verify(mockMesh).dispose();
        }
    }
}
