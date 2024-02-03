package com.timestored.sqldash.chart;

class KdbFunctions {
    static double[] mul(double[] nums, double amount) {
        double[] res = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = nums[i] * amount;
        }
        return res;
    }

    static double[] add(double[] nums, double amount) {
        double[] res = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = nums[i] + amount;
        }
        return res;
    }

    static double[] add(double[] nums, double[] amount) {
        double[] res = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = nums[i] + amount[i];
        }
        return res;
    }

    static double[] til(int X) {
        double[] r = new double[X];
        for (int i = 0; i < X; i++) {
            r[i] = i;
        }
        return r;
    }

    static double[] cos(double[] nums) {
        double[] res = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = Math.cos(nums[i]);
        }
        return res;
    }

    static double[] sin(double[] nums) {
        double[] res = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = Math.sin(nums[i]);
        }
        return res;
    }

    static double[] mod(double[] nums, double modder) {
        double[] res = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = nums[i] % modder;
        }
        return res;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\KdbFunctions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */