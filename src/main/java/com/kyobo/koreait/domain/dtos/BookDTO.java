package com.kyobo.koreait.domain.dtos;

import com.kyobo.koreait.domain.vos.BookVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BookDTO {
    private List<BookVO> bookVOS; //현재 페이지가 가지고 있는 게시물 리스트
    private final int pagePerArticle = 8; //현재 페이지에 보여줄 게시물 개수
    private int allPageCount; //전체 페이지 개수
    private int startArticleCount; //현재 페이지에서 최상위 번호
    private int nowPage;
    private int minPage; //현재 페이지에서 보여줄 최소 페이지 번호
    private int maxPage; //현재 페이지에서 보여줄 최대 페이지 번호
    private final int pageRange = 3; //현재 페이지에 보여줄 페이지 개수

    public BookDTO(List<BookVO> bookVOS, int nowPage) {
        this.nowPage = nowPage;
        int allArticleCount = bookVOS.size();
        this.startArticleCount = allArticleCount - ((nowPage - 1) * pagePerArticle);
        //22 / 3 = 7.~~
        int count = allArticleCount / pageRange; //7
        this.allPageCount = ((allArticleCount % pageRange) > 0) ? (count + 1) : count;
//        maxPage = ((int) Math.ceil(nowPage / pageRange)) * pageRange;
        maxPage = (((nowPage-1) / pageRange) + 1) * pageRange;
        minPage = maxPage - pageRange + 1;
        maxPage = Math.min(maxPage, allPageCount);
        //{현재페이지*현재페이지에서보여줄게시물개수} {+현재페이지에서보여줄게시물개수}
        int startIndex = (nowPage - 1) * pagePerArticle; //전체 리스트에서 시작 index
        int endIndex = startIndex + pagePerArticle > allArticleCount - 1 ? allArticleCount : startIndex + pagePerArticle; //전체 리스트에서 마지막 index
        this.bookVOS = bookVOS.subList(startIndex, endIndex);
    }
}
