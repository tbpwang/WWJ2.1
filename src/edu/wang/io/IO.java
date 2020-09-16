/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.io;

import edu.wang.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/5/28
 * @description
 * @parameter
 */
public class IO
{
//    private static final int PRECISION = 6;
    private static boolean checkPath(String filePath)
    {
        File file = new File(filePath);
        //File parentFile = file.getParentFile();
        if (!file.exists())
        {
            return file.mkdirs();
        }
        return true;
    }

    public static void write(String folderName, String title, String content)
    {
        String path = "D:\\outData\\" + folderName + "\\";
        if (!checkPath(path))
        {
            String message = Logging.getMessage("FileError.创建文件夹出错");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        FileWriter rt;
        String pathFileName = path + title + ".txt";
        // String outContent = "   " + content;
        try
        {
            rt = new FileWriter(pathFileName, true);

            rt.write(content);
            rt.write(System.lineSeparator());

            rt.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void write(String folderName, Cell cell)
    {
        String path = "D:\\outData\\" + folderName + "\\";
        if (!checkPath(path))
        {
            String message = Logging.getMessage("FileError.创建文件夹出错");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        FileWriter rt;
        String pathFileName = path + cell.getLevel() + ".txt";
        try
        {
            rt = new FileWriter(pathFileName, true);
            rt.write(cell.toString());
            rt.write(System.lineSeparator());
            rt.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void write(String folderName, Cell[] cells)
    {
        if (cells != null)
            for (Cell cell : cells)
            {
                write(folderName, cell);
            }
    }

    public static void write(String folderName, List<Cell> cellList)
    {
        if (cellList != null)
            for (Cell cell : cellList)
            {
                write(folderName, cell);
            }
    }


    public static String formatDouble(double doubleValue, int precision)
    {
        if (Double.isNaN(doubleValue))
        {
            String message = Logging.getMessage("valueError.NullValue,IO.formatDouble");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        StringBuilder pattern = new StringBuilder("0.");
        String c = "0";

        if (precision <= 0)
        {
            precision = Const.PRECISION;
        }
        for (int i = 0; i < precision; i++)
        {
            pattern.append(c);
        }
//        pattern.append(c.repeat(decimals));
//        String string = new DecimalFormat(pattern.toString()).format(doubleValue);
//        System.out.println(string + "\t" + doubleValue);
//        return string;
        return new DecimalFormat(pattern.toString()).format(doubleValue);
    }

    public static String formatDouble(double doubleValue)
    {
        return formatDouble(doubleValue, Const.PRECISION);
//        String pattern = "0.000000";
////        String pattern = Math.abs(number) >= 100 ? "0.00000000E0" : "0.00000000";
//        return new DecimalFormat(pattern).format(number);
    }

    public static List<Vec4> parsePointsToVec(String fileName)
    {
        // 读取txt文档，该文档中存储了Vec4的点，
        // 每行一个点，格式为 x y z
        File file = new File(fileName);
        BufferedReader reader = null;
        //StringBuffer stringBuffer = new StringBuffer();
        List<Vec4> parseVec = new ArrayList<>();
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null)
            {
                //stringBuffer.append(tempStr);
                String[] str = tempStr.split("\t");
                parseVec.add(
                    new Vec4(Double.parseDouble(str[0]), Double.parseDouble(str[1]), Double.parseDouble(str[2])));
                //System.out.println(vec4);
            }
            reader.close();
//            return stringBuffer.toString();
            return parseVec;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        return parseVec;
    }

    public static double check(double doubleValue, int precision)
    {
        return Double.parseDouble(formatDouble(doubleValue, precision));
        //return Math.abs(value) <= epsilon ? 0.0 : value;
    }

    public static double check(double doubleValue)
    {
        return check(doubleValue, Const.PRECISION);
        //return Math.abs(value) <= epsilon ? 0.0 : value;
    }

//    public static LatLon check(LatLon latLon)
//    {
//        double latitude = check(latLon.latitude.radians);
//        double longitude = check(latLon.longitude.radians);
//        return LatLon.fromRadians(latitude, longitude);
//    }

    public static Vec4 check(double x, double y, double z)
    {
        return new Vec4(check(x), check(y), check(z));
    }

    public static Vec4 check(Vec4 vec4)
    {
        return check(vec4.getX(), vec4.getY(), vec4.getZ());
//        double x, y, z;
//        x = Double.parseDouble(IO.formatDouble(vec4.getX()));
//        y = Double.parseDouble(IO.formatDouble(vec4.getY()));
//        z = Double.parseDouble(IO.formatDouble(vec4.getZ()));
//        return new Vec4(x,y,z);
//        boolean changed = false;
//        if (Math.abs(x) <= epsilon)
//        {
//            x = 0.0;
//            changed = true;
//        }
//        if (Math.abs(y) <= epsilon)
//        {
//            y = 0.0;
//            changed = true;
//        }
//        if (Math.abs(z) <= epsilon)
//        {
//            z = 0.0;
//            changed = true;
//        }
//        return changed ? new Vec4(x, y, z) : vec4;
    }

    public static LatLon vec4ToLatLon(Vec4 vec4)
    {
        double phi = Math.asin(vec4.getY());
        double lambda = Math.atan2(vec4.getX(), vec4.getZ());

        return LatLon.fromRadians(phi, lambda);
    }

    public static Vec4 latLonToVec4(LatLon latLon)
    {
        // 在单位圆上转换
        PolarPoint point = PolarPoint.fromDegrees(latLon.getLatitude().degrees, latLon.getLongitude().degrees, 1.0);
        return check(point.toCartesian());

//        double x, y, z;
//        double phi = latLon.getLatitude().radians;
//        double theta = latLon.getLongitude().radians;
//        x = Math.cos(phi) * Math.cos(theta);
//        y = Math.cos(phi) * Math.sin(theta);
//        z = Math.sin(phi);
//        return check(x, y, z);
    }

    public static Vec4 resultant(List<LatLon> points)
    {
        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        Vec4 temp;
        for (LatLon p : points)
        {
            temp = latLonToVec4(p);
            sumX += temp.getX();
            sumY += temp.getY();
            sumZ += temp.getZ();
        }
        return check(sumX, sumY, sumZ);
    }

    public static Vec4 sphericalMean(List<LatLon> points)
    {
        // 球面平均位置
        Vec4 resultant = resultant(points);
        double resultantLength = resultant.getLength3();
//        double x, y, z;
//        x = resultant.getX() / resultantLength;
//        y = resultant.getY() / resultantLength;
//        z = resultant.getZ() / resultantLength;
        return check(resultant.getX() / resultantLength, resultant.getY() / resultantLength,
            resultant.getZ() / resultantLength);
    }

    public static Vec4 massCenter(List<LatLon> points)
    {
        // 质心
        double resultantLength = resultant(points).getLength3();
        int pointNumber = points.size() == 0 ? 1 : points.size();
        double r = resultantLength / pointNumber;
        Vec4 mean = sphericalMean(points);
        return check(r * mean.getX(), r * mean.getY(), r * mean.getZ());
    }

    public static List<LatLon> changeTriangleVertexes(Triangle triangle)
    {
        if (triangle == null)
        {
            String msg = Logging.getMessage("NullValue.三角形为空");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        LatLon top, left, right;
        top = vec4ToLatLon(triangle.getA());
        left = vec4ToLatLon(triangle.getB());
        right = vec4ToLatLon(triangle.getC());
        List<LatLon> vertexes = new ArrayList<>();
        vertexes.add(top);
        vertexes.add(left);
        vertexes.add(right);
        return vertexes;
    }

    public static int sortByRow(Geocode code1, Geocode code2)
    {

        int diff1 = code1.getRow() - code2.getRow();
        int diff2 = code1.getColumn() - code2.getColumn();
        if (diff1 == 0)
        {
            return diff2;
        }
        return diff1;
    }
    public static double asInt(double adouble)
    {
        double aint = Math.rint(adouble);
        double delta = Math.abs(aint - adouble);
        if (delta <= Const.EPSILON)
        {
            return aint;
        }
        else
        {
            return adouble;
        }
    }
    public static LatLon check(LatLon latLon)
    {
        if (latLon == null)
            return null;
        double lat = asInt(latLon.getLatitude().getRadians());
        double lon = asInt(latLon.getLongitude().getRadians());
        return LatLon.fromRadians(lat, lon);
    }
}
