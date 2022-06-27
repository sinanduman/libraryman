package com.sinanduman.library.repository;

import com.sinanduman.library.model.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MemberRepository extends CrudRepository<Member, Integer> {
    Collection<Member> findAll();

    @Query(value =
            "SELECT * " +
                    "FROM member " +
                    "WHERE 1=1 " +
                    "and (case when :name = '' then true else name = :name end) " +
                    "and (case when :surname = '' then true else surname = :surname end) " +
                    "and (case when :email = '' then true else email = :email end) " +
                    "and (case when :phone_number = '' then true else phone_number = :phone_number end)", nativeQuery = true)
    Collection<Member> findAllWithParameters(@Param("name") String name, @Param("surname") String surname, @Param("email") String email, @Param("phone_number") String phoneNumber);

    Member findAllByName(String name);

    Member findAllBySurname(String surname);

    Member findAllByEmail(String email);

    Member findAllByPhoneNumber(String phoneNumber);
}
