package com.buzzinate.buzzads.thrift;

import com.twitter.scrooge.ScroogeOption;
import com.twitter.finagle.SourcedException;
import com.twitter.scrooge.ThriftStruct;
import com.twitter.scrooge.ThriftStructCodec;
import com.twitter.scrooge.Utilities;
import com.twitter.util.Future;
import com.twitter.util.FutureEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
import org.apache.thrift.TApplicationException;

import com.twitter.finagle.stats.InMemoryStatsReceiver;
import com.twitter.finagle.stats.NullStatsReceiver;
import com.twitter.finagle.stats.StatsReceiver;
import com.twitter.finagle.thrift.ThriftClientRequest;
import java.util.Arrays;
import org.apache.thrift.TException;
import com.twitter.finagle.Service;
import com.twitter.finagle.stats.Counter;
import com.twitter.util.Function;
import com.twitter.util.Function2;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;

//CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class AdServices {
  public interface Iface {
    public List<AdItem> serve(AdParam param);
  }

  public interface FutureIface {
    public Future<List<AdItem>> serve(AdParam param);
  }

  static class serve_args implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("serve_args");
    private static final TField ParamField = new TField("param", TType.STRUCT, (short) 1);
    final AdParam param;
  
    public static class Builder {
      private AdParam _param = null;
      private Boolean _got_param = false;
  
      public Builder param(AdParam value) {
        this._param = value;
        this._got_param = true;
        return this;
      }
  
      public serve_args build() {
        return new serve_args(
          this._param    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      builder.param(this.param);
      return builder;
    }
  
    public static ThriftStructCodec<serve_args> CODEC = new ThriftStructCodec<serve_args>() {
      public serve_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        AdParam param = null;
        Boolean _done = false;
        _iprot.readStructBegin();
        while (!_done) {
          TField _field = _iprot.readFieldBegin();
          if (_field.type == TType.STOP) {
            _done = true;
          } else {
            switch (_field.id) {
              case 1: /* param */
                switch (_field.type) {
                  case TType.STRUCT:
                    AdParam param_item;
                    param_item = AdParam.decode(_iprot);
                    param = param_item;
                    break;
                  default:
                    TProtocolUtil.skip(_iprot, _field.type);
                }
                builder.param(param);
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
  
      public void encode(serve_args struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static serve_args decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(serve_args struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public serve_args(
    AdParam param
    ) {
      this.param = param;
    }
  
    public AdParam getParam() {
      return this.param;
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
        _oprot.writeFieldBegin(ParamField);
        AdParam param_item = param;
        param_item.write(_oprot);
        _oprot.writeFieldEnd();
      _oprot.writeFieldStop();
      _oprot.writeStructEnd();
    }
  
    private void validate() throws org.apache.thrift.protocol.TProtocolException {
    }
  
    public boolean equals(Object other) {
      if (!(other instanceof serve_args)) return false;
      serve_args that = (serve_args) other;
      return
  this.param.equals(that.param);
    }
  
    public String toString() {
      return "serve_args(" + this.param + ")";
    }
  
    public int hashCode() {
      int hash = 1;
      hash = hash * (this.param == null ? 0 : this.param.hashCode());
      return hash;
    }
  }
  static class serve_result implements ThriftStruct {
    private static final TStruct STRUCT = new TStruct("serve_result");
    private static final TField SuccessField = new TField("success", TType.LIST, (short) 0);
    final ScroogeOption<List<AdItem>> success;
  
    public static class Builder {
      private List<AdItem> _success = Utilities.makeList();
      private Boolean _got_success = false;
  
      public Builder success(List<AdItem> value) {
        this._success = value;
        this._got_success = true;
        return this;
      }
  
      public serve_result build() {
        return new serve_result(
        ScroogeOption.make(this._got_success, this._success)    );
      }
    }
  
    public Builder copy() {
      Builder builder = new Builder();
      if (this.success.isDefined()) builder.success(this.success.get());
      return builder;
    }
  
    public static ThriftStructCodec<serve_result> CODEC = new ThriftStructCodec<serve_result>() {
      public serve_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
        Builder builder = new Builder();
        List<AdItem> success = Utilities.makeList();
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
                    List<AdItem> success_item;
                    TList _list_success_item = _iprot.readListBegin();
                    success_item = new ArrayList<AdItem>();
                    int _i_success_item = 0;
                    AdItem _success_item_element;
                    while (_i_success_item < _list_success_item.size) {
                      _success_item_element = AdItem.decode(_iprot);
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
  
      public void encode(serve_result struct, TProtocol oprot) throws org.apache.thrift.TException {
        struct.write(oprot);
      }
    };
  
    public static serve_result decode(TProtocol _iprot) throws org.apache.thrift.TException {
      return CODEC.decode(_iprot);
    }
  
    public static void encode(serve_result struct, TProtocol oprot) throws org.apache.thrift.TException {
      CODEC.encode(struct, oprot);
    }
  
    public serve_result(
    ScroogeOption<List<AdItem>> success
    ) {
      this.success = success;
    }
  
    public List<AdItem> getSuccess() {
      return this.success.get();
    }
  
    public void write(TProtocol _oprot) throws org.apache.thrift.TException {
      validate();
      _oprot.writeStructBegin(STRUCT);
      if (success.isDefined()) {  _oprot.writeFieldBegin(SuccessField);
        List<AdItem> success_item = success.get();
        _oprot.writeListBegin(new TList(TType.STRUCT, success_item.size()));
        for (AdItem _success_item_element : success_item) {
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
      if (!(other instanceof serve_result)) return false;
      serve_result that = (serve_result) other;
      return
  this.success.equals(that.success);
    }
  
    public String toString() {
      return "serve_result(" + this.success + ")";
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
        this.serviceName = "AdServices";
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
  
    private __Stats __stats_serve = new __Stats("serve");
  
    public Future<List<AdItem>> serve(AdParam param) {
      __stats_serve.requestsCounter.incr();
  
      Future<List<AdItem>> rv = this.service.apply(encodeRequest("serve", new serve_args(param))).flatMap(new Function<byte[], Future<List<AdItem>>>() {
        public Future<List<AdItem>> apply(byte[] in) {
          try {
            serve_result result = decodeResponse(in, serve_result.CODEC);
  
  
            if (result.success.isDefined()) return Future.value(result.success.get());
            return Future.exception(missingResult("serve"));
          } catch (TException e) {
            return Future.exception(e);
          }
        }
      }).rescue(new Function<Throwable, Future<List<AdItem>>>() {
        public Future<List<AdItem>> apply(Throwable t) {
          if (t instanceof SourcedException) {
            ((SourcedException) t).serviceName_$eq(FinagledClient.this.serviceName);
          }
          return Future.exception(t);
        }
      });
  
      rv.addEventListener(new FutureEventListener<List<AdItem>>() {
        public void onSuccess(List<AdItem> result) {
          __stats_serve.successCounter.incr();
        }
  
        public void onFailure(Throwable t) {
          __stats_serve.failuresCounter.incr();
          __stats_serve.failuresScope.counter0(t.getClass().getName()).incr();
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
  
      addFunction("serve", new Function2<TProtocol, Integer, Future<byte[]>>() {
        public Future<byte[]> apply(TProtocol iprot, final Integer seqid) {
          try {
            serve_args args = serve_args.decode(iprot);
            iprot.readMessageEnd();
            Future<List<AdItem>> result;
            try {
              result = iface.serve(args.param);
            } catch (Throwable t) {
              result = Future.exception(t);
            }
            return result.flatMap(new Function<List<AdItem>, Future<byte[]>>() {
              public Future<byte[]> apply(List<AdItem> value){
                return reply("serve", seqid, new serve_result.Builder().success(value).build());
              }
            }).rescue(new Function<Throwable, Future<byte[]>>() {
              public Future<byte[]> apply(Throwable t) {
                return Future.exception(t);
              }
            });
          } catch (TProtocolException e) {
            try {
              iprot.readMessageEnd();
              return exception("serve", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage());
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