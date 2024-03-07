package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.book.CreateBookRequestDto;
import com.example.foodthought.dto.book.GetBookResponseDto;
import com.example.foodthought.dto.book.UpdateBookRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {


    /**
     * 모든 책을 검색한다
     *
     * @param page  default 0
     * @param size  default 10
     * @param sort  정렬기준(String)
     * @param isAsc ASC, DESC
     * @param title 검색할 책 제목(선택)
     * @return List<GetBookResponseDto>
     */
    public ResponseDto<List<GetBookResponseDto>> getAllBook(int page, int size, String sort, boolean isAsc, String title);


    /**
     * 단일 도서를 검색한다
     *
     * @param bookId 검색할 책의 ID값
     * @return GetBookResponseDto
     */
    public ResponseDto<GetBookResponseDto> getBook(Long bookId);


    /**
     * 책 입력
     *
     * @param createBookRequestDto 책 내용 입력
     * @param file                 책 이미지 파일 업로드
     * @return 성공시 true, 실패는 Exception 으로 처리
     * @throws IOException 파일이 없거나 비정상적인 경우 발생
     */
    public ResponseDto<Boolean> createBook(CreateBookRequestDto createBookRequestDto, MultipartFile file) throws IOException;


    /**
     * 책 수정
     *
     * @param bookId               수정할 책 번호
     * @param updateBookRequestDto 수정할 책 내용
     * @param file                 수정할 이미지 파일 업로드
     * @return 성공시 true, 실패는 Exception 으로 처리
     * @throws IOException 파일이 없거나 비정상적인 경우 발생
     */
    public ResponseDto<Boolean> updateBook(Long bookId, UpdateBookRequestDto updateBookRequestDto, MultipartFile file) throws IOException;


    /**
     * 책 삭제
     *
     * @param bookId 삭제할 책 번호
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> deleteBook(Long bookId);

}