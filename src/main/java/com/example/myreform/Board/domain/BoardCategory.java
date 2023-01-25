package com.example.myreform.Board.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)//access 속성을 이용해서 동일한 패키지 내의 클래스에서만 객체를 생성할 수 있도록 제어합니다.
@Getter
@Table(name = "BOARD_CATEGORY")
public class BoardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    @Column(name = "board_category_id")
    private long boardCategoryId;

    @Column(name = "board_id")
    private long boardId;

    @Column(name = "category_id")
    private int categoryId;


}
