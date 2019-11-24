package learnaws.dynamodB;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.UUID;

/**
 * @author kelvin piroddi
 * Sample Lambda Function that writes to a dynamoDb table
 */
public class WriteToDynamoLambda implements RequestHandler<SaveItemRequest, SaveItemResponse>
{
    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "ToDoList";
    private Regions REGION = Regions.US_WEST_2;

    public SaveItemResponse handleRequest(
            SaveItemRequest saveItemRequest, Context context) {

        this.initDynamoDbClient();

        persistData(saveItemRequest);

        SaveItemResponse saveItemResponse = new SaveItemResponse();
        saveItemResponse.setCompleted("Created");
        return saveItemResponse;
    }

    private PutItemOutcome persistData(SaveItemRequest saveItemRequest)
            throws ConditionalCheckFailedException {
        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                .putItem(
                        new PutItemSpec().withItem(new Item()
                                .withPrimaryKey("id", UUID.randomUUID().toString())
                                .withString("item", saveItemRequest.getItem())
                                .withString("completed", "true")));

    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}
