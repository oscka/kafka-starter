package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DemoController {
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/message")
    public String sendMassage(@RequestBody KafkaMessageVO kafkaMessageVO) {
        this.kafkaProducerService.sendMessageToKafka(kafkaMessageVO);
        return "success";
    }
	@GetMapping("/messageTest")
	public String sendMassageTest() {
		KafkaMessageVO kafkaMessageVO = new KafkaMessageVO("name","messageTest");
		this.kafkaProducerService.sendMessageToKafka(kafkaMessageVO);
		return "success";
	}

    @GetMapping("/hello")
    public String getHello(String name) {
        return name + " Hello";
    }

    @GetMapping("/simple")
	public List<Simple> listSimple() {
		List<Simple> list = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			list.add(new Simple(i + 1, "test-" + i, "contents-" + i));
			log.info("for " + i);
		}
		log.info(list.toString());
		return list;
	}

	@GetMapping("/version")
	public String version() {
		log.info("version 1.0");
		return "=====  version 1.0";

	}
}
