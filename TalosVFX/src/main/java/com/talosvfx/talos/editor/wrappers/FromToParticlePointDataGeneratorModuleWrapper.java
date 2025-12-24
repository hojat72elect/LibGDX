
package com.talosvfx.talos.editor.wrappers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.talosvfx.talos.editor.notifications.Notifications;
import com.talosvfx.talos.editor.notifications.events.deprecatedparticles.RegisterDragPoints;
import com.talosvfx.talos.editor.notifications.events.deprecatedparticles.UnRegisterDragPoints;
import com.talosvfx.talos.editor.widgets.ui.DragPoint;
import com.talosvfx.talos.editor.widgets.ui.common.zoomWidgets.TextFieldWithZoom;
import com.talosvfx.talos.runtime.vfx.Slot;
import com.talosvfx.talos.runtime.vfx.modules.AbstractModule;
import com.talosvfx.talos.runtime.vfx.modules.FromToParticlePointDataGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.ParticlePointDataGeneratorModule;
import com.talosvfx.talos.runtime.vfx.modules.StaticValueModule;


public class FromToParticlePointDataGeneratorModuleWrapper extends ModuleWrapper<FromToParticlePointDataGeneratorModule> implements IDragPointProvider {

    private DragPoint dragPointFrom;
    private DragPoint dragPointTo;

    private TextFieldWithZoom numPointsTextField;

    public FromToParticlePointDataGeneratorModuleWrapper() {
        super();
    }

    @Override
    public void setModule(FromToParticlePointDataGeneratorModule module) {
        super.setModule(module);

        numPointsTextField.setText(module.getNumPoints() + "");
        module.setDefaults(dragPointFrom.position, dragPointTo.position);
    }

    @Override
    protected float reportPrefWidth() {
        return 250;
    }

    @Override
    protected void configureSlots() {
        addOutputSlot("from-to", ParticlePointDataGeneratorModule.MODULE);

        addInputSlot("from", FromToParticlePointDataGeneratorModule.FROM);
        addInputSlot("to", FromToParticlePointDataGeneratorModule.TO);

        numPointsTextField = addInputSlotWithTextField("num points", FromToParticlePointDataGeneratorModule.POINTS_COUNT);
        numPointsTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float numPoints = floatFromText(numPointsTextField);
                module.setNumPoints(numPoints);
            }
        });
        numPointsTextField.setText(FromToParticlePointDataGeneratorModule.defaultPoints + "");

        dragPointFrom = new DragPoint(0, 0);
        dragPointTo = new DragPoint(1, 0);
    }

    @Override
    public Class<? extends AbstractModule> getSlotsPreferredModule(Slot slot) {

//        if (slot.getIndex() == FromToParticlePointDataGeneratorModule.FROM) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();;
//        if (slot.getIndex() == FromToParticlePointDataGeneratorModule.TO) return TalosMain.Instance().UIStage().getPreferred3DVectorClass();;
        if (slot.getIndex() == FromToParticlePointDataGeneratorModule.POINTS_COUNT) return StaticValueModule.class;

        return null;
    }

    @Override
    protected void wrapperSelected() {
        RegisterDragPoints registerDragPoints = Notifications.obtainEvent(RegisterDragPoints.class);
        registerDragPoints.setRegisterForDragPoints(this);
        Notifications.fireEvent(registerDragPoints);
    }

    @Override
    protected void wrapperDeselected() {
        UnRegisterDragPoints unregisterDragPoints = Notifications.obtainEvent(UnRegisterDragPoints.class);
        unregisterDragPoints.setUnRegisterForDragPoints(this);
        Notifications.fireEvent(unregisterDragPoints);
    }

    @Override
    public DragPoint[] fetchDragPoints() {
        return new DragPoint[]{dragPointFrom, dragPointTo};
    }

    @Override
    public void dragPointChanged(DragPoint point) {
        if (point == dragPointFrom) {
            module.setDefaults(dragPointFrom.position, dragPointTo.position);
        }

        if (point == dragPointTo) {
            module.setDefaults(dragPointFrom.position, dragPointTo.position);
        }
    }
}
