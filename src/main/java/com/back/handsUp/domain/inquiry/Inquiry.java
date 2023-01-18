package com.back.handsUp.domain.inquiry;

import com.back.handsUp.baseResponse.BaseEntity;
import com.back.handsUp.domain.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "inquiry")
@NoArgsConstructor
@DynamicInsert
public class Inquiry extends BaseEntity {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryIdx;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User user;

    @Builder
    public Inquiry(Long inquiryIdx, String contents, String status, User user) {
        this.inquiryIdx = inquiryIdx;
        this.contents = contents;
        this.status = status;
        this.user = user;
    }
}