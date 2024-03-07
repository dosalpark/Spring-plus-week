package com.example.foodthought.entity;

import com.example.foodthought.dto.book.UpdateBookRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false, length = 255)
    private String publisher;

    @Column(nullable = false, length = 65535)
    private String image;

    @Column(nullable = false, length = 255)
    private String category;


    public void update(UpdateBookRequestDto dto, String image) {
        if(!dto.getTitle().isEmpty())   this.title = dto.getTitle();
        if(!dto.getAuthor().isEmpty())   this.author = dto.getAuthor();
        if(!dto.getPublisher().isEmpty())   this.publisher = dto.getPublisher();
        if(!dto.getCategory().isEmpty())   this.category = dto.getCategory();
        if(!image.isEmpty())    this.image = image;
    }
}
