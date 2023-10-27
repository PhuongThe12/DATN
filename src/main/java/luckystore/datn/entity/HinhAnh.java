package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HinhAnh")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HinhAnh {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LINK")
    private String link;

    @ManyToOne
    @JoinColumn(name = "ID_GIAY")
    @JsonBackReference
    private Giay giay;

    @Column(name = "UU_TIEN")
    private Integer uuTien;

}
