package com.example.controller;

import com.example.Entity.UserInfoEntity;
import com.example.config.Audience;
import com.example.dto.AccessToken;
import com.example.dto.LoginPara;
import com.example.repositoy.IUserInfoRepository;
import com.example.util.JwtHelper;
import com.example.util.MyUtils;
import com.example.util.ResultMsg;
import com.example.util.ResultStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * JWT流程。
 * https://cnodejs.org/topic/5b0c4a7b8a4f51e140d942fc
 *
 * Created by Administrator on 2018/8/20 0020.
 */
@RestController
public class JsonWebToken {

    @Autowired
    private Audience audienceEntity;

    @Autowired
    private IUserInfoRepository userRepositoy;

    @PostMapping("oauth/token")
    public Object getAccessToken(@RequestBody LoginPara loginPara)
    {
        ResultMsg resultMsg;
        try
        {
            if(loginPara.getClientId() == null
                    || (loginPara.getClientId().compareTo(audienceEntity.getClientId()) != 0))
            {
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_CLIENTID.getErrcode(),
                        ResultStatusCode.INVALID_CLIENTID.getErrmsg(), null);
                return resultMsg;
            }

            //验证码校验在后面章节添加


            //验证用户名密码
            UserInfoEntity user = userRepositoy.findUserInfoByName(loginPara.getUserName());
            if (user == null)
            {
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),
                        ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
                return resultMsg;
            }
            else
            {
                String md5Password = MyUtils.getMD5(loginPara.getPassword()+user.getSalt());

                if (md5Password.compareTo(user.getPassword()) != 0)
                {
                    resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),
                            ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
                    return resultMsg;
                }
            }

            //拼装accessToken
            String accessToken = JwtHelper.createJWT(
                    loginPara.getUserName(),
                    String.valueOf(user.getName()),
                    user.getRole(),
                    audienceEntity.getClientId(),
                    audienceEntity.getName(),
                    audienceEntity.getExpiresSecond() * 1000,
                    audienceEntity.getBase64Secret()
            );

            //返回accessToken
            AccessToken accessTokenEntity = new AccessToken();
            accessTokenEntity.setAccess_token(accessToken);
            accessTokenEntity.setExpires_in(audienceEntity.getExpiresSecond());
            accessTokenEntity.setToken_type("bearer");
            resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(),
                    ResultStatusCode.OK.getErrmsg(), accessTokenEntity);
            return resultMsg;

        }
        catch(Exception ex)
        {
            resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(),
                    ResultStatusCode.SYSTEM_ERR.getErrmsg(), null);
            return resultMsg;
        }
    }


    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

}

