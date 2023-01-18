package com.back.handsUp.repository.inquiry;

import com.back.handsUp.domain.inquiry.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
