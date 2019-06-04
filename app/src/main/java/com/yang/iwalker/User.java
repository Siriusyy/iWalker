package com.yang.iwalker;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Integer id;
    private String nickname;
    private String userName;
    private String password;
    private String image;
    private String desc;
    private Boolean gender;
    private Byte status;
    private String extra;
    private Date createTime;
    private Date modifyTime;
    public User(Integer id, String nickname, String userName, String password, String image, String desc, Boolean gender, Byte status, String extra, Date createTime, Date modifyTime) {

        this.id = id;

        this.nickname = nickname;

        this.userName = userName;

        this.password = password;

        this.image = image;

        this.desc = desc;

        this.gender = gender;

        this.status = status;

        this.extra = extra;

        this.createTime = createTime;

        this.modifyTime = modifyTime;

    }

    public User() {

        super();

    }

    public Integer getId() {

        return id;

    }

    public void setId(Integer id) {

        this.id = id;

    }



    public String getNickname() {

        return nickname;

    }

    public void setNickname(String nickname) {

        this.nickname = nickname == null ? null : nickname.trim();

    }

    public String getUserName() {

        return userName;

    }

    public void setUserName(String userName) {

        this.userName = userName == null ? null : userName.trim();

    }

    public String getPassword() {

        return password;

    }

    public void setPassword(String password) {

        this.password = password == null ? null : password.trim();

    }

    public String getImage() {

        return image;

    }

    public void setImage(String image) {

        this.image = image == null ? null : image.trim();

    }

    public String getDesc() {

        return desc;

    }

    public void setDesc(String desc) {

        this.desc = desc == null ? null : desc.trim();

    }

    public Boolean getGender() {

        return gender;

    }

    public void setGender(Boolean gender) {

        this.gender = gender;

    }

    public Byte getStatus() {

        return status;

    }

    public void setStatus(Byte status) {

        this.status = status;

    }

    public String getExtra() {

        return extra;

    }

    public void setExtra(String extra) {

        this.extra = extra == null ? null : extra.trim();

    }

    public Date getCreateTime() {

        return createTime;

    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;

    }

    public Date getModifyTime() {

        return modifyTime;

    }

    public void setModifyTime(Date modifyTime) {

        this.modifyTime = modifyTime;

    }

}