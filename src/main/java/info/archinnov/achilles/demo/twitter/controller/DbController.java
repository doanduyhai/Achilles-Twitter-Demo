package info.archinnov.achilles.demo.twitter.controller;

import info.archinnov.achilles.demo.twitter.service.DbService;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/db", produces = "application/json")
public class DbController {

    @Inject
    private DbService dbService;

    @RequestMapping(value = "/reset", method = RequestMethod.PUT)
    @ResponseBody
    public void resetDb() {
        dbService.resetDb();
    }
}
