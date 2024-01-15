import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteInter extends Remote {
    public List<Person> getPersones() throws RemoteException;
    void addPerson(Person person) throws RemoteException;

    void deletePerson(int personId) throws RemoteException;
    void updatePerson(int id, int age, String name, String address) throws RemoteException;

    List<Person> searchPersonsByName(String name) throws RemoteException;


}
