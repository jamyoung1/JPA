package com.jun.board.service;

import com.jun.board.entity.Board;
import com.jun.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 게시물 작성
    public void write(Board board, MultipartFile file) throws Exception {

        // 프로젝트 경로를 projectPath에 담아준다
        String projectPath = System.getProperty("user.dir") +
                "\\src\\main\\resources\\static\\files";

        // 식별자.. 랜덤으로 이름을 생성 해 준다
        UUID uuid = UUID.randomUUID();
        // 랜덤 식별자가 붙고, _ 붙이고, 파일 이름이 붙게 된다.
        String fileName = uuid + "_" + file.getOriginalFilename();

        // 파일을 생성 해 주는데, 이 경로에 넣어주고 name으로 이름이 담긴다.
        File saveFile = new File(projectPath, fileName);

        // Exception에 대비하라는 경고 창이 뜬다
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }

    // 게시물 리스트
    public Page<Board> boardList(Pageable pageable) {
        // 매개변수가 없을 땐 리턴 값 List
        // Page 클래스로 리턴을 해야한다.
        return boardRepository.findAll(pageable);
    }

    // 게시물 상세 리스트
    public Board boardView(Integer id) {
        // 어떤 게시글을 불러올지 지정해줘야 한다(매개변수로 Integer 값을 넣어줘야 함)
        // findById를 사용하면 Optional 값으로 받아온다.
        return boardRepository.findById(id).get();
    }

    // 게시물 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

}
