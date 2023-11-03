package R.VD.goomong.global.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String state;// ex) 경기도, 경상북도
    private String city;// ex) 수원시, 성남시
    private String street;// ex) 도로명 주소
    private String detail;// ex) 건물 이름, 호실 정보

    protected Address() {

    }

    public Address(String state, String city, String street, String detail) {
        this.state = state;
        this.city = city;
        this.street = street;
        this.detail = detail;
    }
}
