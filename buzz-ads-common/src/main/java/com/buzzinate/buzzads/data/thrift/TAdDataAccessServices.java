package com.buzzinate.buzzads.data.thrift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;

import com.twitter.finagle.Service;
import com.twitter.finagle.SourcedException;
import com.twitter.finagle.stats.Counter;
import com.twitter.finagle.stats.NullStatsReceiver;
import com.twitter.finagle.stats.StatsReceiver;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.scrooge.ScroogeOption;
import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;
import com.twitter.scrooge.Utilities;
import com.twitter.util.Function;
import com.twitter.util.Function2;
import com.twitter.util.Future;
import com.twitter.util.FutureEventListener;
//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class TAdDataAccessServices {
  public interface Iface {
    public List<TAdItem> findAdItems(TAdCriteria criteria, TPagination pagination);
    public List<TAdStats> findAdStats(TAdStatsCriteria criteria, TPagination pagination);
    public List<TCampaignBudget> findCampaignBudgets(List<Integer> campaignIds);
    public Void updateAdItem(TAdItem adItem) throws MissingRequiredFieldException;
    public TPublisherContactInfo findPublisherContact(int userId) throws PublisherContactNotFoundException;
    public Void saveOrUpdatePublisherContact(TPublisherContactInfo publisherContact) throws MissingRequiredFieldException;
    public TPublisherSiteConfig getPublisherSiteConfig(String uuid);
    public List<TFrozenChannel> findAllFrozenList();
  }

  public interface FutureIface {
    public Future<List<TAdItem>> findAdItems(TAdCriteria criteria, TPagination pagination);
    public Future<List<TAdStats>> findAdStats(TAdStatsCriteria criteria, TPagination pagination);
    public Future<List<TCampaignBudget>> findCampaignBudgets(List<Integer> campaignIds);
    public Future<Void> updateAdItem(TAdItem adItem);
    public Future<TPublisherContactInfo> findPublisherContact(int userId);
    public Future<Void> saveOrUpdatePublisherContact(TPublisherContactInfo publisherContact);
    public Future<TPublisherSiteConfig> getPublisherSiteConfig(String uuid);
    public Future<List<TFrozenChannel>> findAllFrozenList();
  }

  static class findAdItems_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findAdItems_args");
    private static final TField CriteriaField = new TField("criteria", TType.STRUCT, (short) 1);
    final TAdCriteria criteria;
    private static final TField PaginationField = new TField("pagination", TType.STRUCT, (short) 2);
    final TPagination pagination;
  
    public static class Builder {
      private TAdCriteria _criteria = null;
      private Boolean _got_criteria = false;
  
      public Builder criteria(TAdCriteria value) {
        this._criteria = value;
        this._got_criteria = true;
        return this;
      }
      private TPagination _pagination = null;
      private Boolean _got_pagination = false;
  
      public Builder pagination(TPagination value) {
        this._pagination = value;
        this._got_pagination = true;
        return this;
      }
  
      public findAdItems_args build() {
        return new findAdItems_args(
          this._criteria,
          this._pagination    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.criteria(this.criteria);
      builder.pagination(this.pagination);
      return builder;
    }
  
    public static ThriftStructCodec<findAdItems_args> CODEC = new ThriftStructCodec<findAdItems_args>() {
      public findAdItems_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        TAdCriteria criteria = null;
        TPagination pagination = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* criteria */
                switch (_field.type) {
                  case TType.STRUCT:
                    TAdCriteria criteria_item;
                    criteria_item = TAdCriteria.decode(_iprot);
                    criteria = criteria_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.criteria(criteria);
                break;
              case 2: /* pagination */
                switch (_field.type) {
                  case TType.STRUCT:
                    TPagination pagination_item;
                    pagination_item = TPagination.decode(_iprot);
                    pagination = pagination_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.pagination(pagination);
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
  
      public void encode(findAdItems_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findAdItems_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findAdItems_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findAdItems_args(
    TAdCriteria criteria,
    TPagination pagination
    ) {
      this.criteria = criteria;
      this.pagination = pagination;
    }
  
    public TAdCriteria getCriteria() {
      return this.criteria;
    }
    public TPagination getPagination() {
      return this.pagination;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(CriteriaField);
        TAdCriteria criteria_item = criteria;
        criteria_item.write(_oprot);
        _oprot.writeFieldEnd();
        _oprot.writeFieldBegin(PaginationField);
        TPagination pagination_item = pagination;
        pagination_item.write(_oprot);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findAdItems_args)) return false;
      findAdItems_args that = (findAdItems_args) other;
      return
  this.criteria.equals(that.criteria) &&
  this.pagination.equals(that.pagination);
    }
  
    public String toString() {
      return "findAdItems_args(" + this.criteria + "," + this.pagination + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.criteria == null ? 0 : this.criteria.hashCode());
      hash = hash * (this.pagination == null ? 0 : this.pagination.hashCode());
      return hash;
    }
  }
  static class findAdItems_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findAdItems_result");
    private static final TField SuccessField = new TField("success", TType.LIST, (short) 0);
    final ScroogeOption<List<TAdItem>> success;
  
    public static class Builder {
      private List<TAdItem> _success = Utilities.makeList();
      private Boolean _got_success = false;
  
      public Builder success(List<TAdItem> value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
  
      public findAdItems_result build() {
        return new findAdItems_result(
        ScroogeOption.make(this._got_success, this._success)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      return builder;
    }
  
    public static ThriftStructCodec<findAdItems_result> CODEC = new ThriftStructCodec<findAdItems_result>() {
      public findAdItems_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        List<TAdItem> success = Utilities.makeList();
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 0: /* success */
                switch (_field.type) {
                  case TType.LIST:
                    List<TAdItem> success_item;
                    TList _list_success_item = _iprot.readListBegin();
                    success_item = new ArrayList<TAdItem>();
                    int _i_success_item = 0;
                    TAdItem _success_item_element;
                    while (_i_success_item < _list_success_item.size) {
                      _success_item_element = TAdItem.decode(_iprot);
                      success_item.add(_success_item_element);
                      _i_success_item += 1;
                    }
                    _iprot.readListEnd();
                    success = success_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.success(success);
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
  
      public void encode(findAdItems_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findAdItems_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findAdItems_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findAdItems_result(
    ScroogeOption<List<TAdItem>> success
    ) {
      this.success = success;
    }
  
    public List<TAdItem> getSuccess() {
      return this.success.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        List<TAdItem> success_item = success.get();
        _oprot.writeListBegin(new TList(TType.STRUCT, success_item.size()));
        for (TAdItem _success_item_element : success_item) {
          _success_item_element.write(_oprot);
        }
        _oprot.writeListEnd();
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findAdItems_result)) return false;
      findAdItems_result that = (findAdItems_result) other;
      return
  this.success.equals(that.success);
    }
  
    public String toString() {
      return "findAdItems_result(" + this.success + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.success.isDefined() ? 0 : this.success.get().hashCode());
      return hash;
    }
  }
  static class findAdStats_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findAdStats_args");
    private static final TField CriteriaField = new TField("criteria", TType.STRUCT, (short) 1);
    final TAdStatsCriteria criteria;
    private static final TField PaginationField = new TField("pagination", TType.STRUCT, (short) 2);
    final TPagination pagination;
  
    public static class Builder {
      private TAdStatsCriteria _criteria = null;
      private Boolean _got_criteria = false;
  
      public Builder criteria(TAdStatsCriteria value) {
        this._criteria = value;
        this._got_criteria = true;
        return this;
      }
      private TPagination _pagination = null;
      private Boolean _got_pagination = false;
  
      public Builder pagination(TPagination value) {
        this._pagination = value;
        this._got_pagination = true;
        return this;
      }
  
      public findAdStats_args build() {
        return new findAdStats_args(
          this._criteria,
          this._pagination    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.criteria(this.criteria);
      builder.pagination(this.pagination);
      return builder;
    }
  
    public static ThriftStructCodec<findAdStats_args> CODEC = new ThriftStructCodec<findAdStats_args>() {
      public findAdStats_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        TAdStatsCriteria criteria = null;
        TPagination pagination = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* criteria */
                switch (_field.type) {
                  case TType.STRUCT:
                    TAdStatsCriteria criteria_item;
                    criteria_item = TAdStatsCriteria.decode(_iprot);
                    criteria = criteria_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.criteria(criteria);
                break;
              case 2: /* pagination */
                switch (_field.type) {
                  case TType.STRUCT:
                    TPagination pagination_item;
                    pagination_item = TPagination.decode(_iprot);
                    pagination = pagination_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.pagination(pagination);
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
  
      public void encode(findAdStats_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findAdStats_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findAdStats_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findAdStats_args(
    TAdStatsCriteria criteria,
    TPagination pagination
    ) {
      this.criteria = criteria;
      this.pagination = pagination;
    }
  
    public TAdStatsCriteria getCriteria() {
      return this.criteria;
    }
    public TPagination getPagination() {
      return this.pagination;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(CriteriaField);
        TAdStatsCriteria criteria_item = criteria;
        criteria_item.write(_oprot);
        _oprot.writeFieldEnd();
        _oprot.writeFieldBegin(PaginationField);
        TPagination pagination_item = pagination;
        pagination_item.write(_oprot);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findAdStats_args)) return false;
      findAdStats_args that = (findAdStats_args) other;
      return
  this.criteria.equals(that.criteria) &&
  this.pagination.equals(that.pagination);
    }
  
    public String toString() {
      return "findAdStats_args(" + this.criteria + "," + this.pagination + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.criteria == null ? 0 : this.criteria.hashCode());
      hash = hash * (this.pagination == null ? 0 : this.pagination.hashCode());
      return hash;
    }
  }
  static class findAdStats_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findAdStats_result");
    private static final TField SuccessField = new TField("success", TType.LIST, (short) 0);
    final ScroogeOption<List<TAdStats>> success;
  
    public static class Builder {
      private List<TAdStats> _success = Utilities.makeList();
      private Boolean _got_success = false;
  
      public Builder success(List<TAdStats> value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
  
      public findAdStats_result build() {
        return new findAdStats_result(
        ScroogeOption.make(this._got_success, this._success)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      return builder;
    }
  
    public static ThriftStructCodec<findAdStats_result> CODEC = new ThriftStructCodec<findAdStats_result>() {
      public findAdStats_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        List<TAdStats> success = Utilities.makeList();
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 0: /* success */
                switch (_field.type) {
                  case TType.LIST:
                    List<TAdStats> success_item;
                    TList _list_success_item = _iprot.readListBegin();
                    success_item = new ArrayList<TAdStats>();
                    int _i_success_item = 0;
                    TAdStats _success_item_element;
                    while (_i_success_item < _list_success_item.size) {
                      _success_item_element = TAdStats.decode(_iprot);
                      success_item.add(_success_item_element);
                      _i_success_item += 1;
                    }
                    _iprot.readListEnd();
                    success = success_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.success(success);
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
  
      public void encode(findAdStats_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findAdStats_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findAdStats_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findAdStats_result(
    ScroogeOption<List<TAdStats>> success
    ) {
      this.success = success;
    }
  
    public List<TAdStats> getSuccess() {
      return this.success.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        List<TAdStats> success_item = success.get();
        _oprot.writeListBegin(new TList(TType.STRUCT, success_item.size()));
        for (TAdStats _success_item_element : success_item) {
          _success_item_element.write(_oprot);
        }
        _oprot.writeListEnd();
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findAdStats_result)) return false;
      findAdStats_result that = (findAdStats_result) other;
      return
  this.success.equals(that.success);
    }
  
    public String toString() {
      return "findAdStats_result(" + this.success + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.success.isDefined() ? 0 : this.success.get().hashCode());
      return hash;
    }
  }
  static class findCampaignBudgets_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findCampaignBudgets_args");
    private static final TField CampaignIdsField = new TField("campaignIds", TType.LIST, (short) 1);
    final List<Integer> campaignIds;
  
    public static class Builder {
      private List<Integer> _campaignIds = Utilities.makeList();
      private Boolean _got_campaignIds = false;
  
      public Builder campaignIds(List<Integer> value) {
        this._campaignIds = value;
        this._got_campaignIds = true;
        return this;
      }
  
      public findCampaignBudgets_args build() {
        return new findCampaignBudgets_args(
          this._campaignIds    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.campaignIds(this.campaignIds);
      return builder;
    }
  
    public static ThriftStructCodec<findCampaignBudgets_args> CODEC = new ThriftStructCodec<findCampaignBudgets_args>() {
      public findCampaignBudgets_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        List<Integer> campaignIds = Utilities.makeList();
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* campaignIds */
                switch (_field.type) {
                  case TType.LIST:
                    List<Integer> campaignIds_item;
                    TList _list_campaignIds_item = _iprot.readListBegin();
                    campaignIds_item = new ArrayList<Integer>();
                    int _i_campaignIds_item = 0;
                    Integer _campaignIds_item_element;
                    while (_i_campaignIds_item < _list_campaignIds_item.size) {
                      _campaignIds_item_element = _iprot.readI32();
                      campaignIds_item.add(_campaignIds_item_element);
                      _i_campaignIds_item += 1;
                    }
                    _iprot.readListEnd();
                    campaignIds = campaignIds_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.campaignIds(campaignIds);
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
  
      public void encode(findCampaignBudgets_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findCampaignBudgets_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findCampaignBudgets_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findCampaignBudgets_args(
    List<Integer> campaignIds
    ) {
      this.campaignIds = campaignIds;
    }
  
    public List<Integer> getCampaignIds() {
      return this.campaignIds;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(CampaignIdsField);
        List<Integer> campaignIds_item = campaignIds;
        _oprot.writeListBegin(new TList(TType.I32, campaignIds_item.size()));
        for (Integer _campaignIds_item_element : campaignIds_item) {
          _oprot.writeI32(_campaignIds_item_element);
        }
        _oprot.writeListEnd();
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findCampaignBudgets_args)) return false;
      findCampaignBudgets_args that = (findCampaignBudgets_args) other;
      return
  this.campaignIds.equals(that.campaignIds);
    }
  
    public String toString() {
      return "findCampaignBudgets_args(" + this.campaignIds + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.campaignIds == null ? 0 : this.campaignIds.hashCode());
      return hash;
    }
  }
  static class findCampaignBudgets_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findCampaignBudgets_result");
    private static final TField SuccessField = new TField("success", TType.LIST, (short) 0);
    final ScroogeOption<List<TCampaignBudget>> success;
  
    public static class Builder {
      private List<TCampaignBudget> _success = Utilities.makeList();
      private Boolean _got_success = false;
  
      public Builder success(List<TCampaignBudget> value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
  
      public findCampaignBudgets_result build() {
        return new findCampaignBudgets_result(
        ScroogeOption.make(this._got_success, this._success)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      return builder;
    }
  
    public static ThriftStructCodec<findCampaignBudgets_result> CODEC = new ThriftStructCodec<findCampaignBudgets_result>() {
      public findCampaignBudgets_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        List<TCampaignBudget> success = Utilities.makeList();
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 0: /* success */
                switch (_field.type) {
                  case TType.LIST:
                    List<TCampaignBudget> success_item;
                    TList _list_success_item = _iprot.readListBegin();
                    success_item = new ArrayList<TCampaignBudget>();
                    int _i_success_item = 0;
                    TCampaignBudget _success_item_element;
                    while (_i_success_item < _list_success_item.size) {
                      _success_item_element = TCampaignBudget.decode(_iprot);
                      success_item.add(_success_item_element);
                      _i_success_item += 1;
                    }
                    _iprot.readListEnd();
                    success = success_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.success(success);
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
  
      public void encode(findCampaignBudgets_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findCampaignBudgets_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findCampaignBudgets_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findCampaignBudgets_result(
    ScroogeOption<List<TCampaignBudget>> success
    ) {
      this.success = success;
    }
  
    public List<TCampaignBudget> getSuccess() {
      return this.success.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        List<TCampaignBudget> success_item = success.get();
        _oprot.writeListBegin(new TList(TType.STRUCT, success_item.size()));
        for (TCampaignBudget _success_item_element : success_item) {
          _success_item_element.write(_oprot);
        }
        _oprot.writeListEnd();
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findCampaignBudgets_result)) return false;
      findCampaignBudgets_result that = (findCampaignBudgets_result) other;
      return
  this.success.equals(that.success);
    }
  
    public String toString() {
      return "findCampaignBudgets_result(" + this.success + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.success.isDefined() ? 0 : this.success.get().hashCode());
      return hash;
    }
  }
  static class updateAdItem_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("updateAdItem_args");
    private static final TField AdItemField = new TField("adItem", TType.STRUCT, (short) 1);
    final TAdItem adItem;
  
    public static class Builder {
      private TAdItem _adItem = null;
      private Boolean _got_adItem = false;
  
      public Builder adItem(TAdItem value) {
        this._adItem = value;
        this._got_adItem = true;
        return this;
      }
  
      public updateAdItem_args build() {
        return new updateAdItem_args(
          this._adItem    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.adItem(this.adItem);
      return builder;
    }
  
    public static ThriftStructCodec<updateAdItem_args> CODEC = new ThriftStructCodec<updateAdItem_args>() {
      public updateAdItem_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        TAdItem adItem = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* adItem */
                switch (_field.type) {
                  case TType.STRUCT:
                    TAdItem adItem_item;
                    adItem_item = TAdItem.decode(_iprot);
                    adItem = adItem_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.adItem(adItem);
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
  
      public void encode(updateAdItem_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static updateAdItem_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(updateAdItem_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public updateAdItem_args(
    TAdItem adItem
    ) {
      this.adItem = adItem;
    }
  
    public TAdItem getAdItem() {
      return this.adItem;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(AdItemField);
        TAdItem adItem_item = adItem;
        adItem_item.write(_oprot);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof updateAdItem_args)) return false;
      updateAdItem_args that = (updateAdItem_args) other;
      return
  this.adItem.equals(that.adItem);
    }
  
    public String toString() {
      return "updateAdItem_args(" + this.adItem + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.adItem == null ? 0 : this.adItem.hashCode());
      return hash;
    }
  }
  static class updateAdItem_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("updateAdItem_result");
    private static final TField MrfeField = new TField("mrfe", TType.STRUCT, (short) 1);
    final ScroogeOption<MissingRequiredFieldException> mrfe;
  
    public static class Builder {
      private MissingRequiredFieldException _mrfe = null;
      private Boolean _got_mrfe = false;
  
      public Builder mrfe(MissingRequiredFieldException value) {
        this._mrfe = value;
        this._got_mrfe = true;
        return this;
      }
  
      public updateAdItem_result build() {
        return new updateAdItem_result(
        ScroogeOption.make(this._got_mrfe, this._mrfe)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.mrfe.isDefined()) builder.mrfe(this.mrfe.get());
      return builder;
    }
  
    public static ThriftStructCodec<updateAdItem_result> CODEC = new ThriftStructCodec<updateAdItem_result>() {
      public updateAdItem_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        MissingRequiredFieldException mrfe = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* mrfe */
                switch (_field.type) {
                  case TType.STRUCT:
                    MissingRequiredFieldException mrfe_item;
                    mrfe_item = MissingRequiredFieldException.decode(_iprot);
                    mrfe = mrfe_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.mrfe(mrfe);
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
  
      public void encode(updateAdItem_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static updateAdItem_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(updateAdItem_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public updateAdItem_result(
    ScroogeOption<MissingRequiredFieldException> mrfe
    ) {
      this.mrfe = mrfe;
    }
  
    public MissingRequiredFieldException getMrfe() {
      return this.mrfe.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (mrfe.isDefined()) {  _oprot.writeFieldBegin(MrfeField);
        MissingRequiredFieldException mrfe_item = mrfe.get();
        mrfe_item.write(_oprot);
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof updateAdItem_result)) return false;
      updateAdItem_result that = (updateAdItem_result) other;
      return
  this.mrfe.equals(that.mrfe);
    }
  
    public String toString() {
      return "updateAdItem_result(" + this.mrfe + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.mrfe.isDefined() ? 0 : this.mrfe.get().hashCode());
      return hash;
    }
  }
  static class findPublisherContact_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findPublisherContact_args");
    private static final TField UserIdField = new TField("userId", TType.I32, (short) 1);
    final int userId;
  
    public static class Builder {
      private int _userId = 0;
      private Boolean _got_userId = false;
  
      public Builder userId(int value) {
        this._userId = value;
        this._got_userId = true;
        return this;
      }
  
      public findPublisherContact_args build() {
        return new findPublisherContact_args(
          this._userId    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.userId(this.userId);
      return builder;
    }
  
    public static ThriftStructCodec<findPublisherContact_args> CODEC = new ThriftStructCodec<findPublisherContact_args>() {
      public findPublisherContact_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        int userId = 0;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* userId */
                switch (_field.type) {
                  case TType.I32:
                    Integer userId_item;
                    userId_item = _iprot.readI32();
                    userId = userId_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.userId(userId);
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
  
      public void encode(findPublisherContact_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findPublisherContact_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findPublisherContact_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findPublisherContact_args(
    int userId
    ) {
      this.userId = userId;
    }
  
    public int getUserId() {
      return this.userId;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(UserIdField);
        Integer userId_item = userId;
        _oprot.writeI32(userId_item);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findPublisherContact_args)) return false;
      findPublisherContact_args that = (findPublisherContact_args) other;
      return
        this.userId == that.userId
  ;
    }
  
    public String toString() {
      return "findPublisherContact_args(" + this.userId + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * new Integer(this.userId).hashCode();
      return hash;
    }
  }
  static class findPublisherContact_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findPublisherContact_result");
    private static final TField SuccessField = new TField("success", TType.STRUCT, (short) 0);
    final ScroogeOption<TPublisherContactInfo> success;
    private static final TField PcnfeField = new TField("pcnfe", TType.STRUCT, (short) 1);
    final ScroogeOption<PublisherContactNotFoundException> pcnfe;
  
    public static class Builder {
      private TPublisherContactInfo _success = null;
      private Boolean _got_success = false;
  
      public Builder success(TPublisherContactInfo value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
      private PublisherContactNotFoundException _pcnfe = null;
      private Boolean _got_pcnfe = false;
  
      public Builder pcnfe(PublisherContactNotFoundException value) {
        this._pcnfe = value;
        this._got_pcnfe = true;
        return this;
      }
  
      public findPublisherContact_result build() {
        return new findPublisherContact_result(
        ScroogeOption.make(this._got_success, this._success),
        ScroogeOption.make(this._got_pcnfe, this._pcnfe)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      if (this.pcnfe.isDefined()) builder.pcnfe(this.pcnfe.get());
      return builder;
    }
  
    public static ThriftStructCodec<findPublisherContact_result> CODEC = new ThriftStructCodec<findPublisherContact_result>() {
      public findPublisherContact_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        TPublisherContactInfo success = null;
        PublisherContactNotFoundException pcnfe = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 0: /* success */
                switch (_field.type) {
                  case TType.STRUCT:
                    TPublisherContactInfo success_item;
                    success_item = TPublisherContactInfo.decode(_iprot);
                    success = success_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.success(success);
                break;
              case 1: /* pcnfe */
                switch (_field.type) {
                  case TType.STRUCT:
                    PublisherContactNotFoundException pcnfe_item;
                    pcnfe_item = PublisherContactNotFoundException.decode(_iprot);
                    pcnfe = pcnfe_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.pcnfe(pcnfe);
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
  
      public void encode(findPublisherContact_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findPublisherContact_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findPublisherContact_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findPublisherContact_result(
    ScroogeOption<TPublisherContactInfo> success,
    ScroogeOption<PublisherContactNotFoundException> pcnfe
    ) {
      this.success = success;
      this.pcnfe = pcnfe;
    }
  
    public TPublisherContactInfo getSuccess() {
      return this.success.get();
    }
    public PublisherContactNotFoundException getPcnfe() {
      return this.pcnfe.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        TPublisherContactInfo success_item = success.get();
        success_item.write(_oprot);
        _oprot.writeFieldEnd();
      }
      if (pcnfe.isDefined()) {  _oprot.writeFieldBegin(PcnfeField);
        PublisherContactNotFoundException pcnfe_item = pcnfe.get();
        pcnfe_item.write(_oprot);
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findPublisherContact_result)) return false;
      findPublisherContact_result that = (findPublisherContact_result) other;
      return
  this.success.equals(that.success) &&
  this.pcnfe.equals(that.pcnfe);
    }
  
    public String toString() {
      return "findPublisherContact_result(" + this.success + "," + this.pcnfe + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.success.isDefined() ? 0 : this.success.get().hashCode());
      hash = hash * (this.pcnfe.isDefined() ? 0 : this.pcnfe.get().hashCode());
      return hash;
    }
  }
  static class saveOrUpdatePublisherContact_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("saveOrUpdatePublisherContact_args");
    private static final TField PublisherContactField = new TField("publisherContact", TType.STRUCT, (short) 1);
    final TPublisherContactInfo publisherContact;
  
    public static class Builder {
      private TPublisherContactInfo _publisherContact = null;
      private Boolean _got_publisherContact = false;
  
      public Builder publisherContact(TPublisherContactInfo value) {
        this._publisherContact = value;
        this._got_publisherContact = true;
        return this;
      }
  
      public saveOrUpdatePublisherContact_args build() {
        return new saveOrUpdatePublisherContact_args(
          this._publisherContact    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.publisherContact(this.publisherContact);
      return builder;
    }
  
    public static ThriftStructCodec<saveOrUpdatePublisherContact_args> CODEC = new ThriftStructCodec<saveOrUpdatePublisherContact_args>() {
      public saveOrUpdatePublisherContact_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        TPublisherContactInfo publisherContact = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* publisherContact */
                switch (_field.type) {
                  case TType.STRUCT:
                    TPublisherContactInfo publisherContact_item;
                    publisherContact_item = TPublisherContactInfo.decode(_iprot);
                    publisherContact = publisherContact_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.publisherContact(publisherContact);
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
  
      public void encode(saveOrUpdatePublisherContact_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static saveOrUpdatePublisherContact_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(saveOrUpdatePublisherContact_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public saveOrUpdatePublisherContact_args(
    TPublisherContactInfo publisherContact
    ) {
      this.publisherContact = publisherContact;
    }
  
    public TPublisherContactInfo getPublisherContact() {
      return this.publisherContact;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(PublisherContactField);
        TPublisherContactInfo publisherContact_item = publisherContact;
        publisherContact_item.write(_oprot);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof saveOrUpdatePublisherContact_args)) return false;
      saveOrUpdatePublisherContact_args that = (saveOrUpdatePublisherContact_args) other;
      return
  this.publisherContact.equals(that.publisherContact);
    }
  
    public String toString() {
      return "saveOrUpdatePublisherContact_args(" + this.publisherContact + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.publisherContact == null ? 0 : this.publisherContact.hashCode());
      return hash;
    }
  }
  static class saveOrUpdatePublisherContact_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("saveOrUpdatePublisherContact_result");
    private static final TField MrfeField = new TField("mrfe", TType.STRUCT, (short) 1);
    final ScroogeOption<MissingRequiredFieldException> mrfe;
  
    public static class Builder {
      private MissingRequiredFieldException _mrfe = null;
      private Boolean _got_mrfe = false;
  
      public Builder mrfe(MissingRequiredFieldException value) {
        this._mrfe = value;
        this._got_mrfe = true;
        return this;
      }
  
      public saveOrUpdatePublisherContact_result build() {
        return new saveOrUpdatePublisherContact_result(
        ScroogeOption.make(this._got_mrfe, this._mrfe)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.mrfe.isDefined()) builder.mrfe(this.mrfe.get());
      return builder;
    }
  
    public static ThriftStructCodec<saveOrUpdatePublisherContact_result> CODEC = new ThriftStructCodec<saveOrUpdatePublisherContact_result>() {
      public saveOrUpdatePublisherContact_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        MissingRequiredFieldException mrfe = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* mrfe */
                switch (_field.type) {
                  case TType.STRUCT:
                    MissingRequiredFieldException mrfe_item;
                    mrfe_item = MissingRequiredFieldException.decode(_iprot);
                    mrfe = mrfe_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.mrfe(mrfe);
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
  
      public void encode(saveOrUpdatePublisherContact_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static saveOrUpdatePublisherContact_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(saveOrUpdatePublisherContact_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public saveOrUpdatePublisherContact_result(
    ScroogeOption<MissingRequiredFieldException> mrfe
    ) {
      this.mrfe = mrfe;
    }
  
    public MissingRequiredFieldException getMrfe() {
      return this.mrfe.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (mrfe.isDefined()) {  _oprot.writeFieldBegin(MrfeField);
        MissingRequiredFieldException mrfe_item = mrfe.get();
        mrfe_item.write(_oprot);
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof saveOrUpdatePublisherContact_result)) return false;
      saveOrUpdatePublisherContact_result that = (saveOrUpdatePublisherContact_result) other;
      return
  this.mrfe.equals(that.mrfe);
    }
  
    public String toString() {
      return "saveOrUpdatePublisherContact_result(" + this.mrfe + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.mrfe.isDefined() ? 0 : this.mrfe.get().hashCode());
      return hash;
    }
  }
  static class getPublisherSiteConfig_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("getPublisherSiteConfig_args");
    private static final TField UuidField = new TField("uuid", TType.STRING, (short) 1);
    final String uuid;
  
    public static class Builder {
      private String _uuid = null;
      private Boolean _got_uuid = false;
  
      public Builder uuid(String value) {
        this._uuid = value;
        this._got_uuid = true;
        return this;
      }
  
      public getPublisherSiteConfig_args build() {
        return new getPublisherSiteConfig_args(
          this._uuid    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.uuid(this.uuid);
      return builder;
    }
  
    public static ThriftStructCodec<getPublisherSiteConfig_args> CODEC = new ThriftStructCodec<getPublisherSiteConfig_args>() {
      public getPublisherSiteConfig_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        String uuid = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* uuid */
                switch (_field.type) {
                  case TType.STRING:
                    String uuid_item;
                    uuid_item = _iprot.readString();
                    uuid = uuid_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.uuid(uuid);
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
  
      public void encode(getPublisherSiteConfig_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static getPublisherSiteConfig_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(getPublisherSiteConfig_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public getPublisherSiteConfig_args(
    String uuid
    ) {
      this.uuid = uuid;
    }
  
    public String getUuid() {
      return this.uuid;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(UuidField);
        String uuid_item = uuid;
        _oprot.writeString(uuid_item);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof getPublisherSiteConfig_args)) return false;
      getPublisherSiteConfig_args that = (getPublisherSiteConfig_args) other;
      return
  this.uuid.equals(that.uuid);
    }
  
    public String toString() {
      return "getPublisherSiteConfig_args(" + this.uuid + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.uuid == null ? 0 : this.uuid.hashCode());
      return hash;
    }
  }
  static class getPublisherSiteConfig_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("getPublisherSiteConfig_result");
    private static final TField SuccessField = new TField("success", TType.STRUCT, (short) 0);
    final ScroogeOption<TPublisherSiteConfig> success;
  
    public static class Builder {
      private TPublisherSiteConfig _success = null;
      private Boolean _got_success = false;
  
      public Builder success(TPublisherSiteConfig value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
  
      public getPublisherSiteConfig_result build() {
        return new getPublisherSiteConfig_result(
        ScroogeOption.make(this._got_success, this._success)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      return builder;
    }
  
    public static ThriftStructCodec<getPublisherSiteConfig_result> CODEC = new ThriftStructCodec<getPublisherSiteConfig_result>() {
      public getPublisherSiteConfig_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        TPublisherSiteConfig success = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 0: /* success */
                switch (_field.type) {
                  case TType.STRUCT:
                    TPublisherSiteConfig success_item;
                    success_item = TPublisherSiteConfig.decode(_iprot);
                    success = success_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.success(success);
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
  
      public void encode(getPublisherSiteConfig_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static getPublisherSiteConfig_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(getPublisherSiteConfig_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public getPublisherSiteConfig_result(
    ScroogeOption<TPublisherSiteConfig> success
    ) {
      this.success = success;
    }
  
    public TPublisherSiteConfig getSuccess() {
      return this.success.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        TPublisherSiteConfig success_item = success.get();
        success_item.write(_oprot);
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof getPublisherSiteConfig_result)) return false;
      getPublisherSiteConfig_result that = (getPublisherSiteConfig_result) other;
      return
  this.success.equals(that.success);
    }
  
    public String toString() {
      return "getPublisherSiteConfig_result(" + this.success + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.success.isDefined() ? 0 : this.success.get().hashCode());
      return hash;
    }
  }
  static class findAllFrozenList_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findAllFrozenList_args");
  
    public static class Builder {
  
      public findAllFrozenList_args build() {
        return new findAllFrozenList_args(
      );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      return builder;
    }
  
    public static ThriftStructCodec<findAllFrozenList_args> CODEC = new ThriftStructCodec<findAllFrozenList_args>() {
      public findAllFrozenList_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
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
  
      public void encode(findAllFrozenList_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findAllFrozenList_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findAllFrozenList_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findAllFrozenList_args(
    ) {
    }
  
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      return this == other;
    }
  
    public String toString() {
      return "findAllFrozenList_args()";
    }
  
    public int hashCode() {
      return super.hashCode();
    }
  }
  static class findAllFrozenList_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("findAllFrozenList_result");
    private static final TField SuccessField = new TField("success", TType.LIST, (short) 0);
    final ScroogeOption<List<TFrozenChannel>> success;
  
    public static class Builder {
      private List<TFrozenChannel> _success = Utilities.makeList();
      private Boolean _got_success = false;
  
      public Builder success(List<TFrozenChannel> value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
  
      public findAllFrozenList_result build() {
        return new findAllFrozenList_result(
        ScroogeOption.make(this._got_success, this._success)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      return builder;
    }
  
    public static ThriftStructCodec<findAllFrozenList_result> CODEC = new ThriftStructCodec<findAllFrozenList_result>() {
      public findAllFrozenList_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        List<TFrozenChannel> success = Utilities.makeList();
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 0: /* success */
                switch (_field.type) {
                  case TType.LIST:
                    List<TFrozenChannel> success_item;
                    TList _list_success_item = _iprot.readListBegin();
                    success_item = new ArrayList<TFrozenChannel>();
                    int _i_success_item = 0;
                    TFrozenChannel _success_item_element;
                    while (_i_success_item < _list_success_item.size) {
                      _success_item_element = TFrozenChannel.decode(_iprot);
                      success_item.add(_success_item_element);
                      _i_success_item += 1;
                    }
                    _iprot.readListEnd();
                    success = success_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.success(success);
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
  
      public void encode(findAllFrozenList_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static findAllFrozenList_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(findAllFrozenList_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public findAllFrozenList_result(
    ScroogeOption<List<TFrozenChannel>> success
    ) {
      this.success = success;
    }
  
    public List<TFrozenChannel> getSuccess() {
      return this.success.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        List<TFrozenChannel> success_item = success.get();
        _oprot.writeListBegin(new TList(TType.STRUCT, success_item.size()));
        for (TFrozenChannel _success_item_element : success_item) {
          _success_item_element.write(_oprot);
        }
        _oprot.writeListEnd();
        _oprot.writeFieldEnd();
      }
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof findAllFrozenList_result)) return false;
      findAllFrozenList_result that = (findAllFrozenList_result) other;
      return
  this.success.equals(that.success);
    }
  
    public String toString() {
      return "findAllFrozenList_result(" + this.success + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.success.isDefined() ? 0 : this.success.get().hashCode());
      return hash;
    }
  }
  public static class FinagledClient implements FutureIface {
    private com.twitter.finagle.Service<ThriftClientRequest, byte[]> service;
    private String serviceName;
    private TProtocolFactory protocolFactory /* new TBinaryProtocol.Factory */;
    private StatsReceiver scopedStats = new NullStatsReceiver();
  
    public FinagledClient(
      com.twitter.finagle.Service<ThriftClientRequest, byte[]> service,
      TProtocolFactory protocolFactory /* new TBinaryProtocol.Factory */,
      String serviceName,
      StatsReceiver stats
    ) {
      this.service = service;
      this.serviceName = serviceName;
      this.protocolFactory = protocolFactory;
      if (serviceName != "") {
        this.scopedStats = stats.scope(serviceName);
      } else {
        this.scopedStats = stats;
      }
    }
    
    public FinagledClient(
            com.twitter.finagle.Service<ThriftClientRequest, byte[]> service,
            TProtocolFactory protocolFactory /* new TBinaryProtocol.Factory */
    ) {
        this.service = service;
        this.protocolFactory = protocolFactory;
        this.serviceName = "AdDataAccessServices";
    }    
  
    // ----- boilerplate that should eventually be moved into finagle:
  
    protected ThriftClientRequest encodeRequest(String name, ThriftStruct args) {
      TMemoryBuffer buf = new TMemoryBuffer(512);
      TProtocol oprot = protocolFactory.getProtocol(buf);
  
      try {
        oprot.writeMessageBegin(new TMessage(name, TMessageType.CALL, 0));
        args.write(oprot);
        oprot.writeMessageEnd();
      } catch (TException e) {
        // not real.
      }
  
      byte[] bytes = Arrays.copyOfRange(buf.getArray(), 0, buf.length());
      return new ThriftClientRequest(bytes, false);
    }
  
    protected <T extends ThriftStruct> T decodeResponse(byte[] resBytes, ThriftStructCodec<T> codec) throws TException {
      TProtocol iprot = protocolFactory.getProtocol(new TMemoryInputTransport(resBytes));
      TMessage msg = iprot.readMessageBegin();
      try {
        if (msg.type == TMessageType.EXCEPTION) {
          TException exception = TApplicationException.read(iprot);
          if (exception instanceof SourcedException) {
            if (this.serviceName != "") ((SourcedException) exception).serviceName_$eq(this.serviceName);
          }
          throw exception;
        } else {
          return codec.decode(iprot);
        }
      } finally {
        iprot.readMessageEnd();
      }
    }
  
    protected Exception missingResult(String name) {
      return new TApplicationException(
        TApplicationException.MISSING_RESULT,
        "`" + name + "` failed: unknown result"
      );
    }
  
    class __Stats {
      Counter requestsCounter, successCounter, failuresCounter;
      StatsReceiver failuresScope;
  
      public __Stats(String name) {
        StatsReceiver scope = FinagledClient.this.scopedStats.scope(name);
        this.requestsCounter = scope.counter0("requests");
        this.successCounter = scope.counter0("success");
        this.failuresCounter = scope.counter0("failures");
        this.failuresScope = scope.scope("failures");
      }
    }
  
    // ----- end boilerplate.
  
    private __Stats __stats_findAdItems = new __Stats("findAdItems");
  
    public Future<List<TAdItem>> findAdItems(TAdCriteria criteria, TPagination pagination) {
      __stats_findAdItems.requestsCounter.incr();
  
      Future<List<TAdItem>> rv = this.service.apply(encodeRequest("findAdItems", new findAdItems_args(criteria, pagination))).flatMap(new Function<byte[], Future<List<TAdItem>>>() {
        public Future<List<TAdItem>> apply(byte[] in) {
          try {
            findAdItems_result result = decodeResponse(in, findAdItems_result.CODEC);
  
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("findAdItems"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<List<TAdItem>>>() {
        public Future<List<TAdItem>> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<List<TAdItem>>() {
        public void onSuccess(List<TAdItem> result) {
          __stats_findAdItems.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_findAdItems.failuresCounter.incr();
          __stats_findAdItems.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_findAdStats = new __Stats("findAdStats");
  
    public Future<List<TAdStats>> findAdStats(TAdStatsCriteria criteria, TPagination pagination) {
      __stats_findAdStats.requestsCounter.incr();
  
      Future<List<TAdStats>> rv = this.service.apply(encodeRequest("findAdStats", new findAdStats_args(criteria, pagination))).flatMap(new Function<byte[], Future<List<TAdStats>>>() {
        public Future<List<TAdStats>> apply(byte[] in) {
          try {
            findAdStats_result result = decodeResponse(in, findAdStats_result.CODEC);
  
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("findAdStats"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<List<TAdStats>>>() {
        public Future<List<TAdStats>> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<List<TAdStats>>() {
        public void onSuccess(List<TAdStats> result) {
          __stats_findAdStats.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_findAdStats.failuresCounter.incr();
          __stats_findAdStats.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_findCampaignBudgets = new __Stats("findCampaignBudgets");
  
    public Future<List<TCampaignBudget>> findCampaignBudgets(List<Integer> campaignIds) {
      __stats_findCampaignBudgets.requestsCounter.incr();
  
      Future<List<TCampaignBudget>> rv = this.service.apply(encodeRequest("findCampaignBudgets", new findCampaignBudgets_args(campaignIds))).flatMap(new Function<byte[], Future<List<TCampaignBudget>>>() {
        public Future<List<TCampaignBudget>> apply(byte[] in) {
          try {
            findCampaignBudgets_result result = decodeResponse(in, findCampaignBudgets_result.CODEC);
  
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("findCampaignBudgets"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<List<TCampaignBudget>>>() {
        public Future<List<TCampaignBudget>> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<List<TCampaignBudget>>() {
        public void onSuccess(List<TCampaignBudget> result) {
          __stats_findCampaignBudgets.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_findCampaignBudgets.failuresCounter.incr();
          __stats_findCampaignBudgets.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_updateAdItem = new __Stats("updateAdItem");
  
    public Future<Void> updateAdItem(TAdItem adItem) {
      __stats_updateAdItem.requestsCounter.incr();
  
      Future<Void> rv = this.service.apply(encodeRequest("updateAdItem", new updateAdItem_args(adItem))).flatMap(new Function<byte[], Future<Void>>() {
        public Future<Void> apply(byte[] in) {
          try {
            updateAdItem_result result = decodeResponse(in, updateAdItem_result.CODEC);
  
            Exception exception = null;
            if (exception == null && result.mrfe.isDefined()) exception = result.mrfe.get();
            if (exception != null) return Future.exception(exception);
  
            return Future.value(null);
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<Void>>() {
        public Future<Void> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<Void>() {
        public void onSuccess(Void result) {
          __stats_updateAdItem.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_updateAdItem.failuresCounter.incr();
          __stats_updateAdItem.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_findPublisherContact = new __Stats("findPublisherContact");
  
    public Future<TPublisherContactInfo> findPublisherContact(int userId) {
      __stats_findPublisherContact.requestsCounter.incr();
  
      Future<TPublisherContactInfo> rv = this.service.apply(encodeRequest("findPublisherContact", new findPublisherContact_args(userId))).flatMap(new Function<byte[], Future<TPublisherContactInfo>>() {
        public Future<TPublisherContactInfo> apply(byte[] in) {
          try {
            findPublisherContact_result result = decodeResponse(in, findPublisherContact_result.CODEC);
  
            Exception exception = null;
            if (exception == null && result.pcnfe.isDefined()) exception = result.pcnfe.get();
            if (exception != null) return Future.exception(exception);
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("findPublisherContact"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<TPublisherContactInfo>>() {
        public Future<TPublisherContactInfo> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<TPublisherContactInfo>() {
        public void onSuccess(TPublisherContactInfo result) {
          __stats_findPublisherContact.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_findPublisherContact.failuresCounter.incr();
          __stats_findPublisherContact.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_saveOrUpdatePublisherContact = new __Stats("saveOrUpdatePublisherContact");
  
    public Future<Void> saveOrUpdatePublisherContact(TPublisherContactInfo publisherContact) {
      __stats_saveOrUpdatePublisherContact.requestsCounter.incr();
  
      Future<Void> rv = this.service.apply(encodeRequest("saveOrUpdatePublisherContact", new saveOrUpdatePublisherContact_args(publisherContact))).flatMap(new Function<byte[], Future<Void>>() {
        public Future<Void> apply(byte[] in) {
          try {
            saveOrUpdatePublisherContact_result result = decodeResponse(in, saveOrUpdatePublisherContact_result.CODEC);
  
            Exception exception = null;
            if (exception == null && result.mrfe.isDefined()) exception = result.mrfe.get();
            if (exception != null) return Future.exception(exception);
  
            return Future.value(null);
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<Void>>() {
        public Future<Void> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<Void>() {
        public void onSuccess(Void result) {
          __stats_saveOrUpdatePublisherContact.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_saveOrUpdatePublisherContact.failuresCounter.incr();
          __stats_saveOrUpdatePublisherContact.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_getPublisherSiteConfig = new __Stats("getPublisherSiteConfig");
  
    public Future<TPublisherSiteConfig> getPublisherSiteConfig(String uuid) {
      __stats_getPublisherSiteConfig.requestsCounter.incr();
  
      Future<TPublisherSiteConfig> rv = this.service.apply(encodeRequest("getPublisherSiteConfig", new getPublisherSiteConfig_args(uuid))).flatMap(new Function<byte[], Future<TPublisherSiteConfig>>() {
        public Future<TPublisherSiteConfig> apply(byte[] in) {
          try {
            getPublisherSiteConfig_result result = decodeResponse(in, getPublisherSiteConfig_result.CODEC);
  
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("getPublisherSiteConfig"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<TPublisherSiteConfig>>() {
        public Future<TPublisherSiteConfig> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<TPublisherSiteConfig>() {
        public void onSuccess(TPublisherSiteConfig result) {
          __stats_getPublisherSiteConfig.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_getPublisherSiteConfig.failuresCounter.incr();
          __stats_getPublisherSiteConfig.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
    private __Stats __stats_findAllFrozenList = new __Stats("findAllFrozenList");
  
    public Future<List<TFrozenChannel>> findAllFrozenList() {
      __stats_findAllFrozenList.requestsCounter.incr();
  
      Future<List<TFrozenChannel>> rv = this.service.apply(encodeRequest("findAllFrozenList", new findAllFrozenList_args())).flatMap(new Function<byte[], Future<List<TFrozenChannel>>>() {
        public Future<List<TFrozenChannel>> apply(byte[] in) {
          try {
            findAllFrozenList_result result = decodeResponse(in, findAllFrozenList_result.CODEC);
  
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("findAllFrozenList"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<List<TFrozenChannel>>>() {
        public Future<List<TFrozenChannel>> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<List<TFrozenChannel>>() {
        public void onSuccess(List<TFrozenChannel> result) {
          __stats_findAllFrozenList.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_findAllFrozenList.failuresCounter.incr();
          __stats_findAllFrozenList.failuresScope.counter0(t.getClass().getName()).incr();
        }
      });
  
      return rv;
    }
  }
  public static class FinagledService extends Service<byte[], byte[]> {
    final private FutureIface iface;
    final private TProtocolFactory protocolFactory;
  
    public FinagledService(final FutureIface iface, final TProtocolFactory protocolFactory) {
      this.iface = iface;
      this.protocolFactory = protocolFactory;
  
      addFunction("findAdItems", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            findAdItems_args args = findAdItems_args.decode(iprot);
            iprot.readMessageEnd();
            Future<List<TAdItem>> result;
            try {
              result = iface.findAdItems(args.criteria, args.pagination);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<List<TAdItem>, Future<byte[]>>() {
              public Future<byte[]> apply(List<TAdItem> value){
                return reply("findAdItems", seqid, new findAdItems_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("findAdItems", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("findAdStats", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            findAdStats_args args = findAdStats_args.decode(iprot);
            iprot.readMessageEnd();
            Future<List<TAdStats>> result;
            try {
              result = iface.findAdStats(args.criteria, args.pagination);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<List<TAdStats>, Future<byte[]>>() {
              public Future<byte[]> apply(List<TAdStats> value){
                return reply("findAdStats", seqid, new findAdStats_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("findAdStats", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("findCampaignBudgets", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            findCampaignBudgets_args args = findCampaignBudgets_args.decode(iprot);
            iprot.readMessageEnd();
            Future<List<TCampaignBudget>> result;
            try {
              result = iface.findCampaignBudgets(args.campaignIds);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<List<TCampaignBudget>, Future<byte[]>>() {
              public Future<byte[]> apply(List<TCampaignBudget> value){
                return reply("findCampaignBudgets", seqid, new findCampaignBudgets_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("findCampaignBudgets", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("updateAdItem", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            updateAdItem_args args = updateAdItem_args.decode(iprot);
            iprot.readMessageEnd();
            Future<Void> result;
            try {
              result = iface.updateAdItem(args.adItem);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<Void, Future<byte[]>>() {
              public Future<byte[]> apply(Void value){
                return reply("updateAdItem", seqid, new updateAdItem_result.Builder().build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                if (t instanceof MissingRequiredFieldException) {
                  return reply("updateAdItem", seqid, new updateAdItem_result.Builder().mrfe((MissingRequiredFieldException) t).build());
                }
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("updateAdItem", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("findPublisherContact", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            findPublisherContact_args args = findPublisherContact_args.decode(iprot);
            iprot.readMessageEnd();
            Future<TPublisherContactInfo> result;
            try {
              result = iface.findPublisherContact(args.userId);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<TPublisherContactInfo, Future<byte[]>>() {
              public Future<byte[]> apply(TPublisherContactInfo value){
                return reply("findPublisherContact", seqid, new findPublisherContact_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                if (t instanceof PublisherContactNotFoundException) {
                  return reply("findPublisherContact", seqid, new findPublisherContact_result.Builder().pcnfe((PublisherContactNotFoundException) t).build());
                }
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("findPublisherContact", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("saveOrUpdatePublisherContact", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            saveOrUpdatePublisherContact_args args = saveOrUpdatePublisherContact_args.decode(iprot);
            iprot.readMessageEnd();
            Future<Void> result;
            try {
              result = iface.saveOrUpdatePublisherContact(args.publisherContact);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<Void, Future<byte[]>>() {
              public Future<byte[]> apply(Void value){
                return reply("saveOrUpdatePublisherContact", seqid, new saveOrUpdatePublisherContact_result.Builder().build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                if (t instanceof MissingRequiredFieldException) {
                  return reply("saveOrUpdatePublisherContact", seqid, new saveOrUpdatePublisherContact_result.Builder().mrfe((MissingRequiredFieldException) t).build());
                }
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("saveOrUpdatePublisherContact", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("getPublisherSiteConfig", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            getPublisherSiteConfig_args args = getPublisherSiteConfig_args.decode(iprot);
            iprot.readMessageEnd();
            Future<TPublisherSiteConfig> result;
            try {
              result = iface.getPublisherSiteConfig(args.uuid);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<TPublisherSiteConfig, Future<byte[]>>() {
              public Future<byte[]> apply(TPublisherSiteConfig value){
                return reply("getPublisherSiteConfig", seqid, new getPublisherSiteConfig_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("getPublisherSiteConfig", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
      addFunction("findAllFrozenList", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            findAllFrozenList_args args = findAllFrozenList_args.decode(iprot);
            iprot.readMessageEnd();
            Future<List<TFrozenChannel>> result;
            try {
              result = iface.findAllFrozenList();
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<List<TFrozenChannel>, Future<byte[]>>() {
              public Future<byte[]> apply(List<TFrozenChannel> value){
                return reply("findAllFrozenList", seqid, new findAllFrozenList_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("findAllFrozenList", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
            } catch (Exception unrecoverable) {
              return Future.exception(unrecoverable);
            }
          } catch (Throwable t) {
            return Future.exception(t);
          }
        }
      });
    }
  
    // ----- boilerplate that should eventually be moved into finagle:
  
    protected Map<String, Function2<TProtocol, Integer, Future<byte[]>>> functionMap =
      new HashMap<String, Function2<TProtocol, Integer, Future<byte[]>>>();
  
    protected void addFunction(String name, Function2<TProtocol, Integer, Future<byte[]>> fn) {
      functionMap.put(name, fn);
    }
  
    protected Function2<TProtocol, Integer, Future<byte[]>> getFunction(String name) {
      return functionMap.get(name);
    }
  
    protected Future<byte[]> exception(String name, int seqid, int code, String message) {
      try {
        TApplicationException x = new TApplicationException(code, message);
        TMemoryBuffer memoryBuffer = new TMemoryBuffer(512);
        TProtocol oprot = protocolFactory.getProtocol(memoryBuffer);
  
        oprot.writeMessageBegin(new TMessage(name, TMessageType.EXCEPTION, seqid));
        x.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
        return Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()));
      } catch (Exception e) {
        return Future.exception(e);
      }
    }
  
    protected Future<byte[]> reply(String name, int seqid, ThriftStruct result) {
      try {
        TMemoryBuffer memoryBuffer = new TMemoryBuffer(512);
        TProtocol oprot = protocolFactory.getProtocol(memoryBuffer);
  
        oprot.writeMessageBegin(new TMessage(name, TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
  
        return Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()));
      } catch (Exception e) {
        return Future.exception(e);
      }
    }
  
    public final Future<byte[]> apply(byte[] request) {
      TTransport inputTransport = new TMemoryInputTransport(request);
      TProtocol iprot = protocolFactory.getProtocol(inputTransport);
  
      try {
        TMessage msg = iprot.readMessageBegin();
        Function2<TProtocol, Integer, Future<byte[]>> f = functionMap.get(msg.name);
        if (f != null) {
          return f.apply(iprot, msg.seqid);
        } else {
          TProtocolUtil.skip(iprot, TType.STRUCT);
          return exception(msg.name, msg.seqid, TApplicationException.UNKNOWN_METHOD, "Invalid method name: '" + msg.name + "'");
        }
      } catch (Exception e) {
        return Future.exception(e);
      }
    }
  
    // ---- end boilerplate.
  }
}