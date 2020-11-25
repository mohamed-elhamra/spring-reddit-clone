package com.example.springredditclone.controllers;


import com.example.springredditclone.dtos.SubredditDto;
import com.example.springredditclone.requests.SubredditRequest;
import com.example.springredditclone.responses.SubredditResponse;
import com.example.springredditclone.services.SubredditService;
import com.example.springredditclone.utils.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subreddits")
public class SubredditController {

    @Autowired
    private SubredditService subredditService;

    @Autowired
    @Qualifier("CustomizedMapper")
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> createSubreddit(@RequestBody SubredditRequest subredditRequest){
        SubredditDto subredditDto = Mapper.getMapper().map(subredditRequest, SubredditDto.class);
        SubredditDto createdSubreddit = subredditService.createSubreddit(subredditDto);
        SubredditResponse subredditResponse = modelMapper.map(createdSubreddit, SubredditResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(subredditResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllSubreddits(){
        List<SubredditDto> subredditDtoList = subredditService.getAllSubreddits();
        List<SubredditResponse> subredditResponses = subredditDtoList.stream()
                .map(subredditDto->modelMapper.map(subredditDto, SubredditResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(subredditResponses);
    }

}
