package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.TagEntity;
import io.ssafy.openticon.entity.TagListEntity;
import io.ssafy.openticon.repository.TagListRepository;
import io.ssafy.openticon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagListRepository tagListRepository;

    // TODO 1. 새로운 태그가 있다면 태그 테이블에 생성
    @Transactional
    public void addTag(String tagName){
        Optional<TagEntity> tagEntity = tagRepository.findByTagName(tagName);
        if(tagEntity.isEmpty()){
            TagEntity saveTagEntity = TagEntity.builder()
                    .tagName(tagName)
                    .build();
            tagRepository.save(saveTagEntity);
        }
    }

    // TODO 2. 태그 리스트에 이모티콘 팩 아이디와 태그 아이디를 저장
    public void saveTagListEntry(){

    }

    // TODO 3. 태그 리스트에서 이모티콘 팩 아이디로 조회
    public void findTagListsByEmoticonPackId(){

    }

    // TODO 4. 태그 리스트에서 태그 이름으로 조회
    public List<TagListEntity> findTagListsByTagName(String tagName){
        return tagListRepository.findTagListsByTagName(tagName);
    }
}
