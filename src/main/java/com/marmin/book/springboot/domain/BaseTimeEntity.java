package com.marmin.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass//JPA Entity클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식하도록 합니다
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {// 모든 엔티티의 상위 클래스가 되어 Entity들의 createDate,modifiedDate를 자동으로 관리하는 역할

    @CreatedDate//Entity가 생성되어 저장될때 시간이 자동 저장됩니다
    private LocalDateTime createDate;

    @LastModifiedDate//조회한 Entity의 값을 변경할때 자동으로 시간이 저장된다
    private LocalDateTime modifiedDate;

}
