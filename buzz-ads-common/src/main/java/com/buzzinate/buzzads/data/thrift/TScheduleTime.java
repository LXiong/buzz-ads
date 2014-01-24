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
public class TScheduleTime implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TScheduleTime");
  private static final TField StartField = new TField("start", TType.I64, (short) 1);
  final long start;
  private static final TField EndField = new TField("end", TType.I64, (short) 2);
  final long end;

  public static class Builder {
    private long _start = (long) 0;
    private Boolean _got_start = false;

    public Builder start(long value) {
      this._start = value;
      this._got_start = true;
      return this;
    }
    private long _end = (long) 0;
    private Boolean _got_end = false;

    public Builder end(long value) {
      this._end = value;
      this._got_end = true;
      return this;
    }

    public TScheduleTime build() {
      return new TScheduleTime(
        this._start,
        this._end    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.start(this.start);
    builder.end(this.end);
    return builder;
  }

  public static ThriftStructCodec<TScheduleTime> CODEC = new ThriftStructCodec<TScheduleTime>() {
    public TScheduleTime decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      long start = (long) 0;
      long end = (long) 0;
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
                case TType.I64:
                  Long start_item;
                  start_item = _iprot.readI64();
                  start = start_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.start(start);
              break;
            case 2: /* end */
              switch (_field.type) {
                case TType.I64:
                  Long end_item;
                  end_item = _iprot.readI64();
                  end = end_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.end(end);
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

    public void encode(TScheduleTime struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TScheduleTime decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TScheduleTime struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TScheduleTime(
  long start,
  long end
  ) {
    this.start = start;
    this.end = end;
  }

  public long getStart() {
    return this.start;
  }
  public long getEnd() {
    return this.end;
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(StartField);
      Long start_item = start;
      _oprot.writeI64(start_item);
      _oprot.writeFieldEnd();
      _oprot.writeFieldBegin(EndField);
      Long end_item = end;
      _oprot.writeI64(end_item);
      _oprot.writeFieldEnd();
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  }

  public boolean equals(Object other) {
    if (!(other instanceof TScheduleTime)) return false;
    TScheduleTime that = (TScheduleTime) other;
    return
      this.start == that.start &&

      this.end == that.end
;
  }

  public String toString() {
    return "TScheduleTime(" + this.start + "," + this.end + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * new Long(this.start).hashCode();
    hash = hash * new Long(this.end).hashCode();
    return hash;
  }
}