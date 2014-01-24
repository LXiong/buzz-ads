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
public class TAdStats implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TAdStats");
  private static final TField EntryIdField = new TField("entryId", TType.I32, (short) 1);
  final int entryId;
  private static final TField DateDayField = new TField("dateDay", TType.I64, (short) 2);
  final long dateDay;
  private static final TField NetworkField = new TField("network", TType.I32, (short) 3);
  final TAdNetworkEnum network;
  private static final TField ViewsField = new TField("views", TType.I32, (short) 4);
  final int views;
  private static final TField ClicksField = new TField("clicks", TType.I32, (short) 5);
  final int clicks;
  private static final TField CpcClicksField = new TField("cpcClicks", TType.I32, (short) 6);
  final int cpcClicks;

  public static class Builder {
    private int _entryId = 0;
    private Boolean _got_entryId = false;

    public Builder entryId(int value) {
      this._entryId = value;
      this._got_entryId = true;
      return this;
    }
    private long _dateDay = (long) 0;
    private Boolean _got_dateDay = false;

    public Builder dateDay(long value) {
      this._dateDay = value;
      this._got_dateDay = true;
      return this;
    }
    private TAdNetworkEnum _network = null;
    private Boolean _got_network = false;

    public Builder network(TAdNetworkEnum value) {
      this._network = value;
      this._got_network = true;
      return this;
    }
    private int _views = 0;
    private Boolean _got_views = false;

    public Builder views(int value) {
      this._views = value;
      this._got_views = true;
      return this;
    }
    private int _clicks = 0;
    private Boolean _got_clicks = false;

    public Builder clicks(int value) {
      this._clicks = value;
      this._got_clicks = true;
      return this;
    }
    private int _cpcClicks = 0;
    private Boolean _got_cpcClicks = false;

    public Builder cpcClicks(int value) {
      this._cpcClicks = value;
      this._got_cpcClicks = true;
      return this;
    }

    public TAdStats build() {
      return new TAdStats(
        this._entryId,
        this._dateDay,
        this._network,
        this._views,
        this._clicks,
        this._cpcClicks    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.entryId(this.entryId);
    builder.dateDay(this.dateDay);
    builder.network(this.network);
    builder.views(this.views);
    builder.clicks(this.clicks);
    builder.cpcClicks(this.cpcClicks);
    return builder;
  }

  public static ThriftStructCodec<TAdStats> CODEC = new ThriftStructCodec<TAdStats>() {
    public TAdStats decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      int entryId = 0;
      long dateDay = (long) 0;
      TAdNetworkEnum network = null;
      int views = 0;
      int clicks = 0;
      int cpcClicks = 0;
      Boolean _done = false;
      _iprot.readStructBegin();
      while (!_done) {
        TField _field = _iprot.readFieldBegin();
        if (_field.type == TType.STOP) {
          _done = true;
        } else {
          switch (_field.id) {
            case 1: /* entryId */
              switch (_field.type) {
                case TType.I32:
                  Integer entryId_item;
                  entryId_item = _iprot.readI32();
                  entryId = entryId_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.entryId(entryId);
              break;
            case 2: /* dateDay */
              switch (_field.type) {
                case TType.I64:
                  Long dateDay_item;
                  dateDay_item = _iprot.readI64();
                  dateDay = dateDay_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.dateDay(dateDay);
              break;
            case 3: /* network */
              switch (_field.type) {
                case TType.I32:
                  TAdNetworkEnum network_item;
                  network_item = TAdNetworkEnum.findByValue(_iprot.readI32());
                  network = network_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.network(network);
              break;
            case 4: /* views */
              switch (_field.type) {
                case TType.I32:
                  Integer views_item;
                  views_item = _iprot.readI32();
                  views = views_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.views(views);
              break;
            case 5: /* clicks */
              switch (_field.type) {
                case TType.I32:
                  Integer clicks_item;
                  clicks_item = _iprot.readI32();
                  clicks = clicks_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.clicks(clicks);
              break;
            case 6: /* cpcClicks */
              switch (_field.type) {
                case TType.I32:
                  Integer cpcClicks_item;
                  cpcClicks_item = _iprot.readI32();
                  cpcClicks = cpcClicks_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.cpcClicks(cpcClicks);
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

    public void encode(TAdStats struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TAdStats decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TAdStats struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TAdStats(
  int entryId,
  long dateDay,
  TAdNetworkEnum network,
  int views,
  int clicks,
  int cpcClicks
  ) {
    this.entryId = entryId;
    this.dateDay = dateDay;
    this.network = network;
    this.views = views;
    this.clicks = clicks;
    this.cpcClicks = cpcClicks;
  }

  public int getEntryId() {
    return this.entryId;
  }
  public long getDateDay() {
    return this.dateDay;
  }
  public TAdNetworkEnum getNetwork() {
    return this.network;
  }
  public int getViews() {
    return this.views;
  }
  public int getClicks() {
    return this.clicks;
  }
  public int getCpcClicks() {
    return this.cpcClicks;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(EntryIdField);
      Integer entryId_item = entryId;
      _oprot.writeI32(entryId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(DateDayField);
      Long dateDay_item = dateDay;
      _oprot.writeI64(dateDay_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(NetworkField);
      TAdNetworkEnum network_item = network;
      _oprot.writeI32(network_item.getValue());
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(ViewsField);
      Integer views_item = views;
      _oprot.writeI32(views_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(ClicksField);
      Integer clicks_item = clicks;
      _oprot.writeI32(clicks_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(CpcClicksField);
      Integer cpcClicks_item = cpcClicks;
      _oprot.writeI32(cpcClicks_item);
      _oprot.writeFieldEnd();
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  }

  public boolean equals(Object other) {
    if (!(other instanceof TAdStats)) return false;
    TAdStats that = (TAdStats) other;
    return
      this.entryId == that.entryId &&

      this.dateDay == that.dateDay &&

this.network.equals(that.network) &&

      this.views == that.views &&

      this.clicks == that.clicks &&

      this.cpcClicks == that.cpcClicks
;
  }

  public String toString() {
    return "TAdStats(" + this.entryId + "," + this.dateDay + "," + this.network + "," + this.views + "," + this.clicks + "," + this.cpcClicks + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * new Integer(this.entryId).hashCode();
    hash = hash * new Long(this.dateDay).hashCode();
    hash = hash * (this.network == null ? 0 : this.network.hashCode());
    hash = hash * new Integer(this.views).hashCode();
    hash = hash * new Integer(this.clicks).hashCode();
    hash = hash * new Integer(this.cpcClicks).hashCode();
    return hash;
  }
}