package com.example.myreform.Like.Service;

import com.example.myreform.User.domain.User;

public interface LikeService {
    Object addLike(Long boardId, String loginId);
    Object removeLike(Long boardId, String loginId);
}
