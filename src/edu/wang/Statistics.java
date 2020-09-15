/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import java.util.List;

public class Statistics
{
    public static double computeStandardDeviation(List<Double> doubles)
    {
        // compute standard deviation of all
        // ie:divide Number
        int size = doubles.size();
        double sum = 0.0;
        if (size == 0)
        {
            return sum;
        }
        for (Double d :
            doubles)
        {
            sum += d;
        }
        double avg = sum / size;
        sum = 0.0;
        for (Double d :
            doubles)
        {
            sum += Math.pow(d - avg, 2);
        }
        avg = sum / size;
        return Math.sqrt(avg);
    }
}
