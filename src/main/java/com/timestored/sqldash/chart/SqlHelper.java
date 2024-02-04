package com.timestored.sqldash.chart;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class SqlHelper {
    private static final Set<Integer> NUMBERS;
    private static final Set<Integer> TEMPORALS;
    private static final Set<Integer> STRINGYS;

    static {
        Set<Integer> nums = new HashSet<>();
        nums.add(-5);
        nums.add(3);
        nums.add(8);
        nums.add(6);
        nums.add(4);
        nums.add(2);
        nums.add(7);
        nums.add(5);
        nums.add(-6);
        NUMBERS = Collections.unmodifiableSet(nums);

        Set<Integer> times = new HashSet<>();
        times.add(92);
        times.add(93);
        times.add(91);
        TEMPORALS = Collections.unmodifiableSet(times);

        Set<Integer> stringys = new HashSet<>();
        stringys.add(1);
        stringys.add(-16);
        stringys.add(-1);
        stringys.add(-15);
        stringys.add(-9);
        stringys.add(12);
        STRINGYS = Collections.unmodifiableSet(stringys);
    }

    public static boolean isNumeric(int type) {
        return NUMBERS.contains(type);
    }

    public static boolean isTemporal(int type) {
        return TEMPORALS.contains(type);
    }

    public static boolean isStringy(int type) {
        return STRINGYS.contains(type);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\SqlHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */