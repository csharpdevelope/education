package uz.example.flower.model.entity;

import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;
import uz.example.flower.model.enums.Gender;
import uz.example.flower.payload.response.UserDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
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
    private String profileImg;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();


    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(getId());
        userDto.setUsername(getUsername());
        userDto.setFirstname(getFirstname());
        userDto.setLastname(getLastname());
        userDto.setEmail(getEmail());
        userDto.setUrl(getProfileImg());
        return userDto;
    }
}
