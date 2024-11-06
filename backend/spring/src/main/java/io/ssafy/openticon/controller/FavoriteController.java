package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.FavoriteDeleteRequestDto;
import io.ssafy.openticon.controller.request.FavoriteRequestDto;
import io.ssafy.openticon.controller.response.FavoritesResponseDto;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.service.FavoriteService;
import io.ssafy.openticon.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<List<FavoritesResponseDto>> viewFavorite(@AuthenticationPrincipal UserDetails userDetails){
        MemberEntity member=memberService.getMemberByEmail(userDetails.getUsername()).orElseThrow();


        return ResponseEntity.status(HttpStatus.OK).body(favoriteService.view(member.getId()));
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody FavoriteDeleteRequestDto favoriteDeleteRequestDto){
        favoriteService.delete(favoriteDeleteRequestDto.getFavoriteId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
