package com.timestored.sqldash.chart;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class SqlHelper {
    private static final Set<Integer> NUMBERS;
    private static final Set<Integer> TEMPORALS;
    private static final Set<Integer> STRINGYS;

    static {
        Set<Integer> nums = new HashSet<Integer>();
        nums.add(Integer.valueOf(-5));
        nums.add(Integer.valueOf(3));
        nums.add(Integer.valueOf(8));
        nums.add(Integer.valueOf(6));
        nums.add(Integer.valueOf(4));
        nums.add(Integer.valueOf(4));
        nums.add(Integer.valueOf(2));
        nums.add(Integer.valueOf(7));
        nums.add(Integer.valueOf(5));
        nums.add(Integer.valueOf(-6));
        NUMBERS = Collections.unmodifiableSet(nums);

        Set<Integer> times = new HashSet<Integer>();
        times.add(Integer.valueOf(92));
        times.add(Integer.valueOf(93));
        times.add(Integer.valueOf(91));
        TEMPORALS = Collections.unmodifiableSet(times);

        Set<Integer> stringys = new HashSet<Integer>();
        stringys.add(Integer.valueOf(1));
        stringys.add(Integer.valueOf(-16));
        stringys.add(Integer.valueOf(-1));
        stringys.add(Integer.valueOf(-15));
        stringys.add(Integer.valueOf(-9));
        stringys.add(Integer.valueOf(12));
        STRINGYS = Collections.unmodifiableSet(stringys);
    }

    public static boolean isNumeric(int type) {
        return NUMBERS.contains(Integer.valueOf(type));
    }

    public static boolean isTemporal(int type) {
        return TEMPORALS.contains(Integer.valueOf(type));
    }

    public static boolean isStringy(int type) {
        return STRINGYS.contains(Integer.valueOf(type));
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\SqlHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */