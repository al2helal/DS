import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JavaRMIServerApplication
{
    public static void main(String[] args) throws RemoteException
    {
        Decoder decoder = new Decoder();
        int port = 3801;
        try 
        {
            System.setProperty("java.rmi.server.hostname", "192.168.0.108");
            Registry reg = LocateRegistry.createRegistry(port);
            reg.bind("Hello", decoder);
        }
        catch (Exception ex) 
        {
            System.out.println(ex.toString());
        }
    }
}
