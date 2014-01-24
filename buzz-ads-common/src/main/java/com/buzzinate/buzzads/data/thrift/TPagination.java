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
public class TPagination implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TPagination");
  private static final TField StartField = new TField("start", TType.I16, (short) 1);
  final short start;
  private static final TField CountField = new TField("count", TType.I16, (short) 2);
  final short count;

  public static class Builder {
    private short _start = 0;
    private Boolean _got_start = false;

    public Builder start(short value) {
      this._start = value;
      this._got_start = true;
      return this;
    }
    private short _count = 50;
    private Boolean _got_count = false;

    public Builder count(short value) {
      this._count = value;
      this._got_count = true;
      return this;
    }

    public TPagination build() {
      return new TPagination(
        this._start,
        this._count    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.start(this.start);
    builder.count(this.count);
    return builder;
  }

  public static ThriftStructCodec<TPagination> CODEC = new ThriftStructCodec<TPagination>() {
    public TPagination decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      short start = 0;
      short count = 50;
      Boolean _done = false;
      _iprot.readStructBegin();
      while (!_done) {
        TField _field = _iprot.readFieldBegin();
        if (_field.type == TType.STOP) {
          _done = true;
        } else {
          switch (_field.id) {
            case 1: /* start */
              switch (_field.type) {
                case TType.I16:
                  Short start_item;
                  start_item = _iprot.readI16();
                  start = start_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.start(start);
              break;
            case 2: /* count */
              switch (_field.type) {
                case TType.I16:
                  Short count_item;
                  count_item = _iprot.readI16();
                  count = count_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.count(count);
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

    public void encode(TPagination struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TPagination decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TPagination struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TPagination(
  short start,
  short count
  ) {
    this.start = start;
    this.count = count;
  }

  public short getStart() {
    return this.start;
  }
  public short getCount() {
    return this.count;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(StartField);
      Short start_item = start;
      _oprot.writeI16(start_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(CountField);
      Short count_item = count;
      _oprot.writeI16(count_item);
      _oprot.writeFieldEnd();
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  }

  public boolean equals(Object other) {
    if (!(other instanceof TPagination)) return false;
    TPagination that = (TPagination) other;
    return
      this.start == that.start &&

      this.count == that.count
;
  }

  public String toString() {
    return "TPagination(" + this.start + "," + this.count + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * new Short(this.start).hashCode();
    hash = hash * new Short(this.count).hashCode();
    return hash;
  }
}