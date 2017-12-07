package com.junhsue.ksee.mvp.presenter;

import com.junhsue.ksee.R;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.mvp.contract.CircleContract;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.ToastUtil;

/**
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * Created by hunter_J on 2017/11/1.
 */

public class CirclePresenter implements CircleContract.Presenter {

    private CircleContract.View view;

    public CirclePresenter(CircleContract.View view) {
        this.view = view;
    }

    @Override
    public void loadData(final int loadType, final int pageIndex, int pagesize, String top, String circle_id) {
        final String mPageIndex = String.valueOf(pageIndex);
        String mPagesize = String.valueOf(pagesize);
        OKHttpNewSocialCircle.getInstance().getCircleBarList(circle_id,mPageIndex,mPagesize,top, new RequestCallback<PostDetailListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                if (view != null){
                    view.update2loadDataFail();
                }
                switch (errorCode){
                    case NetResultCode.SERVER_NO_DATA:
                        break;
                }
            }

            @Override
            public void onSuccess(PostDetailListDTO response) {
                if (view!=null){
                    view.update2loadData(loadType, response);
                }
            }
        });
    }

    /**
     * @Title: deleteCircle
     * @Description: 删除动态
     * @param circleId
     * @return: void 返回类型
     * @throws
     */
    @Override
    public void deleteCircle(final String circleId) {
    }

    @Override
    public void addFavort(final int circlePosition, final String postId) {
        OKHttpNewSocialCircle.getInstance().collectPost(postId, "14", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(MyApplication.getApplication()).setContent("收藏失败").setShow();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                if (view!=null){
                    view.update2AddFavorite(circlePosition);
                }
            }
        });
    }

    /**
     *
     * @Title: deleteFavort
     * @Description: 取消收藏
     * @param circlePosition
     * @return void    返回类型
     * @throws
     */
    @Override
    public void deleteFavort(final int circlePosition, String postId) {
        OKHttpNewSocialCircle.getInstance().cancelCollectPost(postId, "14", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(MyApplication.getApplication()).setContent("取消收藏失败").setShow();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                if (view!=null){
                    view.update2DeleteFavort(circlePosition);
                }
            }
        });
    }

    /**
     *
     * @Title: AddApproval
     * @Description: 点赞
     * @param circlePosition
     * @return void    返回类型
     * @throws
     */
    @Override
    public void addApproval(final int circlePosition, String postId) {
        OKHttpNewSocialCircle.getInstance().postCommentApproval(postId, "14", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(MyApplication.getApplication()).setContent("点赞失败").setShow();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                if (view != null){
                    view.update2AddApproval(circlePosition);
                }
            }
        });
    }

    @Override
    public void deleteApproval(final int circlePosition, String postId) {
        OKHttpNewSocialCircle.getInstance().postCommentCancelApproval(postId, "14", new RequestCallback<ResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                ToastUtil.getInstance(MyApplication.getApplication()).setContent("取消点赞失败").setShow();
            }

            @Override
            public void onSuccess(ResultEntity response) {
                if (view != null){
                    view.update2DeleteApproval(circlePosition);
                }
            }
        });
    }

    /**
     * @Title: addComment
     * @Description: 增加评论
     * @param content
     * @return void 返回类型
     * @throws
     */
    public void addComment(final String content, String postId){
    }

    /**
     * @Title: deleteComment
     * @Description: 删除评论
     * @param circlePosition
     * @return void    返回类型
     * @throws
     */
    @Override
    public void deleteComment(final int circlePosition, String postId) {
    }

    /**
     * 清除对外部对象的引用，防止内存泄露
     */
    public void recycle(){this.view = null;}

}
