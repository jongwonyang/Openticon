package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.EmoticonEntity;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.repository.EmoticonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
