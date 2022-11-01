package com.jun.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Board {
    // 테이블의 컬럼과 동일하게
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;
}
