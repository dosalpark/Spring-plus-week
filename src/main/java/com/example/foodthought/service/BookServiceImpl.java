package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.GetBookResponseDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import com.example.foodthought.entity.Book;
import com.example.foodthought.repository.BookRepository;
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

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;
    private final StorageService storageService;


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<GetBookResponseDto>> getAllBook(int page, int size, String sort, boolean isAsc, String title) {
        title = title.trim();
        if (title.isEmpty()) {
            if (bookRepository.findAllByTitleContains(title).isEmpty()) {
                throw new IllegalArgumentException("도서명에 " + title + "이 들어가는 책이 없습니다.");
            }
        } else {
            if (bookRepository.findAll().isEmpty()) {
                throw new IllegalArgumentException("등록된 책이 없습니다.");
            }
        }
        PageRequest pageRequest = PageRequest.of(page, size, !isAsc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        Page<Book> books = bookRepository.findAllByTitleContains(pageRequest, title);
        return ResponseDto.success(200, convertToDtoList(books));
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
        boolean success = true;
        return ResponseDto.success(201, success);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> updateBook(Long bookId, UpdateBookRequestDto updateBookRequestDto, MultipartFile file) throws IOException {
        Book book = findBook(bookId);
        book.update(updateBookRequestDto, convertToString(file));
        bookRepository.save(book);
        boolean success = true;
        return ResponseDto.success(201, success);
    }


    @Override
    @Transactional
    public ResponseDto<Boolean> deleteBook(Long bookId) {
        bookRepository.delete(findBook(bookId));
        boolean success = true;
        return ResponseDto.success(201, success);
    }


    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(()
                -> new IllegalArgumentException("해당하는 책이 없습니다.")
        );
    }


    private GetBookResponseDto convertToDto(Book book) {
        return GetBookResponseDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .image(book.getImage())
                .category(book.getCategory())
                .build();
    }


    private List<GetBookResponseDto> convertToDtoList(Page<Book> books) {
        List<GetBookResponseDto> dtoList = new ArrayList<>();
        for (Book book : books) {
            GetBookResponseDto dto = GetBookResponseDto.builder()
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .publisher(book.getPublisher())
                    .image(book.getImage())
                    .category(book.getCategory())
                    .createAt(book.getCreateAt())
                    .modifiedAt(book.getModifiedAt())
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
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
            throw new IOException("이미지를 업로드 해주세요");
        }
        return storageService.uploadFile(file);
    }
}