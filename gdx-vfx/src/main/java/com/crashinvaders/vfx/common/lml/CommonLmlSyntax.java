package com.crashinvaders.vfx.common.lml;

import com.crashinvaders.vfx.common.lml.attributes.AdvancedColorLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.BoundsLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ButtonCheckedImageLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.CheckBoxTextSpaceLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ContainerPadBottomLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ContainerPadLeftLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ContainerPadRightLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ContainerPadTopLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.DummyBuildingLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.HorizontalGroupExpandLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.HorizontalGroupGrowLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.HorizontalGroupWrapLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ImageDrawableLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.LabelFontScaleLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.OnBackPressedLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.OnEnterPressedLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.OriginLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.PatchedOnClickLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.PositionLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.TextButtonFontScaleLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.VerticalGroupExpandLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.VerticalGroupGrowLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.VerticalGroupWrapLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.VerticalScrollSliderLmlAttribute;
import com.crashinvaders.vfx.common.lml.attributes.ZIndexLmlAttribute;
import com.crashinvaders.vfx.common.lml.tags.GroupLmlTag;
import com.crashinvaders.vfx.common.lml.tags.ShrinkContainerLmlTag;
import com.crashinvaders.vfx.common.lml.tags.TransformScalableWrapperLmlTag;
import com.github.czyzby.lml.parser.impl.DefaultLmlSyntax;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerAlignLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerFillLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerFillXLmlAttribute;
import com.github.czyzby.lml.parser.impl.attribute.container.ContainerFillYLmlAttribute;

/**
 * Extension to {@link DefaultLmlSyntax}. Adds some new/improved elements and tags.
 */
public class CommonLmlSyntax extends DefaultLmlSyntax {

    @Override
    protected void registerActorTags() {
        super.registerActorTags();

        addTagProvider(new GroupLmlTag.TagProvider(), "group");
        addTagProvider(new ShrinkContainerLmlTag.TagProvider(), "shrinkContainer");
        addTagProvider(new TransformScalableWrapperLmlTag.TagProvider(), "scaleWrapper");
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();

        registerCheckBoxAttributes();
        registerSliderAttributes();
    }

    @Override
    protected void registerCommonAttributes() {
        super.registerCommonAttributes();

        addAttributeProcessor(new OnEnterPressedLmlAttribute(), "enter", "onEnter");
        addAttributeProcessor(new OnBackPressedLmlAttribute(), "back", "onBack", "onEscape");
        addAttributeProcessor(new AdvancedColorLmlAttribute(), "color");
        addAttributeProcessor(new OriginLmlAttribute(), "origin");
        addAttributeProcessor(new ZIndexLmlAttribute(), "zIndex");
        addAttributeProcessor(new PositionLmlAttribute(), "position");
        addAttributeProcessor(new BoundsLmlAttribute(), "bounds");
        addAttributeProcessor(new PatchedOnClickLmlAttribute(), "onClick", "click");

        // SkeletonGroupLmlTag's specific attribute names that should be reserved.
        addBuildingAttributeProcessor(new DummyBuildingLmlAttribute(), "slot", "attachment");
    }

    @Override
    protected void registerContainerAttributes() {
        super.registerContainerAttributes();

        addAttributeProcessor(new ContainerPadLeftLmlAttribute(), "padLeft", "containerPadLeft");
        addAttributeProcessor(new ContainerPadRightLmlAttribute(), "padRight", "containerPadRight");
        addAttributeProcessor(new ContainerPadTopLmlAttribute(), "padTop", "containerPadTop");
        addAttributeProcessor(new ContainerPadBottomLmlAttribute(), "padBottom", "containerPadBottom");
        addAttributeProcessor(new ContainerFillLmlAttribute(), "containerFill");
        addAttributeProcessor(new ContainerFillXLmlAttribute(), "containerFillX");
        addAttributeProcessor(new ContainerFillYLmlAttribute(), "containerFillY");
        addAttributeProcessor(new ContainerAlignLmlAttribute(), "containerAlign");
    }

    @Override
    protected void registerLabelAttributes() {
        super.registerLabelAttributes();

        addAttributeProcessor(new LabelFontScaleLmlAttribute(), "fontScale");
    }

    @Override
    protected void registerButtonAttributes() {
        super.registerButtonAttributes();

        addAttributeProcessor(new TextButtonFontScaleLmlAttribute(), "fontScale");
        addAttributeProcessor(new ButtonCheckedImageLmlAttribute(), "checkedImage");
    }

    @Override
    protected void registerImageAttributes() {
        super.registerImageAttributes();

        addAttributeProcessor(new ImageDrawableLmlAttribute(), "drawable");
    }

    @Override
    protected void registerHorizontalGroupAttributes() {
        super.registerHorizontalGroupAttributes();

        addAttributeProcessor(new HorizontalGroupExpandLmlAttribute(), "expand", "groupExpand");
        addAttributeProcessor(new HorizontalGroupGrowLmlAttribute(), "grow", "groupGrow");
        addAttributeProcessor(new HorizontalGroupWrapLmlAttribute(), "wrap", "groupWrap");
    }

    @Override
    protected void registerVerticalGroupAttributes() {
        super.registerVerticalGroupAttributes();

        addAttributeProcessor(new VerticalGroupExpandLmlAttribute(), "expand", "groupExpand");
        addAttributeProcessor(new VerticalGroupGrowLmlAttribute(), "grow", "groupGrow");
        addAttributeProcessor(new VerticalGroupWrapLmlAttribute(), "wrap", "groupWrap");
    }

    protected void registerCheckBoxAttributes() {
        addAttributeProcessor(new CheckBoxTextSpaceLmlAttribute(), "textSpace");
    }

    protected void registerSliderAttributes() {
        addAttributeProcessor(new VerticalScrollSliderLmlAttribute(), "scrollerVertical", "scrollerV");
    }
}