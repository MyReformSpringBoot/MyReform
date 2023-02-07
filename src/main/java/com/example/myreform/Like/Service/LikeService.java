package com.example.myreform.Like.Service;

import com.example.myreform.User.domain.User;

public interface LikeService {
    Object addLike(Long boardId, String nickname);
    Object removeLike(Long boardId, String nickname);
}
