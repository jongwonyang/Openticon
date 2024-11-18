package io.ssafy.openticon.dto;

import io.ssafy.openticon.controller.request.ImageCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GpuRequest {
    private String prompt;
    private int seed;
    private int num_inference_steps;
    private int guidance_scale;
}
