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
public class PublisherContactNotFoundException extends Exception implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("PublisherContactNotFoundException");

  public static class Builder {

    public PublisherContactNotFoundException build() {
      return new PublisherContactNotFoundException(
    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    return builder;
  }

  public static ThriftStructCodec<PublisherContactNotFoundException> CODEC = new ThriftStructCodec<PublisherContactNotFoundException>() {
    public PublisherContactNotFoundException decode(TProtocol _iprot) throws org.apache.thrift.TException {
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

    public void encode(PublisherContactNotFoundException struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static PublisherContactNotFoundException decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(PublisherContactNotFoundException struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public PublisherContactNotFoundException(
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
    return "PublisherContactNotFoundException()";
  }

  public int hashCode() {
    return super.hashCode();
  }
}