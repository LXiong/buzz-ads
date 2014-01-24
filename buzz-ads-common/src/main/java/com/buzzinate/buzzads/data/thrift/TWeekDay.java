package com.buzzinate.buzzads.data.thrift;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public enum TWeekDay {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

    private final int value;

    private TWeekDay(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Thrift IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Find a the enum type by its integer value, as defined in the Thrift IDL.
     * 
     * @return null if the value is not found.
     */
    public static TWeekDay findByValue(int value) {
        switch (value) {
        case 1 :
            return MONDAY;
        case 2 :
            return TUESDAY;
        case 3 :
            return WEDNESDAY;
        case 4 :
            return THURSDAY;
        case 5 :
            return FRIDAY;
        case 6 :
            return SATURDAY;
        case 7 :
            return SUNDAY;
        default :
            return null;
        }
    }
}