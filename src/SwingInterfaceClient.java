import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

public class SwingInterfaceClient {

    private RemoteInter remoteObject;
    private JFrame frame;
    private JTextField nameField;
    private JTextField searchField;
    private JTextField ageField;
    private JTextField addressField;
    private DefaultTableModel tableModel;
    private JTable table;


    public SwingInterfaceClient(RemoteInter remoteObject) {
        this.remoteObject = remoteObject;
        createUI();
    }

    private void createUI() {
        frame = new JFrame("Interface Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel();
        // Créer un JLabel
        JLabel welcomeLabel = new JLabel("Gestion des Clients                 ");

        // Appliquer un style Font pour rendre le texte en gras et centré
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.BLUE);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

         // Ajouter le JLabel au panel principal
        mainPanel.add(welcomeLabel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = new JTextField(20);
        ageField = new JTextField(20);
        addressField = new JTextField(20);

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Âge:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Adresse:"));
        formPanel.add(addressField);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter ");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addPerson();
                    JOptionPane.showMessageDialog(frame, "Nouveau Person ajouté avec succès.");
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du Person : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton clearButton = new JButton("Vider");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteSelectedPerson();
                    JOptionPane.showMessageDialog(frame, "Personne supprimée avec succès.");
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression de la personne : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });





        // Modifiez votre bouton "Modifier" pour appeler la méthode updateSelectedPerson
        JButton updateButton = new JButton("Modifier");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateSelectedPerson();
                    JOptionPane.showMessageDialog(frame, "Personne modifiée avec succès.");
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la modification de la personne : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Nom à rechercher:"));
        searchField = new JTextField(20);

        searchPanel.add(searchField);

        // Ajout d'un KeyListener au champ de recherche
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    searchPersonsByName();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });



        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        // Panel pour la table
        JPanel tablePanel = new JPanel();
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Âge");
        tableModel.addColumn("Adresse");

        JScrollPane tableScrollPane = new JScrollPane(table);
        tablePanel.add(tableScrollPane);

        // Ajout des panels au panel principal
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(searchPanel);
        mainPanel.add(tablePanel);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtenez la ligne sélectionnée
                int selectedRow = table.getSelectedRow();

                // Assurez-vous que la ligne est valide
                if (selectedRow != -1) {
                    // Obtenez les valeurs de chaque colonne dans la ligne sélectionnée
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String name = (String) tableModel.getValueAt(selectedRow, 1);
                    int age = (int) tableModel.getValueAt(selectedRow, 2);
                    String address = (String) tableModel.getValueAt(selectedRow, 3);

                    // Mettez à jour les champs du formulaire avec les valeurs récupérées
                    nameField.setText(name);
                    ageField.setText(Integer.toString(age));
                    addressField.setText(address);
                }
            }
        });





        // Ajout du panel principal au contenu de la fenêtre
        frame.getContentPane().add(mainPanel);

        // Ajuster la taille et centrer la fenêtre
        frame.pack();
        frame.setLocationRelativeTo(null);

        // Rendre l'interface visible
        frame.setVisible(true);

        // Initialiser la table avec les données
        refreshTable();
    }

    private void addPerson() throws RemoteException {
        // Récupérer les valeurs depuis les champs de texte
        String name = nameField.getText();
        String ageText = ageField.getText();
        String address = addressField.getText();

        // Vérifier si le champ d'âge est vide
        if (ageText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Veuillez saisir une valeur pour l'âge.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Sortir de la méthode si le champ d'âge est vide
        }

        // Convertir l'âge en entier
        int age = Integer.parseInt(ageText);

        // Créer un nouveau Person
        Person newPerson = new Person();
        newPerson.setName(name);
        newPerson.setAge(age);
        newPerson.setAddress(address);

        // Ajouter le Person à distance
        remoteObject.addPerson(newPerson);

        // Vider les champs après l'ajout
        clearFields();

        // Rafraîchir la table avec les données mises à jour
        refreshTable();
    }
    private void searchPersonsByName() throws RemoteException {
        String searchName = searchField.getText();
        List<Person> searchResults = remoteObject.searchPersonsByName(searchName);

        // Nettoyer la table avant de l'actualiser
        tableModel.setRowCount(0);

        // Remplir la table avec les résultats de la recherche
        for (Person p : searchResults) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getAddress()});
        }
    }
    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        addressField.setText("");
    }
    private void deleteSelectedPerson() throws RemoteException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int personId = (int) tableModel.getValueAt(selectedRow, 0);
            remoteObject.deletePerson(personId);
            refreshTable(); // Rechargez les données après la suppression
            clearFields(); // Effacez également les champs du formulaire après la suppression
        }
    }
    // Modification de la méthode dans SwingInterfaceClient
    private void updateSelectedPerson() throws RemoteException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText();
            String ageText = ageField.getText();
            String address = addressField.getText();

            // Ajoutez cette vérification pour éviter la ClassCastException
            if (ageText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez saisir un âge valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortez de la méthode si l'âge n'est pas valide
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Veuillez saisir un âge valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortez de la méthode si l'âge n'est pas un nombre valide
            }

            // Ajoutez des vérifications similaires pour le nom et l'adresse si nécessaire

            try {
                remoteObject.updatePerson(id, age, name, address);
                refreshTable(); // Rechargez les données après la modification
                clearFields(); // Effacez également les champs du formulaire après la modification
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }




    private void refreshTable() {
        try {
            // Appel de la méthode distante à l'aide de l'objet obtenu
            List<Person> liste = remoteObject.getPersones();

            // Nettoyer la table avant de l'actualiser
            tableModel.setRowCount(0);

            // Remplir la table avec les données
            for (Person p : liste) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getAddress()});
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
