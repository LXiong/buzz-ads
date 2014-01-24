package com.buzzinate.buzzads.data.thrift;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public enum TAdsTypeEnum {
  AVERAGE(0),
  ACCELERATE(1);

  private final int value;

  private TAdsTypeEnum(int value) {
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
  public static TAdsTypeEnum findByValue(int value) {
    switch(value) {
      case 0: return AVERAGE;
      case 1: return ACCELERATE;
      default: return null;
    }
  }
}