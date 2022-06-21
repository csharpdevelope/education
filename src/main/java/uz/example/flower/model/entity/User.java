package uz.example.flower.model.entity;

import lombok.Getter;
import lombok.Setter;
import uz.example.flower.model.base.BaseEntity;
import uz.example.flower.model.enums.Gender;
import uz.example.flower.payload.response.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String firstname;
    private String lastname;

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
        List<String> roles = new ArrayList<>();
        for (Role role: getRoles()) {
            roles.add(role.getName().name());
        }
        userDto.setRole(roles.get(0));
        return userDto;
    }
}
