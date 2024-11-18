package io.ssafy.openticon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
public class EmoticonFileAndName {
    File file;
    String url;
}
