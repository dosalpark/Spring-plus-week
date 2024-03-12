package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.GetBookResponseDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import com.example.foodthought.entity.Book;
import com.example.foodthought.exception.customException.BookNotFoundException;
import com.example.foodthought.exception.customException.BookSearchNotFoundException;
import com.example.foodthought.exception.customException.ImageNotFoundException;
import com.example.foodthought.repository.book.BookRepository;
import com.example.foodthought.util.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.foodthought.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;
    private final StorageService storageService;


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<Page<GetBookResponseDto>> getAllBook(int page, int size, String sort, boolean isAsc, String title) {
        title = title.trim();
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<GetBookResponseDto> books = bookRepository.findAllByTitleContains(pageRequest, title);
        return ResponseDto.success(200, books);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetBookResponseDto> getBook(Long bookId) {
        return ResponseDto.success(200, convertToDto(findBook(bookId)));
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> createBook(CreateBookRequestDto createBookRequestDto, MultipartFile file) throws IOException {
        bookRepository.save(convertToBook(createBookRequestDto, convertToString(file)));
        return ResponseDto.success(201, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateBook(Long bookId, UpdateBookRequestDto updateBookRequestDto, MultipartFile file) throws IOException {
        Book book = findBook(bookId);
        book.update(updateBookRequestDto, convertToString(file));
        bookRepository.save(book);
        return ResponseDto.success(200, true);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteBook(Long bookId) {
        bookRepository.delete(findBook(bookId));
        return ResponseDto.success(200, true);
    }


    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(()
                -> new BookNotFoundException(NOT_FOUND_BOOK));
    }


    private GetBookResponseDto convertToDto(Book book) {
        return GetBookResponseDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .image(book.getImage())
                .category(book.getCategory())
                .createdAt(book.getCreatedAt())
                .modifiedAt(book.getModifiedAt())
                .build();
    }


    private Book convertToBook(CreateBookRequestDto dto, String image) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .image(image)
                .category(dto.getCategory())
                .build();
    }


    private String convertToString(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ImageNotFoundException(NOT_FOUND_IMAGE);
        }
        return storageService.uploadFile(file);
    }
}