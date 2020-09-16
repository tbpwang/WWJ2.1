/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

import java.util.List;

/**
 * @author Zheng WANG
 * @create 2019/9/2
 * @description 根据 Raskin（1994），“Spatial Analysis on the Sphere: A Review (94-7)”
 */
public class SphereStatisticsPoint
{
    //  locations of Point are on the sphere surface
    private List<Vec4> points;
    private int size;
    // statistics:
    private Vec4 sphericalMean;
    private Vec4 resultant;
    private Vec4 massCenter;
    // private Vec4 sphericalMedian;

    // the distance of the center of mass (4.1.3) from the sphere surface
    // as: spherical standard deviation
    private double dispersion;
    private boolean dispersionFlag;
    // Another measure of dispersion with the matrix of second moments (or orientation matrix) T
    // Note:while Matrix is 4*4, T is 3*3.
    private Matrix matrixT;

    //* For large sample sizes, the standard error(σ,OR sigma) of the estimate of the spherical mean can be
    //* estimated using the Central Limit Theorem
    //* this quantity is often incorrectly referred to in the literature as a spherical variance
    // private double sigma;

    //* Confidence intervals
    // private double confidenceIntervals;

    //* Regression and correlation
    //* The general spherical regression prob1em is to find the best fitting orthogonal matrix A such that
    //* y= Ax
    //* where x and y are points on the sphere.

    //* correlation coefficient p

    public SphereStatisticsPoint(List<Vec4> points)
    {
        if (points == null || points.size() == 0)
        {
            String message = Logging.getMessage("Error.空值：不能计算空值数据集");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.points = points;
        this.size = points.size();
        this.dispersionFlag = false;
    }

    private void setResultant()
    {
        double x = 0, y = 0, z = 0;
        for (Vec4 v :
            this.points)
        {
            x += v.getX();
            y += v.getY();
            z += v.getZ();
        }

        this.resultant = IO.check(x, y, z);
    }

    public Vec4 getResultant()
    {
        if (resultant == null)
        {
            setResultant();
        }
        return resultant;
    }

    private void setSphericalMean()
    {
        double length = IO.check(getResultant().getLength3()) <= Const.EPSILON ? 1.0 : getResultant().getLength3();
        Vec4 v = getResultant().divide3(length);
        this.sphericalMean = IO.check(v);
    }

    public Vec4 getSphericalMean()
    {
        if (sphericalMean == null)
        {
            setSphericalMean();
        }
        return sphericalMean;
    }

    private void setMassCenter()
    {
        Vec4 v = getSphericalMean().multiply3(getResultant().getLength3() / this.size);
        this.massCenter = IO.check(v);
    }

    public Vec4 getMassCenter()
    {
        if (this.massCenter == null)
        {
            setMassCenter();
        }
        return massCenter;
    }

//    public Vec4 getSphericalMedian()
//    {
//       TODO: 2019/9/2
//        return sphericalMedian;
//    }

    private void setDispersion()
    {
        if (!dispersionFlag)
        {
            double rdn = getResultant().getLength3() / this.size;
            this.dispersion = 1.0 - rdn;
            dispersionFlag = true;
        }
    }

    public double getDispersion()
    {
        return Const.getGlobe().getRadius() * getDispersionUnit();
    }

    public double getDispersionUnit()
    {
        setDispersion();
        return this.dispersion;
    }

    private void setMatrixT()
    {
        // 3*3
        double r11 = 0.0, r12 = 0.0, r13 = 0.0;
        double r22 = 0.0, r23 = 0.0;
        double r33 = 0.0;
        // 填充 0 形成4*4
        final double r14 = 0.0;
        final double r24 = 0.0;
        final double r34 = 0.0;
        final double r44 = 0.0;
        for (Vec4 v :
            this.points)
        {
            double x, y, z;
            x = v.getX();
            y = v.getY();
            z = v.getZ();
            r11 += x * x;
            r12 += x * y;
            r13 += x * z;
            r22 += y * y;
            r23 += y * z;
            r33 += z * z;
        }
        r11 = IO.check(r11 / size);
        r12 = IO.check(r12 / size);
        r13 = IO.check(r13 / size);
        r22 = IO.check(r22 / size);
        r23 = IO.check(r23 / size);
        r33 = IO.check(r33 / size);

        this.matrixT = new Matrix(r11, r12, r13, r14, r12, r22, r23, r24, r13, r23, r33, r34, r14, r24, r34, r44);
    }

    public Matrix getMatrixT()
    {
        if (this.matrixT == null)
        {
            setMatrixT();
        }
        return matrixT;
    }
}
