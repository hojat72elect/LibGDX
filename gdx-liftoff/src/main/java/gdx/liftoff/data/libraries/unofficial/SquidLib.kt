@file:Suppress("unused") // Extension classes accessed via reflection.

package gdx.liftoff.data.libraries.unofficial

import gdx.liftoff.data.libraries.camelCaseToKebabCase
import gdx.liftoff.data.platforms.Core
import gdx.liftoff.data.platforms.GWT
import gdx.liftoff.data.project.Project
import gdx.liftoff.views.Extension

/**
 * Base class of SquidLib libraries.
 *  *  */
abstract class SquidLibExtension : ThirdPartyExtension() {
    override val defaultVersion = "3.0.6"
    override val group = "com.squidpony"
    override val name: String
        get() = id.camelCaseToKebabCase()
    override val url = "https://github.com/yellowstonegames/SquidLib"
}

/**
 * Base class of SquidSquad libraries.
 *  *  */
abstract class SquidSquadExtension : ThirdPartyExtension() {
    override val defaultVersion = "4.0.4"
    override val group = "com.squidpony"
    override val name: String
        get() = id.lowercase()
    override val url = "https://github.com/yellowstonegames/SquidSquad"
}

/**
 * Utilities for grid-based games.
 *  *  */
@Extension
class SquidLibUtil : SquidLibExtension() {
    override val id = "squidlibUtil"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "squidpony.squidlib-util")

        RegExodus().initiate(project)
    }
}

/**
 * Text-based display for roguelike games.
 *  *  */
@Extension
class SquidLib : SquidLibExtension() {
    override val id = "squidlib"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "squidpony.squidlib")

        SquidLibUtil().initiate(project)
        Anim8().initiate(project)
    }
}

/**
 * Extra save/load support for SquidLib objects.
 *  *  */
@Extension
class SquidLibExtra : SquidLibExtension() {
    override val id = "squidlibExtra"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "squidpony.squidlib-extra")

        SquidLibUtil().initiate(project)
    }
}

/**
 * Core utilities used by all SquidSquad extensions.
 *  *  */
@Extension
class SquidSquadCore : SquidSquadExtension() {
    override val id = "squidCore"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidcore")

        Jdkgdxds().initiate(project)
        Juniper().initiate(project)
        RegExodus().initiate(project)
    }
}

/**
 * SquidSquad's code for various 2D spatial "stuff."
 *  *  */
@Extension
class SquidSquadGrid : SquidSquadExtension() {
    override val id = "squidGrid"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidgrid")

        SquidSquadCore().initiate(project)
        Crux().initiate(project)
    }
}

/**
 * SquidSquad's code for pathfinding, often copied from simple-graphs.
 *  *  */
@Extension
class SquidSquadPath : SquidSquadExtension() {
    override val id = "squidPath"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidpath")

        SquidSquadGrid().initiate(project)
    }
}

/**
 * SquidSquad's code for pathfinding, mostly delegating to Gand.
 *  *  */
@Extension
class SquidSquadSeek : SquidSquadExtension() {
    override val id = "squidSeek"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidseek")

        SquidSquadGrid().initiate(project)
        Gand().initiate(project)
    }
}

/**
 * SquidSquad's code for walking-scale map generation; mostly dungeons.
 *  *  */
@Extension
class SquidSquadPlace : SquidSquadExtension() {
    override val id = "squidPlace"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidplace")

        SquidSquadGrid().initiate(project)
    }
}

/**
 * SquidSquad's code for smoothly changing between positions/colors/etc.
 *  *  */
@Extension
class SquidSquadSmooth : SquidSquadExtension() {
    override val id = "squidSmooth"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidsmooth")

        SquidSquadGrid().initiate(project)
    }
}

/**
 * SquidSquad's code for world- and continent-scale map generation.
 *  *  */
@Extension
class SquidSquadWorld : SquidSquadExtension() {
    override val id = "squidWorld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidworld")

        SquidSquadPlace().initiate(project)
    }
}

/**
 * SquidSquad's code for text-based display on a grid.
 *  *  */
@Extension
class SquidSquadGlyph : SquidSquadExtension() {
    override val id = "squidGlyph"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidglyph")

        SquidSquadGrid().initiate(project)
        TextraTypist().initiate(project)
    }
}

/**
 * SquidSquad's compatibility code to maintain similar output to SquidLib.
 *  *  */
@Extension
class SquidSquadOld : SquidSquadExtension() {
    override val id = "squidOld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidold")

        SquidSquadCore().initiate(project)
    }
}

/**
 * SquidSquad's code for natural language text imitation, adaptation, and/or generation.
 *  *  */
@Extension
class SquidSquadText : SquidSquadExtension() {
    override val id = "squidText"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidtext")

        SquidSquadCore().initiate(project)
    }
}

/**
 * SquidSquad's code for input handling and key rebinding.
 *  *  */
@Extension
class SquidSquadPress : SquidSquadExtension() {
    override val id = "squidPress"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidpress")

        SquidSquadCore().initiate(project)
    }
}

/**
 * SquidSquad's JSON compatibility code for SquidCore.
 *  *  */
@Extension
class SquidSquadStoreCore : SquidSquadExtension() {
    override val id = "squidStoreCore"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidstorecore")

        SquidSquadCore().initiate(project)
        JdkgdxdsInterop().initiate(project)
    }
}

/**
 * SquidSquad's JSON compatibility code for SquidGrid.
 *  *  */
@Extension
class SquidSquadStoreGrid : SquidSquadExtension() {
    override val id = "squidStoreGrid"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidstoregrid")

        SquidSquadStoreCore().initiate(project)
        SquidSquadGrid().initiate(project)
    }
}

/**
 * SquidSquad's JSON compatibility code for SquidOld.
 *  *  */
@Extension
class SquidSquadStoreOld : SquidSquadExtension() {
    override val id = "squidStoreOld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidstoreold")

        SquidSquadStoreCore().initiate(project)
        SquidSquadOld().initiate(project)
    }
}

/**
 * SquidSquad's JSON compatibility code for SquidPath.
 *  *  */
@Extension
class SquidSquadStorePath : SquidSquadExtension() {
    override val id = "squidStorePath"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidstorepath")

        SquidSquadStoreCore().initiate(project)
        SquidSquadStoreGrid().initiate(project)
        SquidSquadPath().initiate(project)
    }
}

/**
 * SquidSquad's JSON compatibility code for SquidText.
 *  *  */
@Extension
class SquidSquadStoreText : SquidSquadExtension() {
    override val id = "squidStoreText"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidstoretext")

        SquidSquadStoreCore().initiate(project)
        SquidSquadText().initiate(project)
    }
}

/**
 * SquidSquad's JSON compatibility code for SquidWorld.
 *  *  */
@Extension
class SquidSquadStoreWorld : SquidSquadExtension() {
    override val id = "squidStoreWorld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        addDependency(project, GWT.ID, "$group:$name:sources")
        addGwtInherit(project, "com.github.yellowstonegames.squidstoreworld")

        SquidSquadStoreCore().initiate(project)
        SquidSquadStoreGrid().initiate(project)
        SquidSquadWorld().initiate(project)
    }
}

/**
 * SquidSquad's Kryo compatibility code for SquidCore.
 *  *  */
@Extension
class SquidSquadFreezeCore : SquidSquadExtension() {
    override val id = "squidFreezeCore"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadCore().initiate(project)
        Kryo().initiate(project)
        KryoRegExodus().initiate(project)
        KryoDigital().initiate(project)
        KryoJuniper().initiate(project)
        KryoJdkgdxds().initiate(project)
    }
}

/**
 * SquidSquad's Kryo compatibility code for SquidGrid.
 *  *  */
@Extension
class SquidSquadFreezeGrid : SquidSquadExtension() {
    override val id = "squidFreezeGrid"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadGrid().initiate(project)
        SquidSquadFreezeCore().initiate(project)
    }
}

/**
 * SquidSquad's Kryo compatibility code for SquidOld.
 *  *  */
@Extension
class SquidSquadFreezeOld : SquidSquadExtension() {
    override val id = "squidFreezeOld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadOld().initiate(project)
        SquidSquadFreezeCore().initiate(project)
    }
}

/**
 * SquidSquad's Kryo compatibility code for SquidPath.
 *  *  */
@Extension
class SquidSquadFreezePath : SquidSquadExtension() {
    override val id = "squidFreezePath"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadPath().initiate(project)
        SquidSquadFreezeCore().initiate(project)
        SquidSquadFreezeGrid().initiate(project)
    }
}

/**
 * SquidSquad's Kryo compatibility code for SquidText.
 *  *  */
@Extension
class SquidSquadFreezeText : SquidSquadExtension() {
    override val id = "squidFreezeText"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadText().initiate(project)
        SquidSquadFreezeCore().initiate(project)
    }
}

/**
 * SquidSquad's Kryo compatibility code for SquidWorld.
 *  *  */
@Extension
class SquidSquadFreezeWorld : SquidSquadExtension() {
    override val id = "squidFreezeWorld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadWorld().initiate(project)
        SquidSquadFreezeCore().initiate(project)
        SquidSquadFreezeGrid().initiate(project)
    }
}

/**
 * SquidSquad's Fory compatibility code for SquidCore.
 *  *  */
@Extension
class SquidSquadWrathCore : SquidSquadExtension() {
    override val id = "squidWrathCore"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadCore().initiate(project)
        Fory().initiate(project)
        TantrumRegExodus().initiate(project)
        TantrumDigital().initiate(project)
        TantrumJdkgdxds().initiate(project)
    }
}

/**
 * SquidSquad's Fory compatibility code for SquidGrid.
 *  *  */
@Extension
class SquidSquadWrathGrid : SquidSquadExtension() {
    override val id = "squidWrathGrid"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadGrid().initiate(project)
        SquidSquadWrathCore().initiate(project)
    }
}

/**
 * SquidSquad's Fory compatibility code for SquidOld.
 *  *  */
@Extension
class SquidSquadWrathOld : SquidSquadExtension() {
    override val id = "squidWrathOld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadOld().initiate(project)
        SquidSquadWrathCore().initiate(project)
    }
}

/**
 * SquidSquad's Fory compatibility code for SquidPath.
 *  *  */
@Extension
class SquidSquadWrathPath : SquidSquadExtension() {
    override val id = "squidWrathPath"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadPath().initiate(project)
        SquidSquadWrathCore().initiate(project)
        SquidSquadWrathGrid().initiate(project)
    }
}

/**
 * SquidSquad's Fory compatibility code for SquidText.
 *  *  */
@Extension
class SquidSquadWrathText : SquidSquadExtension() {
    override val id = "squidWrathText"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadText().initiate(project)
        SquidSquadWrathCore().initiate(project)
    }
}

/**
 * SquidSquad's Fory compatibility code for SquidWorld.
 *  *  */
@Extension
class SquidSquadWrathWorld : SquidSquadExtension() {
    override val id = "squidWrathWorld"

    override fun initiateDependencies(project: Project) {
        addDependency(project, Core.ID, "$group:$name")

        SquidSquadWorld().initiate(project)
        SquidSquadWrathCore().initiate(project)
        SquidSquadWrathGrid().initiate(project)
    }
}
