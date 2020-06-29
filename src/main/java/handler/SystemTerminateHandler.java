package handler;
import libs.SharedMemory;
import org.java_websocket.WebSocket;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class SystemTerminateHandler implements SignalHandler {

    public static void install(String signalName, SignalHandler handler) {
        Signal signal = new Signal(signalName);
        SystemTerminateHandler diagnosticSignalHandler = new SystemTerminateHandler();
        SignalHandler oldHandler = Signal.handle(signal, diagnosticSignalHandler);
        diagnosticSignalHandler.setHandler(handler);
        diagnosticSignalHandler.setOldHandler(oldHandler);
    }
    private SignalHandler oldHandler;
    private static SignalHandler handler;

    private SystemTerminateHandler() {
    }

    private void setOldHandler(SignalHandler oldHandler) {
        this.oldHandler = oldHandler;
    }

    private void setHandler(SignalHandler handler) {
        this.handler = handler;
    }

    // Signal handler method
    @Override
    public void handle(Signal sig) {
        System.out.println("Diagnostic Signal handler called for signal " + sig);
        try {
            handler.handle(sig);
//            if (oldHandler != SIG_DFL && oldHandler != SIG_IGN) {
//                oldHandler.handle(sig);
//            }
        } catch (Exception e) {
            System.out.println("Signal handler failed, reason " + e);
        }
    }

    public static void register() {
        SystemTerminateHandler.handler = (sig) -> {
            SystemTerminateHandler.stopSys(sig);
        };
        SystemTerminateHandler.install("TERM", handler);
        SystemTerminateHandler.install("INT", handler);
        SystemTerminateHandler.install("ABRT", handler);
    }

    public static void stopSys(Signal signal){
        System.out.println("PREPARE TERMINATE SYSTEM");
        System.out.println("SIGNAL " + signal);
        System.out.println("closing connections ");
        SharedMemory.deviceSocket.close();
        for (WebSocket user:
                SharedMemory.frontendUsersSocket){
            user.close();
        }
        synchronized (SharedMemory.threads) {
            for (Thread thread :
                    SharedMemory.threads) {
                if (thread.isAlive())
                    thread.interrupt();
            }
        }
        System.out.println("System stopped");
        System.exit(0);
    }


}