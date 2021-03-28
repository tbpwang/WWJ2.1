/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.display;

import edu.wang.Geocode;
import edu.wang.io.Const;
import edu.wang.model.*;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @Author: Joel Wang
 * @Time: 2021/3/16 10:38
 * @Param:
 * @Function: 微分法，计算QTM面积示意图
 */
public class ShowAQTMCell extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            RenderableLayer layer = new RenderableLayer();
            layer.setName("ShowAQTMCell");

            List<Path> paths = new ArrayList<>();

            // one facet of octant
            LatLon top = LatLon.fromDegrees(0,45);
            LatLon left = LatLon.fromDegrees(45,0);
            LatLon left1 = LatLon.fromDegrees(45,9);
            LatLon left2 = LatLon.fromDegrees(45,18);
            LatLon left3 = LatLon.fromDegrees(45,27);
            LatLon left4 = LatLon.fromDegrees(45,36);
            LatLon left5 = LatLon.fromDegrees(45,45);
            LatLon left6 = LatLon.fromDegrees(45,54);
            LatLon left7 = LatLon.fromDegrees(45,63);
            LatLon left8 = LatLon.fromDegrees(45,72);
            LatLon left9 = LatLon.fromDegrees(45,81);
            LatLon right = LatLon.fromDegrees(45,90);
            QTMTriangle triangle = new QTMTriangle(top,left,right,"");
//            QTMTriangle triangle1 = new QTMTriangle(top,left,left1,"");
            QTMTriangle triangle2 = new QTMTriangle(top,left1,left2,"");
            QTMTriangle triangle3 = new QTMTriangle(top,left2,left3,"");
            QTMTriangle triangle4 = new QTMTriangle(top,left3,left4,"");
            QTMTriangle triangle5 = new QTMTriangle(top,left4,left5,"");
            QTMTriangle triangle6 = new QTMTriangle(top,left5,left6,"");
            QTMTriangle triangle7 = new QTMTriangle(top,left6,left7,"");
            QTMTriangle triangle8 = new QTMTriangle(top,left7,left8,"");
            QTMTriangle triangle9 = new QTMTriangle(top,left8,left9,"");
//            QTMTriangle triangle10 = new QTMTriangle(top,left9,right,"");

            Path path = new Path();
            List<Position> positions = new ArrayList<>();
            positions.add(new Position(left,0));
            positions.add(new Position(left1,0));
            positions.add(new Position(left2,0));
            positions.add(new Position(left3,0));
            positions.add(new Position(left4,0));
            positions.add(new Position(left5,0));
            positions.add(new Position(left6,0));
            positions.add(new Position(left7,0));
            positions.add(new Position(left8,0));
            positions.add(new Position(left9,0));
            positions.add(new Position(right,0));
            path.setPositions(positions);
            path.setPathType(AVKey.GREAT_CIRCLE);
//            Color color = new Color(244,177,131);
            Color color = new Color(108,165,217);
            path.setAttributes(Const.defaultPathAttribute());
            RenderableLayer layer1 = new RenderableLayer();
            layer1.addRenderable(path);
            insertBeforeCompass(getWwd(),layer1);

//            List<QTMTriangle> triangles = GlobalQTM.getGlobalQTMs().subList(0,1);
            List<QTMTriangle> triangles = new ArrayList<>();
            triangles.add(triangle);
            triangles.add(triangle2);
            triangles.add(triangle3);
            triangles.add(triangle4);
            triangles.add(triangle5);
            triangles.add(triangle6);
            triangles.add(triangle7);
            triangles.add(triangle8);
            triangles.add(triangle9);

            for (QTMTriangle atri :
                triangles)
            {
                paths.addAll(Arrays.asList(atri.renderPath()));
            }



            layer.addRenderables(paths);

            insertBeforeCompass(getWwd(),layer);

            getWwd().getView().setEyePosition(Position.fromDegrees(triangle.getCenter().latitude.degrees,triangle.getCenter().longitude.degrees,3* Const.RADIUS));

            getWwd().redrawNow();


        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("ShowAQTMCell", ShowAQTMCell.AppFrame.class);
    }


    private static RenderableLayer initQTMLayer(LatLon A, LatLon B, LatLon C, Geocode id)
    {
        QTMTriangle qtmTriangle = new QTMTriangle(A, B, C, id);

        Path[] edges = qtmTriangle.renderPath();
        RenderableLayer layer = new RenderableLayer();
        layer.addRenderables(Arrays.asList(edges));

        return layer;
    }

    private static RenderableLayer sphericalPolygon(LatLon A, LatLon B, LatLon C, LatLon D)
    {
        List<Position> pathPositions = new ArrayList<>();
        double elevation = Const.RADIUS;
        pathPositions.add(new Position(A, elevation));
        pathPositions.add(new Position(B, elevation));
        pathPositions.add(new Position(C, elevation));
        pathPositions.add(new Position(D, elevation));
        Polygon polygon = new Polygon(pathPositions);
        polygon.addInnerBoundary(pathPositions);
        polygon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);

        // Create and set an attribute bundle.
        ShapeAttributes normalAttributes = new BasicShapeAttributes();
        normalAttributes.setInteriorMaterial(Material.YELLOW);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setInteriorOpacity(0.8);
        normalAttributes.setOutlineMaterial(Material.GREEN);
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setDrawOutline(true);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setEnableLighting(true);

        ShapeAttributes highlightAttributes = new BasicShapeAttributes(normalAttributes);
        highlightAttributes.setOutlineMaterial(Material.WHITE);
        highlightAttributes.setOutlineOpacity(1);

        polygon.setAttributes(normalAttributes);
        polygon.setHighlightAttributes(highlightAttributes);
        polygon.setValue(AVKey.DISPLAY_NAME, "QTM");

        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(polygon);

        return layer;
    }

}
