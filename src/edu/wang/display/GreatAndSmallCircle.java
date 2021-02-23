/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.display;

import edu.wang.io.Const;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import javafx.geometry.Pos;

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
            List<RenderableLayer> layers = new ArrayList<>();
            Position p1 = Position.fromDegrees(48.59, -180);
            Position p2 = Position.fromDegrees(48.59, 0);
            Position p3 = Position.fromDegrees(48.59, 180);
            List<Position> positions = new ArrayList<>();
            positions.add(p1);
            positions.add(p2);
            positions.add(p3);


            SurfacePolygon polygon = new SurfacePolygon();
            polygon.setLocations(positions);
//            polygon.setAttributes(Const.defaultPolygonAttribute(0.8, Color.white));
//            polygon.setAttributes(Const.defaultPolygonAttribute(0.5, new Color(0,0,177)));
            polygon.setAttributes(Const.defaultPolygonAttribute(0.7));
//            polygon.setAttributes(Const.defaultPolygonAttribute(1, Color.darkGray));
            polygon.setOuterBoundary(positions);
            RenderableLayer layer = new RenderableLayer();
            layer.addRenderable(polygon);
            layers.add(layer);

            Position top = Position.fromDegrees(90, 140);
            Position left = Position.fromDegrees(0, 140);
            Position right = Position.fromDegrees(0, -130);
            List<Position> triangleEdges = new ArrayList<>();
            triangleEdges.add(top);
            triangleEdges.add(left);
            triangleEdges.add(right);
            SurfacePolygon triangle = new SurfacePolygon();
            triangle.setLocations(triangleEdges);
            triangle.setAttributes(Const.defaultPolygonAttribute(0.5));
            RenderableLayer layer1 = new RenderableLayer();
            layer1.addRenderable(triangle);
            layers.add(layer1);
//
            for (RenderableLayer lyr :
                layers)
            {
                insertBeforeCompass(getWwd(), lyr);
            }
            insertBeforeCompass(getWwd(),new LatLonGraticuleLayer());
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
                path.setAttributes(Const.defaultPathAttribute(new Color(0, 0, 180)));
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
