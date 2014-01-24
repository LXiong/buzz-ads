package com.buzzinate.buzzads.thrift;

public enum AdEntryTypeEnum {
  TEXT(0),
  IMAGE(1),
  FLASH(2),
  UNKNOWN(99);

  private final int value;

  private AdEntryTypeEnum(int value) {
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
  public static AdEntryTypeEnum findByValue(int value) {
    switch(value) {
      case 0: return TEXT;
      case 1: return IMAGE;
      case 2: return FLASH;
      case 99: return UNKNOWN;
      default: return TEXT;
    }
  }
}