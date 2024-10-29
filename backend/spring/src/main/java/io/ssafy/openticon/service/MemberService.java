package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<MemberEntity> getMemberByEmail(String email){
        return memberRepository.findMemberByEmail(email);
    }
}
