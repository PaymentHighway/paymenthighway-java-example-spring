package io.paymenthighway.example.utils;

import org.apache.http.NameValuePair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sorting {

    public static Comparator<NameValuePair> getByKeyComparator() {
        return new Comparator<NameValuePair>() {
            public int compare(NameValuePair p1, NameValuePair p2) {
                return p1.getName().compareTo(p2.getName());
            }
        };
    }

    public static List<NameValuePair> sortParametersByKey(List<NameValuePair> nameValuePairs) {
        Collections.sort(nameValuePairs, getByKeyComparator());
        return nameValuePairs;
    }
}
