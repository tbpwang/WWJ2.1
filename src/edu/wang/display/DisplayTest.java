/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.display;

import edu.wang.*;
import edu.wang.analysis.EssentialVariable2;
import edu.wang.impl.IcosahedronInscribed;
import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwindx.examples.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Zheng WANG
 * @create 2019/5/7
 * @description 显示图形，测试程序
 * @parameter
 */
public class DisplayTest extends ApplicationTemplate
{
    public static void main(String[] args)
    {
        start("TestShow", TestShowAPP.class);
    }

    public static class TestShowAPP extends ApplicationTemplate.AppFrame
    {
        public TestShowAPP()
        {
//            int level = 3;
////            LatLon p1 = LatLon.fromDegrees(90, 90);
////            LatLon p2 = LatLon.fromDegrees(0, 0);
////            LatLon p3 = LatLon.fromDegrees(0, 90);
////            List<LatLon> list = new ArrayList<>();
////            list.add(p1);
////            list.add(p2);
////            list.add(p3);
////            ZhaoQTM initCell = new ZhaoQTM(p1, p2, p3, new Geocode("A"));
////            getWwd().getView().setEyePosition(
////                new Position(Position.getCenter(list), DGGS.getGlobe().getRadius() * 1.8));
////
////            Mesh mesh = fill(initCell, level);
////            RenderableLayer layer = new RenderableLayer();
////            layer.setName("ZhaoQTM");
////            for (List<Mesh.Node> nodeList : mesh.getNodes())
////            {
////                for (Mesh.Node node : nodeList)
////                {
////                    ZhaoQTM cell = (ZhaoQTM) node.baseTriangle();
////                    for (Path path : cell.renderPath())
////                    {
////                        layer.addRenderable(path);
////                    }
////                }
////            }
////            insertBeforeCompass(getWwd(), layer);
//
//            List<SphericalTriangleOctahedron> initTriangles = new ArrayList<>();
//            initTriangles.add(SphericalTriangleOctahedron.first);
//            initTriangles.add(SphericalTriangleOctahedron.second);
//            initTriangles.add(SphericalTriangleOctahedron.third);
//            initTriangles.add(SphericalTriangleOctahedron.fourth);
//            initTriangles.add(SphericalTriangleOctahedron.fifth);
//            initTriangles.add(SphericalTriangleOctahedron.sixth);
//
//            RenderableLayer lyr;
//            for (int i = 0; i < initTriangles.size(); i++)
//            {
//                lyr = new RenderableLayer();
//                lyr.setName("MiddleArcTri" + i);
////                MiddleArcTriangle triangle = new MiddleArcTriangle(p1, p2, p3, new Geocode("M"));
//                MiddleArcSurfaceTriangle triangle = new MiddleArcSurfaceTriangle(
//                    initTriangles.get(i).baseTriangle().getTop(), initTriangles.get(i).baseTriangle().getLeft(),
//                    initTriangles.get(i).baseTriangle().getRight(), new Geocode());
//                Mesh triMesh = fill(triangle, level);
//
//                for (Mesh.Node node : triMesh.getNodes())
//                {
//                    MiddleArcSurfaceTriangle cell = (MiddleArcSurfaceTriangle) node.getCell();
////                        for (Path path : cell.renderPath(setAttribute(i * 2)))
//                    for (Path path : cell.renderPath(setAttribute((i + 1) * 20, (i + 1) * 120, (i + 1) * 20)))
//                    {
//                        lyr.addRenderable(path);
//                    }
//                }
//
//                insertBeforeCompass(getWwd(), lyr);
//            }

            for (RenderableLayer layer :
                displaceIcosahedron())
            {
                insertBeforeCompass(getWwd(), layer);
            }
//            insertBeforeCompass(getWwd(),displaceIcosahedron());
        }


        private List<RenderableLayer> displaceIcosahedron()
        {
            IcosahedronInscribed icosahedron = IcosahedronInscribed.getInstance();
            Triangle[] facets = icosahedron.getFacets();
            List<RenderableLayer> layers = new ArrayList<>();
//            layer.setName("Icosahedron");
//            List<Position> positions = new ArrayList<>();
            List<Position> positions;
            List<Path> pathList = new ArrayList<>();
            List<SurfacePolygon> surfaces = new ArrayList<>();
            List<LatLon> centerOfMass = new ArrayList<>();
            for (Triangle t : facets)
            {
                positions = new ArrayList<>();
                positions.add(new Position(IO.vec4ToLatLon(t.getA()), 0.0));
                positions.add(new Position(IO.vec4ToLatLon(t.getB()), 0.0));
                positions.add(new Position(IO.vec4ToLatLon(t.getC()), 0.0));
                positions.add(new Position(IO.vec4ToLatLon(t.getA()), 0.0));
                List<Vec4> vertexes = new ArrayList<>();
                vertexes.add(t.getA());
                vertexes.add(t.getB());
                vertexes.add(t.getC());
//                Vec4.computeAveragePoint(vertexes);
                centerOfMass.add(IO.vec4ToLatLon(Vec4.computeAveragePoint(vertexes)));

//                pathList.add(new Path(positions));
                surfaces.add(new SurfacePolygon(positions));
            }
//            Path path = new Path(positions);
//            path.setDrawVerticals(true);
//            path.setPathType(AVKey.GREAT_CIRCLE);
//            path.setAttributes(setAttribute());
//            layer.addRenderable(path);
//            AnnotationLayer centerOfMassLayer = new AnnotationLayer();

            RenderableLayer pointLayer = new RenderableLayer();
//             // Create a placemark without an image.
            PointPlacemark pp;// = new PointPlacemark(Position.fromDegrees(29, -104.5, 2e4));
            for (LatLon ll :
                centerOfMass)
            {
                pp = new PointPlacemark(new Position(ll, 0.0));
//                pp.setLabelText("Placemark G");
//                pp.setValue(AVKey.DISPLAY_NAME, "Clamp to ground, White label, Red point, Scale 5");
                pp.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
                pp.setAttributes(pointAttribute());
                pointLayer.addRenderable(pp);
            }
            layers.add(pointLayer);
//

            RenderableLayer layer;
            // SurfacePolygon
            for (SurfacePolygon s :
                surfaces)
            {
                s.setAttributes(setAttribute());
                s.setPathType(AVKey.GREAT_CIRCLE);
                layer = new RenderableLayer();
                layer.addRenderable(s);
                layers.add(layer);
            }

            // Path
//            for (Path p :
//                pathList)
//            {
//                p.setDrawVerticals(true);
//                p.setPathType(AVKey.GREAT_CIRCLE);
//                p.setAttributes(setAttribute());
//                layer = new RenderableLayer();
//                layer.addRenderable(p);
//                layers.add(layer);
//            }

            return layers;
        }

        private PointPlacemarkAttributes pointAttribute()
        {
            PointPlacemarkAttributes attrs = new PointPlacemarkAttributes();
            attrs.setLabelColor("ffffffff");
            attrs.setLineColor("ff0000ff");
            attrs.setUsePointAsDefaultImage(true);
            attrs.setScale(5d);
            return attrs;
        }

        private ShapeAttributes setAttribute()
        {
            ShapeAttributes attr = new BasicShapeAttributes();
            attr.setOutlineMaterial(new Material(new Color(50, 150, 50)));
            attr.setInteriorOpacity(0.1);
            attr.setOutlineWidth(5.0);
//            attr.setOutlineStippleFactor(3);
//            attr.setOutlineStipplePattern();
            return attr;
        }

        private ShapeAttributes setAttribute(int stippleFactor)
        {
            ShapeAttributes attr = new BasicShapeAttributes();
            attr.setOutlineMaterial(new Material(new Color(50, 150, 50)));
            attr.setOutlineWidth(5.0);
            attr.setOutlineStippleFactor(stippleFactor);
//            attr.setOutlineStipplePattern();
            return attr;
        }

        private ShapeAttributes setAttribute(int red, int green, int blue)
        {
            ShapeAttributes attr = new BasicShapeAttributes();
            attr.setOutlineMaterial(new Material(new Color(red % 256, green % 256, blue % 256)));
            attr.setOutlineWidth(3.0);
            attr.setOutlineStippleFactor(0);
//            attr.setOutlineStipplePattern();
            return attr;
        }

        private Mesh fill(MiddleArcSurfaceTriangle initCell, int level)
        {
            List<MiddleArcSurfaceTriangle> list = new ArrayList<>();
            list.add(initCell);
            for (int i = 0; i < level; i++)
            {
                EssentialVariable2.refineTriangle(list);
            }
            Mesh mesh = new SurfaceTriangleMesh(level);
            for (MiddleArcSurfaceTriangle cell : list)
            {
                mesh.addNode(cell);
            }
            return mesh;
        }

        private Mesh fill(QTMTriangle initCell, int level)
        {
            List<QTMTriangle> list = new ArrayList<>();
            list.add(initCell);
            List<QTMTriangle> temp = new ArrayList<>();
            for (int i = 0; i < level; i++)
            {
                for (QTMTriangle cell : list)
                {
                    temp.addAll(Arrays.asList(cell.refine()));
                }
                list.clear();
                list.addAll(temp);
                temp.clear();
            }
            Mesh mesh = new SurfaceTriangleMesh(level);
            for (QTMTriangle cell : list)
            {
                mesh.addNode(cell);
            }
            return mesh;
        }
    }
}
