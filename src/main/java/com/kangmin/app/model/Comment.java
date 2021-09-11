package com.kangmin.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

import static com.kangmin.app.model.Account.ID_LENGTH;

@Data
@Entity
@Table(name = "tb_comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @Column(length = ID_LENGTH)
    private String accountId;

    @ManyToOne
    private Blog blog;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public Comment() {

    }

    public Comment(final String content, final String accountId) {
        this.content = content;
        this.accountId = accountId;
        this.createTime = new Date();
    }
}
