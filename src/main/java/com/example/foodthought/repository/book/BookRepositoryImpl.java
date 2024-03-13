package com.example.foodthought.repository.book;

import com.example.foodthought.config.QueryFactoryConfig;
import com.example.foodthought.dto.book.GetBookResponseDto;
import com.example.foodthought.entity.Book;
import com.example.foodthought.entity.QBook;
import com.example.foodthought.exception.customException.BookSearchNotFoundException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodthought.exception.ErrorCode.NOT_FOUND_SEARCH_TITLE_BOOK;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final QueryFactoryConfig config;


    @Override
    public Page<GetBookResponseDto> findAllByTitleContains(Pageable pageable, String title) {
        QBook book = QBook.book;

        List<GetBookResponseDto> bookList = config.jpaQueryFactory()
                .select(Projections.fields(GetBookResponseDto.class,
                        book.title,
                        book.author,
                        book.publisher,
                        book.image,
                        book.category,
                        book.createdAt,
                        book.modifiedAt))
                .from(book)
                .orderBy(createBookOrderSpecifier(pageable).toArray(new OrderSpecifier[0]))
                .where(book.title.contains(title))
                .fetch();

        if (bookList.isEmpty()) {
            throw new BookSearchNotFoundException(NOT_FOUND_SEARCH_TITLE_BOOK, title);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bookList.size());

        return new PageImpl<>(bookList.subList(start, end), pageable, bookList.size());
    }

    public List<OrderSpecifier<?>> createBookOrderSpecifier(Pageable pageable) {
        List<OrderSpecifier<?>> orderList = new ArrayList<>();
        //정렬값이 기본값이면 기본적으로 작성일/DESC 로 리턴
        if (pageable.getSort().stream().count() == 1 &&  //정렬기준이 2개이상이면서
                pageable.getSort().stream().findFirst().map(order -> order.getProperty().equals("createdAt")).orElse(false) && //첫번째 값이 createAt이고
                !pageable.getSort().isSorted()) { //정렬기준이 DESC면
            PathBuilder<Book> bookPath = new PathBuilder<>(Book.class, "book");
            DateTimePath<LocalDateTime> createdAtPath = bookPath.getDateTime("createdAt", LocalDateTime.class);
            orderList.add(new OrderSpecifier<>(Order.DESC, createdAtPath));
            return orderList;
        } else if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<Book> bookPath = new PathBuilder<>(Book.class, "book");
                StringPath stringPath;
                DateTimePath<LocalDateTime> dateTimePath;

                switch (order.getProperty()) {
                    case "createdAt":
                        dateTimePath = bookPath.getDateTime("createdAt", LocalDateTime.class);
                        orderList.add(new OrderSpecifier<>(direction, dateTimePath));
                        break;
                    case "modifiedAt":
                        dateTimePath = bookPath.getDateTime("modifiedAt", LocalDateTime.class);
                        orderList.add(new OrderSpecifier<>(direction, dateTimePath));
                        break;
                    case "title":
                        stringPath = bookPath.getString("title");
                        orderList.add(new OrderSpecifier<>(direction, stringPath));
                        break;
                    case "author":
                        stringPath = bookPath.getString("author");
                        orderList.add(new OrderSpecifier<>(direction, stringPath));
                        break;
                    case "publisher":
                        stringPath = bookPath.getString("publisher");
                        orderList.add(new OrderSpecifier<>(direction, stringPath));
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
