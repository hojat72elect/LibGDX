package games.rednblack.editor.view;

import com.badlogic.gdx.utils.viewport.ScreenViewport;

import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.proxy.ResolutionManager;
import games.rednblack.editor.proxy.ResourceManager;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.data.CompositeItemVO;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.editor.renderer.data.SceneVO;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.puremvc.Facade;

/**
 * Mediates scene communication between editor and current runtime
 *
 *
 */
public class SceneControlMediator {

    private final Facade facade;
    private final ProjectManager projectManager;
    /**
     * main holder of the scene
     */
    public SceneLoader sceneLoader;


    /**
     * current scene tools
     */
    private SceneVO currentSceneVo;

    /**
     * tools object of the root element of the scene
     */
    private CompositeItemVO rootSceneVO;

    public SceneControlMediator(SceneLoader sceneLoader) {
        this.sceneLoader = sceneLoader;
        facade = Facade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    public ProjectInfoVO getProjectInfoVO() {
        return sceneLoader.getRm().getProjectVO();
    }

    public void initScene(String sceneName) {
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);

        ScreenViewport viewport = new ScreenViewport();
        float sandboxUpp = Sandbox.getInstance().getUIStage().getUIScaleDensity();
        float upp = 1f / resourceManager.getProjectVO().pixelToWorld * sandboxUpp;
        viewport.setUnitsPerPixel(upp);

        currentSceneVo = sceneLoader.loadScene(sceneName, viewport);

        rootSceneVO = new CompositeItemVO(currentSceneVo.composite);
        Sandbox.getInstance().getEngine().process();
    }

    public void updateAmbientLights() {
        sceneLoader.setAmbientInfo(sceneLoader.getSceneVO());
    }

    public CompositeItemVO getRootSceneVO() {
        return rootSceneVO;
    }

    public SceneVO getCurrentSceneVO() {
        return currentSceneVo;
    }

    public int getRootEntity() {
        return sceneLoader.getRoot();
    }
}
