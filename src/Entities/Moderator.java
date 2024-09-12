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

    public Moderator(User user, String permission) {
        super(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword());
        this.permission = permission;
    }

    public Moderator(long id, long userId, String permission) {
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}
