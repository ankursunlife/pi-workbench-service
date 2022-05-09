package com.aarete.pi.entity;

import com.aarete.pi.bean.IdNameBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "state")
public class StateEntity {

    @Id
    @Column(name = "state_id")
    private String id;

    @Column(name = "state_name")
    private String name;

    @Column(name = "state_desc")
    private String desc;

    public static StateEntity getInstance(IdNameBean masterTableRequest) {
        StateEntity entity = new StateEntity();
        BeanUtils.copyProperties(masterTableRequest, entity);
        return entity;
    }

    public static IdNameBean getBean(StateEntity entity) {
        IdNameBean bean = new IdNameBean();
        BeanUtils.copyProperties(entity, bean);
        return bean;
    }

}
