package games.rednblack.editor.data.migrations.migrators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import games.rednblack.editor.data.migrations.IVersionMigrator;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.h2d.common.vo.ProjectVO;
import games.rednblack.puremvc.Facade;

public class VersionMigTo005 implements IVersionMigrator {

    private String projectPath;

    private final Json json = new Json();
    private final JsonReader jsonReader = new JsonReader();
    private Facade facade;
    private ProjectManager projectManager;

    @Override
    public void setProject(String path, ProjectVO vo, ProjectInfoVO projectInfoVO) {
        facade = Facade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        projectPath = path;
        json.setOutputType(JsonWriter.OutputType.json);
    }

    @Override
    public boolean doMigration() {
        // Rename folder animations to spine-animations in orig (if exist);
        File animationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "animations");
        if (animationsDir.exists() && animationsDir.isDirectory()) {
            File spineAnimationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "spine-animations");
            animationsDir.renameTo(spineAnimationsDir);
        }

        // get list of resolutions
        String prjInfoFilePath = projectPath + "/project.dt";
        FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
        String projectInfoContents = null;
        try {
            projectInfoContents = FileUtils.readFileToString(projectInfoFile.file(), "utf-8");
            ProjectInfoVO currentProjectInfoVO = json.fromJson(ProjectInfoVO.class, projectInfoContents);
            projectManager.currentProjectInfoVO = currentProjectInfoVO;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // change sLights to sLights
        File scenesDir = new File(projectPath + File.separator + "scenes");
        for (File entry : scenesDir.listFiles()) {
            if (!entry.isDirectory()) {
                try {
                    String content = FileUtils.readFileToString(new FileHandle(entry).file(), "utf-8");
                    content = content.replaceAll("\"slights\":", "\"sLights\":");
                    FileUtils.writeStringToFile(new File(entry.getAbsolutePath()), content, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
