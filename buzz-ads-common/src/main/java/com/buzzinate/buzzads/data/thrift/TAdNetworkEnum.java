package com.buzzinate.buzzads.data.thrift;

public enum TAdNetworkEnum {
  LEZHI(0),
  BSHARE(1),
  BUZZADS(2),
  WJF(3),
  MULTIMEDIA(4);

  private final int value;

  private TAdNetworkEnum(int value) {
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
  public static TAdNetworkEnum findByValue(int value) {
    switch(value) {
      case 0: return LEZHI;
      case 1: return BSHARE;
      case 2: return BUZZADS;
      case 3: return WJF;
      case 4: return MULTIMEDIA;
      default: return null;
    }
  }
}