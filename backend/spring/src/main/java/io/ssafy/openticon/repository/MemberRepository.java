package io.ssafy.openticon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.ssafy.openticon.entity.MemberEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    @Query("SELECT m FROM MemberEntity m WHERE m.email = :username AND m.isResigned = false")
    Optional<MemberEntity> findMemberByEmail(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MemberEntity m WHERE m.email = :email AND m.isResigned = false")
    boolean existsMemberByEmail(@Param("email") String email);

    boolean existsMemberByNickname(String nickname);

}
