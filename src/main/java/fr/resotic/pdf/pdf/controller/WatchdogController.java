package fr.resotic.pdf.pdf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
public class WatchdogController {

    @GetMapping("/cron/watchdog")
    @ResponseBody
    public String watchdog() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.FRENCH);
        StringBuilder sb = new StringBuilder("{\"result\": \"SYSDATE=" + format.format(new Date()));

        try {
            sb.append(" TEST=OK\"}");
            return sb.toString();
        } catch (Exception e) {

        }
        sb.append(" TEST=ERREUR\"}");
        return sb.toString();
    }
}
