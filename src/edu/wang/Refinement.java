/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

/**
 * @author Zheng WANG
 * @create 2019/4/29 10:08
 * @description 用来细分单元格或者平面多边形
 * @parameter
 */
public interface Refinement
{
    Cell[] refine();
}
