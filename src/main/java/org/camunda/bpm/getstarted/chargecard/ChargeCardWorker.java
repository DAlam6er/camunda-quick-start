package org.camunda.bpm.getstarted.chargecard;

import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;

public class ChargeCardWorker {
    private final static Logger log = LoggerFactory.getLogger(ChargeCardWorker.class);

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000)
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("charge-card")
                .lockDuration(1000) // the default lock duration is 20 seconds
                .handler(((externalTask, externalTaskService) -> {
                    // put your business logic here

                    // get a process variable
                    String item = externalTask.getVariable("item");
                    Integer amount = externalTask.getVariable("amount");

                    log.info("Charging credit card with an amount of '{}' € for the item '{}'...", amount, item);
                    try {
                        Desktop.getDesktop().browse(new URI(
                                "https://docs.camunda.org/get-started/quick-start/complete"
                        ));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    // Complete the task
                    externalTaskService.complete(externalTask);
                }))
                .open();
    }
}
