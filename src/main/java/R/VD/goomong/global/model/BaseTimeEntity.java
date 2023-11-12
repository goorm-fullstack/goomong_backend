package R.VD.goomong.global.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @Column
    private ZonedDateTime regDate;

    @Column
    private ZonedDateTime chgDate;

    @PrePersist
    public void prePersist() {
        this.regDate = ZonedDateTime.now();
        this.chgDate = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.chgDate = ZonedDateTime.now();
    }

    /**
     *
     * DateTimeFormatter dateTimeFormatter
     * = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.XXX");
     *
     * ZonedDateTime.format(dateTimeFormatter);
     *
     */
}
