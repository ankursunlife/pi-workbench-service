package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortingAndPaginationBean {

	@ApiModelProperty(notes = "This is created as a list to make it future ready to support multiple sorting on table. In MVP, API wont be supporting mulitple sorting.")
	private List<SortingBean> sortingBeanList;

	@ApiModelProperty(notes = "Defaulted to 50", example = "50,100")
	private int count = 50;

	@ApiModelProperty(notes = "Page number should start from 0")
	private int pageNumber;

}
