package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.repositories.UserRepository;

import java.io.Serializable;
import java.util.*;

public class Community implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String manager;
    private ArrayList<String> members;
    private Queue<String> messages;

    public Community(String name, String description, String manager) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.members = new ArrayList<>();
        this.members.add(manager); // O gerente é automaticamente um membro
        this.messages = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Set<String> getMembers() {
        return new LinkedHashSet<>(members);
    }

    public int getMemberCount() {
        return members.size();
    }

    public boolean addMember(String login) {
        if (!members.contains(login)) {
            members.add(login);
            return true;
        }
        return false;
    }

    public boolean removeMember(String login) {
        if (login.equals(manager)) {
            return false; // Cannot remove the manager
        }
        return members.remove(login);
    }

    public boolean isMember(String login) {
        return members.contains(login);
    }

    public boolean isManager(String login) {
        return manager.equals(login);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return "Community{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manager='" + manager + '\'' +
                ", members=" + members +
                '}';
    }
}
