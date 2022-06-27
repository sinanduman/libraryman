package com.sinanduman.library.controller;

import com.sinanduman.library.auth.TokenManager;
import com.sinanduman.library.model.ApiResponse;
import com.sinanduman.library.model.Member;
import com.sinanduman.library.model.Role;
import com.sinanduman.library.service.MemberService;
import com.sinanduman.library.utility.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/library/api/v1")
public class MemberController {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    @PostMapping(value = "/members")
    public ResponseEntity<?> createMember(@RequestBody Member member, HttpServletRequest request) {
        Set<Role> allowedRoles = Set.of(Role.LIB_BM_C);
        if (notAuthorized(allowedRoles, request)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED);
        }

        if (!Validation.isValidMember(member)) {
            return new ResponseEntity<>(new ApiResponse("invalidParameter", "Parameter is invalid"), HttpStatus.BAD_REQUEST);
        }

        Member newMember = memberService.save(member);
        if (newMember == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PutMapping(value = "/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable String id, @RequestBody Member member, HttpServletRequest request) {
        Set<Role> allowedRoles = Set.of(Role.LIB_BM_C);
        if (notAuthorized(allowedRoles, request)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED);
        }

        Member tmpMember = memberService.getMemberById(id);
        if (tmpMember == null) {
            return new ResponseEntity<>(new ApiResponse("memberNotFound", "Member is not found."), HttpStatus.NOT_FOUND);
        }

        if (!Validation.isValidMember(member)) {
            return new ResponseEntity<>(new ApiResponse("invalidParameter", "Parameter is invalid"), HttpStatus.BAD_REQUEST);
        }

        Member newMember = memberService.update(id, member);
        if (newMember == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @DeleteMapping(value = "/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable String id, HttpServletRequest request) {
        Set<Role> allowedRoles = Set.of(Role.LIB_M_D);
        Set<Role> userRoles = tokenManager.getUserRolesFrom(getToken(request));

        if (notAuthorized(allowedRoles, request)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED);
        }
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/members/{id}")
    public ResponseEntity<?> getMember(@PathVariable String id, HttpServletRequest request) {
        Set<Role> allowedRoles = Set.of(Role.LIB_M_R, Role.LIB_M_R_ALL);
        Set<Role> userRoles = tokenManager.getUserRolesFrom(getToken(request));
        String memberId = tokenManager.getUserIdFrom(getToken(request));

        if (notAuthorized(allowedRoles, request)) {
            return new ResponseEntity<>(new ApiResponse(), HttpStatus.UNAUTHORIZED);
        }

        if (userRoles.contains(Role.LIB_M_R) && !memberId.equals(id)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED);
        }

        Member member = memberService.getMemberById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping(value = "/members")
    public ResponseEntity<Object> getMembers(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "surname", defaultValue = "") String surname,
            @RequestParam(name = "email", defaultValue = "") String email,
            @RequestParam(name = "phoneNumber", defaultValue = "") String phoneNumber, HttpServletRequest request) {

        Set<Role> allowedRoles = Set.of(Role.LIB_M_R_ALL);
        Set<Role> userRoles = tokenManager.getUserRolesFrom(getToken(request));
        String memberId = tokenManager.getUserIdFrom(getToken(request));

        if (notAuthorized(allowedRoles, request)) {
            return new ResponseEntity<>(new ApiResponse("notAuthorized", "Not Authorized"), HttpStatus.UNAUTHORIZED);
        }

        Collection<Member> memberList = memberService.getMembers(name, surname, email, phoneNumber);
        return ResponseEntity.ok(memberList);
    }

    private boolean notAuthorized(Set<Role> allowedRoles, HttpServletRequest request) {
        return !tokenManager.isAuthorizedForTheAction(getToken(request), allowedRoles);
    }

    private String getToken(HttpServletRequest request) {
        return tokenManager.extractTokenFrom(request);
    }
}
