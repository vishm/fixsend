import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;


public class FixSend {
  private final static Logger log = LoggerFactory.getLogger(FixSend.class);

  public static void main(String[] args) throws Exception {
    try {
      InputStream inputStream = getSettingsInputStream(args);
      SessionSettings settings = new SessionSettings(inputStream);
      inputStream.close();

      var k = 9;

      MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
     // LogFactory logFactory = new ScreenLogFactory(true, true, true, logHeartbeats);
      MessageFactory messageFactory = new DefaultMessageFactory();

      Application application = new FixSendApplication();
      //LogFactory logFactory = new FileLogFactory(settings);
      LogFactory logFactory = new ScreenLogFactory(true, true, true);
      Initiator initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory, messageFactory);
      initiator.start();

      var sessionId = initiator.getSessions().get(0);
      send42(sessionId);
      System.out.println("press <enter> to quit");
      System.in.read();

      initiator.stop();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private static InputStream getSettingsInputStream(String[] args) throws FileNotFoundException {
    InputStream inputStream = null;
    if (args.length == 0) {
      inputStream = FixSend.class.getResourceAsStream("FixSend/FixSend.cfg");
    } else if (args.length == 1) {
      inputStream = new FileInputStream(args[0]);
    }
    if (inputStream == null) {
      System.out.println("usage: " + FixSend.class.getName() + " [configFile].");
      System.exit(1);
    }
    return inputStream;
  }


  public static void send42(SessionID sessionID) {

    var currentTime = LocalDateTime.now(Clock.systemUTC());

    var newOrderSingle = new quickfix.fix42.NewOrderSingle(
        new ClOrdID("1234"), new HandlInst(HandlInst.MANUAL_ORDER_BEST_EXECUTION),
        new Symbol("MSFT"),
        new Side(Side.BUY),
        new TransactTime(currentTime), new OrdType(OrdType.MARKET));
    newOrderSingle.set(new OrderQty(30.0));

    send(newOrderSingle, sessionID);
  }

  public static void send44(SessionID sessionID) {


    var currentTime = LocalDateTime.now(Clock.systemUTC());

    quickfix.fix44.NewOrderSingle newOrderSingle = new quickfix.fix44.NewOrderSingle(
        new ClOrdID("1234"), new Side(Side.BUY),
        new TransactTime(currentTime),  new OrdType(OrdType.MARKET));
    newOrderSingle.set(new Symbol("MSFT"));
    newOrderSingle.set(new OrderQty(30.0));


    send(newOrderSingle, sessionID);
  }


  private static void send(quickfix.Message message, SessionID sessionID) {
    try {
      Session.sendToTarget(message, sessionID);
    } catch (SessionNotFound e) {
      System.out.println(e);
    }
  }
}
