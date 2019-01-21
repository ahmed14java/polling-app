package com.callicoder.controller;

import com.callicoder.config.CurrentUser;
import com.callicoder.config.service.UserPrincipal;
import com.callicoder.model.Poll;
import com.callicoder.payload.*;
import com.callicoder.repository.PollRepository;
import com.callicoder.repository.UserRepository;
import com.callicoder.repository.VoteRepository;
import com.callicoder.service.PollService;
import com.callicoder.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping
    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal userPrincipal,
                                                @RequestParam(value = "page" , defaultValue = "0") int page,
                                                @RequestParam(value = "size" , defaultValue = "30") int size){
        return pollService.getAllPolls(userPrincipal , page , size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER)")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest){
        Poll poll = pollService.createPoll(pollRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }

    @GetMapping("/{pollId}")
    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable Long pollId){
        return pollService.getPollById(pollId , currentUser);
    }

    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long pollId,
                                 @Valid @RequestBody VoteRequest voteRequest) {
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
    }

}
