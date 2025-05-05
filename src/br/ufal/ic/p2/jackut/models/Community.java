package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.repositories.UserRepository;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a community in the Jackut social network.
 * A community is a group of users who share common interests.
 * Each community has a name, description, manager (owner), members, and messages.
 *
 * <p>This class implements Serializable to allow persistence of community data.</p>
 */
public class Community implements Serializable {

    /**
     * Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the community. Acts as a unique identifier.
     */
    private String name;

    /**
     * The description of the community, explaining its purpose or focus.
     */
    private String description;

    /**
     * The login of the user who manages (owns) the community.
     */
    private String manager;

    /**
     * List of logins of users who are members of this community.
     */
    private ArrayList<String> members;

    /**
     * Queue of messages posted to this community.
     */
    private Queue<String> messages;

    /**
     * Creates a new community with the specified name, description, and manager.
     * The manager is automatically added as a member of the community.
     *
     * @param name The name of the community
     * @param description The description of the community
     * @param manager The login of the user who will manage the community
     */
    public Community(String name, String description, String manager) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.members = new ArrayList<>();
        this.members.add(manager); // O gerente é automaticamente um membro
        this.messages = new LinkedList<>();
    }

    /**
     * Gets the name of the community.
     *
     * @return The community name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the community.
     *
     * @return The community description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a new description for the community.
     *
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the login of the community manager.
     *
     * @return The manager's login
     */
    public String getManager() {
        return manager;
    }

    /**
     * Sets a new manager for the community.
     *
     * @param manager The login of the new manager
     */
    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * Gets the set of members in this community.
     * Returns a copy to prevent direct modification of the internal list.
     *
     * @return A set containing the logins of all members
     */
    public Set<String> getMembers() {
        return new LinkedHashSet<>(members);
    }

    /**
     * Gets the number of members in this community.
     *
     * @return The member count
     */
    public int getMemberCount() {
        return members.size();
    }

    /**
     * Adds a user as a member of this community.
     *
     * @param login The login of the user to add
     * @return true if the user was added, false if they were already a member
     */
    public boolean addMember(String login) {
        if (!members.contains(login)) {
            members.add(login);
            return true;
        }
        return false;
    }

    /**
     * Removes a user from this community.
     * The manager cannot be removed from the community.
     *
     * @param login The login of the user to remove
     * @return true if the user was removed, false if they were not a member or are the manager
     */
    public boolean removeMember(String login) {
        if (login.equals(manager)) {
            return false; // Cannot remove the manager
        }
        return members.remove(login);
    }

    /**
     * Checks if a user is a member of this community.
     *
     * @param login The login of the user to check
     * @return true if the user is a member, false otherwise
     */
    public boolean isMember(String login) {
        return members.contains(login);
    }

    /**
     * Checks if a user is the manager of this community.
     *
     * @param login The login of the user to check
     * @return true if the user is the manager, false otherwise
     */
    public boolean isManager(String login) {
        return manager.equals(login);
    }

    /**
     * Adds a message to the community's message queue.
     *
     * @param message The message to add
     */
    public void addMessage(String message) {
        messages.add(message);
    }

    /**
     * Returns a string representation of this community.
     *
     * @return A string containing the community's name, description, manager, and members
     */
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
