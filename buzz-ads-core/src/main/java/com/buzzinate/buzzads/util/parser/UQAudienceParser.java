package com.buzzinate.buzzads.util.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.*;

import java.io.*;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-5-29
 * Time: 下午12:21
 * UQ audience的json解析类,用于读出单条记录的字符串
 */
public class UQAudienceParser implements Iterator<String> {
    private static final String TAG = "UQAudienceParser";
    private static Log LOG = LogFactory.getLog(UQAudienceParser.class);
    private InputStream source;
    private JsonFactory jsonFactory = new JsonFactory();
    private JsonParser jsonParser;
    private boolean hasNext = false;
    private JsonToken currentToken;
    private boolean isArray = false;

    public UQAudienceParser(InputStream source) {
        this.source = source;
        init();
    }

//    public static void main(String[] args) {
//        try {
//            UQAudienceParser parser = new UQAudienceParser(new FileInputStream("C:\\Users\\chris\\test_write.json"));
//            int count = 1;
//            long start = System.currentTimeMillis();
//            while (parser.hasNext()) {
//                String data = parser.next();
//                System.out.println(count + "::" + data);
//                count++;
//            }
//            long end = System.currentTimeMillis();
//            System.out.println("avg is " + ((float) (end - start) / count) + " ms");
//            System.out.println("total time is " + (end - start) / 1000 + " s");
//            System.out.println("count is " + (count -1));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 初始化解析器并移动到文件头
     *
     * @throws IOException
     */
    private void init() {
        try {
            jsonFactory = new JsonFactory();
            jsonParser = jsonFactory.createJsonParser(source);
            currentToken = jsonParser.nextToken();
            if (currentToken == JsonToken.START_ARRAY) {
                hasNext = true;
                isArray = true;
            } else if (currentToken == JsonToken.START_OBJECT) {
                hasNext = true;
                isArray = false;
            }
        } catch (IOException e) {
            LOG.error(TAG + "::parser json error!", e);
            hasNext = false;
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public String next() {
        if (hasNext == false) {
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(byteArrayOutputStream, JsonEncoding.UTF8);

            if (isArray) {
                currentToken = jsonParser.nextToken();
                if (currentToken == JsonToken.END_ARRAY) {
                    if (!jsonParser.isClosed()) {
                        jsonParser.close();
                    }
                    hasNext = false;
                } else if (currentToken == JsonToken.START_OBJECT) {
                    writeObject(jsonGenerator, jsonParser);
                } else {
                    while (currentToken != JsonToken.START_OBJECT) {
                        currentToken = jsonParser.nextToken();
                    }
                    writeObject(jsonGenerator, jsonParser);

                }
            } else {
                if (currentToken == JsonToken.END_OBJECT) {
                    if (!jsonParser.isClosed()) {
                        jsonParser.close();
                    }
                    hasNext = false;
                } else {
                    writeObject(jsonGenerator, jsonParser);

                }
            }
            if (!jsonGenerator.isClosed()) {
                jsonGenerator.close();
            }
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            hasNext = false;
            LOG.error(TAG + "::build json string error!", e);
            return "";
        }
    }

    /**
     * 循环并将对象的内容输出到generator
     *
     * @param generator
     * @param parser
     */
    private void writeObject(JsonGenerator generator, JsonParser parser) {
        try {
            generator.writeStartObject();
            currentToken = parser.nextToken();
            while (currentToken != JsonToken.END_OBJECT) {
                if (currentToken == JsonToken.FIELD_NAME) {
                    generator.writeFieldName(parser.getCurrentName());
                } else if (currentToken == JsonToken.START_OBJECT) {
                    writeObject(generator, parser);
                } else if (currentToken == JsonToken.START_ARRAY) {
                    generator.writeStartArray();
                } else if (currentToken == JsonToken.END_ARRAY) {
                    generator.writeEndArray();
                } else if (currentToken == JsonToken.VALUE_STRING) {
                    generator.writeString(parser.getText());
                } else if (currentToken == JsonToken.VALUE_NUMBER_INT) {
                    generator.writeNumber(parser.getIntValue());
                } else if (currentToken == JsonToken.VALUE_NUMBER_FLOAT) {
                    generator.writeNumber(parser.getFloatValue());
                } else if (currentToken == JsonToken.VALUE_TRUE) {
                    generator.writeBoolean(true);
                } else if (currentToken == JsonToken.VALUE_FALSE) {
                    generator.writeBoolean(false);
                } else if (currentToken == JsonToken.VALUE_NULL) {
                    generator.writeNull();
                }
                currentToken = parser.nextToken();
            }
            generator.writeEndObject();
        } catch (IOException e) {
            LOG.error(TAG + "::write object to jsonGenerator error!", e);
        }
    }

    @Override
    public void remove() {
        //TODO 未实现
    }
}
