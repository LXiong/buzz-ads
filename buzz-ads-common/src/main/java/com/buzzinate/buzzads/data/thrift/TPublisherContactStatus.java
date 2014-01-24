package com.buzzinate.buzzads.data.thrift;

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 9:05:33 PM
 * 
 */
public enum TPublisherContactStatus {
    NORMAL(0), FROZEN(1);

    private final int value;

    private TPublisherContactStatus(int value) {
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
    public static TPublisherContactStatus findByValue(int value) {
        switch (value) {
        case 0 :
            return NORMAL;
        case 1 :
            return FROZEN;
        default :
            return null;
        }
    }
}