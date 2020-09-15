/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/4/28
 * @description 闭合基本单元格，attributes of a cell
 * @parameter 构成单元格的顶点（初始顶点和终结顶点相同），地理编码ID
 */
public abstract class Cell extends DGG implements Area, Refinement
{

    //    // 表示单元格的基本形状取值为：
//    // "TRI"(3),"QUA"(4),"HEX"(6)....
    private int shape;
    private int level;

    private List<LatLon> geoVertices;

    // reference point
    private LatLon center;

    // Geo-coding
    private Geocode geocode;

    public Cell(Iterable<? extends LatLon> locations, String ID)
    {
        if (locations == null)
        {
            String message = Logging.getMessage("nullValue.VertexIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        geoVertices = new ArrayList<>();

        for (LatLon point : locations)
        {
            geoVertices.add(IO.check(point));
        }

        shape = geoVertices.size();

//        if (!geoVertices.get(0).equals(geoVertices.get(shape)))
//        {
//            geoVertices.add(geoVertices.get(0));
//            shape++;
//        }

        if (shape < 3)
        {
            String message = Logging.getMessage("lackValue.VertexLessThan3");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        //this.globe = DGGS.getGlobe();
        center = LatLon.getCenter(Cons.getGlobe(), locations);
        geocode = new Geocode(ID);
        this.level = ID.length() - 1;
    }

    public Cell(LatLon top, LatLon left, LatLon right, String ID)
    {
        if (top == null || left == null || right == null)
        {
            String message = Logging.getMessage("nullValue.顶点为空");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        geoVertices = new ArrayList<>();
        ArrayList<LatLon> temp = new ArrayList<>();
        temp.add(IO.check(top));
        if (left.getLongitude().degrees < right.getLongitude().degrees)
        {
            temp.add(IO.check(left));
            temp.add(IO.check(right));
        }
        else
        {
            temp.add(IO.check(right));
            temp.add(IO.check(left));
        }
        center = IO.check(LatLon.getCenter(Cons.getGlobe(), temp));
        geoVertices.addAll(temp);
        // geoVertices.add(top);
        shape = 3;
        geocode = new Geocode(ID);
        this.level = ID.length() - 1;
    }

    public int getLevel()
    {
        return level;
    }

    public abstract double getUnitArea();

//    public abstract double computeArea();

    public abstract Cell[] refine();

    public abstract Path[] renderPath();

    public int getShape()
    {
        return shape;
    }

    public LatLon getCenter()
    {
        return center;
    }

    public Geocode getGeocode()
    {
        return geocode;
    }

    public List<LatLon> getGeoVertices()
    {
        return geoVertices;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(geoVertices, geocode.getID(), level);
    }

    @Override
    public String toString()
    {
        StringBuilder locations = new StringBuilder();

        int total = geoVertices.size();
        int number;
        if (LatLon.equals(geoVertices.get(0), geoVertices.get(total - 1)))
        {
            number = total - 1;
        }
        else
        {
            number = total;
        }
        List<LatLon> vertices = geoVertices.subList(0, number);
        for (LatLon l :
            vertices)
        {
            locations.append(l.toString()).append("\t");
        }

        return "{" +
            getGeocode().toString() +
            ", " + getShape() +
            ", " + locations.toString() +
            '}';
    }

//    @Override
//    public boolean equals(Object o)
//    {
//        if (this == o)
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        Cell cell = (Cell) o;
////        return shape == cell.shape &&
////            level == cell.level &&
////            Objects.equals(geoVertices, cell.geoVertices) &&
////            Objects.equals(center, cell.center) &&
////            Objects.equals(geocode, cell.geocode);
////
////        if (this == o)
////            return true;
////        if (o == null || getClass() != o.getClass())
////            return false;
////        Cell cell = (Cell) o;
//        boolean isEqual = true;
//        for (int i = 0; i < getGeoVertices().size(); i++)
//        {
////            geoVertices.get(i).equals(cell.geoVertices.get(i));
//            if (!LatLon.equals(getGeoVertices().get(i), cell.getGeoVertices().get(i)))
//            {
//                isEqual = false;
//                break;
//            }
//        }
////        if (cell.level != -1 && level != -1 && cell.level != level)
////        {
////            isEqual = false;
////        }
//        return isEqual && geocode.getID().equals(cell.geocode.getID());
//    }
}
