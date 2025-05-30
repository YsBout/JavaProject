
package view;
import controller.ContactManager;
import model.Personne;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FRAME extends javax.swing.JFrame {
     private ContactManager contactManager;
    private DefaultListModel<String> contactsListModel;
    private JList<String> contactsList;
    private List<Personne> filteredContacts = new ArrayList<>();
private boolean isFiltering = false;

    
    public FRAME() {
        initComponents();
        contactManager = new ContactManager();
        initContactsList();
         jPanel2.setLayout(new BorderLayout());
        showHomeView();
        
    }
  private void initContactsList() {
        contactsListModel = new DefaultListModel<>();
        contactsList = new JList<>(contactsListModel);
        contactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showContactDetails(contactsList.getSelectedIndex());
            }
        });
        jScrollPane1.setViewportView(contactsList);
        refreshContactsList();
    }
 private void refreshContactsList() {
    contactsListModel.clear();
    List<Personne> contacts = contactManager.getContacts();
    
    // Tri sécurisé avec gestion des nulls
    contacts.sort(Comparator.comparing(
        (Personne p) -> p.getNom().toLowerCase(),
        Comparator.nullsFirst(Comparator.naturalOrder())
    ).thenComparing(
        (Personne p) -> p.getPrenom().toLowerCase(),
        Comparator.nullsFirst(Comparator.naturalOrder())
    ));
    
    for (Personne contact : contacts) {
        String displayName = contact.getNom();
        if (!contact.getPrenom().isEmpty()) {
            displayName += ", " + contact.getPrenom();
        }
        contactsListModel.addElement(displayName);
    }
}
private void showHomeView() {
    jPanel2.removeAll();
    jPanel2.setLayout(new BorderLayout());

    // Panel central pour contenir les labels, avec BoxLayout vertical
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Centrer horizontalement les labels
    JLabel welcomeLabel = new JLabel("Bienvenue dans votre application");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
    welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // centrage horizontal

    JLabel countLabel = new JLabel("Nombre de contacts: " + contactManager.getContacts().size());
    countLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // centrage horizontal

    // Ajouter un espace vertical entre les labels
    contentPanel.add(Box.createVerticalGlue());  // pousse vers le centre verticalement
    contentPanel.add(welcomeLabel);
    contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));  // espace vertical
    contentPanel.add(countLabel);
    contentPanel.add(Box.createVerticalGlue());  // pousse vers le centre verticalement

    jPanel2.add(contentPanel, BorderLayout.CENTER);

    jPanel2.revalidate();
    jPanel2.repaint();
}

private void showCreateContactView() {
    jPanel2.removeAll();
    jPanel2.setLayout(new BorderLayout());
    jPanel2.putClientProperty("FlatLaf.style", "background:transparent");
    jPanel2.setOpaque(false);

    // Police plus grande pour tout le contenu
    Font labelFont = new Font("Arial", Font.PLAIN, 18);
    Font fieldFont = new Font("Arial", Font.PLAIN, 18);
    Font buttonFont = new Font("Arial", Font.BOLD, 18);

    // Panel formulaire (labels + champs)
    JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    formPanel.setOpaque(false);

    JLabel nomLabel = new JLabel("Nom*:");
    nomLabel.setFont(labelFont);
    JTextField nomField = new JTextField();
    nomField.setFont(fieldFont);

    JLabel prenomLabel = new JLabel("Prénom:");
    prenomLabel.setFont(labelFont);
    JTextField prenomField = new JTextField();
    prenomField.setFont(fieldFont);

    JLabel emailLabel = new JLabel("Email:");
    emailLabel.setFont(labelFont);
    JTextField emailField = new JTextField();
    emailField.setFont(fieldFont);

    JLabel telephoneLabel = new JLabel("Téléphone*:");
    telephoneLabel.setFont(labelFont);
    JTextField telephoneField = new JTextField();
    telephoneField.setFont(fieldFont);

    formPanel.add(nomLabel);
    formPanel.add(nomField);
    formPanel.add(prenomLabel);
    formPanel.add(prenomField);
    formPanel.add(emailLabel);
    formPanel.add(emailField);
    formPanel.add(telephoneLabel);
    formPanel.add(telephoneField);

    // Panel boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
    buttonPanel.setOpaque(false);

    JButton saveButton = new JButton("Enregistrer");
    saveButton.setFont(buttonFont);
    saveButton.setBackground(new Color(76, 175, 80)); // vert
    saveButton.setForeground(Color.WHITE);
    saveButton.setFocusPainted(false);
    saveButton.addActionListener(e -> {
        String nom = nomField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || telephone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom et le téléphone sont obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Personne newContact = new Personne(
            nom,
            prenomField.getText().trim(),
            emailField.getText().trim(),
            telephone,
            null
        );

        contactManager.addContact(newContact);
        refreshContactsList();
        showHomeView();
    });

    JButton cancelButton = new JButton("Annuler");
    cancelButton.setFont(buttonFont);
    cancelButton.setBackground(new Color(244, 67, 54)); // rouge
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setFocusPainted(false);
    cancelButton.addActionListener(e -> showHomeView());

    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    // Ajout des panels dans jPanel2
    jPanel2.add(formPanel, BorderLayout.CENTER);
    jPanel2.add(buttonPanel, BorderLayout.SOUTH);

    jPanel2.revalidate();
    jPanel2.repaint();
}


   private void showContactDetails(int index) {
    if (index < 0 || index >= contactManager.getContacts().size()) return;

 Personne contact;
 if(isFiltering && index <filteredContacts.size()){
     contact = filteredContacts.get(index);
}else{
     contact = contactManager.getContacts().get(index);
     
 }
 

    
    jPanel2.removeAll();
    jPanel2.setLayout(new BorderLayout(10, 10));
    jPanel2.putClientProperty("FlatLaf.style", "background:transparent");
    jPanel2.setOpaque(false);

    Font labelFont = new Font("Arial", Font.PLAIN, 18);
    Font buttonFont = new Font("Arial", Font.BOLD, 18);

    // Panel pour les détails
    JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
    detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    detailsPanel.setOpaque(false);

    JLabel nomLabel = new JLabel("Nom: " + contact.getNom());
    nomLabel.setFont(labelFont);
    JLabel prenomLabel = new JLabel("Prénom: " + contact.getPrenom());
    prenomLabel.setFont(labelFont);
    JLabel emailLabel = new JLabel("Email: " + contact.getEmail());
    emailLabel.setFont(labelFont);
    JLabel telephoneLabel = new JLabel("Téléphone: " + contact.getTelephone());
    telephoneLabel.setFont(labelFont);

    detailsPanel.add(nomLabel);
    detailsPanel.add(prenomLabel);
    detailsPanel.add(emailLabel);
    detailsPanel.add(telephoneLabel);

    // Panel pour les boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    buttonPanel.setOpaque(false);

    // Bouton Modifier
    JButton editButton = new JButton("Modifier");
    editButton.setFont(buttonFont);
    editButton.setBackground(new Color(76, 175, 80)); // vert
    editButton.setForeground(Color.WHITE);
    editButton.setFocusPainted(false);
    editButton.addActionListener(e -> showEditContactView(index));

    // Bouton Supprimer
    JButton deleteButton = new JButton("Supprimer");
    deleteButton.setFont(buttonFont);
    deleteButton.setBackground(new Color(244, 67, 54)); // rouge
    deleteButton.setForeground(Color.WHITE);
    deleteButton.setFocusPainted(false);
    deleteButton.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Voulez-vous vraiment supprimer ce contact?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            contactManager.removeContact(contact);
            refreshContactsList();
            showHomeView();
        }
    });

    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    jPanel2.add(detailsPanel, BorderLayout.CENTER);
    jPanel2.add(buttonPanel, BorderLayout.SOUTH);

    jPanel2.revalidate();
    jPanel2.repaint();
}

    
    
    
    private void showEditContactView(int index) {
    if (index < 0 || index >= contactManager.getContacts().size()) return;

Personne contact;
 if(isFiltering && index <filteredContacts.size()){
     contact = filteredContacts.get(index);
}else{
     contact = contactManager.getContacts().get(index);
     
 }
 jPanel2.removeAll();
    jPanel2.setLayout(new GridLayout(6, 2, 10, 10));
    jPanel2.putClientProperty("FlatLaf.style", "background:transparent");
    jPanel2.setOpaque(false);

    Font labelFont = new Font("Arial", Font.PLAIN, 18);
    Font fieldFont = new Font("Arial", Font.PLAIN, 16);
    Font buttonFont = new Font("Arial", Font.BOLD, 18);

    JLabel nomLabel = new JLabel("Nom*:");
    nomLabel.setFont(labelFont);
    JTextField nomField = new JTextField(contact.getNom());
    nomField.setFont(fieldFont);

    JLabel prenomLabel = new JLabel("Prénom:");
    prenomLabel.setFont(labelFont);
    JTextField prenomField = new JTextField(contact.getPrenom());
    prenomField.setFont(fieldFont);

    JLabel emailLabel = new JLabel("Email:");
    emailLabel.setFont(labelFont);
    JTextField emailField = new JTextField(contact.getEmail());
    emailField.setFont(fieldFont);

    JLabel telephoneLabel = new JLabel("Téléphone*:");
    telephoneLabel.setFont(labelFont);
    JTextField telephoneField = new JTextField(contact.getTelephone());
    telephoneField.setFont(fieldFont);

    JButton saveButton = new JButton("Enregistrer");
    saveButton.setFont(buttonFont);
    saveButton.setBackground(new Color(76, 175, 80)); // vert
    saveButton.setForeground(Color.WHITE);
    saveButton.setFocusPainted(false);
    saveButton.addActionListener(e -> {
        String nom = nomField.getText().trim();
        String telephone = telephoneField.getText().trim();

        if (nom.isEmpty() || telephone.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Le nom et le téléphone sont obligatoires", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Mise à jour du contact
        contact.setNom(nom);
        contact.setPrenom(prenomField.getText().trim());
        contact.setEmail(emailField.getText().trim());
        contact.setTelephone(telephone);

        contactManager.saveContacts(); // Sauvegarde les modifications
        refreshContactsList();
        showContactDetails(index); // Recharge la vue détaillée
    });

    JButton cancelButton = new JButton("Annuler");
    cancelButton.setFont(buttonFont);
    cancelButton.setBackground(new Color(200, 200, 200)); // gris clair
    cancelButton.setForeground(Color.BLACK);
    cancelButton.setFocusPainted(false);
    cancelButton.addActionListener(e -> showContactDetails(index));

    jPanel2.add(nomLabel);
    jPanel2.add(nomField);
    jPanel2.add(prenomLabel);
    jPanel2.add(prenomField);
    jPanel2.add(emailLabel);
    jPanel2.add(emailField);
    jPanel2.add(telephoneLabel);
    jPanel2.add(telephoneField);
    jPanel2.add(new JLabel()); // espace vide
    jPanel2.add(saveButton);
    jPanel2.add(new JLabel()); // espace vide
    jPanel2.add(cancelButton);

    jPanel2.revalidate();
    jPanel2.repaint();
}


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion Contact");
        setBackground(new java.awt.Color(204, 204, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204)));
        jPanel1.setForeground(new java.awt.Color(0, 51, 204));

        jTextField1.setToolTipText("");
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Search.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Contact list :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addContainerGap(12, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1))
        );

        jTextField1.getAccessibleContext().setAccessibleName("");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(51, 0, 153));
        jPanel3.setForeground(new java.awt.Color(153, 153, 153));

        jButton1.setBackground(new java.awt.Color(102, 0, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Create new conatct");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Settings");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Home.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Settings.png"))); // NOI18N

        jButton3.setBackground(new java.awt.Color(102, 51, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Home");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(21, 21, 21)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jLabel4)
                    .addComponent(jButton1)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(162, 162, 162)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
     String searchText = jTextField1.getText().toLowerCase();
    filteredContacts.clear();
    isFiltering = false;

    if (searchText.isEmpty()) {
        refreshContactsList(); // Affiche tous les contacts
        return;
    }

    contactsListModel.clear();
    for (Personne contact : contactManager.getContacts()) {
        String displayName = contact.getNom() + (contact.getPrenom().isEmpty() ? "" : ", " + contact.getPrenom());
        if (displayName.toLowerCase().contains(searchText)) {
            filteredContacts.add(contact);
            contactsListModel.addElement(displayName);
        }
    }

    isFiltering = true;
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         showCreateContactView();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
            jPanel2.removeAll();
    jPanel2.setLayout(new BorderLayout());
        JPanel settingsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
    settingsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Bouton Export JSON
    JButton exportJsonBtn = new JButton("Exporter en JSON");
    exportJsonBtn.addActionListener(e -> exportContacts("json"));
    
    // Bouton Export TXT
    JButton exportTxtBtn = new JButton("Exporter en TXT");
    exportTxtBtn.addActionListener(e -> exportContacts("txt"));
    
    // Bouton Import
    JButton importBtn = new JButton("Importer des contacts");
    importBtn.addActionListener(e -> importContacts());
    
    // Bouton Formatage
    JButton clearAllBtn = new JButton("Supprimer tous les contacts");
    clearAllBtn.addActionListener(e -> clearAllContacts());
    
    settingsPanel.add(exportJsonBtn);
    settingsPanel.add(exportTxtBtn);
    settingsPanel.add(importBtn);
    settingsPanel.add(clearAllBtn);

    jPanel2.removeAll();
    jPanel2.add(settingsPanel, BorderLayout.CENTER);
    jPanel2.revalidate();
    jPanel2.repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       showHomeView();
    }//GEN-LAST:event_jButton3ActionPerformed

    
  
    private void exportContacts(String format) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Exporter les contacts");
    fileChooser.setFileFilter(new FileNameExtensionFilter(
        format.toUpperCase() + " Files", 
        format.toLowerCase()
    ));
    
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String filePath = file.getAbsolutePath();
        
        // Ajouter l'extension si elle n'est pas présente
        if (!filePath.toLowerCase().endsWith("." + format)) {
            filePath += "." + format;
        }
        
        try {
            contactManager.exportContacts(filePath, format);
            JOptionPane.showMessageDialog(this, 
                "Exportation réussie!", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de l'export: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

private void importContacts() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Importer des contacts");
    fileChooser.setFileFilter(new FileNameExtensionFilter(
        "JSON Files", 
        "json"
    ));
    
    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous remplacer vos contacts existants?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                contactManager.importContacts(file.getAbsolutePath());
                refreshContactsList();
                JOptionPane.showMessageDialog(this, 
                    "Importation réussie!", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de l'import: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

private void clearAllContacts() {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Voulez-vous vraiment supprimer tous les contacts?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );
    
    if (confirm == JOptionPane.YES_OPTION) {
        contactManager.clearAllContacts();
        refreshContactsList();
        showHomeView();
        JOptionPane.showMessageDialog(this, 
            "Tous les contacts ont été supprimés", 
            "Succès", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
