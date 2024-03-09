package com.example.foodthought.entity;

import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;


    @Column(nullable = false)
    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.POST;


    @Column(nullable = false, length = 65535)
    private String contents;

    public void update(UpdateBoardRequestDto dto, User user) {
        this.user = user;
        if (!Objects.isNull(dto.getBookId())) this.bookId = dto.getBookId();
        if (!dto.getContents().isEmpty()) this.contents = dto.getContents();
    }

    public void updateStatusBoard(UpdateStatusRequestDto updateStatusRequestDto) {
        String upperStatus = (updateStatusRequestDto.getStatus()).toUpperCase();
        this.status = Status.valueOf(upperStatus);
    }

}
