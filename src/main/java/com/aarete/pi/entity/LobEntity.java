package com.aarete.pi.entity;

import com.aarete.pi.bean.IdNameBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Lob")
public class LobEntity {

    @Id
    @Column(name = "lob_id")
    private String id;

    @Column(name = "lob_name")
    private String name;

    @Column(name = "lob_desc")
    private String desc;

    public static LobEntity getInstance(IdNameBean masterTableRequest) {
        LobEntity entity = new LobEntity();
        BeanUtils.copyProperties(masterTableRequest, entity);
        return entity;
    }

    public static IdNameBean getBean(LobEntity entity) {
        IdNameBean bean = new IdNameBean();
        BeanUtils.copyProperties(entity, bean);
        return bean;
    }
}
