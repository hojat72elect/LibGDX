package com.crashinvaders.vfx.gl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.nio.IntBuffer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VfxGLUtilsTest {

    @Mock
    Application app;
    @Mock
    GL20 gl;
    @Mock
    VfxGlExtension glExtension;
    @Mock
    FileHandle vertexFile;
    @Mock
    FileHandle fragmentFile;

    private AutoCloseable mocks;

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        Gdx.app = app;
        Gdx.gl = gl;
        Gdx.gl20 = gl;

        // Manually inject mock glExtension since it's initialized in static block
        VfxGLUtils.glExtension = glExtension;

        // Reset static fields
        VfxGLUtils.prependVertexCode = "";
        VfxGLUtils.prependFragmentCode = "";
        VfxGLUtils.enableGLQueryStates = false;
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void testGetBoundFboHandle() {
        when(glExtension.getBoundFboHandle()).thenReturn(42);
        assertEquals(42, VfxGLUtils.getBoundFboHandle());
        verify(glExtension).getBoundFboHandle();
    }

    @Test
    public void testGetViewport() {
        doAnswer(invocation -> {
            IntBuffer buffer = invocation.getArgument(1);
            buffer.put(0, 10);
            buffer.put(1, 20);
            buffer.put(2, 300);
            buffer.put(3, 400);
            return null;
        }).when(gl).glGetIntegerv(eq(GL20.GL_VIEWPORT), any(IntBuffer.class));

        VfxGlViewport viewport = VfxGLUtils.getViewport();

        assertEquals(10, viewport.x);
        assertEquals(20, viewport.y);
        assertEquals(300, viewport.width);
        assertEquals(400, viewport.height);
    }

    @Test
    public void testIsGLEnabled() {
        VfxGLUtils.enableGLQueryStates = false;
        assertFalse(VfxGLUtils.isGLEnabled(GL20.GL_BLEND));

        VfxGLUtils.enableGLQueryStates = true;

        // Use doAnswer to simulate writing 1 (true) to the ByteBuffer
        doAnswer(invocation -> {
            java.nio.ByteBuffer buffer = invocation.getArgument(1);
            buffer.put((byte) 1);
            buffer.flip();
            return null;
        }).when(gl).glGetBooleanv(eq(GL20.GL_BLEND), any(java.nio.ByteBuffer.class));

        assertTrue(VfxGLUtils.isGLEnabled(GL20.GL_BLEND));

        // Test unknown pName
        assertFalse(VfxGLUtils.isGLEnabled(0x1234));
    }

    @Test
    public void testCompileShader() {
        when(vertexFile.name()).thenReturn("vertex.glsl");
        when(vertexFile.readString()).thenReturn("void main() { gl_Position = vec4(0.0); }");
        when(fragmentFile.name()).thenReturn("fragment.glsl");
        when(fragmentFile.readString()).thenReturn("void main() { gl_FragColor = vec4(1.0); }");

        VfxGLUtils.prependVertexCode = "#define VERTEX\n";
        VfxGLUtils.prependFragmentCode = "#define FRAGMENT\n";

        try (MockedConstruction<ShaderProgram> mocked = mockConstruction(ShaderProgram.class, (mock, context) -> {
            // Verify constructor arguments
            String vert = (String) context.arguments().get(0);
            String frag = (String) context.arguments().get(1);

            assertTrue(vert.contains("#define VERTEX"));
            assertTrue(vert.contains("MY_DEFINE"));
            assertTrue(vert.contains("void main() { gl_Position = vec4(0.0); }"));

            assertTrue(frag.contains("#define FRAGMENT"));
            assertTrue(frag.contains("MY_DEFINE"));
            assertTrue(frag.contains("void main() { gl_FragColor = vec4(1.0); }"));

            when(mock.isCompiled()).thenReturn(true);
        })) {
            ShaderProgram shader = VfxGLUtils.compileShader(vertexFile, fragmentFile, "MY_DEFINE");
            assertNotNull(shader);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test(expected = GdxRuntimeException.class)
    public void testCompileShaderError() {
        when(vertexFile.name()).thenReturn("vertex.glsl");
        when(vertexFile.readString()).thenReturn("bad vertex");
        when(fragmentFile.name()).thenReturn("fragment.glsl");
        when(fragmentFile.readString()).thenReturn("bad fragment");

        try (MockedConstruction<ShaderProgram> mocked = mockConstruction(ShaderProgram.class, (mock, context) -> {
            when(mock.isCompiled()).thenReturn(false);
            when(mock.getLog()).thenReturn("Compile Error Log");
        })) {
            VfxGLUtils.compileShader(vertexFile, fragmentFile);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileShaderNullVertex() {
        VfxGLUtils.compileShader(null, fragmentFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileShaderNullFragment() {
        // Wait, VfxGLUtils checks fragmentFile first then vertexFile?
        // Let's re-read the code.
        // 80: if (fragmentFile == null) { throw new IllegalArgumentException("Vertex
        // shader file cannot be null."); }
        // 83: if (vertexFile == null) { throw new IllegalArgumentException("Fragment
        // shader file cannot be null."); }

        // Oops, the error messages in VfxGLUtils are swapped or at least confusing!
        // 80: if (fragmentFile == null) throws "Vertex shader file cannot be null."
        VfxGLUtils.compileShader(vertexFile, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileShaderNullDefines() {
        VfxGLUtils.compileShader(vertexFile, fragmentFile, null);
    }
}
