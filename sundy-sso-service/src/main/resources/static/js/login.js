$(function () {

    var redirectMainUrl = $.cookie('redirectKey');

//    登录
    $('.login .phone input').blur(function () {
        if ($(this).val() == '') {
            $(this).siblings('.width_100').removeClass('hidden');
        }
        ;
    });
    $('.login .phone input').focus(function () {
        $(this).siblings('.width_100').addClass('hidden');
    });

    $('#registAid').click(function () {
        $(this).attr('href', '/toregist?redirect=' + redirectMainUrl)
    });

    $('#loginAid').click(function () {
        $(this).attr('href', '/tologin?redirect=' + redirectMainUrl)
    });

    $('#resetPasswordAid').click(function () {
        $(this).attr('href', '/toresetpwd?redirect=' + redirectMainUrl)
    });


//  点击登录
    $('.login .submit').click(function () {
        var phone = $('.login .phone input').val();
        var pass = $('.login .password input').val();
        var verify = $('.login .verify input').val();

        if (verify) {

            if (phone !== '' && pass !== "" && verify !== "") {

                if (verify.length != 4) {

                    $('.login .content form input.width_min').siblings('.width_100').removeClass('hidden');

                    return false;
                }

                var checkCaptchaResult = checkCaptchaCode(phone, verify, $('.login .content form input.width_min').siblings('.width_100'));
                //ajax post 登录
                if (checkCaptchaResult) {
                    login(phone, pass, checkCaptchaResult, redirectMainUrl);
                }

            } else {
                $('.login p.msg').text("请填全信息");
                $('.login p.msg').removeClass('hidden');
                setTimeout(function () {
                    $('.login p.msg').addClass('hidden');
                }, 3000)
            }

        } else {

            if (phone !== '' && pass !== "") {

                login(phone, pass,null, redirectMainUrl);

            } else {
                $('.login p.msg').text("请填全信息");
                $('.login p.msg').removeClass('hidden');
                setTimeout(function () {
                    $('.login p.msg').addClass('hidden');
                }, 3000)
            }
        }

    });

    function login(phone, password, signature, redirectUrl) {

        var userObject = new Object();

        userObject.phone = phone;

        userObject.password = password;

        userObject.signData = signature;

        $.ajax({
            url: '/login',
            type: 'POST',
            data: JSON.stringify(userObject),
            dataType: "json",
            contentType: "application/json",
            async: false,
            cache: false,
            success: function (data) {
                console.log(data);
                if (data && data.code == '000000') {

                    window.location.href = redirectUrl;

                    return false;

                } else {

                    var resMap = data.data;

                    var capSig = resMap.captchaSign;

                    if (capSig && capSig === 'true') {

                        var imgUrl = '/captcha?phone=' + phone;

                        $('.login .content form .verify img').attr("src", imgUrl);

                        $('.login .content form .verify').show();

                    }

                    var resMessage = data.message;

                    $('.login p.msg').text(resMessage);

                    $('.login p.msg').removeClass('hidden');

                    setTimeout(function () {
                        $('.login p.msg').text(resMessage).addClass('hidden');
                    }, 3000)

                    return false;

                }
            },
            error: function (err) {
                console.log(err)
            }
        })

    }

    function regist(phone, password, username, signature, redirectUrl) {

        var userObject = new Object();

        userObject.phone = phone;

        userObject.password = password;

        userObject.username = username;

        userObject.signData = signature;

        $.ajax({
            url: '/regist',
            type: 'POST',
            data: JSON.stringify(userObject),
            dataType: "json",
            contentType: "application/json",
            async: false,
            cache: false,
            success: function (data) {
                if (data && data.code == '000000') {

                    window.location.href = redirectUrl;

                    return false;

                } else {

                    var resMessage = data.message;
                    $('.register p.msg').text(resMessage);
                    $('.register p.msg').removeClass('hidden');
                    setTimeout(function () {
                        $('.register p.msg').addClass('hidden');
                    }, 3000)

                    return false;
                }
            },
            error: function (err) {
                console.log(err)
            }
        })

    }


    function resetPassword(phone, password, signature, redirectUrl) {

        var userObject = new Object();

        userObject.phone = phone;

        userObject.password = password;

        userObject.signData = signature;

        $.ajax({
            url: '/resetpwd',
            type: 'POST',
            data: JSON.stringify(userObject),
            dataType: "json",
            contentType: "application/json",
            async: false,
            cache: false,
            success: function (data) {
                if (data && data.code == '000000') {

                    window.location.href = redirectUrl;

                    return false;

                } else {

                    var resMessage = data.message;
                    $('.resetPass p.msg').text(resMessage);
                    $('.resetPass p.msg').removeClass('hidden');
                    setTimeout(function () {
                        $('.resetPass p.msg').addClass('hidden');
                    }, 3000)

                    return false;
                }
            },
            error: function (err) {
                console.log(err)
            }
        })

    }


    $('.register .protocol i').click(function () {
        $(this).toggleClass('active');
    });

    //登录页获取图片验证码按钮
    $('.login .content form button').click(function () {

        var timestamp = new Date().getTime();

        $('.login .content form img').attr('src', $('.login .content form img').attr('src') + '&' + timestamp);

        return false;

    });
    //注册页获取手机验证码按钮
    $('.register .content form button').click(function (e) {
        if (e && e.preventDefault) {
            e.preventDefault();
        } else {
            window.event.returnValue = false;
        }
        var phone = $('.register .phone input').val();

        var reg = /^1[34578]\d{9}$/;

        if (!reg.test(phone)) {
            $('.register .phone input').siblings('.width_100').removeClass('hidden');
            return false;

        }

        ajaxPhoneCode(phone, 1);

        settime($(this));

    });

    //重置密码页获取手机验证码按钮
    $('.reset .content form .message button').click(function () {

        var phone = $('.reset .phone input').val();

        var reg = /^1[34578]\d{9}$/;

        if (!reg.test(phone)) {
            $('.reset .phone input').siblings('.width_100').removeClass('hidden');
            return false;
        }

        ajaxPhoneCode(phone, 2);

        settime($(this));

    });

    function ajaxPhoneCode(phone, sign) {

        $.ajax({
            url: '/phonecode/set?phone=' + phone + '&sign=' + sign,
            type: 'GET',
            async: false,
            cache: false,
            success: function (data) {
                console.log(data);
            },
            error: function (err) {
                console.log(err);
            }
        })

    }

    //手机验证码输入框失去焦点事件
    $('.register .content form input.width_min').blur(function () {

        var phone = $('.register .phone input').val();

        var phoneCode = $(this).val();

        var reg = /^\d{4}$/;

        if (!reg.test(phoneCode)) {
            $(this).siblings('.width_100').removeClass('hidden');
            return false;
        } else {
            $(this).siblings('.width_100').addClass('hidden');
        }

        checkPhoneCode(phone, phoneCode, $(this).siblings('.width_100'));
    });

    //图片验证码输入框失去焦点事件
    $('.login .content form input.width_min').blur(function () {

        var phone = $('.login .phone input').val();

        var capCode = $(this).val();

        if (capCode.length != 4) {

            $(this).siblings('.width_100').removeClass('hidden');

            return false;

        } else {

            $(this).siblings('.width_100').addClass('hidden');
        }

        checkCaptchaCode(phone, capCode, $(this).siblings('.width_100'));
    });

    function checkCaptchaCode(phone, capCode, elem) {
        var flag = false;
        $.ajax({
            url: '/captcha/valid?phone=' + phone + '&captchaCode=' + capCode,
            type: 'GET',
            async: false,
            cache: false,
            success: function (data) {
                console.log(data);
                if (data && data.code == '000000') {
                    flag = data.data;
                } else {
                    elem.text(data.message).removeClass('hidden');
                    flag = false;
                }
            },
            error: function (err) {
                console.log(err);
            }
        })
        return flag;
    }

    function checkPhoneCode(phone, phoneCode, elem) {

        var flag = false;

        $.ajax({
            url: '/phonecode/valid?phone=' + phone + '&phonecode=' + phoneCode,
            type: 'GET',
            async: false,
            cache: false,
            success: function (data) {
                console.log(data);
                if (data && data.code == '000000') {
                    flag = data.data;
                } else {
                    flag = false;
                    elem.text(data.message).removeClass('hidden');
                }
            },
            error: function (err) {
                console.log(err);
            }
        });

        return flag;
    }

    $('.register .phone input').blur(function () {
        var reg = /^1[34578]\d{9}$/;
        if (!reg.test($(this).val())) {
            $(this).siblings('.width_100').removeClass('hidden');
        }
    });

    $('.register .phone input').focus(function () {
        $(this).siblings('.width_100').addClass('hidden');
    });

    $('.register .passwords input').blur(function () {
        var pass_1 = $('.register .password input').val();
        var pass_2 = $('.register .passwords input').val();
        if (pass_1 !== pass_2) {
            $('.register .passwords').find('.width_100').removeClass('hidden');
        }
    });

    $('.register .passwords input,.register .password input').focus(function () {
        $('.register .passwords').find('.width_100').addClass('hidden');
    })

    //注册提交
    $('.register .submit').click(function () {
        var phones = $('.register .phone input').val();
        var names = $('.register .name input').val();
        var verifys = $('.register .verify input').val();
        var password = $('.register .password input').val();
        var passwords = $('.register .passwords input').val();
        var hasVal = $('.register').find('i').hasClass('active');
        if (phones !== '' && names !== '' && verifys !== '' && password !== '' && passwords !== '' && hasVal === true) {

            var reg = /^\d{4}$/;

            if (!reg.test(verifys)) {
                $('.register .content form input.width_min').siblings('.width_100').removeClass('hidden');
                return false;
            }

            var checkPhoneResult = checkPhoneCode(phones, verifys, $('.register .content form input.width_min').siblings('.width_100'));
            //ajax post 注册
            if (checkPhoneResult) {
                regist(phones, password, names, checkPhoneResult, redirectMainUrl);
            }

        } else {
            $('.register p.msg').text("请填全信息后再注册");
            $('.register p.msg').removeClass('hidden');
            setTimeout(function () {
                $('.register p.msg').addClass('hidden');
            }, 3000);
        }
    });

//    找回密码
    $('.reset .phone input').blur(function () {
        var reg = /^1[34578]\d{9}$/;
        if (!reg.test($(this).val())) {
            $(this).siblings('.width_100').removeClass('hidden');
            return false;
        }

        //判断手机有没有注册
        checkPhoneRegist($(this).val(), $(this).siblings('.width_100'));

    });

    function checkPhoneRegist(phone, elem) {

        $.ajax({
            url: '/checkregist/' + phone,
            type: 'GET',
            async: false,
            cache: false,
            success: function (data) {
                console.log(data);
                if (data && data.code == '000000') {

                } else {
                    elem.text(data.message).removeClass('hidden');
                    return false;
                }
            },
            error: function (err) {
                console.log(err);
            }
        })

    }

    $('.reset .phone input').focus(function () {
        $(this).siblings('.width_100').addClass('hidden');
    });

//    重置密码
    $('.reset .submit').click(function () {
        var phone = $('.reset .phone input').val();
        var message = $('.reset .message input').val();
        if (phone !== '' && message !== "") {

            var reg = /^\d{4}$/;

            if (!reg.test(message)) {
                $('.register .content form input.width_min').siblings('.width_100').removeClass('hidden');
                return false;
            }

            var checkPhoneResult = checkPhoneCode(phone, message, $('.register .content form input.width_min').siblings('.width_100'));

            if (checkPhoneResult) {
                window.location.href = 'http://' + window.location.host + '/tosetpwd?phone=' + phone + '&redirect=' + redirectMainUrl + '&signature=' + checkPhoneResult;
            }

        } else {
            $('.reset p.msg').removeClass('hidden');
            setTimeout(function () {
                $('.reset p.msg').addClass('hidden');
            }, 3000)
        }
    });


    $('.resetPass .passwords input').blur(function () {
        var pass_1 = $('.password input').val();
        var pass_2 = $('.passwords input').val();
        if (pass_1 !== pass_2) {
            $(this).siblings('.width_100').removeClass('hidden');
        }
    });

    $('.resetPass .passwords input ,.resetPass .password input').focus(function () {
        $(this).siblings('.width_100').addClass('hidden');
    })

    //    设置密码
    $('.resetPass .submit').click(function () {
        var pass_1 = $('.password input').val();
        var pass_2 = $('.passwords input').val();
        if (pass_1 === pass_2) {

            var locationHref = window.location.href;

            var phone = locationHref.substring(locationHref.indexOf("phone=") + 6, locationHref.indexOf('&'));

            var signature = locationHref.substring(locationHref.indexOf("signature=") + 10);

            resetPassword(phone, pass_1, signature, redirectMainUrl);

        } else {
            $('.resetPass p.msg').removeClass('hidden');
            setTimeout(function () {
                $('.resetPass p.msg').addClass('hidden');
            }, 3000);
        }
    })

    //    倒计时
    var countdown = 60;

    function settime(val) {
        if (countdown == 0) {
            val.attr('disabled', false);
            val.html('');
            val.html('获取验证码');
            countdown = 60;
            val.css({
                'background': '#016ad7',
                'cursor': 'pointer'
            });
        } else {
            val.attr('disabled', true);
            val.css({
                'background': '#ccc',
                'cursor': 'not-allowed'
            });
            val.html('');
            val.html(countdown + " s")
            countdown--;
            setTimeout(function () {
                settime(val)
            }, 1000)
        }
    }

})