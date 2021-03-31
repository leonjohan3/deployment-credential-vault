package org.dcv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

//@Value
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingError {

    @NotBlank
    String errorMessage;
}
