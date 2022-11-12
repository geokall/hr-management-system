package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HUA_DIVISION")
@Getter
@Setter
@NoArgsConstructor
public class HuaDivision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "division")
    private HuaUser user;

}
