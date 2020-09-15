/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import gov.nasa.worldwind.util.Logging;

import java.util.Objects;
import java.util.regex.*;

/**
 * @author Zheng WANG
 * @create 2019/5/13 10:24
 * @description 单元格的地理编码以及行列编码，适用于二分边法的三角形剖分
 * @parameter row行号，默认初始值为1；column列号，默认初始值为1；ID剖分编码 左下角为坐标原点，沿着三角的边向右为x，斜向上为y
 */
public class Geocode implements Rank
{
    private int row, column;
    private String ID;

    public Geocode()
    {
        this("");
    }

    public Geocode(String ID)
    {
//        if (ID.equals(""))
//        {
//            this.ID = "";
//            row = -1;
//            column = -1;
//            return;
//        }
        this.ID = ID;
        if (!hasNumber(ID))
        {
            row = 1;
            column = 1;
        }else
        {
            row = -1;
            column = -1;
        }
//        if (contains(OctahedronBaseID.class, ID.substring(0, 1)))
//        {
//
//                row = 1;
//                column = 1;
//        }
//        else
//        {
////            String message = Logging.getMessage("BaseIDError.IDIsNotInBaseID");
////            Logging.logger().severe(message);
////            throw new IllegalArgumentException(message);
//            this.ID = ID;
//            row = -1;
//            column = -1;
//        }
    }

    private boolean hasNumber(String content)
    {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public Geocode(int row, int column, String ID)
    {
//        if (contains(OctahedronBaseID.class, ID.substring(0, 1)))
//        {
//            this.ID = ID;
//            this.row = row;
//            this.column = column;
//        }
//        else
//        {
//            String message = Logging.getMessage("BaseIDError.ID不在基本ID中");
//            Logging.logger().severe(message);
//            throw new IllegalArgumentException(message);
//        }
        this.ID = ID;
        this.row = row;
        this.column = column;
    }

    private boolean contains(Class<? extends Enum> clazz, String value)
    {
        Object[] arr = clazz.getEnumConstants();
        for (Object e : arr)
        {
            if (((Enum) e).name().equals(value))
            {
                return true;
            }
        }
        return false;
    }

    public String getID()
    {
        return ID;
    }

    public void setID(String ID)
    {
//        if (contains(OctahedronBaseID.class, ID.substring(0, 1)))
//        {
//            this.ID = ID;
//        }
//        else
//        {
//            String message = Logging.getMessage("Value.Error.BaseID不匹配");
//            Logging.logger().severe(message);
//            throw new IllegalArgumentException(message);
//        }
        this.ID = ID;
    }

    public int getRow()
    {
        return row;
    }

    public String getBaseID()
    {
        return ID.substring(0, 1);
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getColumn()
    {
        return column;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public boolean isUp()
{
    String id = getID();

    char[] chars = id.toCharArray();
    int counter = 0;
    char centerCode = '0';
    for (char c : chars)
    {
        if (c == centerCode)
            counter++;
    }
    return counter % 2 == 0;
}

    public Geocode[] binaryRefine()
    {
        // 0,1,2,3
        Geocode[] geocode = new Geocode[4];
        geocode[0] = new Geocode(ID + "0");
        geocode[1] = new Geocode(ID + "1");
        geocode[2] = new Geocode(ID + "2");
        geocode[3] = new Geocode(ID + "3");
        int row, column;
        row = 2 * this.row;
        column = 2 * this.column;

        if (isUp())
        {
            // (2*i-1,2*j)
            geocode[0].setRow(row - 1);
            geocode[0].setColumn(column);
            // (2*i,2*j-1)
            geocode[1].setRow(row);
            geocode[1].setColumn(column - 1);
            // (2*i-1,2*j-1)
            geocode[2].setRow(row - 1);
            geocode[2].setColumn(column - 1);
            // (2*j-1,2*j+1))
            geocode[3].setRow(row - 1);
            geocode[3].setColumn(column + 1);
        }
        else
        {
            // (2i,2j-1)
            geocode[0].setRow(row);
            geocode[0].setColumn(column - 1);
            // (2i-1,2j)
            geocode[1].setRow(row - 1);
            geocode[1].setColumn(column);
            // (2i,2j-2)
            geocode[2].setRow(row);
            geocode[2].setColumn(column - 2);
            // (2i,2j)
            geocode[3].setRow(row);
            geocode[3].setColumn(column);
        }
        return geocode;
    }

    public int compareTo(Geocode other)
    {
        if (this.equals(other))
            return 0;
        if (this.getID().length() > other.getID().length())
            return 1;
        if (this.getID().length() < other.getID().length())
            return -1;
        int diff1 = this.getRow() - other.getRow();
        int diff2 = this.getColumn() - other.getColumn();
        if (diff1 == 0)
        {
            return diff2 >= 0 ? diff2 > 0 ? 1 : 0 : -1;
        }
        return diff1 > 0 ? 1 : -1;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Geocode geocode = (Geocode) o;
        return row == geocode.row &&
            column == geocode.column &&
            ID.equals(geocode.ID);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(row, column, ID);
    }

    @Override
    public String toString()
    {
        return "(" + row + "," + column + ")/" + getID();
    }
}
