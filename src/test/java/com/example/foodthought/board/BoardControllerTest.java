package com.example.foodthought.board;

import com.example.foodthought.common.dto.ResponseDto;
import com.example.foodthought.controller.BoardController;
import com.example.foodthought.dto.board.CreateBoardRequestDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.dto.board.UpdateBoardRequestDto;
import com.example.foodthought.entity.User;
import com.example.foodthought.exception.customException.BoardNotFoundException;
import com.example.foodthought.exception.customException.BookNotFoundException;
import com.example.foodthought.exception.customException.PermissionDeniedException;
import com.example.foodthought.security.UserDetailsImpl;
import com.example.foodthought.service.BoardService;
import com.example.foodthought.test.DummyTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.foodthought.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)})
@ActiveProfiles("test")
class BoardControllerTest implements DummyTest {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BoardService boardService;

    @BeforeEach
    void mockMVCSetup() {
        User mockUser = mock(User.class); //mockUser 생성
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        given(mockUserDetails.getUser()).willReturn(mockUser);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mockUserDetails, null));

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("게시글 생성 - 성공")
    void test1() throws Exception {
        //given
        given(boardService.createBoard(any(CreateBoardRequestDto.class), any(User.class)))
                .willReturn(ResponseDto.success(201, true));
        String postInfo = objectMapper.writeValueAsString(TEST_BOARD_REQUEST_DTO);

        //when - then
        mockMvc.perform(post("/api/boards")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 생성 - 입력받은 값중 책에 대한 값이 없는 값일 때")
    void test2() throws Exception {
        //given
        given(boardService.createBoard(any(CreateBoardRequestDto.class), any(User.class)))
                .willThrow(new BookNotFoundException(NOT_FOUND_BOOK));
        String postInfo = objectMapper.writeValueAsString(TEST_BOARD_REQUEST_DTO);

        //when - then
        mockMvc.perform(post("/api/boards")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("해당하는 책이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("전체 게시물 조회 - 성공")
    void test3() throws Exception {
        //given
        int page = 0;
        int size = 2;
        String sort = "createdAt";
        boolean isAsc = false;
        PageRequest pageable = PageRequest.of(page, size, isAsc ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        List<GetBoardResponseDto> boardList = new ArrayList<>();
        boardList.add(TEST_BOARD_RESPONSE_DTO);
        boardList.add(TEST_ANOTHER_BOARD_RESPONSE_DTO);
        Page<GetBoardResponseDto> boardPage = new PageImpl<>(boardList.subList(0, 2), pageable, boardList.size());
        given(boardService.getAllBoards(any(int.class), any(int.class), any(String.class), any(boolean.class)))
                .willReturn(ResponseDto.success(200, boardPage));

        Map<String, Object> sortInfo = new HashMap<>();
        sortInfo.put("page", page);
        sortInfo.put("size", size);
        sortInfo.put("sort", sort);
        sortInfo.put("isAsc", isAsc);


        String getInfo = objectMapper.writeValueAsString(sortInfo);

        //when - then
        mockMvc.perform(get("/api/boards")
                        .content(getInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.content[1].title").value("title"))
                .andExpect(jsonPath("$.data.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.data.pageable.sort.sorted").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("전체 게시물 조회 - 조회할 게시물이 없는 경우")
    void test4() throws Exception {
        //given
        int page = 0;
        int size = 2;
        String sort = "createdAt";
        boolean isAsc = false;
        PageRequest pageable = PageRequest.of(page, size, isAsc ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        List<GetBoardResponseDto> boardList = new ArrayList<>();
        boardList.add(TEST_BOARD_RESPONSE_DTO);
        boardList.add(TEST_ANOTHER_BOARD_RESPONSE_DTO);
        Page<GetBoardResponseDto> boardPage = new PageImpl<>(boardList.subList(0, 2), pageable, boardList.size());
        given(boardService.getAllBoards(any(int.class), any(int.class), any(String.class), any(boolean.class)))
                .willThrow(new BoardNotFoundException(NOT_FOUND_SEARCH_BOARD));

        Map<String, Object> sortInfo = new HashMap<>();
        sortInfo.put("page", page);
        sortInfo.put("size", size);
        sortInfo.put("sort", sort);
        sortInfo.put("isAsc", isAsc);

        String getInfo = objectMapper.writeValueAsString(sortInfo);

        //when - then
        mockMvc.perform(get("/api/boards")
                        .content(getInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("등록된 게시물이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("단일 게시물 조회 - 성공")
    void test5() throws Exception {
        //given
        given(boardService.getBoard(any(Long.class)))
                .willReturn(ResponseDto.success(200, TEST_BOARD_RESPONSE_DTO));

        //when - then
        mockMvc.perform(get("/api/boards/{boardId}", TEST_BOARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.author").value("author"))
                .andExpect(jsonPath("$.data.publisher").value("publisher"))
                .andExpect(jsonPath("$.data.category").value("category"))
                .andExpect(jsonPath("$.data.image").value("url"))
                .andExpect(jsonPath("$.data.userId").value("username"))
                .andExpect(jsonPath("$.data.contents").value("content"))
                .andDo(print());
    }

    @Test
    @DisplayName("단일 게시물 조회 - 조회할 게시물이 없는 경우")
    void test6() throws Exception {
        //given
        given(boardService.getBoard(any(Long.class)))
                .willThrow(new BoardNotFoundException(NOT_FOUND_SEARCH_BOARD));

        //when - then
        mockMvc.perform(get("/api/boards/{boardId}", TEST_NOT_FOUND_BOARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("등록된 게시물이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 - 성공")
    void test7() throws Exception {
        //given
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("UpdateBoardRequestDto", TEST_UPDATE_BOARD_REQUEST_DTO);
        updateInfo.put("User", TEST_USER);

        given(boardService.updateBoard(any(Long.class), any(UpdateBoardRequestDto.class), any(User.class)))
                .willReturn(ResponseDto.success(200, true));

        String putInfo = objectMapper.writeValueAsString(updateInfo);

        //when - then
        mockMvc.perform(put("/api/boards/{boardId}", TEST_BOARD_ID)
                        .content(putInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 - 수정할 게시물이 존재하지 않음")
    void test8() throws Exception {
        //given
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("UpdateBoardRequestDto", TEST_UPDATE_BOARD_REQUEST_DTO);
        updateInfo.put("User", TEST_USER);

        given(boardService.updateBoard(any(Long.class), any(UpdateBoardRequestDto.class), any(User.class)))
                .willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));

        String putInfo = objectMapper.writeValueAsString(updateInfo);

        //when - then
        mockMvc.perform(put("/api/boards/{boardId}", TEST_NOT_FOUND_BOARD_ID)
                        .content(putInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("해당하는 게시물이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 - 수정값에 입력한 책이 존재하지 않음")
    void test9() throws Exception {
        //given
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("UpdateBoardRequestDto", TEST_NOT_FOUND_BOOK_UPDATE_BOARD_REQUEST_DTO);
        updateInfo.put("User", TEST_USER);

        given(boardService.updateBoard(any(Long.class), any(UpdateBoardRequestDto.class), any(User.class)))
                .willThrow(new BookNotFoundException(NOT_FOUND_BOOK));

        String putInfo = objectMapper.writeValueAsString(updateInfo);

        //when - then
        mockMvc.perform(put("/api/boards/{boardId}", TEST_BOARD_ID)
                        .content(putInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("해당하는 책이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 - 타인 게시물 수정")
    void test10() throws Exception {
        //given
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("UpdateBoardRequestDto", TEST_UPDATE_BOARD_REQUEST_DTO);
        updateInfo.put("User", TEST_ANOTHER_USER);

        given(boardService.updateBoard(any(Long.class), any(UpdateBoardRequestDto.class), any(User.class)))
                .willThrow(new PermissionDeniedException(PERMISSION_DENIED));

        String putInfo = objectMapper.writeValueAsString(updateInfo);

        //when - then
        mockMvc.perform(put("/api/boards/{boardId}", TEST_BOARD_ID)
                        .content(putInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("권한이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제 - 성공")
    void test11() throws Exception {
        //given
        given(boardService.deleteBoard(any(Long.class), any(User.class)))
                .willReturn(ResponseDto.success(200, true));

        String deleteInfo = objectMapper.writeValueAsString(TEST_USER);

        //when - then
        mockMvc.perform(delete("/api/boards/{boardId}", TEST_BOARD_ID)
                        .content(deleteInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 - 삭제할 게시물이 존재하지 않음")
    void test12() throws Exception {
        //given
        given(boardService.deleteBoard(any(Long.class), any(User.class)))
                .willThrow(new BoardNotFoundException(NOT_FOUND_BOARD));

        String deleteInfo = objectMapper.writeValueAsString(TEST_USER);

        //when - then
        mockMvc.perform(delete("/api/boards/{boardId}", TEST_NOT_FOUND_BOARD_ID)
                        .content(deleteInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("해당하는 게시물이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 - 타인 게시물 수정")
    void test13() throws Exception {
        //given
        given(boardService.deleteBoard(any(Long.class), any(User.class)))
                .willThrow(new PermissionDeniedException(PERMISSION_DENIED));

        String deleteInfo = objectMapper.writeValueAsString(TEST_ANOTHER_USER);

        //when - then
        mockMvc.perform(delete("/api/boards/{boardId}", TEST_BOARD_ID)
                        .content(deleteInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("권한이 없습니다."))
                .andDo(print());
    }
}