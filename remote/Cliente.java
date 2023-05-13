import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente implements ClientInterface, Serializable, Runnable {
  // private static final String IP = "192.168.1.15"; // Puedes cambiar a
  // localhost
  private static final String IP = "localhost"; // Puedes cambiar a localhost
  private static final int PUERTO = 1100; // Si cambias aquí el puerto, recuerda cambiarlo en el servidor
  private static final long serialVersionUID = 1L;
  private Interfaz server;
  private String name;

  protected Cliente(Interfaz chatinterface, String name) throws RemoteException {
    this.server = chatinterface;
    this.name = name;
  }

  @Override
  public void receiveMessage(String message) throws RemoteException {
    System.out.println("Received message: para " + name + " el mensaje del servidor es" + message);
  }

  @Override
  public void run() {
    try {
      server.registerClient(this);
      // Para acciones 1
      // server.depositar(500);
      // server.retirar(500);
      // para enviar instrucciones
      // Primer parametro es la action y despues el monto 1: retirar 2:depositar
      server.sendInstruccions(new String[] { "2:500", "1:500" });
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws RemoteException, NotBoundException {
    Registry registry = LocateRegistry.getRegistry(IP, PUERTO);
    Interfaz server = (Interfaz) registry.lookup("Calculadora"); // Buscar en el registro...
    new Thread(new Cliente(server, "Julio")).start();
    new Thread(new Cliente(server, "Ricardo")).start();

  }
}