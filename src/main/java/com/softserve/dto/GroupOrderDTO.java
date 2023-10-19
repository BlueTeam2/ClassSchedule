package com.softserve.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupOrderDTO extends GroupDTO {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long afterId;
}
