package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
