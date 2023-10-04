package com.dong.tech.controller;

import com.dong.tech.domain.Member;
import com.dong.tech.dto.MemberDTO;
import com.dong.tech.service.MemberService;
import com.dong.tech.service.RegisterMemberService;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The Class MemberController.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@RestController
public class MemberController {
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("user")
    public List<Member>getMember(){
        return memberService.getAllMember();
    }

    @GetMapping("/user/{id}")
    public Member findMember(@PathVariable("id") String id){
        return  memberService.findMember(id);
    }

//    @PostMapping("/join")
//    public ResponseEntity<String> join(@RequestMapping){ return }



}
