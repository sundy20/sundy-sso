package com.sundy.sso.service.web.vo;

import com.sundy.sso.service.validate.group.UserLoginGroup;
import com.sundy.sso.service.validate.group.UserRegistGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 注册用户时的输入参数
 */
public class EnUserVO {

    @NotBlank(groups = {UserRegistGroup.class, UserLoginGroup.class}, message = "email can not be null")
    private String email;

    @NotBlank(groups = {UserRegistGroup.class, UserLoginGroup.class}, message = "Use 6-30 characters with a mix of letters and numbers")
    @Length(groups = {UserRegistGroup.class, UserLoginGroup.class},max = 30,min = 6, message = "Use 6-30 characters with a mix of letters and numbers")
    private String password;

    private String signData;

    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
