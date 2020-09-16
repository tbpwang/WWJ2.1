/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.display;

import edu.wang.io.Const;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/11/4
 * @description 图形显示大圆弧和小圆弧的差异
 */
public class GreatAndSmallCircle extends ApplicationTemplate
{
    public static void main(String[] args)
    {
        start("GreatAndSmallCircleShow", TestAPP.class);
    }

    public static class TestAPP extends ApplicationTemplate.AppFrame
    {
        public TestAPP()
        {
            Position p1 = Position.fromDegrees(10, 0);
            Position p2 = Position.fromDegrees(10, 70);
            Position p3 = Position.fromDegrees(70, 0);
            Position p4 = Position.fromDegrees(70, 70);
            Position p5 = Position.fromDegrees(45, 0);
            Position p6 = Position.fromDegrees(45, 70);
            Position p7 = Position.fromDegrees(-30, 0);
            Position p8 = Position.fromDegrees(-30, 70);

            List<RenderableLayer> layers = new ArrayList<>();
            layers.add(setLayer(new Path(p1, p2), AVKey.GREAT_CIRCLE, "p1p2GC"));
            layers.add(setLayer(new Path(p1, p2), AVKey.RHUMB_LINE, "p1p2RL"));
            layers.add(setLayer(new Path(p1, p2), AVKey.LINEAR, "p1p2L"));
            layers.add(setLayer(new Path(p3, p4), AVKey.GREAT_CIRCLE, "p3p4GC"));
            layers.add(setLayer(new Path(p3, p4), AVKey.RHUMB_LINE, "p3p4RL"));
            layers.add(setLayer(new Path(p3, p4), AVKey.LINEAR, "p3p4L"));
            layers.add(setLayer(new Path(p5, p6), AVKey.GREAT_CIRCLE, "p5p6GC"));
            layers.add(setLayer(new Path(p5, p6), AVKey.RHUMB_LINE, "p5p6RL"));
            layers.add(setLayer(new Path(p5, p6), AVKey.LINEAR, "p5p6L"));
            layers.add(setLayer(new Path(p1, p4), AVKey.GREAT_CIRCLE, "p1p4GC"));
            layers.add(setLayer(new Path(p1, p4), AVKey.RHUMB_LINE, "p1p4RL"));
            layers.add(setLayer(new Path(p1, p4), AVKey.LINEAR, "p1p4L"));
            layers.add(setLayer(new Path(p1, p8), AVKey.GREAT_CIRCLE, "p1p8GC"));
            layers.add(setLayer(new Path(p1, p8), AVKey.RHUMB_LINE, "p1p8RL"));
            layers.add(setLayer(new Path(p1, p8), AVKey.LINEAR, "p1p8L"));
            layers.add(setLayer(new Path(p7, p8), AVKey.GREAT_CIRCLE, "p7p8RL"));
            layers.add(setLayer(new Path(p7, p8), AVKey.RHUMB_LINE, "p7p8RL"));
            layers.add(setLayer(new Path(p7, p8), AVKey.LINEAR, "p7p8L"));

            for (RenderableLayer layer :
                layers)
            {
                insertBeforeCompass(getWwd(), layer);
            }
        }

        private static RenderableLayer setLayer(Path path, String type, String name)
        {
            RenderableLayer layer = new RenderableLayer();
            path.setPathType(type);
            if (type == AVKey.RHUMB_LINE)
            {
                path.setAttributes(Const.defaultPathAttribute(new Color(180, 0, 0)));
            }
            else if (type == AVKey.LINEAR)
            {
                path.setAttributes(Const.defaultPathAttribute(new Color(0,0,180)));
            }
            else
            {
                path.setAttributes(Const.defaultPathAttribute());
            }
            layer.addRenderable(path);
            layer.setName(name);
            return layer;
        }
    }
}
