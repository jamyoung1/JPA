package com.jun.board.controller;

import com.jun.board.entity.Board;
import com.jun.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm() {

        return "boardwrite";    // boardwrite.html 로 이동
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size=10, sort="id", direction= Sort.Direction.DESC)
                            Pageable pageable, String searchKeyword) {
        // 검색 했을 때와 안 했을 때 구분을 지어줘야 한다.

        Page<Board> list = null;

        // 검색하는 단어가 없을 때 기존의 게시글 리스트를 보여주고
        // 검색하는 단어가 있을 때 검색 된 리스트를 보여준다.
        if(searchKeyword == null) {
            list = boardService.boardList(pageable);
        }else{
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        // pageable에서 넘어온 현재 페이지를 가져올 수 있다.
        // +1 하는 이유. Pageable에서 가져온 페이지는 0부터 시작한다.
        int nowPage = list.getPageable().getPageNumber() + 1;
        // 만약 현재 페이지가 1 페이지라면 음수가 나 올 수가 있다.
        // 두 값을 비교해서 높은 값을 꺼내게 된다.
        int startPage = Math.max(nowPage - 4, 1);
        // 만약 nowPage가 9가 나오고 TotalPage가 10인데
        // TotalPage가 넘어가게 된다.
        // 현재 페이지 수 보다 넘어가지 않게 한다.
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "boardlist";
    }


    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id) {

        model.addAttribute("board",boardService.boardView(id));
        return "boardview";
    }
    
    // 게시물 작성
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {

        boardService.write(board, file);

        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");


        return "message";
    }

    // 게시글 삭제
    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {

        boardService.boardDelete(id);
        model.addAttribute("message","글 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");

        return "message";
    }

    // 게시글 수정
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
    // url이 들어왓을때 id부분이 인식이 되서 Integer 형태의 id로 들어오게 된다.
        // localhost:8080/board/modify/8
        // ?가 아닌 /로 깔끔하게 표시가 된다

        model.addAttribute("board", boardService.boardView(id));
        // 상세페이지에 있는 글의 내용과, 수정할 때 넘어가는 내용이 같다.
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id);

        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        boardService.write(boardTemp, file);

        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");


        return "message";
    }
}
