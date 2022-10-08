package pro.wtao.security.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.wtao.framework.security.model.Result;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 10:05    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/delete")
    public Result<String> delete() {
        return Result.ok("delete");
    }

    @GetMapping("/save")
    public Result<String> save() {
        return Result.ok("save");
    }

    @GetMapping("/query")
    public Result<String> query() {
        return Result.ok("query");
    }
}
