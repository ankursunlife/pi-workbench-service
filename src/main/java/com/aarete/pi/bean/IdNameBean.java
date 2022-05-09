package com.aarete.pi.bean;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdNameBean {

	private String id;

	@NotBlank(message = "Name is mandatory")
    private String name;

    private String desc;

    @JsonProperty("isSelected")
    private boolean isSelected;

    public IdNameBean(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }
}
