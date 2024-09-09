package Entities;

public class Moderator {
    private Long id;
    private User user;
    private String permission;

    public Moderator() {
    }

    public Moderator(Long id, User user, String permission) {
        this.id = id;
        this.user = user;
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
