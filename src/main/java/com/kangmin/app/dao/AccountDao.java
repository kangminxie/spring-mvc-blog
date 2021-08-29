package com.kangmin.app.dao;

import com.kangmin.app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, String> {

    /*
        methods provided by CrudRepository<T, ID>:
        T = Account
        ID = String

        CREATE:
            <S extends T> S save(S entity);

        READ:
            Optional<T> findById(ID id);
            boolean existsById(ID id);
            long count();

        UPDATE:
            <S extends T> S save(S entity);

        DELETE:
            void deleteById(ID id);
            void delete(T entity);

        PagingAndSortingRepository:
            Page<T> findAll(Pageable pageable);
            Iterable<T> findAll(Sort sort);
     */


    // Optional<Account> findById(String id);
    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    Optional<Account> findAccountByEmailOrUsername(String email, String username);

    // boolean existsById(String id);
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);

    // Account save(Account entity);

    // void deleteById(String id);

    // Page<Account> findAll(Pageable pageable);
}
