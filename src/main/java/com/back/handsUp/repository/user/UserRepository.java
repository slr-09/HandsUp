package com.back.handsUp.repository.user;

import com.back.handsUp.domain.user.School;
import com.back.handsUp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdx(Long userIdx);

    Optional<User> findByEmailAndStatus(String email, String status);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByNicknameAndSchoolIdx(String nickname, School school);
}
