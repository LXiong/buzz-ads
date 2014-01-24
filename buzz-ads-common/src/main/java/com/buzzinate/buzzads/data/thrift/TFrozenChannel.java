package com.buzzinate.buzzads.data.thrift;

import com.twitter.scrooge.ScroogeOption;
import com.twitter.scrooge.Utilities;
import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;
import com.twitter.util.Function2;
import org.apache.thrift.protocol.*;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class TFrozenChannel implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TFrozenChannel");
  private static final TField UuidField = new TField("uuid", TType.STRING, (short) 1);
  final String uuid;
  private static final TField ChannelLevelField = new TField("channelLevel", TType.I32, (short) 2);
  final int channelLevel;
  private static final TField DomainField = new TField("domain", TType.STRING, (short) 3);
  final String domain;

  public static class Builder {
    private String _uuid = null;
    private Boolean _got_uuid = false;

    public Builder uuid(String value) {
      this._uuid = value;
      this._got_uuid = true;
      return this;
    }
    private int _channelLevel = 0;
    private Boolean _got_channelLevel = false;

    public Builder channelLevel(int value) {
      this._channelLevel = value;
      this._got_channelLevel = true;
      return this;
    }
    private String _domain = null;
    private Boolean _got_domain = false;

    public Builder domain(String value) {
      this._domain = value;
      this._got_domain = true;
      return this;
    }

    public TFrozenChannel build() {
      if (!_got_uuid)
      throw new IllegalStateException("Required field 'uuid' was not found for struct TFrozenChannel");
      if (!_got_channelLevel)
      throw new IllegalStateException("Required field 'channelLevel' was not found for struct TFrozenChannel");
      if (!_got_domain)
      throw new IllegalStateException("Required field 'domain' was not found for struct TFrozenChannel");
      return new TFrozenChannel(
        this._uuid,
        this._channelLevel,
        this._domain    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.uuid(this.uuid);
    builder.channelLevel(this.channelLevel);
    builder.domain(this.domain);
    return builder;
  }

  public static ThriftStructCodec<TFrozenChannel> CODEC = new ThriftStructCodec<TFrozenChannel>() {
    public TFrozenChannel decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      String uuid = null;
      int channelLevel = 0;
      String domain = null;
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
            case 2: /* channelLevel */
              switch (_field.type) {
                case TType.I32:
                  Integer channelLevel_item;
                  channelLevel_item = _iprot.readI32();
                  channelLevel = channelLevel_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.channelLevel(channelLevel);
              break;
            case 3: /* domain */
              switch (_field.type) {
                case TType.STRING:
                  String domain_item;
                  domain_item = _iprot.readString();
                  domain = domain_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.domain(domain);
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

    public void encode(TFrozenChannel struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TFrozenChannel decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TFrozenChannel struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TFrozenChannel(
  String uuid,
  int channelLevel,
  String domain
  ) {
    this.uuid = uuid;
    this.channelLevel = channelLevel;
    this.domain = domain;
  }

  public String getUuid() {
    return this.uuid;
  }
  public int getChannelLevel() {
    return this.channelLevel;
  }
  public String getDomain() {
    return this.domain;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(UuidField);
      String uuid_item = uuid;
      _oprot.writeString(uuid_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(ChannelLevelField);
      Integer channelLevel_item = channelLevel;
      _oprot.writeI32(channelLevel_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(DomainField);
      String domain_item = domain;
      _oprot.writeString(domain_item);
      _oprot.writeFieldEnd();
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  if (this.uuid == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'uuid' cannot be null");
  if (this.domain == null)
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'domain' cannot be null");
  }

  public boolean equals(Object other) {
    if (!(other instanceof TFrozenChannel)) return false;
    TFrozenChannel that = (TFrozenChannel) other;
    return
this.uuid.equals(that.uuid) &&
      this.channelLevel == that.channelLevel
 &&
this.domain.equals(that.domain);
  }

  public String toString() {
    return "TFrozenChannel(" + this.uuid + "," + this.channelLevel + "," + this.domain + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * (this.uuid == null ? 0 : this.uuid.hashCode());
    hash = hash * new Integer(this.channelLevel).hashCode();
    hash = hash * (this.domain == null ? 0 : this.domain.hashCode());
    return hash;
  }
}