package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.TagEntity;
import io.ssafy.openticon.entity.TagListEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.TagListRepository;
import io.ssafy.openticon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    private final TagListRepository tagListRepository;

    public TagService(TagRepository tagRepository, TagListRepository tagListRepository){
        this.tagRepository = tagRepository;
        this.tagListRepository = tagListRepository;
    }

    @Transactional
    public void addTag(String tagName){
        Optional<TagEntity> tagEntity = tagRepository.findByTagName(tagName);
        if(tagEntity.isEmpty()){
            TagEntity saveTagEntity = TagEntity.builder()
                    .tagName(tagName)
                    .build();
            try{
                tagRepository.save(saveTagEntity);
            }catch(DataIntegrityViolationException e){
                throw new OpenticonException(ErrorCode.DUPLICATE_TAG_NAME);
            }
        }
    }

    public List<TagListEntity> findTagListsByTagName(String tagName){
        return tagListRepository.findTagListsByTagName(tagName);
    }
}
