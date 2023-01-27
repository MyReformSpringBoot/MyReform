package com.example.myreform.Board.domain;


import com.example.myreform.Board.category.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)//access 속성을 이용해서 동일한 패키지 내의 클래스에서만 객체를 생성할 수 있도록 제어합니다.
@Getter
@Table(name = "BOARD_CATEGORY")
public class BoardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    @Column(name = "board_category_id")
    private Long boardCategoryId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public BoardCategory(Board board, Category category){
        this.board = board;
        this.category = category;
    }

    // 연관관계 편의 메서드, 수정 시 사용
    public void setBoard(Board board) {
        this.board = board;
        board.getBoardCategories().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getBoardCategories().add(this);
    }
}
