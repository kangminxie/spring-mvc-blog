package com.kangmin.app.model;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tb_blog")
public class Blog implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    private String imageId;

    private Integer views;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "blog")
    private final List<Comment> comments = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    private Blog(
        final String title,
        final String description,
        final String content,
        final Account account,
        final Category category,
        final String imageId
    ) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.account = account;
        this.category = category;
        this.imageId = imageId;
        this.views = 0;
        final Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
    }

    public Blog() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String description;
        private String content;
        private Account account;
        private Category category;
        private String imageId;

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withContent(final String content) {
            this.content = content;
            return this;
        }

        public Builder withAccount(final Account account) {
            this.account = account;
            return this;
        }

        public Builder withCategory(final Category category) {
            this.category = category;
            return this;
        }

        public Builder withImageId(final String imageId) {
            this.imageId = imageId;
            return this;
        }

        public Blog build() {
            return new Blog(title, description, content, account, category, imageId);
        }
    }
}
