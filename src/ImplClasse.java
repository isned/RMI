import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implémenter l'interface de l'objet distant
public class ImplClasse extends UnicastRemoteObject implements RemoteInter {
    private Connection conn = null;
    private Statement stmt = null;

    protected ImplClasse() throws RemoteException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/my_db", "root", "");
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RemoteException("Erreur lors de la création de la connexion à la base de données", e);
        }
    }

    public List<Person> getPersones() throws RemoteException {
        List<Person> liste = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Person";
            try (ResultSet res = stmt.executeQuery(sql)) {
                // Extraire des données de ResultSet
                while (res.next()) {
                    int id = res.getInt(1);
                    int age = res.getInt(2);
                    String name = res.getString(3);
                    String address = res.getString(4);

                    liste.add(new Person(id, age, name, address));
                }
            } catch (SQLException e) {
                System.out.println("erreur" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }


    public void addPerson(Person person) throws RemoteException {
        try {
            String sql = "INSERT INTO Person (name, age, address) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, person.getName());
                preparedStatement.setInt(2, person.getAge());
                preparedStatement.setString(3, person.getAddress());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la requête : " + e.getMessage());
        }
    }

    public void deletePerson(int personId) throws RemoteException {
        try {
            String sql = "DELETE FROM Person WHERE id=?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, personId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la requête de suppression : " + e.getMessage());
        }
    }

    @Override
    public void updatePerson(int id, int age, String name, String address) throws RemoteException {
        try {
            System.out.println("Méthode updatePerson appelée avec id=" + id + ", age=" + age + ", name=" + name + ", address=" + address);

            String sql = "UPDATE Person SET name = ?, age = ?, address = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, address);
                preparedStatement.setInt(4, id);

                preparedStatement.executeUpdate();

                System.out.println("Mise à jour réussie pour l'ID " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la requête de mise à jour : " + e.getMessage());
            throw new RemoteException("Erreur dans la requête de mise à jour", e);
        }
    }


    @Override
    public List<Person> searchPersonsByName(String name) throws RemoteException {
        List<Person> searchResults = new ArrayList<>();

        try {
            // Requête SQL pour rechercher une personne par nom
            String sql = "SELECT * FROM Person WHERE name LIKE ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + name + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Person person = new Person();
                        person.setId(resultSet.getInt("id"));
                        person.setName(resultSet.getString("name"));
                        person.setAge(resultSet.getInt("age"));
                        person.setAddress(resultSet.getString("address"));
                        searchResults.add(person);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur dans la requête de recherche : " + e.getMessage());
            throw new RemoteException("Erreur dans la requête de recherche", e);
        }

        return searchResults;
    }


}
