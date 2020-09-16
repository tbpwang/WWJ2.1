/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.SurfaceTriangle;
import edu.wang.*;
import edu.wang.io.*;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.Path;
//import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/5/8 16:23
 * @description
 * @parameter
 */
public class QTMTriangle extends SurfaceTriangle
{
    private final boolean isParallelBottom;
    private QTMTriangle[] subTriangles;

    public QTMTriangle(LatLon top, LatLon left, LatLon right, Geocode geocode)
    {
        super(top, left, right, geocode);
        double delta = Math.abs(right.getLatitude().radians - left.getLatitude().radians);
        isParallelBottom = !(IO.check(delta) > Const.EPSILON);
    }

    public QTMTriangle(LatLon top, LatLon left, LatLon right, String id)
    {
        this(top, left, right, new Geocode(id));
    }

    public boolean isStrictQTM()
    {
        return isParallelBottom;
    }

    public static QTMTriangle cast(SurfaceTriangle triangle)
    {
        if (triangle == null)
        {
            return null;
        }
        LatLon A, B, C;
        A = IO.check(triangle.getTop());
        B = IO.check(triangle.getLeft());
        C = IO.check(triangle.getRight());
        return new QTMTriangle(A, B, C, triangle.getGeocode());
    }

    @Override
    public double getUnitArea()
    {
//        return getUnitArea(true);
        return someAreas();
    }

    private double someAreas()
    {
        // 微分法

        int interval = 1000;
        double minLon = getLeft().getLongitude().radians;
        double maxLon = getRight().getLongitude().radians;
//        double lat1 = getLeft().getLatitude().radians;
//        double lat2 = getRight().getLatitude().radians;

        double delta = IO.check((maxLon - minLon) / interval);
        while (delta <= Const.EPSILON)
        {
            interval = interval / 10;
            delta = IO.check((maxLon - minLon) / interval);
        }
        LatLon tempFrom, tempTo;
        double addArea = 0.0;
        for (int i = 0; i < interval; i++)
        {
            tempFrom = LatLon.interpolateRhumb((double) i / interval, getLeft(), getRight());
            tempTo = LatLon.interpolateRhumb((i + 1.0) / interval, getLeft(), getRight());
            addArea += Area.unitSphereSurfaceTriangleArea(getTop(), tempFrom, tempTo);
        }

        return addArea;

//        from = minLon;
//        to = minLon + delta;
//        double area = 0.0;
//        for (int i = 0; i < interval; i++)
//        {
//            LatLon left = LatLon.fromRadians(lat1, from);
//            LatLon right = LatLon.fromRadians(lat1, to);
//            from = from + delta;
//            if (i == interval - 1)
//            {
//                to = maxLon;
//            }
//            else
//            {
//                to += delta;
//            }
//            SurfaceTriangle triangle = new SurfaceTriangle(getTop(), left, right, "");
//            area += triangle.getUnitArea();
//        }
//        return area;
    }

    private Angle bottomAngle(LatLon leftSide, LatLon rightSide)
    {
        // if 大圆弧leftSide-rightSide存在，
        // 那么，计算该大圆弧与赤道面的夹角：bottomAngle
        // leftLat 和 rigtLat不一定相等 TODO
//        double leftLat= leftSide.getLatitude().degrees;
//        double rigtLat = rightSide.getLatitude().degrees;
//        double Flag =
//        double minLat = Math.min(Math.abs(leftLat),Math.abs(rigtLat));
        //

        Vec4 pole = new Vec4(0, 0, 1);

        Vec4 left = IO.latLonToVec4(leftSide);
        Vec4 right = IO.latLonToVec4(rightSide);
        Vec4 l2r = left.cross3(right);
        return l2r.angleBetween3(pole);
    }

    public double getUnitArea(boolean isGeographicalPole)
    {
        // 目前仅针对地理极点的小圆弧为底大圆弧为腰的三角形面积计算
        // 任意极点的上述三角形面积计算待定TODO:
        // if (poleNorthSouth)
        double radius = 1.0;

        double minLongitude = getLeft().getLongitude().degrees;
        double maxLongitude = getRight().getLongitude().degrees;

        double deltaDegree = maxLongitude - minLongitude;
        double rate = deltaDegree / 360.0;

        double area;

        // latitude范围[0,90]
        Angle bottom = getLeft().latitude.add(getRight().latitude).divide(2.0);
        double areaPBC = 2 * Math.PI * radius * (1.0 - Math.abs(bottom.sin())) * rate;

        double topLatitudeDegree = getTop().getLatitude().degrees;
        if (isUp() && topLatitudeDegree > 0 && 90.0 - topLatitudeDegree <= Const.EPSILON)
        {
            return areaPBC;
        }
        if (isUp() && topLatitudeDegree < 0 && 90.0 + topLatitudeDegree <= Const.EPSILON)
        {
            return areaPBC;
        }

        double topLongitudeDegree = getTop().getLongitude().degrees;
//        double leftLongitudeDegree = getLeft().getLongitude().degrees;
//        double rightLongitudeDegree = getRight().getLongitude().degrees;

        // 只考虑北半球
        // pole = (90,0)
        LatLon pole = LatLon.fromDegrees(90.0, 0.0);
        if (isUp())
        {
            if (topLongitudeDegree <= minLongitude)
            {
                // PAB
                double areaPAB = 0.0;
                if (topLongitudeDegree != minLongitude)
                {
                    areaPAB = Area.unitSphereSurfaceTriangleArea(pole, getTop(), getLeft());
                }
                // PAC
                double areaPAC = Area.unitSphereSurfaceTriangleArea(pole, getTop(), getRight());

                area = areaPAB + areaPBC - areaPAC;
            }
            else if (topLongitudeDegree > minLongitude && topLongitudeDegree < maxLongitude)
            {
                // PBA
                double areaPBA = Area.unitSphereSurfaceTriangleArea(pole, getLeft(), getTop());
                // PAC
                double areaPAC = Area.unitSphereSurfaceTriangleArea(pole, getTop(), getRight());

                area = areaPBC - areaPBA - areaPAC;
            }
            else
            {
                // topLongitudeDegree >= maxLongitude
                // PBA
                double areaPBA = Area.unitSphereSurfaceTriangleArea(pole, getLeft(), getTop());
                // PCA
                double areaPCA = 0.0;
                if (topLongitudeDegree != maxLongitude)
                {
                    areaPCA = Area.unitSphereSurfaceTriangleArea(pole, getLeft(), getTop());
                }

                area = areaPBC + areaPCA - areaPBA;
            }
        }
        else
        {
            // down
            if (topLongitudeDegree <= minLongitude)
            {
                // PAB
                double areaPAB = 0.0;
                if (topLongitudeDegree != minLongitude)
                {
                    areaPAB = Area.unitSphereSurfaceTriangleArea(pole, getTop(), getLeft());
                }
                // PAC
                double areaPAC = Area.unitSphereSurfaceTriangleArea(pole, getTop(), getRight());

                area = areaPAC - areaPAB - areaPBC;
            }
            else if (topLongitudeDegree > minLongitude && topLongitudeDegree < maxLongitude)
            {
                // PBA
                double areaPBA = Area.unitSphereSurfaceTriangleArea(pole, getLeft(), getTop());
                // PAC
                double areaPAC = Area.unitSphereSurfaceTriangleArea(pole, getTop(), getRight());

                area = areaPBA + areaPAC - areaPBC;
            }
            else
            {
                // topLongitudeDegree >= maxLongitude
                // PBA
                double areaPBA = Area.unitSphereSurfaceTriangleArea(pole, getLeft(), getTop());
                // PCA
                double areaPCA = 0.0;
                if (topLongitudeDegree != maxLongitude)
                {
                    areaPCA = Area.unitSphereSurfaceTriangleArea(pole, getRight(), getTop());
                }

                area = areaPBA - areaPBC - areaPCA;
            }
        }

        return area;
    }

    @Override
    public Path[] renderPath()
    {
        List<Position> positions = new ArrayList<>();
        // 两边为大圆弧 AVKey.GREAT_CIRCLE
        // 底边为纬线即恒向线 AVKey.RHUMB_LINE
        Path path1, path2;

        positions.add(new Position(getRight(), 0));
        positions.add(new Position(getTop(), 0));
        positions.add(new Position(getLeft(), 0));

        path1 = new Path(positions);
        path1.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        path1.setFollowTerrain(true);
        path1.setPathType(AVKey.GREAT_CIRCLE);
        path1.setAttributes(Const.defaultPathAttribute());

        path2 = new Path(new Position(getLeft(), 0), new Position(getRight(), 0));
        path2.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        path2.setFollowTerrain(true);
        path2.setPathType(AVKey.RHUMB_LINE);
        path2.setAttributes(Const.defaultPathAttribute());

        return new Path[] {path1, path2};
    }

    public double computeLoss()
    {
        if (isStrictQTM())
            return 0.0;
        double area = this.getUnitArea(true);

        QTMTriangle[] subs;
        if (subTriangles == null)
            subs = refine();
        else
            subs = subTriangles;

        double area0 = subs[0].getUnitArea(true);
        double area1 = subs[1].getUnitArea(true);
        double area2 = subs[2].getUnitArea(true);
        double area3 = subs[3].getUnitArea(true);
        return area - area0 - area1 - area2 - area3;
    }

    @Override
    public QTMTriangle[] refine()
    {
        // QTM
        if (subTriangles != null)
        {
            return subTriangles;
        }
        // 两边是大圆弧 GreatCircle
        // 底边沿着纬线  纬度不变，经度均分
        // 计算小圆弧中点
        // 此处采用简略方法：纬度不变，经度取中值

        LatLon A = getTop();
        LatLon B = getLeft();
        LatLon C = getRight();

        double latB = B.getLatitude().degrees;
        double latC = C.getLatitude().degrees;
        // double delta = latC-latB;
        LatLon aa = LatLon.interpolateRhumb(0.5, B, C);
        LatLon bb = LatLon.interpolateGreatCircle(0.5, A, C);
        LatLon cc = LatLon.interpolateGreatCircle(0.5, A, B);

        subTriangles = new QTMTriangle[4];
        Geocode[] geocodes = getGeocode().binaryRefine();
        subTriangles[0] = new QTMTriangle(aa, cc, bb, geocodes[0]);
        subTriangles[1] = new QTMTriangle(A, cc, bb, geocodes[1]);
        subTriangles[2] = new QTMTriangle(cc, B, aa, geocodes[2]);
        subTriangles[3] = new QTMTriangle(bb, aa, C, geocodes[3]);

        return subTriangles;
    }

    private QTMTriangle[] refine2()
    {
        if (subTriangles != null)
        {
            return subTriangles;
        }

        // 两边是大圆弧 GreatCircle
        // 底边沿着纬线  纬度不变，经度均分

        // 计算小圆弧中点
        // 此处采用简略方法：纬度不变，经度取中值

//        LatLon a = LatLon.interpolateRhumb(0.5, getGeoVertices().get(1), getGeoVertices().get(2));
//        LatLon b = LatLon.interpolateGreatCircle(0.5, getGeoVertices().get(0), getGeoVertices().get(2));
//        LatLon c = LatLon.interpolateGreatCircle(0.5, getGeoVertices().get(1), getGeoVertices().get(0));
//        String ID = this.getGeocode().getID();
//        ZhaoQTM[] subCells = new ZhaoQTM[4];

        LatLon pA, pB, pC;
        pA = getTop();
        pB = getLeft();
        pC = getRight();

        double midBottomLatitude = IO.check((pB.getLatitude().getRadians() + pC.getLatitude().getRadians()) / 2.0);
//        double bottomLatitude = IO.check((pC.getLatitude().add(pB.getLatitude())).divide(2.0).degrees, 4);
//        double midBottomLongitude = IO.check((pB.longitude.add(pC.longitude)).divide(2.0).degrees, 4);
        double pBLon = pB.getLongitude().getRadians();
        double pCLon = pC.getLongitude().getRadians();
        if (IO.check(pBLon + Math.PI) <= Const.EPSILON && IO.check(pCLon) > Const.EPSILON)
        {
            pBLon = Math.PI;
        }
        if (IO.check(Math.PI - pCLon) <= Const.EPSILON && IO.check(pBLon) < -Const.EPSILON)
        {
            pCLon = -Math.PI;
        }
        double midBottomLongitude = IO.check((pBLon + pCLon) / 2.0);

        LatLon bTemp = LatLon.interpolateGreatCircle(0.5, pA, pC);
        LatLon cTemp = LatLon.interpolateGreatCircle(0.5, pB, pA);
//        double bTLon = bTemp.getLongitude().getRadians();
//        double cTLon = cTemp.getLongitude().getRadians();
//        if (IO.check(cTLon + Math.PI) <= Const.EPSILON && IO.check(bTLon) > Const.EPSILON)
//        {
//            cTLon = Math.PI;
//        }
//        if (IO.check(Math.PI - bTLon) <= Const.EPSILON && IO.check(cTLon) < -Const.EPSILON)
//        {
//            bTLon = -Math.PI;
//        }
        System.out.println("bTemp\t" + bTemp);
        System.out.println("cTemp\t" + cTemp);
//        double latTbTcMid = IO.check((bTemp.getLatitude().add(cTemp.getLatitude())).divide(2.0).degrees, 4);
        double bTLat = bTemp.getLatitude().getRadians();
        double cTLat = cTemp.getLatitude().getRadians();
        double tbTcMidLat = IO.check((bTLat + cTLat) / 2.0);

        LatLon a;
        if (isStrictQTM())
        {
            a = LatLon.fromDegrees(pB.getLatitude().degrees, Math.toDegrees(midBottomLongitude));
        }
        else
        {
            // 在大圆弧上根据纬度算经度
//            a = LatLon.interpolateRhumb(0.5, pB, pC);
            double longitudeDegree = (QTMTriangle.calculateLongitude(pB, pC, Math.toDegrees(midBottomLatitude)));
//            System.out.println("pointALongitude");
//            longitudeDegree = IO.check(longitudeDegree, 4);
            a = LatLon.fromDegrees(Math.toDegrees(midBottomLatitude), longitudeDegree);
        }

        double bLongitudeDegree = QTMTriangle.calculateLongitude(pA, pC, Math.toDegrees(tbTcMidLat));
//        System.out.println("pointBLongitude");
        double cLongitudeDegree = QTMTriangle.calculateLongitude(pA, pB, Math.toDegrees(tbTcMidLat));
//        System.out.println("pointCLongitude");

        LatLon b = LatLon.fromDegrees(Math.toDegrees(tbTcMidLat), bLongitudeDegree);
        LatLon c = LatLon.fromDegrees(Math.toDegrees(tbTcMidLat), cLongitudeDegree);

//        QTMTriangle[] subCells = new QTMTriangle[4];
        subTriangles = new QTMTriangle[4];
        Geocode[] geocodes = getGeocode().binaryRefine();
        subTriangles[0] = new QTMTriangle(a, c, b, geocodes[0]);
        subTriangles[1] = new QTMTriangle(pA, c, b, geocodes[1]);
        subTriangles[2] = new QTMTriangle(c, pB, a, geocodes[2]);
        subTriangles[3] = new QTMTriangle(b, a, pC, geocodes[3]);

//        LatLon a = LatLon.interpolateRhumb(0.5, getGeoVertices().get(1), getGeoVertices().get(2));
//        LatLon b = LatLon.interpolateGreatCircle(0.5, getGeoVertices().get(0), getGeoVertices().get(2));
//        LatLon c = LatLon.interpolateGreatCircle(0.5, getGeoVertices().get(1), getGeoVertices().get(0));
//        String ID = this.getGeocode().getID();
//        ZhaoQTM[] subCells = new ZhaoQTM[4];
//        Geocode[] geocodes = this.geocode.binaryRefine();
//        subCells[0] = new ZhaoQTM(a, c, b, geocodes[0]);
//        subCells[1] = new ZhaoQTM(getGeoVertices().get(0), c, b, geocodes[1]);
//        subCells[2] = new ZhaoQTM(c, getGeoVertices().get(1), a, geocodes[2]);
//        subCells[3] = new ZhaoQTM(b, a, getGeoVertices().get(2), geocodes[3]);
//        return subCells;

        return subTriangles;
    }

    private static LatLon adjustAdjacentDateline(LatLon adjustPoint, LatLon referencePoint)
    {
        LatLon changePoint = null;
        if (LatLon.locationsCrossDateline(adjustPoint, referencePoint))
        {
            if (referencePoint.getLongitude().getRadians() > 0
                && IO.check(adjustPoint.getLongitude().getRadians() + Math.PI) <= Const.EPSILON)
            {
                changePoint = LatLon.fromDegrees(adjustPoint.getLatitude().degrees, 180);
            }
            if (referencePoint.getLongitude().getRadians() < 0
                && IO.check(Math.PI - adjustPoint.getLongitude().getRadians()) <= Const.EPSILON)
            {
                changePoint = LatLon.fromDegrees(adjustPoint.getLatitude().degrees, -180);
            }
        }
        return changePoint != null ? changePoint : adjustPoint;
    }

    public static double calculateLongitude(LatLon p1, LatLon p2, double lat)
    {
        //
        LatLon pp1, pp2;
        pp1 = QTMTriangle.adjustAdjacentDateline(p1, p2);
        pp2 = QTMTriangle.adjustAdjacentDateline(p2, p1);

        // 平面一般方程 ax+by+cz+d=0
        // 分别计算大圆弧平面和已知纬度的平面
//        double lat = Math.toRadians(latitudeDegree);
        double lon, lonResult;
        // 大圆弧过原点(0,0,0)
        // 大圆弧方程 Ax+By+Cz = 0；
        Vec4 vP1, vP2;
        if (p1.getLatitude().compareTo(p2.getLatitude()) >= 0)
        {
            vP1 = IO.latLonToVec4(p1);
            vP2 = IO.latLonToVec4(p2);
        }
        else
        {
            vP1 = IO.latLonToVec4(p2);
            vP2 = IO.latLonToVec4(p1);
        }

        Vec4 vP3 = vP1.cross3(vP2);
        double b = vP3.x;
        double c = vP3.y;
        double a = vP3.z;
        // ax+by+cz=0
        // a*cos(lat)*cos(lon)+b*cos(lat)*sin(lon)+c*sin(lat)=0
        // 计算经度，直接三角函数，或者
        // 利用“辅助角公式”
        // a*sinx+b*cosx=sqrt(a^2+b^2)*sin(x+atan2(b,a))
        double aa = b * Math.cos(lat);
        double bb = a * Math.cos(lat);
        double cc = c * Math.sin(lat);
        // aa*sin(lon)+bb*cos(lon)+cc=0;
        if (aa == 0 && bb != 0)
        {
            // aa*sin(lon)+bb*cos(lon)+cc=0;
            double temp = Math.acos(-1 * cc / bb);
            return IO.check(temp);
        }
        if (bb == 0 && aa != 0)
        {
            // aa*sin(lon)+bb*cos(lon)+cc=0;
            double temp = Math.asin(-1 * cc / aa);
            return IO.check(temp);
        }

        // aa*sin(lon)+bb*cos(lon)+cc=0;
        // a*sinx+b*cosx=sqrt(a^2+b^2)*sin(x+atan2(b,a))
        // sqrt(a^2+b^2)*sin(x+atan2(b,a))+cc=0
        double arctan = Math.atan2(bb, aa);
//        lonRadian = Math.asin(-1 * cc / Math.sqrt(Math.pow(aa, 2) + Math.pow(bb, 2))) - arctan;
        double t1 = -1 * cc;
        double t2 = Math.sqrt(Math.pow(aa, 2) + Math.pow(bb, 2));
        double t3 = t1 / t2;
        double t4 = Math.asin(t3);
        lon = t4 - arctan;
        lon = IO.check(lon);

        if (Math.abs(lon) <= Const.EPSILON)
        {
            lonResult = 0.0;
        }
        else
        {
            double minLongitude = (Math.min(p1.getLongitude().radians, p2.getLongitude().radians));
            minLongitude = IO.check(minLongitude);
            double maxLongitude = (Math.max(p1.getLongitude().radians, p2.getLongitude().radians));
            maxLongitude = IO.check(maxLongitude);

            if (lon >= minLongitude && lon <= maxLongitude)
            {
                lonResult = lon;
            }
            else if (lon < minLongitude)
            {
                lonResult = lon + Math.PI;
            }
            else
            {
                // lon > maxLongitude
                lonResult = lon - Math.PI;
            }
        }

        return IO.check(lonResult);
//        return 0.0;
    }

    private void test()
    {

    }

    private static double calculateLongitude3(LatLon p1, LatLon p2, double latitudeDegree)
    {
        // 平面一般方程 ax+by+cz+d=0
        // 分别计算大圆弧平面和已知纬度的平面
        double lat = Math.toRadians(latitudeDegree);
        double lon, lonResult;
        // 大圆弧过原点(0,0,0)
        // 大圆弧方程 Ax+By+Cz = 0；
        Vec4 vP1, vP2;
        if (p1.getLatitude().compareTo(p2.getLatitude()) >= 0)
        {
            vP1 = IO.latLonToVec4(p1);
            vP2 = IO.latLonToVec4(p2);
        }
        else
        {
            vP1 = IO.latLonToVec4(p2);
            vP2 = IO.latLonToVec4(p1);
        }
        Vec4 vP3 = vP1.cross3(vP2);
        double b = vP3.x;
        double c = vP3.y;
        double a = vP3.z;
        // ax+by+cz=0
        // a*cos(lat)*cos(lon)+b*cos(lat)*sin(lon)+c*sin(lat)=0
        // 计算经度，直接三角函数，或者
        // 利用“辅助角公式”
        // a*sinx+b*cosx=sqrt(a^2+b^2)*sin(x+atan2(b,a))
        double aa = b * Math.cos(lat);
        double bb = a * Math.cos(lat);
        double cc = c * Math.sin(lat);
        // aa*sin(lon)+bb*cos(lon)+cc=0;
        if (aa == 0 && bb != 0)
        {
            // aa*sin(lon)+bb*cos(lon)+cc=0;
            double temp = Math.acos(-1 * cc / bb);
            return IO.check(Math.toDegrees(temp), 4);
        }
        if (bb == 0 && aa != 0)
        {
            // aa*sin(lon)+bb*cos(lon)+cc=0;
            double temp = Math.asin(-1 * cc / aa);
            return IO.check(Math.toDegrees(temp), 4);
        }

        // aa*sin(lon)+bb*cos(lon)+cc=0;
        // a*sinx+b*cosx=sqrt(a^2+b^2)*sin(x+atan2(b,a))
        // sqrt(a^2+b^2)*sin(x+atan2(b,a))+cc=0
        double arctan = Math.atan2(bb, aa);
//        lonRadian = Math.asin(-1 * cc / Math.sqrt(Math.pow(aa, 2) + Math.pow(bb, 2))) - arctan;
        double t1 = -1 * cc;
        double t2 = Math.sqrt(Math.pow(aa, 2) + Math.pow(bb, 2));
        double t3 = t1 / t2;
        double t4 = Math.asin(t3);
        lon = t4 - arctan;
        lon = IO.check(lon);

        if (Math.abs(lon) <= Const.EPSILON)
        {
            lonResult = 0.0;
        }
        else
        {
            double minLongitude = (Math.min(p1.getLongitude().radians, p2.getLongitude().radians));
            minLongitude = IO.check(minLongitude);
            double maxLongitude = (Math.max(p1.getLongitude().radians, p2.getLongitude().radians));
            maxLongitude = IO.check(maxLongitude);

            if (lon >= minLongitude && lon <= maxLongitude)
            {
                lonResult = lon;
            }
            else if (lon < minLongitude)
            {
                lonResult = lon + Math.PI * 2;
            }
            else
            {
                lonResult = lon - 2 * Math.PI;
            }
        }

        return IO.check(Math.toDegrees(lonResult));
//        return 0.0;
    }

    private static double calculateLongitude2(LatLon p1, LatLon p2, double latitudeDegree)
    {
        // 平面一般方程 ax+by+cz+d=0
        // 分别计算大圆弧平面和已知纬度的平面
        double longitudeRadian, longitudeResult;
        // 大圆弧过原点(0,0,0)
        // 大圆弧方程 Ax+By+Cz = 0；
        Vec4 vP1, vP2;
        if (p1.getLatitude().compareTo(p2.getLatitude()) >= 0)
        {
            vP1 = IO.latLonToVec4(p1);
            vP2 = IO.latLonToVec4(p2);
        }
        else
        {
            vP1 = IO.latLonToVec4(p2);
            vP2 = IO.latLonToVec4(p1);
        }
        Vec4 vP3 = vP1.cross3(vP2);
        double A = vP3.x;
        double B = vP3.y;
        double C = vP3.z;
        // coordinates are the same as IO.vec4ToLatLon.
        // 小圆弧方程 y = r×sin(theta)
        // A*cos()*
//        double y = Math.sin(Math.toRadians(latitudeDegree));
        //
        double cosLatitude = Math.cos(Math.toRadians(latitudeDegree));
        double sinLatitude = Math.sin(Math.toRadians(latitudeDegree));
        // 计算经度，直接三角函数，或者
        // 利用“辅助角公式”
        // a*sinx+b*cosx=sqrt(a^2+b^2)*sin(x+atan2(b,a))
        double a = A * cosLatitude;
        double b = C * cosLatitude;
        // a*sin(longitude)+b*cos(longitude)+B*sin(latitude)=0
//        if a1==0
//        longitude=asin(-c1*tan(B)/b1);
//        end
//        if b1==0
//        longitude=acos(-c1*tan(B)/a1);
//        end
        if (a == 0 && b == 0)
        {
            return 0.0;
        }
        if (a == 0 && b != 0)
        {
            // longitudeResult
//            System.out.println("===============");
//            System.out.println("a\t=\t0");
            double temp = Math.acos(-1 * B * sinLatitude / b);
            return IO.check(Math.toDegrees(temp), 4);
        }
        if (b == 0 && a != 0)
        {
            // longitudeResult
//            System.out.println("==============");
//            System.out.println("b\t=\t0");
            double temp = Math.asin(-1 * B * sinLatitude / a);
            return IO.check(Math.toDegrees(temp), 4);
        }

        double atan2 = Math.atan2(b, a);

//        longitudeRadian = (Math.asin(-1 * B * sinLatitude / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2))) - atan2);
        double t1 = -1 * B * sinLatitude;
        double t2 = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        double t3 = t1 / t2;
        double t4 = Math.asin(t3);
        longitudeRadian = t4 - atan2;
//        double temp = longitudeRadian;
        longitudeRadian = IO.check(longitudeRadian);

        if (Math.abs(Math.toDegrees(longitudeRadian)) <= Const.EPSILON)
        {
            longitudeResult = 0.0;
        }
        else
        {
            double minLongitude = (Math.min(p1.getLongitude().radians, p2.getLongitude().radians));
            minLongitude = IO.check(minLongitude);
            double maxLongitude = (Math.max(p1.getLongitude().radians, p2.getLongitude().radians));
            maxLongitude = IO.check(maxLongitude);

//            longitudeResult = longitudeRadian >= minLongitude && longitudeRadian <= maxLongitude ?
//                longitudeRadian : longitudeRadian < minLongitude ? longitudeRadian + 2*Math.PI
//                : longitudeRadian - 2*Math.PI;
            if (longitudeRadian >= minLongitude && longitudeRadian <= maxLongitude)
            {
                longitudeResult = longitudeRadian;
            }
            else if (longitudeRadian < minLongitude)
            {
                longitudeResult = longitudeRadian + Math.PI * 2;
            }
            else
            {
                longitudeResult = longitudeRadian - 2 * Math.PI;
            }
        }

//        System.out.println("==============");
//        System.out.println("a\t!=\t0,b\t!=\t0");

        return IO.check(Math.toDegrees(longitudeResult));
    }

    @Override
    public List<Double> edgeLengths()
    {
        List<Double> edges = new ArrayList<>();
        double delt = Math.abs(getLeft().longitude.radians - getRight().longitude.radians);

        edges.add(Const.RADIUS * getRight().latitude.cos() * delt);// a
        edges.add(LatLon.greatCircleDistance(getTop(), getRight()).radians * Const.RADIUS);// b
        edges.add(LatLon.greatCircleDistance(getLeft(), getTop()).radians * Const.RADIUS);// c
        return edges;
    }

    @Override
    public List<Angle> innerAngle()
    {
        List<Angle> angles = new ArrayList<>();
        angles.add(SurfaceTriangle.computeAngleA(getTop(), getLeft(), getRight()));
        angles.add(bottomAngle(getLeft(), getTop()));
        angles.add(bottomAngle(getTop(), getRight()));
        return angles;
    }

//    @Override
//    public double angleStandardDeviation()
//    {
//        double A, B, C, avg, AA, BB, CC, stdd;
//        A = innerAngle().get(0).degrees;
//        B = innerAngle().get(1).degrees;
//        C = innerAngle().get(2).degrees;
//        avg = (A + B + C) / 3.0;
//        AA = Math.pow(A - avg, 2);
//        BB = Math.pow(B - avg, 2);
//        CC = Math.pow(C - avg, 2);
//        stdd = Math.sqrt((AA + BB + CC) / 3.0);
//        return stdd;
//    }
}
