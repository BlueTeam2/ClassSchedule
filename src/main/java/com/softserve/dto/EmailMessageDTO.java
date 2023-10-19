package com.softserve.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EmailMessageDTO {
    private String subject;
    private String text;
    private List<MultipartFile> attachments;
    private List<String> receivers;
}
