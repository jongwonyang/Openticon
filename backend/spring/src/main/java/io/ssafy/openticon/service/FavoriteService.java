package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.FavoritesEntity;
import io.ssafy.openticon.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
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
}
