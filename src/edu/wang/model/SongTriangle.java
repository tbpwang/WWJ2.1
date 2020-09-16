/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.SurfaceTriangle;
import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2020/6/19 14:23
 * @description 构造球面小圆弧构成的三角形，根据Song Lian,1997
 * @parameter 三个小圆弧
 */

public class SongTriangle extends SurfaceTriangle
{
//    // 底边的相对极和相对纬度
//    private LatLon bottomPole;
//    private Angle bottomColatitude;
//    // 左边的相对极和相对纬度
//    private LatLon leftPole;
//    private Angle leftColatitude;
//    // 右边的相对极和相对纬度
//    private LatLon rightPole;
//    private Angle rightColatitude;

    // 三角形向上，逆时针方向排列
    // 三角形向下，顺时针方向排列
    private SmallCircle bottomEdge;
    private SmallCircle leftEdge;
    private SmallCircle rightEdge;

    public SongTriangle(SmallCircle bottomEdge, SmallCircle rightEdge, SmallCircle leftEdge,
        String id)
    {
        super(leftEdge.getFirst().latLon, bottomEdge.getFirst().latLon, rightEdge.getFirst().latLon, id);
        this.bottomEdge = bottomEdge;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    //    private static LatLon[] parsing(SmallCircle bottomEdge, SmallCircle leftEdge, SmallCircle rightEdge)
//    {
//        LatLon first = bottomEdge.getLatLons()[0];
//        LatLon second = leftEdge.getLatLons()[0].getLongitude().compareTo(first.getLongitude())>=0?leftEdge.getLatLons()[1]:leftEdge.getLatLons()[0];
//        LatLon third = leftEdge.getLatLons()[0].getLongitude().compareTo(first.getLongitude())==0?leftEdge.getLatLons()[1]:leftEdge.getLatLons()[0];
//    }
    public SmallCircle getBottomEdge()
    {
        return bottomEdge;
    }

    public SmallCircle getLeftEdge()
    {
        return leftEdge;
    }

    public SmallCircle getRightEdge()
    {
        return rightEdge;
    }

    @Override
    public List<Double> edgeLengths()
    {
        //
        // 从底开始，底边->右边->左边
        List<Double> edgeList = new ArrayList<>();
        edgeList.add(getBottomEdge().getLength());
        edgeList.add(getRightEdge().getLength());
        edgeList.add(getLeftEdge().getLength());
        return edgeList;
    }

    @Override
    public List<Angle> innerAngle()
    {
        // 顶角->左角->右角
        List<Angle> interior = new ArrayList<>();
        Vec4 o1 = getBottomEdge().getCircleCenter();
        Vec4 o2 = getRightEdge().getCircleCenter();
        Vec4 o3 = getLeftEdge().getCircleCenter();
        interior.add(o2.angleBetween3(o3));
        interior.add(o3.angleBetween3(o1));
        interior.add(o1.angleBetween3(o2));
        return interior;
    }

    private double semiLune(SmallCircle smallCircle)
    {
        if (!smallCircle.isHasCoLatitude())
        {
            return Double.NaN;
        }
        double greatArea = Area.unitSphereSurfaceTriangleArea(smallCircle.getPole(), smallCircle.getFirst().latLon,
            smallCircle.getLast().latLon);
        double smallArea = arcCrownArea(smallCircle);
        // 正负都有可能，
        // 负数表示大圆弧在小圆弧外面
        return smallArea - greatArea;
    }

    private double arcCrownArea(SmallCircle smallCircle)
    {
        if (!smallCircle.isHasCoLatitude())
        {
            return Double.NaN;
        }
        //      double length,h,sum;
//      area = 2 * PI * R * h
//        sum = 2.0 * Math.PI * R * h * length/(2.0 * Math.PI * getBottomEdge().radius());
        return smallCircle.crownHeight() * smallCircle.getLength() / smallCircle.getArcRadius();
    }

    @Override
    public double getUnitArea()
    {
        if (bottomEdge.isHasCoLatitude() && leftEdge.isHasCoLatitude() && rightEdge.isHasCoLatitude())
        {
            double sum = semiLune(bottomEdge) - semiLune(leftEdge) - semiLune(rightEdge);
            double greatArea = Area.unitSphereSurfaceTriangleArea(getTop(), getLeft(), getRight());
            return greatArea + sum;
        }
        else
        {
            return Double.NaN;
        }
    }

    @Override
    public SongTriangle[] refine()
    {

        SmallCircle bottomEdge = getBottomEdge();
        SmallCircle rightEdge = getRightEdge();
        SmallCircle leftEdge = getLeftEdge();
        if (!(bottomEdge.isHasCoLatitude() || rightEdge.isHasCoLatitude() || leftEdge.isHasCoLatitude()))
        {
            return null;
        }
        LatLon bottomPole = bottomEdge.getPole();
        LatLon midBottom = bottomEdge.getMiddlePoint();
//        LatLon midBottomLL = Const.vec4ToLatLon(midBottom);

        LatLon rightPole = rightEdge.getPole();
        LatLon midRight = rightEdge.getMiddlePoint();
//        LatLon midRightLL = Const.vec4ToLatLon(midRight);

        LatLon leftPole = leftEdge.getPole();
        LatLon midLeft = leftEdge.getMiddlePoint();
//        LatLon midLeftLL = Const.vec4ToLatLon(midLeft);

        SongTriangle[] triangles = new SongTriangle[4];
        String id = getGeocode().getID();

        // "1"
        SmallCircle[] splitLeft = leftEdge.bisect();
        SmallCircle[] splitRight = rightEdge.bisect();
//        LatLon left2RightPole = calculateSmallCircleEdge();
//        SmallCircle midLeft2RightAsBottomEdge = new SmallCircle(midLeft, midRight, calculateSmallCircleEdge());
        SmallCircle midLeft2RightAsBottomEdge = calculateSmallCircleEdge(splitRight[1], splitLeft[0]);
        SongTriangle one = new SongTriangle(midLeft2RightAsBottomEdge, splitRight[1], splitLeft[0], id + "1");
//        SongTriangle one = new SongTriangle(midLeft2Right, splitRight[1], splitLeft[0], id + "1");
        // "2"
        SmallCircle[] splitBottom = bottomEdge.bisect();
//        SmallCircle midBottom2Left = new SmallCircle(midBottom, midLeft, rightPole);
        SmallCircle midBottom2Left = calculateSmallCircleEdge(splitLeft[1], splitBottom[0]);
        SongTriangle two = new SongTriangle(splitBottom[0], midBottom2Left, splitLeft[1], id + "2");
        // "3"
//        SmallCircle midRight2Bottom = new SmallCircle(midRight, midBottom, leftPole);
        SmallCircle midRight2Bottom = calculateSmallCircleEdge(splitBottom[1], splitRight[0]);
        SongTriangle three = new SongTriangle(splitBottom[1], splitRight[0], midRight2Bottom, id + "3");
        // "0"
        SongTriangle zero = new SongTriangle(midLeft2RightAsBottomEdge, midRight2Bottom, midBottom2Left, id + "0");

        // 子三角形
        triangles[0] = zero;
        triangles[1] = one;
        triangles[2] = two;
        triangles[3] = three;

        return triangles;
    }

    private SmallCircle calculateSmallCircleEdge(SmallCircle relativeRightEdge, SmallCircle relativeLeftEdge)
    {
        if (!(relativeLeftEdge.isHasCoLatitude() && relativeRightEdge.isHasCoLatitude()))
        {
            String msg = Logging.getMessage("ErrorTriangle:父三角形极点未知。");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        LatLon lFirst = relativeLeftEdge.getFirst().latLon;
        LatLon lLast = relativeLeftEdge.getLast().latLon;
        LatLon rFirst = relativeRightEdge.getFirst().latLon;
        LatLon rLast = relativeRightEdge.getLast().latLon;
        if (!rLast.equals(lFirst))
        {
            String msg = Logging.getMessage("ErrorTriangle:顶点不重合，构不成三角形。");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        Angle leftSide = LatLon.greatCircleDistance(lFirst, lLast);
        Angle rightSide = LatLon.greatCircleDistance(rFirst, rLast);
        Angle btmSideLength = LatLon.greatCircleDistance(lLast, rFirst);
//        LatLon leftPole = relativeLeftEdge.getPole();
//        LatLon rightPole = relativeRightEdge.getPole();
        Angle lCoLatitudeAngle = relativeLeftEdge.getCoLatitude();
        Angle rCoLatitudeAngle = relativeRightEdge.getCoLatitude();
        double lSemiLuneArea, rSemiLuneArea;
        // 对于左三角形：
        lSemiLuneArea = calculateSemiLuneArea(leftSide, lCoLatitudeAngle);
        // 对于右三角形：
        rSemiLuneArea = calculateSemiLuneArea(rightSide, rCoLatitudeAngle);
        double area = getUnitArea() / 4.0;
        double triArea = Area.unitSphereSurfaceTriangleArea(relativeLeftEdge.getFirst().latLon, relativeLeftEdge.getLast().latLon,
            relativeRightEdge.getFirst().latLon);
        double dArea1 = triArea - lSemiLuneArea - rSemiLuneArea;
        double bSemiLuneArea = area - dArea1;
        Angle coLatitude;
        LatLon[] poles;
        LatLon pole;
        double toPole1, toPole2;
        if (bSemiLuneArea >= 0)
        {
            coLatitude = calculateRelativeCoLatitude(bSemiLuneArea, btmSideLength);
            poles = getPoleByCoLatitude(lLast, rFirst, coLatitude);
            toPole1 = LatLon.greatCircleDistance(poles[0], lLast).radians;
            toPole2 = LatLon.greatCircleDistance(poles[1], lLast).radians;
            // 正的取近的
            if (toPole1 > toPole2)
                pole = poles[1];
            else
                pole = poles[0];
        }
        else
        {
            coLatitude = calculateRelativeCoLatitude(-bSemiLuneArea, btmSideLength);
            poles = getPoleByCoLatitude(lLast, rFirst, coLatitude);
            toPole1 = LatLon.greatCircleDistance(poles[0], lLast).radians;
            toPole2 = LatLon.greatCircleDistance(poles[1], lLast).radians;
            // 负的取远的
            if (toPole1 > toPole2)
                pole = poles[0];
            else
                pole = poles[1];
        }

//        SmallCircle btEdge = new SmallCircle(lLast, rFirst);
//        btEdge.setPole(pole);
//
//        return btEdge;
        return new SmallCircle(lLast, rFirst, pole);
    }

    private LatLon[] getPoleByCoLatitude(LatLon first, LatLon last, Angle coLatitude)
    {
        Vec4 vFirst = IO.latLonToVec4(first);
        double xf = vFirst.getX();
        double yf = vFirst.getY();
        double zf = vFirst.getZ();
        Vec4 vLast = IO.latLonToVec4(last);
        double xl = vLast.getX();
        double yl = vLast.getY();
        double zl = vLast.getZ();

        // 三个方程，三个未知数
        // coLatitude.cos = xf*xp+yf*yp+zf*zp;
        // coLatitude.cos = xl*xp+yl*yp+zl*zp;
        // Math.pow(xp,2) + Math.pow(yp,2) + Math.pow(zp,2) = 1;
        double A = (xf - xl) / (zl - zf);
        double B = (yf - yl) / (zl - zf);
        double C = (xf / zf + A) / (B + yf / zf);
        double D = coLatitude.cos() / zf / (B + yf / zf);
        double E = A - B * C;
        double F = B * D;
        double a = 1 + C * C + E * E;
        double b = 2 * E * F - 2 * C * D;
        double c = D * D + F * F - 1;
        // a*x^2+b*x+c=0
        double[] roots = rootsOfQuadraticEquation(a, b, c);
        double xp = roots[0];
        double yp = D - C * xp;
        double zp = A * xp + B * yp;
        Vec4 pole0 = new Vec4(xp, yp, zp);

        xp = roots[1];
        yp = D - C * xp;
        zp = A * xp + B * yp;
        Vec4 pole1 = new Vec4(xp, yp, zp);

        LatLon[] poles = new LatLon[2];
        poles[0] = IO.vec4ToLatLon(pole0);
        poles[1] = IO.vec4ToLatLon(pole1);
        return poles;
    }

    private double[] rootsOfQuadraticEquation(double a, double b, double c)
    {
        // aX^2+bx+c=0
        double[] roots = new double[2];

        if (a == 0)
        {
            roots[0] = Double.isNaN(c / b) ? Double.NaN : c / b;
            roots[1] = roots[0];
        }
        double sqrt = Math.sqrt(Math.pow(b, 2) - 4 * a * c);
        roots[0] = (-b + sqrt) / 2 / a;
        roots[1] = (-b - sqrt) / 2 / a;
        return roots;
    }

    private Angle calculateRelativeCoLatitude(double semiLuneArea, Angle btmLength)
    {
        // by song(1997)
//        Angle coLatitude;
        // 迭代函数
        // 变量cos(alpha)取值范围[0,1]，alpha取值范围[0,90]
        double cosBtm = btmLength.cos();
//        double coLat = Angle.fromDegrees(45.0);
        double x0 = Math.PI / 4;
        double x1 = Math.PI / 2;
        double f_x0 = calculateRelativeCoLatitudeFunction(cosBtm, x0, semiLuneArea);
        double f_x1 = calculateRelativeCoLatitudeFunction(cosBtm, x1, semiLuneArea);
        double x2 = x1 - f_x1 * (x1 - x0) / (f_x1 - f_x0);
        while (Math.abs(x2 - x1) > Const.EPSILON)
        {
            x0 = x1;
            x1 = x2;
            f_x0 = calculateRelativeCoLatitudeFunction(cosBtm, x0, semiLuneArea);
            f_x1 = calculateRelativeCoLatitudeFunction(cosBtm, x1, semiLuneArea);
            x2 = x1 - f_x1 * (x1 - x0) / (f_x1 - f_x0);
        }
        return Angle.fromRadians(x2);
    }

    private double calculateRelativeCoLatitudeFunction(double bottomCos, double initCoLat, double semiLuneArea)
    {
        if (initCoLat <= Const.EPSILON)
        {
            System.out.println("semiLuneArea - Math.PI = ");
            System.out.println(semiLuneArea - Math.PI);
//            return 0.0;
            return semiLuneArea - Math.PI;
        }
        double top = Math.acos(1 + (bottomCos - 1) / (1 - Math.pow(Math.cos(initCoLat), 2)));
        double btm = Math.asin(Math.sqrt((1 + Math.cos(top)) / (1 + bottomCos)));
        return semiLuneArea - (Math.PI - 2 * btm - top * Math.cos(initCoLat));
    }

    private double calculateSemiLuneArea(Angle edgeLength, Angle coLatitude)
    {
        double top;
        double btm;
        double semLuneArea;
        top = calculateRelativeCoLatitudeTopAngle(edgeLength, coLatitude);
        btm = Math.asin(Math.sqrt((1.0 + Math.cos(top)) / (1.0 + edgeLength.cos())));
        semLuneArea = Math.PI - 2 * btm - top * coLatitude.cos();
        return semLuneArea;
    }

    private double calculateRelativeCoLatitudeTopAngle(Angle edgeLength, Angle coLatitude)
    {
        return Math.acos(1.0 - (1.0 - edgeLength.cos()) / coLatitude.sin() / coLatitude.sin());
    }

    @Override
    public Path[] renderPath()
    {
        // todo:
        return null;
    }
}
