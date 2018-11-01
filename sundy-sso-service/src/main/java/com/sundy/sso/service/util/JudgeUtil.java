package com.sundy.sso.service.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 判断工具类
 */
public class JudgeUtil {

    /**
     * 校验银行卡卡号
     *
     * @param cardNo 银行卡号
     * @return
     */
    public static boolean checkBankCard(String cardNo) {
        char bit = getBankCardCheckCode(cardNo.substring(0, cardNo.length() - 1));
        return cardNo.charAt(cardNo.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (StringUtils.isEmpty(nonCheckCodeCardId) || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 手机号码验证
     *
     * @param mobile 手机号码
     */
    public static boolean checkMobile(String mobile) {
        return Pattern.matches("1\\d{10}", mobile);

    }

    /**
     * 身份证信息验证
     *
     * @param idCardNo
     */
    public static boolean checkIdCard(String idCardNo) {
        String reg = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
        return Pattern.matches(reg, idCardNo);
    }

    /**
     * 邮箱格式验证
     */
    public static boolean checkEmail(String email) {
        String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        return Pattern.matches(regex, email);
    }

    /**
     * 密码格式
     *
     * @param password
     * @return
     */
    public static boolean checkPassWord(String password) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return Pattern.matches(regex, password);
    }

    /**
     * 护照验证
     */
    public static boolean checkPassport(String passportNo) {
        if (StringUtil.isEmpty(passportNo)) {
            return false;
        }
        if (passportNo.length() < 7) {
            return false;
        }
        return true;
    }

    /**
     * 军人证 士兵证
     */
    public static boolean checkArmyman(String armymanNo) {
        if (StringUtil.isEmpty(armymanNo)) {
            return false;
        }
        if (armymanNo.length() < 10 || armymanNo.length() > 18) {
            return false;
        }
        return true;
    }

    /**
     * 出生证
     */
    public static boolean checkbirthCertificate(String birthNo) {
        String regex = "^[A-Za-z]*[0-9]{9}$";
        return Pattern.matches(regex, birthNo);
    }

    /**
     * 邮编号码
     */
    public static boolean checkZipCode(String zipCode) {
        String zipCodeRegex = "^[0-9]{6}$";
        return Pattern.matches(zipCodeRegex, zipCode);
    }

    /**
     * 电话号码校验
     */
    public static boolean checkPhone(String phone) {
        String phoneRegex = "(0|4)\\d{2,3}-\\d{7,8}";
        return Pattern.matches(phoneRegex, phone);
    }
}
