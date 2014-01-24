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
public class MissingRequiredFieldException extends Exception implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("MissingRequiredFieldException");

  public static class Builder {

    public MissingRequiredFieldException build() {
      return new MissingRequiredFieldException(
    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    return builder;
  }

  public static ThriftStructCodec<MissingRequiredFieldException> CODEC = new ThriftStructCodec<MissingRequiredFieldException>() {
    public MissingRequiredFieldException decode(TProtocol _iprot) throws org.apache.thrift.TException {
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

    public void encode(MissingRequiredFieldException struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static MissingRequiredFieldException decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(MissingRequiredFieldException struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public MissingRequiredFieldException(
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
    return "MissingRequiredFieldException()";
  }

  public int hashCode() {
    return super.hashCode();
  }
}