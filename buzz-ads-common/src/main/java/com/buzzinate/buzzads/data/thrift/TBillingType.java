package com.buzzinate.buzzads.data.thrift;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public enum TBillingType {
  CPM(0),
  CPA(1),
  CPS(2),
  CPC(3);

  private final int value;

  private TBillingType(int value) {
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
  public static TBillingType findByValue(int value) {
    switch(value) {
      case 0: return CPM;
      case 1: return CPA;
      case 2: return CPS;
      case 3: return CPC;
      default: return null;
    }
  }
}
//CHECKSTYLE:ON