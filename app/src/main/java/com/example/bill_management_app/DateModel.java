package com.example.bill_management_app;

import java.io.Serializable;

public class DateModel implements Serializable {

    private int day;
    private int month;
    private int year;

    public DateModel() {
        this.day = 1;
        this.month = 1;
        this.year = 1000;
    }

    public DateModel(int day, int month, int year) {
        if (isValidDate(day, month, year)) {
            this.day = day;
            this.month = month;
            this.year = year;
        } else {
            // Handle invalid date input, maybe throw an IllegalArgumentException
            throw new IllegalArgumentException("Invalid date");
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (isValidDate(day, this.month, this.year)) {
            this.day = day;
        } else {
            throw new IllegalArgumentException("Invalid day for the current month and year");
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (isValidDate(this.day, month, this.year)) {
            this.month = month;
        } else {
            throw new IllegalArgumentException("Invalid month");
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (isValidDate(this.day, this.month, year)) {
            this.year = year;
        } else {
            throw new IllegalArgumentException("Invalid year");
        }
    }

    static boolean isValidDate(int day, int month, int year) {
        if (year < 1) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        int[] daysInMonth = {31, 28 + (isLeapYear(year) ? 1 : 0), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (day < 1 || day > daysInMonth[month - 1]) {
            return false;
        }
        return true;
    }

    // Check if a year is a leap year
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    @Override
    public String toString() {
        return day +"/"+month +"/"+year;
    }
}
