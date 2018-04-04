package com.android.renly.edu_yunzhi.Bean;

public class User {
    private int id;//用户id
    private String username;//用户姓名
    private String nickname;//昵称
    private String number;//����
    private String idCard;//���֤��
    private String password;//密码
    private String imgUrl;//头像地址
    private String phone;//手机号码
    private String sex;//性别
    private String registerTime;//注册时间
    private String email;//邮箱地址
    private String remark;//
//    private Department department;//

    public User() {
        super();
    }

    //学号 用户名 密码 邮箱
    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //学号 用户名 密码 邮箱 带头像注册
    public User(int id, String username, String password, String imgUrl, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.imgUrl = imgUrl;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
