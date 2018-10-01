import java.rmi.Naming;
import java.util.Scanner;
import java.rmi.RemoteException;

public class Client
{
   public static void main(String[] args) 
   {
       DecodingInterface decoder = null;
       try
       {
           decoder = (DecodingInterface)Naming.lookup("//0.0.0.0:3801/Hello");
       }
       catch(Exception ex)
       {
           System.out.println(ex.toString());
       }
       System.out.println("1.Ceaser");
       System.out.println("2.Vigenere");
       String key = "GORDiANBangladesh";
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