package com.wow.carlauncher.view.activity.launcher;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.DrawerTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTransformer;
import com.wow.carlauncher.view.activity.set.setItem.SetEnum;

public enum ItemTransformer implements SetEnum {
    None("默认动画", 0),
    BackgroundToForeground("BackgroundToForeground", 1),
    Accordion("Accordion", 2),
    CubeIn("CubeIn", 3),
    CubeOut("CubeOut", 4),
    DepthPage("DepthPage", 5),
    Drawer("Drawer", 6),
    FlipHorizontal("FlipHorizontal", 7),
    FlipVertical("FlipVertical", 8),
    ForegroundToBackground("ForegroundToBackground", 9),
    RotateDown("RotateDown", 10),
    RotateUp("RotateUp", 11),
    ScaleInOut("ScaleInOut", 12),
    Stack("Stack", 13),
    Tablet("Tablet", 14),
    ZoomIn("ZoomIn", 15),
    ZoomOutSlide("ZoomOutSlide", 16),
    ZoomOut("ZoomOut", 17);


    private String name;
    private Integer id;

    ItemTransformer(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ABaseTransformer getTransformer() {
        switch (this) {
            case BackgroundToForeground:
                return new BackgroundToForegroundTransformer();
            case Accordion:
                return new AccordionTransformer();
            case CubeIn:
                return new CubeInTransformer();
            case CubeOut:
                return new CubeOutTransformer();
            case DepthPage:
                return new DepthPageTransformer();
            case Drawer:
                return new DrawerTransformer();
            case FlipHorizontal:
                return new FlipHorizontalTransformer();
            case FlipVertical:
                return new FlipVerticalTransformer();
            case ForegroundToBackground:
                return new ForegroundToBackgroundTransformer();
            case RotateDown:
                return new RotateDownTransformer();
            case RotateUp:
                return new RotateUpTransformer();
            case ScaleInOut:
                return new ScaleInOutTransformer();
            case Stack:
                return new StackTransformer();
            case Tablet:
                return new TabletTransformer();
            case ZoomIn:
                return new ZoomInTransformer();
            case ZoomOutSlide:
                return new ZoomOutSlideTransformer();
            case ZoomOut:
                return new ZoomOutTransformer();
        }
        return new DefaultTransformer();
    }

    public static ItemTransformer getById(Integer id) {
        switch (id) {
            case 1:
                return BackgroundToForeground;
            case 2:
                return Accordion;
            case 3:
                return CubeIn;
            case 4:
                return CubeOut;
            case 5:
                return DepthPage;
            case 6:
                return Drawer;
            case 7:
                return FlipHorizontal;
            case 8:
                return FlipVertical;
            case 9:
                return ForegroundToBackground;
            case 10:
                return RotateDown;
            case 11:
                return RotateUp;
            case 12:
                return ScaleInOut;
            case 13:
                return Stack;
            case 14:
                return Tablet;
            case 15:
                return ZoomIn;
            case 16:
                return ZoomOutSlide;
            case 17:
                return ZoomOut;
        }
        return None;
    }
}
