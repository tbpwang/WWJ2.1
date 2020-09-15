/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.*;

/**
 * @author Zheng WANG
 * @create 2020/6/19 14:23
 * @description 构造球面小圆弧
 * @parameter 1)小圆弧两端点坐标: first && last， 2)相对极点坐标: pole， 3)相对方位角: coLatitude;
 */

public class SmallCircle extends GridEdge
{
    private LatLon pole;
    private Angle coLatitude;
    private boolean hasCoLatitude;

    public SmallCircle(LatLon first, LatLon last)
    {
        super(first,last);
        pole = null;
        coLatitude = null;
        hasCoLatitude = false;
    }

    public SmallCircle(LatLon first, LatLon last, LatLon relativePole)
    {
        super(first, last);
        Angle angle1, angle2;
        angle1 = LatLon.greatCircleDistance(relativePole, first);
        angle2 = LatLon.greatCircleDistance(relativePole, last);
        double dAngle = IO.check(Math.abs(angle1.subtract(angle2).radians));
//        double dtest = Math.abs(angle1.subtract(angle2).degrees);
        if (dAngle > Cons.EPSILON)
        {
            String message = Logging.getMessage("ErrorSmallCirclePole:小圆弧的相对极点错误！");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        this.pole = IO.check(relativePole);
        hasCoLatitude = true;
        this.coLatitude = Angle.fromRadians(angle1.radians);
    }

    public Angle getCoLatitude()
    {
        return coLatitude;
    }

    public void  setCoLatitude(Angle coLatitude)
    {
        if (isHasCoLatitude())
        {
            String message = Logging.getMessage("ReSetSmallCirclePole:小圆弧的相对极已经存在，无法重复设置！");
            Logging.logger().severe(message);
            return;
        }
        this.coLatitude = coLatitude;
    }

    public boolean isHasCoLatitude()
    {
        return hasCoLatitude;
    }

    public void setPole(LatLon relativePole)
    {
        if (isHasCoLatitude())
        {
            String message = Logging.getMessage("ReSetSmallCirclePole:小圆弧的相对极已经存在，无法重复设置！");
            Logging.logger().severe(message);
            return;
        }
        Angle angle1, angle2;
        angle1 = LatLon.greatCircleDistance(relativePole, getFirst().latLon);
        angle2 = LatLon.greatCircleDistance(relativePole, getLast().latLon);
        double dAngle = IO.check(Math.abs(angle1.subtract(angle2).radians));
        if (dAngle > Cons.EPSILON)
        {
            String message = Logging.getMessage("ErrorSmallCirclePole:小圆弧的相对极设置错误！");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.pole = relativePole;
        hasCoLatitude = true;
        coLatitude = angle1;
    }

    public LatLon getPole()
    {
        return pole;
    }

    public Vec4 getCircleCenter()
    {
        return isHasCoLatitude() ? IO.latLonToVec4(getPole()).multiply3(getCoLatitude().cos()) : null;
    }

    public LatLon getMiddlePoint()
    {
        if (!isHasCoLatitude())
            return null;
        Vec4 smallCenter = getCircleCenter();

        Vec4 pA = getFirst().vec4;
        Vec4 pB = getLast().vec4;
        Vec4 mAB = pA.add3(pB).divide3(2.0);
        Vec4 ddPoint = mAB.subtract3(smallCenter).normalize3().multiply3(getArcRadius());
        Vec4 middle = ddPoint.add3(smallCenter);
        return IO.check(IO.vec4ToLatLon(middle));
    }

    public double getArcRadius()
    {
        return isHasCoLatitude() ? Math.abs(getCoLatitude().sin()) : -1.0;
    }

    public double crownHeight()
    {
        return isHasCoLatitude() ? (1.0 - getCoLatitude().cos()) : -1.0;
    }

    public double getLength()
    {
        if (!isHasCoLatitude())
            return Double.NaN;
        double r = IO.check(getArcRadius());

        if (r <= Cons.EPSILON)
        {
            return 0.0;
        }
        Vec4 first = getFirst().vec4;
        Vec4 last = getLast().vec4;
        double halfDis = first.distanceTo3(last) / 2.0;

        double angle = 2.0 * Math.asin(halfDis / r);
        return angle * r;
    }

    public SmallCircle[] bisect()
    {
        if (!isHasCoLatitude())
            return null;
        LatLon middle = getMiddlePoint();
        SmallCircle partOne = new SmallCircle(getFirst().latLon, middle, getPole());
        SmallCircle partTwo = new SmallCircle(middle, getLast().latLon, getPole());
        SmallCircle[] parts = new SmallCircle[2];
        parts[0] = partOne;
        parts[1] = partTwo;
        return parts;
    }

    public SmallCircle inverse()
    {
        return isHasCoLatitude() ? new SmallCircle(getLast().latLon, getFirst().latLon, getPole())
            : new SmallCircle(getLast().latLon, getFirst().latLon);
    }
}
