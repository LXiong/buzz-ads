package com.buzzinate.buzzads.data.thrift;

/**
 * 
 * @author jeffrey.ji <jeffrey.ji@buzzinate.com> Mar 5, 2013 9:06:39 PM
 * 
 */
public enum TBidTypeEnum {
    CPM(0), CPA(1), CPS(2), CPC(3), CPT(4), CPD(5);

    private final int value;

    private TBidTypeEnum(int value) {
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
    public static TBidTypeEnum findByValue(int value) {
        switch (value) {
        case 0 :
            return CPM;
        case 1 :
            return CPA;
        case 2 :
            return CPS;
        case 3 :
            return CPC;
        case 4 :
            return CPT;
        case 5 :
            return CPD;
        default :
            return null;
        }
    }
}