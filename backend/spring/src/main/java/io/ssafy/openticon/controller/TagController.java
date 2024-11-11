package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.SwitchViewRequestDto;
import io.ssafy.openticon.controller.request.TagRequestDto;
import io.ssafy.openticon.controller.response.PurchaseEmoticonResponseDto;
import io.ssafy.openticon.controller.response.TagResponseDto;
import io.ssafy.openticon.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("list")
    @Operation(summary = "태그 3개")
    public ResponseEntity<TagResponseDto> getTags() {
        TagResponseDto tags = new TagResponseDto(tagService.getTags());
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    @PostMapping("/insert")
    @Operation(summary = "태그 3개 추가")
    public ResponseEntity<TagResponseDto> insertTags(@RequestBody TagRequestDto dto){
        tagService.updateTags(dto.getTags());
        TagResponseDto tags = new TagResponseDto(tagService.getTags());
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }
}
