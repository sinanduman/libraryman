package com.sinanduman.library.service;

import com.sinanduman.library.model.Member;
import com.sinanduman.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member save(Member member) {
        /* Date: 2022-02-20T10:00:00 */
        int sampleLength = 19;
        if (member.getJoinDate().trim().length() > sampleLength)
            member.setJoinDate(member.getJoinDate().trim().substring(0, sampleLength));
        return memberRepository.save(member);
    }

    public Collection<Member> findAll() {
        return memberRepository.findAll();
    }

    public Collection<Member> getMembers(String name, String surname, String email, String phoneNumber) {
        if (name.equals("") && surname.equals("") && email.equals("") && phoneNumber.equals(""))
            return Collections.emptyList();
        return memberRepository.findAllWithParameters(name, surname, email, phoneNumber);
    }

    public void delete(String id) {
        memberRepository.deleteById(Integer.parseInt(id));
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(Integer.parseInt(id)).orElse(null);
    }

    public Member update(String id, Member member) {
        if (member.getMemberId() == null)
            member.setMemberId(Integer.parseInt(id));
        return save(member);
    }
}
