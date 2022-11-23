package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "HUA_MANAGER")
@Getter
@Setter
@NoArgsConstructor
public class HuaManager extends HuaUser {

    @ManyToMany(mappedBy = "managers")
    private Set<HuaUser> users = new HashSet<>();

    @OneToOne(mappedBy = "manager")
    private HuaJobInformation jobInformation;

}
