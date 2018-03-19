package com.itcast.enrol.management.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itcast.enrol.common.annotation.CheckIntValues;
import com.itcast.enrol.common.annotation.Session;
import com.itcast.enrol.common.base.BaseBody;
import com.itcast.enrol.common.base.BasePage;
import com.itcast.enrol.common.base.ManageBaseBody;
import com.itcast.enrol.common.base.ReturnStatus;
import com.itcast.enrol.common.dao.banner.BannerMapper;
import com.itcast.enrol.common.entity.Banner;
import com.itcast.enrol.common.utils.FileUtils;
import com.itcast.enrol.management.service.ManagerBannerService;
import com.itcast.enrol.management.vo.UserEx;


/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/10/21.
 */
@RestController
@RequestMapping("/managenment/banner")
@Validated
public class ManagerBannerController {

    @Autowired
    private ManagerBannerService managerBannerService;

    @Autowired
    private BannerMapper bannerDao;

    @Value("${file.goodsImg.basePath}")
    private String goodsImgPath;

    @Value("${file.goodsImg.goodsUri}")
    private String goodsImgUri;


    /**
     * 新增banner
     *
     * @param banner
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/mergeBanner")
    public BaseBody<Long> mergeBanner(@Session UserEx userEx, HttpServletRequest request, @Valid Banner banner, BindingResult bindingResult, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        BaseBody<Long> bannerBaseBody = new BaseBody<>();

        if (bindingResult.hasErrors()) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setCode(1006);
            bannerBaseBody.setMessage(bindingResult.getFieldError().getDefaultMessage());
            return bannerBaseBody;
        }

        if (null == banner) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setMessage("参数为null");
            bannerBaseBody.setCode(500);
            return bannerBaseBody;
        }
        if (banner.getId() == null) {
            if (null == file || file.isEmpty()) {
                bannerBaseBody.setSuccess(ReturnStatus.FAILD);
                bannerBaseBody.setMessage("请上传banner图!");
                bannerBaseBody.setCode(500);
                return bannerBaseBody;
            }
        }
        if (!(file == null || null == file.getOriginalFilename())) {
            //创建本地文件
            if (null == banner.getId()) {
                String prefx = "banner_";
                String fileNameFrom = file.getOriginalFilename();
                String fileImguffix = fileNameFrom.substring(fileNameFrom.lastIndexOf("."));
                String fileName = String.valueOf(System.currentTimeMillis());
                FileUtils.createFile(goodsImgPath, prefx + fileName + fileImguffix, file.getBytes());
                banner.setImageUrl(goodsImgUri + prefx + fileName + fileImguffix);
            } else {
                String version = String.valueOf(System.currentTimeMillis());
                Banner banerForDb = managerBannerService.selectByPrimaryKey(banner.getId());
                FileUtils.createFile(goodsImgPath, banerForDb.getImageUrl().substring(banerForDb.getImageUrl().lastIndexOf("/") + 1), file.getBytes());
            }
        }
        byte status = 0;
        banner.setStatus(status);
        byte isCover = 0;
        banner.setIsCover(isCover);

        if (banner.getId() != null) {
            banner.setEditor(userEx.getName());
        } else {
            banner.setCreator(userEx.getName());
        }
        managerBannerService.mergeBanner(banner);
        bannerBaseBody.setCode(200);
        bannerBaseBody.setSuccess(ReturnStatus.SUCCESS);
        bannerBaseBody.setMessage("保存成功！");
        bannerBaseBody.setContent(banner.getId());
        return bannerBaseBody;
    }

    /**
     * 发布或取消发布 banner
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/upOrDownBanner")
    public BaseBody<Banner> upOrDownBanner(@RequestParam(defaultValue = "0") Integer id, @CheckIntValues(values = {0, 1}, ableNull = false, message = "不支持的参数值") Integer status) {

        BaseBody<Banner> bannerBaseBody = new BaseBody<Banner>();

        if (status.intValue() == 1) {
            Integer count = bannerDao.getCountForEnable();
            if (count.intValue() >= 5) {
                bannerBaseBody.setSuccess(ReturnStatus.FAILD);
                bannerBaseBody.setMessage("最多只能发布5个banner!");
                bannerBaseBody.setCode(500);
                return bannerBaseBody;
            }
        }
        int effRow = managerBannerService.upOrDownBanner(id, status.intValue());
        if (effRow != 1) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setMessage("修改失败!");
            bannerBaseBody.setCode(500);
            return bannerBaseBody;
        }
        bannerBaseBody.setMessage("修改成功!");
        return bannerBaseBody;
    }


    /**
     * 删除banner
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delBanner")
    public BaseBody<Banner> delBanner(@RequestParam(defaultValue = "0") Long id) {

        BaseBody<Banner> bannerBaseBody = new BaseBody<Banner>();

        if (id == 0) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setCode(500);
            bannerBaseBody.setMessage("参数值不正确!");
            return bannerBaseBody;
        }
        Banner conditions = new Banner();
        conditions.setId(id);
        Banner banner = managerBannerService.selectOne(conditions);
        if (null == banner) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setCode(500);
            bannerBaseBody.setMessage("banner 不存在!");
            return bannerBaseBody;
        }
        if (banner.getStatus().byteValue() == 1) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setCode(500);
            bannerBaseBody.setMessage("不可以删除发布状态的banner!");
            return bannerBaseBody;
        }

        managerBannerService.delByPrimaryKey(id);

        bannerBaseBody.setMessage("删除成功!");
        return bannerBaseBody;
    }

    /**
     * banner info 预览
     *
     * @param id
     * @return
     */
    @GetMapping("/getBanner")
    public BaseBody<Banner> getBanner(Long id) {

        BaseBody<Banner> bannerBaseBody = new BaseBody<Banner>();
        if (id == null) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setCode(500);
            bannerBaseBody.setMessage("参数值不正确!");
            return bannerBaseBody;
        }
        Banner banner = managerBannerService.selectByPrimaryKey(id);
        if (null == banner) {
            bannerBaseBody.setSuccess(ReturnStatus.FAILD);
            bannerBaseBody.setCode(500);
            bannerBaseBody.setMessage("查询失败!");
            return bannerBaseBody;
        }
        bannerBaseBody.setContent(banner);
        bannerBaseBody.setMessage("查询成功!");

        return bannerBaseBody;
    }

    /**
     * 条件查询banner
     *
     * @param banner
     * @return
     */
    @GetMapping("/queryBanners")
    public ManageBaseBody<Banner> queryBanners(Banner banner, @RequestParam(defaultValue = "1", name = "page") int startNum, @RequestParam(defaultValue = "10", name = "limit") int pageSize) {

        ManageBaseBody<Banner> baseBody = new ManageBaseBody();
        if (null == banner) {
            banner = new Banner();
        }

        BasePage<Banner> bannerBasePage = managerBannerService.queryBannersToPage(banner, startNum, pageSize);
        baseBody.setMsg("查询成功!");
        baseBody.setCode(0);
        baseBody.setCount(bannerBasePage.getCount());
        baseBody.setData(bannerBasePage.getPageData());
        return baseBody;
    }


}
