package uz.example.flower.model.entity;

import uz.example.flower.model.base.BaseEntity;
import uz.example.flower.model.enums.Gender;
import uz.example.flower.payload.response.UserDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private Integer emailCode;
    private String firstname;
    private String lastname;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.MALE;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(Integer emailCode) {
        this.emailCode = emailCode;
    }

    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(getId());
        userDto.setUsername(getUsername());
        userDto.setFirstname(getFirstname());
        userDto.setLastname(getLastname());
        userDto.setEmail(getEmail());
        return userDto;
    }
}
