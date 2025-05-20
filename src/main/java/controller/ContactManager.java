package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Personne;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private static final String FILE_NAME = "contacts.json";
    private List<Personne> contacts;

    public ContactManager() {
        contacts = loadContacts();
    }

    public List<Personne> getContacts() {
        return contacts;
    }

    public void addContact(Personne personne) {
        contacts.add(personne);
        saveContacts();
    }

    public void removeContact(Personne personne) {
        contacts.remove(personne);
        saveContacts();
    }

    public void saveContacts() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            new Gson().toJson(contacts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Personne> loadContacts() {
    try (Reader reader = new FileReader(FILE_NAME)) {
        Type listType = new TypeToken<ArrayList<Personne>>() {}.getType();
        List<Personne> loadedContacts = new Gson().fromJson(reader, listType);
        
        if (loadedContacts == null) {
            return new ArrayList<>();
        }
        
        // Protection contre les valeurs null
        for (Personne p : loadedContacts) {
            if (p.getNom() == null) p.setNom("");
            if (p.getPrenom() == null) p.setPrenom("");
            if (p.getEmail() == null) p.setEmail("");
            if (p.getTelephone() == null) p.setTelephone("");
        }
        
        return loadedContacts;
    } catch (IOException e) {
        return new ArrayList<>();
    }
}
 
    // Dans ContactManager.java
public void exportContacts(String filePath, String format) throws IOException {
    try (Writer writer = new FileWriter(filePath)) {
        if (format.equalsIgnoreCase("json")) {
            new Gson().toJson(contacts, writer);
        } else if (format.equalsIgnoreCase("txt")) {
            for (Personne contact : contacts) {
                writer.write(String.format(
                    "Nom: %s, Prénom: %s, Email: %s, Téléphone: %s\n",
                    contact.getNom(),
                    contact.getPrenom(),
                    contact.getEmail(),
                    contact.getTelephone()
                ));
            }
        }
    }
}

public void importContacts(String filePath) throws IOException {
    if (filePath.toLowerCase().endsWith(".json")) {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Personne>>(){}.getType();
            List<Personne> importedContacts = new Gson().fromJson(reader, listType);
            if (importedContacts != null) {
                contacts = importedContacts;
                saveContacts();
            }
        }
    }
}

public void clearAllContacts() {
    contacts.clear();
    saveContacts();
}
}
