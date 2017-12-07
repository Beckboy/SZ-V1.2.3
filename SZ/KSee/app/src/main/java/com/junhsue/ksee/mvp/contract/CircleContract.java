package com.junhsue.ksee.mvp.contract;

import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.entity.PostDetailEntity;

import java.util.List;

/**
 * Created by hunter_J on 2017/11/1.
 */

public interface CircleContract {

    interface View extends BaseView{
        void update2DeleteCircle(String circleId);
        void update2AddFavorite(int circlePosition);
        void update2DeleteFavort(int circlePosition);
        void update2AddApproval(int circlePosition);
        void update2DeleteApproval(int circlePosition);
        void upadte2AddComment(int circlePosition);
        void update2DeleteComment(int circlePosition);
        void update2loadData(int loadType, PostDetailListDTO datas);
        void update2loadDataFail();
    }

    interface Presenter extends BasePresenter{
        void loadData(final int loadType, final int pageIndex, int pagesize, String top, String circle_id);
        void deleteCircle(String circleId);
        void addFavort(int circlePosition, String postId);
        void deleteFavort(int circlePosition, String postId);
        void addApproval(int circlePosition, String postId);
        void deleteApproval(int circlePosition, String postId);
        void deleteComment(int circlePosition, String postId);
    }

}
