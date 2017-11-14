package com.oole.hw3.utility;

import java.util.Comparator;

/**
 * List Ordering Comparator
 * Comparator class to perform comparisons between class names depending on presence of $ in name
 * Used to sort a data structure like List. All class names containing $ sign are moved to the end
 */
public class ListOrderingComparator implements Comparator<String> {

    /**
     * Sorts strings in descending order based on presence of $ sign
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(String o1, String o2) {
        if(o2.contains("$"))
            return -1;
        else if(o1.contains("$"))
            return 1;
        else
            return 0;
    }
}
