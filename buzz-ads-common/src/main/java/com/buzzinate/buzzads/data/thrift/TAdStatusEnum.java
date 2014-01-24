package com.buzzinate.buzzads.data.thrift;

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 9:06:25 PM
 * 
 */
public enum TAdStatusEnum {
    READY(0), ENABLED(1), PAUSED(2), DISABLED(3), SUSPENDED(4), DELETED(5), VERIFYING(6), REJECTED(7);

    private final int value;

    private TAdStatusEnum(int value) {
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
    public static TAdStatusEnum findByValue(int value) {
        switch (value) {
        case 0 :
            return READY;
        case 1 :
            return ENABLED;
        case 2 :
            return PAUSED;
        case 3 :
            return DISABLED;
        case 4 :
            return SUSPENDED;
        case 5 :
            return DELETED;
        case 6 :
            return VERIFYING;
        case 7 :
            return REJECTED;
        default :
            return null;
        }
    }
}