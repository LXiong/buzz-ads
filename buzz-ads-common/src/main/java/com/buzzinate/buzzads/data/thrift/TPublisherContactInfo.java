package com.buzzinate.buzzads.data.thrift;

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
public class TPublisherContactInfo implements ThriftStruct {
  private static final TStruct STRUCT = new TStruct("TPublisherContactInfo");
  private static final TField UserIdField = new TField("userId", TType.I32, (short) 1);
  final int userId;
  private static final TField NameField = new TField("name", TType.STRING, (short) 2);
  final ScroogeOption<String> name;
  private static final TField EmailField = new TField("email", TType.STRING, (short) 3);
  final ScroogeOption<String> email;
  private static final TField MobileField = new TField("mobile", TType.STRING, (short) 4);
  final ScroogeOption<String> mobile;
  private static final TField QqField = new TField("qq", TType.STRING, (short) 5);
  final ScroogeOption<String> qq;
  private static final TField RevMethodField = new TField("revMethod", TType.I32, (short) 6);
  final ScroogeOption<TPublisherContactRevMethod> revMethod;
  private static final TField RevAccountField = new TField("revAccount", TType.STRING, (short) 7);
  final ScroogeOption<String> revAccount;
  private static final TField RevNameField = new TField("revName", TType.STRING, (short) 8);
  final ScroogeOption<String> revName;
  private static final TField RevBankField = new TField("revBank", TType.STRING, (short) 9);
  final ScroogeOption<String> revBank;
  private static final TField StatusField = new TField("status", TType.I32, (short) 10);
  final ScroogeOption<TPublisherContactStatus> status;

  public static class Builder {
    private int _userId = 0;
    private Boolean _got_userId = false;

    public Builder userId(int value) {
      this._userId = value;
      this._got_userId = true;
      return this;
    }
    private String _name = null;
    private Boolean _got_name = false;

    public Builder name(String value) {
      this._name = value;
      this._got_name = true;
      return this;
    }
    private String _email = null;
    private Boolean _got_email = false;

    public Builder email(String value) {
      this._email = value;
      this._got_email = true;
      return this;
    }
    private String _mobile = null;
    private Boolean _got_mobile = false;

    public Builder mobile(String value) {
      this._mobile = value;
      this._got_mobile = true;
      return this;
    }
    private String _qq = null;
    private Boolean _got_qq = false;

    public Builder qq(String value) {
      this._qq = value;
      this._got_qq = true;
      return this;
    }
    private TPublisherContactRevMethod _revMethod = null;
    private Boolean _got_revMethod = false;

    public Builder revMethod(TPublisherContactRevMethod value) {
      this._revMethod = value;
      this._got_revMethod = true;
      return this;
    }
    private String _revAccount = null;
    private Boolean _got_revAccount = false;

    public Builder revAccount(String value) {
      this._revAccount = value;
      this._got_revAccount = true;
      return this;
    }
    private String _revName = null;
    private Boolean _got_revName = false;

    public Builder revName(String value) {
      this._revName = value;
      this._got_revName = true;
      return this;
    }
    private String _revBank = null;
    private Boolean _got_revBank = false;

    public Builder revBank(String value) {
      this._revBank = value;
      this._got_revBank = true;
      return this;
    }
    private TPublisherContactStatus _status = null;
    private Boolean _got_status = false;

    public Builder status(TPublisherContactStatus value) {
      this._status = value;
      this._got_status = true;
      return this;
    }

    public TPublisherContactInfo build() {
      return new TPublisherContactInfo(
        this._userId,
      ScroogeOption.make(this._got_name, this._name),
      ScroogeOption.make(this._got_email, this._email),
      ScroogeOption.make(this._got_mobile, this._mobile),
      ScroogeOption.make(this._got_qq, this._qq),
      ScroogeOption.make(this._got_revMethod, this._revMethod),
      ScroogeOption.make(this._got_revAccount, this._revAccount),
      ScroogeOption.make(this._got_revName, this._revName),
      ScroogeOption.make(this._got_revBank, this._revBank),
      ScroogeOption.make(this._got_status, this._status)    );
    }
  }

  public Builder copy() {
    Builder builder = new Builder();
    builder.userId(this.userId);
    if (this.name.isDefined()) builder.name(this.name.get());
    if (this.email.isDefined()) builder.email(this.email.get());
    if (this.mobile.isDefined()) builder.mobile(this.mobile.get());
    if (this.qq.isDefined()) builder.qq(this.qq.get());
    if (this.revMethod.isDefined()) builder.revMethod(this.revMethod.get());
    if (this.revAccount.isDefined()) builder.revAccount(this.revAccount.get());
    if (this.revName.isDefined()) builder.revName(this.revName.get());
    if (this.revBank.isDefined()) builder.revBank(this.revBank.get());
    if (this.status.isDefined()) builder.status(this.status.get());
    return builder;
  }

  public static ThriftStructCodec<TPublisherContactInfo> CODEC = new ThriftStructCodec<TPublisherContactInfo>() {
    public TPublisherContactInfo decode(TProtocol _iprot) throws org.apache.thrift.TException {
      Builder builder = new Builder();
      int userId = 0;
      String name = null;
      String email = null;
      String mobile = null;
      String qq = null;
      TPublisherContactRevMethod revMethod = null;
      String revAccount = null;
      String revName = null;
      String revBank = null;
      TPublisherContactStatus status = null;
      Boolean _done = false;
      _iprot.readStructBegin();
      while (!_done) {
        TField _field = _iprot.readFieldBegin();
        if (_field.type == TType.STOP) {
          _done = true;
        } else {
          switch (_field.id) {
            case 1: /* userId */
              switch (_field.type) {
                case TType.I32:
                  Integer userId_item;
                  userId_item = _iprot.readI32();
                  userId = userId_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.userId(userId);
              break;
            case 2: /* name */
              switch (_field.type) {
                case TType.STRING:
                  String name_item;
                  name_item = _iprot.readString();
                  name = name_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.name(name);
              break;
            case 3: /* email */
              switch (_field.type) {
                case TType.STRING:
                  String email_item;
                  email_item = _iprot.readString();
                  email = email_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.email(email);
              break;
            case 4: /* mobile */
              switch (_field.type) {
                case TType.STRING:
                  String mobile_item;
                  mobile_item = _iprot.readString();
                  mobile = mobile_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.mobile(mobile);
              break;
            case 5: /* qq */
              switch (_field.type) {
                case TType.STRING:
                  String qq_item;
                  qq_item = _iprot.readString();
                  qq = qq_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.qq(qq);
              break;
            case 6: /* revMethod */
              switch (_field.type) {
                case TType.I32:
                  TPublisherContactRevMethod revMethod_item;
                  revMethod_item = TPublisherContactRevMethod.findByValue(_iprot.readI32());
                  revMethod = revMethod_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.revMethod(revMethod);
              break;
            case 7: /* revAccount */
              switch (_field.type) {
                case TType.STRING:
                  String revAccount_item;
                  revAccount_item = _iprot.readString();
                  revAccount = revAccount_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.revAccount(revAccount);
              break;
            case 8: /* revName */
              switch (_field.type) {
                case TType.STRING:
                  String revName_item;
                  revName_item = _iprot.readString();
                  revName = revName_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.revName(revName);
              break;
            case 9: /* revBank */
              switch (_field.type) {
                case TType.STRING:
                  String revBank_item;
                  revBank_item = _iprot.readString();
                  revBank = revBank_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.revBank(revBank);
              break;
            case 10: /* status */
              switch (_field.type) {
                case TType.I32:
                  TPublisherContactStatus status_item;
                  status_item = TPublisherContactStatus.findByValue(_iprot.readI32());
                  status = status_item;
                  break;
                default:
                  TProtocolUtil.skip(_iprot, _field.type);
              }
              builder.status(status);
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

    public void encode(TPublisherContactInfo struct, TProtocol oprot) throws org.apache.thrift.TException {
      struct.write(oprot);
    }
  };

  public static TPublisherContactInfo decode(TProtocol _iprot) throws org.apache.thrift.TException {
    return CODEC.decode(_iprot);
  }

  public static void encode(TPublisherContactInfo struct, TProtocol oprot) throws org.apache.thrift.TException {
    CODEC.encode(struct, oprot);
  }

  public TPublisherContactInfo(
  int userId,
  ScroogeOption<String> name,
  ScroogeOption<String> email,
  ScroogeOption<String> mobile,
  ScroogeOption<String> qq,
  ScroogeOption<TPublisherContactRevMethod> revMethod,
  ScroogeOption<String> revAccount,
  ScroogeOption<String> revName,
  ScroogeOption<String> revBank,
  ScroogeOption<TPublisherContactStatus> status
  ) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.mobile = mobile;
    this.qq = qq;
    this.revMethod = revMethod;
    this.revAccount = revAccount;
    this.revName = revName;
    this.revBank = revBank;
    this.status = status;
  }
  public ScroogeOption getNameOption() {
      return this.name;
    }
    public ScroogeOption getEmailOption() {
      return this.email;
    }
    public ScroogeOption getMobileOption() {
      return this.mobile;
    }
    public ScroogeOption getQqOption() {
      return this.qq;
    }
    public ScroogeOption getRevMethodOption() {
      return this.revMethod;
    }
    public ScroogeOption getRevAccountOption() {
      return this.revAccount;
    }
    public ScroogeOption getRevNameOption() {
      return this.revName;
    }
    public ScroogeOption getRevBankOption() {
      return this.revBank;
    }
    public ScroogeOption getStatusoption() {
      return this.status;  
    }
  public int getUserId() {
    return this.userId;
  }
  public String getName() {
    return this.name.get();
  }
  public String getEmail() {
    return this.email.get();
  }
  public String getMobile() {
    return this.mobile.get();
  }
  public String getQq() {
    return this.qq.get();
  }
  public TPublisherContactRevMethod getRevMethod() {
    return this.revMethod.get();
  }
  public String getRevAccount() {
    return this.revAccount.get();
  }
  public String getRevName() {
    return this.revName.get();
  }
  public String getRevBank() {
    return this.revBank.get();
  }
  public TPublisherContactStatus getStatus() {
    return this.status.get();
  }

  public void write(TProtocol _oprot) throws org.apache.thrift.TException {
    validate();
    _oprot.writeStructBegin(STRUCT);
      _oprot.writeFieldBegin(UserIdField);
      Integer userId_item = userId;
      _oprot.writeI32(userId_item);
      _oprot.writeFieldEnd();
    if (name.isDefined()) {  _oprot.writeFieldBegin(NameField);
      String name_item = name.get();
      _oprot.writeString(name_item);
      _oprot.writeFieldEnd();
    }
    if (email.isDefined()) {  _oprot.writeFieldBegin(EmailField);
      String email_item = email.get();
      _oprot.writeString(email_item);
      _oprot.writeFieldEnd();
    }
    if (mobile.isDefined()) {  _oprot.writeFieldBegin(MobileField);
      String mobile_item = mobile.get();
      _oprot.writeString(mobile_item);
      _oprot.writeFieldEnd();
    }
    if (qq.isDefined()) {  _oprot.writeFieldBegin(QqField);
      String qq_item = qq.get();
      _oprot.writeString(qq_item);
      _oprot.writeFieldEnd();
    }
    if (revMethod.isDefined()) {  _oprot.writeFieldBegin(RevMethodField);
      TPublisherContactRevMethod revMethod_item = revMethod.get();
      _oprot.writeI32(revMethod_item.getValue());
      _oprot.writeFieldEnd();
    }
    if (revAccount.isDefined()) {  _oprot.writeFieldBegin(RevAccountField);
      String revAccount_item = revAccount.get();
      _oprot.writeString(revAccount_item);
      _oprot.writeFieldEnd();
    }
    if (revName.isDefined()) {  _oprot.writeFieldBegin(RevNameField);
      String revName_item = revName.get();
      _oprot.writeString(revName_item);
      _oprot.writeFieldEnd();
    }
    if (revBank.isDefined()) {  _oprot.writeFieldBegin(RevBankField);
      String revBank_item = revBank.get();
      _oprot.writeString(revBank_item);
      _oprot.writeFieldEnd();
    }
    if (status.isDefined()) {  _oprot.writeFieldBegin(StatusField);
      TPublisherContactStatus status_item = status.get();
      _oprot.writeI32(status_item.getValue());
      _oprot.writeFieldEnd();
    }
    _oprot.writeFieldStop();
    _oprot.writeStructEnd();
  }

  private void validate() throws org.apache.thrift.protocol.TProtocolException {
  }

  public boolean equals(Object other) {
    if (!(other instanceof TPublisherContactInfo)) return false;
    TPublisherContactInfo that = (TPublisherContactInfo) other;
    return
      this.userId == that.userId &&

this.name.equals(that.name) &&

this.email.equals(that.email) &&

this.mobile.equals(that.mobile) &&

this.qq.equals(that.qq) &&

this.revMethod.equals(that.revMethod) &&

this.revAccount.equals(that.revAccount) &&

this.revName.equals(that.revName) &&

this.revBank.equals(that.revBank) &&

this.status.equals(that.status)
;
  }

  public String toString() {
    return "TPublisherContactInfo(" + this.userId + "," + this.name + "," + this.email + "," + this.mobile + "," + this.qq + "," + this.revMethod + "," + this.revAccount + "," + this.revName + "," + this.revBank + "," + this.status + ")";
  }

  public int hashCode() {
    int hash = 1;
    hash = hash * new Integer(this.userId).hashCode();
    hash = hash * (this.name.isDefined() ? 0 : this.name.get().hashCode());
    hash = hash * (this.email.isDefined() ? 0 : this.email.get().hashCode());
    hash = hash * (this.mobile.isDefined() ? 0 : this.mobile.get().hashCode());
    hash = hash * (this.qq.isDefined() ? 0 : this.qq.get().hashCode());
    hash = hash * (this.revMethod.isDefined() ? 0 : this.revMethod.get().hashCode());
    hash = hash * (this.revAccount.isDefined() ? 0 : this.revAccount.get().hashCode());
    hash = hash * (this.revName.isDefined() ? 0 : this.revName.get().hashCode());
    hash = hash * (this.revBank.isDefined() ? 0 : this.revBank.get().hashCode());
    hash = hash * (this.status.isDefined() ? 0 : this.status.get().hashCode());
    return hash;
  }
}