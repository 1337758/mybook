<template>
    <div class="content_aboutMe">
        <h3>Sun Flower</h3>
        <div class="aboutMe_left">
            <img alt="" src="static/img/img.jpg">
        </div>
        <div class="aboutMe_right">
            <p>姓名：runze</p>
            <p>性别：男</p>
            <p>职业：软件开发</p>
            <p>现居：地球</p>
            <p>爱好：旅游</p>
        </div>
        <div id="about_me_follow">
            <a href="https://weibo.com/" target="_Blank">
                <img id="about_me_sinaweb" alt="" src="static/img/blog/sinaweb.png">
            </a>
            <img id="about_me_weichat" alt="" src="static/img/blog/weichat.png" @click="showWechat">
<!--            <a href="https://github.com/imyanger" target="_Blank">-->
<!--                <img id="about_me_github" alt="" src="static/0img/blog/github.png">-->
<!--            </a>-->
            <a href="https://blog.csdn.net/" target="_Blank">
                <img id="about_me_csdn" alt="" src="static/img/blog/csdn.png">
            </a>
            <!-- 引入微信公众号关注的模态框 -->
            <el-dialog :visible.sync="dialogVisible" :close-on-click-modal="false" width="450px">
                <center>
                    <el-row>
                        <img src="static/img/blog/erweima.jpg">
                    </el-row>
                    <el-row>
                        <span id="wechat_des">喜欢我你就关注我</span>
                    </el-row>
                </center>
           </el-dialog>
        </div>
    </div>
</template>

<script>
    export default {
        data(){
            return {
                dialogVisible: false
            }
        },
        methods: {
            showWechat(){
                this.dialogVisible = true;
            }
        }
    }
</script>


<style scoped>
    .content_aboutMe {
        width: 100%;
        height: 250px;
        margin-bottom: 20px;
    }
    .aboutMe_left {
        width: 90px;
        height: 120px;
        float: left;
        margin: 5px 0 10px 10px;
    }
    .aboutMe_left img {
        width: 90px;
        height: 120px;
    }
    .content_aboutMe h3 {
        line-height: 40px;
        color: #38485a;
        font-size: 18px;
        border-bottom: 1px solid #ff3300;
        text-align: center;
        height: 40px;
        font-weight: 500;
    }
    .aboutMe_right {
        height: 120px;
        width: 160px;
        float: right;
        margin: 5px 5px 0 10px;
    }
    .aboutMe_right p {
        line-height: 16px;
        display: block;
        -webkit-margin-start: 0px;
        -webkit-margin-end: 0px;
        font-size: 14px;
        line-height: 24px;
    }
    #about_me_follow {
        width: 280px;
        height: 65px;
        margin-top: 150px;
    }
    #about_me_sinaweb, #about_me_weichat, #about_me_github {
        width: 40px;
        height: 40px;
        float: left;
        margin-left: 23px;
        cursor: pointer;
    }
    #about_me_csdn {
        width: 36px;
        height: 36px;
        float: left;
        margin-left: 23px;
        cursor: pointer;
    }
    #wechat_des {
        font-size: 20px;
        font-weight: 500;
    }
</style>
