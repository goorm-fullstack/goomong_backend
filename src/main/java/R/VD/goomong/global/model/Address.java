package R.VD.goomong.global.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;// ex) 경기도, 경상북도
    private String city;// ex) 수원시, 성남시
    private String street;// ex) 도로명 주소
    private String detail;// ex) 건물 이름, 호실 정보
}
