import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
  private static final int PUERTO = 1100; // Si cambias aquí el puerto, recuerda cambiarlo en el cliente
  private static final String USUARIO_CUENTA = "Cliente1";
  private static Banco cuentas = new Banco();
  private static List<ClientInterface> clients = new ArrayList<ClientInterface>();;
  private static final long serialVersionUID = 1L;

  public static void sendMessageToAll(String message) {
    System.out.println("Sending message to all clients: ");
    for (ClientInterface client : clients) {
      try {
        client.receiveMessage(message);
      } catch (RemoteException e) {
        // Handle communication error with client if necessary
      }
    }
  }

  public static void main(String[] args) throws RemoteException, AlreadyBoundException {
    Remote remote = UnicastRemoteObject.exportObject(new Interfaz() {
      @Override
      public synchronized void depositar(int monto) throws RemoteException {
        cuentas.depositar(USUARIO_CUENTA, monto);
        sendMessageToAll(cuentas.mostrarCuentas());
      };

      @Override
      public synchronized void retirar(int monto) throws RemoteException {
        cuentas.sacar(USUARIO_CUENTA, monto);
        sendMessageToAll(cuentas.mostrarCuentas());
      };

      @Override
      public void registerClient(ClientInterface client) throws RemoteException {
        clients.add(client);
        System.out.println("New client registered.");
      }

      public void sendInstruccions(String[] instrucciones) throws RemoteException {
        int length = instrucciones.length;
        for (int i = 0; i < length; i++) {
          String action = instrucciones[i];
          int monto = Integer.parseInt(action.substring(2, action.length()));
          if (action.charAt(0) == '1') {
            this.retirar(monto);
          } else {
            this.depositar(monto);
          }
        }
      }

    }, 0);
    Registry registry = LocateRegistry.createRegistry(PUERTO);
    System.out.println("Servidor escuchando en el puerto " + String.valueOf(PUERTO));
    System.out.println(cuentas.mostrarCuentas());
    registry.bind("Calculadora", remote); // Registrar calculadora
  }
}