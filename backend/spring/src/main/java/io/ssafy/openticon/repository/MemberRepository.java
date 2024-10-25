package io.ssafy.openticon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.ssafy.openticon.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findMemberByEmail(String username);

    boolean existsMemberByEmail(String email);

}
