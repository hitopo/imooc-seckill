//使用模块式编程
//避免js代码过于混乱
// 存放seckill主要交互的逻辑
var seckill = {
    // 封装秒杀业务传递的url
    URL: {
        //获取服务器时间url
        now: function () {
            return "/seckill/time/now";
        },
        //获取秒杀接口url
        exposer: function (seckillId) {
            return "/seckill/" + seckillId + "/exposer";
        },
        //执行秒杀操作url
        execution: function (seckillId, md5) {
            return "/seckill/" + seckillId + "/" + md5 + "/execution";
        }
    },
    //验证手机号
    validatePhone: function (phone) {
        //isNaN()返回的是传递的参数不是数字
        return !!(phone && phone.length === 11 && !isNaN(phone));
    },
    //执行秒杀操作
    handleSeckill: function (seckillId, node) {
        //将节点隐藏，并将按钮安装在里面
        //在节点中添加按钮
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        //获取秒杀接口md5
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //回调函数
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀接口
                    var md5 = exposer['md5'];
                    //获取秒杀地址
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:" + killUrl);  //todo
                    //绑定点击事件
                    //只绑定一次，防止用户多次点击发送大量请求给服务器
                    //造成服务器宕机
                    $('#killBtn').one('click', function () {
                        //执行秒杀操作
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                if(stateInfo === "秒杀成功"){
                                    node.html('<span class="label label-success">' + stateInfo + '</span>');
                                } else {
                                    node.html('<span class="label label-danger">' + stateInfo + '</span>');
                                }
                            }
                        });
                    });
                    //显示保存好信息的结点
                    node.show();
                } else {
                    //还未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新进行计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                //获取秒杀接口失败
                console.log("result:" + result);  //todo
            }
        });
    },
    //计时交互
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