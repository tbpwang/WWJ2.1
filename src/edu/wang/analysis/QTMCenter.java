/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.Geocode;
import edu.wang.io.IO;
import edu.wang.model.QTMTriangle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.util.*;

public class QTMCenter extends ApplicationTemplate
{
    public static class TAppFrame extends ApplicationTemplate.AppFrame
    {
        public TAppFrame()
        {
            int sub = 3;
            List<QTMTriangle> triangles = initTriangles(sub);
            RenderableLayer layerTri = new RenderableLayer();
            List<LatLon> centers = new ArrayList<>();
//            assert triangles != null;
            for (QTMTriangle triangle : triangles)
            {
                centers.add(triangle.getCenter());
                Path[] edges = triangle.renderPath();
                layerTri.addRenderables(Arrays.asList(edges));
            }

            for (LatLon ll :
                centers)
            {
                String string = IO.formatDouble(ll.latitude.degrees,3) + "," + IO.formatDouble(ll.longitude.degrees,3);
                IO.write("QTMCenters", "CerterPoints" + sub, string);
            }
            insertBeforeCompass(getWwd(), layerTri);
        }

        static List<QTMTriangle> initTriangles(int sub)
        {
            LatLon p1 = LatLon.fromDegrees(90, 0);
            LatLon p2 = LatLon.fromDegrees(0, 0);
            LatLon p3 = LatLon.fromDegrees(0, 90);
            QTMTriangle triangle = new QTMTriangle(p1, p2, p3, new Geocode("A"));
            List<QTMTriangle> QTMTriangles = new ArrayList<>();
            QTMTriangles.add(triangle);
            for (int i = 0; i < sub; i++)
            {
                List<QTMTriangle> tempTriangles = new ArrayList<>();
                for (QTMTriangle tri :
                    QTMTriangles)
                {
                    tempTriangles.addAll(Arrays.asList(tri.refine()));
                }
                QTMTriangles.clear();
                QTMTriangles.addAll(tempTriangles);
            }

//            triangle.refine()
            return QTMTriangles;
        }
    }

    public static void main(String[] args)
    {
        start("Subdivision", TAppFrame.class);
    }
}
