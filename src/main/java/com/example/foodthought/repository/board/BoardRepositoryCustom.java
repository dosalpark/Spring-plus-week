package com.example.foodthought.repository.board;

import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {


    /**
     * 이용자가 검색 할 수 없는 BLOCKED 상태의 게시물을 빼고 조회
     *
     * @param pageable pageing처리에 필요한 값들 입력받음
     * @return Page<GetBoardResponseDto>
     */
    Page<GetBoardResponseDto> findAllByStatusIn(Pageable pageable);


    /**
     * 모든 상태의 게시물을 전체 조회
     *
     * @param pageable pageing처리에 필요한 값들 입력받음
     * @return Page<GetBoardAdminResponseDto>
     */
    Page<GetBoardAdminResponseDto> findAllAdmin(Pageable pageable);
}
