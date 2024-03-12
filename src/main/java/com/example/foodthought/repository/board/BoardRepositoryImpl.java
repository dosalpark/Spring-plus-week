package com.example.foodthought.repository.board;

import com.example.foodthought.config.QueryFactoryConfig;
import com.example.foodthought.dto.board.GetBoardAdminResponseDto;
import com.example.foodthought.dto.board.GetBoardResponseDto;
import com.example.foodthought.entity.Board;
import com.example.foodthought.entity.QBoard;
import com.example.foodthought.exception.customException.BoardNotFoundException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodthought.entity.QBook.book;
import static com.example.foodthought.entity.Status.NOTICE;
import static com.example.foodthought.entity.Status.POST;
import static com.example.foodthought.exception.ErrorCode.NOT_FOUND_SEARCH_BOARD;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final QueryFactoryConfig config;

    @Override
    public Page<GetBoardResponseDto> findAllByStatusIn(Pageable pageable) {
        QBoard board = QBoard.board;
        List<GetBoardResponseDto> boardList = config.jpaQueryFactory()
                .select(Projections.fields(GetBoardResponseDto.class,
                        book.title,
                        book.author,
                        book.publisher,
                        book.image,
                        book.category,
                        board.contents))
                .from(board)
                .orderBy(createBoardOrderSpecifier(pageable).toArray(new OrderSpecifier[0]))
                .join(book).on(board.bookId.eq(book.id))
                .where(board.status.in(POST, NOTICE))
                .fetch();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), boardList.size());

        if (boardList.isEmpty()) {
            throw new BoardNotFoundException(NOT_FOUND_SEARCH_BOARD);
        }

        return new PageImpl<>(boardList.subList(start, end), pageable, boardList.size());
    }


    @Override
    public Page<GetBoardAdminResponseDto> findAllAdmin(Pageable pageable) {
        QBoard board = QBoard.board;
        List<GetBoardAdminResponseDto> boardList = config.jpaQueryFactory()
                .select(Projections.fields(GetBoardAdminResponseDto.class,
                        book.title,
                        book.author,
                        book.publisher,
                        book.image,
                        book.category,
                        board.contents,
                        board.status))
                .from(board)
                .orderBy(createBoardOrderSpecifier(pageable).toArray(new OrderSpecifier[0]))
                .join(book).on(board.bookId.eq(book.id))
                .fetch();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), boardList.size());

        if (boardList.isEmpty()) {
            throw new BoardNotFoundException(NOT_FOUND_SEARCH_BOARD);
        }

        return new PageImpl<>(boardList.subList(start, end), pageable, boardList.size());
    }



    public List<OrderSpecifier<LocalDateTime>> createBoardOrderSpecifier(Pageable pageable) {
        List<OrderSpecifier<LocalDateTime>> orderList = new ArrayList<>();
        //정렬값이 기본값이면 기본적으로 작성일/DESC 로 리턴
        if (pageable.getSort().stream().count() == 1 &&  //정렬기준이 2개이상이면서
                pageable.getSort().stream().findFirst().map(order -> order.getProperty().equals("createdAt")).orElse(false) && //첫번째 값이 createAt이고
                !pageable.getSort().isSorted()) { //정렬기준이 DESC면
            PathBuilder<Board> boardPath = new PathBuilder<>(Board.class, "board");
            DateTimePath<LocalDateTime> createdAtPath = boardPath.getDateTime("createdAt", LocalDateTime.class);
            orderList.add(new OrderSpecifier<>(Order.DESC, createdAtPath));
            return orderList;
        } else if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<Board> boardPath = new PathBuilder<>(Board.class, "board");
                DateTimePath<LocalDateTime> dateTimePath;

                switch (order.getProperty()) {
                    case "createdAt":
                        dateTimePath = boardPath.getDateTime("createdAt", LocalDateTime.class);
                        orderList.add(new OrderSpecifier<>(direction, dateTimePath));
                        break;
                    case "modifiedAt":
                        dateTimePath = boardPath.getDateTime("modifiedAt", LocalDateTime.class);
                        orderList.add(new OrderSpecifier<>(direction, dateTimePath));
                        break;
                    default:
                        throw new IllegalArgumentException("정렬기준을 정확히 입력해주세요");
                }
            }
            return orderList;
        }
        return null;
    }
}
