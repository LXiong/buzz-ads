package com.buzzinate.buzzads.thrift;

import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;

import com.twitter.scrooge.ScroogeOption;
import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class AdParam implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("AdParam");
  private static final TField UrlField = new TField("url", TType.STRING, (short) 1);
  final String url;
  private static final TField TitleField = new TField("title", TType.STRING, (short) 2);
  final ScroogeOption<String> title;
  private static final TField KeywordsField = new TField("keywords", TType.STRING, (short) 3);
  final ScroogeOption<String> keywords;
  private static final TField ResourceTypeField = new TField("resourceType", TType.I32, (short) 4);
  final AdEntryTypeEnum resourceType;
  private static final TField CountField = new TField("count", TType.I32, (short) 5);
  final int count;
  private static final TField UseridField = new TField("userid", TType.STRING, (short) 6);
  final String userid;
  private static final TField IpField = new TField("ip", TType.STRING, (short) 7);
  final ScroogeOption<String> ip;
  private static final TField NetWorkField = new TField("netWork", TType.I32, (short) 8);
  final AdNetworkEnum netWork;
  private static final TField UuidField = new TField("uuid", TType.STRING, (short) 9);
  final ScroogeOption<String> uuid;
  private static final TField ResourceSizeField = new TField("resourceSize", TType.I32, (short) 10);
  final AdEntrySizeEnum resourceSize;

  public static class Builder {
    private String _url = null;
    private Boolean _got_url = false;

    public Builder url(String value) {
      this._url = value;
      this._got_url = true;
      return this;
    }
    private String _title = null;
    private Boolean _got_title = false;

    public Builder title(String value) {
      this._title = value;
      this._got_title = true;
      return this;
    }
    private String _keywords = null;
    private Boolean _got_keywords = false;

    public Builder keywords(String value) {
      this._keywords = value;
      this._got_keywords = true;
      return this;
    }
    private AdEntryTypeEnum _resourceType = AdEntryTypeEnum.IMAGE;
    private Boolean _got_resourceType = false;

    public Builder resourceType(AdEntryTypeEnum value) {
      this._resourceType = value;
      this._got_resourceType = true;
      return this;
    }
    private int _count = 1;
    private Boolean _got_count = false;

    public Builder count(int value) {
      this._count = value;
      this._got_count = true;
      return this;
    }
    private String _userid = "1";
    private Boolean _got_userid = false;

    public Builder userid(String value) {
      this._userid = value;
      this._got_userid = true;
      return this;
    }
    private String _ip = null;
    private Boolean _got_ip = false;

    public Builder ip(String value) {
      this._ip = value;
      this._got_ip = true;
      return this;
    }
    private AdNetworkEnum _netWork = AdNetworkEnum.LEZHI;
    private Boolean _got_netWork = false;

    public Builder netWork(AdNetworkEnum value) {
      this._netWork = value;
      this._got_netWork = true;
      return this;
    }
    private String _uuid = null;
    private Boolean _got_uuid = false;

    public Builder uuid(String value) {
      this._uuid = value;
      this._got_uuid = true;
      return this;
    }
    private AdEntrySizeEnum _resourceSize = AdEntrySizeEnum.SIZE80X80;
    private Boolean _got_resourceSize = false;

    public Builder resourceSize(AdEntrySizeEnum value) {
      this._resourceSize = value;
      this._got_resourceSize = true;
      return this;
    }

    public AdParam build() {
      if (!_got_url)
      throw new IllegalStateException("Required field 'url' was not found for struct AdParam");
      if (!_got_resourceType)
      throw new IllegalStateException("Required field 'resourceType' was not found for struct AdParam");
      if (!_got_netWork)
      throw new IllegalStateException("Required field 'netWork' was not found for struct AdParam");
      return new AdParam(
        this._url,
      ScroogeOption.make(this._got_title, this._title),
      ScroogeOption.make(this._got_keywords, this._keywords),
        this._resourceType,
        this._count,
        this._userid,
      ScroogeOption.make(this._got_ip, this._ip),
        this._netWork,
      ScroogeOption.make(this._got_uuid, this._uuid),
        this._resourceSize    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.url(this.url);
    if (this.title.isDefined()) builder.title(this.title.get());
    if (this.keywords.isDefined()) builder.keywords(this.keywords.get());
    builder.resourceType(this.resourceType);
    builder.count(this.count);
    builder.userid(this.userid);
    if (this.ip.isDefined()) builder.ip(this.ip.get());
    builder.netWork(this.netWork);
    if (this.uuid.isDefined()) builder.uuid(this.uuid.get());
    builder.resourceSize(this.resourceSize);
    return builder;
  }

  public static ThriftStructCodec<AdParam> CODEC = new ThriftStructCodec<AdParam>() {
    public AdParam decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      String url = null;
      String title = null;
      String keywords = null;
      AdEntryTypeEnum resourceType = AdEntryTypeEnum.IMAGE;
      int count = 1;
      String userid = "1";
      String ip = null;
      AdNetworkEnum netWork = AdNetworkEnum.LEZHI;
      String uuid = null;
      AdEntrySizeEnum resourceSize = AdEntrySizeEnum.SIZE80X80;
      Boolean _done = false;
      _iprot.readStructBegin();
      while (!_done) {
        TField _field = _iprot.readFieldBegin();
        if (_field.type == TType.STOP) {
          _done = true;
        } else {
          switch (_field.id) {
            case 1: /* url */
              switch (_field.type) {
                case TType.STRING:
                  String url_item;
                  url_item = _iprot.readString();
                  url = url_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.url(url);
              break;
            case 2: /* title */
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
            case 3: /* keywords */
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
            case 4: /* resourceType */
              switch (_field.type) {
                case TType.I32:
                  AdEntryTypeEnum resourceType_item;
                  resourceType_item = AdEntryTypeEnum.findByValue(_iprot.readI32());
                  resourceType = resourceType_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.resourceType(resourceType);
              break;
            case 5: /* count */
              switch (_field.type) {
                case TType.I32:
                  Integer count_item;
                  count_item = _iprot.readI32();
                  count = count_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.count(count);
              break;
            case 6: /* userid */
              switch (_field.type) {
                case TType.STRING:
                  String userid_item;
                  userid_item = _iprot.readString();
                  userid = userid_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.userid(userid);
              break;
            case 7: /* ip */
              switch (_field.type) {
                case TType.STRING:
                  String ip_item;
                  ip_item = _iprot.readString();
                  ip = ip_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.ip(ip);
              break;
            case 8: /* netWork */
              switch (_field.type) {
                case TType.I32:
                  AdNetworkEnum netWork_item;
                  netWork_item = AdNetworkEnum.findByValue(_iprot.readI32());
                  netWork = netWork_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.netWork(netWork);
              break;
            case 9: /* uuid */
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
            case 10: /* resourceSize */
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

    public void encode(AdParam struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static AdParam decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(AdParam struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public AdParam(
  String url,
  ScroogeOption<String> title,
  ScroogeOption<String> keywords,
  AdEntryTypeEnum resourceType,
  int count,
  String userid,
  ScroogeOption<String> ip,
  AdNetworkEnum netWork,
  ScroogeOption<String> uuid,
  AdEntrySizeEnum resourceSize
  ) {
    this.url = url;
    this.title = title;
    this.keywords = keywords;
    this.resourceType = resourceType;
    this.count = count;
    this.userid = userid;
    this.ip = ip;
    this.netWork = netWork;
    this.uuid = uuid;
    this.resourceSize = resourceSize;
  }

  public String getUrl() {
    return this.url;
  }
  public String getTitle() {
    return this.title.get();
  }
  public String getKeywords() {
    return this.keywords.get();
  }
  public AdEntryTypeEnum getResourceType() {
    return this.resourceType;
  }
  public int getCount() {
    return this.count;
  }
  public String getUserid() {
    return this.userid;
  }
  public String getIp() {
    return this.ip.get();
  }
  public AdNetworkEnum getNetWork() {
    return this.netWork;
  }
  public String getUuid() {
    return this.uuid.get();
  }
  public AdEntrySizeEnum getResourceSize() {
    return this.resourceSize;
  }
  
  public ScroogeOption getTitleOption() {
      return this.title;
  }
  public ScroogeOption getKeywordsOption() {
      return this.keywords;
  }
  public ScroogeOption getIpOption() {
      return this.ip;
  }
  public ScroogeOption getUuidOption() {
      return uuid;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(UrlField);
      String url_item = url;
      _oprot.writeString(url_item);
      _oprot.writeFieldEnd();
    if (title.isDefined()) {  _oprot.writeFieldBegin(TitleField);
      String title_item = title.get();
      _oprot.writeString(title_item);
      _oprot.writeFieldEnd();
    }
    if (keywords.isDefined()) {  _oprot.writeFieldBegin(KeywordsField);
      String keywords_item = keywords.get();
      _oprot.writeString(keywords_item);
      _oprot.writeFieldEnd();
    }
      _oprot.writeFieldBegin(ResourceTypeField);
      AdEntryTypeEnum resourceType_item = resourceType;
      _oprot.writeI32(resourceType_item.getValue());
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(CountField);
      Integer count_item = count;
      _oprot.writeI32(count_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(UseridField);
      String userid_item = userid;
      _oprot.writeString(userid_item);
      _oprot.writeFieldEnd();
    if (ip.isDefined()) {  _oprot.writeFieldBegin(IpField);
      String ip_item = ip.get();
      _oprot.writeString(ip_item);
      _oprot.writeFieldEnd();
    }
      _oprot.writeFieldBegin(NetWorkField);
      AdNetworkEnum netWork_item = netWork;
      _oprot.writeI32(netWork_item.getValue());
      _oprot.writeFieldEnd();
    if (uuid.isDefined()) {  _oprot.writeFieldBegin(UuidField);
      String uuid_item = uuid.get();
      _oprot.writeString(uuid_item);
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
  if (this.url == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'url' cannot be null");
  if (this.resourceType == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'resourceType' cannot be null");
  if (this.netWork == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'netWork' cannot be null");
  }

  public boolean equals(Object other) {
    if (!(other instanceof AdParam)) return false;
    AdParam that = (AdParam) other;
    return
this.url.equals(that.url) &&
this.title.equals(that.title) &&
this.keywords.equals(that.keywords) &&
this.resourceType.equals(that.resourceType) &&
      this.count == that.count
 &&
this.userid.equals(that.userid) &&
this.ip.equals(that.ip) &&
this.netWork.equals(that.netWork) &&
this.uuid.equals(that.uuid) &&
this.resourceSize.equals(that.resourceSize);
  }

  public String toString() {
    return "AdParam(" + this.url + "," + this.title + "," + this.keywords + "," + this.resourceType + "," + this.count + "," + this.userid + "," + this.ip + "," + this.netWork + "," + this.uuid + "," + this.resourceSize + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * (this.url == null ? 0 : this.url.hashCode());
    hash = hash * (this.title.isDefined() ? 0 : this.title.get().hashCode());
    hash = hash * (this.keywords.isDefined() ? 0 : this.keywords.get().hashCode());
    hash = hash * (this.resourceType == null ? 0 : this.resourceType.hashCode());
    hash = hash * new Integer(this.count).hashCode();
    hash = hash * (this.userid == null ? 0 : this.userid.hashCode());
    hash = hash * (this.ip.isDefined() ? 0 : this.ip.get().hashCode());
    hash = hash * (this.netWork == null ? 0 : this.netWork.hashCode());
    hash = hash * (this.uuid.isDefined() ? 0 : this.uuid.get().hashCode());
    hash = hash * (this.resourceSize == null ? 0 : this.resourceSize.hashCode());
    return hash;
  }
}