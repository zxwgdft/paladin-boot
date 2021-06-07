package com.paladin.data.controller;

import com.paladin.common.core.ControllerSupport;
import com.paladin.common.service.cache.SysVisitCacheService;
import com.paladin.data.controller.dto.GenerateTableOptionDTO;
import com.paladin.data.database.DataBaseSource;
import com.paladin.data.database.model.DataBase;
import com.paladin.data.database.model.Table;
import com.paladin.data.generate.GenerateColumnOption;
import com.paladin.data.generate.GenerateTableOption;
import com.paladin.data.generate.build.BuilderType;
import com.paladin.data.model.DBConnection;
import com.paladin.data.model.build.DbBuildColumn;
import com.paladin.data.model.build.DbBuildTable;
import com.paladin.data.service.DBConnectionService;
import com.paladin.data.service.GenerateService;
import com.paladin.data.service.build.DbBuildTableService;
import com.paladin.data.service.dto.DBConnectionDTO;
import com.paladin.data.service.dto.DBConnectionQueryDTO;
import com.paladin.data.service.vo.BuildColumnVO;
import com.paladin.framework.api.R;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.spring.DevelopCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/data/connection")
@Conditional(DevelopCondition.class)
public class DBConnectionController extends ControllerSupport {

    @Autowired
    private DBConnectionService connectionService;

    @Autowired
    private GenerateService generateService;

    @Autowired
    private DbBuildTableService buildTableService;

    @GetMapping(value = "/index")
    public String index(HttpServletRequest request) {
        return "/data/connection/db_connection_index";
    }

    @RequestMapping("/find/page")
    @ResponseBody
    public PageResult<DBConnection> findPage(DBConnectionQueryDTO query) {
        return connectionService.findPage(query);
    }

    @GetMapping("/get")
    @ResponseBody
    public DBConnection getDetail(@RequestParam String id) {
        return connectionService.get(id);
    }

    @GetMapping("/add")
    public String addInput() {
        return "/data/connection/db_connection_add";
    }

    @GetMapping("/detail")
    public String detailInput(@RequestParam String id, Model model) {
        model.addAttribute("id", id);
        return "/data/connection/db_connection_detail";
    }

    @PostMapping("/save")
    @ResponseBody
    public DBConnection save(@Valid DBConnectionDTO dbConnectionDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        DBConnection model = beanCopy(dbConnectionDTO, new DBConnection());
        String id = model.getName();
        connectionService.save(model);
        return connectionService.get(id);
    }

    @PostMapping("/update")
    @ResponseBody
    public Object update(@Valid DBConnectionDTO dbConnectionDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        String id = dbConnectionDTO.getName();
        DBConnection model = beanCopy(dbConnectionDTO, connectionService.getWhole(id));
        connectionService.updateWhole(model);
        return connectionService.get(id);
    }

    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam String id) {
        connectionService.deleteById(id);
        return R.success();
    }

    @RequestMapping("/connect")
    public String connect(DBConnection connection, Model model) {
        connectionService.connect(connection);
        try {
            return "redirect:/data/connection/db/index?dbName=" + URLEncoder.encode(connection.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("非法数据库名称：" + connection.getName());
        }
    }

    @RequestMapping("/db/index")
    public String dbIndex(@RequestParam String dbName, Model model) {
        model.addAttribute("dbName", dbName);
        return "/data/connection/db_index";
    }

    @RequestMapping("/db/table")
    @ResponseBody
    public String[] tableList(@RequestParam String dbName) {
        Table[] tables = connectionService.getDBTables(dbName);
        String[] tableNames = new String[tables.length];
        for (int i = 0; i < tables.length; i++) {
            tableNames[i] = tables[i].getName();
        }
        return tableNames;
    }

    @RequestMapping("/db/column")
    @ResponseBody
    public List<BuildColumnVO> tableList(@RequestParam String dbName, @RequestParam String tableName) {
        return generateService.getBuildColumn(dbName, tableName);
    }

    @RequestMapping("/db/build/input")
    public String toBuild(@RequestParam String dbName, @RequestParam String tableName, HttpServletRequest request, Model model) {

        model.addAttribute("dbName", dbName);
        model.addAttribute("tableName", tableName);

        DbBuildTable buildTable = buildTableService.getDbBuildColumn(dbName, tableName);

        String title = tableName;
        if (buildTable != null) {
            title = buildTable.getTableTitle();
        }

        model.addAttribute("tableTitle", title);
        model.addAttribute("projectPath", visitCacheService.getCache(request, CACHE_PROJECT_PATH));

        return "/data/connection/build";
    }

    @Autowired
    private SysVisitCacheService visitCacheService;

    private final static String CACHE_PROJECT_PATH = "data_project_path";

    @RequestMapping("/db/build/boot")
    @ResponseBody
    public R buildBoot(HttpServletRequest request, @RequestBody GenerateTableOptionDTO option) {

        String dbName = option.getDbName();
        String tableName = option.getTableName();

        DataBaseSource dataBaseSource = connectionService.getDataBaseSource(dbName);

        if (dataBaseSource == null) {
            throw new BusinessException("不存在数据库：" + dbName);
        }

        DataBase dataBase = dataBaseSource.getDataBase(false);
        Table table = dataBase.getChild(tableName);

        if (table == null) {
            throw new BusinessException("不存在表：" + tableName);
        }

        GenerateTableOption tableOption = new GenerateTableOption(table, dataBaseSource.getDataBaseConfig().getType());

        beanCopy(option, tableOption);

        List<DbBuildColumn> columnBuildOptions = option.getColumnBuildOptions();
        for (DbBuildColumn columnBuildOption : columnBuildOptions) {
            GenerateColumnOption columnOption = tableOption.getColumnOption(columnBuildOption.getColumnName());
            columnOption.setBuildColumnOption(columnBuildOption);
        }

        String projectPath = option.getProjectPath();

        if (projectPath == null || projectPath.length() == 0) {
            throw new BusinessException("项目路径不能为空");
        }

        generateService.buildProjectFile(tableOption, BuilderType.MODEL, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.MODEL_VO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.MODEL_DTO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.QUERY_DTO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.EXPORT_QUERY_DTO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.MAPPER, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.SERVICE, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.CONTROLLER, projectPath);

        generateService.buildProjectFile(tableOption, BuilderType.SQLMAPPER, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.JAVASCRIPT, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_INDEX, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_ADD, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_DETAIL, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_EDIT, projectPath);

        visitCacheService.putCache(request, CACHE_PROJECT_PATH, projectPath);

        generateService.saveBuildOption(tableOption, dbName);

        return R.success();
    }

    @RequestMapping("/db/build/file")
    @ResponseBody
    public R buildFile(HttpServletRequest request, HttpServletResponse response, @RequestBody GenerateTableOptionDTO option) {

        String dbName = option.getDbName();
        String tableName = option.getTableName();

        DataBaseSource dataBaseSource = connectionService.getDataBaseSource(dbName);

        if (dataBaseSource == null) {
            throw new BusinessException("不存在数据库：" + dbName);
        }

        DataBase dataBase = dataBaseSource.getDataBase(false);
        Table table = dataBase.getChild(tableName);

        if (table == null) {
            throw new BusinessException("不存在表：" + tableName);
        }

        GenerateTableOption tableOption = new GenerateTableOption(table, dataBaseSource.getDataBaseConfig().getType());

        beanCopy(option, tableOption);

        List<DbBuildColumn> columnBuildOptions = option.getColumnBuildOptions();
        for (DbBuildColumn columnBuildOption : columnBuildOptions) {
            GenerateColumnOption columnOption = tableOption.getColumnOption(columnBuildOption.getColumnName());
            columnOption.setBuildColumnOption(columnBuildOption);
        }

        String projectPath = option.getFilePath();

        if (projectPath == null || projectPath.length() == 0) {
            throw new BusinessException("项目路径不能为空");
        }

        generateService.buildProjectFile(tableOption, BuilderType.MODEL, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.MODEL_VO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.MODEL_DTO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.QUERY_DTO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.EXPORT_QUERY_DTO, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.MAPPER, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.SERVICE, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.CONTROLLER, projectPath);

        generateService.buildProjectFile(tableOption, BuilderType.SQLMAPPER, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.JAVASCRIPT, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_INDEX, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_ADD, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_DETAIL, projectPath);
        generateService.buildProjectFile(tableOption, BuilderType.PAGE_EDIT, projectPath);

        generateService.saveBuildOption(tableOption, dbName);

        return R.success();
    }

}
