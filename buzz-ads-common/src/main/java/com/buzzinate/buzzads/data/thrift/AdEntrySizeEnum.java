package com.buzzinate.buzzads.data.thrift;

public enum AdEntrySizeEnum {
    SIZE80X80(1, 80, 80), SIZE660X90(2, 660, 90), SIZE610X100(3, 610, 100), SIZE300X250(4, 300, 250), SIZE300X100(
            5, 300, 100), SIZE200X200(6, 200, 200), SIZE300X280(7, 300, 280);

    private final int value;

    private final int width;

    private final int height;

    private AdEntrySizeEnum(int value, int width, int height) {
        this.value = value;
        this.width = width;
        this.height = height;
    }

    /**
     * Get the integer value of this enum value, as defined in the Thrift IDL.
     */
    public int getValue() {
        return value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Find a the enum type by its integer value, as defined in the Thrift IDL.
     * 
     * @return null if the value is not found.
     */
    public static AdEntrySizeEnum findByValue(int value) {
        switch (value) {
        case 1:
            return SIZE80X80;
        case 2:
            return SIZE660X90;
        case 3:
            return SIZE610X100;
        case 4:
            return SIZE300X250;
        case 5:
            return SIZE300X100;
        case 6:
            return SIZE200X200;
        case 7:
            return SIZE300X280;
        default:
            return SIZE80X80;
        }
    }
}