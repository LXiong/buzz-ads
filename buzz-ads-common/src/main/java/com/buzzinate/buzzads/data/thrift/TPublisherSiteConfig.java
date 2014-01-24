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
public class TPublisherSiteConfig implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TPublisherSiteConfig");
  private static final TField UuidField = new TField("uuid", TType.STRING, (short) 1);
  final String uuid;
  private static final TField BlackKeywordsField = new TField("blackKeywords", TType.STRING, (short) 2);
  final ScroogeOption<String> blackKeywords;
  private static final TField BlackDomainsField = new TField("blackDomains", TType.SET, (short) 3);
  final ScroogeOption<Set<String>> blackDomains;
  private static final TField BlackEntryLinksField = new TField("blackEntryLinks", TType.SET, (short) 4);
  final ScroogeOption<Set<String>> blackEntryLinks;

  public static class Builder {
    private String _uuid = null;
    private Boolean _got_uuid = false;

    public Builder uuid(String value) {
      this._uuid = value;
      this._got_uuid = true;
      return this;
    }
    private String _blackKeywords = null;
    private Boolean _got_blackKeywords = false;

    public Builder blackKeywords(String value) {
      this._blackKeywords = value;
      this._got_blackKeywords = true;
      return this;
    }
    private Set<String> _blackDomains = Utilities.makeSet();
    private Boolean _got_blackDomains = false;

    public Builder blackDomains(Set<String> value) {
      this._blackDomains = value;
      this._got_blackDomains = true;
      return this;
    }
    private Set<String> _blackEntryLinks = Utilities.makeSet();
    private Boolean _got_blackEntryLinks = false;

    public Builder blackEntryLinks(Set<String> value) {
      this._blackEntryLinks = value;
      this._got_blackEntryLinks = true;
      return this;
    }

    public TPublisherSiteConfig build() {
      return new TPublisherSiteConfig(
        this._uuid,
      ScroogeOption.make(this._got_blackKeywords, this._blackKeywords),
      ScroogeOption.make(this._got_blackDomains, this._blackDomains),
      ScroogeOption.make(this._got_blackEntryLinks, this._blackEntryLinks)    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.uuid(this.uuid);
    if (this.blackKeywords.isDefined()) builder.blackKeywords(this.blackKeywords.get());
    if (this.blackDomains.isDefined()) builder.blackDomains(this.blackDomains.get());
    if (this.blackEntryLinks.isDefined()) builder.blackEntryLinks(this.blackEntryLinks.get());
    return builder;
  }

  public static ThriftStructCodec<TPublisherSiteConfig> CODEC = new ThriftStructCodec<TPublisherSiteConfig>() {
    public TPublisherSiteConfig decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      String uuid = null;
      String blackKeywords = null;
      Set<String> blackDomains = Utilities.makeSet();
      Set<String> blackEntryLinks = Utilities.makeSet();
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
            case 2: /* blackKeywords */
              switch (_field.type) {
                case TType.STRING:
                  String blackKeywords_item;
                  blackKeywords_item = _iprot.readString();
                  blackKeywords = blackKeywords_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.blackKeywords(blackKeywords);
              break;
            case 3: /* blackDomains */
              switch (_field.type) {
                case TType.SET:
                  Set<String> blackDomains_item;
                  TSet _set_blackDomains_item = _iprot.readSetBegin();
                  blackDomains_item = new HashSet<String>();
                  int _i_blackDomains_item = 0;
                  String _blackDomains_item_element;
                  while (_i_blackDomains_item < _set_blackDomains_item.size) {
                    _blackDomains_item_element = _iprot.readString();
                    blackDomains_item.add(_blackDomains_item_element);
                    _i_blackDomains_item += 1;
                  }
                  _iprot.readSetEnd();
                  blackDomains = blackDomains_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.blackDomains(blackDomains);
              break;
            case 4: /* blackEntryLinks */
              switch (_field.type) {
                case TType.SET:
                  Set<String> blackEntryLinks_item;
                  TSet _set_blackEntryLinks_item = _iprot.readSetBegin();
                  blackEntryLinks_item = new HashSet<String>();
                  int _i_blackEntryLinks_item = 0;
                  String _blackEntryLinks_item_element;
                  while (_i_blackEntryLinks_item < _set_blackEntryLinks_item.size) {
                    _blackEntryLinks_item_element = _iprot.readString();
                    blackEntryLinks_item.add(_blackEntryLinks_item_element);
                    _i_blackEntryLinks_item += 1;
                  }
                  _iprot.readSetEnd();
                  blackEntryLinks = blackEntryLinks_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.blackEntryLinks(blackEntryLinks);
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

    public void encode(TPublisherSiteConfig struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TPublisherSiteConfig decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TPublisherSiteConfig struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TPublisherSiteConfig(
  String uuid,
  ScroogeOption<String> blackKeywords,
  ScroogeOption<Set<String>> blackDomains,
  ScroogeOption<Set<String>> blackEntryLinks
  ) {
    this.uuid = uuid;
    this.blackKeywords = blackKeywords;
    this.blackDomains = blackDomains;
    this.blackEntryLinks = blackEntryLinks;
  }

  public String getUuid() {
    return this.uuid;
  }
  public String getBlackKeywords() {
    return this.blackKeywords.get();
  }
  public Set<String> getBlackDomains() {
    return this.blackDomains.get();
  }
  public Set<String> getBlackEntryLinks() {
    return this.blackEntryLinks.get();
  }
  
  public ScroogeOption getBlackKeywordsOption() {
      return this.blackKeywords;
  }
  public ScroogeOption getBlackDomainsOption() {
      return this.blackDomains;
  }
  public ScroogeOption getBlackEntryLinksOption() {
      return this.blackEntryLinks;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(UuidField);
      String uuid_item = uuid;
      _oprot.writeString(uuid_item);
      _oprot.writeFieldEnd();
    if (blackKeywords.isDefined()) {  _oprot.writeFieldBegin(BlackKeywordsField);
      String blackKeywords_item = blackKeywords.get();
      _oprot.writeString(blackKeywords_item);
      _oprot.writeFieldEnd();
    }
    if (blackDomains.isDefined()) {  _oprot.writeFieldBegin(BlackDomainsField);
      Set<String> blackDomains_item = blackDomains.get();
      _oprot.writeSetBegin(new TSet(TType.STRING, blackDomains_item.size()));
      for (String _blackDomains_item_element : blackDomains_item) {
        _oprot.writeString(_blackDomains_item_element);
      }
      _oprot.writeSetEnd();
      _oprot.writeFieldEnd();
    }
    if (blackEntryLinks.isDefined()) {  _oprot.writeFieldBegin(BlackEntryLinksField);
      Set<String> blackEntryLinks_item = blackEntryLinks.get();
      _oprot.writeSetBegin(new TSet(TType.STRING, blackEntryLinks_item.size()));
      for (String _blackEntryLinks_item_element : blackEntryLinks_item) {
        _oprot.writeString(_blackEntryLinks_item_element);
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
    if (!(other instanceof TPublisherSiteConfig)) return false;
    TPublisherSiteConfig that = (TPublisherSiteConfig) other;
    return
this.uuid.equals(that.uuid) &&
this.blackKeywords.equals(that.blackKeywords) &&
this.blackDomains.equals(that.blackDomains) &&
this.blackEntryLinks.equals(that.blackEntryLinks);
  }

  public String toString() {
    return "TPublisherSiteConfig(" + this.uuid + "," + this.blackKeywords + "," + this.blackDomains + "," + this.blackEntryLinks + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * (this.uuid == null ? 0 : this.uuid.hashCode());
    hash = hash * (this.blackKeywords.isDefined() ? 0 : this.blackKeywords.get().hashCode());
    hash = hash * (this.blackDomains.isDefined() ? 0 : this.blackDomains.get().hashCode());
    hash = hash * (this.blackEntryLinks.isDefined() ? 0 : this.blackEntryLinks.get().hashCode());
    return hash;
  }
}