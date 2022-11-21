package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "HUA_BONUS")
@Getter
@Setter
@NoArgsConstructor
public class HuaBonus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bonus_date")
    private LocalDate bonusDate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private HuaUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaBonus huaBonus = (HuaBonus) o;
        return Objects.equals(id, huaBonus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
