package com.buzzinate.buzzads.thrift;

public enum AdEntrySizeEnum {
  SIZE80X80(1),
  SIZE660X90(2),
  SIZE610X100(3),
  SIZE300X250(4),
  SIZE300X100(5),
  SIZE200X200(6),
  SIZE300X280(7);

  private final int value;

  private AdEntrySizeEnum(int value) {
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
   * @return null if the value is not found.
   */
  public static AdEntrySizeEnum findByValue(int value) {
    switch(value) {
      case 1: return SIZE80X80;
      case 2: return SIZE660X90;
      case 3: return SIZE610X100;
      case 4: return SIZE300X250;
      case 5: return SIZE300X100;
      case 6: return SIZE200X200;
      case 7: return SIZE300X280;
      default: return null;
    }
  }
}