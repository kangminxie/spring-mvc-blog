package com.kangmin.app.dao;

import com.kangmin.app.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BlogDao extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

        /*
        methods provided by CrudRepository<T, ID>:
        T = Blog
        ID = Long

        CREATE:
            <S extends T> S save(S entity);

        READ:
            Optional<T> findById(ID id);
            boolean existsById(ID id);
            long count();
            T getOne(ID id);

        UPDATE:
            <S extends T> S save(S entity);

        DELETE:
            void deleteById(ID id);
            void delete(T entity);

        PagingAndSortingRepository:
            Page<T> findAll(Pageable pageable);
            Iterable<T> findAll(Sort sort);
     */

    // Optional<Blog> findById(Long id);

    Optional<Blog> findByAccountIdAndId(String accountId, Long id);

    Optional<Blog> findByAccountUsernameAndId(String username, Long id);

    List<Blog> findAllByCategoryId(Long id);

    Page<Blog> findByCategoryId(Long id, Pageable pageable);

    @Query("select b from Blog b where b.title like %?1% or b.description like %?1% or b.content like %?1% or b.category.name like %?1%")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Query("select b from Blog b where b.category.id = ?2 and (b.title like %?1% or b.description like %?1% or b.content like %?1% or b.category.name like %?1%)")
    Page<Blog> findByQueryAndCategoryId(String query, Long id, Pageable pageable);

    @Query("select b from Blog b where b.account.id = ?1 and (b.title like ?2 or b.content like ?2)")
    Page<Blog> findByQuery(String accountId, String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

//    @Transactional
//    @Modifying
//    @Query("update Blog b set b.views = b.views+1 where b.account.id = ?1 and b.id = ?2")
//    int updateViews(String accountId, Long id);
}
