package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "HUA_CONTACT")
@Getter
@Setter
@NoArgsConstructor
public class HuaContact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(name = "work_number", length = 20)
    private String workNumber;

    @Column(name = "home_number", length = 20)
    private String homeNumber;

    @Column(name = "business_email", nullable = false, length = 35, unique = true)
    private String businessEmail;

    @Column(name = "personal_email", nullable = false, length = 35, unique = true)
    private String personalEmail;

    @OneToOne(mappedBy = "contact")
    private HuaUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaContact that = (HuaContact) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
