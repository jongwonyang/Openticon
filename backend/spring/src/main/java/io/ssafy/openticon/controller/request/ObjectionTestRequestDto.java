package io.ssafy.openticon.controller.request;

import io.ssafy.openticon.dto.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectionTestRequestDto {
    private Long emoticonPackId;
    private ReportType reportType = ReportType.EXAMINE;
}
