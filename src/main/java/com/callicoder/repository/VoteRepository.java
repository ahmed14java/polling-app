package com.callicoder.repository;

import com.callicoder.model.ChoiseVoteCount;
import com.callicoder.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote , Long>{

    @Query("SELECT NEW com.callicoder.model.ChoiseVoteCount(v.choice.id , count(v.id)) FROM Vote v WHERE v.poll.id in :pollIds GROUP BY v.choice.id")
    List<ChoiseVoteCount> countByPollIdInGroupByChoiseId(@Param("pollIds") List<Long> pollIds);

    @Query("SELECT NEW com.callicoder.model.ChoiseVoteCount(v.choice.id , count(v.id)) FROM Vote v WHERE v.poll.id in :pollId GROUP BY v.choice.id")
    List<ChoiseVoteCount> countByPollIdGroupByChoiseId(@Param("pollId") Long pollId);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.poll.id = :pollIds ")
    List<Vote> findByUserIdAndPollIdIn(@Param("userId") Long userId, @Param("pollIds") List<Long> pollIds);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.poll.id = :pollId")
    Vote findByUserIdAndPollId(@Param("userId") Long userId, @Param("pollId") Long pollId);

    @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);


    @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
    Page<Long> findVotedPollIdsByUserId(@Param("userId") Long userId, Pageable pageable);
}
