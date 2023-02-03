package model;

public class Login {
    private String email;
    private String password;

    public Login() {

    }
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public static Login from(User user) {
        return new Login(user.getEmail(), user.getPassword());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
