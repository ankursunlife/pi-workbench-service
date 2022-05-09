package com.aarete.pi.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDTO {

    private String nameId;

    private String nameValue;

    private String columnOneValue;

    private String columnTwoValue;

}
