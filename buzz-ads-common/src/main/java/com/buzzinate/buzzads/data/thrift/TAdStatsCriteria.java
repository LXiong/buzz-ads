package com.buzzinate.buzzads.data.thrift;

import java.util.HashSet;
import java.util.Set;

import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TSet;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;

import com.twitter.scrooge.ScroogeOption;
import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;
import com.twitter.scrooge.Utilities;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class TAdStatsCriteria implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TAdStatsCriteria");
  private static final TField EntryIdField = new TField("entryId", TType.I32, (short) 1);
  final ScroogeOption<Integer> entryId;
  private static final TField DateDayField = new TField("dateDay", TType.I64, (short) 2);
  final ScroogeOption<Long> dateDay;
  private static final TField NetworkField = new TField("network", TType.SET, (short) 3);
  final ScroogeOption<Set<TAdNetworkEnum>> network;

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
    private Set<TAdNetworkEnum> _network = Utilities.makeSet();
    private Boolean _got_network = false;

    public Builder network(Set<TAdNetworkEnum> value) {
      this._network = value;
      this._got_network = true;
      return this;
    }

    public TAdStatsCriteria build() {
      return new TAdStatsCriteria(
      ScroogeOption.make(this._got_entryId, this._entryId),
      ScroogeOption.make(this._got_dateDay, this._dateDay),
      ScroogeOption.make(this._got_network, this._network)    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    if (this.entryId.isDefined()) builder.entryId(this.entryId.get());
    if (this.dateDay.isDefined()) builder.dateDay(this.dateDay.get());
    if (this.network.isDefined()) builder.network(this.network.get());
    return builder;
  }

  public static ThriftStructCodec<TAdStatsCriteria> CODEC = new ThriftStructCodec<TAdStatsCriteria>() {
    public TAdStatsCriteria decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      int entryId = 0;
      long dateDay = (long) 0;
      Set<TAdNetworkEnum> network = Utilities.makeSet();
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
                case TType.SET:
                  Set<TAdNetworkEnum> network_item;
                  TSet _set_network_item = _iprot.readSetBegin();
                  network_item = new HashSet<TAdNetworkEnum>();
                  int _i_network_item = 0;
                  TAdNetworkEnum _network_item_element;
                  while (_i_network_item < _set_network_item.size) {
                    _network_item_element = TAdNetworkEnum.findByValue(_iprot.readI32());
                    network_item.add(_network_item_element);
                    _i_network_item += 1;
                  }
                  _iprot.readSetEnd();
                  network = network_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.network(network);
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

    public void encode(TAdStatsCriteria struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TAdStatsCriteria decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TAdStatsCriteria struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TAdStatsCriteria(
  ScroogeOption<Integer> entryId,
  ScroogeOption<Long> dateDay,
  ScroogeOption<Set<TAdNetworkEnum>> network
  ) {
    this.entryId = entryId;
    this.dateDay = dateDay;
    this.network = network;
  }
  
  public ScroogeOption getEntryIdOption() {
      return this.entryId;
  }
  public ScroogeOption getDateDayOption() {
      return this.dateDay;
  }
  public ScroogeOption getNetworkOption() {
      return this.network;
  }
  public int getEntryId() {
    return this.entryId.get();
  }
  public long getDateDay() {
    return this.dateDay.get();
  }
  public Set<TAdNetworkEnum> getNetwork() {
    return this.network.get();
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
    if (entryId.isDefined()) {  _oprot.writeFieldBegin(EntryIdField);
      Integer entryId_item = entryId.get();
      _oprot.writeI32(entryId_item);
      _oprot.writeFieldEnd();
    }
    if (dateDay.isDefined()) {  _oprot.writeFieldBegin(DateDayField);
      Long dateDay_item = dateDay.get();
      _oprot.writeI64(dateDay_item);
      _oprot.writeFieldEnd();
    }
    if (network.isDefined()) {  _oprot.writeFieldBegin(NetworkField);
      Set<TAdNetworkEnum> network_item = network.get();
      _oprot.writeSetBegin(new TSet(TType.I32, network_item.size()));
      for (TAdNetworkEnum _network_item_element : network_item) {
        _oprot.writeI32(_network_item_element.getValue());
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  }

  public boolean equals(Object other) {
    if (!(other instanceof TAdStatsCriteria)) return false;
    TAdStatsCriteria that = (TAdStatsCriteria) other;
    return
      this.entryId.equals(that.entryId) &&

      this.dateDay.equals(that.dateDay) &&

this.network.equals(that.network)
;
  }

  public String toString() {
    return "TAdStatsCriteria(" + this.entryId + "," + this.dateDay + "," + this.network + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * (this.entryId.isDefined() ? 0 : new Integer(this.entryId.get()).hashCode());
    hash = hash * (this.dateDay.isDefined() ? 0 : new Long(this.dateDay.get()).hashCode());
    hash = hash * (this.network.isDefined() ? 0 : this.network.get().hashCode());
    return hash;
  }
}