package com.example.foodthought.service;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.dto.admin.UpdateStatusRequestDto;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.entity.User;
import org.springframework.data.domain.Page;

public interface BoardService {


    /**
     * 게시물 생성
     *
     * @param create 생성할 게시물 내용
     * @param user   작성한 유저이름
     * @return 성공시 true 반환, 실패시 exception
     */
    public ResponseDto<Boolean> createBoard(CreateBoardRequestDto create, User user);


    /**
     * 전체 게시물 조회
     *
     * @param page  default 0
     * @param size  default 10
     * @param sort  정렬기준(String)
     * @param isAsc ASC, DESC
     * @return Page<GetBoardResponseDto>
     */
    public ResponseDto<Page<GetBoardResponseDto>> getAllBoards(int page, int size, String sort, boolean isAsc);


    /**
     * 단일 게시물 조회
     *
     * @param boardId 검색할 게시물 ID값
     * @return GetBoardResponseDto
     */
    public ResponseDto<GetBoardResponseDto> getBoard(Long boardId);


    /**
     * 게시물 수정
     *
     * @param boardId               수정할 게시물 ID
     * @param updateBoardRequestDto 수정할 내용
     * @param user                  삭제 할 유저
     * @return 성공시 true 반환, 실패시 exception
     */
    public ResponseDto<Boolean> updateBoard(Long boardId, UpdateBoardRequestDto updateBoardRequestDto, User user);


    /**
     * 게시물 삭제
     *
     * @param boardId 삭제할 게시물 ID값
     * @param user    삭제 할 유저
     * @return 성공시 true 반환, 실패시 exception
     */
    public ResponseDto<Boolean> deleteBoard(Long boardId, User user);


    /**
     * 관리자용 모든 게시물 검색
     *
     * @param page  default 0
     * @param size  default 10
     * @param sort  정렬기준(String)
     * @param isAsc ASC, DESC
     * @return Page<GetBoardAdminResponseDto>
     */
    public ResponseDto<Page<GetBoardAdminResponseDto>> getAdminAllBoard(int page, int size, String sort, boolean isAsc);


    /**
     * 관리자용 게시물 삭제
     *
     * @param boardId 삭제할 게시물 ID값
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> deleteAdminBoard(Long boardId);


    /**
     * 관리자용 게시물 상태변경 처리
     *
     * @param boardId 상태 변경할 게시물 ID값
     * @return 성공시 true, 실패는 Exception 으로 처리
     */
    public ResponseDto<Boolean> updateStatusBoard(Long boardId, UpdateStatusRequestDto updateStatusRequestDto);
}