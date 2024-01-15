import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.List;

public class Client {


    public static void main(String[] args)  {
        System.out.println("Lancement un client");

        // Invocation de Remote
        String IPserver = "127.0.0.1:9002";
        String url = "rmi://" + IPserver + "/RemoteInter";

        try {

            RemoteInter r = (RemoteInter) Naming.lookup(url);
            SwingInterfaceClient clientInterface = new SwingInterfaceClient(r);

        } catch (NotBoundException e) {
            System.out.println("Erreur binding: " + e.getMessage());

        } catch (MalformedURLException e)  {
            System.out.println("Erreur URL: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur réseau ou méthode: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("erreur"+e.getMessage());
        }


    }
}
