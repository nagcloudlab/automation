package org.example.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class Example1 {
    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

//         Support Java 8+ Date/Time
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Pretty print
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        FundTransferRequest request = TestDataBuilder.build();
//
//        String json = mapper.writeValueAsString(request);
//        System.out.println(json);

        //-----------------------------------------------
//
        FundTransferRequest request =
                mapper.readValue(
                        new File("/Users/nag/automation/play-with-xml-json/src/main/resources/transfer-request.json"),
                        FundTransferRequest.class
                );
//
        System.out.println(request.getCustomer().getName());
        System.out.println(request.getTransfers().size());


    }
}

/*

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer {
    ...
}

@JsonProperty("from_account")
private String fromAccount;

@JsonInclude(JsonInclude.Include.NON_NULL)
private String remarks;



 */
