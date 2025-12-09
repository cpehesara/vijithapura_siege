package com.vijithapura.siege.dsa;

import com.vijithapura.siege.entities.Unit;
import java.util.ArrayList;

/**
 * DSA: SORTING ALGORITHMS
 * Implements Quick Sort and Merge Sort for unit management
 */
public class SortingAlgorithms {

    /**
     * Quick Sort - Sort units by health (ascending)
     * Used for: Finding units that need healing priority
     */
    public static void quickSortByHealth(ArrayList<Unit> units, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(units, low, high);
            quickSortByHealth(units, low, pivotIndex - 1);
            quickSortByHealth(units, pivotIndex + 1, high);
        }
    }

    private static int partition(ArrayList<Unit> units, int low, int high) {
        float pivot = units.get(high).getHealth();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (units.get(j).getHealth() < pivot) {
                i++;
                // Swap
                Unit temp = units.get(i);
                units.set(i, units.get(j));
                units.set(j, temp);
            }
        }

        // Swap pivot
        Unit temp = units.get(i + 1);
        units.set(i + 1, units.get(high));
        units.set(high, temp);

        return i + 1;
    }

    /**
     * Merge Sort - Sort units by distance from point
     * Used for: Finding nearest enemies
     */
    public static void mergeSortByDistance(ArrayList<UnitDistance> unitDistances, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            mergeSortByDistance(unitDistances, left, middle);
            mergeSortByDistance(unitDistances, middle + 1, right);

            merge(unitDistances, left, middle, right);
        }
    }

    private static void merge(ArrayList<UnitDistance> arr, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        ArrayList<UnitDistance> leftArr = new ArrayList<>();
        ArrayList<UnitDistance> rightArr = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            leftArr.add(arr.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightArr.add(arr.get(middle + 1 + j));
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArr.get(i).distance <= rightArr.get(j).distance) {
                arr.set(k, leftArr.get(i));
                i++;
            } else {
                arr.set(k, rightArr.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr.set(k, leftArr.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            arr.set(k, rightArr.get(j));
            j++;
            k++;
        }
    }

    /**
     * Helper class for sorting units by distance
     */
    public static class UnitDistance {
        public Unit unit;
        public float distance;

        public UnitDistance(Unit unit, float distance) {
            this.unit = unit;
            this.distance = distance;
        }
    }

    /**
     * Bubble Sort - Simple sorting for small lists
     * Used for: Sorting UI elements
     */
    public static void bubbleSortByHealth(ArrayList<Unit> units) {
        int n = units.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (units.get(j).getHealth() > units.get(j + 1).getHealth()) {
                    // Swap
                    Unit temp = units.get(j);
                    units.set(j, units.get(j + 1));
                    units.set(j + 1, temp);
                }
            }
        }
    }
}
