package com.example.myreform.domain.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)//access 속성을 이용해서 동일한 패키지 내의 클래스에서만 객체를 생성할 수 있도록 제어합니다.
@Getter
@Table(name = "BOARD_IMAGE")
public class BoardImage {//extends Image
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    @Column(name = "board_image_id")
    private long boardImageId;

    @Column(name = "board_id")
    private Long boardId;
    @ManyToOne(cascade = CascadeType.ALL)//부모 객체에서 자식 객체를 바인딩하여 한번에 저장하려는데 자식 객체가 아직 데이터 베이스에 저장되지 않았기 때문에 error발생 가능 => 따라서  cascade 옵션을 추가
    @JoinColumn(name = "image_id")
    private Image image;

    @Builder
    public BoardImage(Long boardId, Image image){
        this.boardId = boardId;
        this.image = image;
    }
}