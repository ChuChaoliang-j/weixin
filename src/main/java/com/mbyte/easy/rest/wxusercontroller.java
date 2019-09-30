//package com.mbyte.easy.rest;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.mbyte.easy.common.controller.BaseController;
//import com.mbyte.easy.common.web.AjaxResult;
//import com.mbyte.easy.rest.entity.WeChatAppLoginReq;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * <p>
// *     这是微信小程序的登录接口
// * </p>
// * @Author 吴天豪
// * @Date 2019.4.24
// */
//@Controller
//@RequestMapping("/rest")
//public class wxusercontroller extends BaseController {
//
//    /**
//     * TODO 此处填写小程序的appid
//     **/
//     //  @Value("${weixin.appid}")
//        private String appid = "wx373707216f76359b";
//         private String secret = "cee39d50fd49ecb594deb0add18f21d8";
//            @Autowired
//            ITUserService itUserService;
//
//            @Autowired
//            ITProjectService itProjectService;
//
//            @Autowired
//            ITUnitpriceService itUnitpriceService;
//
//            @Autowired
//            ITCprojectService itCprojectService;
//
//            /**
//             *这个是用来验证登录的
//             */
//            @ResponseBody
//            @RequestMapping(value = {"/getId"})
//
//            public AjaxResult getId(@ModelAttribute WeChatAppLoginReq req, String encryptedData, String iv) throws IOException {
//                System.out.println("这个是登录的");
//                /**
//                 * TODO 小程序登录时获取的 code
//                 **/
//                String jsCode = req.getCode();
//                /*
//                 * TODO 授权类型，此处只需填写 authorization_code
//                 */
//                String grantType = "authorization_code";
//
//                String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=" + grantType;
//                System.out.println(url);
//                CloseableHttpClient client = HttpClients.createDefault();
//                HttpGet httpGet = new HttpGet(url);
//                CloseableHttpResponse res = client.execute(httpGet);
//                if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                    HttpEntity entity = res.getEntity();
//                    String result = EntityUtils.toString(entity, "UTF-8");
//                    JSONObject jsonObject = JSON.parseObject(result);
//                    TUser weixinUser = new TUser();
//                    System.out.println(jsonObject);
//                    String openid = jsonObject.get("openid").toString();
//                    weixinUser.setOpenId(openid);
//                    QueryWrapper<TUser> queryWrapper = new QueryWrapper<TUser>();
//                    if (weixinUser.getOpenId() != null && !"".equals(weixinUser.getOpenId())) {
//                        queryWrapper = queryWrapper.eq("openId", weixinUser.getOpenId());
//                    }
//                    TUser oldUser = itUserService.getOne(queryWrapper);
//                    weixinUser.setUpdatetime(LocalDateTime.now());
//                    if (oldUser != null) {
//                        itUserService.update(weixinUser, queryWrapper);
//                    } else {
//
//                        itUserService.save(weixinUser);
//                    }
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    String openId = jsonObject.getString("openid");
//                    map.put("openId", openId);
//                    return success(map);
//                }
//                return error();
//            }
//
//    /**
//     * 绑定用户信息
//     *
//     * @param
//     * @return object
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/bindingUserInfo"})
//    public AjaxResult bindingUserInfo(@ModelAttribute TUser weixinUser) {
//        if (weixinUser.getOpenId() != null && !"".equals(weixinUser.getOpenId())) {
//            QueryWrapper<TUser> queryWrapper = new QueryWrapper<TUser>();
//            System.out.println("这是getOpenId"+weixinUser.getOpenId());
//            queryWrapper = queryWrapper.eq("openId", weixinUser.getOpenId());
//            boolean flag = itUserService.update(weixinUser, queryWrapper);
//            if (flag) {
//                return success(weixinUser);
//
//            } else {
//                error("更新失败");
//            }
//           //  return toAjax(itUserService.update(weixinUser, queryWrapper));
//
//        }
//        return error("传参错误");
//    }
//
//    /**
//     * 检查绑定状态
//     *
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/checkBinding"})
//    public AjaxResult checkBinding(@ModelAttribute TUser weixinUser) {
//        if (weixinUser.getOpenId() != null && !"".equals(weixinUser.getOpenId())) {
//            QueryWrapper<TUser> queryWrapper = new QueryWrapper<TUser>();
//            queryWrapper = queryWrapper.eq("openId", weixinUser.getOpenId());
//            weixinUser = itUserService.getOne(queryWrapper);
//            if (weixinUser.getPhone() != null && !"".equals(weixinUser.getPhone())) {
//                return success();
//            } else {
//                return error();
//            }
//        }
//        return error("传参错误");
//    }
//
//    /**
//     * 显示用户信息
//     * @param weixinUser
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/queryUserInfo"})
//    public AjaxResult queryUserInfo(@ModelAttribute TUser weixinUser) {
//        //查询个人信息
//        System.out.println("这是查询个人信息");
//        QueryWrapper<TUser> ConferenceQueryWrapper = new QueryWrapper<TUser>();
//        if(weixinUser.getOpenId()!=null && !"".equals(weixinUser.getOpenId())){
//            ConferenceQueryWrapper = ConferenceQueryWrapper.eq("openId", weixinUser.getOpenId());
//            weixinUser = itUserService.getOne(ConferenceQueryWrapper);
//            return success(weixinUser);
//        }else {
//            System.out.println("这是error");
//            return error();
//        }
//    }
//
//    /**
//     *用户自己修改用户信息
//     * @param weixinUser
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/updateUserinfo"})
//    public AjaxResult upUserinfo(@ModelAttribute TUser weixinUser) {
//        //修改个人信息
//        QueryWrapper<TUser> updateQueryWrapper = new QueryWrapper<TUser>();
//        if (weixinUser.getOpenId() != null && !"".equals(weixinUser.getOpenId())) {
//            updateQueryWrapper = updateQueryWrapper.eq("openId", weixinUser.getOpenId());
//            boolean flag = itUserService.update(weixinUser, updateQueryWrapper);
//            if (flag) {
//        return success(weixinUser);
//            //    return toAjax(itUserService.update(weixinUser, updateQueryWrapper));
//            } else {
//                error("更新失败");
//            }
//        }
//        else{
//            itUserService.save(weixinUser);
//            return success(weixinUser);
//        }
//        return error("传参错误");
//    }
//
//    /**
//     * 添加计时任务书
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/addtPrjeoct"})
//    public AjaxResult addprojectplan(@ModelAttribute TProject tProject) {
//            QueryWrapper<TProject> addQueryWarpper = new QueryWrapper<TProject>();
//            //判断是否记录存在 存在则不用增加 否则就要增加
//            if(tProject.getOpenId()!=null && !"".equals(tProject.getOpenId())){
//
//                itProjectService.save(tProject);
//                return success();
//            }
//            else{
//                return error("传参失败");
//            }
//    }
//
//    /**
//     * 添加计件任务书
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/addCp"})
//    public AjaxResult addCProject(@ModelAttribute TCproject tCProject) {
//        QueryWrapper<TCproject> addQueryCWarpper = new QueryWrapper<TCproject>();
//        //判断是否记录存在 存在则不用增加 否则就要增加
//        if(tCProject.getOpenId()!=null && !"".equals(tCProject.getOpenId())){
//
//            itCprojectService.save(tCProject);
//            return success();
//        }
//        else{
//            return error("传参失败");
//        }
//    }
//    /**
//     * 获取任务书信息,计时任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/gettaskplan")
//    public AjaxResult gettaskplan(@ModelAttribute TProject tProject) {
//        QueryWrapper<TProject> queryWrapper = new QueryWrapper<TProject>();
//        if (tProject.getOpenId() !=null) {
//            queryWrapper = queryWrapper.eq("openId", tProject.getOpenId());
//            tProject = itProjectService.getOne(queryWrapper);
//            System.out.println("========================"+tProject+"=============================");
//            return success(tProject);
//        }else{
//            return error();
//        }
//    }
//    /**
//     * 获取任务书信息,计件任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/getCtaskplan")
//    public AjaxResult getCtaskplan(@ModelAttribute TCproject tcProject) {
//        QueryWrapper<TCproject> queryCWrapper = new QueryWrapper<TCproject>();
//        if (tcProject.getId() > 0) {
//            queryCWrapper = queryCWrapper.eq("id", tcProject.getId());
//            tcProject = itCprojectService.getOne(queryCWrapper);
//            return success(tcProject);
//        }else{
//            return error();
//        }
//    }
//
//
//    /**
//     * 修改任务书信息,计时任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/updatetaskplan")
//    public AjaxResult updatetaskplan(@ModelAttribute TProject tproject) {
//        QueryWrapper<TProject> uptproject = new QueryWrapper<TProject>();
//        if (tproject.getId() > 0) {
//            uptproject = uptproject.eq("id", tproject.getId());
//            boolean flag = itProjectService.update(tproject,uptproject);
//            if(flag){
//                return success("修改成功！");
//            }
//            else{
//                return error();
//            }
//        }else{
//            return error();
//        }
//    }
//
//    /**
//     * 修改任务书信息,计件任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/upCdatetaskplan")
//    public AjaxResult upCdatetaskplan(@ModelAttribute TCproject tcProject) {
//        QueryWrapper<TCproject> uptproject = new QueryWrapper<TCproject>();
//        if (tcProject.getId() > 0) {
//            uptproject = uptproject.eq("id", tcProject.getId());
//            boolean flag = itCprojectService.update(tcProject,uptproject);
//            if(flag){
//                return success("修改成功！");
//            }
//            else{
//                return error();
//            }
//        }else{
//            return error();
//        }
//    }
//
//    /**
//     * 搜索任务书状态,计时任务书，传入状态值，根据状态
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/searchtaskplan")
//    public AjaxResult searchtaskplan(@ModelAttribute TProject tProject) {
//        QueryWrapper<TProject> uptproject = new QueryWrapper<TProject>();
//        System.out.println("==============="+tProject.getProjectState()+"=============");
//        if (tProject.getProjectState() !=null) {
//            System.out.println("===============start=============");
//            uptproject = uptproject.eq("project_state", tProject.getProjectState());
//            tProject = itProjectService.getOne(uptproject);
//            return success(tProject);
//        }
//        else{
//            return error("没找到！");
//        }
//    }
//
//
//    /**
//     * 搜索任务书状态,计件任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/searchtaskCplan")
//    public AjaxResult searchtaskCplan(@ModelAttribute TCproject tcProject) {
//        QueryWrapper<TCproject> uptproject = new QueryWrapper<TCproject>();
//        if (tcProject.getId() > 0) {
//            uptproject = uptproject.eq("id", tcProject.getId());
//            tcProject = itCprojectService.getOne(uptproject);
//            return success(tcProject);
//        }else{
//            return error("没找到！");
//        }
//    }
//
//    /**
//     * 同意计时任务书
//     */
//    @ResponseBody
//    @RequestMapping(value = {"/agreetPrjeoct"})
//    public AjaxResult agreetPrjeoct(@ModelAttribute TProject tProject) {
//        QueryWrapper<TProject> addQueryWarpper = new QueryWrapper<TProject>();
//        //判断是否记录存在 存在则不用增加 否则就要增加
//        if(tProject.getOpenId()!=null && !"".equals(tProject.getOpenId())){
//            boolean flag = itProjectService.update(tProject,addQueryWarpper);
//            if(flag){
//                return success("修改成功！");
//            }
//            else{
//                return error();
//            }
//
//        }
//        else{
//            return error("修改失败");
//        }
//    }
//
//
//    /**
//     * 搜索任务书状态,计时任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/searchT")
//    public AjaxResult searchT(@ModelAttribute TProject tProject) {
//        QueryWrapper<TProject> Setproject = new QueryWrapper<TProject>();
//        System.out.println("==============="+tProject.getProjectName()+"=============");
//        if (tProject.getProjectName() !=null) {
//            System.out.println("===============start=============");
//            Setproject = Setproject.eq("project_name", tProject.getProjectName());
//            tProject = itProjectService.getOne(Setproject);
//            return success(tProject);
//        }
//        else{
//            return error("没找到！");
//        }
//    }
//
//    /**
//     * 搜索任务书,计件任务书
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/searchAlcproject")
//    public AjaxResult searchAlcproject(@ModelAttribute TCproject tCProject) {
//        QueryWrapper<TCproject> Setproject = new QueryWrapper<TCproject>();
//        System.out.println("==============="+tCProject.getProjectName()+"=============");
//        if (tCProject.getProjectName() !=null) {
//            System.out.println("===============start============="+tCProject.getProjectName());
//            Setproject = Setproject.eq("project_name", tCProject.getProjectName());
//            tCProject = itCprojectService.getOne(Setproject);
//            return success(tCProject);
//        }
//        else{
//            return error("没找到！");
//        }
//    }
//}
