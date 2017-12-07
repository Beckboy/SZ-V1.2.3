package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 2017/7/17.
 */

public class ReceiptInfoEnity extends BaseEntity {

    /**
     * 发票的类型
     * 1: 收据
     * 2: 增值税普通发票
     * 3: 增值税专用发票
     */
    public String receipt_type = "";

    /**
     * 发票的内容
     * 1: 咨询费
     * 2: 服务费
     */
    public String fee_type = "";

    /**
     * 发票抬头: 单位名称
     */
    public String organization_name = "";

    /**
     * 纳税人识别号: 统一社会信用代码
     */
    public String organization_identify = "";

    /**
     * 地址: 单位注册地址
     */
    public String organization_address = "";

    /**
     * 电话: 单位注册电话
     */
    public String organization_phone = "";

    /**
     * 开户行: 单位开户行名称
     */
    public String organization_bank_name = "";

    /**
     * 账号: 单位开户行账号
     */
    public String organization_bank_account = "";

    /**
     * 寄送人: 发票收件人
     */
    public String receiver_person_name = "";

    /**
     * 寄送人号码: 收件人的联系方式
     */
    public String receiver_person_phone = "";

    /**
     * 寄送地址: 发票寄送地址
     */
    public String receiver_person_address = "";

}
