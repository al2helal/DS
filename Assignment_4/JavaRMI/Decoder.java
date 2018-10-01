import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class Decoder extends UnicastRemoteObject implements DecodingInterface
{
    public Decoder() throws RemoteException
    {

    }
    private String getContent(String fileName)
    {
        String input = "";
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                input += line;
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        return input;
    }
    @Override
    public String decodeVigenere(String key) throws RemoteException
    {
        String cipher = getContent("TabulaRecta.txt");
        String result  = "";
        cipher = cipher.toUpperCase();
        key = key.toUpperCase();
        char ch;
        System.out.println("key leng="+key.length());
        for(int i=0;i<cipher.length(); i++)
        {
            if(cipher.charAt(i)>='A' && cipher.charAt(i)<='Z')
            {
                ch = (char)(((((int)cipher.charAt(i) - (int)key.charAt(i%key.length())) + 26) % 26) + 65);
                System.out.println("input="+cipher.charAt(i)+" output="+ch);
                result += ch;
            }
        }
        return result;
    }
    @Override
    public String decodeCeaser(String key) throws RemoteException
    {
        String cipher = getContent("Cipher.txt");
        System.out.println("cipher"+cipher);
        String result = "";
        key = key.toUpperCase();
        ArrayList<Character> finalKey = new ArrayList<>();
        for(int i=0;i<key.length();i++)
        {
            if(finalKey.contains(key.charAt(i)) != true)
            {
                finalKey.add(key.charAt(i));
            }
        }
        for(int i=65;i<91;i++)
        {
            if(finalKey.contains((char)(i)) != true)
            {
                finalKey.add((char)(i));
            }
        }
        System.out.println("key="+finalKey.toString());
        char ch;
        for(int i=0;i<cipher.length();i++)
        {
            ch = (char)((finalKey.indexOf(cipher.charAt(i)))+65);
            result += ch;
            System.out.println(cipher.charAt(i)+"="+finalKey.indexOf(cipher.charAt(i))+"="+ch);
        }
        System.out.println("result="+result);
        return result;
    }
}