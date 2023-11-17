package R.VD.goomong.member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String AuthToken;                   //UUID
    private Boolean expired;                      //만료 여부
    private LocalDateTime expireDate;           //만료 시간

    @Builder
    public EmailAuth(Long id, String email, String authToken, Boolean expired, LocalDateTime expireDate) {
        this.id = id;
        this.email = email;
        AuthToken = authToken;
        this.expired = expired;
        this.expireDate = expireDate;
    }

    public void useToken() {
        this.expired = true;
    }
}
