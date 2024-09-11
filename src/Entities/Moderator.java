package Entities;

public class Moderator extends User {
    private String permission;

    public Moderator() {
        super();
    }

    public Moderator(Long id, String name, String email, String phone, String password, String permission) {
        super(id, name, email, phone, password);
        this.permission = permission;
    }

    public Moderator(long id, User user, String permission) {
    }

    public Moderator(long moderatorId, long userId, String name, String email, String phone, String password, String permission) {
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


}
