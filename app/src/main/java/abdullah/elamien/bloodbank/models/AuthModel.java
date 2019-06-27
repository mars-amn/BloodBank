package abdullah.elamien.bloodbank.models;

public class AuthModel {
    private String name;
    private String email;
    private String password;

    public AuthModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public AuthModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
