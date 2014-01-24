package com.buzzinate.buzzads.data.thrift;

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 9:05:46 PM
 * 
 */
public enum TPublisherContactRevMethod {
    ALIPAY(0);

    private final int value;

    private TPublisherContactRevMethod(int value) {
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
    public static TPublisherContactRevMethod findByValue(int value) {
        switch (value) {
        case 0 :
            return ALIPAY;
        default :
            return null;
        }
    }
}