package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ActorUtils}.
 */
public class ActorUtilsTest {

    private Stage mockStage;
    private Actor mockActor;
    private Group mockRoot;
    private OrthographicCamera mockCamera;

    @Before
    public void setUp() {
        mockStage = mock(Stage.class);
        mockActor = mock(Actor.class);
        mockRoot = mock(Group.class);
        mockCamera = new OrthographicCamera();
        mockCamera.position.set(400, 300, 0);
        mockCamera.zoom = 1f;
    }

    @Test(expected = IllegalStateException.class)
    public void testKeepWithinStageActorWithoutStage() {
        when(mockActor.getStage()).thenReturn(null);

        ActorUtils.keepWithinStage(mockActor);
    }

    @Test
    public void testKeepWithinStageWithOrthographicCamera() {
        // Setup mock stage with orthographic camera
        when(mockStage.getCamera()).thenReturn(mockCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        
        // Setup mock actor
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getX(anyInt())).thenReturn(100f);
        when(mockActor.getY(anyInt())).thenReturn(100f);
        when(mockActor.getWidth()).thenReturn(50f);
        when(mockActor.getHeight()).thenReturn(50f);
        
        // Should not throw exception
        ActorUtils.keepWithinStage(mockActor);
        
        // Verify that position methods were called (indicating calculations were performed)
        verify(mockActor, atLeastOnce()).getX(anyInt());
        verify(mockActor, atLeastOnce()).getY(anyInt());
    }

    @Test
    public void testKeepWithinStageWithNonOrthographicCameraAndRootParent() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        when(mockStage.getRoot()).thenReturn(mockRoot);

        // Setup mock actor
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(mockRoot);
        when(mockActor.getX()).thenReturn(100f);
        when(mockActor.getY()).thenReturn(100f);
        when(mockActor.getWidth()).thenReturn(50f);
        when(mockActor.getHeight()).thenReturn(50f);
        when(mockActor.getRight()).thenReturn(150f);
        when(mockActor.getTop()).thenReturn(150f);

        // Should not throw exception
        ActorUtils.keepWithinStage(mockActor);

        // Verify that position methods were called
        verify(mockActor).getX();
        verify(mockActor).getY();
        verify(mockActor).getRight();
        verify(mockActor).getTop();
    }

    @Test
    public void testKeepWithinStageWithNonOrthographicCameraAndNonRootParent() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);

        // Setup mock actor with non-root parent
        Group nonRootParent = mock(Group.class);
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(nonRootParent);

        // Should not throw exception
        ActorUtils.keepWithinStage(mockActor);
    }

    @Test
    public void testKeepWithinStageWithStageAndActorParameters() {
        // Setup mock stage with orthographic camera
        when(mockStage.getCamera()).thenReturn(mockCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        
        // Setup mock actor
        when(mockActor.getX(anyInt())).thenReturn(100f);
        when(mockActor.getY(anyInt())).thenReturn(100f);
        when(mockActor.getWidth()).thenReturn(50f);
        when(mockActor.getHeight()).thenReturn(50f);

        // Should not throw exception
        ActorUtils.keepWithinStage(mockStage, mockActor);

        // Verify that position methods were called
        verify(mockActor, atLeastOnce()).getX(anyInt());
        verify(mockActor, atLeastOnce()).getY(anyInt());
    }

    @Test
    public void testKeepWithinStageActorOutOfBoundsRight() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        when(mockStage.getRoot()).thenReturn(mockRoot);

        // Setup mock actor that's out of bounds on the right
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(mockRoot);
        when(mockActor.getX()).thenReturn(750f); // Out of bounds
        when(mockActor.getY()).thenReturn(100f);
        when(mockActor.getWidth()).thenReturn(100f); // Would extend to 850, out of 800 width
        when(mockActor.getHeight()).thenReturn(50f);
        when(mockActor.getRight()).thenReturn(850f);
        when(mockActor.getTop()).thenReturn(150f);

        // Should not throw exception and should adjust position
        ActorUtils.keepWithinStage(mockActor);

        // Verify that setPosition was called to adjust the position
        verify(mockActor).setX(anyFloat());
    }

    @Test
    public void testKeepWithinStageActorOutOfBoundsLeft() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        when(mockStage.getRoot()).thenReturn(mockRoot);

        // Setup mock actor that's out of bounds on the left
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(mockRoot);
        when(mockActor.getX()).thenReturn(-50f); // Out of bounds
        when(mockActor.getY()).thenReturn(100f);
        when(mockActor.getWidth()).thenReturn(100f);
        when(mockActor.getHeight()).thenReturn(50f);
        when(mockActor.getRight()).thenReturn(50f);
        when(mockActor.getTop()).thenReturn(150f);

        // Should not throw exception and should adjust position
        ActorUtils.keepWithinStage(mockActor);

        // Verify that setPosition was called to adjust the position
        verify(mockActor).setX(0f);
    }

    @Test
    public void testKeepWithinStageActorOutOfBoundsTop() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        when(mockStage.getRoot()).thenReturn(mockRoot);

        // Setup mock actor that's out of bounds on the top
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(mockRoot);
        when(mockActor.getX()).thenReturn(100f);
        when(mockActor.getY()).thenReturn(550f); // Out of bounds
        when(mockActor.getWidth()).thenReturn(50f);
        when(mockActor.getHeight()).thenReturn(100f); // Would extend to 650, out of 600 height
        when(mockActor.getRight()).thenReturn(150f);
        when(mockActor.getTop()).thenReturn(650f);

        // Should not throw exception and should adjust position
        ActorUtils.keepWithinStage(mockActor);

        // Verify that setPosition was called to adjust the position
        verify(mockActor).setY(anyFloat());
    }

    @Test
    public void testKeepWithinStageActorOutOfBoundsBottom() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        when(mockStage.getRoot()).thenReturn(mockRoot);

        // Setup mock actor that's out of bounds on the bottom
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(mockRoot);
        when(mockActor.getX()).thenReturn(100f);
        when(mockActor.getY()).thenReturn(-50f); // Out of bounds
        when(mockActor.getWidth()).thenReturn(50f);
        when(mockActor.getHeight()).thenReturn(100f);
        when(mockActor.getRight()).thenReturn(150f);
        when(mockActor.getTop()).thenReturn(50f);

        // Should not throw exception and should adjust position
        ActorUtils.keepWithinStage(mockActor);

        // Verify that setPosition was called to adjust the position
        verify(mockActor).setY(0f);
    }

    @Test
    public void testKeepWithinStageActorInBounds() {
        // Setup mock stage with non-orthographic camera
        Camera nonOrthographicCamera = mock(Camera.class);
        when(mockStage.getCamera()).thenReturn(nonOrthographicCamera);
        when(mockStage.getWidth()).thenReturn(800f);
        when(mockStage.getHeight()).thenReturn(600f);
        when(mockStage.getRoot()).thenReturn(mockRoot);

        // Setup mock actor that's within bounds
        when(mockActor.getStage()).thenReturn(mockStage);
        when(mockActor.getParent()).thenReturn(mockRoot);
        when(mockActor.getX()).thenReturn(100f);
        when(mockActor.getY()).thenReturn(100f);
        when(mockActor.getWidth()).thenReturn(50f);
        when(mockActor.getHeight()).thenReturn(50f);
        when(mockActor.getRight()).thenReturn(150f);
        when(mockActor.getTop()).thenReturn(150f);

        // Should not throw exception and should not adjust position
        ActorUtils.keepWithinStage(mockActor);

        // Verify that setPosition was not called since actor is in bounds
        verify(mockActor, never()).setX(anyFloat());
        verify(mockActor, never()).setY(anyFloat());
    }
}
