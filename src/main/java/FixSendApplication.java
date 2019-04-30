import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

public class FixSendApplication implements Application {

  public void onLogout(SessionID sessionID) {

  }

  public void toAdmin(Message message, SessionID sessionID) {

  }

  public void fromAdmin(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

  }

  public void toApp(Message message, SessionID sessionID) throws DoNotSend {
    System.out.println("toApp:" +message);
  }

  public void fromApp(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
      System.out.println("fromApp:" +message);
  }

  public void onLogon(SessionID sessionID) {

  }

  public void onCreate(SessionID sessionID) {

  }

}
