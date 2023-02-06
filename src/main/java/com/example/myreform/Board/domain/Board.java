package com.example.myreform.Board.domain;

import com.example.myreform.Board.dto.AllBoardFindDto;
import com.example.myreform.Board.dto.BoardUpdateDto;
import com.example.myreform.Like.domain.Like;
import com.example.myreform.User.domain.User;
import com.example.myreform.config.BaseEntity;
import com.example.myreform.Board.dto.OneBoardFindDto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "BOARD")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicInsert
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 45, nullable = false, name = "title") // 제목 길이 체크
    private String title;

    @Column(nullable = false, name = "contents")
    private String contents;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "count_of_like")
    private Long countOfLike = 0L;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardCategory> boardCategories = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Like> likes = new ArrayList<>();


    // 연관관계 편의 메서드, 수정 시 사용
    public void setUser(User user) {
        this.user = user;
        user.getBoard().add(this);
    }

    public void delete() {
        super.delete();
    }

    @Builder
    public Board(Long boardId, User user, String title, String contents, Integer price, Long countOfLike){
        this.boardId = boardId;
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.price = price;
        this.countOfLike = countOfLike;
    }


    public OneBoardFindDto toOneBoardFindDto() {
        return OneBoardFindDto.builder()
                .boardId(boardId)
                .categoryId(getCategoryId())
                .user(user)
                .title(title)
                .contents(contents)
                .price(price)
                .countOfLike(countOfLike)
                .updateAt(getUpdateAt())
                .imageUrl(getUrlString())
                .build();
    }

    public AllBoardFindDto toAllBoardFindDto() {
        return AllBoardFindDto.builder()
                .categoryId(getCategoryId())
                .boardId(boardId)
                .title(title)
                .contents(contents)
                .updateAt(getUpdateAt())
                .price(price)
                .countOfLike(countOfLike)
                .nickname(user.getNickname())
                .imageUrl(getUrlString())
                .build();
    }

    public void update(BoardUpdateDto boardUpdateDto) {
        this.title = boardUpdateDto.getTitle();
        this.contents = boardUpdateDto.getContents();
        this.price = boardUpdateDto.getPrice();
    }

    private List<String> getUrlString() {
        return boardImages.stream()
                .map(x -> x.toImageFindDto().getImageURL())
                .collect(Collectors.toList());
    }

    private List<Integer> getCategoryId() {
        return boardCategories.stream()
                .map(x -> x.getCategory().getCategoryId())
                .collect(Collectors.toList());
    }

    public void updateLike(boolean like) {
        if(like) this.countOfLike += 1;
        else this.countOfLike -= 1;
    }
}
