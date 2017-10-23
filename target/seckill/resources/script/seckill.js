//使用模块式编程
//避免js代码过于混乱
// 存放seckill主要交互的逻辑
var seckill = {
    // 封装秒杀业务传递的url
    URL: {
        now: function () {
            return "/seckill/time/now";
        }
    },
    //验证手机号
    validatePhone: function (phone) {
        //isNaN()返回的是传递的参数不是数字
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    handleSeckill: function (seckillId, node) {
        alert('秒杀进行中！');
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //秒杀尚未开始
            //开始计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //显示时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀进行中
            //秒杀逻辑
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        init: function (params) {
            // 手机验证和登录，计时交互
            // 规划交互逻辑
            // 从cookie中寻找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //cookie中不存在手机号或者手机号格式不对
                // 显示填写手机号弹出层
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false   //关闭键盘事件

                });
                //绑定点击事件
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhonekey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //将手机号写入cookie
                        $.cookie('killPhone', inputPhone, {
                            expires: 7, path: 'seckill'
                        });
                        //刷新界面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide()
                            .html('<label class="label label-danger">手机号错误！</label>')
                            .show(300);
                    }
                });
            } else {
                //cookie中存在手机号
                //计时交互
                var startTime = params['startTime'];
                var endTime = params['endTime'];
                var seckillId = params['seckillId'];
                //发送ajax请求获取服务器当前时间
                $.get(seckill.URL.now(), {}, function (result) {
                    if (result && result['success']) {
                        //返回了结果，并且成功
                        var nowTime = result['data'];
                        //显示计时
                        seckill.countdown(seckillId, nowTime, startTime, endTime);
                    } else {
                        console.log('result' + result);
                    }
                });
            }
        }
    }

};