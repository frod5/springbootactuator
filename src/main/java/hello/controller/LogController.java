package hello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogController {

    //다음과 같은 패턴을 사용해서 특정 로거 이름을 기준으로 조회할 수 있다.
    //http://localhost:8080/actuator/loggers/{로거이름}
    //http://localhost:8080/actuator/loggers/hello.controller

    //POST로 http://localhost:8080/actuator/loggers/{로거이름}
    //Body에 configuredLevel 을 변경하고 요청하면, 서버 재시작 없이 로그 레벨을 실사간으로 바꿀 수 있다.
    //단, 서버 재시작시 지정된 로그레벨로 돌아감.

    @GetMapping("/log")
    public String log() {
        log.trace("trace log");
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
        return "ok";
    }
}
