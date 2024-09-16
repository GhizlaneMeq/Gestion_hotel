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

    public Moderator(Long id, String permission) {
        super(id, null, null, null, null);
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Moderator{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
