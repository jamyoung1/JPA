package com.jun.board.repository;

import com.jun.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    //  리턴 타입을 Page로. 제네릭을 써서 보드 객체를 담는다
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

}
