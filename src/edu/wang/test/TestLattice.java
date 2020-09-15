/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.test;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwindx.examples.*;

import java.awt.*;

public class TestLattice extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            // Add the graticule layer
            insertBeforeCompass(getWwd(), compareGreatCircle());
            insertBeforePlacenames(getWwd(), new LatLonGraticuleLayer());
        }

        private RenderableLayer compareGreatCircle()
        {
            Position p1, p2, p3, p4, p5, p6, p7, p8, p9;
            p1 = Position.fromDegrees(20, 0);
            p2 = Position.fromDegrees(20, 50);
            p3 = Position.fromDegrees(20, 90);
            p4 = Position.fromDegrees(50, 0);
            p5 = Position.fromDegrees(50, 50);
            p6 = Position.fromDegrees(50, 90);
            p7 = Position.fromDegrees(80, 0);
            p8 = Position.fromDegrees(80, 50);
            p9 = Position.fromDegrees(80, 90);
            Path path1 = getGreatCirclePath(p1, p2);
            Path path2 = getGreatCirclePath(p1, p3);
            Path path3 = getGreatCirclePath(p4, p5);
            Path path4 = getGreatCirclePath(p4, p6);
            Path path5 = getGreatCirclePath(p7, p8);
            Path path6 = getGreatCirclePath(p7, p9);
            Path path7 = getSmallCirclePath(p1, p4);
            Path path8 = getSmallCirclePath(p1, p7);
            Path path9 = getSmallCirclePath(p2, p5);
            Path path10 = getSmallCirclePath(p2, p8);

            RenderableLayer layer = new RenderableLayer();
            layer.addRenderable(path1);
            layer.addRenderable(path2);
            layer.addRenderable(path3);
            layer.addRenderable(path4);
            layer.addRenderable(path5);
            layer.addRenderable(path6);
            layer.addRenderable(path7);
            layer.addRenderable(path8);
            layer.addRenderable(path9);
            layer.addRenderable(path10);
            return layer;
        }

        private Path getSmallCirclePath(Position p1, Position p2)
        {
            Path path = new Path(p1, p2);
            path.setPathType(AVKey.LOXODROME);
            BasicShapeAttributes shapeAttributes = new BasicShapeAttributes();
            shapeAttributes.setOutlineMaterial(new Material(Color.YELLOW));
            shapeAttributes.setInteriorMaterial(new Material(Color.YELLOW));
            path.setAttributes(shapeAttributes);
            path.setFollowTerrain(true);
//            path.setHighlighted(true);
//            path.setVisible(true);
            return path;
        }

        private Path getGreatCirclePath(Position pos1, Position pos2)
        {
            Path path = new Path(pos1, pos2);
            path.setPathType(AVKey.GREAT_CIRCLE);
            BasicShapeAttributes shapeAttributes = new BasicShapeAttributes();
            shapeAttributes.setOutlineMaterial(new Material(Color.RED));
            shapeAttributes.setInteriorMaterial(new Material(Color.RED));
            path.setAttributes(shapeAttributes);
            path.setFollowTerrain(true);
//            path.setHighlighted(true);
//            path.setVisible(true);
            return path;
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Lat-Lon Graticule", TestLattice.AppFrame.class);
    }
}
