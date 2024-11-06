package io.ssafy.openticon.controller;

import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.service.FavoriteService;
import io.ssafy.openticon.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @PostMapping("")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody FavoriteRequestDto favoriteRequestDto){
        MemberEntity member=memberService.getMemberByEmail(userDetails.getUsername()).orElseThrow();

        favoriteService.add(member.getId(),favoriteRequestDto.getEmoticonId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
