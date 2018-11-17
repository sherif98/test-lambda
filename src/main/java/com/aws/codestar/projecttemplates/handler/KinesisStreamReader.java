package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class KinesisStreamReader {

    public void handle(KinesisEvent event, Context context) {
        context.getLogger().log("function is called");
        event.getRecords().forEach(r -> {
            ByteBuffer bytes = r.getKinesis().getData();
            String data = StandardCharsets.UTF_8.decode(bytes).toString();
            context.getLogger().log("data is" + data);
        });
    }
}
