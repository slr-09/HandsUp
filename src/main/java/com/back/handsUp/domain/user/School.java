package com.back.handsUp.domain.user;

import com.back.handsUp.baseResponse.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "school")
@NoArgsConstructor
@DynamicInsert
public class School extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolIdx;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private String status;

    @Builder
    public School(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public void changeStatus (String newStatus) {
        this.status = newStatus;
    }
}

