package com.buzzinate.buzzads.data.thrift;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public enum TAdEntryType {
  TEXT(0),
  IMAGE(1),
  UNKNOWN(99);

  private final int value;

  private TAdEntryType(int value) {
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
  public static TAdEntryType findByValue(int value) {
    switch(value) {
      case 0: return TEXT;
      case 1: return IMAGE;
      case 99: return UNKNOWN;
      default: return null;
    }
  }
}
//CHECKSTYLE:ON