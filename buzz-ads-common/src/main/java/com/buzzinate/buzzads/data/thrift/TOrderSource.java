package com.buzzinate.buzzads.data.thrift;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public enum TOrderSource {
  BUZZ(0),
  CHANET(1);

  private final int value;

  private TOrderSource(int value) {
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
  public static TOrderSource findByValue(int value) {
    switch(value) {
      case 0: return BUZZ;
      case 1: return CHANET;
      default: return null;
    }
  }
}
//CHECKSTYLE:ON