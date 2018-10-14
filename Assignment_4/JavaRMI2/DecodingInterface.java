import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DecodingInterface extends Remote
{
    public String decodeVigenere(String key) throws RemoteException;
    public String decodeCeaser(String key) throws RemoteException;
}