package com.oole.hw3.utility;

import java.util.Comparator;

public class ListOrderingComparator implements Comparator<String> {
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
