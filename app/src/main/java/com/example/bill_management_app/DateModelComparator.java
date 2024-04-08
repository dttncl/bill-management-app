package com.example.bill_management_app;

import java.util.Comparator;

public class DateModelComparator implements Comparator<DateModel> {
    @Override
    public int compare(DateModel date1, DateModel date2) {
        // Handle null cases
        if (date1 == null && date2 == null) {
            return 0;
        } else if (date1 == null) {
            return 1; // date2 is considered greater
        } else if (date2 == null) {
            return -1; // date1 is considered greater
        }

        // Compare years
        if (date1.getYear() != date2.getYear()) {
            return Integer.compare(date1.getYear(), date2.getYear());
        }

        // Compare months
        if (date1.getMonth() != date2.getMonth()) {
            return Integer.compare(date1.getMonth(), date2.getMonth());
        }

        // Compare days
        return Integer.compare(date1.getDay(), date2.getDay());
    }
}

