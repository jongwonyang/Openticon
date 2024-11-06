package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.response.FavoritesResponseDto;
import io.ssafy.openticon.entity.FavoritesEntity;
import io.ssafy.openticon.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final EmoticonService emoticonService;

    public FavoriteService(FavoriteRepository favoriteRepository, EmoticonService emoticonService) {
        this.favoriteRepository = favoriteRepository;
        this.emoticonService = emoticonService;
    }

    public void add(Long memberId, Long emoticonId){
        FavoritesEntity favoritesEntity=FavoritesEntity.builder()
                .memberId(memberId)
                .emoticonId(emoticonId)
                .build();

        if(favoriteRepository.findByMemberIdAndEmoticonId(memberId,emoticonId).isPresent()){
            throw new IllegalArgumentException("이미 즐겨찾기에 추가함.");
        }
        favoriteRepository.save(favoritesEntity);
    }

    public List<FavoritesResponseDto> view(Long memberId){
        List<FavoritesEntity> favoritesEntities= favoriteRepository.findByMemberId(memberId);

        List<FavoritesResponseDto> result=new ArrayList<>();
        for(FavoritesEntity favoritesEntity:favoritesEntities){
            result.add(new FavoritesResponseDto(favoritesEntity.getId(),emoticonService.getEmoticon(favoritesEntity.getEmoticonId())));
        }

        return result;
    }
}
