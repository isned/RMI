import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class MainServer {

    public static void main(String[] args) {
        System.out.println("Démarrage du serveur...");

        try {
            // Crée l'objet distant
            ImplClasse obj = new ImplClasse();

            // Crée ou obtient le registre RMI sur le port 9002
            LocateRegistry.createRegistry(9002);

            // Crée l'URL pour l'objet distant
            String url = "rmi://localhost:9002/RemoteInter";

            // Enregistre l'objet distant avec l'URL
            Naming.rebind(url, obj);

            System.out.println("Le serveur est prêt.");
        } catch (RemoteException e) {
            System.err.println("Erreur lors de l'enregistrement de l'objet distant: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur générale: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
