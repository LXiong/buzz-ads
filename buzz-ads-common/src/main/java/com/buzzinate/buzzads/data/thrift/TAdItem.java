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
public class TAdItem implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TAdItem");
  private static final TField CampaignIdField = new TField("campaignId", TType.I32, (short) 1);
  final int campaignId;
  private static final TField OrderIdField = new TField("orderId", TType.I32, (short) 2);
  final int orderId;
  private static final TField EntryIdField = new TField("entryId", TType.I32, (short) 3);
  final int entryId;
  private static final TField AdvertiserIdField = new TField("advertiserId", TType.I32, (short) 4);
  final int advertiserId;
  private static final TField StatusField = new TField("status", TType.I32, (short) 5);
  final TAdStatusEnum status;
  private static final TField BidPriceField = new TField("bidPrice", TType.I32, (short) 6);
  final ScroogeOption<Integer> bidPrice;
  private static final TField KeywordsField = new TField("keywords", TType.STRING, (short) 7);
  final ScroogeOption<String> keywords;
  private static final TField LinkField = new TField("link", TType.STRING, (short) 8);
  final ScroogeOption<String> link;
  private static final TField ResourceTypeField = new TField("resourceType", TType.I32, (short) 9);
  final ScroogeOption<TResourceTypeEnum> resourceType;
  private static final TField ResourceUrlField = new TField("resourceUrl", TType.STRING, (short) 10);
  final ScroogeOption<String> resourceUrl;
  private static final TField DisplayUrlField = new TField("displayUrl", TType.STRING, (short) 11);
  final ScroogeOption<String> displayUrl;
  private static final TField TitleField = new TField("title", TType.STRING, (short) 12);
  final ScroogeOption<String> title;
  private static final TField DescriptionField = new TField("description", TType.STRING, (short) 13);
  final ScroogeOption<String> description;
  private static final TField NetworkField = new TField("network", TType.SET, (short) 14);
  final ScroogeOption<Set<TAdNetworkEnum>> network;
  private static final TField BidTypeField = new TField("bidType", TType.I32, (short) 15);
  final ScroogeOption<TBidTypeEnum> bidType;
  private static final TField StartDateField = new TField("startDate", TType.I64, (short) 16);
  final ScroogeOption<Long> startDate;
  private static final TField EndDateField = new TField("endDate", TType.I64, (short) 17);
  final ScroogeOption<Long> endDate;
  private static final TField ScheduleDayField = new TField("scheduleDay", TType.SET, (short) 18);
  final ScroogeOption<Set<TWeekDay>> scheduleDay;
  private static final TField ScheduleTimeField = new TField("scheduleTime", TType.SET, (short) 19);
  final ScroogeOption<Set<TScheduleTime>> scheduleTime;
  private static final TField LocationsField = new TField("locations", TType.SET, (short) 20);
  final ScroogeOption<Set<String>> locations;
  private static final TField AudienceCategoriesField = new TField("audienceCategories", TType.SET, (short) 21);
  final ScroogeOption<Set<Integer>> audienceCategories;
  private static final TField DestinationField = new TField("destination", TType.STRING, (short) 22);
  final ScroogeOption<String> destination;
  private static final TField OrderFrequencyField = new TField("orderFrequency", TType.I32, (short) 23);
  final int orderFrequency;
  private static final TField EntryFrequencyField = new TField("entryFrequency", TType.I32, (short) 24);
  final int entryFrequency;
  private static final TField AdsTypeField = new TField("adsType", TType.I32, (short) 25);
  final TAdsTypeEnum adsType;
  private static final TField ChannelsField = new TField("channels", TType.SET, (short) 26);
  final ScroogeOption<Set<String>> channels;
  private static final TField ResourceSizeField = new TField("resourceSize", TType.I32, (short) 27);
  final AdEntrySizeEnum resourceSize;

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
    private TAdStatusEnum _status = null;
    private Boolean _got_status = false;

    public Builder status(TAdStatusEnum value) {
      this._status = value;
      this._got_status = true;
      return this;
    }
    private int _bidPrice = 0;
    private Boolean _got_bidPrice = false;

    public Builder bidPrice(int value) {
      this._bidPrice = value;
      this._got_bidPrice = true;
      return this;
    }
    private String _keywords = null;
    private Boolean _got_keywords = false;

    public Builder keywords(String value) {
      this._keywords = value;
      this._got_keywords = true;
      return this;
    }
    private String _link = null;
    private Boolean _got_link = false;

    public Builder link(String value) {
      this._link = value;
      this._got_link = true;
      return this;
    }
    private TResourceTypeEnum _resourceType = null;
    private Boolean _got_resourceType = false;

    public Builder resourceType(TResourceTypeEnum value) {
      this._resourceType = value;
      this._got_resourceType = true;
      return this;
    }
    private String _resourceUrl = null;
    private Boolean _got_resourceUrl = false;

    public Builder resourceUrl(String value) {
      this._resourceUrl = value;
      this._got_resourceUrl = true;
      return this;
    }
    private String _displayUrl = null;
    private Boolean _got_displayUrl = false;

    public Builder displayUrl(String value) {
      this._displayUrl = value;
      this._got_displayUrl = true;
      return this;
    }
    private String _title = null;
    private Boolean _got_title = false;

    public Builder title(String value) {
      this._title = value;
      this._got_title = true;
      return this;
    }
    private String _description = null;
    private Boolean _got_description = false;

    public Builder description(String value) {
      this._description = value;
      this._got_description = true;
      return this;
    }
    private Set<TAdNetworkEnum> _network = Utilities.makeSet();
    private Boolean _got_network = false;

    public Builder network(Set<TAdNetworkEnum> value) {
      this._network = value;
      this._got_network = true;
      return this;
    }
    private TBidTypeEnum _bidType = null;
    private Boolean _got_bidType = false;

    public Builder bidType(TBidTypeEnum value) {
      this._bidType = value;
      this._got_bidType = true;
      return this;
    }
    private long _startDate = (long) 0;
    private Boolean _got_startDate = false;

    public Builder startDate(long value) {
      this._startDate = value;
      this._got_startDate = true;
      return this;
    }
    private long _endDate = (long) 0;
    private Boolean _got_endDate = false;

    public Builder endDate(long value) {
      this._endDate = value;
      this._got_endDate = true;
      return this;
    }
    private Set<TWeekDay> _scheduleDay = Utilities.makeSet();
    private Boolean _got_scheduleDay = false;

    public Builder scheduleDay(Set<TWeekDay> value) {
      this._scheduleDay = value;
      this._got_scheduleDay = true;
      return this;
    }
    private Set<TScheduleTime> _scheduleTime = Utilities.makeSet();
    private Boolean _got_scheduleTime = false;

    public Builder scheduleTime(Set<TScheduleTime> value) {
      this._scheduleTime = value;
      this._got_scheduleTime = true;
      return this;
    }
    private Set<String> _locations = Utilities.makeSet();
    private Boolean _got_locations = false;

    public Builder locations(Set<String> value) {
      this._locations = value;
      this._got_locations = true;
      return this;
    }
    private Set<Integer> _audienceCategories = Utilities.makeSet();
    private Boolean _got_audienceCategories = false;

    public Builder audienceCategories(Set<Integer> value) {
      this._audienceCategories = value;
      this._got_audienceCategories = true;
      return this;
    }
    private String _destination = null;
    private Boolean _got_destination = false;

    public Builder destination(String value) {
      this._destination = value;
      this._got_destination = true;
      return this;
    }
    private int _orderFrequency = 0;
    private Boolean _got_orderFrequency = false;

    public Builder orderFrequency(int value) {
      this._orderFrequency = value;
      this._got_orderFrequency = true;
      return this;
    }
    private int _entryFrequency = 0;
    private Boolean _got_entryFrequency = false;

    public Builder entryFrequency(int value) {
      this._entryFrequency = value;
      this._got_entryFrequency = true;
      return this;
    }
    private TAdsTypeEnum _adsType = TAdsTypeEnum.AVERAGE;
    private Boolean _got_adsType = false;

    public Builder adsType(TAdsTypeEnum value) {
      this._adsType = value;
      this._got_adsType = true;
      return this;
    }
    private Set<String> _channels = Utilities.makeSet();
    private Boolean _got_channels = false;

    public Builder channels(Set<String> value) {
      this._channels = value;
      this._got_channels = true;
      return this;
    }
    private AdEntrySizeEnum _resourceSize = AdEntrySizeEnum.SIZE80X80;
    private Boolean _got_resourceSize = false;

    public Builder resourceSize(AdEntrySizeEnum value) {
      this._resourceSize = value;
      this._got_resourceSize = true;
      return this;
    }

    public TAdItem build() {
      if (!_got_resourceSize)
      throw new IllegalStateException("Required field 'resourceSize' was not found for struct TAdItem");
      return new TAdItem(
        this._campaignId,
        this._orderId,
        this._entryId,
        this._advertiserId,
        this._status,
      ScroogeOption.make(this._got_bidPrice, this._bidPrice),
      ScroogeOption.make(this._got_keywords, this._keywords),
      ScroogeOption.make(this._got_link, this._link),
      ScroogeOption.make(this._got_resourceType, this._resourceType),
      ScroogeOption.make(this._got_resourceUrl, this._resourceUrl),
      ScroogeOption.make(this._got_displayUrl, this._displayUrl),
      ScroogeOption.make(this._got_title, this._title),
      ScroogeOption.make(this._got_description, this._description),
      ScroogeOption.make(this._got_network, this._network),
      ScroogeOption.make(this._got_bidType, this._bidType),
      ScroogeOption.make(this._got_startDate, this._startDate),
      ScroogeOption.make(this._got_endDate, this._endDate),
      ScroogeOption.make(this._got_scheduleDay, this._scheduleDay),
      ScroogeOption.make(this._got_scheduleTime, this._scheduleTime),
      ScroogeOption.make(this._got_locations, this._locations),
      ScroogeOption.make(this._got_audienceCategories, this._audienceCategories),
      ScroogeOption.make(this._got_destination, this._destination),
        this._orderFrequency,
        this._entryFrequency,
        this._adsType,
      ScroogeOption.make(this._got_channels, this._channels),
        this._resourceSize    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.campaignId(this.campaignId);
    builder.orderId(this.orderId);
    builder.entryId(this.entryId);
    builder.advertiserId(this.advertiserId);
    builder.status(this.status);
    if (this.bidPrice.isDefined()) builder.bidPrice(this.bidPrice.get());
    if (this.keywords.isDefined()) builder.keywords(this.keywords.get());
    if (this.link.isDefined()) builder.link(this.link.get());
    if (this.resourceType.isDefined()) builder.resourceType(this.resourceType.get());
    if (this.resourceUrl.isDefined()) builder.resourceUrl(this.resourceUrl.get());
    if (this.displayUrl.isDefined()) builder.displayUrl(this.displayUrl.get());
    if (this.title.isDefined()) builder.title(this.title.get());
    if (this.description.isDefined()) builder.description(this.description.get());
    if (this.network.isDefined()) builder.network(this.network.get());
    if (this.bidType.isDefined()) builder.bidType(this.bidType.get());
    if (this.startDate.isDefined()) builder.startDate(this.startDate.get());
    if (this.endDate.isDefined()) builder.endDate(this.endDate.get());
    if (this.scheduleDay.isDefined()) builder.scheduleDay(this.scheduleDay.get());
    if (this.scheduleTime.isDefined()) builder.scheduleTime(this.scheduleTime.get());
    if (this.locations.isDefined()) builder.locations(this.locations.get());
    if (this.audienceCategories.isDefined()) builder.audienceCategories(this.audienceCategories.get());
    if (this.destination.isDefined()) builder.destination(this.destination.get());
    builder.orderFrequency(this.orderFrequency);
    builder.entryFrequency(this.entryFrequency);
    builder.adsType(this.adsType);
    if (this.channels.isDefined()) builder.channels(this.channels.get());
    builder.resourceSize(this.resourceSize);
    return builder;
  }

  public static ThriftStructCodec<TAdItem> CODEC = new ThriftStructCodec<TAdItem>() {
    public TAdItem decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      int campaignId = 0;
      int orderId = 0;
      int entryId = 0;
      int advertiserId = 0;
      TAdStatusEnum status = null;
      int bidPrice = 0;
      String keywords = null;
      String link = null;
      TResourceTypeEnum resourceType = null;
      String resourceUrl = null;
      String displayUrl = null;
      String title = null;
      String description = null;
      Set<TAdNetworkEnum> network = Utilities.makeSet();
      TBidTypeEnum bidType = null;
      long startDate = (long) 0;
      long endDate = (long) 0;
      Set<TWeekDay> scheduleDay = Utilities.makeSet();
      Set<TScheduleTime> scheduleTime = Utilities.makeSet();
      Set<String> locations = Utilities.makeSet();
      Set<Integer> audienceCategories = Utilities.makeSet();
      String destination = null;
      int orderFrequency = 0;
      int entryFrequency = 0;
      TAdsTypeEnum adsType = TAdsTypeEnum.AVERAGE;
      Set<String> channels = Utilities.makeSet();
      AdEntrySizeEnum resourceSize = AdEntrySizeEnum.SIZE80X80;
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
                case TType.I32:
                  TAdStatusEnum status_item;
                  status_item = TAdStatusEnum.findByValue(_iprot.readI32());
                  status = status_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.status(status);
              break;
            case 6: /* bidPrice */
              switch (_field.type) {
                case TType.I32:
                  Integer bidPrice_item;
                  bidPrice_item = _iprot.readI32();
                  bidPrice = bidPrice_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.bidPrice(bidPrice);
              break;
            case 7: /* keywords */
              switch (_field.type) {
                case TType.STRING:
                  String keywords_item;
                  keywords_item = _iprot.readString();
                  keywords = keywords_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.keywords(keywords);
              break;
            case 8: /* link */
              switch (_field.type) {
                case TType.STRING:
                  String link_item;
                  link_item = _iprot.readString();
                  link = link_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.link(link);
              break;
            case 9: /* resourceType */
              switch (_field.type) {
                case TType.I32:
                  TResourceTypeEnum resourceType_item;
                  resourceType_item = TResourceTypeEnum.findByValue(_iprot.readI32());
                  resourceType = resourceType_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.resourceType(resourceType);
              break;
            case 10: /* resourceUrl */
              switch (_field.type) {
                case TType.STRING:
                  String resourceUrl_item;
                  resourceUrl_item = _iprot.readString();
                  resourceUrl = resourceUrl_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.resourceUrl(resourceUrl);
              break;
            case 11: /* displayUrl */
              switch (_field.type) {
                case TType.STRING:
                  String displayUrl_item;
                  displayUrl_item = _iprot.readString();
                  displayUrl = displayUrl_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.displayUrl(displayUrl);
              break;
            case 12: /* title */
              switch (_field.type) {
                case TType.STRING:
                  String title_item;
                  title_item = _iprot.readString();
                  title = title_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.title(title);
              break;
            case 13: /* description */
              switch (_field.type) {
                case TType.STRING:
                  String description_item;
                  description_item = _iprot.readString();
                  description = description_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.description(description);
              break;
            case 14: /* network */
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
            case 15: /* bidType */
              switch (_field.type) {
                case TType.I32:
                  TBidTypeEnum bidType_item;
                  bidType_item = TBidTypeEnum.findByValue(_iprot.readI32());
                  bidType = bidType_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.bidType(bidType);
              break;
            case 16: /* startDate */
              switch (_field.type) {
                case TType.I64:
                  Long startDate_item;
                  startDate_item = _iprot.readI64();
                  startDate = startDate_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.startDate(startDate);
              break;
            case 17: /* endDate */
              switch (_field.type) {
                case TType.I64:
                  Long endDate_item;
                  endDate_item = _iprot.readI64();
                  endDate = endDate_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.endDate(endDate);
              break;
            case 18: /* scheduleDay */
              switch (_field.type) {
                case TType.SET:
                  Set<TWeekDay> scheduleDay_item;
                  TSet _set_scheduleDay_item = _iprot.readSetBegin();
                  scheduleDay_item = new HashSet<TWeekDay>();
                  int _i_scheduleDay_item = 0;
                  TWeekDay _scheduleDay_item_element;
                  while (_i_scheduleDay_item < _set_scheduleDay_item.size) {
                    _scheduleDay_item_element = TWeekDay.findByValue(_iprot.readI32());
                    scheduleDay_item.add(_scheduleDay_item_element);
                    _i_scheduleDay_item += 1;
                  }
                  _iprot.readSetEnd();
                  scheduleDay = scheduleDay_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.scheduleDay(scheduleDay);
              break;
            case 19: /* scheduleTime */
              switch (_field.type) {
                case TType.SET:
                  Set<TScheduleTime> scheduleTime_item;
                  TSet _set_scheduleTime_item = _iprot.readSetBegin();
                  scheduleTime_item = new HashSet<TScheduleTime>();
                  int _i_scheduleTime_item = 0;
                  TScheduleTime _scheduleTime_item_element;
                  while (_i_scheduleTime_item < _set_scheduleTime_item.size) {
                    _scheduleTime_item_element = TScheduleTime.decode(_iprot);
                    scheduleTime_item.add(_scheduleTime_item_element);
                    _i_scheduleTime_item += 1;
                  }
                  _iprot.readSetEnd();
                  scheduleTime = scheduleTime_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.scheduleTime(scheduleTime);
              break;
            case 20: /* locations */
              switch (_field.type) {
                case TType.SET:
                  Set<String> locations_item;
                  TSet _set_locations_item = _iprot.readSetBegin();
                  locations_item = new HashSet<String>();
                  int _i_locations_item = 0;
                  String _locations_item_element;
                  while (_i_locations_item < _set_locations_item.size) {
                    _locations_item_element = _iprot.readString();
                    locations_item.add(_locations_item_element);
                    _i_locations_item += 1;
                  }
                  _iprot.readSetEnd();
                  locations = locations_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.locations(locations);
              break;
            case 21: /* audienceCategories */
              switch (_field.type) {
                case TType.SET:
                  Set<Integer> audienceCategories_item;
                  TSet _set_audienceCategories_item = _iprot.readSetBegin();
                  audienceCategories_item = new HashSet<Integer>();
                  int _i_audienceCategories_item = 0;
                  Integer _audienceCategories_item_element;
                  while (_i_audienceCategories_item < _set_audienceCategories_item.size) {
                    _audienceCategories_item_element = _iprot.readI32();
                    audienceCategories_item.add(_audienceCategories_item_element);
                    _i_audienceCategories_item += 1;
                  }
                  _iprot.readSetEnd();
                  audienceCategories = audienceCategories_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.audienceCategories(audienceCategories);
              break;
            case 22: /* destination */
              switch (_field.type) {
                case TType.STRING:
                  String destination_item;
                  destination_item = _iprot.readString();
                  destination = destination_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.destination(destination);
              break;
            case 23: /* orderFrequency */
              switch (_field.type) {
                case TType.I32:
                  Integer orderFrequency_item;
                  orderFrequency_item = _iprot.readI32();
                  orderFrequency = orderFrequency_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.orderFrequency(orderFrequency);
              break;
            case 24: /* entryFrequency */
              switch (_field.type) {
                case TType.I32:
                  Integer entryFrequency_item;
                  entryFrequency_item = _iprot.readI32();
                  entryFrequency = entryFrequency_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.entryFrequency(entryFrequency);
              break;
            case 25: /* adsType */
              switch (_field.type) {
                case TType.I32:
                  TAdsTypeEnum adsType_item;
                  adsType_item = TAdsTypeEnum.findByValue(_iprot.readI32());
                  adsType = adsType_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.adsType(adsType);
              break;
            case 26: /* channels */
              switch (_field.type) {
                case TType.SET:
                  Set<String> channels_item;
                  TSet _set_channels_item = _iprot.readSetBegin();
                  channels_item = new HashSet<String>();
                  int _i_channels_item = 0;
                  String _channels_item_element;
                  while (_i_channels_item < _set_channels_item.size) {
                    _channels_item_element = _iprot.readString();
                    channels_item.add(_channels_item_element);
                    _i_channels_item += 1;
                  }
                  _iprot.readSetEnd();
                  channels = channels_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.channels(channels);
              break;
            case 27: /* resourceSize */
              switch (_field.type) {
                case TType.I32:
                  AdEntrySizeEnum resourceSize_item;
                  resourceSize_item = AdEntrySizeEnum.findByValue(_iprot.readI32());
                  resourceSize = resourceSize_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.resourceSize(resourceSize);
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

    public void encode(TAdItem struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TAdItem decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TAdItem struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TAdItem(
  int campaignId,
  int orderId,
  int entryId,
  int advertiserId,
  TAdStatusEnum status,
  ScroogeOption<Integer> bidPrice,
  ScroogeOption<String> keywords,
  ScroogeOption<String> link,
  ScroogeOption<TResourceTypeEnum> resourceType,
  ScroogeOption<String> resourceUrl,
  ScroogeOption<String> displayUrl,
  ScroogeOption<String> title,
  ScroogeOption<String> description,
  ScroogeOption<Set<TAdNetworkEnum>> network,
  ScroogeOption<TBidTypeEnum> bidType,
  ScroogeOption<Long> startDate,
  ScroogeOption<Long> endDate,
  ScroogeOption<Set<TWeekDay>> scheduleDay,
  ScroogeOption<Set<TScheduleTime>> scheduleTime,
  ScroogeOption<Set<String>> locations,
  ScroogeOption<Set<Integer>> audienceCategories,
  ScroogeOption<String> destination,
  int orderFrequency,
  int entryFrequency,
  TAdsTypeEnum adsType,
  ScroogeOption<Set<String>> channels,
  AdEntrySizeEnum resourceSize
  ) {
    this.campaignId = campaignId;
    this.orderId = orderId;
    this.entryId = entryId;
    this.advertiserId = advertiserId;
    this.status = status;
    this.bidPrice = bidPrice;
    this.keywords = keywords;
    this.link = link;
    this.resourceType = resourceType;
    this.resourceUrl = resourceUrl;
    this.displayUrl = displayUrl;
    this.title = title;
    this.description = description;
    this.network = network;
    this.bidType = bidType;
    this.startDate = startDate;
    this.endDate = endDate;
    this.scheduleDay = scheduleDay;
    this.scheduleTime = scheduleTime;
    this.locations = locations;
    this.audienceCategories = audienceCategories;
    this.destination = destination;
    this.orderFrequency = orderFrequency;
    this.entryFrequency = entryFrequency;
    this.adsType = adsType;
    this.channels = channels;
    this.resourceSize = resourceSize;
  }

  public int getCampaignId() {
    return this.campaignId;
  }
  public int getOrderId() {
    return this.orderId;
  }
  public int getEntryId() {
    return this.entryId;
  }
  public int getAdvertiserId() {
    return this.advertiserId;
  }
  public TAdStatusEnum getStatus() {
    return this.status;
  }
  public int getBidPrice() {
    return this.bidPrice.get();
  }
  public String getKeywords() {
    return this.keywords.get();
  }
  public String getLink() {
    return this.link.get();
  }
  public TResourceTypeEnum getResourceType() {
    return this.resourceType.get();
  }
  public String getResourceUrl() {
    return this.resourceUrl.get();
  }
  public String getDisplayUrl() {
    return this.displayUrl.get();
  }
  public String getTitle() {
    return this.title.get();
  }
  public String getDescription() {
    return this.description.get();
  }
  public Set<TAdNetworkEnum> getNetwork() {
    return this.network.get();
  }
  public TBidTypeEnum getBidType() {
    return this.bidType.get();
  }
  public long getStartDate() {
    return this.startDate.get();
  }
  public long getEndDate() {
    return this.endDate.get();
  }
  public Set<TWeekDay> getScheduleDay() {
    return this.scheduleDay.get();
  }
  public Set<TScheduleTime> getScheduleTime() {
    return this.scheduleTime.get();
  }
  public Set<String> getLocations() {
    return this.locations.get();
  }
  public Set<Integer> getAudienceCategories() {
    return this.audienceCategories.get();
  }
  public String getDestination() {
    return this.destination.get();
  }
  public int getOrderFrequency() {
    return this.orderFrequency;
  }
  public int getEntryFrequency() {
    return this.entryFrequency;
  }
  public TAdsTypeEnum getAdsType() {
    return this.adsType;
  }
  public Set<String> getChannels() {
    return this.channels.get();
  }
  public AdEntrySizeEnum getResourceSize() {
    return this.resourceSize;
  }
  
  public ScroogeOption getBidPriceOption() {
      return this.bidPrice;
  }
  public ScroogeOption getKeywordsOption() {
      return this.keywords;
  }
  public ScroogeOption getLinkOption() {
      return this.link;
  }
  public ScroogeOption getResourceTypeOption() {
      return this.resourceType;
  }
  public ScroogeOption getResourceUrlOption() {
      return this.resourceUrl;
  }
  public ScroogeOption getDisplayUrlOption() {
      return this.displayUrl;
  }
  public ScroogeOption getTitleOption() {
      return this.title;
  }
  public ScroogeOption getDescriptionOption() {
      return this.description;
  }
  public ScroogeOption getNetworkOption() {
      return this.network;
  }
  public ScroogeOption getBidTypeOption() {
      return this.bidType;
  }
  public ScroogeOption getStartDateOption() {
      return this.startDate;
  }
  public ScroogeOption getEndDateOption() {
      return this.endDate;
  }
  public ScroogeOption getScheduleDayOption() {
      return this.scheduleDay;
  }
  public ScroogeOption getScheduleTimeOption() {
      return this.scheduleTime;
  }
  public ScroogeOption getLocationsOption() {
      return this.locations;
  }
  public ScroogeOption getAudienceCategoriesOption() {
      return this.audienceCategories;
  }
  public ScroogeOption getDestinationOption() {
      return this.destination;
  }
  public ScroogeOption getChannelsOption() {
      return this.channels;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(CampaignIdField);
      Integer campaignId_item = campaignId;
      _oprot.writeI32(campaignId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(OrderIdField);
      Integer orderId_item = orderId;
      _oprot.writeI32(orderId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(EntryIdField);
      Integer entryId_item = entryId;
      _oprot.writeI32(entryId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(AdvertiserIdField);
      Integer advertiserId_item = advertiserId;
      _oprot.writeI32(advertiserId_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(StatusField);
      TAdStatusEnum status_item = status;
      _oprot.writeI32(status_item.getValue());
      _oprot.writeFieldEnd();
    if (bidPrice.isDefined()) {  _oprot.writeFieldBegin(BidPriceField);
      Integer bidPrice_item = bidPrice.get();
      _oprot.writeI32(bidPrice_item);
      _oprot.writeFieldEnd();
    }
    if (keywords.isDefined()) {  _oprot.writeFieldBegin(KeywordsField);
      String keywords_item = keywords.get();
      _oprot.writeString(keywords_item);
      _oprot.writeFieldEnd();
    }
    if (link.isDefined()) {  _oprot.writeFieldBegin(LinkField);
      String link_item = link.get();
      _oprot.writeString(link_item);
      _oprot.writeFieldEnd();
    }
    if (resourceType.isDefined()) {  _oprot.writeFieldBegin(ResourceTypeField);
      TResourceTypeEnum resourceType_item = resourceType.get();
      _oprot.writeI32(resourceType_item.getValue());
      _oprot.writeFieldEnd();
    }
    if (resourceUrl.isDefined()) {  _oprot.writeFieldBegin(ResourceUrlField);
      String resourceUrl_item = resourceUrl.get();
      _oprot.writeString(resourceUrl_item);
      _oprot.writeFieldEnd();
    }
    if (displayUrl.isDefined()) {  _oprot.writeFieldBegin(DisplayUrlField);
      String displayUrl_item = displayUrl.get();
      _oprot.writeString(displayUrl_item);
      _oprot.writeFieldEnd();
    }
    if (title.isDefined()) {  _oprot.writeFieldBegin(TitleField);
      String title_item = title.get();
      _oprot.writeString(title_item);
      _oprot.writeFieldEnd();
    }
    if (description.isDefined()) {  _oprot.writeFieldBegin(DescriptionField);
      String description_item = description.get();
      _oprot.writeString(description_item);
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
      TBidTypeEnum bidType_item = bidType.get();
      _oprot.writeI32(bidType_item.getValue());
      _oprot.writeFieldEnd();
    }
    if (startDate.isDefined()) {  _oprot.writeFieldBegin(StartDateField);
      Long startDate_item = startDate.get();
      _oprot.writeI64(startDate_item);
      _oprot.writeFieldEnd();
    }
    if (endDate.isDefined()) {  _oprot.writeFieldBegin(EndDateField);
      Long endDate_item = endDate.get();
      _oprot.writeI64(endDate_item);
      _oprot.writeFieldEnd();
    }
    if (scheduleDay.isDefined()) {  _oprot.writeFieldBegin(ScheduleDayField);
      Set<TWeekDay> scheduleDay_item = scheduleDay.get();
      _oprot.writeSetBegin(new TSet(TType.I32, scheduleDay_item.size()));
      for (TWeekDay _scheduleDay_item_element : scheduleDay_item) {
        _oprot.writeI32(_scheduleDay_item_element.getValue());
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
    if (scheduleTime.isDefined()) {  _oprot.writeFieldBegin(ScheduleTimeField);
      Set<TScheduleTime> scheduleTime_item = scheduleTime.get();
      _oprot.writeSetBegin(new TSet(TType.STRUCT, scheduleTime_item.size()));
      for (TScheduleTime _scheduleTime_item_element : scheduleTime_item) {
        _scheduleTime_item_element.write(_oprot);
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
    if (locations.isDefined()) {  _oprot.writeFieldBegin(LocationsField);
      Set<String> locations_item = locations.get();
      _oprot.writeSetBegin(new TSet(TType.STRING, locations_item.size()));
      for (String _locations_item_element : locations_item) {
        _oprot.writeString(_locations_item_element);
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
    if (audienceCategories.isDefined()) {  _oprot.writeFieldBegin(AudienceCategoriesField);
      Set<Integer> audienceCategories_item = audienceCategories.get();
      _oprot.writeSetBegin(new TSet(TType.I32, audienceCategories_item.size()));
      for (Integer _audienceCategories_item_element : audienceCategories_item) {
        _oprot.writeI32(_audienceCategories_item_element);
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
    if (destination.isDefined()) {  _oprot.writeFieldBegin(DestinationField);
      String destination_item = destination.get();
      _oprot.writeString(destination_item);
      _oprot.writeFieldEnd();
    }
      _oprot.writeFieldBegin(OrderFrequencyField);
      Integer orderFrequency_item = orderFrequency;
      _oprot.writeI32(orderFrequency_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(EntryFrequencyField);
      Integer entryFrequency_item = entryFrequency;
      _oprot.writeI32(entryFrequency_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(AdsTypeField);
      TAdsTypeEnum adsType_item = adsType;
      _oprot.writeI32(adsType_item.getValue());
      _oprot.writeFieldEnd();
    if (channels.isDefined()) {  _oprot.writeFieldBegin(ChannelsField);
      Set<String> channels_item = channels.get();
      _oprot.writeSetBegin(new TSet(TType.STRING, channels_item.size()));
      for (String _channels_item_element : channels_item) {
        _oprot.writeString(_channels_item_element);
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
      _oprot.writeFieldBegin(ResourceSizeField);
      AdEntrySizeEnum resourceSize_item = resourceSize;
      _oprot.writeI32(resourceSize_item.getValue());
      _oprot.writeFieldEnd();
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  if (this.resourceSize == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'resourceSize' cannot be null");
  }

  public boolean equals(Object other) {
    if (!(other instanceof TAdItem)) return false;
    TAdItem that = (TAdItem) other;
    return
      this.campaignId == that.campaignId &&

      this.orderId == that.orderId &&

      this.entryId == that.entryId &&

      this.advertiserId == that.advertiserId &&

this.status.equals(that.status) &&

      this.bidPrice.equals(that.bidPrice) &&

this.keywords.equals(that.keywords) &&

this.link.equals(that.link) &&

this.resourceType.equals(that.resourceType) &&

this.resourceUrl.equals(that.resourceUrl) &&

this.displayUrl.equals(that.displayUrl) &&

this.title.equals(that.title) &&

this.description.equals(that.description) &&

this.network.equals(that.network) &&

this.bidType.equals(that.bidType) &&

      this.startDate.equals(that.startDate) &&

      this.endDate.equals(that.endDate) &&

this.scheduleDay.equals(that.scheduleDay) &&

this.scheduleTime.equals(that.scheduleTime) &&

this.locations.equals(that.locations) &&

this.audienceCategories.equals(that.audienceCategories) &&

this.destination.equals(that.destination) &&

      this.orderFrequency == that.orderFrequency &&

      this.entryFrequency == that.entryFrequency &&

this.adsType.equals(that.adsType) &&

this.channels.equals(that.channels) &&

this.resourceSize.equals(that.resourceSize)
;
  }

  public String toString() {
    return "TAdItem(" + this.campaignId + "," + this.orderId + "," + this.entryId + "," + this.advertiserId + "," + this.status + "," + this.bidPrice + "," + this.keywords + "," + this.link + "," + this.resourceType + "," + this.resourceUrl + "," + this.displayUrl + "," + this.title + "," + this.description + "," + this.network + "," + this.bidType + "," + this.startDate + "," + this.endDate + "," + this.scheduleDay + "," + this.scheduleTime + "," + this.locations + "," + this.audienceCategories + "," + this.destination + "," + this.orderFrequency + "," + this.entryFrequency + "," + this.adsType + "," + this.channels + "," + this.resourceSize + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * new Integer(this.campaignId).hashCode();
    hash = hash * new Integer(this.orderId).hashCode();
    hash = hash * new Integer(this.entryId).hashCode();
    hash = hash * new Integer(this.advertiserId).hashCode();
    hash = hash * (this.status == null ? 0 : this.status.hashCode());
    hash = hash * (this.bidPrice.isDefined() ? 0 : new Integer(this.bidPrice.get()).hashCode());
    hash = hash * (this.keywords.isDefined() ? 0 : this.keywords.get().hashCode());
    hash = hash * (this.link.isDefined() ? 0 : this.link.get().hashCode());
    hash = hash * (this.resourceType.isDefined() ? 0 : this.resourceType.get().hashCode());
    hash = hash * (this.resourceUrl.isDefined() ? 0 : this.resourceUrl.get().hashCode());
    hash = hash * (this.displayUrl.isDefined() ? 0 : this.displayUrl.get().hashCode());
    hash = hash * (this.title.isDefined() ? 0 : this.title.get().hashCode());
    hash = hash * (this.description.isDefined() ? 0 : this.description.get().hashCode());
    hash = hash * (this.network.isDefined() ? 0 : this.network.get().hashCode());
    hash = hash * (this.bidType.isDefined() ? 0 : this.bidType.get().hashCode());
    hash = hash * (this.startDate.isDefined() ? 0 : new Long(this.startDate.get()).hashCode());
    hash = hash * (this.endDate.isDefined() ? 0 : new Long(this.endDate.get()).hashCode());
    hash = hash * (this.scheduleDay.isDefined() ? 0 : this.scheduleDay.get().hashCode());
    hash = hash * (this.scheduleTime.isDefined() ? 0 : this.scheduleTime.get().hashCode());
    hash = hash * (this.locations.isDefined() ? 0 : this.locations.get().hashCode());
    hash = hash * (this.audienceCategories.isDefined() ? 0 : this.audienceCategories.get().hashCode());
    hash = hash * (this.destination.isDefined() ? 0 : this.destination.get().hashCode());
    hash = hash * new Integer(this.orderFrequency).hashCode();
    hash = hash * new Integer(this.entryFrequency).hashCode();
    hash = hash * (this.adsType == null ? 0 : this.adsType.hashCode());
    hash = hash * (this.channels.isDefined() ? 0 : this.channels.get().hashCode());
    hash = hash * (this.resourceSize == null ? 0 : this.resourceSize.hashCode());
    return hash;
  }
}