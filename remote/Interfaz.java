import java.rmi.Remote;
import java.rmi.RemoteException;

/*
	Declarar firma de métodos que serán sobrescritos
*/
public interface Interfaz extends Remote {

  void depositar(int monto) throws RemoteException;

  void retirar(int monto) throws RemoteException;

  void registerClient(ClientInterface client) throws RemoteException;

  void sendInstruccions(String[] instrucciones) throws RemoteException;

}