/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
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
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2020/11/17 14:17
 * @Param:
 */
public class ShowQTM extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            // Add a dragger to enable shape dragging
            // this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));

            RenderableLayer layer = new RenderableLayer();
            layer.setName("ShowQTM");

            List<Path> paths = new ArrayList<>();

            // one facet of octant
             List<QTMTriangle> triangles = GlobalQTM.getGlobalQTMs().subList(0,1);

            // Globe of QTM
//            List<QTMTriangle> triangles = GlobalQTM.getGlobalQTMs();
            List<QTMTriangle> temp;


            int level = 4;

            for (int i = 0; i < level; i++)
            {
                temp = new ArrayList<>();

                for (QTMTriangle triangle :
                    triangles)
                {
                    temp.addAll(Arrays.asList(triangle.refine()));
                }
                triangles.clear();
                triangles.addAll(temp);
            }

            for (QTMTriangle triangle :
                triangles)
            {
                paths.addAll(Arrays.asList(triangle.renderPath()));
            }

            layer.addRenderables(paths);

            insertBeforeCompass(getWwd(),layer);

            getWwd().getView().setEyePosition(Position.fromDegrees(30,26,3*Const.RADIUS));

            getWwd().redrawNow();


        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("ShowQTM", ShowQTM.AppFrame.class);
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
