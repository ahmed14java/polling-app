package com.callicoder.model;

public class ChoiseVoteCount {

    private Long choiseId;
    private Long voteCounte;

    public ChoiseVoteCount(Long choiseId, Long voteCounte) {
        this.choiseId = choiseId;
        this.voteCounte = voteCounte;
    }

    public Long getChoiseId() {
        return choiseId;
    }

    public void setChoiseId(Long choiseId) {
        this.choiseId = choiseId;
    }

    public Long getVoteCounte() {
        return voteCounte;
    }

    public void setVoteCounte(Long voteCounte) {
        this.voteCounte = voteCounte;
    }
}
