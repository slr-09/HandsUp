package com.back.handsUp.repository.user;

import com.back.handsUp.domain.user.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByCharacterIdx(Long characterIdx);


}
