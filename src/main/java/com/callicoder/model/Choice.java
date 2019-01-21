package com.callicoder.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "choices")
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "poll_id" , nullable = false)
    private Poll poll;

    public Choice() {
    }

    public Choice(@NotBlank String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

}
