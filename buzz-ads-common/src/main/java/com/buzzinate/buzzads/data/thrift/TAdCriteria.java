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
public class TAdCriteria implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TAdCriteria");
  private static final TField CampaignIdField = new TField("campaignId", TType.I32, (short) 1);
  final ScroogeOption<Integer> campaignId;
  private static final TField OrderIdField = new TField("orderId", TType.I32, (short) 2);
  final ScroogeOption<Integer> orderId;
  private static final TField EntryIdField = new TField("entryId", TType.I32, (short) 3);
  final ScroogeOption<Integer> entryId;
  private static final TField AdvertiserIdField = new TField("advertiserId", TType.I32, (short) 4);
  final ScroogeOption<Integer> advertiserId;
  private static final TField StatusField = new TField("status", TType.SET, (short) 5);
  final ScroogeOption<Set<TAdStatusEnum>> status;
  private static final TField NetworkField = new TField("network", TType.SET, (short) 6);
  final ScroogeOption<Set<TAdNetworkEnum>> network;
  private static final TField BidTypeField = new TField("bidType", TType.SET, (short) 7);
  final ScroogeOption<Set<TBidTypeEnum>> bidType;

  public static class Builder {
    private int _campaignId = 0;
    private Boolean _got_campaignId = false;

    public Builder campaignId(int value) {
      this._campaignId = value;
      this._got_campaignId = true;
      return this;
    }
    private int _orderId = 0;
    private Boolean _got_orderId = false;

    public Builder orderId(int value) {
      this._orderId = value;
      this._got_orderId = true;
      return this;
    }
    private int _entryId = 0;
    private Boolean _got_entryId = false;

    public Builder entryId(int value) {
      this._entryId = value;
      this._got_entryId = true;
      return this;
    }
    private int _advertiserId = 0;
    private Boolean _got_advertiserId = false;

    public Builder advertiserId(int value) {
      this._advertiserId = value;
      this._got_advertiserId = true;
      return this;
    }
    private Set<TAdStatusEnum> _status = Utilities.makeSet();
    private Boolean _got_status = false;

    public Builder status(Set<TAdStatusEnum> value) {
      this._status = value;
      this._got_status = true;
      return this;
    }
    private Set<TAdNetworkEnum> _network = Utilities.makeSet();
    private Boolean _got_network = false;

    public Builder network(Set<TAdNetworkEnum> value) {
      this._network = value;
      this._got_network = true;
      return this;
    }
    private Set<TBidTypeEnum> _bidType = Utilities.makeSet();
    private Boolean _got_bidType = false;

    public Builder bidType(Set<TBidTypeEnum> value) {
      this._bidType = value;
      this._got_bidType = true;
      return this;
    }

    public TAdCriteria build() {
      return new TAdCriteria(
      ScroogeOption.make(this._got_campaignId, this._campaignId),
      ScroogeOption.make(this._got_orderId, this._orderId),
      ScroogeOption.make(this._got_entryId, this._entryId),
      ScroogeOption.make(this._got_advertiserId, this._advertiserId),
      ScroogeOption.make(this._got_status, this._status),
      ScroogeOption.make(this._got_network, this._network),
      ScroogeOption.make(this._got_bidType, this._bidType)    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    if (this.campaignId.isDefined()) builder.campaignId(this.campaignId.get());
    if (this.orderId.isDefined()) builder.orderId(this.orderId.get());
    if (this.entryId.isDefined()) builder.entryId(this.entryId.get());
    if (this.advertiserId.isDefined()) builder.advertiserId(this.advertiserId.get());
    if (this.status.isDefined()) builder.status(this.status.get());
    if (this.network.isDefined()) builder.network(this.network.get());
    if (this.bidType.isDefined()) builder.bidType(this.bidType.get());
    return builder;
  }

  public static ThriftStructCodec<TAdCriteria> CODEC = new ThriftStructCodec<TAdCriteria>() {
    public TAdCriteria decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      int campaignId = 0;
      int orderId = 0;
      int entryId = 0;
      int advertiserId = 0;
      Set<TAdStatusEnum> status = Utilities.makeSet();
      Set<TAdNetworkEnum> network = Utilities.makeSet();
      Set<TBidTypeEnum> bidType = Utilities.makeSet();
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
            case 2: /* orderId */
              switch (_field.type) {
                case TType.I32:
                  Integer orderId_item;
                  orderId_item = _iprot.readI32();
                  orderId = orderId_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.orderId(orderId);
              break;
            case 3: /* entryId */
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
            case 4: /* advertiserId */
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
            case 5: /* status */
              switch (_field.type) {
                case TType.SET:
                  Set<TAdStatusEnum> status_item;
                  TSet _set_status_item = _iprot.readSetBegin();
                  status_item = new HashSet<TAdStatusEnum>();
                  int _i_status_item = 0;
                  TAdStatusEnum _status_item_element;
                  while (_i_status_item < _set_status_item.size) {
                    _status_item_element = TAdStatusEnum.findByValue(_iprot.readI32());
                    status_item.add(_status_item_element);
                    _i_status_item += 1;
                  }
                  _iprot.readSetEnd();
                  status = status_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.status(status);
              break;
            case 6: /* network */
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
            case 7: /* bidType */
              switch (_field.type) {
                case TType.SET:
                  Set<TBidTypeEnum> bidType_item;
                  TSet _set_bidType_item = _iprot.readSetBegin();
                  bidType_item = new HashSet<TBidTypeEnum>();
                  int _i_bidType_item = 0;
                  TBidTypeEnum _bidType_item_element;
                  while (_i_bidType_item < _set_bidType_item.size) {
                    _bidType_item_element = TBidTypeEnum.findByValue(_iprot.readI32());
                    bidType_item.add(_bidType_item_element);
                    _i_bidType_item += 1;
                  }
                  _iprot.readSetEnd();
                  bidType = bidType_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.bidType(bidType);
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

    public void encode(TAdCriteria struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TAdCriteria decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TAdCriteria struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TAdCriteria(
  ScroogeOption<Integer> campaignId,
  ScroogeOption<Integer> orderId,
  ScroogeOption<Integer> entryId,
  ScroogeOption<Integer> advertiserId,
  ScroogeOption<Set<TAdStatusEnum>> status,
  ScroogeOption<Set<TAdNetworkEnum>> network,
  ScroogeOption<Set<TBidTypeEnum>> bidType
  ) {
    this.campaignId = campaignId;
    this.orderId = orderId;
    this.entryId = entryId;
    this.advertiserId = advertiserId;
    this.status = status;
    this.network = network;
    this.bidType = bidType;
  }
  public ScroogeOption getCampaignIdOption() {
      return this.campaignId;
  }
  public ScroogeOption getOrderIdOption() {
      return this.orderId;
  }
  public ScroogeOption getEntryIdOption() {
      return this.entryId;
  }
  public ScroogeOption getAdvertiserIdOption() {
      return this.advertiserId;
  }
  public ScroogeOption getStatusOption() {
      return this.status;
  }
  public ScroogeOption getNetworkOption() {
      return this.network;
  }
  public ScroogeOption getBidTypeOption() {
      return this.bidType;
  }
  public int getCampaignId() {
    return this.campaignId.get();
  }
  public int getOrderId() {
    return this.orderId.get();
  }
  public int getEntryId() {
    return this.entryId.get();
  }
  public int getAdvertiserId() {
    return this.advertiserId.get();
  }
  public Set<TAdStatusEnum> getStatus() {
    return this.status.get();
  }
  public Set<TAdNetworkEnum> getNetwork() {
    return this.network.get();
  }
  public Set<TBidTypeEnum> getBidType() {
    return this.bidType.get();
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
    if (campaignId.isDefined()) {  _oprot.writeFieldBegin(CampaignIdField);
      Integer campaignId_item = campaignId.get();
      _oprot.writeI32(campaignId_item);
      _oprot.writeFieldEnd();
    }
    if (orderId.isDefined()) {  _oprot.writeFieldBegin(OrderIdField);
      Integer orderId_item = orderId.get();
      _oprot.writeI32(orderId_item);
      _oprot.writeFieldEnd();
    }
    if (entryId.isDefined()) {  _oprot.writeFieldBegin(EntryIdField);
      Integer entryId_item = entryId.get();
      _oprot.writeI32(entryId_item);
      _oprot.writeFieldEnd();
    }
    if (advertiserId.isDefined()) {  _oprot.writeFieldBegin(AdvertiserIdField);
      Integer advertiserId_item = advertiserId.get();
      _oprot.writeI32(advertiserId_item);
      _oprot.writeFieldEnd();
    }
    if (status.isDefined()) {  _oprot.writeFieldBegin(StatusField);
      Set<TAdStatusEnum> status_item = status.get();
      _oprot.writeSetBegin(new TSet(TType.I32, status_item.size()));
      for (TAdStatusEnum _status_item_element : status_item) {
        _oprot.writeI32(_status_item_element.getValue());
      }
      _oprot.writeSetEnd();
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
    if (bidType.isDefined()) {  _oprot.writeFieldBegin(BidTypeField);
      Set<TBidTypeEnum> bidType_item = bidType.get();
      _oprot.writeSetBegin(new TSet(TType.I32, bidType_item.size()));
      for (TBidTypeEnum _bidType_item_element : bidType_item) {
        _oprot.writeI32(_bidType_item_element.getValue());
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
    if (!(other instanceof TAdCriteria)) return false;
    TAdCriteria that = (TAdCriteria) other;
    return
      this.campaignId.equals(that.campaignId) &&

      this.orderId.equals(that.orderId) &&

      this.entryId.equals(that.entryId) &&

      this.advertiserId.equals(that.advertiserId) &&

this.status.equals(that.status) &&

this.network.equals(that.network) &&

this.bidType.equals(that.bidType)
;
  }

  public String toString() {
    return "TAdCriteria(" + this.campaignId + "," + this.orderId + "," + this.entryId + "," + this.advertiserId + "," + this.status + "," + this.network + "," + this.bidType + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * (this.campaignId.isDefined() ? 0 : new Integer(this.campaignId.get()).hashCode());
    hash = hash * (this.orderId.isDefined() ? 0 : new Integer(this.orderId.get()).hashCode());
    hash = hash * (this.entryId.isDefined() ? 0 : new Integer(this.entryId.get()).hashCode());
    hash = hash * (this.advertiserId.isDefined() ? 0 : new Integer(this.advertiserId.get()).hashCode());
    hash = hash * (this.status.isDefined() ? 0 : this.status.get().hashCode());
    hash = hash * (this.network.isDefined() ? 0 : this.network.get().hashCode());
    hash = hash * (this.bidType.isDefined() ? 0 : this.bidType.get().hashCode());
    return hash;
  }
}