import java.rmi.Naming;
import java.util.Scanner;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Client
{
   public static void main(String[] args)
   {
       DecodingInterface decoder = null;
       try
       {
            Registry reg = LocateRegistry.getRegistry("192.168.0.108", 3801);
            //Registry reg = LocateRegistry.getRegistry("192.168.0.116", 3801);
            decoder= (DecodingInterface) reg.lookup("Hello");
           //decoder = (DecodingInterface)Naming.lookup("//192.168.0.116:3801/Hello");
       }
       catch(Exception ex)
       {
           System.out.println(ex.toString());
       }
       System.out.println("1.Ceaser");
       System.out.println("2.Vigenere");
       String key = "GORDiAN";
       Scanner scanner = new Scanner(System.in);
       int input = scanner.nextInt();
       try
       {
            if(input == 1)
            {
                System.out.println(decoder.decodeCeaser(key));
            }
            else if (input == 2)
            {
                System.out.println(decoder.decodeVigenere(key));
            }
        }
        catch(RemoteException ex)
        {
            System.out.println(ex.toString());
        }
   }
}
