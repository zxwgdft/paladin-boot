package com.paladin.common.controller.syst;

import com.paladin.common.core.container.ConstantsContainer;
import com.paladin.common.service.syst.SysConstantService;
import com.paladin.common.service.syst.dto.SysConstantQuery;
import com.paladin.framework.common.R;
import com.paladin.framework.web.ControllerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/common/sys/constant")
public class SysConstantController extends ControllerSupport {

    @Autowired
    private SysConstantService sysConstantService;


    @RequestMapping("/find")
    @ResponseBody
    public Object find(SysConstantQuery query) {
        return R.success(sysConstantService.searchAll(query));
    }

    @RequestMapping("/find/page")
    @ResponseBody
    public Object findPage(SysConstantQuery query) {
        return R.success(sysConstantService.searchPage(query));
    }
    
    
    @RequestMapping("/find/all/key")
    @ResponseBody
    public Object findKeys() {
        return R.success(ConstantsContainer.getAllKey());
    }

}