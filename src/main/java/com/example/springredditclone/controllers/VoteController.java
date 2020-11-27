package com.example.springredditclone.controllers;

import com.example.springredditclone.dtos.VoteDto;
import com.example.springredditclone.requests.VoteRequest;
import com.example.springredditclone.responses.VoteResponse;
import com.example.springredditclone.services.VoteService;
import com.example.springredditclone.utils.Mapper;
import com.example.springredditclone.utils.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponse> createVote(@RequestBody VoteRequest voteRequest){
        VoteDto voteDto = Mapper.getMapper().map(voteRequest, VoteDto.class);
        System.out.println(voteRequest.toString());
        System.out.println(voteDto.toString());
        VoteDto createdVote = voteService.createVote(voteDto, voteRequest.getPostId());
        VoteResponse response = Mapper.getMapper().map(createdVote, VoteResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
