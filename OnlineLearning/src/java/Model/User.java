package Model;

public class User {
    private int UserID;
    private String Username;
    private String Password;
    private String Name;
    private String Gender;
    private String Phone;
    private String Email;
    private String Address;
    private String Avatar;
    private String Role;
    private String Status;
    private String Token;

    public User() {
    }

    
    public User(int UserID, String Username, String Password, String Name, String Gender, String Phone, String Email, String Address, String Avatar, String Role, String Status, String Token) {
        this.UserID = UserID;
        this.Username = Username;
        this.Password = Password;
        this.Name = Name;
        this.Gender = Gender;
        this.Phone = Phone;
        this.Email = Email;
        this.Address = Address;
        this.Avatar = Avatar;
        this.Role = Role;
        this.Status = Status;
        this.Token = Token;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

}
