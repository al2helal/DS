import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class JavaRMIServerApplication
{
    public static void main(String[] args) throws RemoteException
    {
        Decoder decoder = new Decoder();
        int port = 3801;
        try 
        {
            LocateRegistry.createRegistry(port);
            Naming.bind("//0.0.0.0:3801/Hello", decoder);
        }
        catch (Exception ex) 
        {
            System.out.println(ex.toString());
        }
    }
}