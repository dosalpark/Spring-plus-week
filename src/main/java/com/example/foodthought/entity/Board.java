package com.example.foodthought.entity;

import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;


    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.Post;


    @Column(nullable = false, length = 65535)
    private String contents;

    public void update(UpdateBoardRequestDto dto, User user) {
        this.user = user;
        this.bookId = dto.getBookId();
        if(!dto.getContents().isEmpty()) this.contents = dto.getContents();
    }

    public void block() {
        this.status = Status.Blocked;
    }

}
