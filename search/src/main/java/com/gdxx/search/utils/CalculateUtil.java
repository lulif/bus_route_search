package com.gdxx.search.utils;

/**
 * @program: BusRouteSearch
 * @author: lulif
 * @create: 2021-02-12 22:02
 * @description:
 **/
public class CalculateUtil {

    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 根据经纬度两点距离(返回千米)
     *
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     * @return
     */
    public static double getDistance(double latitude1, double longitude1,
                                     double latitude2, double longitude2) {
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        double a = lat1 - lat2;
        double b = lng1 - lng2;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        String str = String.format("%.2f", s);
        return Double.parseDouble(str);
    }
}
