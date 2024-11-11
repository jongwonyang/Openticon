package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.controller.request.TagRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter

public class TagResponseDto {
    private List<String> tags;

    public TagResponseDto(List<String> tags){
        this.tags = tags;
    }

    public TagResponseDto(TagRequestDto dto){
        this.tags = dto.getTags();
    }
}
