package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HashTagChiTiet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTagChiTiet {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_GIAY")
    private Giay giay;

    @ManyToOne
    @JoinColumn(name = "ID_HASH_TAG")
    private HashTag hashTag;

}
