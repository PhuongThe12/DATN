package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Districts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Districts {
    @Id
    @Column(name = "ID", nullable = false, length = 20)
    private String id;

    @Column(name = "TEN", nullable = false)
    private String ten;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "code_name")
    private String codeName;

    @ManyToOne
    @JoinColumn(name = "PROVINCE_ID")
    private Provinces provinces;


}