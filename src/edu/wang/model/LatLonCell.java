/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2020/10/17 17:09
 * @Param: 4 Vertexes
 */
public class LatLonCell
{
    private LatLon upperLeft, upperRight, lowerLeft, lowerRight;
    //    private LatLon northwest,northeast,southwest,southeast;
    private int shape = -1;//3,4 OR -1
    private String ID;

    public LatLonCell(LatLon upperLeft, LatLon upperRight, LatLon lowerLeft, LatLon lowerRight, String ID)
    {
        // as Z curve shows ;
        if (upperLeft == null || upperRight == null || lowerLeft == null || lowerRight == null)
        {
            String message = Logging.getMessage("nullValue.四个角点有空值");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        double upperLatLeft = (Math.max(upperLeft.getLatitude().radians, lowerLeft.getLatitude().radians));
        double upperLatRight = (Math.max(upperRight.getLatitude().radians, lowerRight.getLatitude().radians));
        double upperLat = (upperLatLeft + upperLatRight) / 2.0;

        double lowerLatLeft = (Math.min(upperLeft.getLatitude().radians, lowerLeft.getLatitude().radians));
        double lowerLatRight = (Math.min(upperRight.getLatitude().radians, lowerRight.getLatitude().radians));
        double lowerLat = (lowerLatLeft + lowerLatRight) / 2.0;

        double leftLon;
        double rightLon;
        if ((Math.PI / 2.0 - upperLatLeft) <= Const.EPSILON || (Math.PI / 2.0 - upperLatRight) <= Const.EPSILON)
        {
            shape = 3;
            leftLon = Math.min(lowerLeft.getLongitude().radians, lowerRight.getLongitude().radians);
            rightLon = Math.max(lowerLeft.getLongitude().radians, lowerRight.getLongitude().radians);
        }
        else if ((Math.PI / 2.0 + lowerLatLeft) <= Const.EPSILON || (Math.PI / 2.0 + lowerLatRight) <= Const.EPSILON)
        {
            shape = 3;
            leftLon = Math.min(upperLeft.getLongitude().radians, upperRight.getLongitude().radians);
            rightLon = Math.max(upperLeft.getLongitude().radians, upperRight.getLongitude().radians);
        }
        else
        {
            shape = 4;
            double rightLonUpper = (Math.max(upperLeft.getLongitude().radians, upperRight.getLongitude().radians));
            double rightLonLower = (Math.max(lowerLeft.getLongitude().radians, lowerRight.getLongitude().radians));
            double leftLonUpper = (Math.min(upperLeft.getLongitude().radians, upperRight.getLongitude().radians));
            double leftLonLower = (Math.min(lowerLeft.getLongitude().radians, lowerRight.getLongitude().radians));
            leftLon = (leftLonUpper + leftLonLower) / 2.0;
            rightLon = (rightLonUpper + rightLonLower) / 2.0;
        }

        this.lowerLeft = LatLon.fromRadians(IO.check(lowerLat), IO.check(leftLon));
        this.lowerRight = LatLon.fromRadians(IO.check(lowerLat), IO.check(rightLon));
        this.upperLeft = LatLon.fromRadians(IO.check(upperLat), IO.check(leftLon));
        this.upperRight = LatLon.fromRadians(IO.check(upperLat), IO.check(rightLon));

        this.ID = ID;
    }

    public String getID()
    {
        return ID;
    }

    private Vec4[] getVertices()
    {
        Vec4[] vertices = new Vec4[4];
        vertices[0] = IO.latLonToVec4(upperLeft);
        vertices[1] = IO.latLonToVec4(upperRight);
        vertices[2] = IO.latLonToVec4(lowerLeft);
        vertices[3] = IO.latLonToVec4(lowerRight);
        return vertices;
    }

    private double compositeVectorLength()
    {
        double xSum = getVertices()[0].x + getVertices()[1].x + getVertices()[2].x + getVertices()[3].x;
        double ySum = getVertices()[0].y + getVertices()[1].y + getVertices()[2].y + getVertices()[3].y;
        double zSum = getVertices()[0].z + getVertices()[1].z + getVertices()[2].z + getVertices()[3].z;
        return Math.sqrt(Math.pow(xSum, 2) + Math.pow(ySum, 2) + Math.pow(zSum, 2));
    }

    public Vec4 sphericalMean()
    {
        double xSum = getVertices()[0].x + getVertices()[1].x + getVertices()[2].x + getVertices()[3].x;
        double ySum = getVertices()[0].y + getVertices()[1].y + getVertices()[2].y + getVertices()[3].y;
        double zSum = getVertices()[0].z + getVertices()[1].z + getVertices()[2].z + getVertices()[3].z;
        Vec4 pp = new Vec4(xSum / shape, ySum / shape, zSum / shape);
        return pp.multiply3(compositeVectorLength() / shape);
    }

    public double distribute()
    {
        return Const.RADIUS * (1 - compositeVectorLength() / shape);
    }

    public double computeFractal()
    {
        // df = 2ln(P/4)/lnA
        return 2 * Math.log(computePerimeter() / 4.0) / Math.log(computeArea());
    }

    public double computeShapeIndex()
    {
        // w*sqrt(A)/P
        double w;
        if (shape == 3)
            w = 4.559014;
        else
            w = 4.0;
        return w * Math.sqrt(computeArea()) / computePerimeter();
    }

    public double computeCompactness()
    {
        // sqrt(4PIA-(A/r)^2)/P
        return Math.sqrt(4 * Math.PI * computeArea() - Math.pow(computeArea() / Const.RADIUS, 2)) / computePerimeter();
    }

    public double computePerimeter()
    {
        double left = LatLon.greatCircleDistance(upperLeft, lowerLeft).radians;
        double right = LatLon.greatCircleDistance(upperRight, lowerRight).radians;
        double upper, bottom;
        if ((Math.PI / 2.0 - upperLeft.getLatitude().radians) <= Const.EPSILON)
        {
            upper = 0.0;
        }
        else
        {
            double delta = upperRight.getLongitude().subtract(upperLeft.getLongitude()).radians;
            upper = delta * upperLeft.getLatitude().cos();
//            upper = LatLon.rhumbDistance(upperLeft,upperRight).radians;
        }
        if ((Math.PI / 2.0 + lowerLeft.getLatitude().radians) <= Const.EPSILON)
        {
            bottom = 0.0;
        }
        else
        {
            double delta = lowerRight.getLongitude().subtract(lowerLeft.getLongitude()).radians;
            bottom = delta * lowerLeft.getLatitude().cos();
        }

        return Const.RADIUS * (left + bottom + right + upper);
    }

    public double computeArea()
    {
        double upperLat = upperLeft.getLatitude().radians;
        double lowerLat = lowerLeft.getLatitude().radians;
        double deltaLon = (upperRight.longitude.radians - upperLeft.longitude.radians) / (2.0 * Math.PI);
        double minArea = Math.min(Area.sphericalCapArea(upperLat), Area.sphericalCapArea(lowerLat));
        double maxArea = Math.max(Area.sphericalCapArea(upperLat), Area.sphericalCapArea(lowerLat));
        return deltaLon * (maxArea - minArea);
    }

    public Angle[] innerAngle()
    {
        Angle[] inner = new Angle[shape];
        if (shape == 4)
        {
            inner[0] = Angle.POS90;
            inner[1] = Angle.POS90;
            inner[2] = Angle.POS90;
            inner[3] = Angle.POS90;
        }
        else
        {
            if ((Math.PI / 2.0 - upperLeft.getLatitude().radians) <= Const.EPSILON)
            {
                inner[0] = lowerRight.getLongitude().subtract(lowerLeft.getLongitude());
                inner[1] = Angle.POS90;
                inner[2] = Angle.POS90;
            }
            else
            {
                inner[0] = Angle.POS90;
                inner[1] = Angle.POS90;
                inner[2] = upperRight.getLongitude().subtract(upperLeft.getLongitude());
            }
        }
        return inner;
    }

    public LatLon getLowerLeft()
    {
        return lowerLeft;
    }

    public LatLon getLowerRight()
    {
        return lowerRight;
    }

    public LatLon getUpperLeft()
    {
        return upperLeft;
    }

    public LatLon getUpperRight()
    {
        return upperRight;
    }

    public int getShape()
    {
        return shape;
    }

    public LatLonCell[] refine()
    {
        double latUpper = upperLeft.getLatitude().radians;
        double latLower = lowerLeft.getLatitude().radians;
        double lonLeft = upperLeft.getLongitude().radians;
        double lonRight = upperRight.getLongitude().radians;
        double latMid = (latUpper + latLower) / 2.0;
        double lonMid = (lonLeft + lonRight) / 2.0;

        LatLon upperMid = LatLon.fromRadians(IO.check(latUpper), IO.check(lonMid));
        LatLon lowerMid = LatLon.fromRadians(IO.check(latLower), IO.check(lonMid));
        LatLon leftMid = LatLon.fromRadians(IO.check(latMid), IO.check(lonLeft));
        LatLon rightMid = LatLon.fromRadians(IO.check(latMid), IO.check(lonRight));
        LatLon center = LatLon.fromRadians(IO.check(latMid), IO.check(lonMid));

        String id = ID;

        LatLonCell[] subCells = new LatLonCell[4];

        subCells[0] = new LatLonCell(upperLeft, upperMid, leftMid, center, id + "0");
        subCells[1] = new LatLonCell(upperMid, upperRight, center, rightMid, id + "1");
        subCells[2] = new LatLonCell(leftMid, center, lowerLeft, lowerMid, id + "2");
        subCells[3] = new LatLonCell(center, rightMid, lowerMid, lowerRight, id + "3");

        return subCells;
    }

    public Path[] renderCell()
    {
        List<Position> sideLeft = new ArrayList<>();
        List<Position> sideBottom = new ArrayList<>();
        List<Position> sideRight = new ArrayList<>();
        List<Position> sideUpper = new ArrayList<>();

        Path pathLeft = new Path();
        Path pathBottom = new Path();
        Path pathRight = new Path();
        Path pathUpper = new Path();

        // great circle
        sideLeft.add(new Position(upperLeft, 0));
        sideLeft.add(new Position(lowerLeft, 0));
        pathLeft.setPositions(sideLeft);
        pathLeft.setPathType(AVKey.GREAT_CIRCLE);
        pathLeft.setAttributes(Const.defaultPathAttribute());

        sideRight.add(new Position(lowerRight, 0));
        sideRight.add(new Position(upperRight, 0));
        pathRight.setPositions(sideRight);
        pathRight.setPathType(AVKey.GREAT_CIRCLE);
        pathRight.setAttributes(Const.defaultPathAttribute());

        // small circle
        sideBottom.add(new Position(lowerLeft, 0));
        sideBottom.add(new Position(lowerRight, 0));
        pathBottom.setPositions(sideBottom);
        pathBottom.setPathType(AVKey.RHUMB_LINE);
        pathBottom.setAttributes(Const.defaultPathAttribute());

        sideUpper.add(new Position(upperRight, 0));
        sideUpper.add(new Position(upperLeft, 0));
        pathUpper.setPositions(sideBottom);
        pathUpper.setPathType(AVKey.RHUMB_LINE);
        pathUpper.setAttributes(Const.defaultPathAttribute());

        Path[] paths = new Path[4];
        paths[0] = pathLeft;
        paths[1] = pathBottom;
        paths[2] = pathRight;
        paths[3] = pathUpper;

        return paths;
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        string.append(ID).append("\t");
        string.append(shape).append("\t");
        if (shape == 4)
        {
            string.append(upperLeft).append("\t");
            string.append(upperRight).append("\t");
            string.append(lowerLeft).append("\t");
            string.append(lowerRight).append("\t");
        }
        else
        {
            if ((Math.PI / 2.0 - upperLeft.getLatitude().radians) <= Const.EPSILON)
            {
                string.append(upperLeft).append("\t");
                string.append(lowerLeft).append("\t");
                string.append(lowerRight).append("\t");
            }
            else
            {
                string.append(upperLeft).append("\t");
                string.append(upperRight).append("\t");
                string.append(lowerLeft);
            }
        }

        return string.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        LatLonCell cell = obj instanceof LatLonCell ? (LatLonCell) obj : null;
        if (cell == null)
            return false;
        if (this.shape != cell.shape)
            return false;
        return cell.upperLeft.equals(this.upperLeft) && cell.lowerLeft.equals(this.lowerLeft) && cell.lowerRight.equals(
            this.lowerRight) && cell.upperRight.equals(this.upperRight);
    }
}
