package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.TagEntity;
import io.ssafy.openticon.entity.TagListEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.TagListRepository;
import io.ssafy.openticon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    private final TagListRepository tagListRepository;

    private final RedisTemplate<String, List<String>> redisTagTemplate;
    private final String TAG_KEY = "tagList";

    public TagService(TagRepository tagRepository, TagListRepository tagListRepository, @Qualifier("redisTagTemplate")RedisTemplate<String, List<String>> redisTagTemplate){
        this.tagRepository = tagRepository;
        this.tagListRepository = tagListRepository;
        this.redisTagTemplate = redisTagTemplate;
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

    public List<String> initTags(){
        List<TagEntity> tagList = tagRepository.findAll();
        List<String> tags = new ArrayList<>();
        Collections.shuffle(tagList);
        if(tagList.size() < 3){
            for(TagEntity tagEntity: tagList){
                tags.add(tagEntity.getTagName());
            }
        }else{
            for(TagEntity tagEntity : tagList.subList(0, 3)){
                tags.add(tagEntity.getTagName());
            }
        }
        return tags;
    }

    public List<TagListEntity> findTagListsByTagName(String tagName){
        return tagListRepository.findTagListsByTagName(tagName);
    }

    public List<String> getTags() {
        return redisTagTemplate.opsForValue().get(TAG_KEY);
    }

    public void updateTags(List<String> tags) {
        redisTagTemplate.opsForValue().set(TAG_KEY, tags);
    }
}
