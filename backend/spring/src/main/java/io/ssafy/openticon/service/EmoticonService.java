package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.EmoticonEntity;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.repository.EmoticonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class EmoticonService {

    private final EmoticonRepository emoticonRepository;

    public void saveEmoticons(List<String> emoticons, EmoticonPackEntity emoticonPackEntity){

        for(int i=0; i<emoticons.size();i++){
            EmoticonEntity emoticonEntity=new EmoticonEntity(emoticonPackEntity,i, emoticons.get(i));
            emoticonRepository.save(emoticonEntity);
        }
    }

    public List<String> getEmoticons(Long packId){

        List<EmoticonEntity> emoticonEntities=emoticonRepository.getEmoticonEntitiesByEmoticonPackId(packId);
        Collections.sort(emoticonEntities);

        List<String> result=new ArrayList<>();
        for(EmoticonEntity emoticonEntity:emoticonEntities){
            result.add(emoticonEntity.getImagePath());
        }
        return result;
    }

    public String getEmoticon(EmoticonPackEntity emoticonPack, Integer order){
        return emoticonRepository.getEmoticonEntityByEmoticonPackAndEmoticonOrder(emoticonPack,order).getImagePath();
    }

    public String getEmoticon(Long emoticonId){
        return emoticonRepository.getEmoticonEntityById(emoticonId).getImagePath();
    }
}
