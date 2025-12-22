package games.rednblack.editor.data.migrations.migrators;

import games.rednblack.editor.data.migrations.IVersionMigrator;
import games.rednblack.editor.renderer.data.ProjectInfoVO;
import games.rednblack.h2d.common.vo.ProjectVO;

public class DummyMig implements IVersionMigrator {
    @Override
    public void setProject(String path, ProjectVO vo, ProjectInfoVO projectInfoVO) {

    }

    @Override
    public boolean doMigration() {
        return true;
    }
}
