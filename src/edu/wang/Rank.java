/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import java.io.Serializable;

/**
 * @author Zheng WANG
 * @create 2019/5/16
 * @description row, column, ID, baseID
 * @parameter
 */
public interface Rank extends Serializable
{
//    void setBaseID(String baseID);
    String getBaseID();
    void setRow(int row);
    int getRow();
    void setColumn(int column);
    int getColumn();
//    private String BaseID;
//    private int row, column;
//
//    public Number(String baseID, int row, int column)
//    {
//        BaseID = baseID;
//        this.row = row;
//        this.column = column;
//    }
//
//    public String getBaseID()
//    {
//        return BaseID;
//    }
//
//    public int getRow()
//    {
//        return row;
//    }
//
//    public int getColumn()
//    {
//        return column;
//    }
}
