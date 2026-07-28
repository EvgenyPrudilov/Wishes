package ru.cohenrol.authserver.domain.service;

//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.cohenrol.authserver.broker.logging.Log;
import ru.cohenrol.authserver.broker.logging.events.LogEvent;

//@Service
public class KafkaLogProducerService {

//    private final KafkaTemplate<String, Log> kafkaTemplate;
//    private final KafkaTopic kafkaTopic = KafkaTopic.Logs;
//
//    public KafkaLogProducerService(KafkaTemplate<String, Log> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void send(LogEvent log) {
//            kafkaTemplate.send(kafkaTopic.toString(), log);
//
//    }
}