package com.dong.tech.controller;

import com.dong.tech.dto.MemberDTO;
import com.dong.tech.service.RegisterMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class AuthorizationController.
 *
 * @author dongsulee
 * @date 2023/10/04
 */
@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private final RegisterMemberService registerMemberService;

    public AuthorizationController(RegisterMemberService registerMemberService) {
        this.registerMemberService = registerMemberService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberDTO memberDTO){
        try {
            registerMemberService.join(memberDTO.getId(),memberDTO.getPwd(),memberDTO.getName());
            return ResponseEntity.ok("join success");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
