/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.display;

import edu.wang.io.Const;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @Author: Joel Wang
 * @Time: 2021/3/28 10:14
 * @Param: 用于正六面体形成的剖分，也许用于数据立方体，基本单元为“正方形”
 */
public class CubeSubdivision extends ApplicationTemplate
{
    public static class CubeFrame extends ApplicationTemplate.AppFrame
    {
        public CubeFrame()
        {
            super(true, true, true);
            Color color = Color.ORANGE;
            RenderableLayer layer1 = cubePathsLayer(color);
            RenderableLayer layer2 = cube1(color);

            insertBeforeCompass(getWwd(), layer1);
            insertBeforeCompass(getWwd(), layer2);
        }

        private static RenderableLayer cubePathsLayer(Color color)
        {
            RenderableLayer layer = new RenderableLayer();

            LatLon p01, p02, p03, p00, p11, p12, p13, p10;
            p00 = LatLon.fromDegrees(45, -90);
            p01 = LatLon.fromDegrees(45, 0);
            p02 = LatLon.fromDegrees(45, 90);
            p03 = LatLon.fromDegrees(45, 180);

            p10 = LatLon.fromDegrees(-45, -90);
            p11 = LatLon.fromDegrees(-45, 0);
            p12 = LatLon.fromDegrees(-45, 90);
            p13 = LatLon.fromDegrees(-45, 180);

            Path path0 = new Path(new Position(p00, 0), new Position(p10, 0));
            Path path1 = new Path(new Position(p01, 0), new Position(p11, 0));
            Path path2 = new Path(new Position(p02, 0), new Position(p12, 0));
            Path path3 = new Path(new Position(p03, 0), new Position(p13, 0));
            Path path4 = new Path(new Position(p00, 0), new Position(p01, 0));
            Path path5 = new Path(new Position(p01, 0), new Position(p02, 0));
            Path path6 = new Path(new Position(p02, 0), new Position(p03, 0));
            Path path7 = new Path(new Position(p03, 0), new Position(p00, 0));
            Path path8 = new Path(new Position(p10, 0), new Position(p11, 0));
            Path path9 = new Path(new Position(p11, 0), new Position(p12, 0));
            Path path10 = new Path(new Position(p12, 0), new Position(p13, 0));
            Path path11 = new Path(new Position(p13, 0), new Position(p10, 0));

//            Color color = Color.ORANGE;
//            Color color = new Color(250,250,250);
//            Color color = new Color(237, 177, 32);
            path0.setAttributes(Const.defaultPathAttribute(color));
            path1.setAttributes(Const.defaultPathAttribute(color));
            path2.setAttributes(Const.defaultPathAttribute(color));
            path3.setAttributes(Const.defaultPathAttribute(color));
            path4.setAttributes(Const.defaultPathAttribute(color));
            path5.setAttributes(Const.defaultPathAttribute(color));
            path6.setAttributes(Const.defaultPathAttribute(color));
            path7.setAttributes(Const.defaultPathAttribute(color));
            path8.setAttributes(Const.defaultPathAttribute(color));
            path9.setAttributes(Const.defaultPathAttribute(color));
            path10.setAttributes(Const.defaultPathAttribute(color));
            path11.setAttributes(Const.defaultPathAttribute(color));

            path0.setPathType(AVKey.GREAT_CIRCLE);
            path1.setPathType(AVKey.GREAT_CIRCLE);
            path2.setPathType(AVKey.GREAT_CIRCLE);
            path3.setPathType(AVKey.GREAT_CIRCLE);
            path4.setPathType(AVKey.GREAT_CIRCLE);
            path5.setPathType(AVKey.GREAT_CIRCLE);
            path6.setPathType(AVKey.GREAT_CIRCLE);
            path7.setPathType(AVKey.GREAT_CIRCLE);
            path8.setPathType(AVKey.GREAT_CIRCLE);
            path9.setPathType(AVKey.GREAT_CIRCLE);
            path10.setPathType(AVKey.GREAT_CIRCLE);
            path11.setPathType(AVKey.GREAT_CIRCLE);

            List<Path> paths = new ArrayList<>();
            paths.add(path0);
            paths.add(path1);
            paths.add(path2);
            paths.add(path3);
            paths.add(path4);
            paths.add(path5);
            paths.add(path6);
            paths.add(path7);
            paths.add(path8);
            paths.add(path9);
            paths.add(path10);
            paths.add(path11);

            layer.addRenderables(paths);
            layer.setName("cubePaths");

            return layer;
        }

        private static RenderableLayer cube(ShapeAttributes attributes)
        {
            LatLon p01, p02, p03, p00, p11, p12, p13, p10;
            p00 = LatLon.fromDegrees(45, -90);
            p01 = LatLon.fromDegrees(45, 0);
            p02 = LatLon.fromDegrees(45, 90);
            p03 = LatLon.fromDegrees(45, 180);

            p10 = LatLon.fromDegrees(-45, -90);
            p11 = LatLon.fromDegrees(-45, 0);
            p12 = LatLon.fromDegrees(-45, 90);
            p13 = LatLon.fromDegrees(-45, 180);

            //LatLon.getCenter()
            return null;
        }

        private static RenderableLayer cube1(Color color)
        {
            RenderableLayer layer = new RenderableLayer();
            layer.setName("cubeLayers");
            List<SurfaceQuad> quads = new ArrayList<>();

            LatLon p01, p02, p03, p00, p11, p12, p13, p10;
            p00 = LatLon.fromDegrees(45, -90);
            p01 = LatLon.fromDegrees(45, 0);
            p02 = LatLon.fromDegrees(45, 90);
            p03 = LatLon.fromDegrees(45, 180);

            p10 = LatLon.fromDegrees(-45, -90);
            p11 = LatLon.fromDegrees(-45, 0);
            p12 = LatLon.fromDegrees(-45, 90);
            p13 = LatLon.fromDegrees(-45, 180);

            double size0 = Const.RADIUS * Angle.fromDegrees(45).cos() * Math.PI / 2;
            double size1 = Const.RADIUS * Math.PI / 2;

            SurfaceSquare square0 = new SurfaceSquare(LatLon.fromDegrees(90, 0), size0, Angle.fromDegrees(45));
            quads.add(square0);

            SurfaceSquare square1 = new SurfaceSquare(LatLon.fromDegrees(0, -45), size1);
            quads.add(square1);

            SurfaceSquare square5 = new SurfaceSquare(LatLon.fromDegrees(-90, 0), size0, Angle.fromDegrees(45));
            quads.add(square5);

            layer.addRenderables(quads);
            return layer;
        }
    }

    public static void main(String[] args)
    {
        start("CubeSubdivision", CubeFrame.class);
    }
}
