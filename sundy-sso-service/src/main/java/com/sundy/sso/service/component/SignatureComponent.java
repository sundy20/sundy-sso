package com.sundy.sso.service.component;

import com.sundy.sso.service.config.JwtProperties;
import com.sundy.sso.service.util.CodecUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created on 2018/1/10
 *
 * @author plus.wang
 * @description 验签组件
 */
@Component
public class SignatureComponent {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 使用SHA1算法生成签名字符串 32位
     */
    private String signWithSHA1(String data) {
        return DigestUtils.sha1Hex(data + "&" + jwtProperties.getTokenSigningKey());
    }

    public String genData(String phone) {

        String data = String.valueOf(System.currentTimeMillis()) + "&" + CodecUtil.createUUID() + "&" + phone;

        String signature = signWithSHA1(data);

        return CodecUtil.encodeBASE64(data + "&" + signature);
    }

    public boolean checkSignature(String phone, String signData) {

        if (!StringUtils.isEmpty(signData)) {

            String decodeData = CodecUtil.decodeBASE64(signData);

            String s[] = decodeData.split("&");

            if (s.length == 4) {

                String signature = s[3];

                String data = s[0] + "&" + s[1] + "&" + phone;

                String signDest = signWithSHA1(data);

                return signDest.equals(signature);

            } else {

                return false;
            }
        }

        return false;
    }

}
