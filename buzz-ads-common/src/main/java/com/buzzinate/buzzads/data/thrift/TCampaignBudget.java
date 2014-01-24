package com.buzzinate.buzzads.data.thrift;

import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;

import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class TCampaignBudget implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TCampaignBudget");
  private static final TField CampaignIdField = new TField("campaignId", TType.I32, (short) 1);
  final int campaignId;
  private static final TField AdvertiserIdField = new TField("advertiserId", TType.I32, (short) 2);
  final int advertiserId;
  private static final TField AdvertiserBalanceField = new TField("advertiserBalance", TType.I64, (short) 3);
  final long advertiserBalance;
  private static final TField BudgetDayField = new TField("budgetDay", TType.I64, (short) 4);
  final long budgetDay;
  private static final TField BudgetTotalField = new TField("budgetTotal", TType.I64, (short) 5);
  final long budgetTotal;
  private static final TField MaxBudgetDayField = new TField("maxBudgetDay", TType.I64, (short) 6);
  final long maxBudgetDay;
  private static final TField MaxBudgetTotalField = new TField("maxBudgetTotal", TType.I64, (short) 7);
  final long maxBudgetTotal;

  public static class Builder {
    private int _campaignId = 0;
    private Boolean _got_campaignId = false;

    public Builder campaignId(int value) {
      this._campaignId = value;
      this._got_campaignId = true;
      return this;
    }
    private int _advertiserId = 0;
    private Boolean _got_advertiserId = false;

    public Builder advertiserId(int value) {
      this._advertiserId = value;
      this._got_advertiserId = true;
      return this;
    }
    private long _advertiserBalance = (long) 0;
    private Boolean _got_advertiserBalance = false;

    public Builder advertiserBalance(long value) {
      this._advertiserBalance = value;
      this._got_advertiserBalance = true;
      return this;
    }
    private long _budgetDay = (long) 0;
    private Boolean _got_budgetDay = false;

    public Builder budgetDay(long value) {
      this._budgetDay = value;
      this._got_budgetDay = true;
      return this;
    }
    private long _budgetTotal = (long) 0;
    private Boolean _got_budgetTotal = false;

    public Builder budgetTotal(long value) {
      this._budgetTotal = value;
      this._got_budgetTotal = true;
      return this;
    }
    private long _maxBudgetDay = (long) 0;
    private Boolean _got_maxBudgetDay = false;

    public Builder maxBudgetDay(long value) {
      this._maxBudgetDay = value;
      this._got_maxBudgetDay = true;
      return this;
    }
    private long _maxBudgetTotal = (long) 0;
    private Boolean _got_maxBudgetTotal = false;

    public Builder maxBudgetTotal(long value) {
      this._maxBudgetTotal = value;
      this._got_maxBudgetTotal = true;
      return this;
    }

    public TCampaignBudget build() {
      return new TCampaignBudget(
        this._campaignId,
        this._advertiserId,
        this._advertiserBalance,
        this._budgetDay,
        this._budgetTotal,
        this._maxBudgetDay,
        this._maxBudgetTotal    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.campaignId(this.campaignId);
    builder.advertiserId(this.advertiserId);
    builder.advertiserBalance(this.advertiserBalance);
    builder.budgetDay(this.budgetDay);
    builder.budgetTotal(this.budgetTotal);
    builder.maxBudgetDay(this.maxBudgetDay);
    builder.maxBudgetTotal(this.maxBudgetTotal);
    return builder;
  }

  public static ThriftStructCodec<TCampaignBudget> CODEC = new ThriftStructCodec<TCampaignBudget>() {
    public TCampaignBudget decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      int campaignId = 0;
      int advertiserId = 0;
      long advertiserBalance = (long) 0;
      long budgetDay = (long) 0;
      long budgetTotal = (long) 0;
      long maxBudgetDay = (long) 0;
      long maxBudgetTotal = (long) 0;
      Boolean _done = false;
      _iprot.readStructBegin();
      while (!_done) {
        TField _field = _iprot.readFieldBegin();
        if (_field.type == TType.STOP) {
          _done = true;
        } else {
          switch (_field.id) {
            case 1: /* campaignId */
              switch (_field.type) {
                case TType.I32:
                  Integer campaignId_item;
                  campaignId_item = _iprot.readI32();
                  campaignId = campaignId_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.campaignId(campaignId);
              break;
            case 2: /* advertiserId */
              switch (_field.type) {
                case TType.I32:
                  Integer advertiserId_item;
                  advertiserId_item = _iprot.readI32();
                  advertiserId = advertiserId_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.advertiserId(advertiserId);
              break;
            case 3: /* advertiserBalance */
              switch (_field.type) {
                case TType.I64:
                  Long advertiserBalance_item;
                  advertiserBalance_item = _iprot.readI64();
                  advertiserBalance = advertiserBalance_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.advertiserBalance(advertiserBalance);
              break;
            case 4: /* budgetDay */
              switch (_field.type) {
                case TType.I64:
                  Long budgetDay_item;
                  budgetDay_item = _iprot.readI64();
                  budgetDay = budgetDay_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.budgetDay(budgetDay);
              break;
            case 5: /* budgetTotal */
              switch (_field.type) {
                case TType.I64:
                  Long budgetTotal_item;
                  budgetTotal_item = _iprot.readI64();
                  budgetTotal = budgetTotal_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.budgetTotal(budgetTotal);
              break;
            case 6: /* maxBudgetDay */
              switch (_field.type) {
                case TType.I64:
                  Long maxBudgetDay_item;
                  maxBudgetDay_item = _iprot.readI64();
                  maxBudgetDay = maxBudgetDay_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.maxBudgetDay(maxBudgetDay);
              break;
            case 7: /* maxBudgetTotal */
              switch (_field.type) {
                case TType.I64:
                  Long maxBudgetTotal_item;
                  maxBudgetTotal_item = _iprot.readI64();
                  maxBudgetTotal = maxBudgetTotal_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.maxBudgetTotal(maxBudgetTotal);
              break;
            default:
              TProtocolUtil.skip(_iprot, _field.type);
          }
          _iprot.readFieldEnd();
        }
      }
      _iprot.readStructEnd();
      try {
        return builder.build();
      } catch (IllegalStateException stateEx) {
        throw new TProtocolException(stateEx.getMessage());
      }
    }

    public void encode(TCampaignBudget struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TCampaignBudget decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TCampaignBudget struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TCampaignBudget(
  int campaignId,
  int advertiserId,
  long advertiserBalance,
  long budgetDay,
  long budgetTotal,
  long maxBudgetDay,
  long maxBudgetTotal
  ) {
    this.campaignId = campaignId;
    this.advertiserId = advertiserId;
    this.advertiserBalance = advertiserBalance;
    this.budgetDay = budgetDay;
    this.budgetTotal = budgetTotal;
    this.maxBudgetDay = maxBudgetDay;
    this.maxBudgetTotal = maxBudgetTotal;
  }

  public int getCampaignId() {
    return this.campaignId;
  }
  public int getAdvertiserId() {
    return this.advertiserId;
  }
  public long getAdvertiserBalance() {
    return this.advertiserBalance;
  }
  public long getBudgetDay() {
    return this.budgetDay;
  }
  public long getBudgetTotal() {
    return this.budgetTotal;
  }
  public long getMaxBudgetDay() {
    return this.maxBudgetDay;
  }
  public long getMaxBudgetTotal() {
    return this.maxBudgetTotal;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(CampaignIdField);
      Integer campaignId_item = campaignId;
      _oprot.writeI32(campaignId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(AdvertiserIdField);
      Integer advertiserId_item = advertiserId;
      _oprot.writeI32(advertiserId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(AdvertiserBalanceField);
      Long advertiserBalance_item = advertiserBalance;
      _oprot.writeI64(advertiserBalance_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(BudgetDayField);
      Long budgetDay_item = budgetDay;
      _oprot.writeI64(budgetDay_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(BudgetTotalField);
      Long budgetTotal_item = budgetTotal;
      _oprot.writeI64(budgetTotal_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(MaxBudgetDayField);
      Long maxBudgetDay_item = maxBudgetDay;
      _oprot.writeI64(maxBudgetDay_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(MaxBudgetTotalField);
      Long maxBudgetTotal_item = maxBudgetTotal;
      _oprot.writeI64(maxBudgetTotal_item);
      _oprot.writeFieldEnd();
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  }

  public boolean equals(Object other) {
    if (!(other instanceof TCampaignBudget)) return false;
    TCampaignBudget that = (TCampaignBudget) other;
    return
      this.campaignId == that.campaignId &&

      this.advertiserId == that.advertiserId &&

      this.advertiserBalance == that.advertiserBalance &&

      this.budgetDay == that.budgetDay &&

      this.budgetTotal == that.budgetTotal &&

      this.maxBudgetDay == that.maxBudgetDay &&

      this.maxBudgetTotal == that.maxBudgetTotal
;
  }

  public String toString() {
    return "TCampaignBudget(" + this.campaignId + "," + this.advertiserId + "," + this.advertiserBalance + "," + this.budgetDay + "," + this.budgetTotal + "," + this.maxBudgetDay + "," + this.maxBudgetTotal + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * new Integer(this.campaignId).hashCode();
    hash = hash * new Integer(this.advertiserId).hashCode();
    hash = hash * new Long(this.advertiserBalance).hashCode();
    hash = hash * new Long(this.budgetDay).hashCode();
    hash = hash * new Long(this.budgetTotal).hashCode();
    hash = hash * new Long(this.maxBudgetDay).hashCode();
    hash = hash * new Long(this.maxBudgetTotal).hashCode();
    return hash;
  }
}