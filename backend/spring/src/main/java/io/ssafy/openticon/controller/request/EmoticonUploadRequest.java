package io.ssafy.openticon.controller.request;

import io.ssafy.openticon.dto.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class EmoticonUploadRequest {

    private String packTitle;

    private Category category;

    private String description;

    private int price;

    private List<String> tags;

    private MultipartFile thumbnail;

    private MultipartFile thumbnail_list;

    private List<MultipartFile> emoticons;
}
