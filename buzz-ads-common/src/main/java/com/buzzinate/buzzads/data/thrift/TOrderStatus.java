package com.buzzinate.buzzads.data.thrift;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public enum TOrderStatus {
  READY(0),
  LIVE(1),
  PAUSE(2),
  COMPLETED(3),
  TERMINATED(4);

  private final int value;

  private TOrderStatus(int value) {
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
  public static TOrderStatus findByValue(int value) {
    switch(value) {
      case 0: return READY;
      case 1: return LIVE;
      case 2: return PAUSE;
      case 3: return COMPLETED;
      case 4: return TERMINATED;
      default: return null;
    }
  }
}
//CHECKSTYLE:ON