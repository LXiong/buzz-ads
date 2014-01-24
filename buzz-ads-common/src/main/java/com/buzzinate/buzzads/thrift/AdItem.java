package com.buzzinate.buzzads.thrift;

import com.twitter.scrooge.ScroogeOption;
import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TStruct;
import org.apache.thrift.protocol.TType;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")

public class AdItem implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("AdItem");
  private static final TField UrlField = new TField("url", TType.STRING, (short) 1);
  final String url;
  private static final TField TitleField = new TField("title", TType.STRING, (short) 2);
  final String title;
  private static final TField PicField = new TField("pic", TType.STRING, (short) 3);
  final ScroogeOption<String> pic;
  private static final TField AdEntryIdField = new TField("adEntryId", TType.I32, (short) 4);
  final ScroogeOption<Integer> adEntryId;
  private static final TField DisplayUrlField = new TField("displayUrl", TType.STRING, (short) 5);
  final ScroogeOption<String> displayUrl;
  private static final TField DescriptionField = new TField("description", TType.STRING, (short) 6);
  final ScroogeOption<String> description;

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
    private String _pic = null;
    private Boolean _got_pic = false;

    public Builder pic(String value) {
      this._pic = value;
      this._got_pic = true;
      return this;
    }
    private int _adEntryId = 0;
    private Boolean _got_adEntryId = false;

    public Builder adEntryId(int value) {
      this._adEntryId = value;
      this._got_adEntryId = true;
      return this;
    }
    private String _displayUrl = null;
    private Boolean _got_displayUrl = false;

    public Builder displayUrl(String value) {
      this._displayUrl = value;
      this._got_displayUrl = true;
      return this;
    }
    private String _description = null;
    private Boolean _got_description = false;

    public Builder description(String value) {
      this._description = value;
      this._got_description = true;
      return this;
    }

    public AdItem build() {
      if (!_got_url)
      throw new IllegalStateException("Required field 'url' was not found for struct AdItem");
      if (!_got_title)
      throw new IllegalStateException("Required field 'title' was not found for struct AdItem");
      return new AdItem(
        this._url,
        this._title,
      ScroogeOption.make(this._got_pic, this._pic),
      ScroogeOption.make(this._got_adEntryId, this._adEntryId),
      ScroogeOption.make(this._got_displayUrl, this._displayUrl),
      ScroogeOption.make(this._got_description, this._description)    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.url(this.url);
    builder.title(this.title);
    if (this.pic.isDefined()) builder.pic(this.pic.get());
    if (this.adEntryId.isDefined()) builder.adEntryId(this.adEntryId.get());
    if (this.displayUrl.isDefined()) builder.displayUrl(this.displayUrl.get());
    if (this.description.isDefined()) builder.description(this.description.get());
    return builder;
  }

  public static ThriftStructCodec<AdItem> CODEC = new ThriftStructCodec<AdItem>() {
    public AdItem decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      String url = null;
      String title = null;
      String pic = null;
      int adEntryId = 0;
      String displayUrl = null;
      String description = null;
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
            case 3: /* pic */
              switch (_field.type) {
                case TType.STRING:
                  String pic_item;
                  pic_item = _iprot.readString();
                  pic = pic_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.pic(pic);
              break;
            case 4: /* adEntryId */
              switch (_field.type) {
                case TType.I32:
                  Integer adEntryId_item;
                  adEntryId_item = _iprot.readI32();
                  adEntryId = adEntryId_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.adEntryId(adEntryId);
              break;
            case 5: /* displayUrl */
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
            case 6: /* description */
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

    public void encode(AdItem struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static AdItem decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(AdItem struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public AdItem(
  String url,
  String title,
  ScroogeOption<String> pic,
  ScroogeOption<Integer> adEntryId,
  ScroogeOption<String> displayUrl,
  ScroogeOption<String> description
  ) {
    this.url = url;
    this.title = title;
    this.pic = pic;
    this.adEntryId = adEntryId;
    this.displayUrl = displayUrl;
    this.description = description;
  }

  public String getUrl() {
    return this.url;
  }
  public String getTitle() {
    return this.title;
  }
  public String getPic() {
    return this.pic.get();
  }
  public ScroogeOption getPicOption() {
      return this.pic;
  }
  public int getAdEntryId() {
    return this.adEntryId.get();
  }
  public ScroogeOption getAdEntryIdOption() {
      return this.adEntryId;
  }
  public String getDisplayUrl() {
    return this.displayUrl.get();
  }
  public ScroogeOption getDisplayUrlOption() {
      return this.displayUrl;
  }
  public String getDescription() {
    return this.description.get();
  }
  public ScroogeOption getDescriptionOption() {
      return this.description;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(UrlField);
      String url_item = url;
      _oprot.writeString(url_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(TitleField);
      String title_item = title;
      _oprot.writeString(title_item);
      _oprot.writeFieldEnd();
    if (pic.isDefined()) {  _oprot.writeFieldBegin(PicField);
      String pic_item = pic.get();
      _oprot.writeString(pic_item);
      _oprot.writeFieldEnd();
    }
    if (adEntryId.isDefined()) {  _oprot.writeFieldBegin(AdEntryIdField);
      Integer adEntryId_item = adEntryId.get();
      _oprot.writeI32(adEntryId_item);
      _oprot.writeFieldEnd();
    }
    if (displayUrl.isDefined()) {  _oprot.writeFieldBegin(DisplayUrlField);
      String displayUrl_item = displayUrl.get();
      _oprot.writeString(displayUrl_item);
      _oprot.writeFieldEnd();
    }
    if (description.isDefined()) {  _oprot.writeFieldBegin(DescriptionField);
      String description_item = description.get();
      _oprot.writeString(description_item);
      _oprot.writeFieldEnd();
    }
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  if (this.url == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'url' cannot be null");
  if (this.title == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'title' cannot be null");
  }

  public boolean equals(Object other) {
    if (!(other instanceof AdItem)) return false;
    AdItem that = (AdItem) other;
    return
this.url.equals(that.url) &&
this.title.equals(that.title) &&
this.pic.equals(that.pic) &&
      this.adEntryId.equals(that.adEntryId)
 &&
this.displayUrl.equals(that.displayUrl) &&
this.description.equals(that.description);
  }

  public String toString() {
    return "AdItem(" + this.url + "," + this.title + "," + this.pic + "," + this.adEntryId + "," + this.displayUrl + "," + this.description + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * (this.url == null ? 0 : this.url.hashCode());
    hash = hash * (this.title == null ? 0 : this.title.hashCode());
    hash = hash * (this.pic.isDefined() ? 0 : this.pic.get().hashCode());
    hash = hash * (this.adEntryId.isDefined() ? 0 : new Integer(this.adEntryId.get()).hashCode());
    hash = hash * (this.displayUrl.isDefined() ? 0 : this.displayUrl.get().hashCode());
    hash = hash * (this.description.isDefined() ? 0 : this.description.get().hashCode());
    return hash;
  }
}