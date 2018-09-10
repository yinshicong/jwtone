package com.example.dto;

import lombok.Data;

/**
 * Created by Administrator on 2018/8/20 0020.
 */
@Data
public class LoginPara {

        private String clientId;

        private String userName;

        private String password;

        private String captchaCode;

        private String captchaValue;

}
