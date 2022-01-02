package com.github.ste29;

import java.util.Arrays;

public class Utils {

    public static <T> T last(T[] arr, int n) {
        if(arr.length == 0) {
            throw new IllegalArgumentException("No item in the arr");
        }
        return arr[arr.length - n];
    }

    public static String getExtension(String filename) {
        return last(filename.split("\\."),1);
    }

    public static String stripExtenstion(String filename) {
        String[] parts = filename.split("\\.");
        return String.join(".", Arrays.copyOf(parts, parts.length - 1));
    }

}