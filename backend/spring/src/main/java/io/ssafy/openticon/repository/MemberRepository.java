package io.ssafy.openticon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.ssafy.openticon.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    Optional<MemberEntity> findMemberByEmail(String username);

    boolean existsMemberByEmail(String email);

}
