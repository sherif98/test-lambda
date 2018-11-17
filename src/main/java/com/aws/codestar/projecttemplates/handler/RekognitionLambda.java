package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;

public class RekognitionLambda {

    public void handle(Operation operation, Context context) {
        System.out.println("function is called");

        String streamProcessorName = "TestStreamProcessor";
        String kinesisVideoStreamArn = "arn:aws:kinesisvideo:us-east-1:577962240200:stream/test/1542394943520";
        String kinesisDataStreamArn = "arn:aws:kinesis:us-east-1:577962240200:stream/reko_result";
        String roleArn = " arn:aws:iam::577962240200:policy/RekoPolicy ";
        String collectionId = "MyCollection";
        Float matchThreshold = 50F;

        LambdaLogger logger = context.getLogger();
        logger.log("operation is" + operation.value);
        logger.log("creating stream manager");
        try {
            StreamManager sm = new StreamManager(streamProcessorName,
                    kinesisVideoStreamArn,
                    kinesisDataStreamArn,
                    roleArn,
                    collectionId,
                    matchThreshold);

            logger.log("created stream manager");

            switch (operation.value) {
                case "create":
                    sm.createStreamProcessor();
                    break;
                case "start":
                    sm.startStreamProcessor();
                    break;
                case "stop":
                    sm.stopStreamProcessor();
                    break;
                case "list":
                    sm.listStreamProcessors();
                    break;
                case "describe":
                    sm.describeStreamProcessor();
                    break;
                default:
                    throw new IllegalArgumentException("unknown operation");
            }
//            sm.createStreamProcessor();
//            sm.startStreamProcessor();
            //sm.deleteStreamProcessor();
            //sm.deleteStreamProcessor();
            //sm.stopStreamProcessor();
            //sm.listStreamProcessors();
            //sm.describeStreamProcessor();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class StreamManager {

    private String streamProcessorName;
    private String kinesisVideoStreamArn;
    private String kinesisDataStreamArn;
    private String roleArn;
    private String collectionId;
    private float matchThreshold;

    private AmazonRekognition rekognitionClient;


    public StreamManager(String spName,
                         String kvStreamArn,
                         String kdStreamArn,
                         String iamRoleArn,
                         String collId,
                         Float threshold) {
        streamProcessorName = spName;
        kinesisVideoStreamArn = kvStreamArn;
        kinesisDataStreamArn = kdStreamArn;
        roleArn = iamRoleArn;
        collectionId = collId;
        matchThreshold = threshold;
//        rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

    }

    public void createStreamProcessor() {
        //Setup input parameters
        KinesisVideoStream kinesisVideoStream = new KinesisVideoStream().withArn(kinesisVideoStreamArn);
        StreamProcessorInput streamProcessorInput =
                new StreamProcessorInput().withKinesisVideoStream(kinesisVideoStream);
        KinesisDataStream kinesisDataStream = new KinesisDataStream().withArn(kinesisDataStreamArn);
        StreamProcessorOutput streamProcessorOutput =
                new StreamProcessorOutput().withKinesisDataStream(kinesisDataStream);
        createCollection();
        FaceSearchSettings faceSearchSettings =
                new FaceSearchSettings().withCollectionId(collectionId).withFaceMatchThreshold(matchThreshold);
        StreamProcessorSettings streamProcessorSettings =
                new StreamProcessorSettings().withFaceSearch(faceSearchSettings);

        //Create the stream processor
        CreateStreamProcessorResult createStreamProcessorResult = rekognitionClient.createStreamProcessor(
                new CreateStreamProcessorRequest().withInput(streamProcessorInput).withOutput(streamProcessorOutput)
                        .withSettings(streamProcessorSettings).withRoleArn(roleArn).withName(streamProcessorName));

        //Display result
        System.out.println("Stream Processor " + streamProcessorName + " created.");
        System.out.println("StreamProcessorArn - " + createStreamProcessorResult.getStreamProcessorArn());
    }

    private void createCollection() {


        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();


        System.out.println("Creating collection: " +
                collectionId);

        CreateCollectionRequest request = new CreateCollectionRequest()
                .withCollectionId(collectionId);

        CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request);
        System.out.println("CollectionArn : " +
                createCollectionResult.getCollectionArn());
        System.out.println("Status code : " +
                createCollectionResult.getStatusCode().toString());

    }

    public void startStreamProcessor() {
        StartStreamProcessorResult startStreamProcessorResult =
                rekognitionClient.startStreamProcessor(new StartStreamProcessorRequest().withName(streamProcessorName));
        System.out.println("Stream Processor " + streamProcessorName + " started.");
    }

    public void stopStreamProcessor() {
        StopStreamProcessorResult stopStreamProcessorResult =
                rekognitionClient.stopStreamProcessor(new StopStreamProcessorRequest().withName(streamProcessorName));
        System.out.println("Stream Processor " + streamProcessorName + " stopped.");
    }

    public void deleteStreamProcessor() {
        DeleteStreamProcessorResult deleteStreamProcessorResult = rekognitionClient
                .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(streamProcessorName));
        System.out.println("Stream Processor " + streamProcessorName + " deleted.");
    }

    public void describeStreamProcessor() {
        DescribeStreamProcessorResult describeStreamProcessorResult = rekognitionClient
                .describeStreamProcessor(new DescribeStreamProcessorRequest().withName(streamProcessorName));

        //Display various stream processor attributes.
        System.out.println("Arn - " + describeStreamProcessorResult.getStreamProcessorArn());
        System.out.println("Input kinesisVideo stream - "
                + describeStreamProcessorResult.getInput().getKinesisVideoStream().getArn());
        System.out.println("Output kinesisData stream - "
                + describeStreamProcessorResult.getOutput().getKinesisDataStream().getArn());
        System.out.println("RoleArn - " + describeStreamProcessorResult.getRoleArn());
        System.out.println(
                "CollectionId - " + describeStreamProcessorResult.getSettings().getFaceSearch().getCollectionId());
        System.out.println("Status - " + describeStreamProcessorResult.getStatus());
        System.out.println("Status message - " + describeStreamProcessorResult.getStatusMessage());
        System.out.println("Creation timestamp - " + describeStreamProcessorResult.getCreationTimestamp());
        System.out.println("Last update timestamp - " + describeStreamProcessorResult.getLastUpdateTimestamp());
    }

    public void listStreamProcessors() {
        ListStreamProcessorsResult listStreamProcessorsResult =
                rekognitionClient.listStreamProcessors(new ListStreamProcessorsRequest().withMaxResults(100));

        //List all stream processors (and state) returned from Rekognition
        for (StreamProcessor streamProcessor : listStreamProcessorsResult.getStreamProcessors()) {
            System.out.println("StreamProcessor name - " + streamProcessor.getName());
            System.out.println("Status - " + streamProcessor.getStatus());
        }
    }
}

class Operation {
    String value;
}