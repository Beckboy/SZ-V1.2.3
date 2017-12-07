package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.QuestionEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 搜索列表DTO
 * Created by Sugar on 17/3/22.
 */

public class SelectedQuestionDTO extends BaseEntity {

    /**
     * "page": 0,
     * "pagesize": 3,
     * "total": 4,
     * "list": [
     * {
     * "id": 3,
     * "title": "民办学校管理问题",
     * "content": null,
     * "zan": 5,
     * "reply": 6,
     * "publish_time": 1489146100,
     * "collect": 5,
     * "nickname": "拾知管理员",
     * "avatar": "http://omn2drzeo.bkt.clouddn.com/Ft-tiBmvf_2AYagXIhDJw1qaQrEF"
     * },
     * {
     * "id": 1,
     * "title": "招生问题我们要怎么解决问题",
     * "content": null,
     * "zan": 3,
     * "reply": 4,
     * "publish_time": 1490010117,
     * "collect": 6,
     * "nickname": "拾知管理员",
     * "avatar": "http://omn2drzeo.bkt.clouddn.com/Ft-tiBmvf_2AYagXIhDJw1qaQrEF",
     * "name": "招生"
     * },
     * {
     * "id": 4,
     * "title": "民办经营问题",
     * "content": null,
     * "zan": 6,
     * "reply": 7,
     * "publish_time": 1489405300,
     * "collect": 4,
     * "nickname": "拾知管理员",
     * "avatar": "http://omn2drzeo.bkt.clouddn.com/Ft-tiBmvf_2AYagXIhDJw1qaQrEF",
     * "name": "招生"
     * }
     * ]
     */

    /**
     * 当前页
     */
    public int page;
    /**
     * 没有数据大小
     */
    public int pagesize;
    /**
     * 总共页数
     */
    public int total;
    /**
     * 每页的问题数据集合
     */
    public ArrayList<QuestionEntity> list = new ArrayList<>();

}
