package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Provinces")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Provinces {
    @Id
    @Column(name = "ID", nullable = false, length = 20)
    private String id;

    @Column(name = "TEN", nullable = false)
    private String ten;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "code_name")
    private String codeName;

}