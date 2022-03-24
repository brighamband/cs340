package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.document.Item;

public class DynamoUtils {
    public static String getStrAttrVal(Item item, String attrName) {
        return (String) item.get(attrName);
    }
}
